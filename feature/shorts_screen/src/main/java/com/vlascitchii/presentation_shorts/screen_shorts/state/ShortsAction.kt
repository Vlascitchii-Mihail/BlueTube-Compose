package com.vlascitchii.presentation_shorts.screen_shorts.state

import com.vlascitchii.presentation_common.ui.state_common.UiAction

sealed class ShortsAction : UiAction {

    data object ListenToVideoQueueAction : ShortsAction()
    data object FetchShortsAction : ShortsAction()
}