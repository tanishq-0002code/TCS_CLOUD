package com.example.telegramcloudgallery.core

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Injectable dispatcher provider so DAO / TDLib work never runs on the main
 * thread and unit tests can swap in a deterministic test dispatcher.
 *
 * Deliberately framework-free (no Hilt) — the project uses a tiny hand-rolled
 * [com.example.telegramcloudgallery.di.AppContainer] service locator, so this
 * only needs to be an interface with a default implementation.
 */
interface DispatcherProvider {
    val io: CoroutineDispatcher
    val default: CoroutineDispatcher
    val main: CoroutineDispatcher
}

class DefaultDispatcherProvider : DispatcherProvider {
    override val io: CoroutineDispatcher get() = Dispatchers.IO
    override val default: CoroutineDispatcher get() = Dispatchers.Default
    override val main: CoroutineDispatcher get() = Dispatchers.Main
}