package com.example.telegramcloudgallery.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.outlined.FolderCopy
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.telegramcloudgallery.data.db.AppDatabase
import com.example.telegramcloudgallery.data.db.MediaDao
import com.example.telegramcloudgallery.ui.components.GlidePreviewVideoCard
import com.example.telegramcloudgallery.ui.components.TagChipGroup

/**
 * GalleryScreen - Fully wired with Room database.
 * Displays media in grid with tag/folder filters and glide preview cards.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryScreen(
    database: AppDatabase,
    onNavigateToFolders: () -> Unit = {},
    onNavigateToSyncSettings: () -> Unit = {},
    onNavigateToReels: () -> Unit = {},
    onNavigateToAuth: () -> Unit = {},
    onNavigateToSecurity: () -> Unit = {}
) {
    val gridState = rememberLazyGridState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedFolderIds by remember { mutableStateOf(emptySet<Long>()) }
    var selectedTagIds by remember { mutableStateOf(emptySet<Long>()) }

    val folders by database.folderDao().observeAll().collectAsState(initial = emptyList())
    val tags by database.tagDao().observeAll().collectAsState(initial = emptyList())
    val media = if (selectedFolderIds.isNotEmpty()) {
        database.mediaDao().observeCardsInFolders(selectedFolderIds.toList())
    } else if (selectedTagIds.isNotEmpty()) {
        database.mediaDao().observeCardsByAllTags(
            tagIds = selectedTagIds.toList(),
            tagCount = selectedTagIds.size,
            folderIds = listOf(MediaDao.NOTHING)
        )
    } else {
        // All media - observe by "All Media" or just get all? For simplicity, observe all via a different approach
        database.mediaDao().observeInFolders(folders.map { it.id }.ifEmpty { listOf(-1) })
            .collectAsState(initial = emptyList()).value
            .map { com.example.telegramcloudgallery.data.db.relation.MediaItemWithTags(it, emptyList()) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Telegram Cloud Gallery") },
                actions = {
                    IconButton(onClick = onNavigateToReels) {
                        Icon(imageVector = Icons.AutoMirrored.Outlined.List, contentDescription = "Reels")
                    }
                    IconButton(onClick = onNavigateToFolders) {
                        Icon(imageVector = Icons.Outlined.FolderCopy, contentDescription = "Folders")
                    }
                    IconButton(onClick = onNavigateToSyncSettings) {
                        Icon(imageVector = Icons.Outlined.Settings, contentDescription = "Sync settings")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TagChipGroup(
                tags = tags,
                folders = folders,
                selectedTagIds = selectedTagIds,
                selectedFolderIds = selectedFolderIds,
                onTagToggle = { tagId ->
                    selectedTagIds = if (tagId in selectedTagIds) {
                        selectedTagIds - tagId
                    } else {
                        selectedTagIds + tagId
                    }
                },
                onFolderToggle = { folderId ->
                    selectedFolderIds = if (folderId in selectedFolderIds) {
                        selectedFolderIds - folderId
                    } else {
                        selectedFolderIds + folderId
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Card(
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Media Library", style = MaterialTheme.typography.titleSmall)
                    if (media.isEmpty()) {
                        Text(
                            "No media indexed yet. Connect Telegram and sync to see your media.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 150.dp),
                state = gridState,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(media, key = { it.item.id }) { card ->
                    GlidePreviewVideoCard(card = card)
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick = onNavigateToAuth) {
                    Text("Re-authenticate")
                }
                TextButton(onClick = onNavigateToSecurity) {
                    Text("Security")
                }
            }
        }
    }
}
