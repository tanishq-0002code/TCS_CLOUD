package com.example.telegramcloudgallery.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.telegramcloudgallery.data.db.entity.MediaFolderEntity
import com.example.telegramcloudgallery.data.db.relation.MediaFolderWithCount
import kotlinx.coroutines.flow.Flow

/**
 * CRUD + recursive tree traversal for virtual folders.
 *
 * The DAO is deliberately "dumb": all business rules (cycle prevention, name
 * uniqueness, multi-table transactions) live in the repository layer so they
 * stay unit-testable without Room.
 *
 * Note on moving a folder: re-parenting [folderId] to a new parent automatically
 * moves the *whole subtree*, because the descendant links live on the children
 * and never need rewriting. Only [move] has to be called.
 */
@Dao
interface FolderDao {

    // ------------------------------------------------------------------ write

    @Insert
    suspend fun insert(folder: MediaFolderEntity): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAll(folders: List<MediaFolderEntity>): List<Long>

    @Update
    suspend fun update(folder: MediaFolderEntity)

    @Delete
    suspend fun delete(folder: MediaFolderEntity)

    /**
     * Deletes a folder and — thanks to `ON DELETE CASCADE` on `parent_id` —
     * every descendant. Media items keep their rows and get `folder_id = NULL`.
     */
    @Query("DELETE FROM media_folders WHERE id = :folderId")
    suspend fun deleteSubtree(folderId: Long)

    @Query("UPDATE media_folders SET name = :name, updated_at = :now WHERE id = :folderId")
    suspend fun rename(folderId: Long, name: String, now: Long)

    @Query("UPDATE media_folders SET parent_id = :newParentId, updated_at = :now WHERE id = :folderId")
    suspend fun move(folderId: Long, newParentId: Long?, now: Long)

    /** Dual mapping: bind a virtual folder to a Telegram chat and/or Chat Folder. */
    @Query(
        """
        UPDATE media_folders
           SET telegram_chat_id = :chatId,
               telegram_chat_folder_id = :chatFolderId,
               updated_at = :now
         WHERE id = :folderId
        """
    )
    suspend fun mapToTelegram(folderId: Long, chatId: Long, chatFolderId: Int, now: Long)

    @Query("UPDATE media_folders SET auto_sync = :enabled, updated_at = :now WHERE id = :folderId")
    suspend fun setAutoSync(folderId: Long, enabled: Boolean, now: Long)

    @Query("UPDATE media_folders SET auto_tag_rule = :rule, updated_at = :now WHERE id = :folderId")
    suspend fun setAutoTagRule(folderId: Long, rule: String?, now: Long)

    @Query("UPDATE media_folders SET sort_order = :sortOrder WHERE id = :folderId")
    suspend fun updateSortOrder(folderId: Long, sortOrder: Int)

    /** Re-points media that previously lived in [fromFolderId]. */
    @Query("UPDATE media_items SET folder_id = :toFolderId, updated_at = :now WHERE folder_id = :fromFolderId")
    suspend fun reassignItems(fromFolderId: Long, toFolderId: Long?, now: Long)

    // ------------------------------------------------------------------- read

    @Query("SELECT * FROM media_folders ORDER BY sort_order ASC, name COLLATE NOCASE ASC")
    fun observeAll(): Flow<List<MediaFolderEntity>>

    @Query("SELECT * FROM media_folders WHERE id = :folderId")
    fun observeById(folderId: Long): Flow<MediaFolderEntity?>

    @Query("SELECT * FROM media_folders WHERE id = :folderId")
    suspend fun findById(folderId: Long): MediaFolderEntity?

    @Query("SELECT * FROM media_folders WHERE id IN (:folderIds)")
    fun observeByIds(folderIds: List<Long>): Flow<List<MediaFolderEntity>>

    @Query("SELECT * FROM media_folders WHERE parent_id = :parentId ORDER BY sort_order ASC, name COLLATE NOCASE ASC")
    fun observeChildren(parentId: Long?): Flow<List<MediaFolderEntity>>

    @Query("SELECT * FROM media_folders WHERE parent_id IS NULL ORDER BY sort_order ASC, name COLLATE NOCASE ASC")
    fun observeRoots(): Flow<List<MediaFolderEntity>>

    @Query("SELECT * FROM media_folders WHERE name = :name COLLATE NOCASE AND parent_id IS :parentId LIMIT 1")
    suspend fun findByName(name: String, parentId: Long?): MediaFolderEntity?

    /** Direct-child counters so folder chips can show a badge cheaply. */
    @Query(
        """
        SELECT f.*,
               (SELECT COUNT(*) FROM media_items m WHERE m.folder_id = f.id) AS item_count,
               (SELECT COUNT(*) FROM media_items m
                 WHERE m.folder_id = f.id
                   AND m.media_type IN ('VIDEO', 'ANIMATION')) AS video_count
          FROM media_folders f
         ORDER BY f.sort_order ASC, f.name COLLATE NOCASE ASC
        """
    )
    fun observeAllWithCounts(): Flow<List<MediaFolderWithCount>>

    // ------------------------------------------------------- recursive helpers

    /** Self + every transitive child, via a recursive CTE. */
    @Query(
        """
        WITH RECURSIVE subtree(id) AS (
            SELECT :rootId
            UNION
            SELECT f.id
              FROM media_folders f
              INNER JOIN subtree s ON f.parent_id = s.id
        )
        SELECT * FROM media_folders
         WHERE id IN (SELECT id FROM subtree)
         ORDER BY sort_order ASC, name COLLATE NOCASE ASC
        """
    )
    fun observeSubtree(rootId: Long): Flow<List<MediaFolderEntity>>

    @Query(
        """
        WITH RECURSIVE subtree(id) AS (
            SELECT :rootId
            UNION
            SELECT f.id
              FROM media_folders f
              INNER JOIN subtree s ON f.parent_id = s.id
        )
        SELECT id FROM subtree
        """
    )
    suspend fun descendantIds(rootId: Long): List<Long>

    /** Ancestor chain, root first, excluding [folderId] itself. */
    @Query(
        """
        WITH RECURSIVE chain(id, parent_id, depth) AS (
            SELECT id, parent_id, 0 FROM media_folders WHERE id = :folderId
            UNION ALL
            SELECT f.id, f.parent_id, chain.depth + 1
              FROM media_folders f
              INNER JOIN chain ON f.id = chain.parent_id
        )
        SELECT id FROM chain WHERE depth > 0 ORDER BY depth DESC
        """
    )
    suspend fun ancestorIds(folderId: Long): List<Long>

    /** How deep a folder sits; a root folder returns `0`. */
    @Query(
        """
        WITH RECURSIVE chain(id, parent_id, depth) AS (
            SELECT id, parent_id, 0 FROM media_folders WHERE id = :folderId
            UNION ALL
            SELECT f.id, f.parent_id, chain.depth + 1
              FROM media_folders f
              INNER JOIN chain ON f.id = chain.parent_id
        )
        SELECT COUNT(*) - 1 FROM chain
        """
    )
    suspend fun depthOf(folderId: Long): Int

    // -------------------------------------------------- sync / telegram queries

    /** Folders `FolderSyncWorker` must push to Telegram. */
    @Query(
        """
        SELECT * FROM media_folders
         WHERE auto_sync = 1
           AND telegram_chat_id != 0
         ORDER BY sort_order ASC
        """
    )
    suspend fun foldersNeedingSync(): List<MediaFolderEntity>

    @Query("SELECT * FROM media_folders WHERE telegram_chat_folder_id != 0")
    fun observeTelegramMappedFolders(): Flow<List<MediaFolderEntity>>

    @Query("SELECT COALESCE(MAX(sort_order), -1) FROM media_folders WHERE parent_id IS :parentId")
    suspend fun maxSortOrderUnder(parentId: Long?): Int

    @Query("SELECT COUNT(*) FROM media_folders")
    suspend fun count(): Int
}