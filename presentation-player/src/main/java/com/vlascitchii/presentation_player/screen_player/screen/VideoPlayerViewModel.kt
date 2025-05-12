package com.vlascitchii.presentation_player.screen_player.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.vlascitchii.domain.usecase.VideoPlayerUseCase
import com.vlascitchii.presentation_common.entity.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_common.network_observer.ConnectivityStatus
import com.vlascitchii.presentation_common.network_observer.NetworkConnectivityObserver
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
    private val networkConnectivityObserver: NetworkConnectivityObserver
) : ViewModel() {

    private val emptyPagingData = PagingData.empty<YoutubeVideoUiModel>()
    private var _relatedVideoStateFlow: MutableStateFlow<UiState<StateFlow<PagingData<YoutubeVideoUiModel>>>> =
        MutableStateFlow<UiState<StateFlow<PagingData<YoutubeVideoUiModel>>>>(UiState.Loading)
    val relatedVideoStateFlow: StateFlow<UiState<StateFlow<PagingData<YoutubeVideoUiModel>>>> get() = _relatedVideoStateFlow

    private var videoPlaybackPosition: Float = INITIAL_VIDEO_PLAYBACK_POSITION

    private val _isVideoPlaysFlow = MutableStateFlow(true)
    val isVideoPlaysFlow: StateFlow<Boolean> = _isVideoPlaysFlow

    private var _playerOrientationState = MutableStateFlow(OrientationState.PORTRAIT)
    val playerOrientationState: StateFlow<OrientationState> = _playerOrientationState

    private var _fullscreenWidgetIsClicked = MutableStateFlow(false)
    val fullscreenWidgetIsClicked: StateFlow<Boolean> = _fullscreenWidgetIsClicked

    fun updatePlaybackPosition(newPlaybackTime: Float = INITIAL_VIDEO_PLAYBACK_POSITION) {
        videoPlaybackPosition = newPlaybackTime
    }

    fun getCurrentPlaybackPosition(): Float {
        return if (videoPlaybackPosition > INITIAL_VIDEO_PLAYBACK_POSITION) videoPlaybackPosition
        else INITIAL_VIDEO_PLAYBACK_POSITION
    }

    fun getSearchedRelatedVideos(query: String) {
        viewModelScope.launch {
            if (_relatedVideoStateFlow.value == emptyPagingData) {
                videoPlayerUseCase.execute(VideoPlayerUseCase.Request(query)).map {
                    videoPlayerConverter.convert(it)
                }.collect {
                    _relatedVideoStateFlow.value = it
                }
            }
        }
    }

    fun updateVideoIsPlayState(isPlaying: Boolean) {
        _isVideoPlaysFlow.value = isPlaying
    }

    fun updatePlayerOrientationState(newPlayerOrientationState: OrientationState) {
        _playerOrientationState.value = newPlayerOrientationState
    }

    fun setFullscreenWidgetIsClicked(isClicked: Boolean) {
        _fullscreenWidgetIsClicked.value = isClicked
    }

    fun getCurrentConnectivityState(): Flow<ConnectivityStatus> {
        return networkConnectivityObserver.observe()
    }
}