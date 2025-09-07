package com.vlascitchii.presentation_player.screen_player.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.vlascitchii.domain.usecase.VideoPlayerUseCase
import com.vlascitchii.domain.util.VideoResult
import com.vlascitchii.presentation_common.model.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_common.network_observer.NetworkConnectivityObserver
import com.vlascitchii.presentation_common.network_observer.NetworkConnectivityStatus
import com.vlascitchii.presentation_common.ui.state.UiState
import com.vlascitchii.presentation_player.screen_player.OrientationState
import com.vlascitchii.presentation_player.screen_player.utils.VideoPlayerConverter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val INITIAL_VIDEO_PLAYBACK_POSITION = 0F

@HiltViewModel
class VideoPlayerViewModel @Inject constructor(
    private val videoPlayerUseCase: VideoPlayerUseCase,
    private val videoPlayerConverter: VideoPlayerConverter,
    private val networkConnectivityObserver: NetworkConnectivityObserver,
    ) : ViewModel() {

    private var _relatedVideoStateFlow: MutableStateFlow<UiState<Flow<PagingData<YoutubeVideoUiModel>>>> =
        MutableStateFlow<UiState<Flow<PagingData<YoutubeVideoUiModel>>>>(UiState.Loading)
    val relatedVideoStateFlow: StateFlow<UiState<Flow<PagingData<YoutubeVideoUiModel>>>> get() = _relatedVideoStateFlow

    var videoPlaybackPosition: Float = INITIAL_VIDEO_PLAYBACK_POSITION
        private set

    private val _isVideoPlaysFlow = MutableStateFlow(true)
    val isVideoPlaysFlow: StateFlow<Boolean> = _isVideoPlaysFlow

    private var _playerOrientationState = MutableStateFlow(OrientationState.PORTRAIT)
    val playerOrientationState: StateFlow<OrientationState> = _playerOrientationState

    private var _fullscreenWidgetIsClicked = MutableStateFlow(false)
    val fullscreenWidgetIsClicked: StateFlow<Boolean> = _fullscreenWidgetIsClicked

    private var _connectivityObserver:
            Flow<NetworkConnectivityStatus> = networkConnectivityObserver.observe()
    val connectivityObserver: Flow<NetworkConnectivityStatus> = _connectivityObserver

    fun updatePlaybackPosition(newPlaybackTime: Float) {
        videoPlaybackPosition = newPlaybackTime
    }

    fun getSearchedRelatedVideos(query: String) {
        viewModelScope.launch {
            videoPlayerUseCase.execute(VideoPlayerUseCase.PlayerRequest(query, viewModelScope))
                .map { playerSearchVideoResult: VideoResult<VideoPlayerUseCase.PlayerResponse> ->
                    videoPlayerConverter.convert(playerSearchVideoResult)
                }.collect { uiState: UiState<Flow<PagingData<YoutubeVideoUiModel>>> ->
                    _relatedVideoStateFlow.value = uiState
                }
        }
    }

    fun updateVideoPlayState(isPlaying: Boolean) {
        _isVideoPlaysFlow.value = isPlaying
    }

    fun updatePlayerOrientationState(newPlayerOrientationState: OrientationState) {
        _playerOrientationState.value = newPlayerOrientationState
    }

    fun setFullscreenWidgetIsClicked(isClicked: Boolean) {
        _fullscreenWidgetIsClicked.value = isClicked
    }
}
