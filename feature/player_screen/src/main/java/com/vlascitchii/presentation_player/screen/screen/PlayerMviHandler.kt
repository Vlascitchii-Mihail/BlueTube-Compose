package com.vlascitchii.presentation_player.screen.screen

import com.vlascitchii.presentation_common.ui.screen.mvi.MviHandler
import com.vlascitchii.presentation_common.ui.state_common.UiSingleEvent
import com.vlascitchii.presentation_player.screen.state.PlayerActionState
import com.vlascitchii.presentation_player.screen.state.PlayerNavigationEvent

class PlayerMviHandler(
    private val videoPlayerViewModel: VideoPlayerViewModel,
    private val navigationHandler: (UiSingleEvent) -> Unit,
    ) : MviHandler<PlayerActionState, PlayerNavigationEvent> {

    override fun submitAction(action: PlayerActionState) {
        videoPlayerViewModel.submitAction(action)
    }

    override fun submitSingleNavigationEvent(event: PlayerNavigationEvent) {
        navigationHandler.invoke(event)
    }
}
