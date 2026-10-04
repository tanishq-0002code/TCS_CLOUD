package com.example.telegramcloudgallery.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.telegramcloudgallery.data.db.AppDatabase
import com.example.telegramcloudgallery.data.db.entity.MediaFolderEntity
import kotlinx.coroutines.launch

/**
 * FolderManagementScreen - Manage virtual folders, tags, and Telegram mapping.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FolderManagementScreen(
    database: AppDatabase,
    onNavigateUp: () -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    val folders by database.folderDao().observeAll().collectAsState(initial = emptyList())
    val tags by database.tagDao().observeAll().collectAsState(initial = emptyList())

    var folderName by rememberSaveable { mutableStateOf("") }
    var mappedChatId by rememberSaveable { mutableStateOf("") }
    var tagName by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Folders & Tags") },
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Create Virtual Folder", style = MaterialTheme.typography.titleMedium)
                    OutlinedTextField(
                        value = folderName,
                        onValueChange = { folderName = it },
                        label = { Text("Folder name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = mappedChatId,
                        onValueChange = { mappedChatId = it },
                        label = { Text("Map to Telegram chat id (optional)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(
                        onClick = {
                            scope.launch {
                                if (folderName.isNotBlank()) {
                                    val chatId = mappedChatId.trim().toLongOrNull() ?: 0L
                                    database.folderDao().insert(
                                        MediaFolderEntity(
                                            name = folderName.trim(),
                                            telegramChatId = chatId
                                        )
                                    )
                                    folderName = ""
                                    mappedChatId = ""
                                }
                            }
                        },
                        enabled = folderName.isNotBlank()
                    ) {
                        Text("Create Folder")
                    }
                }
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Virtual Folders", style = MaterialTheme.typography.titleMedium)
                    folders.forEach { folder ->
                        Text(
                            "• ${folder.emoji ?: "📁"} ${folder.name}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    if (folders.isEmpty()) {
                        Text("No folders yet", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Create Tag", style = MaterialTheme.typography.titleMedium)
                    OutlinedTextField(
                        value = tagName,
                        onValueChange = { tagName = it },
                        label = { Text("Tag name (without #)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(
                        onClick = {
                            scope.launch {
                                if (tagName.isNotBlank()) {
                                    val normalized = tagName.trim().lowercase().replace("#", "")
                                    if (database.tagDao().findByName(normalized) == null) {
                                        database.tagDao().insert(
                                            com.example.telegramcloudgallery.data.db.entity.MediaTagEntity(
                                                name = normalized
                                            )
                                        )
                                    }
                                    tagName = ""
                                }
                            }
                        },
                        enabled = tagName.isNotBlank()
                    ) {
                        Text("Add Tag")
                    }
                }
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Tags", style = MaterialTheme.typography.titleMedium)
                    tags.forEach { tag ->
                        Text("#${tag.name}", style = MaterialTheme.typography.bodyMedium)
                    }
                    if (tags.isEmpty()) {
                        Text("No tags yet", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}