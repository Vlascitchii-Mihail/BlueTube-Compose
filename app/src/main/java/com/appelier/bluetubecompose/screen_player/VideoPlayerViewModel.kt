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

@HiltViewModel
class VideoPlayerViewModel @Inject constructor(
    private val videoRepository: VideoListRepository,
) : ViewModel() {

    private val emptyPagingData = PagingData.empty<YoutubeVideo>()
    private val _relatedVideoStateFlow: MutableState<StateFlow<PagingData<YoutubeVideo>>> = mutableStateOf(
        MutableStateFlow(emptyPagingData)
    )
    val relatedVideoStateFlow: State<StateFlow<PagingData<YoutubeVideo>>> get() = _relatedVideoStateFlow

    fun getSearchedRelatedVideos(query: String) {
        if (_relatedVideoStateFlow.value.value == emptyPagingData) {
            _relatedVideoStateFlow.value = videoRepository
                .fetchVideos(VideoType.SearchedRelatedVideo(query), viewModelScope)
                .cachedIn(viewModelScope)
                .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())
        }
    }
}
