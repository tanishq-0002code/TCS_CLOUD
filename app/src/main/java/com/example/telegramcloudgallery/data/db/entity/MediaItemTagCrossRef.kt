package com.example.telegramcloudgallery.data.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

/**
 * Many-to-many junction between [MediaItemEntity] and [MediaTagEntity].
 *
 * This is the fourth table of the schema — it is unavoidable for the
 * "media matching **multiple** tags" requirement (Rule 2), because a plain
 * `tag_id` column on `media_items` can only hold one tag per row.
 *
 * Both foreign keys cascade, so deleting a media item or a tag never leaves
 * orphan rows behind.
 */
@Entity(
    tableName = "media_item_tags",
    primaryKeys = ["item_id", "tag_id"],
    foreignKeys = [
        ForeignKey(
            entity = MediaItemEntity::class,
            parentColumns = ["id"],
            childColumns = ["item_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = MediaTagEntity::class,
            parentColumns = ["id"],
            childColumns = ["tag_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["tag_id"]),
        Index(value = ["item_id"])
    ]
)
data class MediaItemTagCrossRef(
    @androidx.room.ColumnInfo(name = "item_id") val itemId: Long,
    @androidx.room.ColumnInfo(name = "tag_id") val tagId: Long
) {
    companion object {
        fun of(itemId: Long, tagId: Long): MediaItemTagCrossRef =
            MediaItemTagCrossRef(itemId = itemId, tagId = tagId)
    }
}