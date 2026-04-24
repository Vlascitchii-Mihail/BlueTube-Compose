package com.vlascitchii.presentation_shorts.screen_shorts.ui

import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.vlascitchii.presentation_common.model.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_common.network_observer.NetworkConnectivityStatus
import com.vlascitchii.presentation_common.ui.global_snackbar.SnackBarController
import com.vlascitchii.presentation_common.ui.global_snackbar.SnackBarEvent
import com.vlascitchii.presentation_common.ui.screen.mvi.MviHandler
import com.vlascitchii.presentation_shorts.screen_shorts.state.ShortsAction
import com.vlascitchii.presentation_shorts.screen_shorts.state.ShortsUIEvent
import com.vlascitchii.shorts_screen.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

@Composable
fun ShortsList(
    videos: LazyPagingItems<YoutubeVideoUiModel>,
    videoQueue: MutableSharedFlow<YouTubePlayer?>,
    shortsMviHandler: MviHandler<ShortsAction, ShortsUIEvent>,
    connectivityStatus: NetworkConnectivityStatus,
) {
    val context = LocalContext.current
    val pagerState = rememberPagerState(pageCount = { videos.itemCount })

    LaunchedEffect(connectivityStatus) {
        if (connectivityStatus == NetworkConnectivityStatus.Lost) {
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
        shortsMviHandler.submitAction(ShortsAction.ListenToVideoQueueAction)
        onDispose { unlockOrientation() }
    }

    VerticalPager(
        state = pagerState,
        key = videos.itemKey { it.id },
        pageSize = PageSize.Fill,
        modifier = Modifier.semantics { contentDescription = context.getString(R.string.shorts_pager_description) }
    ) { pageIndex: Int ->
        videos[pageIndex]?.let {
            ShortsItem(
                youTubeVideo = it,
                videoQueue = videoQueue,
                networkConnectivityStatus = connectivityStatus
            )
        }
    }
}
