package com.vlascitchii.presentation_shorts.screen_shorts.shorts_screen

import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.vlascitchii.presentation_common.entity.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_common.network_observer.NetworkConnectivityStatus
import com.vlascitchii.presentation_common.ui.global_snackbar.SnackBarController
import com.vlascitchii.presentation_common.ui.global_snackbar.SnackBarEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

@Composable
fun ShortsList(
    videos: LazyPagingItems<YoutubeVideoUiModel>,
    videoQueue: MutableSharedFlow<YouTubePlayer?>,
    listenToVideoQueue: () -> Unit,
    connectivityStatus: Flow<NetworkConnectivityStatus>,
) {
    val context = LocalContext.current
    val pagerState = rememberPagerState(pageCount = { videos.itemCount })
    val networkConnectivityStatus by connectivityStatus.collectAsStateWithLifecycle(
        initialValue = NetworkConnectivityStatus.Available
    )

    LaunchedEffect(networkConnectivityStatus) {
        if (networkConnectivityStatus == NetworkConnectivityStatus.Lost) {
            SnackBarController.sendEvent(
                event = SnackBarEvent(
                    message = "Wrong internet connection"
                )
            )
        }
    }

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
                videoQueue,
                networkConnectivityStatus = networkConnectivityStatus
            )
        }
    }
}