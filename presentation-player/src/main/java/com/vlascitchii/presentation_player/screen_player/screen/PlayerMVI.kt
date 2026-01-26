package com.vlascitchii.presentation_player.screen_player.screen

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.vlascitchii.presentation_common.model.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_common.ui.screen.ScreenType
import com.vlascitchii.presentation_common.ui.screen.mvi.CommonMVI
import com.vlascitchii.presentation_player.screen_player.state.PlayerActionState
import com.vlascitchii.presentation_player.screen_player.state.PlayerNavigationEvent
import kotlinx.coroutines.CoroutineScope

class PlayerMVI(
    private val videoPlayerViewModel: VideoPlayerViewModel,
    private val backStack: NavBackStack<NavKey>,
    coroutineScope: CoroutineScope
) : CommonMVI<PlayerActionState, PlayerNavigationEvent>(
    coroutineScope,
) {
    override fun handleAction(action: PlayerActionState) {
        when(action) {
            is PlayerActionState.GetRelatedVideosAction -> videoPlayerViewModel.getSearchedRelatedVideos(action.videoTitle)
            is PlayerActionState.UpdatePlayStateAction -> videoPlayerViewModel.updateVideoPlayState(action.isPlaying)
            is PlayerActionState.UpdatePlaybackPositionAction -> videoPlayerViewModel.updatePlaybackPosition(action.playbackPosition)
            is PlayerActionState.UpdatePlayerOrientationStateAction -> videoPlayerViewModel.updatePlayerOrientationState(action.orientationState)
            is PlayerActionState.ApproveOrientationChange -> { videoPlayerViewModel.setInitialScreenLaunchRotationController(action.isOrientationApproved) }
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
