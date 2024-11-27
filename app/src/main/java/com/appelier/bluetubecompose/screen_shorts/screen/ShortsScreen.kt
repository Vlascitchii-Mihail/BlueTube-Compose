package com.appelier.bluetubecompose.screen_shorts.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.appelier.bluetubecompose.core.core_api.network_observer.ConnectivityStatus
import com.appelier.bluetubecompose.core.core_ui.views.PagerContentManager
import com.appelier.bluetubecompose.core.core_ui.views.shorts_screen.ShortsList
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideo
import com.appelier.bluetubecompose.utils.NavigationTags
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun ShortsScreen(
    shortsStateFlow: StateFlow<PagingData<YoutubeVideo>>,
    videoQueue: MutableSharedFlow<YouTubePlayer?>,
    listenToVideoQueue: () -> Unit,
    connectivityStatus: Flow<ConnectivityStatus>,
) {
    Surface(modifier = Modifier.testTag(NavigationTags.SHORTS_SCREEN)) {
        shortsStateFlow.collectAsStateWithLifecycle()
        val shorts = shortsStateFlow.collectAsLazyPagingItems()

        PagerContentManager(
            videoState = shorts,
            contentList = {
                ShortsList(
                    videos = shorts,
                    videoQueue = videoQueue,
                    listenToVideoQueue,
                    connectivityStatus = connectivityStatus
                )
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}
