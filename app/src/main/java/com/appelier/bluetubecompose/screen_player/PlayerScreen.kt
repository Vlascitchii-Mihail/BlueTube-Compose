package com.appelier.bluetubecompose.screen_player

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.appelier.bluetubecompose.core.core_ui.views.VideoDescription
import com.appelier.bluetubecompose.core.core_ui.views.YouTubeVideoList
import com.appelier.bluetubecompose.core.core_ui.views.YoutubeVideoPlayer
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideo
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideo.Companion.DEFAULT_VIDEO_LIST
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun PlayerScreen(
    video: YoutubeVideo,
    lifecycleOwner: LifecycleOwner,
    relatedVideos: State<StateFlow<PagingData<YoutubeVideo>>>,
    navigateToPlayerScreen: (YoutubeVideo) -> Unit,
    popBackStack: () -> Unit,
    getUpdatedPlaybackPosition: (Float) -> Float
) {
    Scaffold(
        content = { paddingValue ->
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)) {
                YoutubeVideoPlayer(
                    videoId = video.id,
                    lifecycleOwner = lifecycleOwner,
                    Modifier,
                    popBackStack,
                    getUpdatedPlaybackPosition
                )

                VideoDescription(video = video)

//                YouTubeVideoList(
//                    videos = relatedVideos.value.collectAsLazyPagingItems(),
//                    modifier = Modifier,
//                    innerPadding = paddingValue,
//                    navigateToPlayerScreen = navigateToPlayerScreen
//                )
            }
        }
    )
}

@Preview
@Composable
fun PlayerScreenPreview() {
    val relatedVideos = remember { mutableStateOf(MutableStateFlow(PagingData.from(DEFAULT_VIDEO_LIST))) }
    PlayerScreen(
        video = YoutubeVideo.DEFAULT_VIDEO,
        lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current,
        relatedVideos = relatedVideos,
        navigateToPlayerScreen = {},
        popBackStack = {},
        getUpdatedPlaybackPosition = { videoPlayback: Float -> videoPlayback }
    )
}