package com.vlascitchii.presentation_player.screen_player.screen

import com.vlascitchii.presentation_common.ui.screen.mvi.CommonMVI
import com.vlascitchii.presentation_common.ui.state.UiSingleEvent
import com.vlascitchii.presentation_player.screen_player.state.PlayerActionState
import com.vlascitchii.presentation_player.screen_player.state.PlayerNavigationEvent
import kotlinx.coroutines.CoroutineScope

class PlayerMVI(
    private val videoPlayerViewModel: VideoPlayerViewModel,
    private val navigationHandler: (UiSingleEvent) -> Unit,
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
        navigationHandler.invoke(singleEvent)
    }
}
