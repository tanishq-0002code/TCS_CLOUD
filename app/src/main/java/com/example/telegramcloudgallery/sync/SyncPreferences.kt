package com.example.telegramcloudgallery.sync

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Persistent sync preferences using DataStore.
 * Stores user sync settings: selected local folders, Wi-Fi/charging toggles,
 * target Telegram channel/folders, sync intervals.
 */
class SyncPreferences(private val context: Context) {
    private val Context.dataStore by preferencesDataStore(name = "sync_prefs")

    companion object {
        private val KEY_WIFI_ONLY = booleanPreferencesKey("wifi_only")
        private val KEY_REQUIRE_CHARGING = booleanPreferencesKey("require_charging")
        private val KEY_ALLOW_METERED = booleanPreferencesKey("allow_metered")
        private val KEY_SYNC_INTERVAL_HOURS = floatPreferencesKey("sync_interval_hours")
        private val KEY_LAST_SYNC_TIME = longPreferencesKey("last_sync_time")
        private val KEY_AUTO_SYNC_ENABLED = booleanPreferencesKey("auto_sync_enabled")
        private val KEY_SELECTED_LOCAL_FOLDERS = stringSetPreferencesKey("selected_local_folders")
        private val KEY_TARGET_TELEGRAM_CHAT_ID = longPreferencesKey("target_telegram_chat_id")
        private val KEY_TARGET_CHAT_FOLDER_ID = intPreferencesKey("target_chat_folder_id")
        private val KEY_SYNC_WHILE_IDLE = booleanPreferencesKey("sync_while_idle")
        private val KEY_USE_CELLULAR_ROAMING = booleanPreferencesKey("use_cellular_roaming")
    }

    // ------------------------------------------------------------------ getters (Flows)

    val wifiOnly: Flow<Boolean> = context.dataStore.data.map { it[KEY_WIFI_ONLY] ?: true }
    val requireCharging: Flow<Boolean> = context.dataStore.data.map { it[KEY_REQUIRE_CHARGING] ?: true }
    val allowMetered: Flow<Boolean> = context.dataStore.data.map { it[KEY_ALLOW_METERED] ?: false }
    val syncIntervalHours: Flow<Float> = context.dataStore.data.map { it[KEY_SYNC_INTERVAL_HOURS] ?: 4f }
    val lastSyncTime: Flow<Long> = context.dataStore.data.map { it[KEY_LAST_SYNC_TIME] ?: 0L }
    val autoSyncEnabled: Flow<Boolean> = context.dataStore.data.map { it[KEY_AUTO_SYNC_ENABLED] ?: false }
    val selectedLocalFolders: Flow<Set<String>> = context.dataStore.data.map { it[KEY_SELECTED_LOCAL_FOLDERS] ?: emptySet() }
    val targetTelegramChatId: Flow<Long> = context.dataStore.data.map { it[KEY_TARGET_TELEGRAM_CHAT_ID] ?: 0L }
    val targetChatFolderId: Flow<Int> = context.dataStore.data.map { it[KEY_TARGET_CHAT_FOLDER_ID] ?: 0 }
    val syncWhileIdle: Flow<Boolean> = context.dataStore.data.map { it[KEY_SYNC_WHILE_IDLE] ?: true }
    val useCellularRoaming: Flow<Boolean> = context.dataStore.data.map { it[KEY_USE_CELLULAR_ROAMING] ?: false }

    // ------------------------------------------------------------------ setters (suspend)

    suspend fun setWifiOnly(value: Boolean) {
        context.dataStore.edit { it[KEY_WIFI_ONLY] = value }
    }

    suspend fun setRequireCharging(value: Boolean) {
        context.dataStore.edit { it[KEY_REQUIRE_CHARGING] = value }
    }

    suspend fun setAllowMetered(value: Boolean) {
        context.dataStore.edit { it[KEY_ALLOW_METERED] = value }
    }

    suspend fun setSyncIntervalHours(value: Float) {
        context.dataStore.edit { it[KEY_SYNC_INTERVAL_HOURS] = value }
    }

    suspend fun setLastSyncTime(value: Long) {
        context.dataStore.edit { it[KEY_LAST_SYNC_TIME] = value }
    }

    suspend fun setAutoSyncEnabled(value: Boolean) {
        context.dataStore.edit { it[KEY_AUTO_SYNC_ENABLED] = value }
    }

    suspend fun setSelectedLocalFolders(folders: Set<String>) {
        context.dataStore.edit { it[KEY_SELECTED_LOCAL_FOLDERS] = folders }
    }

    suspend fun setTargetTelegramChatId(value: Long) {
        context.dataStore.edit { it[KEY_TARGET_TELEGRAM_CHAT_ID] = value }
    }

    suspend fun setTargetChatFolderId(value: Int) {
        context.dataStore.edit { it[KEY_TARGET_CHAT_FOLDER_ID] = value }
    }

    suspend fun setSyncWhileIdle(value: Boolean) {
        context.dataStore.edit { it[KEY_SYNC_WHILE_IDLE] = value }
    }

    suspend fun setUseCellularRoaming(value: Boolean) {
        context.dataStore.edit { it[KEY_USE_CELLULAR_ROAMING] = value }
    }

    // ------------------------------------------------------------------ helpers

    suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }
}

