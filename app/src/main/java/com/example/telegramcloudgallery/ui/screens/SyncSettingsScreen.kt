package com.example.telegramcloudgallery.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.telegramcloudgallery.sync.SyncManager
import com.example.telegramcloudgallery.sync.SyncPreferences
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SyncSettingsScreenContent(
    syncPreferences: SyncPreferences,
    syncManager: SyncManager,
    onNavigateUp: () -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    val wifiOnly by syncPreferences.wifiOnly.collectAsState(initial = true)
    val requireCharging by syncPreferences.requireCharging.collectAsState(initial = true)
    val allowMetered by syncPreferences.allowMetered.collectAsState(initial = false)
    val syncIntervalHours by syncPreferences.syncIntervalHours.collectAsState(initial = 4f)
    val autoSyncEnabled by syncPreferences.autoSyncEnabled.collectAsState(initial = false)
    val lastSyncTime by syncPreferences.lastSyncTime.collectAsState(initial = 0L)
    var interval by rememberSaveable { mutableStateOf(syncIntervalHours) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sync & Auto-Backup") },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Auto Sync", style = MaterialTheme.typography.titleMedium)
                    ListItem(
                        headlineContent = { Text("Enable Background Sync") },
                        supportingContent = { Text("Automatically sync new media") },
                        trailingContent = {
                            Switch(
                                checked = autoSyncEnabled,
                                onCheckedChange = { enabled ->
                                    scope.launch {
                                        syncPreferences.setAutoSyncEnabled(enabled)
                                        if (enabled) syncManager.schedulePeriodicSync(interval) else syncManager.cancelPeriodicSync()
                                    }
                                }
                            )
                        }
                    )
                }
            }
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Constraints", style = MaterialTheme.typography.titleMedium)
                    ListItem(
                        headlineContent = { Text("Wi-Fi only") },
                        trailingContent = { Switch(checked = wifiOnly, onCheckedChange = { scope.launch { syncPreferences.setWifiOnly(it) } }) }
                    )
                    ListItem(
                        headlineContent = { Text("Require charging") },
                        trailingContent = { Switch(checked = requireCharging, onCheckedChange = { scope.launch { syncPreferences.setRequireCharging(it) } }) }
                    )
                    ListItem(
                        headlineContent = { Text("Allow metered network") },
                        trailingContent = { Switch(checked = allowMetered, onCheckedChange = { scope.launch { syncPreferences.setAllowMetered(it) } }) }
                    )
                }
            }
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Sync Interval", style = MaterialTheme.typography.titleMedium)
                    Text("${"%.1f".format(interval)} hours")
                    Slider(value = interval, onValueChange = { interval = it }, valueRange = 1f..24f, steps = 22)
                    Button(onClick = { scope.launch { syncPreferences.setSyncIntervalHours(interval); if (autoSyncEnabled) syncManager.schedulePeriodicSync(interval) } }) { Text("Apply") }
                }
            }
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Manual Sync", style = MaterialTheme.typography.titleMedium)
                    Text(
                        "Last sync: ${if (lastSyncTime == 0L) "Never" else SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault()).format(Date(lastSyncTime))}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                        Button(onClick = { syncManager.enqueueOneTimeSync(false) }, modifier = Modifier.weight(1f)) { Text("Sync Now") }
                        OutlinedButton(onClick = { syncManager.enqueueOneTimeSync(true) }, modifier = Modifier.weight(1f)) { Text("Force Sync") }
                    }
                }
            }
        }
    }
}