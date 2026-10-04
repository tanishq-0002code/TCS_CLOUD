package com.example.telegramcloudgallery.security

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner

/**
 * Observes app lifecycle to automatically lock the app when backgrounded.
 * Locks if app was in background for more than 5 seconds (per spec).
 */
class AppLifecycleObserver(
    private val application: Application,
    private val securityManager: SecurityManager,
    private val onShouldLock: () -> Unit = {}
) : DefaultLifecycleObserver, Application.ActivityLifecycleCallbacks {

    private var foregroundActivityCount = 0
    private var isInForeground = false

    init {
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        application.registerActivityLifecycleCallbacks(this)
    }

    fun unregister() {
        ProcessLifecycleOwner.get().lifecycle.removeObserver(this)
        application.unregisterActivityLifecycleCallbacks(this)
    }

    override fun onStart(owner: LifecycleOwner) {
        // App came to foreground
        isInForeground = true
        if (securityManager.shouldLockOnForeground()) {
            onShouldLock()
        }
    }

    override fun onStop(owner: LifecycleOwner) {
        // App went to background
        isInForeground = false
        securityManager.setLastBackgroundTime(System.currentTimeMillis())
    }

    // Activity lifecycle callbacks for tracking activity count
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
    }

    override fun onActivityStarted(activity: Activity) {
        foregroundActivityCount++
    }

    override fun onActivityResumed(activity: Activity) {
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
        foregroundActivityCount--
        if (foregroundActivityCount <= 0) {
            // App is no longer visible
            securityManager.setLastBackgroundTime(System.currentTimeMillis())
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }
}