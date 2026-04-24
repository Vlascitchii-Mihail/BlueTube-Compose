package com.vlascitchii.presentation_shorts.screen_shorts.state

import androidx.paging.PagingData
import com.vlascitchii.presentation_common.model.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_common.network_observer.NetworkConnectivityStatus
import com.vlascitchii.presentation_common.ui.state_common.UiState
import kotlinx.coroutines.flow.Flow

data class ShortsUiState(
    val shortsState: UiState<Flow<PagingData<YoutubeVideoUiModel>>> = UiState.Loading,
    val networkConnectivityStatus: NetworkConnectivityStatus = NetworkConnectivityStatus.Available
)
