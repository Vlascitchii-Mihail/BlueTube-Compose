package com.appelier.bluetubecompose.screen_video_list.screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideo
import com.appelier.bluetubecompose.screen_video_list.repository.VideoListRepository
import com.appelier.bluetubecompose.search_video.SearchState
import com.appelier.bluetubecompose.utils.VideoType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class VideoListViewModel @Inject constructor(
    private val videoRepository: VideoListRepository,
): ViewModel() {

    private var _videos: MutableState<StateFlow<PagingData<YoutubeVideo>>> = mutableStateOf(getVideosFlow())
    val videos: State<StateFlow<PagingData<YoutubeVideo>>> = _videos

    private val _searchState: MutableState<SearchState> = mutableStateOf(SearchState.CLOSED)
    val searchState: State<SearchState> = _searchState

    private val _searchTextState: MutableState<String> = mutableStateOf("")
    val searchTextState: State<String> = _searchTextState

    private var performedQuery = ""

    private fun getVideosFlow(): StateFlow<PagingData<YoutubeVideo>> {
        return videoRepository
            .fetchVideos(VideoType.Videos, viewModelScope).cachedIn(viewModelScope)
            .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())
    }

    fun getSearchVideosFlow(query: String = "") {
        if (query != "" && query != performedQuery) {
            _videos.value = videoRepository
                .fetchVideos(VideoType.SearchedVideo(query), viewModelScope)
                .cachedIn(viewModelScope)
                .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())
            performedQuery = query
        }
    }

    fun updateSearchState(newSearchState: SearchState) {
        _searchState.value = newSearchState
    }

    fun updateSearchTextState(newSearchTextState: String) {
        _searchTextState.value = newSearchTextState
    }
}