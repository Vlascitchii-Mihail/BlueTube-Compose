package com.vlascitchii.presentation_player.screen_player.screen

import android.content.pm.ActivityInfo
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import com.vlascitchii.presentation_common.entity.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_common.network_observer.ConnectivityStatus
import com.vlascitchii.presentation_common.ui.screen.CommonScreen
import com.vlascitchii.presentation_common.ui.state.UiState
import com.vlascitchii.presentation_common.ui.video_list.YouTubeVideoList
import com.vlascitchii.presentation_common.utils.SnackbarController.sendEvent
import com.vlascitchii.presentation_player.screen_player.OrientationState
import com.vlascitchii.presentation_player.screen_player.VideoDescription
import com.vlascitchii.presentation_player.screen_player.YoutubeVideoPlayer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun PlayerScreen(
    video: YoutubeVideoUiModel,
    relatedVideos: StateFlow<UiState<StateFlow<PagingData<YoutubeVideoUiModel>>>>,
    getRelatedVideos: (String) -> Unit,
    isVideoPlaysFlow: StateFlow<Boolean>,
    updateVideoIsPlayState: (Boolean) -> Unit,
    navigateToPlayerScreen: (YoutubeVideoUiModel) -> Unit,
    popBackStack: () -> Unit,
    updatePlaybackPosition: (Float) -> Unit,
    getPlaybackPosition: () -> Float,
    playerOrientationState: StateFlow<OrientationState>,
    updatePlayerOrientationState: (OrientationState) -> Unit,
    fullscreenWidgetIsClicked: StateFlow<Boolean>,
    setFullscreenWidgetIsClicked: (Boolean) -> Unit,
    connectivityStatus: Flow<ConnectivityStatus>
) {

    relatedVideos.collectAsStateWithLifecycle().value.let { uiStatePagingData: UiState<StateFlow<PagingData<YoutubeVideoUiModel>>> ->
        CommonScreen(uiStatePagingData) { pagingData: StateFlow<PagingData<YoutubeVideoUiModel>> ->
            val networkConnectivityStatus by connectivityStatus.collectAsStateWithLifecycle(
                initialValue = ConnectivityStatus.Available
            )
            LaunchedEffect(networkConnectivityStatus) {
                if (networkConnectivityStatus == ConnectivityStatus.Lost) {
                    sendEvent(
                        event = com.vlascitchii.presentation_common.utils.SnackbarEvent(
                            message = "Wrong internet connection"
                        )
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
//                    .background(MaterialTheme.colorScheme.background)
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
                    setFullscreenWidgetIsClicked,
                    networkConnectivityStatus
                )

                if (LocalConfiguration.current.orientation != ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE) {
                    VideoDescription(video = video)

                    YouTubeVideoList(
                        getVideoState = {
                            getRelatedVideos.invoke(video.snippet.title)
                            relatedVideos
                        },
                        modifier = Modifier,
                        navigateToPlayerScreen = navigateToPlayerScreen,
                    )
                }
            }
        }
    }
}
