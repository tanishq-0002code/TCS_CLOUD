package com.example.telegramcloudgallery.data.db;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.telegramcloudgallery.data.db.entity.MediaFolderEntity;
import com.example.telegramcloudgallery.data.db.relation.MediaFolderWithCount;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class FolderDao_Impl implements FolderDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<MediaFolderEntity> __insertionAdapterOfMediaFolderEntity;

  private final EntityDeletionOrUpdateAdapter<MediaFolderEntity> __deletionAdapterOfMediaFolderEntity;

  private final EntityDeletionOrUpdateAdapter<MediaFolderEntity> __updateAdapterOfMediaFolderEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteSubtree;

  private final SharedSQLiteStatement __preparedStmtOfRename;

  private final SharedSQLiteStatement __preparedStmtOfMove;

  private final SharedSQLiteStatement __preparedStmtOfMapToTelegram;

  private final SharedSQLiteStatement __preparedStmtOfSetAutoSync;

  private final SharedSQLiteStatement __preparedStmtOfSetAutoTagRule;

  private final SharedSQLiteStatement __preparedStmtOfUpdateSortOrder;

  private final SharedSQLiteStatement __preparedStmtOfReassignItems;

  public FolderDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMediaFolderEntity = new EntityInsertionAdapter<MediaFolderEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `media_folders` (`id`,`name`,`parent_id`,`emoji`,`color_argb`,`sort_order`,`include_subfolders`,`telegram_chat_id`,`telegram_chat_folder_id`,`auto_tag_rule`,`auto_sync`,`created_at`,`updated_at`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MediaFolderEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        if (entity.getParentId() == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, entity.getParentId());
        }
        if (entity.getEmoji() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getEmoji());
        }
        statement.bindLong(5, entity.getColorArgb());
        statement.bindLong(6, entity.getSortOrder());
        final int _tmp = entity.getIncludeSubfolders() ? 1 : 0;
        statement.bindLong(7, _tmp);
        statement.bindLong(8, entity.getTelegramChatId());
        statement.bindLong(9, entity.getTelegramChatFolderId());
        if (entity.getAutoTagRule() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getAutoTagRule());
        }
        final int _tmp_1 = entity.getAutoSync() ? 1 : 0;
        statement.bindLong(11, _tmp_1);
        statement.bindLong(12, entity.getCreatedAt());
        statement.bindLong(13, entity.getUpdatedAt());
      }
    };
    this.__deletionAdapterOfMediaFolderEntity = new EntityDeletionOrUpdateAdapter<MediaFolderEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `media_folders` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MediaFolderEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfMediaFolderEntity = new EntityDeletionOrUpdateAdapter<MediaFolderEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `media_folders` SET `id` = ?,`name` = ?,`parent_id` = ?,`emoji` = ?,`color_argb` = ?,`sort_order` = ?,`include_subfolders` = ?,`telegram_chat_id` = ?,`telegram_chat_folder_id` = ?,`auto_tag_rule` = ?,`auto_sync` = ?,`created_at` = ?,`updated_at` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MediaFolderEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        if (entity.getParentId() == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, entity.getParentId());
        }
        if (entity.getEmoji() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getEmoji());
        }
        statement.bindLong(5, entity.getColorArgb());
        statement.bindLong(6, entity.getSortOrder());
        final int _tmp = entity.getIncludeSubfolders() ? 1 : 0;
        statement.bindLong(7, _tmp);
        statement.bindLong(8, entity.getTelegramChatId());
        statement.bindLong(9, entity.getTelegramChatFolderId());
        if (entity.getAutoTagRule() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getAutoTagRule());
        }
        final int _tmp_1 = entity.getAutoSync() ? 1 : 0;
        statement.bindLong(11, _tmp_1);
        statement.bindLong(12, entity.getCreatedAt());
        statement.bindLong(13, entity.getUpdatedAt());
        statement.bindLong(14, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteSubtree = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM media_folders WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfRename = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE media_folders SET name = ?, updated_at = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfMove = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE media_folders SET parent_id = ?, updated_at = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfMapToTelegram = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "\n"
                + "        UPDATE media_folders\n"
                + "           SET telegram_chat_id = ?,\n"
                + "               telegram_chat_folder_id = ?,\n"
                + "               updated_at = ?\n"
                + "         WHERE id = ?\n"
                + "        ";
        return _query;
      }
    };
    this.__preparedStmtOfSetAutoSync = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE media_folders SET auto_sync = ?, updated_at = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfSetAutoTagRule = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE media_folders SET auto_tag_rule = ?, updated_at = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateSortOrder = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE media_folders SET sort_order = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfReassignItems = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE media_items SET folder_id = ?, updated_at = ? WHERE folder_id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final MediaFolderEntity folder,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfMediaFolderEntity.insertAndReturnId(folder);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAll(final List<MediaFolderEntity> folders,
      final Continuation<? super List<Long>> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<List<Long>>() {
      @Override
      @NonNull
      public List<Long> call() throws Exception {
        __db.beginTransaction();
        try {
          final List<Long> _result = __insertionAdapterOfMediaFolderEntity.insertAndReturnIdsList(folders);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final MediaFolderEntity folder,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfMediaFolderEntity.handle(folder);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final MediaFolderEntity folder,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfMediaFolderEntity.handle(folder);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteSubtree(final long folderId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteSubtree.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, folderId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteSubtree.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object rename(final long folderId, final String name, final long now,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfRename.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, name);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, now);
        _argIndex = 3;
        _stmt.bindLong(_argIndex, folderId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfRename.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object move(final long folderId, final Long newParentId, final long now,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfMove.acquire();
        int _argIndex = 1;
        if (newParentId == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindLong(_argIndex, newParentId);
        }
        _argIndex = 2;
        _stmt.bindLong(_argIndex, now);
        _argIndex = 3;
        _stmt.bindLong(_argIndex, folderId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfMove.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object mapToTelegram(final long folderId, final long chatId, final int chatFolderId,
      final long now, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfMapToTelegram.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, chatId);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, chatFolderId);
        _argIndex = 3;
        _stmt.bindLong(_argIndex, now);
        _argIndex = 4;
        _stmt.bindLong(_argIndex, folderId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfMapToTelegram.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object setAutoSync(final long folderId, final boolean enabled, final long now,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSetAutoSync.acquire();
        int _argIndex = 1;
        final int _tmp = enabled ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, now);
        _argIndex = 3;
        _stmt.bindLong(_argIndex, folderId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfSetAutoSync.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object setAutoTagRule(final long folderId, final String rule, final long now,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSetAutoTagRule.acquire();
        int _argIndex = 1;
        if (rule == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, rule);
        }
        _argIndex = 2;
        _stmt.bindLong(_argIndex, now);
        _argIndex = 3;
        _stmt.bindLong(_argIndex, folderId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfSetAutoTagRule.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateSortOrder(final long folderId, final int sortOrder,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateSortOrder.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, sortOrder);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, folderId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateSortOrder.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object reassignItems(final long fromFolderId, final Long toFolderId, final long now,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfReassignItems.acquire();
        int _argIndex = 1;
        if (toFolderId == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindLong(_argIndex, toFolderId);
        }
        _argIndex = 2;
        _stmt.bindLong(_argIndex, now);
        _argIndex = 3;
        _stmt.bindLong(_argIndex, fromFolderId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfReassignItems.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<MediaFolderEntity>> observeAll() {
    final String _sql = "SELECT * FROM media_folders ORDER BY sort_order ASC, name COLLATE NOCASE ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"media_folders"}, new Callable<List<MediaFolderEntity>>() {
      @Override
      @NonNull
      public List<MediaFolderEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfParentId = CursorUtil.getColumnIndexOrThrow(_cursor, "parent_id");
          final int _cursorIndexOfEmoji = CursorUtil.getColumnIndexOrThrow(_cursor, "emoji");
          final int _cursorIndexOfColorArgb = CursorUtil.getColumnIndexOrThrow(_cursor, "color_argb");
          final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sort_order");
          final int _cursorIndexOfIncludeSubfolders = CursorUtil.getColumnIndexOrThrow(_cursor, "include_subfolders");
          final int _cursorIndexOfTelegramChatId = CursorUtil.getColumnIndexOrThrow(_cursor, "telegram_chat_id");
          final int _cursorIndexOfTelegramChatFolderId = CursorUtil.getColumnIndexOrThrow(_cursor, "telegram_chat_folder_id");
          final int _cursorIndexOfAutoTagRule = CursorUtil.getColumnIndexOrThrow(_cursor, "auto_tag_rule");
          final int _cursorIndexOfAutoSync = CursorUtil.getColumnIndexOrThrow(_cursor, "auto_sync");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final List<MediaFolderEntity> _result = new ArrayList<MediaFolderEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MediaFolderEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final Long _tmpParentId;
            if (_cursor.isNull(_cursorIndexOfParentId)) {
              _tmpParentId = null;
            } else {
              _tmpParentId = _cursor.getLong(_cursorIndexOfParentId);
            }
            final String _tmpEmoji;
            if (_cursor.isNull(_cursorIndexOfEmoji)) {
              _tmpEmoji = null;
            } else {
              _tmpEmoji = _cursor.getString(_cursorIndexOfEmoji);
            }
            final int _tmpColorArgb;
            _tmpColorArgb = _cursor.getInt(_cursorIndexOfColorArgb);
            final int _tmpSortOrder;
            _tmpSortOrder = _cursor.getInt(_cursorIndexOfSortOrder);
            final boolean _tmpIncludeSubfolders;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIncludeSubfolders);
            _tmpIncludeSubfolders = _tmp != 0;
            final long _tmpTelegramChatId;
            _tmpTelegramChatId = _cursor.getLong(_cursorIndexOfTelegramChatId);
            final int _tmpTelegramChatFolderId;
            _tmpTelegramChatFolderId = _cursor.getInt(_cursorIndexOfTelegramChatFolderId);
            final String _tmpAutoTagRule;
            if (_cursor.isNull(_cursorIndexOfAutoTagRule)) {
              _tmpAutoTagRule = null;
            } else {
              _tmpAutoTagRule = _cursor.getString(_cursorIndexOfAutoTagRule);
            }
            final boolean _tmpAutoSync;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfAutoSync);
            _tmpAutoSync = _tmp_1 != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new MediaFolderEntity(_tmpId,_tmpName,_tmpParentId,_tmpEmoji,_tmpColorArgb,_tmpSortOrder,_tmpIncludeSubfolders,_tmpTelegramChatId,_tmpTelegramChatFolderId,_tmpAutoTagRule,_tmpAutoSync,_tmpCreatedAt,_tmpUpdatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<MediaFolderEntity> observeById(final long folderId) {
    final String _sql = "SELECT * FROM media_folders WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, folderId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"media_folders"}, new Callable<MediaFolderEntity>() {
      @Override
      @Nullable
      public MediaFolderEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfParentId = CursorUtil.getColumnIndexOrThrow(_cursor, "parent_id");
          final int _cursorIndexOfEmoji = CursorUtil.getColumnIndexOrThrow(_cursor, "emoji");
          final int _cursorIndexOfColorArgb = CursorUtil.getColumnIndexOrThrow(_cursor, "color_argb");
          final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sort_order");
          final int _cursorIndexOfIncludeSubfolders = CursorUtil.getColumnIndexOrThrow(_cursor, "include_subfolders");
          final int _cursorIndexOfTelegramChatId = CursorUtil.getColumnIndexOrThrow(_cursor, "telegram_chat_id");
          final int _cursorIndexOfTelegramChatFolderId = CursorUtil.getColumnIndexOrThrow(_cursor, "telegram_chat_folder_id");
          final int _cursorIndexOfAutoTagRule = CursorUtil.getColumnIndexOrThrow(_cursor, "auto_tag_rule");
          final int _cursorIndexOfAutoSync = CursorUtil.getColumnIndexOrThrow(_cursor, "auto_sync");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final MediaFolderEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final Long _tmpParentId;
            if (_cursor.isNull(_cursorIndexOfParentId)) {
              _tmpParentId = null;
            } else {
              _tmpParentId = _cursor.getLong(_cursorIndexOfParentId);
            }
            final String _tmpEmoji;
            if (_cursor.isNull(_cursorIndexOfEmoji)) {
              _tmpEmoji = null;
            } else {
              _tmpEmoji = _cursor.getString(_cursorIndexOfEmoji);
            }
            final int _tmpColorArgb;
            _tmpColorArgb = _cursor.getInt(_cursorIndexOfColorArgb);
            final int _tmpSortOrder;
            _tmpSortOrder = _cursor.getInt(_cursorIndexOfSortOrder);
            final boolean _tmpIncludeSubfolders;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIncludeSubfolders);
            _tmpIncludeSubfolders = _tmp != 0;
            final long _tmpTelegramChatId;
            _tmpTelegramChatId = _cursor.getLong(_cursorIndexOfTelegramChatId);
            final int _tmpTelegramChatFolderId;
            _tmpTelegramChatFolderId = _cursor.getInt(_cursorIndexOfTelegramChatFolderId);
            final String _tmpAutoTagRule;
            if (_cursor.isNull(_cursorIndexOfAutoTagRule)) {
              _tmpAutoTagRule = null;
            } else {
              _tmpAutoTagRule = _cursor.getString(_cursorIndexOfAutoTagRule);
            }
            final boolean _tmpAutoSync;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfAutoSync);
            _tmpAutoSync = _tmp_1 != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new MediaFolderEntity(_tmpId,_tmpName,_tmpParentId,_tmpEmoji,_tmpColorArgb,_tmpSortOrder,_tmpIncludeSubfolders,_tmpTelegramChatId,_tmpTelegramChatFolderId,_tmpAutoTagRule,_tmpAutoSync,_tmpCreatedAt,_tmpUpdatedAt);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object findById(final long folderId,
      final Continuation<? super MediaFolderEntity> $completion) {
    final String _sql = "SELECT * FROM media_folders WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, folderId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<MediaFolderEntity>() {
      @Override
      @Nullable
      public MediaFolderEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfParentId = CursorUtil.getColumnIndexOrThrow(_cursor, "parent_id");
          final int _cursorIndexOfEmoji = CursorUtil.getColumnIndexOrThrow(_cursor, "emoji");
          final int _cursorIndexOfColorArgb = CursorUtil.getColumnIndexOrThrow(_cursor, "color_argb");
          final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sort_order");
          final int _cursorIndexOfIncludeSubfolders = CursorUtil.getColumnIndexOrThrow(_cursor, "include_subfolders");
          final int _cursorIndexOfTelegramChatId = CursorUtil.getColumnIndexOrThrow(_cursor, "telegram_chat_id");
          final int _cursorIndexOfTelegramChatFolderId = CursorUtil.getColumnIndexOrThrow(_cursor, "telegram_chat_folder_id");
          final int _cursorIndexOfAutoTagRule = CursorUtil.getColumnIndexOrThrow(_cursor, "auto_tag_rule");
          final int _cursorIndexOfAutoSync = CursorUtil.getColumnIndexOrThrow(_cursor, "auto_sync");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final MediaFolderEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final Long _tmpParentId;
            if (_cursor.isNull(_cursorIndexOfParentId)) {
              _tmpParentId = null;
            } else {
              _tmpParentId = _cursor.getLong(_cursorIndexOfParentId);
            }
            final String _tmpEmoji;
            if (_cursor.isNull(_cursorIndexOfEmoji)) {
              _tmpEmoji = null;
            } else {
              _tmpEmoji = _cursor.getString(_cursorIndexOfEmoji);
            }
            final int _tmpColorArgb;
            _tmpColorArgb = _cursor.getInt(_cursorIndexOfColorArgb);
            final int _tmpSortOrder;
            _tmpSortOrder = _cursor.getInt(_cursorIndexOfSortOrder);
            final boolean _tmpIncludeSubfolders;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIncludeSubfolders);
            _tmpIncludeSubfolders = _tmp != 0;
            final long _tmpTelegramChatId;
            _tmpTelegramChatId = _cursor.getLong(_cursorIndexOfTelegramChatId);
            final int _tmpTelegramChatFolderId;
            _tmpTelegramChatFolderId = _cursor.getInt(_cursorIndexOfTelegramChatFolderId);
            final String _tmpAutoTagRule;
            if (_cursor.isNull(_cursorIndexOfAutoTagRule)) {
              _tmpAutoTagRule = null;
            } else {
              _tmpAutoTagRule = _cursor.getString(_cursorIndexOfAutoTagRule);
            }
            final boolean _tmpAutoSync;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfAutoSync);
            _tmpAutoSync = _tmp_1 != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new MediaFolderEntity(_tmpId,_tmpName,_tmpParentId,_tmpEmoji,_tmpColorArgb,_tmpSortOrder,_tmpIncludeSubfolders,_tmpTelegramChatId,_tmpTelegramChatFolderId,_tmpAutoTagRule,_tmpAutoSync,_tmpCreatedAt,_tmpUpdatedAt);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<MediaFolderEntity>> observeByIds(final List<Long> folderIds) {
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT * FROM media_folders WHERE id IN (");
    final int _inputSize = folderIds.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (long _item : folderIds) {
      _statement.bindLong(_argIndex, _item);
      _argIndex++;
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"media_folders"}, new Callable<List<MediaFolderEntity>>() {
      @Override
      @NonNull
      public List<MediaFolderEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfParentId = CursorUtil.getColumnIndexOrThrow(_cursor, "parent_id");
          final int _cursorIndexOfEmoji = CursorUtil.getColumnIndexOrThrow(_cursor, "emoji");
          final int _cursorIndexOfColorArgb = CursorUtil.getColumnIndexOrThrow(_cursor, "color_argb");
          final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sort_order");
          final int _cursorIndexOfIncludeSubfolders = CursorUtil.getColumnIndexOrThrow(_cursor, "include_subfolders");
          final int _cursorIndexOfTelegramChatId = CursorUtil.getColumnIndexOrThrow(_cursor, "telegram_chat_id");
          final int _cursorIndexOfTelegramChatFolderId = CursorUtil.getColumnIndexOrThrow(_cursor, "telegram_chat_folder_id");
          final int _cursorIndexOfAutoTagRule = CursorUtil.getColumnIndexOrThrow(_cursor, "auto_tag_rule");
          final int _cursorIndexOfAutoSync = CursorUtil.getColumnIndexOrThrow(_cursor, "auto_sync");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final List<MediaFolderEntity> _result = new ArrayList<MediaFolderEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MediaFolderEntity _item_1;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final Long _tmpParentId;
            if (_cursor.isNull(_cursorIndexOfParentId)) {
              _tmpParentId = null;
            } else {
              _tmpParentId = _cursor.getLong(_cursorIndexOfParentId);
            }
            final String _tmpEmoji;
            if (_cursor.isNull(_cursorIndexOfEmoji)) {
              _tmpEmoji = null;
            } else {
              _tmpEmoji = _cursor.getString(_cursorIndexOfEmoji);
            }
            final int _tmpColorArgb;
            _tmpColorArgb = _cursor.getInt(_cursorIndexOfColorArgb);
            final int _tmpSortOrder;
            _tmpSortOrder = _cursor.getInt(_cursorIndexOfSortOrder);
            final boolean _tmpIncludeSubfolders;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIncludeSubfolders);
            _tmpIncludeSubfolders = _tmp != 0;
            final long _tmpTelegramChatId;
            _tmpTelegramChatId = _cursor.getLong(_cursorIndexOfTelegramChatId);
            final int _tmpTelegramChatFolderId;
            _tmpTelegramChatFolderId = _cursor.getInt(_cursorIndexOfTelegramChatFolderId);
            final String _tmpAutoTagRule;
            if (_cursor.isNull(_cursorIndexOfAutoTagRule)) {
              _tmpAutoTagRule = null;
            } else {
              _tmpAutoTagRule = _cursor.getString(_cursorIndexOfAutoTagRule);
            }
            final boolean _tmpAutoSync;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfAutoSync);
            _tmpAutoSync = _tmp_1 != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item_1 = new MediaFolderEntity(_tmpId,_tmpName,_tmpParentId,_tmpEmoji,_tmpColorArgb,_tmpSortOrder,_tmpIncludeSubfolders,_tmpTelegramChatId,_tmpTelegramChatFolderId,_tmpAutoTagRule,_tmpAutoSync,_tmpCreatedAt,_tmpUpdatedAt);
            _result.add(_item_1);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<MediaFolderEntity>> observeChildren(final Long parentId) {
    final String _sql = "SELECT * FROM media_folders WHERE parent_id = ? ORDER BY sort_order ASC, name COLLATE NOCASE ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (parentId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, parentId);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"media_folders"}, new Callable<List<MediaFolderEntity>>() {
      @Override
      @NonNull
      public List<MediaFolderEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfParentId = CursorUtil.getColumnIndexOrThrow(_cursor, "parent_id");
          final int _cursorIndexOfEmoji = CursorUtil.getColumnIndexOrThrow(_cursor, "emoji");
          final int _cursorIndexOfColorArgb = CursorUtil.getColumnIndexOrThrow(_cursor, "color_argb");
          final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sort_order");
          final int _cursorIndexOfIncludeSubfolders = CursorUtil.getColumnIndexOrThrow(_cursor, "include_subfolders");
          final int _cursorIndexOfTelegramChatId = CursorUtil.getColumnIndexOrThrow(_cursor, "telegram_chat_id");
          final int _cursorIndexOfTelegramChatFolderId = CursorUtil.getColumnIndexOrThrow(_cursor, "telegram_chat_folder_id");
          final int _cursorIndexOfAutoTagRule = CursorUtil.getColumnIndexOrThrow(_cursor, "auto_tag_rule");
          final int _cursorIndexOfAutoSync = CursorUtil.getColumnIndexOrThrow(_cursor, "auto_sync");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final List<MediaFolderEntity> _result = new ArrayList<MediaFolderEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MediaFolderEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final Long _tmpParentId;
            if (_cursor.isNull(_cursorIndexOfParentId)) {
              _tmpParentId = null;
            } else {
              _tmpParentId = _cursor.getLong(_cursorIndexOfParentId);
            }
            final String _tmpEmoji;
            if (_cursor.isNull(_cursorIndexOfEmoji)) {
              _tmpEmoji = null;
            } else {
              _tmpEmoji = _cursor.getString(_cursorIndexOfEmoji);
            }
            final int _tmpColorArgb;
            _tmpColorArgb = _cursor.getInt(_cursorIndexOfColorArgb);
            final int _tmpSortOrder;
            _tmpSortOrder = _cursor.getInt(_cursorIndexOfSortOrder);
            final boolean _tmpIncludeSubfolders;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIncludeSubfolders);
            _tmpIncludeSubfolders = _tmp != 0;
            final long _tmpTelegramChatId;
            _tmpTelegramChatId = _cursor.getLong(_cursorIndexOfTelegramChatId);
            final int _tmpTelegramChatFolderId;
            _tmpTelegramChatFolderId = _cursor.getInt(_cursorIndexOfTelegramChatFolderId);
            final String _tmpAutoTagRule;
            if (_cursor.isNull(_cursorIndexOfAutoTagRule)) {
              _tmpAutoTagRule = null;
            } else {
              _tmpAutoTagRule = _cursor.getString(_cursorIndexOfAutoTagRule);
            }
            final boolean _tmpAutoSync;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfAutoSync);
            _tmpAutoSync = _tmp_1 != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new MediaFolderEntity(_tmpId,_tmpName,_tmpParentId,_tmpEmoji,_tmpColorArgb,_tmpSortOrder,_tmpIncludeSubfolders,_tmpTelegramChatId,_tmpTelegramChatFolderId,_tmpAutoTagRule,_tmpAutoSync,_tmpCreatedAt,_tmpUpdatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<MediaFolderEntity>> observeRoots() {
    final String _sql = "SELECT * FROM media_folders WHERE parent_id IS NULL ORDER BY sort_order ASC, name COLLATE NOCASE ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"media_folders"}, new Callable<List<MediaFolderEntity>>() {
      @Override
      @NonNull
      public List<MediaFolderEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfParentId = CursorUtil.getColumnIndexOrThrow(_cursor, "parent_id");
          final int _cursorIndexOfEmoji = CursorUtil.getColumnIndexOrThrow(_cursor, "emoji");
          final int _cursorIndexOfColorArgb = CursorUtil.getColumnIndexOrThrow(_cursor, "color_argb");
          final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sort_order");
          final int _cursorIndexOfIncludeSubfolders = CursorUtil.getColumnIndexOrThrow(_cursor, "include_subfolders");
          final int _cursorIndexOfTelegramChatId = CursorUtil.getColumnIndexOrThrow(_cursor, "telegram_chat_id");
          final int _cursorIndexOfTelegramChatFolderId = CursorUtil.getColumnIndexOrThrow(_cursor, "telegram_chat_folder_id");
          final int _cursorIndexOfAutoTagRule = CursorUtil.getColumnIndexOrThrow(_cursor, "auto_tag_rule");
          final int _cursorIndexOfAutoSync = CursorUtil.getColumnIndexOrThrow(_cursor, "auto_sync");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final List<MediaFolderEntity> _result = new ArrayList<MediaFolderEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MediaFolderEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final Long _tmpParentId;
            if (_cursor.isNull(_cursorIndexOfParentId)) {
              _tmpParentId = null;
            } else {
              _tmpParentId = _cursor.getLong(_cursorIndexOfParentId);
            }
            final String _tmpEmoji;
            if (_cursor.isNull(_cursorIndexOfEmoji)) {
              _tmpEmoji = null;
            } else {
              _tmpEmoji = _cursor.getString(_cursorIndexOfEmoji);
            }
            final int _tmpColorArgb;
            _tmpColorArgb = _cursor.getInt(_cursorIndexOfColorArgb);
            final int _tmpSortOrder;
            _tmpSortOrder = _cursor.getInt(_cursorIndexOfSortOrder);
            final boolean _tmpIncludeSubfolders;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIncludeSubfolders);
            _tmpIncludeSubfolders = _tmp != 0;
            final long _tmpTelegramChatId;
            _tmpTelegramChatId = _cursor.getLong(_cursorIndexOfTelegramChatId);
            final int _tmpTelegramChatFolderId;
            _tmpTelegramChatFolderId = _cursor.getInt(_cursorIndexOfTelegramChatFolderId);
            final String _tmpAutoTagRule;
            if (_cursor.isNull(_cursorIndexOfAutoTagRule)) {
              _tmpAutoTagRule = null;
            } else {
              _tmpAutoTagRule = _cursor.getString(_cursorIndexOfAutoTagRule);
            }
            final boolean _tmpAutoSync;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfAutoSync);
            _tmpAutoSync = _tmp_1 != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new MediaFolderEntity(_tmpId,_tmpName,_tmpParentId,_tmpEmoji,_tmpColorArgb,_tmpSortOrder,_tmpIncludeSubfolders,_tmpTelegramChatId,_tmpTelegramChatFolderId,_tmpAutoTagRule,_tmpAutoSync,_tmpCreatedAt,_tmpUpdatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object findByName(final String name, final Long parentId,
      final Continuation<? super MediaFolderEntity> $completion) {
    final String _sql = "SELECT * FROM media_folders WHERE name = ? COLLATE NOCASE AND parent_id IS ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, name);
    _argIndex = 2;
    if (parentId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, parentId);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<MediaFolderEntity>() {
      @Override
      @Nullable
      public MediaFolderEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfParentId = CursorUtil.getColumnIndexOrThrow(_cursor, "parent_id");
          final int _cursorIndexOfEmoji = CursorUtil.getColumnIndexOrThrow(_cursor, "emoji");
          final int _cursorIndexOfColorArgb = CursorUtil.getColumnIndexOrThrow(_cursor, "color_argb");
          final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sort_order");
          final int _cursorIndexOfIncludeSubfolders = CursorUtil.getColumnIndexOrThrow(_cursor, "include_subfolders");
          final int _cursorIndexOfTelegramChatId = CursorUtil.getColumnIndexOrThrow(_cursor, "telegram_chat_id");
          final int _cursorIndexOfTelegramChatFolderId = CursorUtil.getColumnIndexOrThrow(_cursor, "telegram_chat_folder_id");
          final int _cursorIndexOfAutoTagRule = CursorUtil.getColumnIndexOrThrow(_cursor, "auto_tag_rule");
          final int _cursorIndexOfAutoSync = CursorUtil.getColumnIndexOrThrow(_cursor, "auto_sync");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final MediaFolderEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final Long _tmpParentId;
            if (_cursor.isNull(_cursorIndexOfParentId)) {
              _tmpParentId = null;
            } else {
              _tmpParentId = _cursor.getLong(_cursorIndexOfParentId);
            }
            final String _tmpEmoji;
            if (_cursor.isNull(_cursorIndexOfEmoji)) {
              _tmpEmoji = null;
            } else {
              _tmpEmoji = _cursor.getString(_cursorIndexOfEmoji);
            }
            final int _tmpColorArgb;
            _tmpColorArgb = _cursor.getInt(_cursorIndexOfColorArgb);
            final int _tmpSortOrder;
            _tmpSortOrder = _cursor.getInt(_cursorIndexOfSortOrder);
            final boolean _tmpIncludeSubfolders;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIncludeSubfolders);
            _tmpIncludeSubfolders = _tmp != 0;
            final long _tmpTelegramChatId;
            _tmpTelegramChatId = _cursor.getLong(_cursorIndexOfTelegramChatId);
            final int _tmpTelegramChatFolderId;
            _tmpTelegramChatFolderId = _cursor.getInt(_cursorIndexOfTelegramChatFolderId);
            final String _tmpAutoTagRule;
            if (_cursor.isNull(_cursorIndexOfAutoTagRule)) {
              _tmpAutoTagRule = null;
            } else {
              _tmpAutoTagRule = _cursor.getString(_cursorIndexOfAutoTagRule);
            }
            final boolean _tmpAutoSync;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfAutoSync);
            _tmpAutoSync = _tmp_1 != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new MediaFolderEntity(_tmpId,_tmpName,_tmpParentId,_tmpEmoji,_tmpColorArgb,_tmpSortOrder,_tmpIncludeSubfolders,_tmpTelegramChatId,_tmpTelegramChatFolderId,_tmpAutoTagRule,_tmpAutoSync,_tmpCreatedAt,_tmpUpdatedAt);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<MediaFolderWithCount>> observeAllWithCounts() {
    final String _sql = "\n"
            + "        SELECT f.*,\n"
            + "               (SELECT COUNT(*) FROM media_items m WHERE m.folder_id = f.id) AS item_count,\n"
            + "               (SELECT COUNT(*) FROM media_items m\n"
            + "                 WHERE m.folder_id = f.id\n"
            + "                   AND m.media_type IN ('VIDEO', 'ANIMATION')) AS video_count\n"
            + "          FROM media_folders f\n"
            + "         ORDER BY f.sort_order ASC, f.name COLLATE NOCASE ASC\n"
            + "        ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"media_items",
        "media_folders"}, new Callable<List<MediaFolderWithCount>>() {
      @Override
      @NonNull
      public List<MediaFolderWithCount> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfParentId = CursorUtil.getColumnIndexOrThrow(_cursor, "parent_id");
          final int _cursorIndexOfEmoji = CursorUtil.getColumnIndexOrThrow(_cursor, "emoji");
          final int _cursorIndexOfColorArgb = CursorUtil.getColumnIndexOrThrow(_cursor, "color_argb");
          final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sort_order");
          final int _cursorIndexOfIncludeSubfolders = CursorUtil.getColumnIndexOrThrow(_cursor, "include_subfolders");
          final int _cursorIndexOfTelegramChatId = CursorUtil.getColumnIndexOrThrow(_cursor, "telegram_chat_id");
          final int _cursorIndexOfTelegramChatFolderId = CursorUtil.getColumnIndexOrThrow(_cursor, "telegram_chat_folder_id");
          final int _cursorIndexOfAutoTagRule = CursorUtil.getColumnIndexOrThrow(_cursor, "auto_tag_rule");
          final int _cursorIndexOfAutoSync = CursorUtil.getColumnIndexOrThrow(_cursor, "auto_sync");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final int _cursorIndexOfItemCount = CursorUtil.getColumnIndexOrThrow(_cursor, "item_count");
          final int _cursorIndexOfVideoCount = CursorUtil.getColumnIndexOrThrow(_cursor, "video_count");
          final List<MediaFolderWithCount> _result = new ArrayList<MediaFolderWithCount>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MediaFolderWithCount _item;
            final int _tmpItemCount;
            _tmpItemCount = _cursor.getInt(_cursorIndexOfItemCount);
            final int _tmpVideoCount;
            _tmpVideoCount = _cursor.getInt(_cursorIndexOfVideoCount);
            final MediaFolderEntity _tmpFolder;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final Long _tmpParentId;
            if (_cursor.isNull(_cursorIndexOfParentId)) {
              _tmpParentId = null;
            } else {
              _tmpParentId = _cursor.getLong(_cursorIndexOfParentId);
            }
            final String _tmpEmoji;
            if (_cursor.isNull(_cursorIndexOfEmoji)) {
              _tmpEmoji = null;
            } else {
              _tmpEmoji = _cursor.getString(_cursorIndexOfEmoji);
            }
            final int _tmpColorArgb;
            _tmpColorArgb = _cursor.getInt(_cursorIndexOfColorArgb);
            final int _tmpSortOrder;
            _tmpSortOrder = _cursor.getInt(_cursorIndexOfSortOrder);
            final boolean _tmpIncludeSubfolders;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIncludeSubfolders);
            _tmpIncludeSubfolders = _tmp != 0;
            final long _tmpTelegramChatId;
            _tmpTelegramChatId = _cursor.getLong(_cursorIndexOfTelegramChatId);
            final int _tmpTelegramChatFolderId;
            _tmpTelegramChatFolderId = _cursor.getInt(_cursorIndexOfTelegramChatFolderId);
            final String _tmpAutoTagRule;
            if (_cursor.isNull(_cursorIndexOfAutoTagRule)) {
              _tmpAutoTagRule = null;
            } else {
              _tmpAutoTagRule = _cursor.getString(_cursorIndexOfAutoTagRule);
            }
            final boolean _tmpAutoSync;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfAutoSync);
            _tmpAutoSync = _tmp_1 != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _tmpFolder = new MediaFolderEntity(_tmpId,_tmpName,_tmpParentId,_tmpEmoji,_tmpColorArgb,_tmpSortOrder,_tmpIncludeSubfolders,_tmpTelegramChatId,_tmpTelegramChatFolderId,_tmpAutoTagRule,_tmpAutoSync,_tmpCreatedAt,_tmpUpdatedAt);
            _item = new MediaFolderWithCount(_tmpFolder,_tmpItemCount,_tmpVideoCount);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<MediaFolderEntity>> observeSubtree(final long rootId) {
    final String _sql = "\n"
            + "        WITH RECURSIVE subtree(id) AS (\n"
            + "            SELECT ?\n"
            + "            UNION\n"
            + "            SELECT f.id\n"
            + "              FROM media_folders f\n"
            + "              INNER JOIN subtree s ON f.parent_id = s.id\n"
            + "        )\n"
            + "        SELECT * FROM media_folders\n"
            + "         WHERE id IN (SELECT id FROM subtree)\n"
            + "         ORDER BY sort_order ASC, name COLLATE NOCASE ASC\n"
            + "        ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, rootId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"media_folders"}, new Callable<List<MediaFolderEntity>>() {
      @Override
      @NonNull
      public List<MediaFolderEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfParentId = CursorUtil.getColumnIndexOrThrow(_cursor, "parent_id");
          final int _cursorIndexOfEmoji = CursorUtil.getColumnIndexOrThrow(_cursor, "emoji");
          final int _cursorIndexOfColorArgb = CursorUtil.getColumnIndexOrThrow(_cursor, "color_argb");
          final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sort_order");
          final int _cursorIndexOfIncludeSubfolders = CursorUtil.getColumnIndexOrThrow(_cursor, "include_subfolders");
          final int _cursorIndexOfTelegramChatId = CursorUtil.getColumnIndexOrThrow(_cursor, "telegram_chat_id");
          final int _cursorIndexOfTelegramChatFolderId = CursorUtil.getColumnIndexOrThrow(_cursor, "telegram_chat_folder_id");
          final int _cursorIndexOfAutoTagRule = CursorUtil.getColumnIndexOrThrow(_cursor, "auto_tag_rule");
          final int _cursorIndexOfAutoSync = CursorUtil.getColumnIndexOrThrow(_cursor, "auto_sync");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final List<MediaFolderEntity> _result = new ArrayList<MediaFolderEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MediaFolderEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final Long _tmpParentId;
            if (_cursor.isNull(_cursorIndexOfParentId)) {
              _tmpParentId = null;
            } else {
              _tmpParentId = _cursor.getLong(_cursorIndexOfParentId);
            }
            final String _tmpEmoji;
            if (_cursor.isNull(_cursorIndexOfEmoji)) {
              _tmpEmoji = null;
            } else {
              _tmpEmoji = _cursor.getString(_cursorIndexOfEmoji);
            }
            final int _tmpColorArgb;
            _tmpColorArgb = _cursor.getInt(_cursorIndexOfColorArgb);
            final int _tmpSortOrder;
            _tmpSortOrder = _cursor.getInt(_cursorIndexOfSortOrder);
            final boolean _tmpIncludeSubfolders;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIncludeSubfolders);
            _tmpIncludeSubfolders = _tmp != 0;
            final long _tmpTelegramChatId;
            _tmpTelegramChatId = _cursor.getLong(_cursorIndexOfTelegramChatId);
            final int _tmpTelegramChatFolderId;
            _tmpTelegramChatFolderId = _cursor.getInt(_cursorIndexOfTelegramChatFolderId);
            final String _tmpAutoTagRule;
            if (_cursor.isNull(_cursorIndexOfAutoTagRule)) {
              _tmpAutoTagRule = null;
            } else {
              _tmpAutoTagRule = _cursor.getString(_cursorIndexOfAutoTagRule);
            }
            final boolean _tmpAutoSync;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfAutoSync);
            _tmpAutoSync = _tmp_1 != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new MediaFolderEntity(_tmpId,_tmpName,_tmpParentId,_tmpEmoji,_tmpColorArgb,_tmpSortOrder,_tmpIncludeSubfolders,_tmpTelegramChatId,_tmpTelegramChatFolderId,_tmpAutoTagRule,_tmpAutoSync,_tmpCreatedAt,_tmpUpdatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object descendantIds(final long rootId,
      final Continuation<? super List<Long>> $completion) {
    final String _sql = "\n"
            + "        WITH RECURSIVE subtree(id) AS (\n"
            + "            SELECT ?\n"
            + "            UNION\n"
            + "            SELECT f.id\n"
            + "              FROM media_folders f\n"
            + "              INNER JOIN subtree s ON f.parent_id = s.id\n"
            + "        )\n"
            + "        SELECT id FROM subtree\n"
            + "        ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, rootId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Long>>() {
      @Override
      @NonNull
      public List<Long> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final List<Long> _result = new ArrayList<Long>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Long _item;
            _item = _cursor.getLong(0);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object ancestorIds(final long folderId,
      final Continuation<? super List<Long>> $completion) {
    final String _sql = "\n"
            + "        WITH RECURSIVE chain(id, parent_id, depth) AS (\n"
            + "            SELECT id, parent_id, 0 FROM media_folders WHERE id = ?\n"
            + "            UNION ALL\n"
            + "            SELECT f.id, f.parent_id, chain.depth + 1\n"
            + "              FROM media_folders f\n"
            + "              INNER JOIN chain ON f.id = chain.parent_id\n"
            + "        )\n"
            + "        SELECT id FROM chain WHERE depth > 0 ORDER BY depth DESC\n"
            + "        ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, folderId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Long>>() {
      @Override
      @NonNull
      public List<Long> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final List<Long> _result = new ArrayList<Long>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Long _item;
            _item = _cursor.getLong(0);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object depthOf(final long folderId, final Continuation<? super Integer> $completion) {
    final String _sql = "\n"
            + "        WITH RECURSIVE chain(id, parent_id, depth) AS (\n"
            + "            SELECT id, parent_id, 0 FROM media_folders WHERE id = ?\n"
            + "            UNION ALL\n"
            + "            SELECT f.id, f.parent_id, chain.depth + 1\n"
            + "              FROM media_folders f\n"
            + "              INNER JOIN chain ON f.id = chain.parent_id\n"
            + "        )\n"
            + "        SELECT COUNT(*) - 1 FROM chain\n"
            + "        ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, folderId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object foldersNeedingSync(
      final Continuation<? super List<MediaFolderEntity>> $completion) {
    final String _sql = "\n"
            + "        SELECT * FROM media_folders\n"
            + "         WHERE auto_sync = 1\n"
            + "           AND telegram_chat_id != 0\n"
            + "         ORDER BY sort_order ASC\n"
            + "        ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<MediaFolderEntity>>() {
      @Override
      @NonNull
      public List<MediaFolderEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfParentId = CursorUtil.getColumnIndexOrThrow(_cursor, "parent_id");
          final int _cursorIndexOfEmoji = CursorUtil.getColumnIndexOrThrow(_cursor, "emoji");
          final int _cursorIndexOfColorArgb = CursorUtil.getColumnIndexOrThrow(_cursor, "color_argb");
          final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sort_order");
          final int _cursorIndexOfIncludeSubfolders = CursorUtil.getColumnIndexOrThrow(_cursor, "include_subfolders");
          final int _cursorIndexOfTelegramChatId = CursorUtil.getColumnIndexOrThrow(_cursor, "telegram_chat_id");
          final int _cursorIndexOfTelegramChatFolderId = CursorUtil.getColumnIndexOrThrow(_cursor, "telegram_chat_folder_id");
          final int _cursorIndexOfAutoTagRule = CursorUtil.getColumnIndexOrThrow(_cursor, "auto_tag_rule");
          final int _cursorIndexOfAutoSync = CursorUtil.getColumnIndexOrThrow(_cursor, "auto_sync");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final List<MediaFolderEntity> _result = new ArrayList<MediaFolderEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MediaFolderEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final Long _tmpParentId;
            if (_cursor.isNull(_cursorIndexOfParentId)) {
              _tmpParentId = null;
            } else {
              _tmpParentId = _cursor.getLong(_cursorIndexOfParentId);
            }
            final String _tmpEmoji;
            if (_cursor.isNull(_cursorIndexOfEmoji)) {
              _tmpEmoji = null;
            } else {
              _tmpEmoji = _cursor.getString(_cursorIndexOfEmoji);
            }
            final int _tmpColorArgb;
            _tmpColorArgb = _cursor.getInt(_cursorIndexOfColorArgb);
            final int _tmpSortOrder;
            _tmpSortOrder = _cursor.getInt(_cursorIndexOfSortOrder);
            final boolean _tmpIncludeSubfolders;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIncludeSubfolders);
            _tmpIncludeSubfolders = _tmp != 0;
            final long _tmpTelegramChatId;
            _tmpTelegramChatId = _cursor.getLong(_cursorIndexOfTelegramChatId);
            final int _tmpTelegramChatFolderId;
            _tmpTelegramChatFolderId = _cursor.getInt(_cursorIndexOfTelegramChatFolderId);
            final String _tmpAutoTagRule;
            if (_cursor.isNull(_cursorIndexOfAutoTagRule)) {
              _tmpAutoTagRule = null;
            } else {
              _tmpAutoTagRule = _cursor.getString(_cursorIndexOfAutoTagRule);
            }
            final boolean _tmpAutoSync;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfAutoSync);
            _tmpAutoSync = _tmp_1 != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new MediaFolderEntity(_tmpId,_tmpName,_tmpParentId,_tmpEmoji,_tmpColorArgb,_tmpSortOrder,_tmpIncludeSubfolders,_tmpTelegramChatId,_tmpTelegramChatFolderId,_tmpAutoTagRule,_tmpAutoSync,_tmpCreatedAt,_tmpUpdatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<MediaFolderEntity>> observeTelegramMappedFolders() {
    final String _sql = "SELECT * FROM media_folders WHERE telegram_chat_folder_id != 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"media_folders"}, new Callable<List<MediaFolderEntity>>() {
      @Override
      @NonNull
      public List<MediaFolderEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfParentId = CursorUtil.getColumnIndexOrThrow(_cursor, "parent_id");
          final int _cursorIndexOfEmoji = CursorUtil.getColumnIndexOrThrow(_cursor, "emoji");
          final int _cursorIndexOfColorArgb = CursorUtil.getColumnIndexOrThrow(_cursor, "color_argb");
          final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sort_order");
          final int _cursorIndexOfIncludeSubfolders = CursorUtil.getColumnIndexOrThrow(_cursor, "include_subfolders");
          final int _cursorIndexOfTelegramChatId = CursorUtil.getColumnIndexOrThrow(_cursor, "telegram_chat_id");
          final int _cursorIndexOfTelegramChatFolderId = CursorUtil.getColumnIndexOrThrow(_cursor, "telegram_chat_folder_id");
          final int _cursorIndexOfAutoTagRule = CursorUtil.getColumnIndexOrThrow(_cursor, "auto_tag_rule");
          final int _cursorIndexOfAutoSync = CursorUtil.getColumnIndexOrThrow(_cursor, "auto_sync");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final List<MediaFolderEntity> _result = new ArrayList<MediaFolderEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MediaFolderEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final Long _tmpParentId;
            if (_cursor.isNull(_cursorIndexOfParentId)) {
              _tmpParentId = null;
            } else {
              _tmpParentId = _cursor.getLong(_cursorIndexOfParentId);
            }
            final String _tmpEmoji;
            if (_cursor.isNull(_cursorIndexOfEmoji)) {
              _tmpEmoji = null;
            } else {
              _tmpEmoji = _cursor.getString(_cursorIndexOfEmoji);
            }
            final int _tmpColorArgb;
            _tmpColorArgb = _cursor.getInt(_cursorIndexOfColorArgb);
            final int _tmpSortOrder;
            _tmpSortOrder = _cursor.getInt(_cursorIndexOfSortOrder);
            final boolean _tmpIncludeSubfolders;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIncludeSubfolders);
            _tmpIncludeSubfolders = _tmp != 0;
            final long _tmpTelegramChatId;
            _tmpTelegramChatId = _cursor.getLong(_cursorIndexOfTelegramChatId);
            final int _tmpTelegramChatFolderId;
            _tmpTelegramChatFolderId = _cursor.getInt(_cursorIndexOfTelegramChatFolderId);
            final String _tmpAutoTagRule;
            if (_cursor.isNull(_cursorIndexOfAutoTagRule)) {
              _tmpAutoTagRule = null;
            } else {
              _tmpAutoTagRule = _cursor.getString(_cursorIndexOfAutoTagRule);
            }
            final boolean _tmpAutoSync;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfAutoSync);
            _tmpAutoSync = _tmp_1 != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new MediaFolderEntity(_tmpId,_tmpName,_tmpParentId,_tmpEmoji,_tmpColorArgb,_tmpSortOrder,_tmpIncludeSubfolders,_tmpTelegramChatId,_tmpTelegramChatFolderId,_tmpAutoTagRule,_tmpAutoSync,_tmpCreatedAt,_tmpUpdatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object maxSortOrderUnder(final Long parentId,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COALESCE(MAX(sort_order), -1) FROM media_folders WHERE parent_id IS ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (parentId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, parentId);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object count(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM media_folders";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
