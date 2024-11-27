package com.appelier.bluetubecompose.screen_player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.appelier.bluetubecompose.core.core_api.network_observer.ConnectivityStatus
import com.appelier.bluetubecompose.core.core_api.network_observer.NetworkConnectivityObserver
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideo
import com.appelier.bluetubecompose.screen_video_list.repository.VideoListRepository
import com.appelier.bluetubecompose.utils.VideoType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

private const val INITIAL_VIDEO_PLAYBACK_POSITION = 0F

@HiltViewModel
class VideoPlayerViewModel @Inject constructor(
    private val videoRepository: VideoListRepository,
    private val networkConnectivityObserver: NetworkConnectivityObserver
) : ViewModel() {

    private val emptyPagingData = PagingData.empty<YoutubeVideo>()
    private var _relatedVideoStateFlow: StateFlow<PagingData<YoutubeVideo>> = MutableStateFlow(emptyPagingData)
    val relatedVideoStateFlow: StateFlow<PagingData<YoutubeVideo>> get() = _relatedVideoStateFlow

    private var videoPlaybackPosition: Float = INITIAL_VIDEO_PLAYBACK_POSITION

    private val _isVideoPlaysFlow = MutableStateFlow(true)
    val isVideoPlaysFlow: StateFlow<Boolean> = _isVideoPlaysFlow

    private var _playerOrientationState = MutableStateFlow(OrientationState.PORTRAIT)
    val playerOrientationState: StateFlow<OrientationState> = _playerOrientationState

    private var _fullscreenWidgetIsClicked = MutableStateFlow(false)
    val fullscreenWidgetIsClicked:StateFlow<Boolean> = _fullscreenWidgetIsClicked

    fun updatePlaybackPosition(newPlaybackTime: Float = INITIAL_VIDEO_PLAYBACK_POSITION) {
        videoPlaybackPosition = newPlaybackTime
    }

    fun getCurrentPlaybackPosition(): Float {
        return if (videoPlaybackPosition > INITIAL_VIDEO_PLAYBACK_POSITION) videoPlaybackPosition
        else INITIAL_VIDEO_PLAYBACK_POSITION
    }

    fun getSearchedRelatedVideos(query: String) {
        if (_relatedVideoStateFlow.value == emptyPagingData) {
            _relatedVideoStateFlow = videoRepository
                .fetchVideos(VideoType.SearchedRelatedVideo(query), viewModelScope)
                .cachedIn(viewModelScope)
                .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())
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
