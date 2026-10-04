package com.example.telegramcloudgallery.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.telegramcloudgallery.data.db.entity.MediaItemEntity
import com.example.telegramcloudgallery.data.db.relation.MediaItemWithTags
import kotlin.math.roundToInt

/**
 * GlidePreviewVideoCard - Scrubs video/keyframes on horizontal drag.
 * Shows live preview frame during scrubbing. Fully wired for Phase 4.
 */
@Composable
fun GlidePreviewVideoCard(
    card: MediaItemWithTags,
    modifier: Modifier = Modifier,
    previewFramePath: String? = null,
    keyframes: List<String> = emptyList(),
    onScrubProgress: (Float) -> Unit = {},
    onTap: () -> Unit = {}
) {
    val item = card.item
    var cardWidth by remember { mutableIntStateOf(1) }
    var isScrubbing by remember { mutableStateOf(false) }
    var scrubProgress by remember { mutableFloatStateOf(0f) }
    var currentFrameIndex by remember(scrubProgress, keyframes) {
        mutableIntStateOf(
            if (keyframes.isEmpty()) 0 else ((scrubProgress * keyframes.size).toInt().coerceIn(0, keyframes.size - 1))
        )
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp)
            .onSizeChanged { cardWidth = it.width },
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        onClick = onTap
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp))
                .pointerInput(item.isVideo && cardWidth > 0) {
                    if (item.isVideo) {
                        detectHorizontalScrub(
                            cardWidth = cardWidth,
                            onStart = { isScrubbing = true },
                            onProgress = { progress ->
                                scrubProgress = progress.coerceIn(0f, 1f)
                                currentFrameIndex = if (keyframes.isNotEmpty()) {
                                    ((scrubProgress * keyframes.size).toInt().coerceIn(0, keyframes.size - 1))
                                } else {
                                    0
                                }
                                onScrubProgress(scrubProgress)
                            },
                            onStop = { isScrubbing = false }
                        )
                    }
                }
        ) {
            // Preview frame during scrubbing, else thumbnail
            val frameToShow = if (isScrubbing && keyframes.isNotEmpty() && currentFrameIndex < keyframes.size) {
                keyframes[currentFrameIndex]
            } else {
                previewFramePath
            }

            if (frameToShow != null) {
                AsyncImage(
                    model = frameToShow,
                    contentDescription = item.fileName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                // Fallback gradient
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(
                                colors = if (item.isVideo) {
                                    listOf(Color(0xFF0F172A), Color(0xFF1E293B))
                                } else {
                                    listOf(Color(0xFF1F2937), Color(0xFF111827))
                                }
                            )
                        )
                )
            }

            // Scrub timeline overlay
            if (isScrubbing && item.isVideo) {
                ScrubTimeline(progress = scrubProgress, modifier = Modifier.align(Alignment.BottomCenter))
            }

            // Play indicator for videos
            if (item.isVideo && !isScrubbing) {
                Icon(
                    imageVector = Icons.Filled.PlayCircleOutline,
                    contentDescription = "Play",
                    tint = Color.White.copy(alpha = 0.9f),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(48.dp)
                )
            }

            // Info overlay
            InfoOverlay(item = item, tags = card.tags, modifier = Modifier.align(Alignment.BottomStart))
        }
    }
}

@Composable
private fun ScrubTimeline(progress: Float, modifier: Modifier = Modifier) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(4.dp)
            .padding(horizontal = 8.dp)
    ) {
        val trackHeight = 2.dp.toPx()
        val thumbRadius = 6.dp.toPx()
        val centerY = size.height / 2

        // Track background
        drawLine(
            color = Color.White.copy(alpha = 0.5f),
            start = Offset(0f, centerY),
            end = Offset(size.width, centerY),
            strokeWidth = trackHeight
        )
        // Progress
        drawLine(
            color = Color.White,
            start = Offset(0f, centerY),
            end = Offset(size.width * progress, centerY),
            strokeWidth = trackHeight
        )
        // Thumb
        drawCircle(
            color = Color.White,
            radius = thumbRadius,
            center = Offset(size.width * progress, centerY)
        )
    }
}

@Composable
private fun BoxScope.InfoOverlay(
    item: MediaItemEntity,
    tags: List<com.example.telegramcloudgallery.data.db.entity.MediaTagEntity>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(12.dp)
            .background(Color.Black.copy(alpha = 0.45f), RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = item.fileName ?: item.remoteId,
            color = Color.White,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        if (item.isVideo) {
            Text(
                text = "%.1fs".format(item.durationSeconds),
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 10.sp
            )
        }
        if (tags.isNotEmpty()) {
            Text(
                text = tags.take(2).joinToString(" ") { "#${it.name}" } + if (tags.size > 2) " +${tags.size - 2}" else "",
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 9.sp,
                maxLines = 1
            )
        }
    }
}

private suspend fun PointerInputScope.detectHorizontalScrub(
    cardWidth: Int,
    onStart: () -> Unit,
    onProgress: (Float) -> Unit,
    onStop: () -> Unit
) {
    if (cardWidth <= 0) return
    detectHorizontalDragGestures(
        onDragStart = { onStart() },
        onHorizontalDrag = { change: PointerInputChange, _: Offset ->
            val x = change.position.x.coerceIn(0f, cardWidth.toFloat())
            val progress = x / cardWidth.toFloat()
            onProgress(progress)
        },
        onDragEnd = { onStop() },
        onDragCancel = { onStop() }
    )
}
