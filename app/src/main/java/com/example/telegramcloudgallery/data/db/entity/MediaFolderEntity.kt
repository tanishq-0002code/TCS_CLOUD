package com.example.telegramcloudgallery.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * A *virtual folder* in the gallery.
 *
 * The dual-mapping strategy is modelled by two independent columns:
 *
 *  1. **Local-only organisation** — [parentId] builds a real folder tree that
 *     lives purely inside Room. Media is attached through
 *     [MediaItemEntity.folderId].
 *  2. **Telegram organisation** — [telegramChatId] points at a private
 *     supergroup/channel used as the folder's cloud "sub-channel", while
 *     [telegramChatFolderId] points at a Telegram-native *Chat Folder*
 *     (`org.drinkless.tdlib.TdApi.ChatFolder`). `0` means "not mapped".
 */
@Entity(
    tableName = "media_folders",
    foreignKeys = [
        ForeignKey(
            entity = MediaFolderEntity::class,
            parentColumns = ["id"],
            childColumns = ["parent_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["parent_id"]),
        Index(value = ["name"]),
        Index(value = ["telegram_chat_id"])
    ]
)
data class MediaFolderEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    /** User visible label, e.g. "Trip 2026". */
    val name: String,

    /** `null` for a top level folder, otherwise the parent folder's id. */
    @ColumnInfo(name = "parent_id")
    val parentId: Long? = null,

    /** Optional emoji used by the folder-chip row, e.g. "🏖️". */
    val emoji: String? = null,

    /** ARGB accent used by [TagChipGroup] / folder chips. */
    @ColumnInfo(name = "color_argb")
    val colorArgb: Int = 0,

    /** Manual ordering inside [parentId]. */
    @ColumnInfo(name = "sort_order")
    val sortOrder: Int = 0,

    /** When true, queries against this folder transparently include descendants. */
    @ColumnInfo(name = "include_subfolders")
    val includeSubfolders: Boolean = true,

    // ---- Telegram Chat Folder / sub-channel mapping -------------------------

    /** `chat_id` of the Telegram chat (private supergroup) backing this folder. `0` = unmapped. */
    @ColumnInfo(name = "telegram_chat_id")
    val telegramChatId: Long = 0L,

    /** `chat_folder_id` of the Telegram-native Chat Folder. `0` = unmapped. */
    @ColumnInfo(name = "telegram_chat_folder_id")
    val telegramChatFolderId: Int = 0,

    // ---- Automation --------------------------------------------------------

    /**
     * Comma separated list of tags that are *auto applied* to anything synced
     * into this folder, e.g. `Trip2026,Family`.
     */
    @ColumnInfo(name = "auto_tag_rule")
    val autoTagRule: String? = null,

    /** Whether [com.example.telegramcloudgallery.sync.FolderSyncWorker] should push local media here. */
    @ColumnInfo(name = "auto_sync")
    val autoSync: Boolean = false,

    // ---- Bookkeeping -------------------------------------------------------
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "updated_at")
    val updatedAt: Long = System.currentTimeMillis()
) {
    val isTelegramMapped: Boolean
        get() = telegramChatId != 0L || telegramChatFolderId != 0

    /** [autoTagRule] split into a normalised, non-empty tag list. */
    val autoTags: List<String>
        get() = autoTagRule
            ?.split(',', ' ', '#')
            ?.map { it.trim().removePrefix("#") }
            ?.filter { it.isNotBlank() }
            .orEmpty()
}