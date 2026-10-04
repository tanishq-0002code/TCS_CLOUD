package com.example.telegramcloudgallery.data.db

import android.content.Context
import androidx.core.database.use
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.telegramcloudgallery.data.db.entity.MediaFolderEntity
import com.example.telegramcloudgallery.data.db.entity.MediaItemEntity
import com.example.telegramcloudgallery.data.db.entity.MediaItemTagCrossRef
import com.example.telegramcloudgallery.data.db.entity.MediaTagEntity

/**
 * The local, offline source of truth for the gallery.
 *
 * Four tables:
 *  * [MediaFolderEntity]     — virtual folder **tree** (parent/child) plus the
 *                             Telegram Chat Folder / sub-channel mapping.
 *  * [MediaTagEntity]        — flat user tags (`#Vacation`, `#Reels`, …).
 *  * [MediaItemEntity]       — the media index itself.
 *  * [MediaItemTagCrossRef]  — junction enabling *multi-tag* filtering.
 *
 * TDLib keeps its own encrypted database separately; this database is a derived
 * index that can always be rebuilt by re-walking `GetChatHistory`.
 */
@Database(
    entities = [
        MediaFolderEntity::class,
        MediaTagEntity::class,
        MediaItemEntity::class,
        MediaItemTagCrossRef::class
    ],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun folderDao(): FolderDao
    abstract fun tagDao(): TagDao
    abstract fun mediaDao(): MediaDao

    companion object {
        const val DB_NAME = "telegram_cloud_gallery.db"

        /** Well-known folder names seeded on first launch. */
        const val ROOT_ALL_MEDIA = "All Media"
        const val ROOT_LOCAL = "On This Device"
        const val ROOT_REELS = "Reels"

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: build(context.applicationContext).also { instance = it }
            }

        private fun build(context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME)
                .addCallback(SeedCallback)
                // Schema v1 — destructive fallback keeps early development painless.
                // Replace with explicit Migration objects before shipping.
                .fallbackToDestructiveMigration()
                .build()
    }

    /**
     * Seeds a starter hierarchy plus the three tags named in the product spec,
     * so the very first launch renders a usable gallery instead of an empty grid.
     */
    private object SeedCallback : Callback() {

        private val DEFAULT_TAGS = listOf(
            "Vacation" to 0xFF00B0F4.toInt(),
            "Documents" to 0xFFFF6D00.toInt(),
            "Reels" to 0xFF6750A4.toInt()
        )

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            val now = System.currentTimeMillis()

            // --- folder tree -----------------------------------------------------
            insertFolder(db, ROOT_ALL_MEDIA, parentId = null, emoji = "🗂️", now = now, sortOrder = 0)
            insertFolder(db, ROOT_REELS, parentId = null, emoji = "🎬", now = now, sortOrder = 1)
            val localId = insertFolder(
                db, ROOT_LOCAL, parentId = null, emoji = "📱", now = now, sortOrder = 2
            )
            // Nesting demo: everything the sync engine pushes to Telegram lives here.
            insertFolder(
                db, "Auto Backup", parentId = localId, emoji = "☁️",
                now = now, sortOrder = 0, autoSync = true
            )

            // --- starter tags ----------------------------------------------------
            DEFAULT_TAGS.forEachIndexed { index, (name, color) ->
                db.execSQL(
                    "INSERT OR IGNORE INTO media_tags (name, color_argb, created_at) VALUES (?, ?, ?)",
                    arrayOf<Any?>(name, color, now + index)
                )
            }
        }

        private fun insertFolder(
            db: SupportSQLiteDatabase,
            name: String,
            parentId: Long?,
            emoji: String,
            now: Long,
            sortOrder: Int,
            autoSync: Boolean = false
        ): Long {
            db.execSQL(
                """
                INSERT INTO media_folders
                    (name, parent_id, emoji, color_argb, sort_order, include_subfolders,
                     telegram_chat_id, telegram_chat_folder_id, auto_tag_rule, auto_sync,
                     created_at, updated_at)
                VALUES (?, ?, ?, ?, ?, 1, 0, 0, NULL, ?, ?, ?)
                """.trimIndent(),
                arrayOf<Any?>(name, parentId, emoji, 0, sortOrder, if (autoSync) 1 else 0, now, now)
            )
            return db.query("SELECT last_insert_rowid()").use { cursor ->
                cursor.moveToFirst()
                cursor.getLong(0)
            }
        }
    }
}