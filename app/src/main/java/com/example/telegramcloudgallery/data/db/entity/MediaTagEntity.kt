package com.example.telegramcloudgallery.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * A user defined tag (`#Vacation`, `#Documents`, `#Reels`, …).
 *
 * Tags are intentionally *flat* while folders are a *tree*: a single media item
 * can carry many tags but lives in exactly one (virtual) folder. That keeps
 * the multi-tag AND queries in `GalleryScreen` cheap while still allowing
 * nested-folder scoping.
 */
@Entity(
    tableName = "media_tags",
    indices = [
        Index(value = ["name"], unique = true)
    ]
)
data class MediaTagEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    /** Stored **without** the leading `#`. */
    val name: String,

    @ColumnInfo(name = "color_argb")
    val colorArgb: Int = 0,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
) {
    /** Convenience for the chip row: `name` rendered as `#name`. */
    val display: String get() = "#$name"
}