package com.vlascitchii.presentation_video_list.screen

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.vlascitchii.domain.custom_coroutine_scopes.AppCoroutineScope
import com.vlascitchii.domain.usecase.SearchVideoListUseCase
import com.vlascitchii.domain.usecase.VideoListUseCase
import com.vlascitchii.domain.util.VideoResult
import com.vlascitchii.presentation_common.CommonVideoViewModel
import com.vlascitchii.presentation_common.entity.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_common.network_observer.NetworkConnectivityObserver
import com.vlascitchii.presentation_common.ui.state.UiAction
import com.vlascitchii.presentation_common.ui.state.UiSingleEvent
import com.vlascitchii.presentation_common.ui.state.UiState
import com.vlascitchii.presentation_video_list.util.SearchVideoListConverter
import com.vlascitchii.presentation_video_list.util.VideoListConverter
import com.vlascitchii.presentation_video_list.screen.state.SearchState
import com.vlascitchii.presentation_video_list.screen.state.UiVideoListAction
import com.vlascitchii.presentation_video_list.screen.state.VideoType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class VideoListVideoViewModel @Inject constructor(
    private val videoListUseCase: VideoListUseCase,
    private val searchVideoListUseCase: SearchVideoListUseCase,
    private val videoListConverter: VideoListConverter,
    private val searchVideoListConverter: SearchVideoListConverter,
    private val networkConnectivityObserver: NetworkConnectivityObserver,
    @Named("video") private val videoCoroutineScope: AppCoroutineScope,
) : CommonVideoViewModel<YoutubeVideoUiModel, UiState<PagingData<YoutubeVideoUiModel>>, UiAction, UiSingleEvent>(
        networkConnectivityObserver
    ) {

    private var _searchBarState: MutableStateFlow<SearchState> = MutableStateFlow(SearchState.CLOSED)
    val searchBarState: StateFlow<SearchState> = _searchBarState

    private val _searchTextState: MutableStateFlow<String> = MutableStateFlow("")
    val searchTextState: StateFlow<String> = _searchTextState

    override fun handleAction(action: UiAction) {
        when(action) {
            is UiVideoListAction.Loading -> fetchVideoPagingData(VideoType.PopularVideo)
            is UiVideoListAction.SearchVideo -> fetchVideoPagingData(VideoType.SearchVideo(action.query))
            is UiVideoListAction.ChangeSearchBarAppearance -> updateSearchState(action.searchState)
            is UiVideoListAction.TypeInSearchAppBarTextField -> updateSearchTextState(action.query)
        }
    }

    override fun initState(): MutableStateFlow<UiState<PagingData<YoutubeVideoUiModel>>> =
        MutableStateFlow<UiState<PagingData<YoutubeVideoUiModel>>>(
            UiState.Loading
        )

    fun fetchVideoPagingData(videoType: VideoType) {
        viewModelScope.launch {
            when (videoType) {
                is VideoType.PopularVideo -> {
                    videoListUseCase.execute(VideoListUseCase.Request)
                        .map { videoResult: VideoResult<VideoListUseCase.Response> ->
                            videoListConverter.convert(videoResult)
                        }.collect { uiState: UiState<PagingData<YoutubeVideoUiModel>> ->
                            submitUiState(uiState)
                        }
                }

                is VideoType.SearchVideo -> {
                    val searchRequest = SearchVideoListUseCase.Request(videoType.query)
                    searchVideoListUseCase.execute(searchRequest)
                        .map { videoResult: VideoResult<SearchVideoListUseCase.Response> ->
                            searchVideoListConverter.convert(videoResult)
                        }.collect { uiState: UiState<PagingData<YoutubeVideoUiModel>> ->
                            submitUiState(uiState)
                        }
                }
            }
        }
    }

    fun updateSearchState(newSearchState: SearchState) {
        _searchBarState.value = newSearchState
    }

    fun updateSearchTextState(newSearchText: String) {
        _searchTextState.value = newSearchText
        submitAction(UiVideoListAction.SearchVideo(newSearchText))
    }

    override fun onCleared() {
        super.onCleared()
        videoCoroutineScope.onStop()
    }
}
