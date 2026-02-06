package com.vlascitchii.presentation_video_list.screen

import com.vlascitchii.presentation_common.ui.screen.mvi.CommonMVI
import com.vlascitchii.presentation_common.ui.state_common.UiSingleEvent
import com.vlascitchii.presentation_common.ui.video_list.state.UiVideoListAction
import com.vlascitchii.presentation_common.ui.video_list.state.VideoListNavigationEvent
import kotlinx.coroutines.CoroutineScope

class VideoListMVI(
    private val videoListViewModel: VideoListViewModel,
    private val navigationHandler: (UiSingleEvent) -> Unit,
    coroutineScope: CoroutineScope
) : CommonMVI<UiVideoListAction, VideoListNavigationEvent>(coroutineScope) {

    override fun handleAction(action: UiVideoListAction) {
        when(action) {
            is UiVideoListAction.GetVideo -> videoListViewModel.getVideos(action.query)
            is UiVideoListAction.ChangeSearchBarAppearance -> videoListViewModel.updateSearchState(action.searchState)
            is UiVideoListAction.TypeInSearchAppBarTextField -> videoListViewModel.updateSearchTextState(action.query)
        }
    }

    override fun handleNavigationEvent(singleEvent: VideoListNavigationEvent) {
        navigationHandler.invoke(singleEvent)
    }
}
