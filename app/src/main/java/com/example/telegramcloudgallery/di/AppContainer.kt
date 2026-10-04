package com.example.telegramcloudgallery.di

import android.content.Context
import com.example.telegramcloudgallery.core.DefaultDispatcherProvider
import com.example.telegramcloudgallery.core.DispatcherProvider
import com.example.telegramcloudgallery.data.db.AppDatabase
import com.example.telegramcloudgallery.data.telegram.TelegramAuthRepository
import com.example.telegramcloudgallery.data.telegram.TelegramClientManager
import com.example.telegramcloudgallery.data.telegram.TelegramMediaRepository
import com.example.telegramcloudgallery.media.MediaProcessor
import com.example.telegramcloudgallery.media.VideoFrameExtractor
import com.example.telegramcloudgallery.security.AppLifecycleObserver
import com.example.telegramcloudgallery.security.BiometricHelper
import com.example.telegramcloudgallery.security.SecurityManager
import com.example.telegramcloudgallery.sync.MediaContentObserver
import com.example.telegramcloudgallery.sync.SyncManager
import com.example.telegramcloudgallery.sync.SyncPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.io.File

/**
 * Lightweight DI container. In a full app, Hilt would be used.
 * This keeps dependencies centralized and testable.
 */
class AppContainer(context: Context) {
    private val appContext = context.applicationContext

    val dispatcherProvider: DispatcherProvider by lazy { DefaultDispatcherProvider() }
    val database: AppDatabase by lazy { AppDatabase.getInstance(appContext) }
    val syncPreferences: SyncPreferences by lazy { SyncPreferences(appContext) }
    val syncManager: SyncManager by lazy { SyncManager(appContext) }
    val securityManager: SecurityManager by lazy { SecurityManager(appContext) }
    val biometricHelper: BiometricHelper by lazy { BiometricHelper(appContext) }
    val mediaProcessor: MediaProcessor by lazy { MediaProcessor(appContext, dispatcherProvider) }
    val videoFrameExtractor: VideoFrameExtractor by lazy {
        VideoFrameExtractor(appContext, File(appContext.cacheDir, "thumbs"), dispatcherProvider)
    }
    val authRepository: TelegramAuthRepository by lazy {
        TelegramAuthRepository(
            clientManager = TelegramClientManager,
            appScope = CoroutineScope(Dispatchers.IO),
            context = appContext
        )
    }
    val mediaRepository: TelegramMediaRepository by lazy {
        TelegramMediaRepository(
            context = appContext,
            clientManager = TelegramClientManager,
            database = database,
            dispatcherProvider = dispatcherProvider
        )
    }
    val mediaContentObserver: MediaContentObserver by lazy {
        MediaContentObserver(appContext, onMediaChanged = {})
    }
    val appLifecycleObserver: AppLifecycleObserver by lazy {
        AppLifecycleObserver(
            application = appContext as android.app.Application,
            securityManager = securityManager
        )
    }
}