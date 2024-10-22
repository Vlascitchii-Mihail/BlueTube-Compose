package com.appelier.bluetubecompose.core.core_ui.views.shorts_screen

import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideo
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import kotlinx.coroutines.flow.MutableSharedFlow

@Composable
fun ShortsList(
    videos: LazyPagingItems<YoutubeVideo>,
    videoQueue: MutableSharedFlow<YouTubePlayer?>,
    listenToVideoQueue: () -> Unit
) {
    val context = LocalContext.current
    val pagerState = rememberPagerState(pageCount = { videos.itemCount })

    fun lockOrientation() {
        (context as Activity).requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED
    }

    fun unlockOrientation() {
        (context as Activity).requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }

    DisposableEffect(Unit) {
        lockOrientation()
        listenToVideoQueue.invoke()
        onDispose { unlockOrientation() }
    }

    VerticalPager(
        state = pagerState,
        key = videos.itemKey { it },
        pageSize = PageSize.Fill,
    ) { pageIndex: Int ->
        videos[pageIndex]?.let {
                ShortsItem(
                    youTubeVideo = it,
                    videoQueue
                )
            }
        }
}