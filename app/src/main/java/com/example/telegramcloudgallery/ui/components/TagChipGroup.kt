package com.example.telegramcloudgallery.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.telegramcloudgallery.data.db.entity.MediaFolderEntity
import com.example.telegramcloudgallery.data.db.entity.MediaTagEntity

/**
 * `TagChipGroup` — filter bar displaying custom user tags (`#Vacation`,
 * `#Documents`, `#Reels`) and folder chips.
 *
 * The spec requires this component in `ui/components/` and to be used inside
 * `GalleryScreen`. PHASE 4 will wire it to [MediaTagEntity] + [MediaFolderEntity]
 * via Room flows and emit multi-tag + folder selection to the ViewModel.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TagChipGroup(
    tags: List<MediaTagEntity>,
    folders: List<MediaFolderEntity>,
    selectedTagIds: Set<Long>,
    selectedFolderIds: Set<Long>,
    onTagToggle: (Long) -> Unit = {},
    onFolderToggle: (Long) -> Unit = {},
    modifier: Modifier = Modifier
) {
    FlowRow(
        modifier = modifier.padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 2.dp)
    ) {
        // Folder chips
        folders.forEach { folder ->
            val selected = folder.id in selectedFolderIds
            AssistChip(
                onClick = { onFolderToggle(folder.id) },
                label = { Text(folder.name) },
                leadingIcon = { folder.emoji?.let { Text(it) } }
            )
        }
        // Tag chips (#name)
        tags.forEach { tag ->
            val selected = tag.id in selectedTagIds
            AssistChip(
                onClick = { onTagToggle(tag.id) },
                label = { Text("#${tag.name}") }
            )
        }
        if (tags.isEmpty() && folders.isEmpty()) {
            Text(
                text = "No tags or folders yet — add them in Folders & Tags",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}