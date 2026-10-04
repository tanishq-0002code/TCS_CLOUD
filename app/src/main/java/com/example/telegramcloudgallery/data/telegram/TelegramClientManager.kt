package com.example.telegramcloudgallery.data.telegram

import android.os.Build
import android.util.Log
import com.example.telegramcloudgallery.BuildConfig
import com.example.telegramcloudgallery.core.Resource
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import org.drinkless.tdlib.Client
import org.drinkless.tdlib.TdApi
import java.util.Locale
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Thin, lifecycle-aware wrapper around the pre-compiled TDLib AAR.
 *
 * ### Why this class exists
 * TDLib is a *stateful* singleton: one native client per process, requests are
 * asynchronous and answers arrive as `TdApi.Update` objects. Rather than
 * sprinkling `Client.create(...)` and raw `ResultHandler` lambdas across the
 * app, every TDLib touchpoint goes through this object:
 *
 *  * **sending** a request becomes a cancellable `suspend` function,
 *  * **receiving** updates becomes a hot [Flow] that Compose can collect,
 *  * **thread hopping** (TDLib calls back on its own native thread) happens
 *    exactly once, here.
 *
 * ### Activity notifications
 * Some TDLib Java bindings require the host app to forward lifecycle events via
 * `client.setActivity(TdApi.TcActivity)`. The prebuilt artifact used here
 * (`org.drinkless.tdlib.Client`) does **not** expose that method — it manages
 * the activity state internally — so nothing needs to be forwarded from
 * `ProcessLifecycleOwner` to TDLib.
 */
object TelegramClientManager {

    private const val TAG = "TelegramClientManager"

    private const val TDLIB_VERSION = "1.8.56"

    /**
     * TDLib priority values. Higher == more important.
     *
     * Playback uses [PRIORITY_STREAM_PLAYBACK] so that media starts playing
     * after a small prefix has arrived, instead of waiting for the whole file,
     * while bulk pre-caching sits at [PRIORITY_PRECACHE].
     */
    const val PRIORITY_STREAM_PLAYBACK = 1
    const val PRIORITY_PRECACHE = 0
    const val PRIORITY_DOWNLOAD = 4
    const val PRIORITY_UPLOAD = 1

    private val isInitialized = AtomicBoolean(false)

    @Volatile
    private var client: Client? = null

    private val _updates = MutableSharedFlow<TdApi.Update>(
        replay = 0,
        extraBufferCapacity = 512,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    /** Every `TdApi.Update` produced by TDLib — replay-free and hot. */
    val updates: Flow<TdApi.Update> = _updates.asSharedFlow()

    private val _fatalErrors = MutableSharedFlow<Throwable>(
        replay = 0,
        extraBufferCapacity = 8,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    /** Unrecoverable TDLib problems (native crashes, DC migration, …). */
    val fatalErrors: Flow<Throwable> = _fatalErrors.asSharedFlow()

    /** True once the native client exists. Safe to call from any thread. */
    val isClientReady: Boolean get() = client != null

    // --------------------------------------------------------------- lifecycle

    /**
     * Creates the native client exactly once per process.
     * Call from `Application.onCreate`; subsequent calls are no-ops.
     */
    fun initialize(onFatalError: ((Throwable) -> Unit)? = null) {
        if (!isInitialized.compareAndSet(false, true)) return

        // Route TDLib's own verbose logging into logcat while developing.
        Client.setLogMessageHandler(if (BuildConfig.DEBUG) 2 else 0) { _, message ->
            Log.d(TAG, message)
        }

        client = Client.create(
            // -- 1st handler: incoming UPDATES ------------------------------------
            { result ->
                if (result is TdApi.Update) {
                    if (!_updates.tryEmit(result)) {
                        Log.w(TAG, "Update buffer full, dropping ${result.javaClass.simpleName}")
                    }
                } else {
                    Log.d(TAG, "Orphan result: $result")
                }
            },
            // -- 2nd handler: exception raised while handling an update ----------
            { throwable -> Log.e(TAG, "Update handler failure", throwable) },
            // -- 3rd handler: every other exception, incl. native crashes --------
            { throwable ->
                Log.e(TAG, "TDLib client failure", throwable)
                _fatalErrors.tryEmit(throwable)
                onFatalError?.invoke(throwable)
            }
        )

        Log.i(TAG, "TDLib client created (schema $TDLIB_VERSION)")
    }

    /** Drops the reference to the native client (TDLib itself is process-scoped). */
    fun release() {
        client = null
        isInitialized.set(false)
    }

    // ------------------------------------------------------------ request/reply

    /**
     * Sends a TDLib request and suspends until its answer arrives.
     *
     * @throws TdlibException when the call fails at the JNI level.
     * @throws IllegalStateException when [initialize] has not run yet.
     */
    suspend fun <T : TdApi.Object> send(function: TdApi.Function<T>): T {
        val activeClient = requireClient()
        return suspendCancellableCoroutine { continuation ->
            activeClient.send(
                function,
                { result ->
                    if (continuation.isActive) {
                        @Suppress("UNCHECKED_CAST")
                        continuation.resume(result as T)
                    }
                },
                { throwable ->
                    if (continuation.isActive) {
                        continuation.resumeWithException(
                            TdlibException("send(${function.javaClass.simpleName}) failed", throwable)
                        )
                    }
                }
            )
            // TDLib cannot cancel in-flight requests; the late answer is dropped
            // because the continuation is no longer active.
            continuation.invokeOnCancellation {
                Log.d(TAG, "cancelled: ${function.javaClass.simpleName}")
            }
        }
    }

    /** Same as [send] but turns failures into [Resource.Failure]. */
    suspend fun <T : TdApi.Object> sendCatching(function: TdApi.Function<T>): Resource<T> = try {
        Resource.Success(send(function))
    } catch (ce: CancellationException) {
        throw ce
    } catch (t: Throwable) {
        val tdError = t as? TdlibException ?: null
        Resource.Failure(
            message = t.message ?: "TDLib error",
            cause = t,
            code = tdError?.tdlibError?.code ?: Resource.Failure.UNKNOWN_CODE
        )
    }

    /** Fire-and-forget for requests whose answer nobody cares about. */
    suspend fun sendIgnoringResult(function: TdApi.Function<*>) {
        val activeClient = runCatching { requireClient() }.getOrNull() ?: return
        try {
            suspendCancellableCoroutine<TdApi.Object> { continuation ->
                activeClient.send(
                    function,
                    { result -> if (continuation.isActive) continuation.resume(result) },
                    { throwable ->
                        if (continuation.isActive) continuation.resumeWithException(throwable)
                    }
                )
            }
        } catch (ce: CancellationException) {
            throw ce
        } catch (t: Throwable) {
            Log.w(TAG, "ignoring failure for ${function.javaClass.simpleName}", t)
        }
    }

    // --------------------------------------------------------------- bootstrap

    /**
     * Sends `setTdlibParameters`. TDLib keeps answering
     * `AuthorizationStateWaitTdlibParameters` until this has been supplied at
     * least once per process.
     */
    suspend fun configureParameters(
        databaseDirectory: String,
        filesDirectory: String,
        useTestDc: Boolean = false
    ) {
        Log.i(TAG, "setTdlibParameters: db=$databaseDirectory files=$filesDirectory")
        send(
            TdApi.SetTdlibParameters(
                /* useTestDc = */ useTestDc,
                /* databaseDirectory = */ databaseDirectory,
                /* filesDirectory = */ filesDirectory,
                /* databaseEncryptionKey = */ null,
                /* useFileDatabase = */ true,
                /* useChatInfoDatabase = */ true,
                /* useMessageDatabase = */ true,
                /* useSecretChats = */ false,
                /* apiId = */ BuildConfig.TDLIB_API_ID,
                /* apiHash = */ BuildConfig.TDLIB_API_HASH,
                /* systemLanguageCode = */ Locale.getDefault().language,
                /* deviceModel = */ Build.MODEL ?: "Android",
                /* systemVersion = */ Build.VERSION.RELEASE ?: "0",
                /* applicationVersion = */ BuildConfig.VERSION_NAME
            )
        )
    }

    private fun requireClient(): Client = client
        ?: throw IllegalStateException("TelegramClientManager.initialize() was never called")
}

/** Wrapper distinguishing TDLib-reported errors from unexpected JVM failures. */
class TdlibException(
    message: String,
    cause: Throwable? = null,
    /** The `TdApi.Error` payload when the failure came back as a normal answer. */
    val tdlibError: TdApi.Error? = null
) : Exception(message, cause)