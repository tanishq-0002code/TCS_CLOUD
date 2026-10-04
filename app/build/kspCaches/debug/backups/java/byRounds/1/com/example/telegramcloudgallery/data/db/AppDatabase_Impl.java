package com.example.telegramcloudgallery.data.db;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile FolderDao _folderDao;

  private volatile TagDao _tagDao;

  private volatile MediaDao _mediaDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `media_folders` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `parent_id` INTEGER, `emoji` TEXT, `color_argb` INTEGER NOT NULL, `sort_order` INTEGER NOT NULL, `include_subfolders` INTEGER NOT NULL, `telegram_chat_id` INTEGER NOT NULL, `telegram_chat_folder_id` INTEGER NOT NULL, `auto_tag_rule` TEXT, `auto_sync` INTEGER NOT NULL, `created_at` INTEGER NOT NULL, `updated_at` INTEGER NOT NULL, FOREIGN KEY(`parent_id`) REFERENCES `media_folders`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_media_folders_parent_id` ON `media_folders` (`parent_id`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_media_folders_name` ON `media_folders` (`name`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_media_folders_telegram_chat_id` ON `media_folders` (`telegram_chat_id`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `media_tags` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `color_argb` INTEGER NOT NULL, `created_at` INTEGER NOT NULL)");
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_media_tags_name` ON `media_tags` (`name`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `media_items` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `remote_id` TEXT NOT NULL, `chat_id` INTEGER NOT NULL, `message_id` INTEGER NOT NULL, `folder_id` INTEGER, `media_type` TEXT NOT NULL, `file_name` TEXT, `mime_type` TEXT, `file_size` INTEGER NOT NULL, `duration_ms` INTEGER NOT NULL, `width` INTEGER NOT NULL, `height` INTEGER NOT NULL, `caption_text` TEXT, `caption_entities_json` TEXT, `remote_file_id` INTEGER NOT NULL, `local_file_id` INTEGER NOT NULL, `thumbnail_file_id` INTEGER NOT NULL, `local_path` TEXT, `is_downloaded` INTEGER NOT NULL, `upload_state` TEXT NOT NULL, `upload_progress` INTEGER NOT NULL, `upload_error` TEXT, `local_content_hash` TEXT, `message_date` INTEGER NOT NULL, `created_at` INTEGER NOT NULL, `updated_at` INTEGER NOT NULL, FOREIGN KEY(`folder_id`) REFERENCES `media_folders`(`id`) ON UPDATE NO ACTION ON DELETE SET NULL )");
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_media_items_chat_id_message_id` ON `media_items` (`chat_id`, `message_id`)");
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_media_items_remote_id` ON `media_items` (`remote_id`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_media_items_folder_id` ON `media_items` (`folder_id`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_media_items_media_type` ON `media_items` (`media_type`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_media_items_upload_state` ON `media_items` (`upload_state`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_media_items_message_date` ON `media_items` (`message_date`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_media_items_local_content_hash` ON `media_items` (`local_content_hash`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `media_item_tags` (`item_id` INTEGER NOT NULL, `tag_id` INTEGER NOT NULL, PRIMARY KEY(`item_id`, `tag_id`), FOREIGN KEY(`item_id`) REFERENCES `media_items`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`tag_id`) REFERENCES `media_tags`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_media_item_tags_tag_id` ON `media_item_tags` (`tag_id`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_media_item_tags_item_id` ON `media_item_tags` (`item_id`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '0ef435314721b935e617976bd29cc1f9')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `media_folders`");
        db.execSQL("DROP TABLE IF EXISTS `media_tags`");
        db.execSQL("DROP TABLE IF EXISTS `media_items`");
        db.execSQL("DROP TABLE IF EXISTS `media_item_tags`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsMediaFolders = new HashMap<String, TableInfo.Column>(13);
        _columnsMediaFolders.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaFolders.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaFolders.put("parent_id", new TableInfo.Column("parent_id", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaFolders.put("emoji", new TableInfo.Column("emoji", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaFolders.put("color_argb", new TableInfo.Column("color_argb", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaFolders.put("sort_order", new TableInfo.Column("sort_order", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaFolders.put("include_subfolders", new TableInfo.Column("include_subfolders", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaFolders.put("telegram_chat_id", new TableInfo.Column("telegram_chat_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaFolders.put("telegram_chat_folder_id", new TableInfo.Column("telegram_chat_folder_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaFolders.put("auto_tag_rule", new TableInfo.Column("auto_tag_rule", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaFolders.put("auto_sync", new TableInfo.Column("auto_sync", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaFolders.put("created_at", new TableInfo.Column("created_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaFolders.put("updated_at", new TableInfo.Column("updated_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMediaFolders = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysMediaFolders.add(new TableInfo.ForeignKey("media_folders", "CASCADE", "NO ACTION", Arrays.asList("parent_id"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesMediaFolders = new HashSet<TableInfo.Index>(3);
        _indicesMediaFolders.add(new TableInfo.Index("index_media_folders_parent_id", false, Arrays.asList("parent_id"), Arrays.asList("ASC")));
        _indicesMediaFolders.add(new TableInfo.Index("index_media_folders_name", false, Arrays.asList("name"), Arrays.asList("ASC")));
        _indicesMediaFolders.add(new TableInfo.Index("index_media_folders_telegram_chat_id", false, Arrays.asList("telegram_chat_id"), Arrays.asList("ASC")));
        final TableInfo _infoMediaFolders = new TableInfo("media_folders", _columnsMediaFolders, _foreignKeysMediaFolders, _indicesMediaFolders);
        final TableInfo _existingMediaFolders = TableInfo.read(db, "media_folders");
        if (!_infoMediaFolders.equals(_existingMediaFolders)) {
          return new RoomOpenHelper.ValidationResult(false, "media_folders(com.example.telegramcloudgallery.data.db.entity.MediaFolderEntity).\n"
                  + " Expected:\n" + _infoMediaFolders + "\n"
                  + " Found:\n" + _existingMediaFolders);
        }
        final HashMap<String, TableInfo.Column> _columnsMediaTags = new HashMap<String, TableInfo.Column>(4);
        _columnsMediaTags.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaTags.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaTags.put("color_argb", new TableInfo.Column("color_argb", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaTags.put("created_at", new TableInfo.Column("created_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMediaTags = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMediaTags = new HashSet<TableInfo.Index>(1);
        _indicesMediaTags.add(new TableInfo.Index("index_media_tags_name", true, Arrays.asList("name"), Arrays.asList("ASC")));
        final TableInfo _infoMediaTags = new TableInfo("media_tags", _columnsMediaTags, _foreignKeysMediaTags, _indicesMediaTags);
        final TableInfo _existingMediaTags = TableInfo.read(db, "media_tags");
        if (!_infoMediaTags.equals(_existingMediaTags)) {
          return new RoomOpenHelper.ValidationResult(false, "media_tags(com.example.telegramcloudgallery.data.db.entity.MediaTagEntity).\n"
                  + " Expected:\n" + _infoMediaTags + "\n"
                  + " Found:\n" + _existingMediaTags);
        }
        final HashMap<String, TableInfo.Column> _columnsMediaItems = new HashMap<String, TableInfo.Column>(26);
        _columnsMediaItems.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaItems.put("remote_id", new TableInfo.Column("remote_id", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaItems.put("chat_id", new TableInfo.Column("chat_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaItems.put("message_id", new TableInfo.Column("message_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaItems.put("folder_id", new TableInfo.Column("folder_id", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaItems.put("media_type", new TableInfo.Column("media_type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaItems.put("file_name", new TableInfo.Column("file_name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaItems.put("mime_type", new TableInfo.Column("mime_type", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaItems.put("file_size", new TableInfo.Column("file_size", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaItems.put("duration_ms", new TableInfo.Column("duration_ms", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaItems.put("width", new TableInfo.Column("width", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaItems.put("height", new TableInfo.Column("height", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaItems.put("caption_text", new TableInfo.Column("caption_text", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaItems.put("caption_entities_json", new TableInfo.Column("caption_entities_json", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaItems.put("remote_file_id", new TableInfo.Column("remote_file_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaItems.put("local_file_id", new TableInfo.Column("local_file_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaItems.put("thumbnail_file_id", new TableInfo.Column("thumbnail_file_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaItems.put("local_path", new TableInfo.Column("local_path", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaItems.put("is_downloaded", new TableInfo.Column("is_downloaded", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaItems.put("upload_state", new TableInfo.Column("upload_state", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaItems.put("upload_progress", new TableInfo.Column("upload_progress", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaItems.put("upload_error", new TableInfo.Column("upload_error", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaItems.put("local_content_hash", new TableInfo.Column("local_content_hash", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaItems.put("message_date", new TableInfo.Column("message_date", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaItems.put("created_at", new TableInfo.Column("created_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaItems.put("updated_at", new TableInfo.Column("updated_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMediaItems = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysMediaItems.add(new TableInfo.ForeignKey("media_folders", "SET NULL", "NO ACTION", Arrays.asList("folder_id"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesMediaItems = new HashSet<TableInfo.Index>(7);
        _indicesMediaItems.add(new TableInfo.Index("index_media_items_chat_id_message_id", true, Arrays.asList("chat_id", "message_id"), Arrays.asList("ASC", "ASC")));
        _indicesMediaItems.add(new TableInfo.Index("index_media_items_remote_id", true, Arrays.asList("remote_id"), Arrays.asList("ASC")));
        _indicesMediaItems.add(new TableInfo.Index("index_media_items_folder_id", false, Arrays.asList("folder_id"), Arrays.asList("ASC")));
        _indicesMediaItems.add(new TableInfo.Index("index_media_items_media_type", false, Arrays.asList("media_type"), Arrays.asList("ASC")));
        _indicesMediaItems.add(new TableInfo.Index("index_media_items_upload_state", false, Arrays.asList("upload_state"), Arrays.asList("ASC")));
        _indicesMediaItems.add(new TableInfo.Index("index_media_items_message_date", false, Arrays.asList("message_date"), Arrays.asList("ASC")));
        _indicesMediaItems.add(new TableInfo.Index("index_media_items_local_content_hash", false, Arrays.asList("local_content_hash"), Arrays.asList("ASC")));
        final TableInfo _infoMediaItems = new TableInfo("media_items", _columnsMediaItems, _foreignKeysMediaItems, _indicesMediaItems);
        final TableInfo _existingMediaItems = TableInfo.read(db, "media_items");
        if (!_infoMediaItems.equals(_existingMediaItems)) {
          return new RoomOpenHelper.ValidationResult(false, "media_items(com.example.telegramcloudgallery.data.db.entity.MediaItemEntity).\n"
                  + " Expected:\n" + _infoMediaItems + "\n"
                  + " Found:\n" + _existingMediaItems);
        }
        final HashMap<String, TableInfo.Column> _columnsMediaItemTags = new HashMap<String, TableInfo.Column>(2);
        _columnsMediaItemTags.put("item_id", new TableInfo.Column("item_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaItemTags.put("tag_id", new TableInfo.Column("tag_id", "INTEGER", true, 2, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMediaItemTags = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysMediaItemTags.add(new TableInfo.ForeignKey("media_items", "CASCADE", "NO ACTION", Arrays.asList("item_id"), Arrays.asList("id")));
        _foreignKeysMediaItemTags.add(new TableInfo.ForeignKey("media_tags", "CASCADE", "NO ACTION", Arrays.asList("tag_id"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesMediaItemTags = new HashSet<TableInfo.Index>(2);
        _indicesMediaItemTags.add(new TableInfo.Index("index_media_item_tags_tag_id", false, Arrays.asList("tag_id"), Arrays.asList("ASC")));
        _indicesMediaItemTags.add(new TableInfo.Index("index_media_item_tags_item_id", false, Arrays.asList("item_id"), Arrays.asList("ASC")));
        final TableInfo _infoMediaItemTags = new TableInfo("media_item_tags", _columnsMediaItemTags, _foreignKeysMediaItemTags, _indicesMediaItemTags);
        final TableInfo _existingMediaItemTags = TableInfo.read(db, "media_item_tags");
        if (!_infoMediaItemTags.equals(_existingMediaItemTags)) {
          return new RoomOpenHelper.ValidationResult(false, "media_item_tags(com.example.telegramcloudgallery.data.db.entity.MediaItemTagCrossRef).\n"
                  + " Expected:\n" + _infoMediaItemTags + "\n"
                  + " Found:\n" + _existingMediaItemTags);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "0ef435314721b935e617976bd29cc1f9", "2a210a5e680c8a13000f1fa8bac1abbb");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "media_folders","media_tags","media_items","media_item_tags");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `media_folders`");
      _db.execSQL("DELETE FROM `media_tags`");
      _db.execSQL("DELETE FROM `media_items`");
      _db.execSQL("DELETE FROM `media_item_tags`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(FolderDao.class, FolderDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(TagDao.class, TagDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(MediaDao.class, MediaDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public FolderDao folderDao() {
    if (_folderDao != null) {
      return _folderDao;
    } else {
      synchronized(this) {
        if(_folderDao == null) {
          _folderDao = new FolderDao_Impl(this);
        }
        return _folderDao;
      }
    }
  }

  @Override
  public TagDao tagDao() {
    if (_tagDao != null) {
      return _tagDao;
    } else {
      synchronized(this) {
        if(_tagDao == null) {
          _tagDao = new TagDao_Impl(this);
        }
        return _tagDao;
      }
    }
  }

  @Override
  public MediaDao mediaDao() {
    if (_mediaDao != null) {
      return _mediaDao;
    } else {
      synchronized(this) {
        if(_mediaDao == null) {
          _mediaDao = new MediaDao_Impl(this);
        }
        return _mediaDao;
      }
    }
  }
}
