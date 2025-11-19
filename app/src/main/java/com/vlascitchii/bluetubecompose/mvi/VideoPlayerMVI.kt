package com.vlascitchii.bluetubecompose.mvi

import androidx.lifecycle.viewModelScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.paging.PagingData
import com.vlascitchii.bluetubecompose.navigation.ScreenType
import com.vlascitchii.presentation_common.model.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_common.ui.screen.CommonMVI
import com.vlascitchii.presentation_common.ui.state.UiState
import com.vlascitchii.presentation_player.screen_player.screen.VideoPlayerViewModel
import com.vlascitchii.presentation_player.screen_player.state.PlayerActionState
import com.vlascitchii.presentation_player.screen_player.state.PlayerNavigationEvent
import kotlinx.coroutines.flow.Flow

class PlayerMVI(
    private val videoPlayerViewModel: VideoPlayerViewModel,
    private val backStack: NavBackStack<NavKey>
) : CommonMVI<YoutubeVideoUiModel,UiState<Flow<PagingData<YoutubeVideoUiModel>>>, PlayerActionState, PlayerNavigationEvent>(
    videoPlayerViewModel.viewModelScope,
) {
    override fun handleAction(action: PlayerActionState) {
        when(action) {
            is PlayerActionState.GetRelatedVideosAction -> videoPlayerViewModel.getSearchedRelatedVideos(action.videoTitle)
            is PlayerActionState.UpdatePlayStateAction -> videoPlayerViewModel.updateVideoPlayState(action.isPlaying)
            is PlayerActionState.UpdatePlaybackPositionAction -> videoPlayerViewModel.updatePlaybackPosition(action.playbackPosition)
            is PlayerActionState.UpdatePlayerOrientationStateAction -> videoPlayerViewModel.updatePlayerOrientationState(action.orientationState)
            is PlayerActionState.SetFullscreenWidgetState -> videoPlayerViewModel.setFullscreenWidgetIsClicked(action.isClicked)
        }
    }

    override fun handleNavigationEvent(singleEvent: PlayerNavigationEvent) {
        when(singleEvent) {
            is PlayerNavigationEvent.NavigationPlayerScreenEvent -> navigateToPlayerScreen(singleEvent.video)
            PlayerNavigationEvent.PopBackStackEvent -> backStack.removeLastOrNull()
        }
    }

    private fun navigateToPlayerScreen(video: YoutubeVideoUiModel) {
        backStack.removeLastOrNull()
        backStack.add(ScreenType.PlayerScreen(video))
    }
}
