package com.appelier.bluetubecompose.screen_shorts.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.appelier.bluetubecompose.core.core_ui.views.PagerContentSupplier
import com.appelier.bluetubecompose.core.core_ui.views.shorts_screen.ShortsList
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideo
import com.appelier.bluetubecompose.utils.NavigationTags
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun ShortsScreen(
    shortsVideoState: State<StateFlow<PagingData<YoutubeVideo>>>?,
    videoQueue: MutableSharedFlow<YouTubePlayer?>,
    listenToVideoQueue: () -> Unit,
) {
    Surface(modifier = Modifier.testTag(NavigationTags.SHORTS_SCREEN)) {
        shortsVideoState?.let {
            PagerContentSupplier(
                shortsVideoState.value.collectAsLazyPagingItems(),
                { videoState: LazyPagingItems<YoutubeVideo> -> ShortsList(videos = videoState, videoQueue = videoQueue, listenToVideoQueue) },
                Modifier.fillMaxSize()
            )
        }
    }
}
