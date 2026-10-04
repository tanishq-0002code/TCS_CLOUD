package com.example.telegramcloudgallery.data.telegram

import android.content.Context
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.telegramcloudgallery.core.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.drinkless.tdlib.TdApi
import java.util.Locale
import kotlin.coroutines.cancellation.CancellationException

/**
 * Manages Telegram authentication state by observing [TdApi.UpdateAuthorizationState]
 * updates emitted by [TelegramClientManager].
 *
 * The repository exposes the current high-level auth state so UI can render the
 * correct step (phone / code / 2FA / ready / loading / error). It also sends
 * the minimal set of functions required by the spec: `setAuthenticationPhoneNumber`,
 * `sendPhoneNumberCode` (via `TdApi.SetAuthenticationPhoneNumber`), `checkPhoneNumberCode`
 * (via `TdApi.CheckAuthenticationCode`), `checkAuthenticationPassword` and `logOut`.
 */
class TelegramAuthRepository(
    private val clientManager: TelegramClientManager,
    private val appScope: CoroutineScope,
    private val context: Context
) {

    private val Context.dataStore by preferencesDataStore(name = "telegram_auth_prefs")

    private val _authState = MutableStateFlow(AuthUiState())
    val authUiState: StateFlow<AuthUiState> = _authState.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _currentUser = MutableStateFlow<TdApi.User?>(null)
    val currentUser: StateFlow<TdApi.User?> = _currentUser.asStateFlow()

    private var updatesJob: Job? = null

    private var currentPhoneNumber: String? = null
    private var lastAuthCodeInfo: TdApi.AuthenticationCodeInfo? = null

    /** Starts observing TDLib authorization updates. Idempotent. */
    fun startObserving() {
        if (updatesJob != null) return
        updatesJob = appScope.launch {
            clientManager.updates
                .collect { update ->
                    when (update) {
                        is TdApi.UpdateAuthorizationState -> {
                            handleAuthorizationState(update.authorizationState)
                        }
                        else -> {
                            // Ignore unrelated updates here.
                        }
                    }
                }
        }
    }

    /** Stops observing updates (useful for tests). */
    fun stopObserving() {
        updatesJob?.cancel()
        updatesJob = null
    }

    // -------------------------------------------------------------------- actions

    /** Requests a login code for the given phone number. */
    suspend fun requestCode(phoneNumber: String): Resource<Unit> {
        val normalized = normalizePhone(phoneNumber)
        currentPhoneNumber = normalized
        _authState.update { it.copy(isLoading = true, errorMessage = null) }
        val result = clientManager.sendCatching(
            TdApi.SetAuthenticationPhoneNumber(
                normalized,
                TdApi.PhoneNumberAuthenticationSettings(
                    /* allowFlashCall = */ false,
                    /* allowMissedCall = */ false,
                    /* allowSmsRetrieverApi = */ false,
                    /* authenticationTokens = */ null
                )
            )
        )
        _authState.update { it.copy(isLoading = false) }
        return when (result) {
            is Resource.Success -> Resource.Success(Unit)
            is Resource.Failure -> {
                _authState.update { s -> s.copy(errorMessage = result.message) }
                result
            }
            Resource.Loading -> Resource.Loading
        }
    }

    /** Resends the last login code (if [lastAuthCodeInfo] is available). */
    suspend fun resendCode(): Resource<Unit> {
        val codeInfo = lastAuthCodeInfo ?: return Resource.Failure("No code to resend")
        _authState.update { it.copy(isLoading = true, errorMessage = null) }
        val result = clientManager.sendCatching(
            TdApi.ResendPhoneNumberCode(
                TdApi.ResendCodeReasonUserRequest()
            )
        )
        _authState.update { it.copy(isLoading = false) }
        return when (result) {
            is Resource.Success -> {
                lastAuthCodeInfo = result.data
                Resource.Success(Unit)
            }
            is Resource.Failure -> {
                _authState.update { s -> s.copy(errorMessage = result.message) }
                result
            }
            Resource.Loading -> Resource.Loading
        }
    }

    /** Checks the OTP/login code returned by Telegram. */
    suspend fun checkCode(code: String): Resource<Unit> {
        _authState.update { it.copy(isLoading = true, errorMessage = null) }
        val result = clientManager.sendCatching(TdApi.CheckAuthenticationCode(code.trim()))
        _authState.update { it.copy(isLoading = false) }
        return when (result) {
            is Resource.Success -> Resource.Success(Unit)
            is Resource.Failure -> {
                _authState.update { s -> s.copy(errorMessage = result.message) }
                result
            }
            Resource.Loading -> Resource.Loading
        }
    }

    /** Checks the 2FA password. */
    suspend fun checkPassword(password: String): Resource<Unit> {
        _authState.update { it.copy(isLoading = true, errorMessage = null) }
        val result = clientManager.sendCatching(TdApi.CheckAuthenticationPassword(password))
        _authState.update { it.copy(isLoading = false) }
        return when (result) {
            is Resource.Success -> Resource.Success(Unit)
            is Resource.Failure -> {
                _authState.update { s -> s.copy(errorMessage = result.message) }
                result
            }
            Resource.Loading -> Resource.Loading
        }
    }

    /** Signs out of the current TDLib session. */
    suspend fun logOut(): Resource<Unit> {
        _authState.update { it.copy(isLoading = true, errorMessage = null) }
        val result = clientManager.sendCatching(TdApi.LogOut())
        _authState.update { it.copy(isLoading = false) }
        return when (result) {
            is Resource.Success -> {
                _isLoggedIn.value = false
                _currentUser.value = null
                lastAuthCodeInfo = null
                currentPhoneNumber = null
                Resource.Success(Unit)
            }
            is Resource.Failure -> {
                _authState.update { s -> s.copy(errorMessage = result.message) }
                result
            }
            Resource.Loading -> Resource.Loading
        }
    }

    // ---------------------------------------------------------------- internal

    private fun handleAuthorizationState(state: TdApi.AuthorizationState) {
        when (state) {
            is TdApi.AuthorizationStateWaitPhoneNumber -> {
                _authState.update {
                    it.copy(
                        step = AuthStep.WAIT_PHONE,
                        isLoading = false,
                        errorMessage = null,
                        codeInfo = null,
                        passwordHint = null
                    )
                }
                _isLoggedIn.value = false
            }
            is TdApi.AuthorizationStateWaitCode -> {
                lastAuthCodeInfo = state.codeInfo
                _authState.update {
                    it.copy(
                        step = AuthStep.WAIT_CODE,
                        isLoading = false,
                        errorMessage = null,
                        codeInfo = state.codeInfo,
                        passwordHint = null
                    )
                }
                _isLoggedIn.value = false
            }
            is TdApi.AuthorizationStateWaitPassword -> {
                _authState.update {
                    it.copy(
                        step = AuthStep.WAIT_PASSWORD,
                        isLoading = false,
                        errorMessage = null,
                        codeInfo = null,
                        passwordHint = state.passwordHint
                    )
                }
                _isLoggedIn.value = false
            }
            is TdApi.AuthorizationStateReady -> {
                _authState.update {
                    it.copy(
                        step = AuthStep.READY,
                        isLoading = false,
                        errorMessage = null,
                        codeInfo = null,
                        passwordHint = null
                    )
                }
                _isLoggedIn.value = true
                appScope.launch {
                    fetchCurrentUser()
                }
            }
            is TdApi.AuthorizationStateWaitTdlibParameters -> {
                // TDLib is waiting for setTdlibParameters. The UI should remain in a
                // loading/initializing state until TelegramClientManager.configureParameters
                // has been called by the application.
                _authState.update {
                    it.copy(
                        step = AuthStep.INITIALIZING,
                        isLoading = true,
                        errorMessage = null
                    )
                }
                _isLoggedIn.value = false
            }
            is TdApi.AuthorizationStateLoggingOut -> {
                _authState.update { it.copy(step = AuthStep.LOGGING_OUT, isLoading = true) }
                _isLoggedIn.value = false
            }
            is TdApi.AuthorizationStateClosing -> {
                _authState.update { it.copy(step = AuthStep.CLOSING, isLoading = true) }
                _isLoggedIn.value = false
            }
            is TdApi.AuthorizationStateClosed -> {
                _authState.update {
                    it.copy(
                        step = AuthStep.CLOSED,
                        isLoading = false,
                        errorMessage = "Session closed"
                    )
                }
                _isLoggedIn.value = false
                _currentUser.value = null
            }
            is TdApi.AuthorizationStateWaitRegistration -> {
                // Registration flow is not required by the spec; surface a generic error.
                _authState.update {
                    it.copy(
                        step = AuthStep.WAIT_PHONE,
                        isLoading = false,
                        errorMessage = "Registration is not supported in this build"
                    )
                }
                _isLoggedIn.value = false
            }
            else -> {
                // Unknown states: keep current UI but mark not logged in.
                _isLoggedIn.value = false
            }
        }
    }

    private suspend fun fetchCurrentUser() {
        try {
            val me = clientManager.send(TdApi.GetMe())
            _currentUser.value = me
        } catch (ce: CancellationException) {
            throw ce
        } catch (t: Throwable) {
            // Fetching the user is best-effort; do not block READY state.
            _authState.update { s -> s.copy(errorMessage = t.message ?: "Failed to fetch user") }
        }
    }

    private fun normalizePhone(phone: String): String {
        val trimmed = phone.trim()
        return if (trimmed.startsWith("+")) trimmed else "+$trimmed"
    }
}

/** High-level authentication steps consumed by Compose UI. */
enum class AuthStep {
    INITIALIZING,
    WAIT_PHONE,
    WAIT_CODE,
    WAIT_PASSWORD,
    READY,
    LOGGING_OUT,
    CLOSING,
    CLOSED
}

/** UI state derived from TDLib authorization updates. */
data class AuthUiState(
    val step: AuthStep = AuthStep.INITIALIZING,
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val codeInfo: TdApi.AuthenticationCodeInfo? = null,
    val passwordHint: String? = null
) {
    val canRequestCode: Boolean get() = step == AuthStep.WAIT_PHONE && !isLoading
    val canCheckCode: Boolean get() = step == AuthStep.WAIT_CODE && !isLoading
    val canCheckPassword: Boolean get() = step == AuthStep.WAIT_PASSWORD && !isLoading
    val isReady: Boolean get() = step == AuthStep.READY && !isLoading
}