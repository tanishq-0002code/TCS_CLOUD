package com.example.telegramcloudgallery.sync

import android.content.ContentResolver
import android.content.Context
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * MediaContentObserver listens to MediaStore changes (images/videos) and
 * triggers debounced sync events to avoid spamming sync on bulk operations.
 */
class MediaContentObserver(
    private val context: Context,
    private val onMediaChanged: () -> Unit,
    private val debounceMs: Long = 1500L
) : ContentObserver(Handler(Looper.getMainLooper())) {

    private val _mediaChanged = MutableLiveData<Unit>()
    val mediaChanged: LiveData<Unit> = _mediaChanged

    private var debounceJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Main)
    private var contentResolver: ContentResolver = context.contentResolver

    private val mediaUris = arrayOf(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        MediaStore.Video.Media.EXTERNAL_CONTENT_URI
    )

    fun register() {
        mediaUris.forEach { uri ->
            try {
                contentResolver.registerContentObserver(uri, true, this)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to register observer for $uri", e)
            }
        }
        Log.d(TAG, "MediaContentObserver registered")
    }

    fun unregister() {
        try {
            contentResolver.unregisterContentObserver(this)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to unregister observer", e)
        }
        debounceJob?.cancel()
        Log.d(TAG, "MediaContentObserver unregistered")
    }

    override fun onChange(selfChange: Boolean) {
        onChange(selfChange, null)
    }

    override fun onChange(selfChange: Boolean, uri: Uri?) {
        debounceJob?.cancel()
        debounceJob = scope.launch {
            delay(debounceMs)
            Log.d(TAG, "MediaStore change detected (uri=$uri, selfChange=$selfChange)")
            _mediaChanged.postValue(Unit)
            onMediaChanged()
        }
    }

    companion object {
        private const val TAG = "MediaContentObserver"
    }
}