package com.appelier.bluetubecompose.screen_player

import android.content.pm.ActivityInfo
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.paging.PagingData
import com.appelier.bluetubecompose.core.core_ui.views.video_list_screen.VideoDescription
import com.appelier.bluetubecompose.core.core_ui.views.video_list_screen.YouTubeVideoList
import com.appelier.bluetubecompose.core.core_ui.views.YoutubeVideoPlayer
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideo
import kotlinx.coroutines.flow.StateFlow

@Composable
fun PlayerScreen(
    video: YoutubeVideo,
    relatedVideos: () -> State<StateFlow<PagingData<YoutubeVideo>>>,
    isVideoPlaysFlow: StateFlow<Boolean>,
    updateVideoIsPlayState: (Boolean) -> Unit,
    navigateToPlayerScreen: (YoutubeVideo) -> Unit,
    popBackStack: () -> Unit,
    updatePlaybackPosition: (Float) -> Unit,
    getPlaybackPosition: () -> Float,
    playerOrientationState: StateFlow<OrientationState>,
    updatePlayerOrientationState: (OrientationState) -> Unit,
    fullscreenWidgetIsClicked: StateFlow<Boolean>,
    setFullscreenWidgetIsClicked: (Boolean) -> Unit
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
                    isVideoPlaysFlow,
                    updateVideoIsPlayState,
                    popBackStack,
                    updatePlaybackPosition,
                    getPlaybackPosition,
                    playerOrientationState,
                    updatePlayerOrientationState,
                    fullscreenWidgetIsClicked,
                    setFullscreenWidgetIsClicked
                )

                if (LocalConfiguration.current.orientation != ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE) {
                    VideoDescription(video = video)

                        YouTubeVideoList(
                            videosStateFlow = relatedVideos,
                            modifier = Modifier,
                            navigateToPlayerScreen = navigateToPlayerScreen,
                        )

                }
            }
        }
    )
}
