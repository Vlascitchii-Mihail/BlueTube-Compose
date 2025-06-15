package com.vlascitchii.presentation_video_list.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.vlascitchii.domain.custom_coroutine_scopes.AppCoroutineScope
import com.vlascitchii.domain.usecase.SearchVideoListUseCase
import com.vlascitchii.domain.usecase.VideoListUseCase
import com.vlascitchii.domain.util.VideoResult
import com.vlascitchii.presentation_common.entity.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_common.network_observer.ConnectivityStatus
import com.vlascitchii.presentation_common.network_observer.NetworkConnectivityObserver
import com.vlascitchii.presentation_common.ui.state.UiState
import com.vlascitchii.presentation_video_list.util.SearchVideoListConverter
import com.vlascitchii.presentation_video_list.util.VideoListConverter
import com.vlascitchii.presentation_video_list.util.state.SearchState
import com.vlascitchii.presentation_video_list.util.state.VideoType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class VideoListViewModel @Inject constructor(
    private val videoListUseCase: VideoListUseCase,
    private val searchVideoListUseCase: SearchVideoListUseCase,
    private val videoListConverter: VideoListConverter,
    private val searchVideoListConverter: SearchVideoListConverter,
    private val networkConnectivityObserver: NetworkConnectivityObserver,
    @Named("video") private val videoCoroutineScope: AppCoroutineScope,
) : ViewModel() {

    //unknown if it will change the job in coroutine context variable in AppCoroutineScope class
//    init { remoteScope.onStart() }

    //    private val emptyPagingData = PagingData.Companion.empty<YoutubeVideoUiModel>()
    private var _videoStateFlow: MutableStateFlow<UiState<PagingData<YoutubeVideoUiModel>>> =
        MutableStateFlow<UiState<PagingData<YoutubeVideoUiModel>>>(UiState.Loading)
    val videoStateFlow: StateFlow<UiState<PagingData<YoutubeVideoUiModel>>> get() = _videoStateFlow

    private var _searchState: MutableStateFlow<SearchState> = MutableStateFlow(SearchState.CLOSED)
    val searchState: StateFlow<SearchState> = _searchState

    private val _searchTextState: MutableStateFlow<String> = MutableStateFlow("")
    val searchTextState: StateFlow<String> = _searchTextState

    private var _connectivityObserver:
            Flow<ConnectivityStatus> = networkConnectivityObserver.observe()
    val connectivityObserver: Flow<ConnectivityStatus> = _connectivityObserver

    fun fetchVideoPagingData(videoType: VideoType) {
        viewModelScope.launch {
            when (videoType) {
                is VideoType.PopularVideo -> {
                    videoListUseCase.execute(VideoListUseCase.Request)
                        .map { videoResult: VideoResult<VideoListUseCase.Response> ->
                            videoListConverter.convert(videoResult)
                        }.collect { uiState: UiState<PagingData<YoutubeVideoUiModel>> ->
                            _videoStateFlow.value = uiState
                        }
                }

                is VideoType.SearchVideo -> {
                    val searchRequest = SearchVideoListUseCase.Request(videoType.query)
                    searchVideoListUseCase.execute(searchRequest)
                        .map { videoResult: VideoResult<SearchVideoListUseCase.Response> ->
                            searchVideoListConverter.convert(videoResult)
                        }.collect { uiState: UiState<PagingData<YoutubeVideoUiModel>> ->
                            _videoStateFlow.value = uiState
                        }
                }
            }
        }
    }

    fun updateSearchState(newSearchState: SearchState) {
        _searchState.value = newSearchState
    }

    fun updateSearchTextState(newSearchTextState: String) {
        _searchTextState.value = newSearchTextState
    }

    //TODO: check is it possible to use only videoStateFlow without this function
//    fun getVideos() = videoStateFlow

    override fun onCleared() {
        super.onCleared()
        videoCoroutineScope.onStop()
    }
}