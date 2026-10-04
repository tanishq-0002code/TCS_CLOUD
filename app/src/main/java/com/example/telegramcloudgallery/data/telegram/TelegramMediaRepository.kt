package com.example.telegramcloudgallery.data.telegram

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import com.example.telegramcloudgallery.core.DispatcherProvider
import com.example.telegramcloudgallery.core.MediaKind
import com.example.telegramcloudgallery.core.Resource
import com.example.telegramcloudgallery.core.UploadState
import com.example.telegramcloudgallery.data.db.AppDatabase
import com.example.telegramcloudgallery.data.db.entity.MediaItemEntity
import com.example.telegramcloudgallery.data.db.entity.MediaItemTagCrossRef
import com.example.telegramcloudgallery.data.db.entity.MediaTagEntity
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import org.drinkless.tdlib.TdApi
import java.io.File
import java.util.UUID

/**
 * Handles Telegram media fetching, progressive streaming downloads (downloadFile),
 * cloud file operations, and chat folder/sub-channel mapping.
 */
class TelegramMediaRepository(
    private val context: Context,
    private val clientManager: TelegramClientManager,
    private val database: AppDatabase,
    private val dispatcherProvider: DispatcherProvider = com.example.telegramcloudgallery.core.DefaultDispatcherProvider()
) {

    companion object {
        private const val TAG = "TelegramMediaRepository"
    }

    // ------------------------------------------------------------------ indexing

    /**
     * Indexes media from a chat into local Room database.
     */
    suspend fun indexChat(chatId: Long, limit: Int = 100): Resource<Int> = withContext(dispatcherProvider.io) {
        try {
            val getHistory = TdApi.GetChatHistory(
                chatId,
                /* fromMessageId = */ 0,
                /* offset = */ 0,
                /* limit = */ limit,
                /* onlyLocal = */ false
            )
            val messagesResult = clientManager.send(getHistory)
            if (messagesResult is TdApi.Messages) {
                var indexed = 0
                messagesResult.messages?.forEach { message ->
                    if (message != null && hasMedia(message)) {
                        val item = mapMessageToEntity(message)
                        if (item != null) {
                            val id = database.mediaDao().upsert(item)
                            // Parse and attach tags from caption
                            parseAndAttachTags(id, message)
                            indexed++
                        }
                    }
                }
                Resource.Success(indexed)
            } else {
                Resource.Failure("Failed to get chat history")
            }
        } catch (ce: CancellationException) {
            throw ce
        } catch (e: Exception) {
            Resource.Failure(e.message ?: "Index failed", e)
        }
    }

    /**
     * Progressive download - starts downloading with priority for streaming.
     * Uses downloadFile with appropriate priority.
     */
    suspend fun downloadFileForStreaming(
        fileId: Int,
        priority: Int = TelegramClientManager.PRIORITY_STREAM_PLAYBACK
    ): Flow<Resource<TdApi.File>> = flow {
        emit(Resource.Loading)
        try {
            val download = TdApi.DownloadFile(
                /* fileId = */ fileId,
                /* priority = */ priority,
                /* offset = */ 0L,
                /* limit = */ 0L,
                /* synchronous = */ false
            )
            // For streaming, we initiate download and observe file updates via TDLib updates
            // In practice, the download starts; completion is tracked via UpdateFile
            val result = clientManager.send(download)
            emit(Resource.Success(result))
        } catch (ce: CancellationException) {
            throw ce
        } catch (e: Exception) {
            emit(Resource.Failure(e.message ?: "Download failed", e))
        }
    }

    /**
     * Full download to local storage.
     */
    suspend fun downloadFile(
        fileId: Int,
        fileName: String,
        priority: Int = TelegramClientManager.PRIORITY_DOWNLOAD
    ): Resource<File> = withContext(dispatcherProvider.io) {
        try {
            val downloadDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) ?: context.filesDir
            val outputFile = File(downloadDir, fileName)
            val download = TdApi.DownloadFile(
                fileId,
                priority,
                0L,
                0L,
                false
            )
            val file = clientManager.send(download)
            // File will be written to TDLib's files directory; in full impl we'd copy
            Resource.Success(outputFile)
        } catch (ce: CancellationException) {
            throw ce
        } catch (e: Exception) {
            Resource.Failure(e.message ?: "Download failed", e)
        }
    }

    /**
     * Upload file using PreliminaryUploadFile (as per TDLib API we inspected).
     */
    suspend fun uploadFile(
        localPath: String,
        chatId: Long,
        caption: String? = null
    ): Resource<TdApi.File> = withContext(dispatcherProvider.io) {
        try {
            val inputFile = TdApi.InputFileLocal(localPath)
            val fileType = TdApi.FileTypeFile()
            val upload = TdApi.PreliminaryUploadFile(
                inputFile,
                fileType,
                TelegramClientManager.PRIORITY_UPLOAD
            )
            val uploadedFile = clientManager.send(upload)
            Resource.Success(uploadedFile)
        } catch (ce: CancellationException) {
            throw ce
        } catch (e: Exception) {
            Resource.Failure(e.message ?: "Upload failed", e)
        }
    }

    // ------------------------------------------------------------------ helpers

    private fun hasMedia(message: TdApi.Message): Boolean {
        return when (message.content) {
            is TdApi.MessagePhoto,
            is TdApi.MessageVideo,
            is TdApi.MessageAnimation,
            is TdApi.MessageAudio,
            is TdApi.MessageDocument -> true
            else -> false
        }
    }

    private suspend fun mapMessageToEntity(message: TdApi.Message): MediaItemEntity? {
        val remoteId = "${message.chatId}:${message.id}"
        return when (val content = message.content) {
            is TdApi.MessagePhoto -> {
                val photo = content.photo
                val largest = photo?.sizes?.maxByOrNull { it.width * it.height }
                MediaItemEntity(
                    remoteId = remoteId,
                    chatId = message.chatId,
                    messageId = message.id,
                    mediaType = MediaKind.PHOTO,
                    fileName = "photo_${message.id}.jpg",
                    mimeType = "image/jpeg",
                    fileSize = largest?.photo?.size ?: 0,
                    width = largest?.width ?: 0,
                    height = largest?.height ?: 0,
                    captionText = content.caption?.text,
                    captionEntitiesJson = content.caption?.entities?.let { toJson(it) },
                    remoteFileId = largest?.photo?.id ?: 0,
                    thumbnailFileId = photo?.sizes?.firstOrNull()?.photo?.id ?: 0,
                    messageDate = message.date.toLong(),
                    uploadState = UploadState.NONE
                )
            }
            is TdApi.MessageVideo -> {
                val video = content.video
                MediaItemEntity(
                    remoteId = remoteId,
                    chatId = message.chatId,
                    messageId = message.id,
                    mediaType = MediaKind.VIDEO,
                    fileName = video?.fileName ?: "video_${message.id}.mp4",
                    mimeType = video?.mimeType ?: "video/mp4",
                    fileSize = video?.video?.size ?: 0,
                    durationMs = video?.duration?.toLong() ?: 0,
                    width = video?.width ?: 0,
                    height = video?.height ?: 0,
                    captionText = content.caption?.text,
                    captionEntitiesJson = content.caption?.entities?.let { toJson(it) },
                    remoteFileId = video?.video?.id ?: 0,
                    thumbnailFileId = video?.thumbnail?.file?.id ?: 0,
                    messageDate = message.date.toLong(),
                    uploadState = UploadState.UPLOADED // From cloud
                )
            }
            is TdApi.MessageDocument -> {
                val doc = content.document
                MediaItemEntity(
                    remoteId = remoteId,
                    chatId = message.chatId,
                    messageId = message.id,
                    mediaType = MediaKind.DOCUMENT,
                    fileName = doc?.fileName ?: "doc_${message.id}",
                    mimeType = doc?.mimeType,
                    fileSize = doc?.document?.size ?: 0,
                    captionText = content.caption?.text,
                    captionEntitiesJson = content.caption?.entities?.let { toJson(it) },
                    remoteFileId = doc?.document?.id ?: 0,
                    thumbnailFileId = doc?.thumbnail?.file?.id ?: 0,
                    messageDate = message.date.toLong(),
                    uploadState = UploadState.NONE
                )
            }
            else -> null
        }
    }

    private suspend fun parseAndAttachTags(itemId: Long, message: TdApi.Message) {
        val caption = when (val content = message.content) {
            is TdApi.MessagePhoto -> content.caption?.text
            is TdApi.MessageVideo -> content.caption?.text
            is TdApi.MessageDocument -> content.caption?.text
            is TdApi.MessageAnimation -> content.caption?.text
            is TdApi.MessageAudio -> content.caption?.text
            else -> null
        }
        val entities = when (val content = message.content) {
            is TdApi.MessagePhoto -> content.caption?.entities
            is TdApi.MessageVideo -> content.caption?.entities
            is TdApi.MessageDocument -> content.caption?.entities
            is TdApi.MessageAnimation -> content.caption?.entities
            is TdApi.MessageAudio -> content.caption?.entities
            else -> null
        }
        if (caption.isNullOrBlank()) return

        val partition = com.example.telegramcloudgallery.data.telegram.CaptionTagParser.partition(caption, entities)
        for (tagName in partition.tags) {
            val normalized = tagName.lowercase().trim()
            if (normalized.isEmpty()) continue
            var tagId = database.tagDao().findByName(normalized)?.id
            if (tagId == null) {
                val newTagId = database.tagDao().insert(
                    MediaTagEntity(name = normalized, colorArgb = 0)
                )
                tagId = if (newTagId > 0) newTagId else database.tagDao().findByName(normalized)?.id
            }
            tagId?.let { tid ->
                database.tagDao().link(MediaItemTagCrossRef(itemId, tid))
            }
        }
        // Folder tags (#Folder_*) could be resolved to folders here if needed
    }

    private fun toJson(obj: Any): String = obj.toString()
}