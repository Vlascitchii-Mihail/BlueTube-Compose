package com.vlascitchii.presentation_player.screen_player.screen

import android.content.pm.ActivityInfo
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.vlascitchii.presentation_common.model.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_common.network_observer.NetworkConnectivityStatus
import com.vlascitchii.presentation_common.ui.global_snackbar.SnackBarController.sendEvent
import com.vlascitchii.presentation_common.ui.global_snackbar.SnackBarEvent
import com.vlascitchii.presentation_common.ui.screen.CommonScreen
import com.vlascitchii.presentation_common.ui.screen.PagerContentManager
import com.vlascitchii.presentation_common.ui.state.UiState
import com.vlascitchii.presentation_common.ui.video_list.ItemsList
import com.vlascitchii.presentation_player.screen_player.OrientationState
import com.vlascitchii.presentation_player.screen_player.VideoDescription
import com.vlascitchii.presentation_player.screen_player.YoutubeVideoPlayer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun PlayerScreen(
    video: YoutubeVideoUiModel,
    relatedVideos: StateFlow<UiState<Flow<PagingData<YoutubeVideoUiModel>>>>,
    getRelatedVideos: (String) -> Unit,
    isVideoPlaysFlow: StateFlow<Boolean>,
    updateVideoIsPlayState: (Boolean) -> Unit,
    navigateToPlayerScreen: (YoutubeVideoUiModel) -> Unit,
    popBackStack: () -> Unit,
    updatePlaybackPosition: (Float) -> Unit,
    playbackPosition: Float,
    playerOrientationState: StateFlow<OrientationState>,
    updatePlayerOrientationState: (OrientationState) -> Unit,
    fullscreenWidgetIsClicked: StateFlow<Boolean>,
    setFullscreenWidgetIsClicked: (Boolean) -> Unit,
    connectivityStatus: Flow<NetworkConnectivityStatus>,
    modifier: Modifier = Modifier
) {

    relatedVideos.collectAsStateWithLifecycle().value.let { uiStatePagingData: UiState<Flow<PagingData<YoutubeVideoUiModel>>> ->
        CommonScreen(uiStatePagingData) { pagingData: Flow<PagingData<YoutubeVideoUiModel>> ->
            val networkConnectivityStatus by connectivityStatus.collectAsStateWithLifecycle(
                initialValue = NetworkConnectivityStatus.Available
            )
            val lazyPagingData = pagingData.collectAsLazyPagingItems()

            LaunchedEffect(networkConnectivityStatus) {
                if (networkConnectivityStatus == NetworkConnectivityStatus.Lost) {
                    sendEvent(
                        event = SnackBarEvent(
                            message = "Wrong internet connection"
                        )
                    )
                }
            }

            Column(
                modifier = modifier
                    .fillMaxSize()
            ) {
//                YoutubeVideoPlayer(
//                    videoId = video.id,
//                    modifier,
//                    isVideoPlaysFlow,
//                    updateVideoIsPlayState,
//                    popBackStack,
//                    updatePlaybackPosition,
//                    playbackPosition,
//                    playerOrientationState,
//                    updatePlayerOrientationState,
//                    fullscreenWidgetIsClicked,
//                    setFullscreenWidgetIsClicked,
//                    networkConnectivityStatus
//                )

                if (LocalConfiguration.current.orientation != ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE) {
                    VideoDescription(video = video)

                    PagerContentManager(
                        videoState = lazyPagingData,
                        contentList = {
                            ItemsList(
                                lazyPagingData,
                                modifier,
                                navigateToPlayerScreen,
                            )
                        },
                        modifier = modifier
                            .fillMaxSize()
                    )
                }
            }
        }
    }
}
