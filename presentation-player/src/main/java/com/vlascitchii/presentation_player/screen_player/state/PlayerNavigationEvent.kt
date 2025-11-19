package com.vlascitchii.presentation_player.screen_player.state

import com.vlascitchii.presentation_common.model.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_common.ui.state.UiSingleEvent

sealed class PlayerNavigationEvent : UiSingleEvent {

    data class NavigationPlayerScreenEvent(val video: YoutubeVideoUiModel) : PlayerNavigationEvent()
    data object PopBackStackEvent : PlayerNavigationEvent()
}
