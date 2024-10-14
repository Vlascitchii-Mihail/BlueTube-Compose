package com.appelier.bluetubecompose.screen_player

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideo
import com.appelier.bluetubecompose.screen_video_list.repository.VideoListRepository
import com.appelier.bluetubecompose.utils.VideoType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

private const val INITIAL_VIDEO_PLAYBACK_POSITION = 0F

@HiltViewModel
class VideoPlayerViewModel @Inject constructor(
    private val videoRepository: VideoListRepository,
) : ViewModel() {

    private val emptyPagingData = PagingData.empty<YoutubeVideo>()
    private var _relatedVideoStateFlow: MutableState<StateFlow<PagingData<YoutubeVideo>>> = mutableStateOf(
        MutableStateFlow(emptyPagingData)
    )
    private val relatedVideoStateFlow: State<StateFlow<PagingData<YoutubeVideo>>> get() = _relatedVideoStateFlow

    private var videoPlaybackPosition: Float = INITIAL_VIDEO_PLAYBACK_POSITION

    private val _isVideoPlaysFlow = MutableStateFlow(true)
    val isVideoPlaysFlow: StateFlow<Boolean> = _isVideoPlaysFlow

    private var _playerOrientationState = MutableStateFlow(OrientationState.PORTRAIT)
    val playerOrientationState: StateFlow<OrientationState> = _playerOrientationState

    private var _fullscreenWidgetIsClicked = MutableStateFlow(false)
    val fullscreenWidgetIsClicked:StateFlow<Boolean> = _fullscreenWidgetIsClicked

    fun getRelatedVideosState() = relatedVideoStateFlow

    fun updatePlaybackPosition(newPlaybackTime: Float = INITIAL_VIDEO_PLAYBACK_POSITION) {
        videoPlaybackPosition = newPlaybackTime
    }

    fun getCurrentPlaybackPosition(): Float {
        return if (videoPlaybackPosition > INITIAL_VIDEO_PLAYBACK_POSITION) videoPlaybackPosition
        else INITIAL_VIDEO_PLAYBACK_POSITION
    }

    fun getSearchedRelatedVideos(query: String) {
        if (_relatedVideoStateFlow.value.value == emptyPagingData) {
            val relatedVideosFlow = videoRepository
                .fetchVideos(VideoType.SearchedRelatedVideo(query), viewModelScope)
                .cachedIn(viewModelScope)
                .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())

            _relatedVideoStateFlow = mutableStateOf(relatedVideosFlow)
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
}
