package com.vlascitchii.presentation_shorts.screen_shorts.screen

import com.vlascitchii.presentation_common.ui.screen.mvi.MviHandler
import com.vlascitchii.presentation_common.ui.state_common.UiSingleEvent
import com.vlascitchii.presentation_shorts.screen_shorts.state.ShortsAction
import com.vlascitchii.presentation_shorts.screen_shorts.state.ShortsUIEvent

class ShortsMviHandler(
    private val shortsViewModel: ShortsViewModel,
    private val navigationHandler: (UiSingleEvent) -> Unit
) : MviHandler<ShortsAction, ShortsUIEvent> {

    override fun submitAction(action: ShortsAction) {
        shortsViewModel.submitAction(action)
    }

    override fun submitSingleNavigationEvent(event: ShortsUIEvent) {
        navigationHandler.invoke(event)
    }
}