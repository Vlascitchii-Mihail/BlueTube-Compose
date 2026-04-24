package com.vlascitchii.presentation_video_list.screen

import com.vlascitchii.presentation_common.ui.screen.mvi.MviHandler
import com.vlascitchii.presentation_common.ui.state_common.UiSingleEvent
import com.vlascitchii.presentation_common.ui.video_list.state.UiVideoListAction
import com.vlascitchii.presentation_common.ui.video_list.state.VideoListNavigationEvent

class VideoListMviHandler(
    private val videoListViewModel: VideoListViewModel,
    private val navigationHandler: (UiSingleEvent) -> Unit
) : MviHandler<UiVideoListAction, VideoListNavigationEvent> {

    override fun submitAction(action: UiVideoListAction) {
        videoListViewModel.submitAction(action)
    }

    override fun submitSingleNavigationEvent(event: VideoListNavigationEvent) {
        navigationHandler.invoke(event)
    }
}
