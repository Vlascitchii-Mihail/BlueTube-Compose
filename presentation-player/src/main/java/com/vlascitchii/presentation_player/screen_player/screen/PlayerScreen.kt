package com.vlascitchii.presentation_player.screen_player.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.vlascitchii.presentation_common.model.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_common.network_observer.NetworkConnectivityStatus
import com.vlascitchii.presentation_common.ui.global_snackbar.SnackBarController.sendEvent
import com.vlascitchii.presentation_common.ui.global_snackbar.SnackBarEvent
import com.vlascitchii.presentation_common.ui.screen.CommonMVI
import com.vlascitchii.presentation_common.ui.screen.CommonScreen
import com.vlascitchii.presentation_common.ui.screen.PagerContentManager
import com.vlascitchii.presentation_common.ui.state.UiState
import com.vlascitchii.presentation_common.ui.video_list.ItemsList
import com.vlascitchii.presentation_player.screen_player.OrientationState
import com.vlascitchii.presentation_player.screen_player.state.PlayerActionState
import com.vlascitchii.presentation_player.screen_player.state.PlayerNavigationEvent
import com.vlascitchii.presentation_player.screen_player.state.PlayerState
import com.vlascitchii.presentation_player.screen_player.ui.VideoDescription
import com.vlascitchii.presentation_player.screen_player.ui.YoutubeVideoPlayer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun PlayerScreen(
    video: YoutubeVideoUiModel,
    playerStateFlow: StateFlow<PlayerState>,
    playerMVI: CommonMVI<YoutubeVideoUiModel,UiState<Flow<PagingData<YoutubeVideoUiModel>>>, PlayerActionState, PlayerNavigationEvent>,
    playbackPosition: Float,
    bottomNavigation: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {

    playerStateFlow.collectAsStateWithLifecycle().value.let { playerUiState:PlayerState ->
        if (playerUiState.relatedVideoState == UiState.Loading) playerMVI.submitAction(
            PlayerActionState.GetRelatedVideosAction(video.snippet.title)
        )
        CommonScreen(playerUiState.relatedVideoState) { pagingData: Flow<PagingData<YoutubeVideoUiModel>> ->

            LaunchedEffect(playerUiState.networkConnectivityStatus) {
                if (playerUiState.networkConnectivityStatus == NetworkConnectivityStatus.Lost) {
                    sendEvent(
                        event = SnackBarEvent(
                            message = "Wrong internet connection"
                        )
                    )
                }
            }

            Scaffold(
                modifier = modifier.fillMaxSize(),
                bottomBar = {
                    if (playerUiState.playerOrientationState == OrientationState.PORTRAIT) {
                    bottomNavigation.invoke()
                    }
                }
            ) { innerPadding: PaddingValues ->
                Column(
                    modifier = modifier
                        .padding(if (playerUiState.playerOrientationState == OrientationState.PORTRAIT) innerPadding else PaddingValues())
                        .fillMaxSize()
                ) {
                    //TODO: Move outside Common screen
                    YoutubeVideoPlayer(
                        videoId = video.id,
                        modifier,
                        playerUiState,
                        playerMVI,
                        playbackPosition
                    )

                    if (playerUiState.playerOrientationState == OrientationState.PORTRAIT) {
                        VideoDescription(video = video)

                        val lazyPagingData = pagingData.collectAsLazyPagingItems()

                        PagerContentManager(
                            videoState = lazyPagingData,
                            contentList = {
                                ItemsList(
                                    lazyPagingData,
                                    modifier,
                                    //TODO: Refactor after update VideoListScreen
                                    navigateToPlayerScreen = { video: YoutubeVideoUiModel -> playerMVI.submitSingleNavigationEvent(
                                        PlayerNavigationEvent.NavigationPlayerScreenEvent(video)
                                    ) },
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
}
