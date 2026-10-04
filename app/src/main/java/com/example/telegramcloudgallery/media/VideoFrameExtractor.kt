package com.example.telegramcloudgallery.media

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.core.net.toUri
import com.example.telegramcloudgallery.core.DispatcherProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.abs

/**
 * VideoFrameExtractor extracts and caches thumbnail frames at key timestamps
 * for smooth scrubbing/glide previews. Uses LRU-style caching in memory and disk.
 */
class VideoFrameExtractor(
    private val context: Context,
    private val cacheDir: File,
    private val dispatcherProvider: DispatcherProvider = com.example.telegramcloudgallery.core.DefaultDispatcherProvider()
) {

    companion object {
        private const val TAG = "VideoFrameExtractor"
        private const val DEFAULT_FRAME_COUNT = 10
        private const val DEFAULT_CACHE_SIZE = 50
    }

    private val memoryCache = ConcurrentHashMap<String, Bitmap>()
    private val maxCacheSize = DEFAULT_CACHE_SIZE

    init {
        if (!cacheDir.exists()) {
            cacheDir.mkdirs()
        }
    }

    /**
     * Extracts frames at regular intervals across video duration for scrubbing preview.
     */
    suspend fun extractKeyframes(
        videoPath: String,
        frameCount: Int = DEFAULT_FRAME_COUNT
    ): List<String> = withContext(dispatcherProvider.io) {
        val retriever = MediaMetadataRetriever()
        try {
            retriever.setDataSource(context, videoPath.toUri())
            val duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLongOrNull() ?: 0L
            if (duration <= 0) return@withContext emptyList()

            val frameInterval = duration / (frameCount + 1)
            val framePaths = mutableListOf<String>()

            (1..frameCount).map { index ->
                val timestampMs = index * frameInterval
                async {
                    extractAndCacheFrame(retriever, videoPath, timestampMs, index)
                }
            }.awaitAll().filterNotNull().also { paths ->
                framePaths.addAll(paths)
            }

            framePaths
        } catch (e: Exception) {
            emptyList()
        } finally {
            retriever.release()
        }
    }

    /**
     * Gets frame at specific timestamp for precise scrubbing.
     */
    suspend fun getFrameAt(
        videoPath: String,
        timestampMs: Long,
        useCache: Boolean = true
    ): String? = withContext(dispatcherProvider.io) {
        val cacheKey = "${videoPath}_${timestampMs}"
        if (useCache && memoryCache.containsKey(cacheKey)) {
            val cached = memoryCache[cacheKey]
            // Could save to disk and return path
        }

        val retriever = MediaMetadataRetriever()
        try {
            retriever.setDataSource(context, videoPath.toUri())
            val bitmap = retriever.getFrameAtTime(
                timestampMs * 1000L,
                MediaMetadataRetriever.OPTION_CLOSEST
            )
            if (bitmap != null) {
                val frameFile = File(cacheDir, "frame_${cacheKey.hashCode()}.jpg")
                FileOutputStream(frameFile).use { out ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out)
                }
                // Cache in memory
                cacheInMemory(cacheKey, bitmap)
                frameFile.absolutePath
            } else {
                null
            }
        } catch (e: Exception) {
            null
        } finally {
            retriever.release()
        }
    }

    private suspend fun extractAndCacheFrame(
        retriever: MediaMetadataRetriever,
        videoPath: String,
        timestampMs: Long,
        index: Int
    ): String? = withContext(Dispatchers.IO) {
        try {
            val bitmap = retriever.getFrameAtTime(
                timestampMs * 1000L,
                MediaMetadataRetriever.OPTION_CLOSEST_SYNC
            )
            if (bitmap != null) {
                val frameFile = File(cacheDir, "keyframe_${videoPath.hashCode()}_$index.jpg")
                FileOutputStream(frameFile).use { out ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 75, out)
                }
                val cacheKey = "${videoPath}_$index"
                cacheInMemory(cacheKey, bitmap)
                frameFile.absolutePath
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun cacheInMemory(key: String, bitmap: Bitmap) {
        if (memoryCache.size >= maxCacheSize) {
            // Simple eviction - remove first entry
            val firstKey = memoryCache.keys.firstOrNull()
            if (firstKey != null) {
                memoryCache.remove(firstKey)
            }
        }
        memoryCache[key] = bitmap
    }

    fun clearCache() {
        memoryCache.clear()
        if (cacheDir.exists()) {
            cacheDir.listFiles()?.forEach { it.delete() }
        }
    }
}