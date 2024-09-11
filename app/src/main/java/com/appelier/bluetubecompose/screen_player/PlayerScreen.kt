package com.appelier.bluetubecompose.screen_player

import android.content.pm.ActivityInfo
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.appelier.bluetubecompose.core.core_ui.views.VideoDescription
import com.appelier.bluetubecompose.core.core_ui.views.YouTubeVideoList
import com.appelier.bluetubecompose.core.core_ui.views.YoutubeVideoPlayer
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideo
import kotlinx.coroutines.flow.StateFlow

@Composable
fun PlayerScreen(
    video: YoutubeVideo,
    relatedVideos: State<StateFlow<PagingData<YoutubeVideo>>>?,
    youTubePlayerPlayState: MutableState<Boolean>,
    navigateToPlayerScreen: (YoutubeVideo) -> Unit,
    popBackStack: () -> Unit,
    updatePlaybackPosition: (Float) -> Unit,
    getPlaybackPosition: () -> Float,
) {
    Scaffold(
        content = { paddingValue ->
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
            ) {
                YoutubeVideoPlayer(
                    videoId = video.id,
                    Modifier,
                    youTubePlayerPlayState,
                    popBackStack,
                    updatePlaybackPosition,
                    getPlaybackPosition,
                )

                if (LocalConfiguration.current.orientation != ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE) {
                    VideoDescription(video = video)

                    relatedVideos?.let { relatedVideos ->
                        YouTubeVideoList(
                            videos = relatedVideos.value.collectAsLazyPagingItems(),
                            modifier = Modifier,
                            navigateToPlayerScreen = navigateToPlayerScreen
                        )
                    }
                }
            }
        }
    )
}
