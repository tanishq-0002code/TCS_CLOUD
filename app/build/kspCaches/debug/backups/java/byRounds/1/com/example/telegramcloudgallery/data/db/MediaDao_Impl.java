package com.example.telegramcloudgallery.data.db;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.LongSparseArray;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.RelationUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.telegramcloudgallery.core.MediaKind;
import com.example.telegramcloudgallery.core.UploadState;
import com.example.telegramcloudgallery.data.db.entity.MediaItemEntity;
import com.example.telegramcloudgallery.data.db.entity.MediaTagEntity;
import com.example.telegramcloudgallery.data.db.relation.MediaItemWithTags;
import java.lang.Class;
import java.lang.Exception;
import java.lang.IllegalArgumentException;
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
public final class MediaDao_Impl implements MediaDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<MediaItemEntity> __insertionAdapterOfMediaItemEntity;

  private final EntityDeletionOrUpdateAdapter<MediaItemEntity> __updateAdapterOfMediaItemEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteById;

  private final SharedSQLiteStatement __preparedStmtOfDeleteByChat;

  private final SharedSQLiteStatement __preparedStmtOfUpdateUploadState;

  private final SharedSQLiteStatement __preparedStmtOfUpdateLocalFile;

  private final SharedSQLiteStatement __preparedStmtOfSetFolder;

  private final SharedSQLiteStatement __preparedStmtOfUpdateFileIds;

  private final SharedSQLiteStatement __preparedStmtOfMarkUploaded;

  public MediaDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMediaItemEntity = new EntityInsertionAdapter<MediaItemEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `media_items` (`id`,`remote_id`,`chat_id`,`message_id`,`folder_id`,`media_type`,`file_name`,`mime_type`,`file_size`,`duration_ms`,`width`,`height`,`caption_text`,`caption_entities_json`,`remote_file_id`,`local_file_id`,`thumbnail_file_id`,`local_path`,`is_downloaded`,`upload_state`,`upload_progress`,`upload_error`,`local_content_hash`,`message_date`,`created_at`,`updated_at`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MediaItemEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getRemoteId());
        statement.bindLong(3, entity.getChatId());
        statement.bindLong(4, entity.getMessageId());
        if (entity.getFolderId() == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, entity.getFolderId());
        }
        statement.bindString(6, __MediaKind_enumToString(entity.getMediaType()));
        if (entity.getFileName() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getFileName());
        }
        if (entity.getMimeType() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getMimeType());
        }
        statement.bindLong(9, entity.getFileSize());
        statement.bindLong(10, entity.getDurationMs());
        statement.bindLong(11, entity.getWidth());
        statement.bindLong(12, entity.getHeight());
        if (entity.getCaptionText() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getCaptionText());
        }
        if (entity.getCaptionEntitiesJson() == null) {
          statement.bindNull(14);
        } else {
          statement.bindString(14, entity.getCaptionEntitiesJson());
        }
        statement.bindLong(15, entity.getRemoteFileId());
        statement.bindLong(16, entity.getLocalFileId());
        statement.bindLong(17, entity.getThumbnailFileId());
        if (entity.getLocalPath() == null) {
          statement.bindNull(18);
        } else {
          statement.bindString(18, entity.getLocalPath());
        }
        final int _tmp = entity.isDownloaded() ? 1 : 0;
        statement.bindLong(19, _tmp);
        statement.bindString(20, __UploadState_enumToString(entity.getUploadState()));
        statement.bindLong(21, entity.getUploadProgress());
        if (entity.getUploadError() == null) {
          statement.bindNull(22);
        } else {
          statement.bindString(22, entity.getUploadError());
        }
        if (entity.getLocalContentHash() == null) {
          statement.bindNull(23);
        } else {
          statement.bindString(23, entity.getLocalContentHash());
        }
        statement.bindLong(24, entity.getMessageDate());
        statement.bindLong(25, entity.getCreatedAt());
        statement.bindLong(26, entity.getUpdatedAt());
      }
    };
    this.__updateAdapterOfMediaItemEntity = new EntityDeletionOrUpdateAdapter<MediaItemEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `media_items` SET `id` = ?,`remote_id` = ?,`chat_id` = ?,`message_id` = ?,`folder_id` = ?,`media_type` = ?,`file_name` = ?,`mime_type` = ?,`file_size` = ?,`duration_ms` = ?,`width` = ?,`height` = ?,`caption_text` = ?,`caption_entities_json` = ?,`remote_file_id` = ?,`local_file_id` = ?,`thumbnail_file_id` = ?,`local_path` = ?,`is_downloaded` = ?,`upload_state` = ?,`upload_progress` = ?,`upload_error` = ?,`local_content_hash` = ?,`message_date` = ?,`created_at` = ?,`updated_at` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MediaItemEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getRemoteId());
        statement.bindLong(3, entity.getChatId());
        statement.bindLong(4, entity.getMessageId());
        if (entity.getFolderId() == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, entity.getFolderId());
        }
        statement.bindString(6, __MediaKind_enumToString(entity.getMediaType()));
        if (entity.getFileName() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getFileName());
        }
        if (entity.getMimeType() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getMimeType());
        }
        statement.bindLong(9, entity.getFileSize());
        statement.bindLong(10, entity.getDurationMs());
        statement.bindLong(11, entity.getWidth());
        statement.bindLong(12, entity.getHeight());
        if (entity.getCaptionText() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getCaptionText());
        }
        if (entity.getCaptionEntitiesJson() == null) {
          statement.bindNull(14);
        } else {
          statement.bindString(14, entity.getCaptionEntitiesJson());
        }
        statement.bindLong(15, entity.getRemoteFileId());
        statement.bindLong(16, entity.getLocalFileId());
        statement.bindLong(17, entity.getThumbnailFileId());
        if (entity.getLocalPath() == null) {
          statement.bindNull(18);
        } else {
          statement.bindString(18, entity.getLocalPath());
        }
        final int _tmp = entity.isDownloaded() ? 1 : 0;
        statement.bindLong(19, _tmp);
        statement.bindString(20, __UploadState_enumToString(entity.getUploadState()));
        statement.bindLong(21, entity.getUploadProgress());
        if (entity.getUploadError() == null) {
          statement.bindNull(22);
        } else {
          statement.bindString(22, entity.getUploadError());
        }
        if (entity.getLocalContentHash() == null) {
          statement.bindNull(23);
        } else {
          statement.bindString(23, entity.getLocalContentHash());
        }
        statement.bindLong(24, entity.getMessageDate());
        statement.bindLong(25, entity.getCreatedAt());
        statement.bindLong(26, entity.getUpdatedAt());
        statement.bindLong(27, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM media_items WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteByChat = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM media_items WHERE chat_id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateUploadState = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "\n"
                + "        UPDATE media_items\n"
                + "           SET upload_state = ?,\n"
                + "               upload_progress = ?,\n"
                + "               upload_error = ?,\n"
                + "               updated_at = ?\n"
                + "         WHERE id = ?\n"
                + "        ";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateLocalFile = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "\n"
                + "        UPDATE media_items\n"
                + "           SET is_downloaded = ?,\n"
                + "               local_path = ?,\n"
                + "               local_file_id = ?,\n"
                + "               updated_at = ?\n"
                + "         WHERE id = ?\n"
                + "        ";
        return _query;
      }
    };
    this.__preparedStmtOfSetFolder = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE media_items SET folder_id = ?, updated_at = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateFileIds = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "\n"
                + "        UPDATE media_items\n"
                + "           SET remote_file_id = ?,\n"
                + "               thumbnail_file_id = ?,\n"
                + "               updated_at = ?\n"
                + "         WHERE chat_id = ? AND message_id = ?\n"
                + "        ";
        return _query;
      }
    };
    this.__preparedStmtOfMarkUploaded = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "\n"
                + "        UPDATE media_items\n"
                + "           SET upload_state = 'UPLOADED',\n"
                + "               upload_progress = 100,\n"
                + "               upload_error = NULL,\n"
                + "               updated_at = ?\n"
                + "         WHERE id = ?\n"
                + "        ";
        return _query;
      }
    };
  }

  @Override
  public Object upsert(final MediaItemEntity item, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfMediaItemEntity.insertAndReturnId(item);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object upsertAll(final List<MediaItemEntity> items,
      final Continuation<? super List<Long>> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<List<Long>>() {
      @Override
      @NonNull
      public List<Long> call() throws Exception {
        __db.beginTransaction();
        try {
          final List<Long> _result = __insertionAdapterOfMediaItemEntity.insertAndReturnIdsList(items);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final MediaItemEntity item, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfMediaItemEntity.handle(item);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteById(final long itemId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteById.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, itemId);
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
          __preparedStmtOfDeleteById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteByChat(final long chatId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteByChat.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, chatId);
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
          __preparedStmtOfDeleteByChat.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateUploadState(final long itemId, final UploadState state, final int progress,
      final String error, final long now, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateUploadState.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, __UploadState_enumToString(state));
        _argIndex = 2;
        _stmt.bindLong(_argIndex, progress);
        _argIndex = 3;
        if (error == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, error);
        }
        _argIndex = 4;
        _stmt.bindLong(_argIndex, now);
        _argIndex = 5;
        _stmt.bindLong(_argIndex, itemId);
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
          __preparedStmtOfUpdateUploadState.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateLocalFile(final long itemId, final boolean downloaded, final String localPath,
      final int localFileId, final long now, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateLocalFile.acquire();
        int _argIndex = 1;
        final int _tmp = downloaded ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
        if (localPath == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, localPath);
        }
        _argIndex = 3;
        _stmt.bindLong(_argIndex, localFileId);
        _argIndex = 4;
        _stmt.bindLong(_argIndex, now);
        _argIndex = 5;
        _stmt.bindLong(_argIndex, itemId);
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
          __preparedStmtOfUpdateLocalFile.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object setFolder(final long itemId, final Long folderId, final long now,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSetFolder.acquire();
        int _argIndex = 1;
        if (folderId == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindLong(_argIndex, folderId);
        }
        _argIndex = 2;
        _stmt.bindLong(_argIndex, now);
        _argIndex = 3;
        _stmt.bindLong(_argIndex, itemId);
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
          __preparedStmtOfSetFolder.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateFileIds(final long chatId, final long messageId, final int remoteFileId,
      final int thumbnailFileId, final long now, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateFileIds.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, remoteFileId);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, thumbnailFileId);
        _argIndex = 3;
        _stmt.bindLong(_argIndex, now);
        _argIndex = 4;
        _stmt.bindLong(_argIndex, chatId);
        _argIndex = 5;
        _stmt.bindLong(_argIndex, messageId);
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
          __preparedStmtOfUpdateFileIds.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object markUploaded(final long itemId, final long now,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfMarkUploaded.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, now);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, itemId);
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
          __preparedStmtOfMarkUploaded.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<MediaItemEntity> observeById(final long itemId) {
    final String _sql = "SELECT * FROM media_items WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, itemId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"media_items"}, new Callable<MediaItemEntity>() {
      @Override
      @Nullable
      public MediaItemEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remote_id");
          final int _cursorIndexOfChatId = CursorUtil.getColumnIndexOrThrow(_cursor, "chat_id");
          final int _cursorIndexOfMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "message_id");
          final int _cursorIndexOfFolderId = CursorUtil.getColumnIndexOrThrow(_cursor, "folder_id");
          final int _cursorIndexOfMediaType = CursorUtil.getColumnIndexOrThrow(_cursor, "media_type");
          final int _cursorIndexOfFileName = CursorUtil.getColumnIndexOrThrow(_cursor, "file_name");
          final int _cursorIndexOfMimeType = CursorUtil.getColumnIndexOrThrow(_cursor, "mime_type");
          final int _cursorIndexOfFileSize = CursorUtil.getColumnIndexOrThrow(_cursor, "file_size");
          final int _cursorIndexOfDurationMs = CursorUtil.getColumnIndexOrThrow(_cursor, "duration_ms");
          final int _cursorIndexOfWidth = CursorUtil.getColumnIndexOrThrow(_cursor, "width");
          final int _cursorIndexOfHeight = CursorUtil.getColumnIndexOrThrow(_cursor, "height");
          final int _cursorIndexOfCaptionText = CursorUtil.getColumnIndexOrThrow(_cursor, "caption_text");
          final int _cursorIndexOfCaptionEntitiesJson = CursorUtil.getColumnIndexOrThrow(_cursor, "caption_entities_json");
          final int _cursorIndexOfRemoteFileId = CursorUtil.getColumnIndexOrThrow(_cursor, "remote_file_id");
          final int _cursorIndexOfLocalFileId = CursorUtil.getColumnIndexOrThrow(_cursor, "local_file_id");
          final int _cursorIndexOfThumbnailFileId = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbnail_file_id");
          final int _cursorIndexOfLocalPath = CursorUtil.getColumnIndexOrThrow(_cursor, "local_path");
          final int _cursorIndexOfIsDownloaded = CursorUtil.getColumnIndexOrThrow(_cursor, "is_downloaded");
          final int _cursorIndexOfUploadState = CursorUtil.getColumnIndexOrThrow(_cursor, "upload_state");
          final int _cursorIndexOfUploadProgress = CursorUtil.getColumnIndexOrThrow(_cursor, "upload_progress");
          final int _cursorIndexOfUploadError = CursorUtil.getColumnIndexOrThrow(_cursor, "upload_error");
          final int _cursorIndexOfLocalContentHash = CursorUtil.getColumnIndexOrThrow(_cursor, "local_content_hash");
          final int _cursorIndexOfMessageDate = CursorUtil.getColumnIndexOrThrow(_cursor, "message_date");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final MediaItemEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpRemoteId;
            _tmpRemoteId = _cursor.getString(_cursorIndexOfRemoteId);
            final long _tmpChatId;
            _tmpChatId = _cursor.getLong(_cursorIndexOfChatId);
            final long _tmpMessageId;
            _tmpMessageId = _cursor.getLong(_cursorIndexOfMessageId);
            final Long _tmpFolderId;
            if (_cursor.isNull(_cursorIndexOfFolderId)) {
              _tmpFolderId = null;
            } else {
              _tmpFolderId = _cursor.getLong(_cursorIndexOfFolderId);
            }
            final MediaKind _tmpMediaType;
            _tmpMediaType = __MediaKind_stringToEnum(_cursor.getString(_cursorIndexOfMediaType));
            final String _tmpFileName;
            if (_cursor.isNull(_cursorIndexOfFileName)) {
              _tmpFileName = null;
            } else {
              _tmpFileName = _cursor.getString(_cursorIndexOfFileName);
            }
            final String _tmpMimeType;
            if (_cursor.isNull(_cursorIndexOfMimeType)) {
              _tmpMimeType = null;
            } else {
              _tmpMimeType = _cursor.getString(_cursorIndexOfMimeType);
            }
            final long _tmpFileSize;
            _tmpFileSize = _cursor.getLong(_cursorIndexOfFileSize);
            final long _tmpDurationMs;
            _tmpDurationMs = _cursor.getLong(_cursorIndexOfDurationMs);
            final int _tmpWidth;
            _tmpWidth = _cursor.getInt(_cursorIndexOfWidth);
            final int _tmpHeight;
            _tmpHeight = _cursor.getInt(_cursorIndexOfHeight);
            final String _tmpCaptionText;
            if (_cursor.isNull(_cursorIndexOfCaptionText)) {
              _tmpCaptionText = null;
            } else {
              _tmpCaptionText = _cursor.getString(_cursorIndexOfCaptionText);
            }
            final String _tmpCaptionEntitiesJson;
            if (_cursor.isNull(_cursorIndexOfCaptionEntitiesJson)) {
              _tmpCaptionEntitiesJson = null;
            } else {
              _tmpCaptionEntitiesJson = _cursor.getString(_cursorIndexOfCaptionEntitiesJson);
            }
            final int _tmpRemoteFileId;
            _tmpRemoteFileId = _cursor.getInt(_cursorIndexOfRemoteFileId);
            final int _tmpLocalFileId;
            _tmpLocalFileId = _cursor.getInt(_cursorIndexOfLocalFileId);
            final int _tmpThumbnailFileId;
            _tmpThumbnailFileId = _cursor.getInt(_cursorIndexOfThumbnailFileId);
            final String _tmpLocalPath;
            if (_cursor.isNull(_cursorIndexOfLocalPath)) {
              _tmpLocalPath = null;
            } else {
              _tmpLocalPath = _cursor.getString(_cursorIndexOfLocalPath);
            }
            final boolean _tmpIsDownloaded;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsDownloaded);
            _tmpIsDownloaded = _tmp != 0;
            final UploadState _tmpUploadState;
            _tmpUploadState = __UploadState_stringToEnum(_cursor.getString(_cursorIndexOfUploadState));
            final int _tmpUploadProgress;
            _tmpUploadProgress = _cursor.getInt(_cursorIndexOfUploadProgress);
            final String _tmpUploadError;
            if (_cursor.isNull(_cursorIndexOfUploadError)) {
              _tmpUploadError = null;
            } else {
              _tmpUploadError = _cursor.getString(_cursorIndexOfUploadError);
            }
            final String _tmpLocalContentHash;
            if (_cursor.isNull(_cursorIndexOfLocalContentHash)) {
              _tmpLocalContentHash = null;
            } else {
              _tmpLocalContentHash = _cursor.getString(_cursorIndexOfLocalContentHash);
            }
            final long _tmpMessageDate;
            _tmpMessageDate = _cursor.getLong(_cursorIndexOfMessageDate);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new MediaItemEntity(_tmpId,_tmpRemoteId,_tmpChatId,_tmpMessageId,_tmpFolderId,_tmpMediaType,_tmpFileName,_tmpMimeType,_tmpFileSize,_tmpDurationMs,_tmpWidth,_tmpHeight,_tmpCaptionText,_tmpCaptionEntitiesJson,_tmpRemoteFileId,_tmpLocalFileId,_tmpThumbnailFileId,_tmpLocalPath,_tmpIsDownloaded,_tmpUploadState,_tmpUploadProgress,_tmpUploadError,_tmpLocalContentHash,_tmpMessageDate,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Object findById(final long itemId,
      final Continuation<? super MediaItemEntity> $completion) {
    final String _sql = "SELECT * FROM media_items WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, itemId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<MediaItemEntity>() {
      @Override
      @Nullable
      public MediaItemEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remote_id");
          final int _cursorIndexOfChatId = CursorUtil.getColumnIndexOrThrow(_cursor, "chat_id");
          final int _cursorIndexOfMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "message_id");
          final int _cursorIndexOfFolderId = CursorUtil.getColumnIndexOrThrow(_cursor, "folder_id");
          final int _cursorIndexOfMediaType = CursorUtil.getColumnIndexOrThrow(_cursor, "media_type");
          final int _cursorIndexOfFileName = CursorUtil.getColumnIndexOrThrow(_cursor, "file_name");
          final int _cursorIndexOfMimeType = CursorUtil.getColumnIndexOrThrow(_cursor, "mime_type");
          final int _cursorIndexOfFileSize = CursorUtil.getColumnIndexOrThrow(_cursor, "file_size");
          final int _cursorIndexOfDurationMs = CursorUtil.getColumnIndexOrThrow(_cursor, "duration_ms");
          final int _cursorIndexOfWidth = CursorUtil.getColumnIndexOrThrow(_cursor, "width");
          final int _cursorIndexOfHeight = CursorUtil.getColumnIndexOrThrow(_cursor, "height");
          final int _cursorIndexOfCaptionText = CursorUtil.getColumnIndexOrThrow(_cursor, "caption_text");
          final int _cursorIndexOfCaptionEntitiesJson = CursorUtil.getColumnIndexOrThrow(_cursor, "caption_entities_json");
          final int _cursorIndexOfRemoteFileId = CursorUtil.getColumnIndexOrThrow(_cursor, "remote_file_id");
          final int _cursorIndexOfLocalFileId = CursorUtil.getColumnIndexOrThrow(_cursor, "local_file_id");
          final int _cursorIndexOfThumbnailFileId = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbnail_file_id");
          final int _cursorIndexOfLocalPath = CursorUtil.getColumnIndexOrThrow(_cursor, "local_path");
          final int _cursorIndexOfIsDownloaded = CursorUtil.getColumnIndexOrThrow(_cursor, "is_downloaded");
          final int _cursorIndexOfUploadState = CursorUtil.getColumnIndexOrThrow(_cursor, "upload_state");
          final int _cursorIndexOfUploadProgress = CursorUtil.getColumnIndexOrThrow(_cursor, "upload_progress");
          final int _cursorIndexOfUploadError = CursorUtil.getColumnIndexOrThrow(_cursor, "upload_error");
          final int _cursorIndexOfLocalContentHash = CursorUtil.getColumnIndexOrThrow(_cursor, "local_content_hash");
          final int _cursorIndexOfMessageDate = CursorUtil.getColumnIndexOrThrow(_cursor, "message_date");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final MediaItemEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpRemoteId;
            _tmpRemoteId = _cursor.getString(_cursorIndexOfRemoteId);
            final long _tmpChatId;
            _tmpChatId = _cursor.getLong(_cursorIndexOfChatId);
            final long _tmpMessageId;
            _tmpMessageId = _cursor.getLong(_cursorIndexOfMessageId);
            final Long _tmpFolderId;
            if (_cursor.isNull(_cursorIndexOfFolderId)) {
              _tmpFolderId = null;
            } else {
              _tmpFolderId = _cursor.getLong(_cursorIndexOfFolderId);
            }
            final MediaKind _tmpMediaType;
            _tmpMediaType = __MediaKind_stringToEnum(_cursor.getString(_cursorIndexOfMediaType));
            final String _tmpFileName;
            if (_cursor.isNull(_cursorIndexOfFileName)) {
              _tmpFileName = null;
            } else {
              _tmpFileName = _cursor.getString(_cursorIndexOfFileName);
            }
            final String _tmpMimeType;
            if (_cursor.isNull(_cursorIndexOfMimeType)) {
              _tmpMimeType = null;
            } else {
              _tmpMimeType = _cursor.getString(_cursorIndexOfMimeType);
            }
            final long _tmpFileSize;
            _tmpFileSize = _cursor.getLong(_cursorIndexOfFileSize);
            final long _tmpDurationMs;
            _tmpDurationMs = _cursor.getLong(_cursorIndexOfDurationMs);
            final int _tmpWidth;
            _tmpWidth = _cursor.getInt(_cursorIndexOfWidth);
            final int _tmpHeight;
            _tmpHeight = _cursor.getInt(_cursorIndexOfHeight);
            final String _tmpCaptionText;
            if (_cursor.isNull(_cursorIndexOfCaptionText)) {
              _tmpCaptionText = null;
            } else {
              _tmpCaptionText = _cursor.getString(_cursorIndexOfCaptionText);
            }
            final String _tmpCaptionEntitiesJson;
            if (_cursor.isNull(_cursorIndexOfCaptionEntitiesJson)) {
              _tmpCaptionEntitiesJson = null;
            } else {
              _tmpCaptionEntitiesJson = _cursor.getString(_cursorIndexOfCaptionEntitiesJson);
            }
            final int _tmpRemoteFileId;
            _tmpRemoteFileId = _cursor.getInt(_cursorIndexOfRemoteFileId);
            final int _tmpLocalFileId;
            _tmpLocalFileId = _cursor.getInt(_cursorIndexOfLocalFileId);
            final int _tmpThumbnailFileId;
            _tmpThumbnailFileId = _cursor.getInt(_cursorIndexOfThumbnailFileId);
            final String _tmpLocalPath;
            if (_cursor.isNull(_cursorIndexOfLocalPath)) {
              _tmpLocalPath = null;
            } else {
              _tmpLocalPath = _cursor.getString(_cursorIndexOfLocalPath);
            }
            final boolean _tmpIsDownloaded;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsDownloaded);
            _tmpIsDownloaded = _tmp != 0;
            final UploadState _tmpUploadState;
            _tmpUploadState = __UploadState_stringToEnum(_cursor.getString(_cursorIndexOfUploadState));
            final int _tmpUploadProgress;
            _tmpUploadProgress = _cursor.getInt(_cursorIndexOfUploadProgress);
            final String _tmpUploadError;
            if (_cursor.isNull(_cursorIndexOfUploadError)) {
              _tmpUploadError = null;
            } else {
              _tmpUploadError = _cursor.getString(_cursorIndexOfUploadError);
            }
            final String _tmpLocalContentHash;
            if (_cursor.isNull(_cursorIndexOfLocalContentHash)) {
              _tmpLocalContentHash = null;
            } else {
              _tmpLocalContentHash = _cursor.getString(_cursorIndexOfLocalContentHash);
            }
            final long _tmpMessageDate;
            _tmpMessageDate = _cursor.getLong(_cursorIndexOfMessageDate);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new MediaItemEntity(_tmpId,_tmpRemoteId,_tmpChatId,_tmpMessageId,_tmpFolderId,_tmpMediaType,_tmpFileName,_tmpMimeType,_tmpFileSize,_tmpDurationMs,_tmpWidth,_tmpHeight,_tmpCaptionText,_tmpCaptionEntitiesJson,_tmpRemoteFileId,_tmpLocalFileId,_tmpThumbnailFileId,_tmpLocalPath,_tmpIsDownloaded,_tmpUploadState,_tmpUploadProgress,_tmpUploadError,_tmpLocalContentHash,_tmpMessageDate,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Object findByRemoteId(final String remoteId,
      final Continuation<? super MediaItemEntity> $completion) {
    final String _sql = "SELECT * FROM media_items WHERE remote_id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, remoteId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<MediaItemEntity>() {
      @Override
      @Nullable
      public MediaItemEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remote_id");
          final int _cursorIndexOfChatId = CursorUtil.getColumnIndexOrThrow(_cursor, "chat_id");
          final int _cursorIndexOfMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "message_id");
          final int _cursorIndexOfFolderId = CursorUtil.getColumnIndexOrThrow(_cursor, "folder_id");
          final int _cursorIndexOfMediaType = CursorUtil.getColumnIndexOrThrow(_cursor, "media_type");
          final int _cursorIndexOfFileName = CursorUtil.getColumnIndexOrThrow(_cursor, "file_name");
          final int _cursorIndexOfMimeType = CursorUtil.getColumnIndexOrThrow(_cursor, "mime_type");
          final int _cursorIndexOfFileSize = CursorUtil.getColumnIndexOrThrow(_cursor, "file_size");
          final int _cursorIndexOfDurationMs = CursorUtil.getColumnIndexOrThrow(_cursor, "duration_ms");
          final int _cursorIndexOfWidth = CursorUtil.getColumnIndexOrThrow(_cursor, "width");
          final int _cursorIndexOfHeight = CursorUtil.getColumnIndexOrThrow(_cursor, "height");
          final int _cursorIndexOfCaptionText = CursorUtil.getColumnIndexOrThrow(_cursor, "caption_text");
          final int _cursorIndexOfCaptionEntitiesJson = CursorUtil.getColumnIndexOrThrow(_cursor, "caption_entities_json");
          final int _cursorIndexOfRemoteFileId = CursorUtil.getColumnIndexOrThrow(_cursor, "remote_file_id");
          final int _cursorIndexOfLocalFileId = CursorUtil.getColumnIndexOrThrow(_cursor, "local_file_id");
          final int _cursorIndexOfThumbnailFileId = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbnail_file_id");
          final int _cursorIndexOfLocalPath = CursorUtil.getColumnIndexOrThrow(_cursor, "local_path");
          final int _cursorIndexOfIsDownloaded = CursorUtil.getColumnIndexOrThrow(_cursor, "is_downloaded");
          final int _cursorIndexOfUploadState = CursorUtil.getColumnIndexOrThrow(_cursor, "upload_state");
          final int _cursorIndexOfUploadProgress = CursorUtil.getColumnIndexOrThrow(_cursor, "upload_progress");
          final int _cursorIndexOfUploadError = CursorUtil.getColumnIndexOrThrow(_cursor, "upload_error");
          final int _cursorIndexOfLocalContentHash = CursorUtil.getColumnIndexOrThrow(_cursor, "local_content_hash");
          final int _cursorIndexOfMessageDate = CursorUtil.getColumnIndexOrThrow(_cursor, "message_date");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final MediaItemEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpRemoteId;
            _tmpRemoteId = _cursor.getString(_cursorIndexOfRemoteId);
            final long _tmpChatId;
            _tmpChatId = _cursor.getLong(_cursorIndexOfChatId);
            final long _tmpMessageId;
            _tmpMessageId = _cursor.getLong(_cursorIndexOfMessageId);
            final Long _tmpFolderId;
            if (_cursor.isNull(_cursorIndexOfFolderId)) {
              _tmpFolderId = null;
            } else {
              _tmpFolderId = _cursor.getLong(_cursorIndexOfFolderId);
            }
            final MediaKind _tmpMediaType;
            _tmpMediaType = __MediaKind_stringToEnum(_cursor.getString(_cursorIndexOfMediaType));
            final String _tmpFileName;
            if (_cursor.isNull(_cursorIndexOfFileName)) {
              _tmpFileName = null;
            } else {
              _tmpFileName = _cursor.getString(_cursorIndexOfFileName);
            }
            final String _tmpMimeType;
            if (_cursor.isNull(_cursorIndexOfMimeType)) {
              _tmpMimeType = null;
            } else {
              _tmpMimeType = _cursor.getString(_cursorIndexOfMimeType);
            }
            final long _tmpFileSize;
            _tmpFileSize = _cursor.getLong(_cursorIndexOfFileSize);
            final long _tmpDurationMs;
            _tmpDurationMs = _cursor.getLong(_cursorIndexOfDurationMs);
            final int _tmpWidth;
            _tmpWidth = _cursor.getInt(_cursorIndexOfWidth);
            final int _tmpHeight;
            _tmpHeight = _cursor.getInt(_cursorIndexOfHeight);
            final String _tmpCaptionText;
            if (_cursor.isNull(_cursorIndexOfCaptionText)) {
              _tmpCaptionText = null;
            } else {
              _tmpCaptionText = _cursor.getString(_cursorIndexOfCaptionText);
            }
            final String _tmpCaptionEntitiesJson;
            if (_cursor.isNull(_cursorIndexOfCaptionEntitiesJson)) {
              _tmpCaptionEntitiesJson = null;
            } else {
              _tmpCaptionEntitiesJson = _cursor.getString(_cursorIndexOfCaptionEntitiesJson);
            }
            final int _tmpRemoteFileId;
            _tmpRemoteFileId = _cursor.getInt(_cursorIndexOfRemoteFileId);
            final int _tmpLocalFileId;
            _tmpLocalFileId = _cursor.getInt(_cursorIndexOfLocalFileId);
            final int _tmpThumbnailFileId;
            _tmpThumbnailFileId = _cursor.getInt(_cursorIndexOfThumbnailFileId);
            final String _tmpLocalPath;
            if (_cursor.isNull(_cursorIndexOfLocalPath)) {
              _tmpLocalPath = null;
            } else {
              _tmpLocalPath = _cursor.getString(_cursorIndexOfLocalPath);
            }
            final boolean _tmpIsDownloaded;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsDownloaded);
            _tmpIsDownloaded = _tmp != 0;
            final UploadState _tmpUploadState;
            _tmpUploadState = __UploadState_stringToEnum(_cursor.getString(_cursorIndexOfUploadState));
            final int _tmpUploadProgress;
            _tmpUploadProgress = _cursor.getInt(_cursorIndexOfUploadProgress);
            final String _tmpUploadError;
            if (_cursor.isNull(_cursorIndexOfUploadError)) {
              _tmpUploadError = null;
            } else {
              _tmpUploadError = _cursor.getString(_cursorIndexOfUploadError);
            }
            final String _tmpLocalContentHash;
            if (_cursor.isNull(_cursorIndexOfLocalContentHash)) {
              _tmpLocalContentHash = null;
            } else {
              _tmpLocalContentHash = _cursor.getString(_cursorIndexOfLocalContentHash);
            }
            final long _tmpMessageDate;
            _tmpMessageDate = _cursor.getLong(_cursorIndexOfMessageDate);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new MediaItemEntity(_tmpId,_tmpRemoteId,_tmpChatId,_tmpMessageId,_tmpFolderId,_tmpMediaType,_tmpFileName,_tmpMimeType,_tmpFileSize,_tmpDurationMs,_tmpWidth,_tmpHeight,_tmpCaptionText,_tmpCaptionEntitiesJson,_tmpRemoteFileId,_tmpLocalFileId,_tmpThumbnailFileId,_tmpLocalPath,_tmpIsDownloaded,_tmpUploadState,_tmpUploadProgress,_tmpUploadError,_tmpLocalContentHash,_tmpMessageDate,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Object findByCoordinates(final long chatId, final long messageId,
      final Continuation<? super MediaItemEntity> $completion) {
    final String _sql = "SELECT * FROM media_items WHERE chat_id = ? AND message_id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, chatId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, messageId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<MediaItemEntity>() {
      @Override
      @Nullable
      public MediaItemEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remote_id");
          final int _cursorIndexOfChatId = CursorUtil.getColumnIndexOrThrow(_cursor, "chat_id");
          final int _cursorIndexOfMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "message_id");
          final int _cursorIndexOfFolderId = CursorUtil.getColumnIndexOrThrow(_cursor, "folder_id");
          final int _cursorIndexOfMediaType = CursorUtil.getColumnIndexOrThrow(_cursor, "media_type");
          final int _cursorIndexOfFileName = CursorUtil.getColumnIndexOrThrow(_cursor, "file_name");
          final int _cursorIndexOfMimeType = CursorUtil.getColumnIndexOrThrow(_cursor, "mime_type");
          final int _cursorIndexOfFileSize = CursorUtil.getColumnIndexOrThrow(_cursor, "file_size");
          final int _cursorIndexOfDurationMs = CursorUtil.getColumnIndexOrThrow(_cursor, "duration_ms");
          final int _cursorIndexOfWidth = CursorUtil.getColumnIndexOrThrow(_cursor, "width");
          final int _cursorIndexOfHeight = CursorUtil.getColumnIndexOrThrow(_cursor, "height");
          final int _cursorIndexOfCaptionText = CursorUtil.getColumnIndexOrThrow(_cursor, "caption_text");
          final int _cursorIndexOfCaptionEntitiesJson = CursorUtil.getColumnIndexOrThrow(_cursor, "caption_entities_json");
          final int _cursorIndexOfRemoteFileId = CursorUtil.getColumnIndexOrThrow(_cursor, "remote_file_id");
          final int _cursorIndexOfLocalFileId = CursorUtil.getColumnIndexOrThrow(_cursor, "local_file_id");
          final int _cursorIndexOfThumbnailFileId = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbnail_file_id");
          final int _cursorIndexOfLocalPath = CursorUtil.getColumnIndexOrThrow(_cursor, "local_path");
          final int _cursorIndexOfIsDownloaded = CursorUtil.getColumnIndexOrThrow(_cursor, "is_downloaded");
          final int _cursorIndexOfUploadState = CursorUtil.getColumnIndexOrThrow(_cursor, "upload_state");
          final int _cursorIndexOfUploadProgress = CursorUtil.getColumnIndexOrThrow(_cursor, "upload_progress");
          final int _cursorIndexOfUploadError = CursorUtil.getColumnIndexOrThrow(_cursor, "upload_error");
          final int _cursorIndexOfLocalContentHash = CursorUtil.getColumnIndexOrThrow(_cursor, "local_content_hash");
          final int _cursorIndexOfMessageDate = CursorUtil.getColumnIndexOrThrow(_cursor, "message_date");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final MediaItemEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpRemoteId;
            _tmpRemoteId = _cursor.getString(_cursorIndexOfRemoteId);
            final long _tmpChatId;
            _tmpChatId = _cursor.getLong(_cursorIndexOfChatId);
            final long _tmpMessageId;
            _tmpMessageId = _cursor.getLong(_cursorIndexOfMessageId);
            final Long _tmpFolderId;
            if (_cursor.isNull(_cursorIndexOfFolderId)) {
              _tmpFolderId = null;
            } else {
              _tmpFolderId = _cursor.getLong(_cursorIndexOfFolderId);
            }
            final MediaKind _tmpMediaType;
            _tmpMediaType = __MediaKind_stringToEnum(_cursor.getString(_cursorIndexOfMediaType));
            final String _tmpFileName;
            if (_cursor.isNull(_cursorIndexOfFileName)) {
              _tmpFileName = null;
            } else {
              _tmpFileName = _cursor.getString(_cursorIndexOfFileName);
            }
            final String _tmpMimeType;
            if (_cursor.isNull(_cursorIndexOfMimeType)) {
              _tmpMimeType = null;
            } else {
              _tmpMimeType = _cursor.getString(_cursorIndexOfMimeType);
            }
            final long _tmpFileSize;
            _tmpFileSize = _cursor.getLong(_cursorIndexOfFileSize);
            final long _tmpDurationMs;
            _tmpDurationMs = _cursor.getLong(_cursorIndexOfDurationMs);
            final int _tmpWidth;
            _tmpWidth = _cursor.getInt(_cursorIndexOfWidth);
            final int _tmpHeight;
            _tmpHeight = _cursor.getInt(_cursorIndexOfHeight);
            final String _tmpCaptionText;
            if (_cursor.isNull(_cursorIndexOfCaptionText)) {
              _tmpCaptionText = null;
            } else {
              _tmpCaptionText = _cursor.getString(_cursorIndexOfCaptionText);
            }
            final String _tmpCaptionEntitiesJson;
            if (_cursor.isNull(_cursorIndexOfCaptionEntitiesJson)) {
              _tmpCaptionEntitiesJson = null;
            } else {
              _tmpCaptionEntitiesJson = _cursor.getString(_cursorIndexOfCaptionEntitiesJson);
            }
            final int _tmpRemoteFileId;
            _tmpRemoteFileId = _cursor.getInt(_cursorIndexOfRemoteFileId);
            final int _tmpLocalFileId;
            _tmpLocalFileId = _cursor.getInt(_cursorIndexOfLocalFileId);
            final int _tmpThumbnailFileId;
            _tmpThumbnailFileId = _cursor.getInt(_cursorIndexOfThumbnailFileId);
            final String _tmpLocalPath;
            if (_cursor.isNull(_cursorIndexOfLocalPath)) {
              _tmpLocalPath = null;
            } else {
              _tmpLocalPath = _cursor.getString(_cursorIndexOfLocalPath);
            }
            final boolean _tmpIsDownloaded;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsDownloaded);
            _tmpIsDownloaded = _tmp != 0;
            final UploadState _tmpUploadState;
            _tmpUploadState = __UploadState_stringToEnum(_cursor.getString(_cursorIndexOfUploadState));
            final int _tmpUploadProgress;
            _tmpUploadProgress = _cursor.getInt(_cursorIndexOfUploadProgress);
            final String _tmpUploadError;
            if (_cursor.isNull(_cursorIndexOfUploadError)) {
              _tmpUploadError = null;
            } else {
              _tmpUploadError = _cursor.getString(_cursorIndexOfUploadError);
            }
            final String _tmpLocalContentHash;
            if (_cursor.isNull(_cursorIndexOfLocalContentHash)) {
              _tmpLocalContentHash = null;
            } else {
              _tmpLocalContentHash = _cursor.getString(_cursorIndexOfLocalContentHash);
            }
            final long _tmpMessageDate;
            _tmpMessageDate = _cursor.getLong(_cursorIndexOfMessageDate);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new MediaItemEntity(_tmpId,_tmpRemoteId,_tmpChatId,_tmpMessageId,_tmpFolderId,_tmpMediaType,_tmpFileName,_tmpMimeType,_tmpFileSize,_tmpDurationMs,_tmpWidth,_tmpHeight,_tmpCaptionText,_tmpCaptionEntitiesJson,_tmpRemoteFileId,_tmpLocalFileId,_tmpThumbnailFileId,_tmpLocalPath,_tmpIsDownloaded,_tmpUploadState,_tmpUploadProgress,_tmpUploadError,_tmpLocalContentHash,_tmpMessageDate,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Object getByChat(final long chatId,
      final Continuation<? super List<MediaItemEntity>> $completion) {
    final String _sql = "SELECT * FROM media_items WHERE chat_id = ? ORDER BY message_date DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, chatId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<MediaItemEntity>>() {
      @Override
      @NonNull
      public List<MediaItemEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remote_id");
          final int _cursorIndexOfChatId = CursorUtil.getColumnIndexOrThrow(_cursor, "chat_id");
          final int _cursorIndexOfMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "message_id");
          final int _cursorIndexOfFolderId = CursorUtil.getColumnIndexOrThrow(_cursor, "folder_id");
          final int _cursorIndexOfMediaType = CursorUtil.getColumnIndexOrThrow(_cursor, "media_type");
          final int _cursorIndexOfFileName = CursorUtil.getColumnIndexOrThrow(_cursor, "file_name");
          final int _cursorIndexOfMimeType = CursorUtil.getColumnIndexOrThrow(_cursor, "mime_type");
          final int _cursorIndexOfFileSize = CursorUtil.getColumnIndexOrThrow(_cursor, "file_size");
          final int _cursorIndexOfDurationMs = CursorUtil.getColumnIndexOrThrow(_cursor, "duration_ms");
          final int _cursorIndexOfWidth = CursorUtil.getColumnIndexOrThrow(_cursor, "width");
          final int _cursorIndexOfHeight = CursorUtil.getColumnIndexOrThrow(_cursor, "height");
          final int _cursorIndexOfCaptionText = CursorUtil.getColumnIndexOrThrow(_cursor, "caption_text");
          final int _cursorIndexOfCaptionEntitiesJson = CursorUtil.getColumnIndexOrThrow(_cursor, "caption_entities_json");
          final int _cursorIndexOfRemoteFileId = CursorUtil.getColumnIndexOrThrow(_cursor, "remote_file_id");
          final int _cursorIndexOfLocalFileId = CursorUtil.getColumnIndexOrThrow(_cursor, "local_file_id");
          final int _cursorIndexOfThumbnailFileId = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbnail_file_id");
          final int _cursorIndexOfLocalPath = CursorUtil.getColumnIndexOrThrow(_cursor, "local_path");
          final int _cursorIndexOfIsDownloaded = CursorUtil.getColumnIndexOrThrow(_cursor, "is_downloaded");
          final int _cursorIndexOfUploadState = CursorUtil.getColumnIndexOrThrow(_cursor, "upload_state");
          final int _cursorIndexOfUploadProgress = CursorUtil.getColumnIndexOrThrow(_cursor, "upload_progress");
          final int _cursorIndexOfUploadError = CursorUtil.getColumnIndexOrThrow(_cursor, "upload_error");
          final int _cursorIndexOfLocalContentHash = CursorUtil.getColumnIndexOrThrow(_cursor, "local_content_hash");
          final int _cursorIndexOfMessageDate = CursorUtil.getColumnIndexOrThrow(_cursor, "message_date");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final List<MediaItemEntity> _result = new ArrayList<MediaItemEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MediaItemEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpRemoteId;
            _tmpRemoteId = _cursor.getString(_cursorIndexOfRemoteId);
            final long _tmpChatId;
            _tmpChatId = _cursor.getLong(_cursorIndexOfChatId);
            final long _tmpMessageId;
            _tmpMessageId = _cursor.getLong(_cursorIndexOfMessageId);
            final Long _tmpFolderId;
            if (_cursor.isNull(_cursorIndexOfFolderId)) {
              _tmpFolderId = null;
            } else {
              _tmpFolderId = _cursor.getLong(_cursorIndexOfFolderId);
            }
            final MediaKind _tmpMediaType;
            _tmpMediaType = __MediaKind_stringToEnum(_cursor.getString(_cursorIndexOfMediaType));
            final String _tmpFileName;
            if (_cursor.isNull(_cursorIndexOfFileName)) {
              _tmpFileName = null;
            } else {
              _tmpFileName = _cursor.getString(_cursorIndexOfFileName);
            }
            final String _tmpMimeType;
            if (_cursor.isNull(_cursorIndexOfMimeType)) {
              _tmpMimeType = null;
            } else {
              _tmpMimeType = _cursor.getString(_cursorIndexOfMimeType);
            }
            final long _tmpFileSize;
            _tmpFileSize = _cursor.getLong(_cursorIndexOfFileSize);
            final long _tmpDurationMs;
            _tmpDurationMs = _cursor.getLong(_cursorIndexOfDurationMs);
            final int _tmpWidth;
            _tmpWidth = _cursor.getInt(_cursorIndexOfWidth);
            final int _tmpHeight;
            _tmpHeight = _cursor.getInt(_cursorIndexOfHeight);
            final String _tmpCaptionText;
            if (_cursor.isNull(_cursorIndexOfCaptionText)) {
              _tmpCaptionText = null;
            } else {
              _tmpCaptionText = _cursor.getString(_cursorIndexOfCaptionText);
            }
            final String _tmpCaptionEntitiesJson;
            if (_cursor.isNull(_cursorIndexOfCaptionEntitiesJson)) {
              _tmpCaptionEntitiesJson = null;
            } else {
              _tmpCaptionEntitiesJson = _cursor.getString(_cursorIndexOfCaptionEntitiesJson);
            }
            final int _tmpRemoteFileId;
            _tmpRemoteFileId = _cursor.getInt(_cursorIndexOfRemoteFileId);
            final int _tmpLocalFileId;
            _tmpLocalFileId = _cursor.getInt(_cursorIndexOfLocalFileId);
            final int _tmpThumbnailFileId;
            _tmpThumbnailFileId = _cursor.getInt(_cursorIndexOfThumbnailFileId);
            final String _tmpLocalPath;
            if (_cursor.isNull(_cursorIndexOfLocalPath)) {
              _tmpLocalPath = null;
            } else {
              _tmpLocalPath = _cursor.getString(_cursorIndexOfLocalPath);
            }
            final boolean _tmpIsDownloaded;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsDownloaded);
            _tmpIsDownloaded = _tmp != 0;
            final UploadState _tmpUploadState;
            _tmpUploadState = __UploadState_stringToEnum(_cursor.getString(_cursorIndexOfUploadState));
            final int _tmpUploadProgress;
            _tmpUploadProgress = _cursor.getInt(_cursorIndexOfUploadProgress);
            final String _tmpUploadError;
            if (_cursor.isNull(_cursorIndexOfUploadError)) {
              _tmpUploadError = null;
            } else {
              _tmpUploadError = _cursor.getString(_cursorIndexOfUploadError);
            }
            final String _tmpLocalContentHash;
            if (_cursor.isNull(_cursorIndexOfLocalContentHash)) {
              _tmpLocalContentHash = null;
            } else {
              _tmpLocalContentHash = _cursor.getString(_cursorIndexOfLocalContentHash);
            }
            final long _tmpMessageDate;
            _tmpMessageDate = _cursor.getLong(_cursorIndexOfMessageDate);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new MediaItemEntity(_tmpId,_tmpRemoteId,_tmpChatId,_tmpMessageId,_tmpFolderId,_tmpMediaType,_tmpFileName,_tmpMimeType,_tmpFileSize,_tmpDurationMs,_tmpWidth,_tmpHeight,_tmpCaptionText,_tmpCaptionEntitiesJson,_tmpRemoteFileId,_tmpLocalFileId,_tmpThumbnailFileId,_tmpLocalPath,_tmpIsDownloaded,_tmpUploadState,_tmpUploadProgress,_tmpUploadError,_tmpLocalContentHash,_tmpMessageDate,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Object getAll(final Continuation<? super List<MediaItemEntity>> $completion) {
    final String _sql = "SELECT * FROM media_items ORDER BY message_date DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<MediaItemEntity>>() {
      @Override
      @NonNull
      public List<MediaItemEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remote_id");
          final int _cursorIndexOfChatId = CursorUtil.getColumnIndexOrThrow(_cursor, "chat_id");
          final int _cursorIndexOfMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "message_id");
          final int _cursorIndexOfFolderId = CursorUtil.getColumnIndexOrThrow(_cursor, "folder_id");
          final int _cursorIndexOfMediaType = CursorUtil.getColumnIndexOrThrow(_cursor, "media_type");
          final int _cursorIndexOfFileName = CursorUtil.getColumnIndexOrThrow(_cursor, "file_name");
          final int _cursorIndexOfMimeType = CursorUtil.getColumnIndexOrThrow(_cursor, "mime_type");
          final int _cursorIndexOfFileSize = CursorUtil.getColumnIndexOrThrow(_cursor, "file_size");
          final int _cursorIndexOfDurationMs = CursorUtil.getColumnIndexOrThrow(_cursor, "duration_ms");
          final int _cursorIndexOfWidth = CursorUtil.getColumnIndexOrThrow(_cursor, "width");
          final int _cursorIndexOfHeight = CursorUtil.getColumnIndexOrThrow(_cursor, "height");
          final int _cursorIndexOfCaptionText = CursorUtil.getColumnIndexOrThrow(_cursor, "caption_text");
          final int _cursorIndexOfCaptionEntitiesJson = CursorUtil.getColumnIndexOrThrow(_cursor, "caption_entities_json");
          final int _cursorIndexOfRemoteFileId = CursorUtil.getColumnIndexOrThrow(_cursor, "remote_file_id");
          final int _cursorIndexOfLocalFileId = CursorUtil.getColumnIndexOrThrow(_cursor, "local_file_id");
          final int _cursorIndexOfThumbnailFileId = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbnail_file_id");
          final int _cursorIndexOfLocalPath = CursorUtil.getColumnIndexOrThrow(_cursor, "local_path");
          final int _cursorIndexOfIsDownloaded = CursorUtil.getColumnIndexOrThrow(_cursor, "is_downloaded");
          final int _cursorIndexOfUploadState = CursorUtil.getColumnIndexOrThrow(_cursor, "upload_state");
          final int _cursorIndexOfUploadProgress = CursorUtil.getColumnIndexOrThrow(_cursor, "upload_progress");
          final int _cursorIndexOfUploadError = CursorUtil.getColumnIndexOrThrow(_cursor, "upload_error");
          final int _cursorIndexOfLocalContentHash = CursorUtil.getColumnIndexOrThrow(_cursor, "local_content_hash");
          final int _cursorIndexOfMessageDate = CursorUtil.getColumnIndexOrThrow(_cursor, "message_date");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final List<MediaItemEntity> _result = new ArrayList<MediaItemEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MediaItemEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpRemoteId;
            _tmpRemoteId = _cursor.getString(_cursorIndexOfRemoteId);
            final long _tmpChatId;
            _tmpChatId = _cursor.getLong(_cursorIndexOfChatId);
            final long _tmpMessageId;
            _tmpMessageId = _cursor.getLong(_cursorIndexOfMessageId);
            final Long _tmpFolderId;
            if (_cursor.isNull(_cursorIndexOfFolderId)) {
              _tmpFolderId = null;
            } else {
              _tmpFolderId = _cursor.getLong(_cursorIndexOfFolderId);
            }
            final MediaKind _tmpMediaType;
            _tmpMediaType = __MediaKind_stringToEnum(_cursor.getString(_cursorIndexOfMediaType));
            final String _tmpFileName;
            if (_cursor.isNull(_cursorIndexOfFileName)) {
              _tmpFileName = null;
            } else {
              _tmpFileName = _cursor.getString(_cursorIndexOfFileName);
            }
            final String _tmpMimeType;
            if (_cursor.isNull(_cursorIndexOfMimeType)) {
              _tmpMimeType = null;
            } else {
              _tmpMimeType = _cursor.getString(_cursorIndexOfMimeType);
            }
            final long _tmpFileSize;
            _tmpFileSize = _cursor.getLong(_cursorIndexOfFileSize);
            final long _tmpDurationMs;
            _tmpDurationMs = _cursor.getLong(_cursorIndexOfDurationMs);
            final int _tmpWidth;
            _tmpWidth = _cursor.getInt(_cursorIndexOfWidth);
            final int _tmpHeight;
            _tmpHeight = _cursor.getInt(_cursorIndexOfHeight);
            final String _tmpCaptionText;
            if (_cursor.isNull(_cursorIndexOfCaptionText)) {
              _tmpCaptionText = null;
            } else {
              _tmpCaptionText = _cursor.getString(_cursorIndexOfCaptionText);
            }
            final String _tmpCaptionEntitiesJson;
            if (_cursor.isNull(_cursorIndexOfCaptionEntitiesJson)) {
              _tmpCaptionEntitiesJson = null;
            } else {
              _tmpCaptionEntitiesJson = _cursor.getString(_cursorIndexOfCaptionEntitiesJson);
            }
            final int _tmpRemoteFileId;
            _tmpRemoteFileId = _cursor.getInt(_cursorIndexOfRemoteFileId);
            final int _tmpLocalFileId;
            _tmpLocalFileId = _cursor.getInt(_cursorIndexOfLocalFileId);
            final int _tmpThumbnailFileId;
            _tmpThumbnailFileId = _cursor.getInt(_cursorIndexOfThumbnailFileId);
            final String _tmpLocalPath;
            if (_cursor.isNull(_cursorIndexOfLocalPath)) {
              _tmpLocalPath = null;
            } else {
              _tmpLocalPath = _cursor.getString(_cursorIndexOfLocalPath);
            }
            final boolean _tmpIsDownloaded;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsDownloaded);
            _tmpIsDownloaded = _tmp != 0;
            final UploadState _tmpUploadState;
            _tmpUploadState = __UploadState_stringToEnum(_cursor.getString(_cursorIndexOfUploadState));
            final int _tmpUploadProgress;
            _tmpUploadProgress = _cursor.getInt(_cursorIndexOfUploadProgress);
            final String _tmpUploadError;
            if (_cursor.isNull(_cursorIndexOfUploadError)) {
              _tmpUploadError = null;
            } else {
              _tmpUploadError = _cursor.getString(_cursorIndexOfUploadError);
            }
            final String _tmpLocalContentHash;
            if (_cursor.isNull(_cursorIndexOfLocalContentHash)) {
              _tmpLocalContentHash = null;
            } else {
              _tmpLocalContentHash = _cursor.getString(_cursorIndexOfLocalContentHash);
            }
            final long _tmpMessageDate;
            _tmpMessageDate = _cursor.getLong(_cursorIndexOfMessageDate);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new MediaItemEntity(_tmpId,_tmpRemoteId,_tmpChatId,_tmpMessageId,_tmpFolderId,_tmpMediaType,_tmpFileName,_tmpMimeType,_tmpFileSize,_tmpDurationMs,_tmpWidth,_tmpHeight,_tmpCaptionText,_tmpCaptionEntitiesJson,_tmpRemoteFileId,_tmpLocalFileId,_tmpThumbnailFileId,_tmpLocalPath,_tmpIsDownloaded,_tmpUploadState,_tmpUploadProgress,_tmpUploadError,_tmpLocalContentHash,_tmpMessageDate,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Object pendingUploads(final Continuation<? super List<MediaItemEntity>> $completion) {
    final String _sql = "SELECT * FROM media_items WHERE upload_state IN ('PENDING', 'FAILED') ORDER BY updated_at ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<MediaItemEntity>>() {
      @Override
      @NonNull
      public List<MediaItemEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remote_id");
          final int _cursorIndexOfChatId = CursorUtil.getColumnIndexOrThrow(_cursor, "chat_id");
          final int _cursorIndexOfMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "message_id");
          final int _cursorIndexOfFolderId = CursorUtil.getColumnIndexOrThrow(_cursor, "folder_id");
          final int _cursorIndexOfMediaType = CursorUtil.getColumnIndexOrThrow(_cursor, "media_type");
          final int _cursorIndexOfFileName = CursorUtil.getColumnIndexOrThrow(_cursor, "file_name");
          final int _cursorIndexOfMimeType = CursorUtil.getColumnIndexOrThrow(_cursor, "mime_type");
          final int _cursorIndexOfFileSize = CursorUtil.getColumnIndexOrThrow(_cursor, "file_size");
          final int _cursorIndexOfDurationMs = CursorUtil.getColumnIndexOrThrow(_cursor, "duration_ms");
          final int _cursorIndexOfWidth = CursorUtil.getColumnIndexOrThrow(_cursor, "width");
          final int _cursorIndexOfHeight = CursorUtil.getColumnIndexOrThrow(_cursor, "height");
          final int _cursorIndexOfCaptionText = CursorUtil.getColumnIndexOrThrow(_cursor, "caption_text");
          final int _cursorIndexOfCaptionEntitiesJson = CursorUtil.getColumnIndexOrThrow(_cursor, "caption_entities_json");
          final int _cursorIndexOfRemoteFileId = CursorUtil.getColumnIndexOrThrow(_cursor, "remote_file_id");
          final int _cursorIndexOfLocalFileId = CursorUtil.getColumnIndexOrThrow(_cursor, "local_file_id");
          final int _cursorIndexOfThumbnailFileId = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbnail_file_id");
          final int _cursorIndexOfLocalPath = CursorUtil.getColumnIndexOrThrow(_cursor, "local_path");
          final int _cursorIndexOfIsDownloaded = CursorUtil.getColumnIndexOrThrow(_cursor, "is_downloaded");
          final int _cursorIndexOfUploadState = CursorUtil.getColumnIndexOrThrow(_cursor, "upload_state");
          final int _cursorIndexOfUploadProgress = CursorUtil.getColumnIndexOrThrow(_cursor, "upload_progress");
          final int _cursorIndexOfUploadError = CursorUtil.getColumnIndexOrThrow(_cursor, "upload_error");
          final int _cursorIndexOfLocalContentHash = CursorUtil.getColumnIndexOrThrow(_cursor, "local_content_hash");
          final int _cursorIndexOfMessageDate = CursorUtil.getColumnIndexOrThrow(_cursor, "message_date");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final List<MediaItemEntity> _result = new ArrayList<MediaItemEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MediaItemEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpRemoteId;
            _tmpRemoteId = _cursor.getString(_cursorIndexOfRemoteId);
            final long _tmpChatId;
            _tmpChatId = _cursor.getLong(_cursorIndexOfChatId);
            final long _tmpMessageId;
            _tmpMessageId = _cursor.getLong(_cursorIndexOfMessageId);
            final Long _tmpFolderId;
            if (_cursor.isNull(_cursorIndexOfFolderId)) {
              _tmpFolderId = null;
            } else {
              _tmpFolderId = _cursor.getLong(_cursorIndexOfFolderId);
            }
            final MediaKind _tmpMediaType;
            _tmpMediaType = __MediaKind_stringToEnum(_cursor.getString(_cursorIndexOfMediaType));
            final String _tmpFileName;
            if (_cursor.isNull(_cursorIndexOfFileName)) {
              _tmpFileName = null;
            } else {
              _tmpFileName = _cursor.getString(_cursorIndexOfFileName);
            }
            final String _tmpMimeType;
            if (_cursor.isNull(_cursorIndexOfMimeType)) {
              _tmpMimeType = null;
            } else {
              _tmpMimeType = _cursor.getString(_cursorIndexOfMimeType);
            }
            final long _tmpFileSize;
            _tmpFileSize = _cursor.getLong(_cursorIndexOfFileSize);
            final long _tmpDurationMs;
            _tmpDurationMs = _cursor.getLong(_cursorIndexOfDurationMs);
            final int _tmpWidth;
            _tmpWidth = _cursor.getInt(_cursorIndexOfWidth);
            final int _tmpHeight;
            _tmpHeight = _cursor.getInt(_cursorIndexOfHeight);
            final String _tmpCaptionText;
            if (_cursor.isNull(_cursorIndexOfCaptionText)) {
              _tmpCaptionText = null;
            } else {
              _tmpCaptionText = _cursor.getString(_cursorIndexOfCaptionText);
            }
            final String _tmpCaptionEntitiesJson;
            if (_cursor.isNull(_cursorIndexOfCaptionEntitiesJson)) {
              _tmpCaptionEntitiesJson = null;
            } else {
              _tmpCaptionEntitiesJson = _cursor.getString(_cursorIndexOfCaptionEntitiesJson);
            }
            final int _tmpRemoteFileId;
            _tmpRemoteFileId = _cursor.getInt(_cursorIndexOfRemoteFileId);
            final int _tmpLocalFileId;
            _tmpLocalFileId = _cursor.getInt(_cursorIndexOfLocalFileId);
            final int _tmpThumbnailFileId;
            _tmpThumbnailFileId = _cursor.getInt(_cursorIndexOfThumbnailFileId);
            final String _tmpLocalPath;
            if (_cursor.isNull(_cursorIndexOfLocalPath)) {
              _tmpLocalPath = null;
            } else {
              _tmpLocalPath = _cursor.getString(_cursorIndexOfLocalPath);
            }
            final boolean _tmpIsDownloaded;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsDownloaded);
            _tmpIsDownloaded = _tmp != 0;
            final UploadState _tmpUploadState;
            _tmpUploadState = __UploadState_stringToEnum(_cursor.getString(_cursorIndexOfUploadState));
            final int _tmpUploadProgress;
            _tmpUploadProgress = _cursor.getInt(_cursorIndexOfUploadProgress);
            final String _tmpUploadError;
            if (_cursor.isNull(_cursorIndexOfUploadError)) {
              _tmpUploadError = null;
            } else {
              _tmpUploadError = _cursor.getString(_cursorIndexOfUploadError);
            }
            final String _tmpLocalContentHash;
            if (_cursor.isNull(_cursorIndexOfLocalContentHash)) {
              _tmpLocalContentHash = null;
            } else {
              _tmpLocalContentHash = _cursor.getString(_cursorIndexOfLocalContentHash);
            }
            final long _tmpMessageDate;
            _tmpMessageDate = _cursor.getLong(_cursorIndexOfMessageDate);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new MediaItemEntity(_tmpId,_tmpRemoteId,_tmpChatId,_tmpMessageId,_tmpFolderId,_tmpMediaType,_tmpFileName,_tmpMimeType,_tmpFileSize,_tmpDurationMs,_tmpWidth,_tmpHeight,_tmpCaptionText,_tmpCaptionEntitiesJson,_tmpRemoteFileId,_tmpLocalFileId,_tmpThumbnailFileId,_tmpLocalPath,_tmpIsDownloaded,_tmpUploadState,_tmpUploadProgress,_tmpUploadError,_tmpLocalContentHash,_tmpMessageDate,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Object notYetUploadedInFolder(final long folderId,
      final Continuation<? super List<MediaItemEntity>> $completion) {
    final String _sql = "\n"
            + "        SELECT * FROM media_items\n"
            + "         WHERE folder_id = ?\n"
            + "           AND upload_state != 'UPLOADED'\n"
            + "         ORDER BY message_date DESC\n"
            + "        ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, folderId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<MediaItemEntity>>() {
      @Override
      @NonNull
      public List<MediaItemEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remote_id");
          final int _cursorIndexOfChatId = CursorUtil.getColumnIndexOrThrow(_cursor, "chat_id");
          final int _cursorIndexOfMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "message_id");
          final int _cursorIndexOfFolderId = CursorUtil.getColumnIndexOrThrow(_cursor, "folder_id");
          final int _cursorIndexOfMediaType = CursorUtil.getColumnIndexOrThrow(_cursor, "media_type");
          final int _cursorIndexOfFileName = CursorUtil.getColumnIndexOrThrow(_cursor, "file_name");
          final int _cursorIndexOfMimeType = CursorUtil.getColumnIndexOrThrow(_cursor, "mime_type");
          final int _cursorIndexOfFileSize = CursorUtil.getColumnIndexOrThrow(_cursor, "file_size");
          final int _cursorIndexOfDurationMs = CursorUtil.getColumnIndexOrThrow(_cursor, "duration_ms");
          final int _cursorIndexOfWidth = CursorUtil.getColumnIndexOrThrow(_cursor, "width");
          final int _cursorIndexOfHeight = CursorUtil.getColumnIndexOrThrow(_cursor, "height");
          final int _cursorIndexOfCaptionText = CursorUtil.getColumnIndexOrThrow(_cursor, "caption_text");
          final int _cursorIndexOfCaptionEntitiesJson = CursorUtil.getColumnIndexOrThrow(_cursor, "caption_entities_json");
          final int _cursorIndexOfRemoteFileId = CursorUtil.getColumnIndexOrThrow(_cursor, "remote_file_id");
          final int _cursorIndexOfLocalFileId = CursorUtil.getColumnIndexOrThrow(_cursor, "local_file_id");
          final int _cursorIndexOfThumbnailFileId = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbnail_file_id");
          final int _cursorIndexOfLocalPath = CursorUtil.getColumnIndexOrThrow(_cursor, "local_path");
          final int _cursorIndexOfIsDownloaded = CursorUtil.getColumnIndexOrThrow(_cursor, "is_downloaded");
          final int _cursorIndexOfUploadState = CursorUtil.getColumnIndexOrThrow(_cursor, "upload_state");
          final int _cursorIndexOfUploadProgress = CursorUtil.getColumnIndexOrThrow(_cursor, "upload_progress");
          final int _cursorIndexOfUploadError = CursorUtil.getColumnIndexOrThrow(_cursor, "upload_error");
          final int _cursorIndexOfLocalContentHash = CursorUtil.getColumnIndexOrThrow(_cursor, "local_content_hash");
          final int _cursorIndexOfMessageDate = CursorUtil.getColumnIndexOrThrow(_cursor, "message_date");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final List<MediaItemEntity> _result = new ArrayList<MediaItemEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MediaItemEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpRemoteId;
            _tmpRemoteId = _cursor.getString(_cursorIndexOfRemoteId);
            final long _tmpChatId;
            _tmpChatId = _cursor.getLong(_cursorIndexOfChatId);
            final long _tmpMessageId;
            _tmpMessageId = _cursor.getLong(_cursorIndexOfMessageId);
            final Long _tmpFolderId;
            if (_cursor.isNull(_cursorIndexOfFolderId)) {
              _tmpFolderId = null;
            } else {
              _tmpFolderId = _cursor.getLong(_cursorIndexOfFolderId);
            }
            final MediaKind _tmpMediaType;
            _tmpMediaType = __MediaKind_stringToEnum(_cursor.getString(_cursorIndexOfMediaType));
            final String _tmpFileName;
            if (_cursor.isNull(_cursorIndexOfFileName)) {
              _tmpFileName = null;
            } else {
              _tmpFileName = _cursor.getString(_cursorIndexOfFileName);
            }
            final String _tmpMimeType;
            if (_cursor.isNull(_cursorIndexOfMimeType)) {
              _tmpMimeType = null;
            } else {
              _tmpMimeType = _cursor.getString(_cursorIndexOfMimeType);
            }
            final long _tmpFileSize;
            _tmpFileSize = _cursor.getLong(_cursorIndexOfFileSize);
            final long _tmpDurationMs;
            _tmpDurationMs = _cursor.getLong(_cursorIndexOfDurationMs);
            final int _tmpWidth;
            _tmpWidth = _cursor.getInt(_cursorIndexOfWidth);
            final int _tmpHeight;
            _tmpHeight = _cursor.getInt(_cursorIndexOfHeight);
            final String _tmpCaptionText;
            if (_cursor.isNull(_cursorIndexOfCaptionText)) {
              _tmpCaptionText = null;
            } else {
              _tmpCaptionText = _cursor.getString(_cursorIndexOfCaptionText);
            }
            final String _tmpCaptionEntitiesJson;
            if (_cursor.isNull(_cursorIndexOfCaptionEntitiesJson)) {
              _tmpCaptionEntitiesJson = null;
            } else {
              _tmpCaptionEntitiesJson = _cursor.getString(_cursorIndexOfCaptionEntitiesJson);
            }
            final int _tmpRemoteFileId;
            _tmpRemoteFileId = _cursor.getInt(_cursorIndexOfRemoteFileId);
            final int _tmpLocalFileId;
            _tmpLocalFileId = _cursor.getInt(_cursorIndexOfLocalFileId);
            final int _tmpThumbnailFileId;
            _tmpThumbnailFileId = _cursor.getInt(_cursorIndexOfThumbnailFileId);
            final String _tmpLocalPath;
            if (_cursor.isNull(_cursorIndexOfLocalPath)) {
              _tmpLocalPath = null;
            } else {
              _tmpLocalPath = _cursor.getString(_cursorIndexOfLocalPath);
            }
            final boolean _tmpIsDownloaded;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsDownloaded);
            _tmpIsDownloaded = _tmp != 0;
            final UploadState _tmpUploadState;
            _tmpUploadState = __UploadState_stringToEnum(_cursor.getString(_cursorIndexOfUploadState));
            final int _tmpUploadProgress;
            _tmpUploadProgress = _cursor.getInt(_cursorIndexOfUploadProgress);
            final String _tmpUploadError;
            if (_cursor.isNull(_cursorIndexOfUploadError)) {
              _tmpUploadError = null;
            } else {
              _tmpUploadError = _cursor.getString(_cursorIndexOfUploadError);
            }
            final String _tmpLocalContentHash;
            if (_cursor.isNull(_cursorIndexOfLocalContentHash)) {
              _tmpLocalContentHash = null;
            } else {
              _tmpLocalContentHash = _cursor.getString(_cursorIndexOfLocalContentHash);
            }
            final long _tmpMessageDate;
            _tmpMessageDate = _cursor.getLong(_cursorIndexOfMessageDate);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new MediaItemEntity(_tmpId,_tmpRemoteId,_tmpChatId,_tmpMessageId,_tmpFolderId,_tmpMediaType,_tmpFileName,_tmpMimeType,_tmpFileSize,_tmpDurationMs,_tmpWidth,_tmpHeight,_tmpCaptionText,_tmpCaptionEntitiesJson,_tmpRemoteFileId,_tmpLocalFileId,_tmpThumbnailFileId,_tmpLocalPath,_tmpIsDownloaded,_tmpUploadState,_tmpUploadProgress,_tmpUploadError,_tmpLocalContentHash,_tmpMessageDate,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Flow<List<MediaItemEntity>> observeInFolders(final List<Long> folderIds) {
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT * FROM media_items WHERE folder_id IN (");
    final int _inputSize = folderIds.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(") ORDER BY message_date DESC");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (long _item : folderIds) {
      _statement.bindLong(_argIndex, _item);
      _argIndex++;
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"media_items"}, new Callable<List<MediaItemEntity>>() {
      @Override
      @NonNull
      public List<MediaItemEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remote_id");
          final int _cursorIndexOfChatId = CursorUtil.getColumnIndexOrThrow(_cursor, "chat_id");
          final int _cursorIndexOfMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "message_id");
          final int _cursorIndexOfFolderId = CursorUtil.getColumnIndexOrThrow(_cursor, "folder_id");
          final int _cursorIndexOfMediaType = CursorUtil.getColumnIndexOrThrow(_cursor, "media_type");
          final int _cursorIndexOfFileName = CursorUtil.getColumnIndexOrThrow(_cursor, "file_name");
          final int _cursorIndexOfMimeType = CursorUtil.getColumnIndexOrThrow(_cursor, "mime_type");
          final int _cursorIndexOfFileSize = CursorUtil.getColumnIndexOrThrow(_cursor, "file_size");
          final int _cursorIndexOfDurationMs = CursorUtil.getColumnIndexOrThrow(_cursor, "duration_ms");
          final int _cursorIndexOfWidth = CursorUtil.getColumnIndexOrThrow(_cursor, "width");
          final int _cursorIndexOfHeight = CursorUtil.getColumnIndexOrThrow(_cursor, "height");
          final int _cursorIndexOfCaptionText = CursorUtil.getColumnIndexOrThrow(_cursor, "caption_text");
          final int _cursorIndexOfCaptionEntitiesJson = CursorUtil.getColumnIndexOrThrow(_cursor, "caption_entities_json");
          final int _cursorIndexOfRemoteFileId = CursorUtil.getColumnIndexOrThrow(_cursor, "remote_file_id");
          final int _cursorIndexOfLocalFileId = CursorUtil.getColumnIndexOrThrow(_cursor, "local_file_id");
          final int _cursorIndexOfThumbnailFileId = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbnail_file_id");
          final int _cursorIndexOfLocalPath = CursorUtil.getColumnIndexOrThrow(_cursor, "local_path");
          final int _cursorIndexOfIsDownloaded = CursorUtil.getColumnIndexOrThrow(_cursor, "is_downloaded");
          final int _cursorIndexOfUploadState = CursorUtil.getColumnIndexOrThrow(_cursor, "upload_state");
          final int _cursorIndexOfUploadProgress = CursorUtil.getColumnIndexOrThrow(_cursor, "upload_progress");
          final int _cursorIndexOfUploadError = CursorUtil.getColumnIndexOrThrow(_cursor, "upload_error");
          final int _cursorIndexOfLocalContentHash = CursorUtil.getColumnIndexOrThrow(_cursor, "local_content_hash");
          final int _cursorIndexOfMessageDate = CursorUtil.getColumnIndexOrThrow(_cursor, "message_date");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final List<MediaItemEntity> _result = new ArrayList<MediaItemEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MediaItemEntity _item_1;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpRemoteId;
            _tmpRemoteId = _cursor.getString(_cursorIndexOfRemoteId);
            final long _tmpChatId;
            _tmpChatId = _cursor.getLong(_cursorIndexOfChatId);
            final long _tmpMessageId;
            _tmpMessageId = _cursor.getLong(_cursorIndexOfMessageId);
            final Long _tmpFolderId;
            if (_cursor.isNull(_cursorIndexOfFolderId)) {
              _tmpFolderId = null;
            } else {
              _tmpFolderId = _cursor.getLong(_cursorIndexOfFolderId);
            }
            final MediaKind _tmpMediaType;
            _tmpMediaType = __MediaKind_stringToEnum(_cursor.getString(_cursorIndexOfMediaType));
            final String _tmpFileName;
            if (_cursor.isNull(_cursorIndexOfFileName)) {
              _tmpFileName = null;
            } else {
              _tmpFileName = _cursor.getString(_cursorIndexOfFileName);
            }
            final String _tmpMimeType;
            if (_cursor.isNull(_cursorIndexOfMimeType)) {
              _tmpMimeType = null;
            } else {
              _tmpMimeType = _cursor.getString(_cursorIndexOfMimeType);
            }
            final long _tmpFileSize;
            _tmpFileSize = _cursor.getLong(_cursorIndexOfFileSize);
            final long _tmpDurationMs;
            _tmpDurationMs = _cursor.getLong(_cursorIndexOfDurationMs);
            final int _tmpWidth;
            _tmpWidth = _cursor.getInt(_cursorIndexOfWidth);
            final int _tmpHeight;
            _tmpHeight = _cursor.getInt(_cursorIndexOfHeight);
            final String _tmpCaptionText;
            if (_cursor.isNull(_cursorIndexOfCaptionText)) {
              _tmpCaptionText = null;
            } else {
              _tmpCaptionText = _cursor.getString(_cursorIndexOfCaptionText);
            }
            final String _tmpCaptionEntitiesJson;
            if (_cursor.isNull(_cursorIndexOfCaptionEntitiesJson)) {
              _tmpCaptionEntitiesJson = null;
            } else {
              _tmpCaptionEntitiesJson = _cursor.getString(_cursorIndexOfCaptionEntitiesJson);
            }
            final int _tmpRemoteFileId;
            _tmpRemoteFileId = _cursor.getInt(_cursorIndexOfRemoteFileId);
            final int _tmpLocalFileId;
            _tmpLocalFileId = _cursor.getInt(_cursorIndexOfLocalFileId);
            final int _tmpThumbnailFileId;
            _tmpThumbnailFileId = _cursor.getInt(_cursorIndexOfThumbnailFileId);
            final String _tmpLocalPath;
            if (_cursor.isNull(_cursorIndexOfLocalPath)) {
              _tmpLocalPath = null;
            } else {
              _tmpLocalPath = _cursor.getString(_cursorIndexOfLocalPath);
            }
            final boolean _tmpIsDownloaded;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsDownloaded);
            _tmpIsDownloaded = _tmp != 0;
            final UploadState _tmpUploadState;
            _tmpUploadState = __UploadState_stringToEnum(_cursor.getString(_cursorIndexOfUploadState));
            final int _tmpUploadProgress;
            _tmpUploadProgress = _cursor.getInt(_cursorIndexOfUploadProgress);
            final String _tmpUploadError;
            if (_cursor.isNull(_cursorIndexOfUploadError)) {
              _tmpUploadError = null;
            } else {
              _tmpUploadError = _cursor.getString(_cursorIndexOfUploadError);
            }
            final String _tmpLocalContentHash;
            if (_cursor.isNull(_cursorIndexOfLocalContentHash)) {
              _tmpLocalContentHash = null;
            } else {
              _tmpLocalContentHash = _cursor.getString(_cursorIndexOfLocalContentHash);
            }
            final long _tmpMessageDate;
            _tmpMessageDate = _cursor.getLong(_cursorIndexOfMessageDate);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item_1 = new MediaItemEntity(_tmpId,_tmpRemoteId,_tmpChatId,_tmpMessageId,_tmpFolderId,_tmpMediaType,_tmpFileName,_tmpMimeType,_tmpFileSize,_tmpDurationMs,_tmpWidth,_tmpHeight,_tmpCaptionText,_tmpCaptionEntitiesJson,_tmpRemoteFileId,_tmpLocalFileId,_tmpThumbnailFileId,_tmpLocalPath,_tmpIsDownloaded,_tmpUploadState,_tmpUploadProgress,_tmpUploadError,_tmpLocalContentHash,_tmpMessageDate,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Flow<List<MediaItemWithTags>> observeCardsInFolders(final List<Long> folderIds) {
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT * FROM media_items WHERE folder_id IN (");
    final int _inputSize = folderIds.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(") ORDER BY message_date DESC");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (long _item : folderIds) {
      _statement.bindLong(_argIndex, _item);
      _argIndex++;
    }
    return CoroutinesRoom.createFlow(__db, true, new String[] {"media_item_tags", "media_tags",
        "media_items"}, new Callable<List<MediaItemWithTags>>() {
      @Override
      @NonNull
      public List<MediaItemWithTags> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remote_id");
            final int _cursorIndexOfChatId = CursorUtil.getColumnIndexOrThrow(_cursor, "chat_id");
            final int _cursorIndexOfMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "message_id");
            final int _cursorIndexOfFolderId = CursorUtil.getColumnIndexOrThrow(_cursor, "folder_id");
            final int _cursorIndexOfMediaType = CursorUtil.getColumnIndexOrThrow(_cursor, "media_type");
            final int _cursorIndexOfFileName = CursorUtil.getColumnIndexOrThrow(_cursor, "file_name");
            final int _cursorIndexOfMimeType = CursorUtil.getColumnIndexOrThrow(_cursor, "mime_type");
            final int _cursorIndexOfFileSize = CursorUtil.getColumnIndexOrThrow(_cursor, "file_size");
            final int _cursorIndexOfDurationMs = CursorUtil.getColumnIndexOrThrow(_cursor, "duration_ms");
            final int _cursorIndexOfWidth = CursorUtil.getColumnIndexOrThrow(_cursor, "width");
            final int _cursorIndexOfHeight = CursorUtil.getColumnIndexOrThrow(_cursor, "height");
            final int _cursorIndexOfCaptionText = CursorUtil.getColumnIndexOrThrow(_cursor, "caption_text");
            final int _cursorIndexOfCaptionEntitiesJson = CursorUtil.getColumnIndexOrThrow(_cursor, "caption_entities_json");
            final int _cursorIndexOfRemoteFileId = CursorUtil.getColumnIndexOrThrow(_cursor, "remote_file_id");
            final int _cursorIndexOfLocalFileId = CursorUtil.getColumnIndexOrThrow(_cursor, "local_file_id");
            final int _cursorIndexOfThumbnailFileId = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbnail_file_id");
            final int _cursorIndexOfLocalPath = CursorUtil.getColumnIndexOrThrow(_cursor, "local_path");
            final int _cursorIndexOfIsDownloaded = CursorUtil.getColumnIndexOrThrow(_cursor, "is_downloaded");
            final int _cursorIndexOfUploadState = CursorUtil.getColumnIndexOrThrow(_cursor, "upload_state");
            final int _cursorIndexOfUploadProgress = CursorUtil.getColumnIndexOrThrow(_cursor, "upload_progress");
            final int _cursorIndexOfUploadError = CursorUtil.getColumnIndexOrThrow(_cursor, "upload_error");
            final int _cursorIndexOfLocalContentHash = CursorUtil.getColumnIndexOrThrow(_cursor, "local_content_hash");
            final int _cursorIndexOfMessageDate = CursorUtil.getColumnIndexOrThrow(_cursor, "message_date");
            final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
            final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
            final LongSparseArray<ArrayList<MediaTagEntity>> _collectionTags = new LongSparseArray<ArrayList<MediaTagEntity>>();
            while (_cursor.moveToNext()) {
              final long _tmpKey;
              _tmpKey = _cursor.getLong(_cursorIndexOfId);
              if (!_collectionTags.containsKey(_tmpKey)) {
                _collectionTags.put(_tmpKey, new ArrayList<MediaTagEntity>());
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipmediaTagsAscomExampleTelegramcloudgalleryDataDbEntityMediaTagEntity(_collectionTags);
            final List<MediaItemWithTags> _result = new ArrayList<MediaItemWithTags>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final MediaItemWithTags _item_1;
              final MediaItemEntity _tmpItem;
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              final String _tmpRemoteId;
              _tmpRemoteId = _cursor.getString(_cursorIndexOfRemoteId);
              final long _tmpChatId;
              _tmpChatId = _cursor.getLong(_cursorIndexOfChatId);
              final long _tmpMessageId;
              _tmpMessageId = _cursor.getLong(_cursorIndexOfMessageId);
              final Long _tmpFolderId;
              if (_cursor.isNull(_cursorIndexOfFolderId)) {
                _tmpFolderId = null;
              } else {
                _tmpFolderId = _cursor.getLong(_cursorIndexOfFolderId);
              }
              final MediaKind _tmpMediaType;
              _tmpMediaType = __MediaKind_stringToEnum(_cursor.getString(_cursorIndexOfMediaType));
              final String _tmpFileName;
              if (_cursor.isNull(_cursorIndexOfFileName)) {
                _tmpFileName = null;
              } else {
                _tmpFileName = _cursor.getString(_cursorIndexOfFileName);
              }
              final String _tmpMimeType;
              if (_cursor.isNull(_cursorIndexOfMimeType)) {
                _tmpMimeType = null;
              } else {
                _tmpMimeType = _cursor.getString(_cursorIndexOfMimeType);
              }
              final long _tmpFileSize;
              _tmpFileSize = _cursor.getLong(_cursorIndexOfFileSize);
              final long _tmpDurationMs;
              _tmpDurationMs = _cursor.getLong(_cursorIndexOfDurationMs);
              final int _tmpWidth;
              _tmpWidth = _cursor.getInt(_cursorIndexOfWidth);
              final int _tmpHeight;
              _tmpHeight = _cursor.getInt(_cursorIndexOfHeight);
              final String _tmpCaptionText;
              if (_cursor.isNull(_cursorIndexOfCaptionText)) {
                _tmpCaptionText = null;
              } else {
                _tmpCaptionText = _cursor.getString(_cursorIndexOfCaptionText);
              }
              final String _tmpCaptionEntitiesJson;
              if (_cursor.isNull(_cursorIndexOfCaptionEntitiesJson)) {
                _tmpCaptionEntitiesJson = null;
              } else {
                _tmpCaptionEntitiesJson = _cursor.getString(_cursorIndexOfCaptionEntitiesJson);
              }
              final int _tmpRemoteFileId;
              _tmpRemoteFileId = _cursor.getInt(_cursorIndexOfRemoteFileId);
              final int _tmpLocalFileId;
              _tmpLocalFileId = _cursor.getInt(_cursorIndexOfLocalFileId);
              final int _tmpThumbnailFileId;
              _tmpThumbnailFileId = _cursor.getInt(_cursorIndexOfThumbnailFileId);
              final String _tmpLocalPath;
              if (_cursor.isNull(_cursorIndexOfLocalPath)) {
                _tmpLocalPath = null;
              } else {
                _tmpLocalPath = _cursor.getString(_cursorIndexOfLocalPath);
              }
              final boolean _tmpIsDownloaded;
              final int _tmp;
              _tmp = _cursor.getInt(_cursorIndexOfIsDownloaded);
              _tmpIsDownloaded = _tmp != 0;
              final UploadState _tmpUploadState;
              _tmpUploadState = __UploadState_stringToEnum(_cursor.getString(_cursorIndexOfUploadState));
              final int _tmpUploadProgress;
              _tmpUploadProgress = _cursor.getInt(_cursorIndexOfUploadProgress);
              final String _tmpUploadError;
              if (_cursor.isNull(_cursorIndexOfUploadError)) {
                _tmpUploadError = null;
              } else {
                _tmpUploadError = _cursor.getString(_cursorIndexOfUploadError);
              }
              final String _tmpLocalContentHash;
              if (_cursor.isNull(_cursorIndexOfLocalContentHash)) {
                _tmpLocalContentHash = null;
              } else {
                _tmpLocalContentHash = _cursor.getString(_cursorIndexOfLocalContentHash);
              }
              final long _tmpMessageDate;
              _tmpMessageDate = _cursor.getLong(_cursorIndexOfMessageDate);
              final long _tmpCreatedAt;
              _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
              final long _tmpUpdatedAt;
              _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
              _tmpItem = new MediaItemEntity(_tmpId,_tmpRemoteId,_tmpChatId,_tmpMessageId,_tmpFolderId,_tmpMediaType,_tmpFileName,_tmpMimeType,_tmpFileSize,_tmpDurationMs,_tmpWidth,_tmpHeight,_tmpCaptionText,_tmpCaptionEntitiesJson,_tmpRemoteFileId,_tmpLocalFileId,_tmpThumbnailFileId,_tmpLocalPath,_tmpIsDownloaded,_tmpUploadState,_tmpUploadProgress,_tmpUploadError,_tmpLocalContentHash,_tmpMessageDate,_tmpCreatedAt,_tmpUpdatedAt);
              final ArrayList<MediaTagEntity> _tmpTagsCollection;
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfId);
              _tmpTagsCollection = _collectionTags.get(_tmpKey_1);
              _item_1 = new MediaItemWithTags(_tmpItem,_tmpTagsCollection);
              _result.add(_item_1);
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<MediaItemWithTags>> observeCardsByAllTags(final List<Long> tagIds,
      final int tagCount, final List<Long> folderIds) {
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("\n");
    _stringBuilder.append("        SELECT m.* FROM media_items m");
    _stringBuilder.append("\n");
    _stringBuilder.append("          INNER JOIN media_item_tags c ON c.item_id = m.id");
    _stringBuilder.append("\n");
    _stringBuilder.append("         WHERE c.tag_id IN (");
    final int _inputSize = tagIds.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    _stringBuilder.append("\n");
    _stringBuilder.append("           AND m.folder_id IN (");
    final int _inputSize_1 = folderIds.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize_1);
    _stringBuilder.append(")");
    _stringBuilder.append("\n");
    _stringBuilder.append("         GROUP BY m.id");
    _stringBuilder.append("\n");
    _stringBuilder.append("        HAVING COUNT(DISTINCT c.tag_id) = ");
    _stringBuilder.append("?");
    _stringBuilder.append("\n");
    _stringBuilder.append("         ORDER BY m.message_date DESC");
    _stringBuilder.append("\n");
    _stringBuilder.append("        ");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 1 + _inputSize + _inputSize_1;
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (long _item : tagIds) {
      _statement.bindLong(_argIndex, _item);
      _argIndex++;
    }
    _argIndex = 1 + _inputSize;
    for (long _item_1 : folderIds) {
      _statement.bindLong(_argIndex, _item_1);
      _argIndex++;
    }
    _argIndex = 1 + _inputSize + _inputSize_1;
    _statement.bindLong(_argIndex, tagCount);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"media_item_tags", "media_tags",
        "media_items"}, new Callable<List<MediaItemWithTags>>() {
      @Override
      @NonNull
      public List<MediaItemWithTags> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remote_id");
            final int _cursorIndexOfChatId = CursorUtil.getColumnIndexOrThrow(_cursor, "chat_id");
            final int _cursorIndexOfMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "message_id");
            final int _cursorIndexOfFolderId = CursorUtil.getColumnIndexOrThrow(_cursor, "folder_id");
            final int _cursorIndexOfMediaType = CursorUtil.getColumnIndexOrThrow(_cursor, "media_type");
            final int _cursorIndexOfFileName = CursorUtil.getColumnIndexOrThrow(_cursor, "file_name");
            final int _cursorIndexOfMimeType = CursorUtil.getColumnIndexOrThrow(_cursor, "mime_type");
            final int _cursorIndexOfFileSize = CursorUtil.getColumnIndexOrThrow(_cursor, "file_size");
            final int _cursorIndexOfDurationMs = CursorUtil.getColumnIndexOrThrow(_cursor, "duration_ms");
            final int _cursorIndexOfWidth = CursorUtil.getColumnIndexOrThrow(_cursor, "width");
            final int _cursorIndexOfHeight = CursorUtil.getColumnIndexOrThrow(_cursor, "height");
            final int _cursorIndexOfCaptionText = CursorUtil.getColumnIndexOrThrow(_cursor, "caption_text");
            final int _cursorIndexOfCaptionEntitiesJson = CursorUtil.getColumnIndexOrThrow(_cursor, "caption_entities_json");
            final int _cursorIndexOfRemoteFileId = CursorUtil.getColumnIndexOrThrow(_cursor, "remote_file_id");
            final int _cursorIndexOfLocalFileId = CursorUtil.getColumnIndexOrThrow(_cursor, "local_file_id");
            final int _cursorIndexOfThumbnailFileId = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbnail_file_id");
            final int _cursorIndexOfLocalPath = CursorUtil.getColumnIndexOrThrow(_cursor, "local_path");
            final int _cursorIndexOfIsDownloaded = CursorUtil.getColumnIndexOrThrow(_cursor, "is_downloaded");
            final int _cursorIndexOfUploadState = CursorUtil.getColumnIndexOrThrow(_cursor, "upload_state");
            final int _cursorIndexOfUploadProgress = CursorUtil.getColumnIndexOrThrow(_cursor, "upload_progress");
            final int _cursorIndexOfUploadError = CursorUtil.getColumnIndexOrThrow(_cursor, "upload_error");
            final int _cursorIndexOfLocalContentHash = CursorUtil.getColumnIndexOrThrow(_cursor, "local_content_hash");
            final int _cursorIndexOfMessageDate = CursorUtil.getColumnIndexOrThrow(_cursor, "message_date");
            final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
            final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
            final LongSparseArray<ArrayList<MediaTagEntity>> _collectionTags = new LongSparseArray<ArrayList<MediaTagEntity>>();
            while (_cursor.moveToNext()) {
              final long _tmpKey;
              _tmpKey = _cursor.getLong(_cursorIndexOfId);
              if (!_collectionTags.containsKey(_tmpKey)) {
                _collectionTags.put(_tmpKey, new ArrayList<MediaTagEntity>());
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipmediaTagsAscomExampleTelegramcloudgalleryDataDbEntityMediaTagEntity(_collectionTags);
            final List<MediaItemWithTags> _result = new ArrayList<MediaItemWithTags>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final MediaItemWithTags _item_2;
              final MediaItemEntity _tmpItem;
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              final String _tmpRemoteId;
              _tmpRemoteId = _cursor.getString(_cursorIndexOfRemoteId);
              final long _tmpChatId;
              _tmpChatId = _cursor.getLong(_cursorIndexOfChatId);
              final long _tmpMessageId;
              _tmpMessageId = _cursor.getLong(_cursorIndexOfMessageId);
              final Long _tmpFolderId;
              if (_cursor.isNull(_cursorIndexOfFolderId)) {
                _tmpFolderId = null;
              } else {
                _tmpFolderId = _cursor.getLong(_cursorIndexOfFolderId);
              }
              final MediaKind _tmpMediaType;
              _tmpMediaType = __MediaKind_stringToEnum(_cursor.getString(_cursorIndexOfMediaType));
              final String _tmpFileName;
              if (_cursor.isNull(_cursorIndexOfFileName)) {
                _tmpFileName = null;
              } else {
                _tmpFileName = _cursor.getString(_cursorIndexOfFileName);
              }
              final String _tmpMimeType;
              if (_cursor.isNull(_cursorIndexOfMimeType)) {
                _tmpMimeType = null;
              } else {
                _tmpMimeType = _cursor.getString(_cursorIndexOfMimeType);
              }
              final long _tmpFileSize;
              _tmpFileSize = _cursor.getLong(_cursorIndexOfFileSize);
              final long _tmpDurationMs;
              _tmpDurationMs = _cursor.getLong(_cursorIndexOfDurationMs);
              final int _tmpWidth;
              _tmpWidth = _cursor.getInt(_cursorIndexOfWidth);
              final int _tmpHeight;
              _tmpHeight = _cursor.getInt(_cursorIndexOfHeight);
              final String _tmpCaptionText;
              if (_cursor.isNull(_cursorIndexOfCaptionText)) {
                _tmpCaptionText = null;
              } else {
                _tmpCaptionText = _cursor.getString(_cursorIndexOfCaptionText);
              }
              final String _tmpCaptionEntitiesJson;
              if (_cursor.isNull(_cursorIndexOfCaptionEntitiesJson)) {
                _tmpCaptionEntitiesJson = null;
              } else {
                _tmpCaptionEntitiesJson = _cursor.getString(_cursorIndexOfCaptionEntitiesJson);
              }
              final int _tmpRemoteFileId;
              _tmpRemoteFileId = _cursor.getInt(_cursorIndexOfRemoteFileId);
              final int _tmpLocalFileId;
              _tmpLocalFileId = _cursor.getInt(_cursorIndexOfLocalFileId);
              final int _tmpThumbnailFileId;
              _tmpThumbnailFileId = _cursor.getInt(_cursorIndexOfThumbnailFileId);
              final String _tmpLocalPath;
              if (_cursor.isNull(_cursorIndexOfLocalPath)) {
                _tmpLocalPath = null;
              } else {
                _tmpLocalPath = _cursor.getString(_cursorIndexOfLocalPath);
              }
              final boolean _tmpIsDownloaded;
              final int _tmp;
              _tmp = _cursor.getInt(_cursorIndexOfIsDownloaded);
              _tmpIsDownloaded = _tmp != 0;
              final UploadState _tmpUploadState;
              _tmpUploadState = __UploadState_stringToEnum(_cursor.getString(_cursorIndexOfUploadState));
              final int _tmpUploadProgress;
              _tmpUploadProgress = _cursor.getInt(_cursorIndexOfUploadProgress);
              final String _tmpUploadError;
              if (_cursor.isNull(_cursorIndexOfUploadError)) {
                _tmpUploadError = null;
              } else {
                _tmpUploadError = _cursor.getString(_cursorIndexOfUploadError);
              }
              final String _tmpLocalContentHash;
              if (_cursor.isNull(_cursorIndexOfLocalContentHash)) {
                _tmpLocalContentHash = null;
              } else {
                _tmpLocalContentHash = _cursor.getString(_cursorIndexOfLocalContentHash);
              }
              final long _tmpMessageDate;
              _tmpMessageDate = _cursor.getLong(_cursorIndexOfMessageDate);
              final long _tmpCreatedAt;
              _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
              final long _tmpUpdatedAt;
              _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
              _tmpItem = new MediaItemEntity(_tmpId,_tmpRemoteId,_tmpChatId,_tmpMessageId,_tmpFolderId,_tmpMediaType,_tmpFileName,_tmpMimeType,_tmpFileSize,_tmpDurationMs,_tmpWidth,_tmpHeight,_tmpCaptionText,_tmpCaptionEntitiesJson,_tmpRemoteFileId,_tmpLocalFileId,_tmpThumbnailFileId,_tmpLocalPath,_tmpIsDownloaded,_tmpUploadState,_tmpUploadProgress,_tmpUploadError,_tmpLocalContentHash,_tmpMessageDate,_tmpCreatedAt,_tmpUpdatedAt);
              final ArrayList<MediaTagEntity> _tmpTagsCollection;
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfId);
              _tmpTagsCollection = _collectionTags.get(_tmpKey_1);
              _item_2 = new MediaItemWithTags(_tmpItem,_tmpTagsCollection);
              _result.add(_item_2);
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<MediaItemEntity>> search(final String query, final int limit) {
    final String _sql = "\n"
            + "        SELECT * FROM media_items\n"
            + "         WHERE file_name LIKE '%' || ? || '%' COLLATE NOCASE\n"
            + "            OR caption_text LIKE '%' || ? || '%' COLLATE NOCASE\n"
            + "            OR remote_id LIKE '%' || ? || '%'\n"
            + "         ORDER BY message_date DESC\n"
            + "         LIMIT ?\n"
            + "        ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 4);
    int _argIndex = 1;
    _statement.bindString(_argIndex, query);
    _argIndex = 2;
    _statement.bindString(_argIndex, query);
    _argIndex = 3;
    _statement.bindString(_argIndex, query);
    _argIndex = 4;
    _statement.bindLong(_argIndex, limit);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"media_items"}, new Callable<List<MediaItemEntity>>() {
      @Override
      @NonNull
      public List<MediaItemEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remote_id");
          final int _cursorIndexOfChatId = CursorUtil.getColumnIndexOrThrow(_cursor, "chat_id");
          final int _cursorIndexOfMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "message_id");
          final int _cursorIndexOfFolderId = CursorUtil.getColumnIndexOrThrow(_cursor, "folder_id");
          final int _cursorIndexOfMediaType = CursorUtil.getColumnIndexOrThrow(_cursor, "media_type");
          final int _cursorIndexOfFileName = CursorUtil.getColumnIndexOrThrow(_cursor, "file_name");
          final int _cursorIndexOfMimeType = CursorUtil.getColumnIndexOrThrow(_cursor, "mime_type");
          final int _cursorIndexOfFileSize = CursorUtil.getColumnIndexOrThrow(_cursor, "file_size");
          final int _cursorIndexOfDurationMs = CursorUtil.getColumnIndexOrThrow(_cursor, "duration_ms");
          final int _cursorIndexOfWidth = CursorUtil.getColumnIndexOrThrow(_cursor, "width");
          final int _cursorIndexOfHeight = CursorUtil.getColumnIndexOrThrow(_cursor, "height");
          final int _cursorIndexOfCaptionText = CursorUtil.getColumnIndexOrThrow(_cursor, "caption_text");
          final int _cursorIndexOfCaptionEntitiesJson = CursorUtil.getColumnIndexOrThrow(_cursor, "caption_entities_json");
          final int _cursorIndexOfRemoteFileId = CursorUtil.getColumnIndexOrThrow(_cursor, "remote_file_id");
          final int _cursorIndexOfLocalFileId = CursorUtil.getColumnIndexOrThrow(_cursor, "local_file_id");
          final int _cursorIndexOfThumbnailFileId = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbnail_file_id");
          final int _cursorIndexOfLocalPath = CursorUtil.getColumnIndexOrThrow(_cursor, "local_path");
          final int _cursorIndexOfIsDownloaded = CursorUtil.getColumnIndexOrThrow(_cursor, "is_downloaded");
          final int _cursorIndexOfUploadState = CursorUtil.getColumnIndexOrThrow(_cursor, "upload_state");
          final int _cursorIndexOfUploadProgress = CursorUtil.getColumnIndexOrThrow(_cursor, "upload_progress");
          final int _cursorIndexOfUploadError = CursorUtil.getColumnIndexOrThrow(_cursor, "upload_error");
          final int _cursorIndexOfLocalContentHash = CursorUtil.getColumnIndexOrThrow(_cursor, "local_content_hash");
          final int _cursorIndexOfMessageDate = CursorUtil.getColumnIndexOrThrow(_cursor, "message_date");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final List<MediaItemEntity> _result = new ArrayList<MediaItemEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MediaItemEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpRemoteId;
            _tmpRemoteId = _cursor.getString(_cursorIndexOfRemoteId);
            final long _tmpChatId;
            _tmpChatId = _cursor.getLong(_cursorIndexOfChatId);
            final long _tmpMessageId;
            _tmpMessageId = _cursor.getLong(_cursorIndexOfMessageId);
            final Long _tmpFolderId;
            if (_cursor.isNull(_cursorIndexOfFolderId)) {
              _tmpFolderId = null;
            } else {
              _tmpFolderId = _cursor.getLong(_cursorIndexOfFolderId);
            }
            final MediaKind _tmpMediaType;
            _tmpMediaType = __MediaKind_stringToEnum(_cursor.getString(_cursorIndexOfMediaType));
            final String _tmpFileName;
            if (_cursor.isNull(_cursorIndexOfFileName)) {
              _tmpFileName = null;
            } else {
              _tmpFileName = _cursor.getString(_cursorIndexOfFileName);
            }
            final String _tmpMimeType;
            if (_cursor.isNull(_cursorIndexOfMimeType)) {
              _tmpMimeType = null;
            } else {
              _tmpMimeType = _cursor.getString(_cursorIndexOfMimeType);
            }
            final long _tmpFileSize;
            _tmpFileSize = _cursor.getLong(_cursorIndexOfFileSize);
            final long _tmpDurationMs;
            _tmpDurationMs = _cursor.getLong(_cursorIndexOfDurationMs);
            final int _tmpWidth;
            _tmpWidth = _cursor.getInt(_cursorIndexOfWidth);
            final int _tmpHeight;
            _tmpHeight = _cursor.getInt(_cursorIndexOfHeight);
            final String _tmpCaptionText;
            if (_cursor.isNull(_cursorIndexOfCaptionText)) {
              _tmpCaptionText = null;
            } else {
              _tmpCaptionText = _cursor.getString(_cursorIndexOfCaptionText);
            }
            final String _tmpCaptionEntitiesJson;
            if (_cursor.isNull(_cursorIndexOfCaptionEntitiesJson)) {
              _tmpCaptionEntitiesJson = null;
            } else {
              _tmpCaptionEntitiesJson = _cursor.getString(_cursorIndexOfCaptionEntitiesJson);
            }
            final int _tmpRemoteFileId;
            _tmpRemoteFileId = _cursor.getInt(_cursorIndexOfRemoteFileId);
            final int _tmpLocalFileId;
            _tmpLocalFileId = _cursor.getInt(_cursorIndexOfLocalFileId);
            final int _tmpThumbnailFileId;
            _tmpThumbnailFileId = _cursor.getInt(_cursorIndexOfThumbnailFileId);
            final String _tmpLocalPath;
            if (_cursor.isNull(_cursorIndexOfLocalPath)) {
              _tmpLocalPath = null;
            } else {
              _tmpLocalPath = _cursor.getString(_cursorIndexOfLocalPath);
            }
            final boolean _tmpIsDownloaded;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsDownloaded);
            _tmpIsDownloaded = _tmp != 0;
            final UploadState _tmpUploadState;
            _tmpUploadState = __UploadState_stringToEnum(_cursor.getString(_cursorIndexOfUploadState));
            final int _tmpUploadProgress;
            _tmpUploadProgress = _cursor.getInt(_cursorIndexOfUploadProgress);
            final String _tmpUploadError;
            if (_cursor.isNull(_cursorIndexOfUploadError)) {
              _tmpUploadError = null;
            } else {
              _tmpUploadError = _cursor.getString(_cursorIndexOfUploadError);
            }
            final String _tmpLocalContentHash;
            if (_cursor.isNull(_cursorIndexOfLocalContentHash)) {
              _tmpLocalContentHash = null;
            } else {
              _tmpLocalContentHash = _cursor.getString(_cursorIndexOfLocalContentHash);
            }
            final long _tmpMessageDate;
            _tmpMessageDate = _cursor.getLong(_cursorIndexOfMessageDate);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new MediaItemEntity(_tmpId,_tmpRemoteId,_tmpChatId,_tmpMessageId,_tmpFolderId,_tmpMediaType,_tmpFileName,_tmpMimeType,_tmpFileSize,_tmpDurationMs,_tmpWidth,_tmpHeight,_tmpCaptionText,_tmpCaptionEntitiesJson,_tmpRemoteFileId,_tmpLocalFileId,_tmpThumbnailFileId,_tmpLocalPath,_tmpIsDownloaded,_tmpUploadState,_tmpUploadProgress,_tmpUploadError,_tmpLocalContentHash,_tmpMessageDate,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Flow<MediaItemWithTags> observeWithTags(final long itemId) {
    final String _sql = "SELECT * FROM media_items WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, itemId);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"media_item_tags", "media_tags",
        "media_items"}, new Callable<MediaItemWithTags>() {
      @Override
      @Nullable
      public MediaItemWithTags call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remote_id");
            final int _cursorIndexOfChatId = CursorUtil.getColumnIndexOrThrow(_cursor, "chat_id");
            final int _cursorIndexOfMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "message_id");
            final int _cursorIndexOfFolderId = CursorUtil.getColumnIndexOrThrow(_cursor, "folder_id");
            final int _cursorIndexOfMediaType = CursorUtil.getColumnIndexOrThrow(_cursor, "media_type");
            final int _cursorIndexOfFileName = CursorUtil.getColumnIndexOrThrow(_cursor, "file_name");
            final int _cursorIndexOfMimeType = CursorUtil.getColumnIndexOrThrow(_cursor, "mime_type");
            final int _cursorIndexOfFileSize = CursorUtil.getColumnIndexOrThrow(_cursor, "file_size");
            final int _cursorIndexOfDurationMs = CursorUtil.getColumnIndexOrThrow(_cursor, "duration_ms");
            final int _cursorIndexOfWidth = CursorUtil.getColumnIndexOrThrow(_cursor, "width");
            final int _cursorIndexOfHeight = CursorUtil.getColumnIndexOrThrow(_cursor, "height");
            final int _cursorIndexOfCaptionText = CursorUtil.getColumnIndexOrThrow(_cursor, "caption_text");
            final int _cursorIndexOfCaptionEntitiesJson = CursorUtil.getColumnIndexOrThrow(_cursor, "caption_entities_json");
            final int _cursorIndexOfRemoteFileId = CursorUtil.getColumnIndexOrThrow(_cursor, "remote_file_id");
            final int _cursorIndexOfLocalFileId = CursorUtil.getColumnIndexOrThrow(_cursor, "local_file_id");
            final int _cursorIndexOfThumbnailFileId = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbnail_file_id");
            final int _cursorIndexOfLocalPath = CursorUtil.getColumnIndexOrThrow(_cursor, "local_path");
            final int _cursorIndexOfIsDownloaded = CursorUtil.getColumnIndexOrThrow(_cursor, "is_downloaded");
            final int _cursorIndexOfUploadState = CursorUtil.getColumnIndexOrThrow(_cursor, "upload_state");
            final int _cursorIndexOfUploadProgress = CursorUtil.getColumnIndexOrThrow(_cursor, "upload_progress");
            final int _cursorIndexOfUploadError = CursorUtil.getColumnIndexOrThrow(_cursor, "upload_error");
            final int _cursorIndexOfLocalContentHash = CursorUtil.getColumnIndexOrThrow(_cursor, "local_content_hash");
            final int _cursorIndexOfMessageDate = CursorUtil.getColumnIndexOrThrow(_cursor, "message_date");
            final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
            final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
            final LongSparseArray<ArrayList<MediaTagEntity>> _collectionTags = new LongSparseArray<ArrayList<MediaTagEntity>>();
            while (_cursor.moveToNext()) {
              final long _tmpKey;
              _tmpKey = _cursor.getLong(_cursorIndexOfId);
              if (!_collectionTags.containsKey(_tmpKey)) {
                _collectionTags.put(_tmpKey, new ArrayList<MediaTagEntity>());
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipmediaTagsAscomExampleTelegramcloudgalleryDataDbEntityMediaTagEntity(_collectionTags);
            final MediaItemWithTags _result;
            if (_cursor.moveToFirst()) {
              final MediaItemEntity _tmpItem;
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              final String _tmpRemoteId;
              _tmpRemoteId = _cursor.getString(_cursorIndexOfRemoteId);
              final long _tmpChatId;
              _tmpChatId = _cursor.getLong(_cursorIndexOfChatId);
              final long _tmpMessageId;
              _tmpMessageId = _cursor.getLong(_cursorIndexOfMessageId);
              final Long _tmpFolderId;
              if (_cursor.isNull(_cursorIndexOfFolderId)) {
                _tmpFolderId = null;
              } else {
                _tmpFolderId = _cursor.getLong(_cursorIndexOfFolderId);
              }
              final MediaKind _tmpMediaType;
              _tmpMediaType = __MediaKind_stringToEnum(_cursor.getString(_cursorIndexOfMediaType));
              final String _tmpFileName;
              if (_cursor.isNull(_cursorIndexOfFileName)) {
                _tmpFileName = null;
              } else {
                _tmpFileName = _cursor.getString(_cursorIndexOfFileName);
              }
              final String _tmpMimeType;
              if (_cursor.isNull(_cursorIndexOfMimeType)) {
                _tmpMimeType = null;
              } else {
                _tmpMimeType = _cursor.getString(_cursorIndexOfMimeType);
              }
              final long _tmpFileSize;
              _tmpFileSize = _cursor.getLong(_cursorIndexOfFileSize);
              final long _tmpDurationMs;
              _tmpDurationMs = _cursor.getLong(_cursorIndexOfDurationMs);
              final int _tmpWidth;
              _tmpWidth = _cursor.getInt(_cursorIndexOfWidth);
              final int _tmpHeight;
              _tmpHeight = _cursor.getInt(_cursorIndexOfHeight);
              final String _tmpCaptionText;
              if (_cursor.isNull(_cursorIndexOfCaptionText)) {
                _tmpCaptionText = null;
              } else {
                _tmpCaptionText = _cursor.getString(_cursorIndexOfCaptionText);
              }
              final String _tmpCaptionEntitiesJson;
              if (_cursor.isNull(_cursorIndexOfCaptionEntitiesJson)) {
                _tmpCaptionEntitiesJson = null;
              } else {
                _tmpCaptionEntitiesJson = _cursor.getString(_cursorIndexOfCaptionEntitiesJson);
              }
              final int _tmpRemoteFileId;
              _tmpRemoteFileId = _cursor.getInt(_cursorIndexOfRemoteFileId);
              final int _tmpLocalFileId;
              _tmpLocalFileId = _cursor.getInt(_cursorIndexOfLocalFileId);
              final int _tmpThumbnailFileId;
              _tmpThumbnailFileId = _cursor.getInt(_cursorIndexOfThumbnailFileId);
              final String _tmpLocalPath;
              if (_cursor.isNull(_cursorIndexOfLocalPath)) {
                _tmpLocalPath = null;
              } else {
                _tmpLocalPath = _cursor.getString(_cursorIndexOfLocalPath);
              }
              final boolean _tmpIsDownloaded;
              final int _tmp;
              _tmp = _cursor.getInt(_cursorIndexOfIsDownloaded);
              _tmpIsDownloaded = _tmp != 0;
              final UploadState _tmpUploadState;
              _tmpUploadState = __UploadState_stringToEnum(_cursor.getString(_cursorIndexOfUploadState));
              final int _tmpUploadProgress;
              _tmpUploadProgress = _cursor.getInt(_cursorIndexOfUploadProgress);
              final String _tmpUploadError;
              if (_cursor.isNull(_cursorIndexOfUploadError)) {
                _tmpUploadError = null;
              } else {
                _tmpUploadError = _cursor.getString(_cursorIndexOfUploadError);
              }
              final String _tmpLocalContentHash;
              if (_cursor.isNull(_cursorIndexOfLocalContentHash)) {
                _tmpLocalContentHash = null;
              } else {
                _tmpLocalContentHash = _cursor.getString(_cursorIndexOfLocalContentHash);
              }
              final long _tmpMessageDate;
              _tmpMessageDate = _cursor.getLong(_cursorIndexOfMessageDate);
              final long _tmpCreatedAt;
              _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
              final long _tmpUpdatedAt;
              _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
              _tmpItem = new MediaItemEntity(_tmpId,_tmpRemoteId,_tmpChatId,_tmpMessageId,_tmpFolderId,_tmpMediaType,_tmpFileName,_tmpMimeType,_tmpFileSize,_tmpDurationMs,_tmpWidth,_tmpHeight,_tmpCaptionText,_tmpCaptionEntitiesJson,_tmpRemoteFileId,_tmpLocalFileId,_tmpThumbnailFileId,_tmpLocalPath,_tmpIsDownloaded,_tmpUploadState,_tmpUploadProgress,_tmpUploadError,_tmpLocalContentHash,_tmpMessageDate,_tmpCreatedAt,_tmpUpdatedAt);
              final ArrayList<MediaTagEntity> _tmpTagsCollection;
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfId);
              _tmpTagsCollection = _collectionTags.get(_tmpKey_1);
              _result = new MediaItemWithTags(_tmpItem,_tmpTagsCollection);
            } else {
              _result = null;
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<MediaItemEntity>> observeReels(final MediaKind kind,
      final List<Long> folderIds) {
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("\n");
    _stringBuilder.append("        SELECT * FROM media_items");
    _stringBuilder.append("\n");
    _stringBuilder.append("         WHERE media_type = ");
    _stringBuilder.append("?");
    _stringBuilder.append("\n");
    _stringBuilder.append("           AND duration_ms > 0");
    _stringBuilder.append("\n");
    _stringBuilder.append("           AND folder_id IN (");
    final int _inputSize = folderIds.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    _stringBuilder.append("\n");
    _stringBuilder.append("         ORDER BY message_date DESC");
    _stringBuilder.append("\n");
    _stringBuilder.append("        ");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 1 + _inputSize;
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    _statement.bindString(_argIndex, __MediaKind_enumToString(kind));
    _argIndex = 2;
    for (long _item : folderIds) {
      _statement.bindLong(_argIndex, _item);
      _argIndex++;
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"media_items"}, new Callable<List<MediaItemEntity>>() {
      @Override
      @NonNull
      public List<MediaItemEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remote_id");
          final int _cursorIndexOfChatId = CursorUtil.getColumnIndexOrThrow(_cursor, "chat_id");
          final int _cursorIndexOfMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "message_id");
          final int _cursorIndexOfFolderId = CursorUtil.getColumnIndexOrThrow(_cursor, "folder_id");
          final int _cursorIndexOfMediaType = CursorUtil.getColumnIndexOrThrow(_cursor, "media_type");
          final int _cursorIndexOfFileName = CursorUtil.getColumnIndexOrThrow(_cursor, "file_name");
          final int _cursorIndexOfMimeType = CursorUtil.getColumnIndexOrThrow(_cursor, "mime_type");
          final int _cursorIndexOfFileSize = CursorUtil.getColumnIndexOrThrow(_cursor, "file_size");
          final int _cursorIndexOfDurationMs = CursorUtil.getColumnIndexOrThrow(_cursor, "duration_ms");
          final int _cursorIndexOfWidth = CursorUtil.getColumnIndexOrThrow(_cursor, "width");
          final int _cursorIndexOfHeight = CursorUtil.getColumnIndexOrThrow(_cursor, "height");
          final int _cursorIndexOfCaptionText = CursorUtil.getColumnIndexOrThrow(_cursor, "caption_text");
          final int _cursorIndexOfCaptionEntitiesJson = CursorUtil.getColumnIndexOrThrow(_cursor, "caption_entities_json");
          final int _cursorIndexOfRemoteFileId = CursorUtil.getColumnIndexOrThrow(_cursor, "remote_file_id");
          final int _cursorIndexOfLocalFileId = CursorUtil.getColumnIndexOrThrow(_cursor, "local_file_id");
          final int _cursorIndexOfThumbnailFileId = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbnail_file_id");
          final int _cursorIndexOfLocalPath = CursorUtil.getColumnIndexOrThrow(_cursor, "local_path");
          final int _cursorIndexOfIsDownloaded = CursorUtil.getColumnIndexOrThrow(_cursor, "is_downloaded");
          final int _cursorIndexOfUploadState = CursorUtil.getColumnIndexOrThrow(_cursor, "upload_state");
          final int _cursorIndexOfUploadProgress = CursorUtil.getColumnIndexOrThrow(_cursor, "upload_progress");
          final int _cursorIndexOfUploadError = CursorUtil.getColumnIndexOrThrow(_cursor, "upload_error");
          final int _cursorIndexOfLocalContentHash = CursorUtil.getColumnIndexOrThrow(_cursor, "local_content_hash");
          final int _cursorIndexOfMessageDate = CursorUtil.getColumnIndexOrThrow(_cursor, "message_date");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final List<MediaItemEntity> _result = new ArrayList<MediaItemEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MediaItemEntity _item_1;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpRemoteId;
            _tmpRemoteId = _cursor.getString(_cursorIndexOfRemoteId);
            final long _tmpChatId;
            _tmpChatId = _cursor.getLong(_cursorIndexOfChatId);
            final long _tmpMessageId;
            _tmpMessageId = _cursor.getLong(_cursorIndexOfMessageId);
            final Long _tmpFolderId;
            if (_cursor.isNull(_cursorIndexOfFolderId)) {
              _tmpFolderId = null;
            } else {
              _tmpFolderId = _cursor.getLong(_cursorIndexOfFolderId);
            }
            final MediaKind _tmpMediaType;
            _tmpMediaType = __MediaKind_stringToEnum(_cursor.getString(_cursorIndexOfMediaType));
            final String _tmpFileName;
            if (_cursor.isNull(_cursorIndexOfFileName)) {
              _tmpFileName = null;
            } else {
              _tmpFileName = _cursor.getString(_cursorIndexOfFileName);
            }
            final String _tmpMimeType;
            if (_cursor.isNull(_cursorIndexOfMimeType)) {
              _tmpMimeType = null;
            } else {
              _tmpMimeType = _cursor.getString(_cursorIndexOfMimeType);
            }
            final long _tmpFileSize;
            _tmpFileSize = _cursor.getLong(_cursorIndexOfFileSize);
            final long _tmpDurationMs;
            _tmpDurationMs = _cursor.getLong(_cursorIndexOfDurationMs);
            final int _tmpWidth;
            _tmpWidth = _cursor.getInt(_cursorIndexOfWidth);
            final int _tmpHeight;
            _tmpHeight = _cursor.getInt(_cursorIndexOfHeight);
            final String _tmpCaptionText;
            if (_cursor.isNull(_cursorIndexOfCaptionText)) {
              _tmpCaptionText = null;
            } else {
              _tmpCaptionText = _cursor.getString(_cursorIndexOfCaptionText);
            }
            final String _tmpCaptionEntitiesJson;
            if (_cursor.isNull(_cursorIndexOfCaptionEntitiesJson)) {
              _tmpCaptionEntitiesJson = null;
            } else {
              _tmpCaptionEntitiesJson = _cursor.getString(_cursorIndexOfCaptionEntitiesJson);
            }
            final int _tmpRemoteFileId;
            _tmpRemoteFileId = _cursor.getInt(_cursorIndexOfRemoteFileId);
            final int _tmpLocalFileId;
            _tmpLocalFileId = _cursor.getInt(_cursorIndexOfLocalFileId);
            final int _tmpThumbnailFileId;
            _tmpThumbnailFileId = _cursor.getInt(_cursorIndexOfThumbnailFileId);
            final String _tmpLocalPath;
            if (_cursor.isNull(_cursorIndexOfLocalPath)) {
              _tmpLocalPath = null;
            } else {
              _tmpLocalPath = _cursor.getString(_cursorIndexOfLocalPath);
            }
            final boolean _tmpIsDownloaded;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsDownloaded);
            _tmpIsDownloaded = _tmp != 0;
            final UploadState _tmpUploadState;
            _tmpUploadState = __UploadState_stringToEnum(_cursor.getString(_cursorIndexOfUploadState));
            final int _tmpUploadProgress;
            _tmpUploadProgress = _cursor.getInt(_cursorIndexOfUploadProgress);
            final String _tmpUploadError;
            if (_cursor.isNull(_cursorIndexOfUploadError)) {
              _tmpUploadError = null;
            } else {
              _tmpUploadError = _cursor.getString(_cursorIndexOfUploadError);
            }
            final String _tmpLocalContentHash;
            if (_cursor.isNull(_cursorIndexOfLocalContentHash)) {
              _tmpLocalContentHash = null;
            } else {
              _tmpLocalContentHash = _cursor.getString(_cursorIndexOfLocalContentHash);
            }
            final long _tmpMessageDate;
            _tmpMessageDate = _cursor.getLong(_cursorIndexOfMessageDate);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item_1 = new MediaItemEntity(_tmpId,_tmpRemoteId,_tmpChatId,_tmpMessageId,_tmpFolderId,_tmpMediaType,_tmpFileName,_tmpMimeType,_tmpFileSize,_tmpDurationMs,_tmpWidth,_tmpHeight,_tmpCaptionText,_tmpCaptionEntitiesJson,_tmpRemoteFileId,_tmpLocalFileId,_tmpThumbnailFileId,_tmpLocalPath,_tmpIsDownloaded,_tmpUploadState,_tmpUploadProgress,_tmpUploadError,_tmpLocalContentHash,_tmpMessageDate,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Flow<List<MediaItemEntity>> observeReelsInDurationRange(final long minMs,
      final long maxMs) {
    final String _sql = "\n"
            + "        SELECT * FROM media_items\n"
            + "         WHERE media_type IN ('VIDEO', 'ANIMATION')\n"
            + "           AND duration_ms BETWEEN ? AND ?\n"
            + "         ORDER BY message_date DESC\n"
            + "        ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, minMs);
    _argIndex = 2;
    _statement.bindLong(_argIndex, maxMs);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"media_items"}, new Callable<List<MediaItemEntity>>() {
      @Override
      @NonNull
      public List<MediaItemEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remote_id");
          final int _cursorIndexOfChatId = CursorUtil.getColumnIndexOrThrow(_cursor, "chat_id");
          final int _cursorIndexOfMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "message_id");
          final int _cursorIndexOfFolderId = CursorUtil.getColumnIndexOrThrow(_cursor, "folder_id");
          final int _cursorIndexOfMediaType = CursorUtil.getColumnIndexOrThrow(_cursor, "media_type");
          final int _cursorIndexOfFileName = CursorUtil.getColumnIndexOrThrow(_cursor, "file_name");
          final int _cursorIndexOfMimeType = CursorUtil.getColumnIndexOrThrow(_cursor, "mime_type");
          final int _cursorIndexOfFileSize = CursorUtil.getColumnIndexOrThrow(_cursor, "file_size");
          final int _cursorIndexOfDurationMs = CursorUtil.getColumnIndexOrThrow(_cursor, "duration_ms");
          final int _cursorIndexOfWidth = CursorUtil.getColumnIndexOrThrow(_cursor, "width");
          final int _cursorIndexOfHeight = CursorUtil.getColumnIndexOrThrow(_cursor, "height");
          final int _cursorIndexOfCaptionText = CursorUtil.getColumnIndexOrThrow(_cursor, "caption_text");
          final int _cursorIndexOfCaptionEntitiesJson = CursorUtil.getColumnIndexOrThrow(_cursor, "caption_entities_json");
          final int _cursorIndexOfRemoteFileId = CursorUtil.getColumnIndexOrThrow(_cursor, "remote_file_id");
          final int _cursorIndexOfLocalFileId = CursorUtil.getColumnIndexOrThrow(_cursor, "local_file_id");
          final int _cursorIndexOfThumbnailFileId = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbnail_file_id");
          final int _cursorIndexOfLocalPath = CursorUtil.getColumnIndexOrThrow(_cursor, "local_path");
          final int _cursorIndexOfIsDownloaded = CursorUtil.getColumnIndexOrThrow(_cursor, "is_downloaded");
          final int _cursorIndexOfUploadState = CursorUtil.getColumnIndexOrThrow(_cursor, "upload_state");
          final int _cursorIndexOfUploadProgress = CursorUtil.getColumnIndexOrThrow(_cursor, "upload_progress");
          final int _cursorIndexOfUploadError = CursorUtil.getColumnIndexOrThrow(_cursor, "upload_error");
          final int _cursorIndexOfLocalContentHash = CursorUtil.getColumnIndexOrThrow(_cursor, "local_content_hash");
          final int _cursorIndexOfMessageDate = CursorUtil.getColumnIndexOrThrow(_cursor, "message_date");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final List<MediaItemEntity> _result = new ArrayList<MediaItemEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MediaItemEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpRemoteId;
            _tmpRemoteId = _cursor.getString(_cursorIndexOfRemoteId);
            final long _tmpChatId;
            _tmpChatId = _cursor.getLong(_cursorIndexOfChatId);
            final long _tmpMessageId;
            _tmpMessageId = _cursor.getLong(_cursorIndexOfMessageId);
            final Long _tmpFolderId;
            if (_cursor.isNull(_cursorIndexOfFolderId)) {
              _tmpFolderId = null;
            } else {
              _tmpFolderId = _cursor.getLong(_cursorIndexOfFolderId);
            }
            final MediaKind _tmpMediaType;
            _tmpMediaType = __MediaKind_stringToEnum(_cursor.getString(_cursorIndexOfMediaType));
            final String _tmpFileName;
            if (_cursor.isNull(_cursorIndexOfFileName)) {
              _tmpFileName = null;
            } else {
              _tmpFileName = _cursor.getString(_cursorIndexOfFileName);
            }
            final String _tmpMimeType;
            if (_cursor.isNull(_cursorIndexOfMimeType)) {
              _tmpMimeType = null;
            } else {
              _tmpMimeType = _cursor.getString(_cursorIndexOfMimeType);
            }
            final long _tmpFileSize;
            _tmpFileSize = _cursor.getLong(_cursorIndexOfFileSize);
            final long _tmpDurationMs;
            _tmpDurationMs = _cursor.getLong(_cursorIndexOfDurationMs);
            final int _tmpWidth;
            _tmpWidth = _cursor.getInt(_cursorIndexOfWidth);
            final int _tmpHeight;
            _tmpHeight = _cursor.getInt(_cursorIndexOfHeight);
            final String _tmpCaptionText;
            if (_cursor.isNull(_cursorIndexOfCaptionText)) {
              _tmpCaptionText = null;
            } else {
              _tmpCaptionText = _cursor.getString(_cursorIndexOfCaptionText);
            }
            final String _tmpCaptionEntitiesJson;
            if (_cursor.isNull(_cursorIndexOfCaptionEntitiesJson)) {
              _tmpCaptionEntitiesJson = null;
            } else {
              _tmpCaptionEntitiesJson = _cursor.getString(_cursorIndexOfCaptionEntitiesJson);
            }
            final int _tmpRemoteFileId;
            _tmpRemoteFileId = _cursor.getInt(_cursorIndexOfRemoteFileId);
            final int _tmpLocalFileId;
            _tmpLocalFileId = _cursor.getInt(_cursorIndexOfLocalFileId);
            final int _tmpThumbnailFileId;
            _tmpThumbnailFileId = _cursor.getInt(_cursorIndexOfThumbnailFileId);
            final String _tmpLocalPath;
            if (_cursor.isNull(_cursorIndexOfLocalPath)) {
              _tmpLocalPath = null;
            } else {
              _tmpLocalPath = _cursor.getString(_cursorIndexOfLocalPath);
            }
            final boolean _tmpIsDownloaded;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsDownloaded);
            _tmpIsDownloaded = _tmp != 0;
            final UploadState _tmpUploadState;
            _tmpUploadState = __UploadState_stringToEnum(_cursor.getString(_cursorIndexOfUploadState));
            final int _tmpUploadProgress;
            _tmpUploadProgress = _cursor.getInt(_cursorIndexOfUploadProgress);
            final String _tmpUploadError;
            if (_cursor.isNull(_cursorIndexOfUploadError)) {
              _tmpUploadError = null;
            } else {
              _tmpUploadError = _cursor.getString(_cursorIndexOfUploadError);
            }
            final String _tmpLocalContentHash;
            if (_cursor.isNull(_cursorIndexOfLocalContentHash)) {
              _tmpLocalContentHash = null;
            } else {
              _tmpLocalContentHash = _cursor.getString(_cursorIndexOfLocalContentHash);
            }
            final long _tmpMessageDate;
            _tmpMessageDate = _cursor.getLong(_cursorIndexOfMessageDate);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new MediaItemEntity(_tmpId,_tmpRemoteId,_tmpChatId,_tmpMessageId,_tmpFolderId,_tmpMediaType,_tmpFileName,_tmpMimeType,_tmpFileSize,_tmpDurationMs,_tmpWidth,_tmpHeight,_tmpCaptionText,_tmpCaptionEntitiesJson,_tmpRemoteFileId,_tmpLocalFileId,_tmpThumbnailFileId,_tmpLocalPath,_tmpIsDownloaded,_tmpUploadState,_tmpUploadProgress,_tmpUploadError,_tmpLocalContentHash,_tmpMessageDate,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Flow<Integer> observeTotalCount() {
    final String _sql = "SELECT COUNT(*) FROM media_items";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"media_items"}, new Callable<Integer>() {
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
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<Integer> observeVideoCount() {
    final String _sql = "SELECT COUNT(*) FROM media_items WHERE media_type IN ('VIDEO', 'ANIMATION')";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"media_items"}, new Callable<Integer>() {
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
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<Integer> observeCountInFolder(final long folderId) {
    final String _sql = "SELECT COUNT(*) FROM media_items WHERE folder_id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, folderId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"media_items"}, new Callable<Integer>() {
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
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<Long> observeLocalDiskUsage() {
    final String _sql = "SELECT COALESCE(SUM(file_size), 0) FROM media_items WHERE is_downloaded = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"media_items"}, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Long _result;
          if (_cursor.moveToFirst()) {
            final long _tmp;
            _tmp = _cursor.getLong(0);
            _result = _tmp;
          } else {
            _result = 0L;
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }

  private String __MediaKind_enumToString(@NonNull final MediaKind _value) {
    switch (_value) {
      case PHOTO: return "PHOTO";
      case VIDEO: return "VIDEO";
      case ANIMATION: return "ANIMATION";
      case AUDIO: return "AUDIO";
      case DOCUMENT: return "DOCUMENT";
      case UNKNOWN: return "UNKNOWN";
      default: throw new IllegalArgumentException("Can't convert enum to string, unknown enum value: " + _value);
    }
  }

  private String __UploadState_enumToString(@NonNull final UploadState _value) {
    switch (_value) {
      case NONE: return "NONE";
      case PENDING: return "PENDING";
      case UPLOADING: return "UPLOADING";
      case UPLOADED: return "UPLOADED";
      case FAILED: return "FAILED";
      default: throw new IllegalArgumentException("Can't convert enum to string, unknown enum value: " + _value);
    }
  }

  private MediaKind __MediaKind_stringToEnum(@NonNull final String _value) {
    switch (_value) {
      case "PHOTO": return MediaKind.PHOTO;
      case "VIDEO": return MediaKind.VIDEO;
      case "ANIMATION": return MediaKind.ANIMATION;
      case "AUDIO": return MediaKind.AUDIO;
      case "DOCUMENT": return MediaKind.DOCUMENT;
      case "UNKNOWN": return MediaKind.UNKNOWN;
      default: throw new IllegalArgumentException("Can't convert value to enum, unknown value: " + _value);
    }
  }

  private UploadState __UploadState_stringToEnum(@NonNull final String _value) {
    switch (_value) {
      case "NONE": return UploadState.NONE;
      case "PENDING": return UploadState.PENDING;
      case "UPLOADING": return UploadState.UPLOADING;
      case "UPLOADED": return UploadState.UPLOADED;
      case "FAILED": return UploadState.FAILED;
      default: throw new IllegalArgumentException("Can't convert value to enum, unknown value: " + _value);
    }
  }

  private void __fetchRelationshipmediaTagsAscomExampleTelegramcloudgalleryDataDbEntityMediaTagEntity(
      @NonNull final LongSparseArray<ArrayList<MediaTagEntity>> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      RelationUtil.recursiveFetchLongSparseArray(_map, true, (map) -> {
        __fetchRelationshipmediaTagsAscomExampleTelegramcloudgalleryDataDbEntityMediaTagEntity(map);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `media_tags`.`id` AS `id`,`media_tags`.`name` AS `name`,`media_tags`.`color_argb` AS `color_argb`,`media_tags`.`created_at` AS `created_at`,_junction.`item_id` FROM `media_item_tags` AS _junction INNER JOIN `media_tags` ON (_junction.`tag_id` = `media_tags`.`id`) WHERE _junction.`item_id` IN (");
    final int _inputSize = _map.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _stmt = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (int i = 0; i < _map.size(); i++) {
      final long _item = _map.keyAt(i);
      _stmt.bindLong(_argIndex, _item);
      _argIndex++;
    }
    final Cursor _cursor = DBUtil.query(__db, _stmt, false, null);
    try {
      // _junction.item_id;
      final int _itemKeyIndex = 4;
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _cursorIndexOfId = 0;
      final int _cursorIndexOfName = 1;
      final int _cursorIndexOfColorArgb = 2;
      final int _cursorIndexOfCreatedAt = 3;
      while (_cursor.moveToNext()) {
        final long _tmpKey;
        _tmpKey = _cursor.getLong(_itemKeyIndex);
        final ArrayList<MediaTagEntity> _tmpRelation = _map.get(_tmpKey);
        if (_tmpRelation != null) {
          final MediaTagEntity _item_1;
          final long _tmpId;
          _tmpId = _cursor.getLong(_cursorIndexOfId);
          final String _tmpName;
          _tmpName = _cursor.getString(_cursorIndexOfName);
          final int _tmpColorArgb;
          _tmpColorArgb = _cursor.getInt(_cursorIndexOfColorArgb);
          final long _tmpCreatedAt;
          _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
          _item_1 = new MediaTagEntity(_tmpId,_tmpName,_tmpColorArgb,_tmpCreatedAt);
          _tmpRelation.add(_item_1);
        }
      }
    } finally {
      _cursor.close();
    }
  }
}
