package com.example.telegramcloudgallery

import android.app.Application
import com.example.telegramcloudgallery.data.telegram.TelegramClientManager
import com.example.telegramcloudgallery.di.AppContainer

/**
 * Application class with DI container.
 */
class TelegramGalleryApp : Application() {
    lateinit var appContainer: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer(this)
        // TDLib must be created once per process
        TelegramClientManager.initialize()
        // Start observing auth
        appContainer.authRepository.startObserving()
    }
}