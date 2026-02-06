package com.vlascitchii.presentation_common.ui.video_list.state

import com.vlascitchii.presentation_common.ui.state_common.UiAction

sealed class UiVideoListAction : UiAction {

    data class GetVideo(val query: String = "") : UiVideoListAction()
    data class ChangeSearchBarAppearance(val searchState: SearchState) : UiVideoListAction()
    data class TypeInSearchAppBarTextField(val query: String) : UiVideoListAction()
}