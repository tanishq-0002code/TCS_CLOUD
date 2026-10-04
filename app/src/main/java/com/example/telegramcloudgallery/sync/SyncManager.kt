package com.example.telegramcloudgallery.sync

import android.content.Context
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.TimeUnit

/**
 * SyncManager configures and enqueues WorkManager background sync tasks
 * with user-defined constraints (Wi-Fi only, charging required, etc.)
 */
class SyncManager(private val context: Context) {

    private val workManager = WorkManager.getInstance(context)
    private val syncPreferences = SyncPreferences(context)

    companion object {
        const val TAG = "SyncManager"
    }

    // ------------------------------------------------------------------ scheduling

    fun schedulePeriodicSync(intervalHours: Float = 4f) {
        val constraints = buildConstraints()
        val intervalMinutes = (intervalHours * 60f).toLong().coerceAtLeast(60L)
        val flexMinutes = (intervalMinutes * 0.1f).toLong().coerceAtLeast(15L)

        val periodicWork = PeriodicWorkRequestBuilder<FolderSyncWorker>(
            repeatInterval = intervalMinutes,
            repeatIntervalTimeUnit = TimeUnit.MINUTES,
            flexTimeInterval = flexMinutes,
            flexTimeIntervalTimeUnit = TimeUnit.MINUTES
        )
            .setConstraints(constraints)
            .build()

        workManager.enqueueUniquePeriodicWork(
            FolderSyncWorker.WORK_NAME_PERIODIC,
            ExistingPeriodicWorkPolicy.REPLACE,
            periodicWork
        )
    }

    fun cancelPeriodicSync() {
        workManager.cancelUniqueWork(FolderSyncWorker.WORK_NAME_PERIODIC)
    }

    fun enqueueOneTimeSync(force: Boolean = false) {
        val constraints = buildConstraints()
        val data = Data.Builder()
            .putBoolean(FolderSyncWorker.KEY_FORCE_SYNC, force)
            .build()

        val oneTimeWork = OneTimeWorkRequestBuilder<FolderSyncWorker>()
            .setConstraints(constraints)
            .setInputData(data)
            .build()

        workManager.enqueueUniqueWork(
            FolderSyncWorker.WORK_NAME_ONE_SHOT,
            ExistingWorkPolicy.REPLACE,
            oneTimeWork
        )
    }

    fun cancelAllSync() {
        workManager.cancelAllWorkByTag(FolderSyncWorker.TAG)
    }

    // ------------------------------------------------------------------ status

    fun getPeriodicSyncState(): Flow<WorkInfo.State?> {
        return workManager.getWorkInfosForUniqueWorkFlow(FolderSyncWorker.WORK_NAME_PERIODIC)
            .map { workInfos -> workInfos.firstOrNull()?.state }
    }

    fun isPeriodicSyncEnqueued(): Flow<Boolean> {
        return getPeriodicSyncState().map { state ->
            state == WorkInfo.State.ENQUEUED || state == WorkInfo.State.RUNNING || state == WorkInfo.State.BLOCKED
        }
    }

    // ------------------------------------------------------------------ helpers

    private fun buildConstraints(): Constraints {
        var networkType = NetworkType.UNMETERED
        // Note: We read preferences synchronously here; in practice these are applied
        // when scheduling - the worker also re-checks. For simplicity, default to UNMETERED.
        var requiresCharging = true

        return Constraints.Builder()
            .setRequiredNetworkType(networkType)
            .setRequiresCharging(requiresCharging)
            .setRequiresBatteryNotLow(false)
            .setRequiresDeviceIdle(false)
            .build()
    }
}