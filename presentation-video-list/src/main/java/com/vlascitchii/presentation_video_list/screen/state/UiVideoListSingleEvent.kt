package com.vlascitchii.presentation_video_list.screen.state

import com.vlascitchii.presentation_common.entity.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_common.ui.state.UiSingleEvent

sealed class UiVideoListSingleEvent : UiSingleEvent {

    data class NavigationToPlayerEvent(val video: YoutubeVideoUiModel) : UiVideoListSingleEvent()
    data class NavigationToSearchVideoList(val videoType: VideoType) : UiVideoListSingleEvent()
}