package com.vlascitchii.presentation_common.ui.video_list.state

import com.vlascitchii.presentation_common.model.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_common.ui.state_common.UiSingleEvent

sealed class VideoListNavigationEvent : UiSingleEvent {

    data class NavigationToParticularSearchedVideoList(val searchVideoListQuery: String) : VideoListNavigationEvent()
    data class NavigationPlayerScreenEvent(val video: YoutubeVideoUiModel) : VideoListNavigationEvent()
}
