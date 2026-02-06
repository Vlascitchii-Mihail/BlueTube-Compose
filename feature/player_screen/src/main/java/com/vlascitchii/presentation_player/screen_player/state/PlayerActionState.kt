package com.vlascitchii.presentation_player.screen_player.state

import com.vlascitchii.presentation_common.ui.state_common.UiAction
import com.vlascitchii.presentation_player.screen_player.OrientationState

sealed class PlayerActionState : UiAction {

    data class GetRelatedVideosAction(val videoTitle: String) : PlayerActionState()
    data class UpdatePlayStateAction(val isPlaying: Boolean) : PlayerActionState()
    data class UpdatePlaybackPositionAction(val playbackPosition: Float) : PlayerActionState()
    data class UpdatePlayerOrientationStateAction(val orientationState: OrientationState) : PlayerActionState()
    data class ApproveOrientationChange(val isOrientationApproved: Boolean) : PlayerActionState()
}
