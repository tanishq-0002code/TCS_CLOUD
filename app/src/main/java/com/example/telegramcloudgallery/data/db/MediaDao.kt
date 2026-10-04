package com.example.telegramcloudgallery.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.telegramcloudgallery.core.MediaKind
import com.example.telegramcloudgallery.core.UploadState
import com.example.telegramcloudgallery.data.db.entity.MediaItemEntity
import com.example.telegramcloudgallery.data.db.relation.MediaItemWithTags
import com.example.telegramcloudgallery.data.db.relation.MediaItemWithTags
import kotlinx.coroutines.flow.Flow

/**
 * Offline index over Telegram messages that carry media.
 *
 * This is the query surface `GalleryScreen`, `ReelFeedScreen` and
 * `FolderSyncWorker` are built on. Everything here is a [Flow] so the UI
 * re-renders automatically after a sync, an upload or a tag edit.
 *
 * **Empty-collection contract:** Room expands `IN (:list)` by binding one
 * argument per element, so an *empty* Kotlin list would leave the statement
 * invalid. Every method that takes a collection therefore documents that the
 * caller must pass at least one element â€” repositories substitute
 * [NOTHING] (an id that can never exist) to express "no restriction".
 */
@Dao
interface MediaDao {

    companion object {
        /** Sentinel bound into `IN (:list)` when the filter list would be empty. */
        const val NOTHING = -1L
    }

    // ------------------------------------------------------------------ write

    /** `remote_id` is unique, so re-indexing an existing chat is idempotent. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: MediaItemEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<MediaItemEntity>): List<Long>

    @Update
    suspend fun update(item: MediaItemEntity)

    @Query("DELETE FROM media_items WHERE id = :itemId")
    suspend fun deleteById(itemId: Long)

    @Query("DELETE FROM media_items WHERE chat_id = :chatId")
    suspend fun deleteByChat(chatId: Long)

    @Query(
        """
        UPDATE media_items
           SET upload_state = :state,
               upload_progress = :progress,
               upload_error = :error,
               updated_at = :now
         WHERE id = :itemId
        """
    )
    suspend fun updateUploadState(
        itemId: Long,
        state: UploadState,
        progress: Int,
        error: String?,
        now: Long
    )

    @Query(
        """
        UPDATE media_items
           SET is_downloaded = :downloaded,
               local_path = :localPath,
               local_file_id = :localFileId,
               updated_at = :now
         WHERE id = :itemId
        """
    )
    suspend fun updateLocalFile(
        itemId: Long,
        downloaded: Boolean,
        localPath: String?,
        localFileId: Int,
        now: Long
    )

    @Query("UPDATE media_items SET folder_id = :folderId, updated_at = :now WHERE id = :itemId")
    suspend fun setFolder(itemId: Long, folderId: Long?, now: Long)

    @Query(
        """
        UPDATE media_items
           SET remote_file_id = :remoteFileId,
               thumbnail_file_id = :thumbnailFileId,
               updated_at = :now
         WHERE chat_id = :chatId AND message_id = :messageId
        """
    )
    suspend fun updateFileIds(
        chatId: Long,
        messageId: Long,
        remoteFileId: Int,
        thumbnailFileId: Int,
        now: Long
    )

    @Query(
        """
        UPDATE media_items
           SET upload_state = 'UPLOADED',
               upload_progress = 100,
               upload_error = NULL,
               updated_at = :now
         WHERE id = :itemId
        """
    )
    suspend fun markUploaded(itemId: Long, now: Long)

    // ------------------------------------------------------------------- read

    @Query("SELECT * FROM media_items WHERE id = :itemId")
    fun observeById(itemId: Long): Flow<MediaItemEntity?>

    @Query("SELECT * FROM media_items WHERE id = :itemId")
    suspend fun findById(itemId: Long): MediaItemEntity?

    @Query("SELECT * FROM media_items WHERE remote_id = :remoteId")
    suspend fun findByRemoteId(remoteId: String): MediaItemEntity?

    @Query("SELECT * FROM media_items WHERE chat_id = :chatId AND message_id = :messageId LIMIT 1")
    suspend fun findByCoordinates(chatId: Long, messageId: Long): MediaItemEntity?

    @Query("SELECT * FROM media_items WHERE chat_id = :chatId ORDER BY message_date DESC")
    suspend fun getByChat(chatId: Long): List<MediaItemEntity>

    @Query("SELECT * FROM media_items ORDER BY message_date DESC")
    suspend fun getAll(): List<MediaItemEntity>

    @Query("SELECT * FROM media_items WHERE upload_state IN ('PENDING', 'FAILED') ORDER BY updated_at ASC")
    suspend fun pendingUploads(): List<MediaItemEntity>

    @Query(
        """
        SELECT * FROM media_items
         WHERE folder_id = :folderId
           AND upload_state != 'UPLOADED'
         ORDER BY message_date DESC
        """
    )
    suspend fun notYetUploadedInFolder(folderId: Long): List<MediaItemEntity>

    // -------------------------------------------------------------- grid feeds

    /** All media in the given folders (already expanded to include descendants). */
    @Query("SELECT * FROM media_items WHERE folder_id IN (:folderIds) ORDER BY message_date DESC")
    fun observeInFolders(folderIds: List<Long>): Flow<List<MediaItemEntity>>

    @Transaction
    @Query("SELECT * FROM media_items WHERE folder_id IN (:folderIds) ORDER BY message_date DESC")
    fun observeCardsInFolders(folderIds: List<Long>): Flow<List<MediaItemWithTags>>

    /**
     * Items carrying **all** of [tagIds] (AND semantics, not OR).
     *
     * @param tagIds non-empty; use [NOTHING] entries only to disable scoping.
     * @param folderIds optional folder scoping; pass `listOf(NOTHING)` for "all".
     * @param tagCount number of tags that must match, normally `tagIds.size`.
     */
    @androidx.room.Transaction
    @Query(
        """
        SELECT m.* FROM media_items m
          INNER JOIN media_item_tags c ON c.item_id = m.id
         WHERE c.tag_id IN (:tagIds)
           AND m.folder_id IN (:folderIds)
         GROUP BY m.id
        HAVING COUNT(DISTINCT c.tag_id) = :tagCount
         ORDER BY m.message_date DESC
        """
    )
    fun observeCardsByAllTags(
        tagIds: List<Long>,
        tagCount: Int,
        folderIds: List<Long>
    ): Flow<List<MediaItemWithTags>>

    /** Free-text search over file name, caption and Telegram coordinates. */
    @Query(
        """
        SELECT * FROM media_items
         WHERE file_name LIKE '%' || :query || '%' COLLATE NOCASE
            OR caption_text LIKE '%' || :query || '%' COLLATE NOCASE
            OR remote_id LIKE '%' || :query || '%'
         ORDER BY message_date DESC
         LIMIT :limit
        """
    )
    fun search(query: String, limit: Int): Flow<List<MediaItemEntity>>

    @Transaction
    @Query("SELECT * FROM media_items WHERE id = :itemId")
    fun observeWithTags(itemId: Long): Flow<MediaItemWithTags?>

    // ------------------------------------------------------------- reel feeds

    @Query(
        """
        SELECT * FROM media_items
         WHERE media_type = :kind
           AND duration_ms > 0
           AND folder_id IN (:folderIds)
         ORDER BY message_date DESC
        """
    )
    fun observeReels(kind: MediaKind, folderIds: List<Long>): Flow<List<MediaItemEntity>>

    @Query(
        """
        SELECT * FROM media_items
         WHERE media_type IN ('VIDEO', 'ANIMATION')
           AND duration_ms BETWEEN :minMs AND :maxMs
         ORDER BY message_date DESC
        """
    )
    fun observeReelsInDurationRange(minMs: Long, maxMs: Long): Flow<List<MediaItemEntity>>

    // ------------------------------------------------------------- statistics

    @Query("SELECT COUNT(*) FROM media_items")
    fun observeTotalCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM media_items WHERE media_type IN ('VIDEO', 'ANIMATION')")
    fun observeVideoCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM media_items WHERE folder_id = :folderId")
    fun observeCountInFolder(folderId: Long): Flow<Int>

    @Query("SELECT COALESCE(SUM(file_size), 0) FROM media_items WHERE is_downloaded = 1")
    fun observeLocalDiskUsage(): Flow<Long>
}
