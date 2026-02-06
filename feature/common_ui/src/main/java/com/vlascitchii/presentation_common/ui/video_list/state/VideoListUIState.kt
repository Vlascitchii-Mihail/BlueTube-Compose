package com.vlascitchii.presentation_common.ui.video_list.state

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.paging.PagingData
import com.vlascitchii.presentation_common.model.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_common.network_observer.NetworkConnectivityStatus
import com.vlascitchii.presentation_common.ui.state_common.UiState
import kotlinx.coroutines.flow.Flow

const val INITIAL_CURSOR_POSITION = 0

data class VideoListUIState(
    val videoListState : UiState<Flow<PagingData<YoutubeVideoUiModel>>> = UiState.Loading,
    val searchBarState : SearchState = SearchState.CLOSED,
    val searchTextState : TextFieldValue = TextFieldValue("", selection = TextRange(INITIAL_CURSOR_POSITION)),
    val networkConnectivityStatus: NetworkConnectivityStatus = NetworkConnectivityStatus.Available
)
