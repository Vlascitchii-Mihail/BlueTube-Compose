package com.appelier.bluetubecompose.screen_video_list.screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.appelier.bluetubecompose.core.core_api.network_observer.ConnectivityStatus
import com.appelier.bluetubecompose.core.core_api.network_observer.NetworkConnectivityObserver
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideo
import com.appelier.bluetubecompose.screen_video_list.repository.VideoListRepository
import com.appelier.bluetubecompose.search_video.SearchState
import com.appelier.bluetubecompose.utils.VideoType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class VideoListViewModel @Inject constructor(
    private val videoRepository: VideoListRepository,
    private val networkConnectivityObserver: NetworkConnectivityObserver
): ViewModel() {

    private val emptyPagingData = PagingData.empty<YoutubeVideo>()
    private var _videoStateFlow: MutableState<StateFlow<PagingData<YoutubeVideo>>> =
        mutableStateOf(MutableStateFlow(emptyPagingData))
    private val videoStateFlow: State<StateFlow<PagingData<YoutubeVideo>>> = _videoStateFlow

    private var _searchState: MutableStateFlow<SearchState> = MutableStateFlow(SearchState.CLOSED)
    val searchState: StateFlow<SearchState> = _searchState

    private val _searchTextState: MutableStateFlow<String> = MutableStateFlow("")
    val searchTextState: StateFlow<String> = _searchTextState

    private var _connectivityObserver: Flow<ConnectivityStatus> = networkConnectivityObserver.observe()
    val connectivityObserver: Flow<ConnectivityStatus> = _connectivityObserver

    fun fetchPopularVideos() {
        if ( _videoStateFlow.value.value == emptyPagingData) {
            _videoStateFlow.value = videoRepository
                .fetchVideos(VideoType.Videos, viewModelScope).cachedIn(viewModelScope)
                .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())
        }
    }

    fun setSearchVideosFlow() {
        _videoStateFlow.value = videoRepository
            .fetchVideos(VideoType.SearchedVideo(_searchTextState.value), viewModelScope)
            .cachedIn(viewModelScope)
            .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())
    }

    fun updateSearchState(newSearchState: SearchState) {
        _searchState.value = newSearchState
    }

    fun updateSearchTextState(newSearchTextState: String) {
        _searchTextState.value = newSearchTextState
    }

    fun getVideos() = videoStateFlow
}
