package com.vlascitchii.presentation_common.ui.screen.mvi

import com.vlascitchii.presentation_common.ui.state_common.UiAction
import com.vlascitchii.presentation_common.ui.state_common.UiSingleEvent

interface MviHandler<ACTION: UiAction, EVENT: UiSingleEvent> {
    fun submitAction(action: ACTION)
    fun submitSingleNavigationEvent(event: EVENT)
}