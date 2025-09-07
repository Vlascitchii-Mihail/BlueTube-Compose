package com.vlascitchii.presentation_video_list.screen.state

import com.vlascitchii.presentation_common.ui.state.UiAction

sealed class UiVideoListAction : UiAction {

    object Loading : UiVideoListAction()
    data class ChangeSearchBarAppearance(val searchState: SearchState) : UiVideoListAction()
    data class TypeInSearchAppBarTextField(val query: String) : UiVideoListAction()
    data class SearchVideo(val query: String) : UiVideoListAction()
}