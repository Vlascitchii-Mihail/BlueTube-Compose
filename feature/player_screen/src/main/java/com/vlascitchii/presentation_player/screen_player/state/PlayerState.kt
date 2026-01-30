package com.vlascitchii.presentation_player.screen_player.state

import androidx.paging.PagingData
import com.vlascitchii.presentation_common.model.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_common.network_observer.NetworkConnectivityStatus
import com.vlascitchii.presentation_common.ui.state.UiState
import com.vlascitchii.presentation_player.screen_player.OrientationState
import kotlinx.coroutines.flow.Flow

data class PlayerState(
    val relatedVideoState: UiState<Flow<PagingData<YoutubeVideoUiModel>>> = UiState.Loading,
    val isVideoPlaying: Boolean = true,
    val playerOrientationState: OrientationState = OrientationState.PORTRAIT,
    val fullscreenWidgetIsClicked: Boolean = false,
    val networkConnectivityStatus: NetworkConnectivityStatus = NetworkConnectivityStatus.Available,
    val isOrientationApproved: Boolean = false
)
