package com.appelier.bluetubecompose.screen_video_list.screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideo
import com.appelier.bluetubecompose.screen_video_list.repository.VideoListRepository
import com.appelier.bluetubecompose.search_video.SearchState
import com.appelier.bluetubecompose.utils.VideoType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class VideoListViewModel @Inject constructor(
    private val videoRepository: VideoListRepository,
): ViewModel() {

    private val emptyPagingData = PagingData.empty<YoutubeVideo>()
    private val _videoStateFlow: MutableState<StateFlow<PagingData<YoutubeVideo>>> = mutableStateOf(MutableStateFlow(emptyPagingData))
    val videoStateFlow: State<StateFlow<PagingData<YoutubeVideo>>> get() = _videoStateFlow

    private val _searchState: MutableState<SearchState> = mutableStateOf(SearchState.CLOSED)
    val searchState: State<SearchState> = _searchState

    private val _searchTextState: MutableState<String> = mutableStateOf("")
    val searchTextState: State<String> = _searchTextState

    fun getVideosFlow() {
        if (_videoStateFlow.value.value == emptyPagingData) {
            _videoStateFlow.value = videoRepository
                .fetchVideos(VideoType.Videos, viewModelScope).cachedIn(viewModelScope)
                .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())
        }
    }

    fun getSearchVideosFlow(query: String = "") {
            _videoStateFlow.value = videoRepository
                .fetchVideos(VideoType.SearchedVideo(query), viewModelScope)
                .cachedIn(viewModelScope)
                .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())
    }

    fun updateSearchState(newSearchState: SearchState) {
        _searchState.value = newSearchState
    }

    fun updateSearchTextState(newSearchTextState: String) {
        _searchTextState.value = newSearchTextState
    }
}


class VideoListViewModelFactory(
    private val videoRepository: VideoListRepository,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VideoListViewModel::class.java)) {
            return VideoListViewModel(videoRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}