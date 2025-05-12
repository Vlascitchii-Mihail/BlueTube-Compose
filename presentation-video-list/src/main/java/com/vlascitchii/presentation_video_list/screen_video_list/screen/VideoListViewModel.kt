package com.vlascitchii.presentation_video_list.screen_video_list.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.vlascitchii.domain.usecase.SearchVideoListUseCase
import com.vlascitchii.domain.usecase.VideoListUseCase
import com.vlascitchii.presentation_common.entity.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_common.network_observer.ConnectivityStatus
import com.vlascitchii.presentation_common.network_observer.NetworkConnectivityObserver
import com.vlascitchii.presentation_common.ui.state.UiState
import com.vlascitchii.presentation_video_list.search_video.SearchState
import com.vlascitchii.presentation_video_list.util.SearchVideoListConverter
import com.vlascitchii.presentation_video_list.util.VideoListConverter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoListViewModel @Inject constructor(
    private val videoListUseCase: VideoListUseCase,
    private val searchVideoListUseCase: SearchVideoListUseCase,
    private val videoListConverter: VideoListConverter,
    private val searchVideoListConverter: SearchVideoListConverter,
    private val networkConnectivityObserver: NetworkConnectivityObserver,
): ViewModel() {

    //unknown if it will change the job in coroutine context variable in AppCoroutineScope class
//    init { remoteScope.onStart() }

    private val emptyPagingData = PagingData.empty<YoutubeVideoUiModel>()
    private var _videoStateFlow: MutableStateFlow<UiState<StateFlow<PagingData<YoutubeVideoUiModel>>>> =
        MutableStateFlow<UiState<StateFlow<PagingData<YoutubeVideoUiModel>>>>(UiState.Loading)
    private val videoStateFlow: StateFlow<UiState<StateFlow<PagingData<YoutubeVideoUiModel>>>> get() = _videoStateFlow

    private var _searchState: MutableStateFlow<SearchState> = MutableStateFlow(SearchState.CLOSED)
    val searchState: StateFlow<SearchState> = _searchState

    private val _searchTextState: MutableStateFlow<String> = MutableStateFlow("")
    val searchTextState: StateFlow<String> = _searchTextState

    private var _connectivityObserver: Flow<ConnectivityStatus> = networkConnectivityObserver.observe()
    val connectivityObserver: Flow<ConnectivityStatus> = _connectivityObserver

    fun fetchPopularVideos() {
        viewModelScope.launch {
//            if ( _videoStateFlow.value.value == emptyPagingData) {

                //TODO: add repeatOnLifecycle
                videoListUseCase.execute(VideoListUseCase.Request()).map {
                    videoListConverter.convert(it)
                }.collect {
                    _videoStateFlow.value = it
                }
//            }
        }
    }

    fun setSearchVideosFlow() {

        viewModelScope.launch {
            //TODO: pageToken = EMPTY_QUERY
            searchVideoListUseCase.execute(SearchVideoListUseCase.Request(pageToken = "", query = _searchTextState.value)).map {
                searchVideoListConverter.convert(it)
            }.collect {
                _videoStateFlow.value = it
            }
        }
    }

    fun updateSearchState(newSearchState: SearchState) {
        _searchState.value = newSearchState
    }

    fun updateSearchTextState(newSearchTextState: String) {
        _searchTextState.value = newSearchTextState
    }

    fun getVideos() = videoStateFlow

    override fun onCleared() {
        super.onCleared()
//        pagerCoroutineScope.onStop()
//        videoCoroutineScope.onStop()
    }
}
