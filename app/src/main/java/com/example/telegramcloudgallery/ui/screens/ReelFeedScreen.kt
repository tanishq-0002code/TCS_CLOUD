package com.example.telegramcloudgallery.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import coil.compose.AsyncImage
import com.example.telegramcloudgallery.core.MediaKind
import com.example.telegramcloudgallery.data.db.AppDatabase
import com.example.telegramcloudgallery.data.db.MediaDao

/**
 * ReelFeedScreen - VerticalPager displaying full-screen vertical videos.
 * ExoPlayer instances managed per page with DisposableEffect to prevent OOM.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ReelFeedScreen(
    database: AppDatabase,
    onNavigateUp: () -> Unit = {}
) {
    val context = LocalContext.current
    val videos by database.mediaDao()
        .observeReels(MediaKind.VIDEO, listOf(MediaDao.NOTHING))
        .collectAsState(initial = emptyList())

    val pagerState = rememberPagerState(pageCount = { videos.size })

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reels") },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (videos.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.padding(24.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("No Reels Found", style = MaterialTheme.typography.titleLarge)
                        Text(
                            "Videos will appear here after indexing your Telegram media.",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            VerticalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) { page ->
                val video = videos[page]
                val exoPlayer = remember(video.id) {
                    ExoPlayer.Builder(context).build().apply {
                        if (video.localPath != null) {
                            setMediaItem(MediaItem.fromUri(video.localPath!!))
                        }
                        repeatMode = Player.REPEAT_MODE_ONE
                        playWhenReady = false
                    }
                }

                DisposableEffect(exoPlayer) {
                    onDispose {
                        exoPlayer.release()
                    }
                }

                LaunchedEffect(pagerState.currentPage, page) {
                    if (pagerState.currentPage == page) {
                        exoPlayer.playWhenReady = true
                        exoPlayer.prepare()
                    } else {
                        exoPlayer.playWhenReady = false
                        exoPlayer.pause()
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Black, Color.DarkGray, Color.Black)
                            )
                        )
                ) {
                    // Video player view would go here - simplified with thumbnail
                    if (video.thumbnailFileId > 0 && video.localPath == null) {
                        AsyncImage(
                            model = video.localPath,
                            contentDescription = video.fileName,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Text(
                        text = video.fileName ?: "Reel ${page + 1}",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                            .background(Color.Black.copy(alpha = 0.5f))
                            .padding(8.dp)
                    )
                }
            }
        }
    }
}