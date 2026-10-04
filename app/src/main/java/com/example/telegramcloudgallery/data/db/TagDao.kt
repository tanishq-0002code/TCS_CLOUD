package com.example.telegramcloudgallery.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.telegramcloudgallery.data.db.entity.MediaItemTagCrossRef
import com.example.telegramcloudgallery.data.db.entity.MediaTagEntity
import com.example.telegramcloudgallery.data.db.relation.MediaTagWithCount
import kotlinx.coroutines.flow.Flow

/**
 * Tag management plus the item↔tag junction operations that power the
 * "media matching multiple tags" filter in `GalleryScreen`.
 */
@Dao
interface TagDao {

    // ------------------------------------------------------------------ write

    /** Returns the new row id, or `-1` when the unique name already existed. */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(tag: MediaTagEntity): Long

    @Update
    suspend fun update(tag: MediaTagEntity)

    @Query("DELETE FROM media_tags WHERE id = :tagId")
    suspend fun deleteById(tagId: Long)

    @Query("UPDATE media_tags SET color_argb = :colorArgb WHERE id = :tagId")
    suspend fun setColor(tagId: Long, colorArgb: Int)

    /** Junction insert — `IGNORE` makes tagging idempotent. */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun link(ref: MediaItemTagCrossRef)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun linkAll(refs: List<MediaItemTagCrossRef>)

    @Query("DELETE FROM media_item_tags WHERE item_id = :itemId AND tag_id = :tagId")
    suspend fun unlink(itemId: Long, tagId: Long)

    @Query("DELETE FROM media_item_tags WHERE item_id = :itemId")
    suspend fun clearForItem(itemId: Long)

    // ------------------------------------------------------------------- read

    @Query("SELECT * FROM media_tags ORDER BY name COLLATE NOCASE ASC")
    fun observeAll(): Flow<List<MediaTagEntity>>

    /** Alphabetical tags plus their usage count — feeds `TagChipGroup`. */
    @Query(
        """
        SELECT t.*,
               (SELECT COUNT(*) FROM media_item_tags c WHERE c.tag_id = t.id) AS usage_count
          FROM media_tags t
         ORDER BY t.name COLLATE NOCASE ASC
        """
    )
    fun observeAllWithCounts(): Flow<List<MediaTagWithCount>>

    @Query("SELECT * FROM media_tags WHERE id = :tagId")
    suspend fun findById(tagId: Long): MediaTagEntity?

    @Query("SELECT * FROM media_tags WHERE id IN (:tagIds)")
    fun observeByIds(tagIds: List<Long>): Flow<List<MediaTagEntity>>

    @Query("SELECT * FROM media_tags WHERE name = :name COLLATE NOCASE LIMIT 1")
    suspend fun findByName(name: String): MediaTagEntity?

    @Query(
        """
        SELECT * FROM media_tags
         WHERE name LIKE '%' || :query || '%' COLLATE NOCASE
         ORDER BY name COLLATE NOCASE ASC
         LIMIT :limit
        """
    )
    fun search(query: String, limit: Int): Flow<List<MediaTagEntity>>

    @Query("SELECT * FROM media_tags WHERE id IN (:tagIds) ORDER BY name COLLATE NOCASE ASC")
    suspend fun findByIds(tagIds: List<Long>): List<MediaTagEntity>

    @Query("SELECT * FROM media_tags WHERE name IN (:names) COLLATE NOCASE")
    suspend fun findByNames(names: List<String>): List<MediaTagEntity>

    @Query("SELECT * FROM media_tags ORDER BY name COLLATE NOCASE ASC")
    suspend fun getAll(): List<MediaTagEntity>

    // ------------------------------------------------------- per-item queries

    @Query(
        """
        SELECT t.* FROM media_tags t
          INNER JOIN media_item_tags c ON c.tag_id = t.id
         WHERE c.item_id = :itemId
         ORDER BY t.name COLLATE NOCASE ASC
        """
    )
    fun observeForItem(itemId: Long): Flow<List<MediaTagEntity>>

    @Query(
        """
        SELECT c.item_id FROM media_item_tags c
         WHERE c.tag_id = :tagId
        """
    )
    suspend fun itemIdsForTag(tagId: Long): List<Long>

    /** Number of media items carrying **all** of [tagIds] (multi-tag AND). */
    @Query(
        """
        SELECT COUNT(*) FROM (
            SELECT c.item_id
              FROM media_item_tags c
             WHERE c.tag_id IN (:tagIds)
             GROUP BY c.item_id
            HAVING COUNT(DISTINCT c.tag_id) = :requiredCount
        )
        """
    )
    suspend fun countItemsWithAllTags(tagIds: List<Long>, requiredCount: Int): Int

    @Query("SELECT COUNT(*) FROM media_item_tags WHERE tag_id = :tagId")
    fun observeUsageCount(tagId: Long): Flow<Int>

    @Query("SELECT COUNT(*) FROM media_tags")
    suspend fun count(): Int
}