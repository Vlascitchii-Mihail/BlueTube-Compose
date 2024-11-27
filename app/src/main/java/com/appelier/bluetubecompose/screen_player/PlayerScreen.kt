package com.appelier.bluetubecompose.screen_player

import android.content.pm.ActivityInfo
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import com.appelier.bluetubecompose.core.core_api.network_observer.ConnectivityStatus
import com.appelier.bluetubecompose.core.core_ui.views.YoutubeVideoPlayer
import com.appelier.bluetubecompose.core.core_ui.views.video_list_screen.VideoDescription
import com.appelier.bluetubecompose.core.core_ui.views.video_list_screen.YouTubeVideoList
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideo
import com.appelier.bluetubecompose.utils.SnackbarController
import com.appelier.bluetubecompose.utils.SnackbarEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun PlayerScreen(
    video: YoutubeVideo,
    relatedVideos: StateFlow<PagingData<YoutubeVideo>>,
    getRelatedVideos: (String) -> Unit,
    isVideoPlaysFlow: StateFlow<Boolean>,
    updateVideoIsPlayState: (Boolean) -> Unit,
    navigateToPlayerScreen: (YoutubeVideo) -> Unit,
    popBackStack: () -> Unit,
    updatePlaybackPosition: (Float) -> Unit,
    getPlaybackPosition: () -> Float,
    playerOrientationState: StateFlow<OrientationState>,
    updatePlayerOrientationState: (OrientationState) -> Unit,
    fullscreenWidgetIsClicked: StateFlow<Boolean>,
    setFullscreenWidgetIsClicked: (Boolean) -> Unit,
    connectivityStatus: Flow<ConnectivityStatus>
) {
    val networkConnectivityStatus by connectivityStatus.collectAsStateWithLifecycle(
        initialValue = ConnectivityStatus.Available
    )
    LaunchedEffect(networkConnectivityStatus) {
        if (networkConnectivityStatus == ConnectivityStatus.Lost) {
            Log.d("Snack", "PlayerScreen LaunchedEffect() event called")
            SnackbarController.sendEvent(
                event = SnackbarEvent(
                    message = "Wrong internet connection"
                )
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
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

            getRelatedVideos.invoke(video.snippet.title)

            val relatedVideoState = MutableStateFlow(relatedVideos).collectAsStateWithLifecycle()
            YouTubeVideoList(
                getVideoState = { relatedVideoState },
                modifier = Modifier,
                navigateToPlayerScreen = navigateToPlayerScreen,
            )
        }

    }
}
