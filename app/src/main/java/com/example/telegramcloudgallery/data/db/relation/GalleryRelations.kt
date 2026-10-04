package com.example.telegramcloudgallery.data.db.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.telegramcloudgallery.data.db.entity.MediaFolderEntity
import com.example.telegramcloudgallery.data.db.entity.MediaItemEntity
import com.example.telegramcloudgallery.data.db.entity.MediaItemTagCrossRef
import com.example.telegramcloudgallery.data.db.entity.MediaTagEntity

/**
 * A media item together with every tag applied to it. Used by the gallery grid
 * (to render tag chips under each card) and by the folder detail screen.
 */
data class MediaItemWithTags(
    @Embedded
    val item: MediaItemEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = MediaItemTagCrossRef::class,
            parentColumn = "item_id",
            entityColumn = "tag_id"
        )
    )
    val tags: List<MediaTagEntity> = emptyList()
)

/** A virtual folder with its direct children — the folder tree UI model. */
data class MediaFolderWithChildren(
    @Embedded
    val folder: MediaFolderEntity,

    @Relation(parentColumn = "id", entityColumn = "parent_id")
    val children: List<MediaFolderEntity> = emptyList()
)

/** Folder plus aggregate counters shown as a badge on the folder chip. */
data class MediaFolderWithCount(
    @Embedded
    val folder: MediaFolderEntity,

    @androidx.room.ColumnInfo(name = "item_count")
    val itemCount: Int = 0,

    @androidx.room.ColumnInfo(name = "video_count")
    val videoCount: Int = 0
)

/** Tag plus how many media items currently carry it. */
data class MediaTagWithCount(
    @Embedded
    val tag: MediaTagEntity,

    @androidx.room.ColumnInfo(name = "usage_count")
    val usageCount: Int = 0
)

/** Everything needed to render one gallery cell in a single Room read. */
data class GalleryCard(
    @Embedded
    val item: MediaItemEntity,
    val tags: List<MediaTagEntity> = emptyList()
) {
    val title: String get() = item.fileName ?: captionFirstLine ?: item.remoteId

    private val captionFirstLine: String?
        get() = item.captionText?.lineSequence()?.firstOrNull()?.takeIf { it.isNotBlank() }
}