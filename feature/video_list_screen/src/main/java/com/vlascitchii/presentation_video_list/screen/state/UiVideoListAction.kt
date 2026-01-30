package com.vlascitchii.presentation_video_list.screen.state

import com.vlascitchii.presentation_common.ui.state.UiAction

sealed class UiVideoListAction : UiAction {

    data class GetVideo(val query: String) : UiVideoListAction()
    data class ChangeSearchBarAppearance(val searchState: SearchState) : UiVideoListAction()
    data class TypeInSearchAppBarTextField(val query: String) : UiVideoListAction()
}