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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

private const val INITIAL_VIDEO_PLAYBACK_POSITION = 0F

@HiltViewModel
class VideoPlayerViewModel @Inject constructor(
    private val videoRepository: VideoListRepository,
) : ViewModel() {

    private var _relatedVideoStateFlow: MutableState<StateFlow<PagingData<YoutubeVideo>>> ? = null
    val relatedVideoStateFlow: State<StateFlow<PagingData<YoutubeVideo>>>? get() = _relatedVideoStateFlow
    private var videoPlaybackPosition: Float = INITIAL_VIDEO_PLAYBACK_POSITION

    var youTubePlayerPlayState = mutableStateOf(true)

    fun updatePlaybackPosition(newPlaybackTime: Float = INITIAL_VIDEO_PLAYBACK_POSITION) {
        videoPlaybackPosition = newPlaybackTime
    }

    fun getCurrentPlaybackPosition(): Float {
        return if (videoPlaybackPosition > INITIAL_VIDEO_PLAYBACK_POSITION) videoPlaybackPosition
        else INITIAL_VIDEO_PLAYBACK_POSITION
    }

    fun getSearchedRelatedVideos(query: String) {
        if (_relatedVideoStateFlow == null) {
            val relatedVideosFlow = videoRepository
                .fetchVideos(VideoType.SearchedRelatedVideo(query), viewModelScope)
                .cachedIn(viewModelScope)
                .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())

            _relatedVideoStateFlow = mutableStateOf(relatedVideosFlow)
        }
    }
}
