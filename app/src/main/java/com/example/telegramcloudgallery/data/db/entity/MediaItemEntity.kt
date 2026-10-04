package com.example.telegramcloudgallery.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.telegramcloudgallery.core.MediaKind
import com.example.telegramcloudgallery.core.UploadState

/**
 * One indexed piece of media, identified by its Telegram coordinates
 * ([chatId] + [messageId]) plus a locally cached copy in [localPath].
 *
 * [remoteFileId] / [localFileId] / [thumbnailFileId] hold TDLib `File.id`
 * values. They are what make **progressive streaming** possible: the player can
 * call `DownloadFile(fileId, priority = 1, …)` and start playing before the
 * file is complete, instead of waiting for a full download.
 */
@Entity(
    tableName = "media_items",
    foreignKeys = [
        ForeignKey(
            entity = MediaFolderEntity::class,
            parentColumns = ["id"],
            childColumns = ["folder_id"],
            // Deleting a folder must never delete the user's media index.
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index(value = ["chat_id", "message_id"], unique = true),
        Index(value = ["remote_id"], unique = true),
        Index(value = ["folder_id"]),
        Index(value = ["media_type"]),
        Index(value = ["upload_state"]),
        Index(value = ["message_date"]),
        Index(value = ["local_content_hash"])
    ]
)
data class MediaItemEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    /** Stable surrogate key: `"<chatId>:<messageId>"`. */
    @ColumnInfo(name = "remote_id")
    val remoteId: String,

    // ---- Telegram coordinates ---------------------------------------------
    @ColumnInfo(name = "chat_id")
    val chatId: Long = 0L,

    @ColumnInfo(name = "message_id")
    val messageId: Long = 0L,

    // ---- Local organisation ------------------------------------------------
    @ColumnInfo(name = "folder_id")
    val folderId: Long? = null,

    // ---- Media descriptor --------------------------------------------------
    @ColumnInfo(name = "media_type")
    val mediaType: MediaKind = MediaKind.UNKNOWN,

    @ColumnInfo(name = "file_name")
    val fileName: String? = null,

    @ColumnInfo(name = "mime_type")
    val mimeType: String? = null,

    @ColumnInfo(name = "file_size")
    val fileSize: Long = 0L,

    @ColumnInfo(name = "duration_ms")
    val durationMs: Long = 0L,

    val width: Int = 0,
    val height: Int = 0,

    // ---- Caption metadata (used by the #Tag convention) -------------------
    @ColumnInfo(name = "caption_text")
    val captionText: String? = null,

    /** Raw `TdApi.TextEntity` JSON so hashtag positions survive round-trips. */
    @ColumnInfo(name = "caption_entities_json")
    val captionEntitiesJson: String? = null,

    // ---- TDLib file handles ------------------------------------------------
    /** Remote `File.id` — drives streaming + thumbnails. */
    @ColumnInfo(name = "remote_file_id")
    val remoteFileId: Int = 0,

    /** `File.local.id` once downloaded. */
    @ColumnInfo(name = "local_file_id")
    val localFileId: Int = 0,

    /** `PhotoSize`/`Video` thumbnail `File.id`. */
    @ColumnInfo(name = "thumbnail_file_id")
    val thumbnailFileId: Int = 0,

    // ---- Local file state --------------------------------------------------
    @ColumnInfo(name = "local_path")
    val localPath: String? = null,

    @ColumnInfo(name = "is_downloaded")
    val isDownloaded: Boolean = false,

    // ---- Cloud sync state --------------------------------------------------
    @ColumnInfo(name = "upload_state")
    val uploadState: UploadState = UploadState.NONE,

    /** 0..100, driven by `TdApi.UpdateFile` during an upload/download. */
    @ColumnInfo(name = "upload_progress")
    val uploadProgress: Int = 0,

    @ColumnInfo(name = "upload_error")
    val uploadError: String? = null,

    /**
     * SHA-256 of the source file, used by `FolderSyncWorker` to skip re-uploading
     * unchanged local files.
     */
    @ColumnInfo(name = "local_content_hash")
    val localContentHash: String? = null,

    // ---- Timestamps --------------------------------------------------------
    @ColumnInfo(name = "message_date")
    val messageDate: Long = 0L,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "updated_at")
    val updatedAt: Long = System.currentTimeMillis()
) {
    val isVideo: Boolean
        get() = mediaType == MediaKind.VIDEO || mediaType == MediaKind.ANIMATION

    /** Aspect ratio, guarding against divide-by-zero for audio/stickers. */
    val aspectRatio: Float
        get() = if (height > 0 && width > 0) width.toFloat() / height.toFloat() else 1f

    val durationSeconds: Float
        get() = if (durationMs > 0) durationMs / 1000f else 0f
}