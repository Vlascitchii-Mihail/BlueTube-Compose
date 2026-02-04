package com.vlascitchii.presentation_video_list.screen

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.vlascitchii.domain.custom_scope.CustomCoroutineScope
import com.vlascitchii.domain.di_common.VIDEO_LIST_CONVERTER
import com.vlascitchii.domain.di_common.VIDEO_LIST_USE_CASE
import com.vlascitchii.domain.usecase.UseCase
import com.vlascitchii.domain.usecase.VideoListUseCase
import com.vlascitchii.domain.util.VideoResult
import com.vlascitchii.presentation_common.model.util.CommonResultConverter
import com.vlascitchii.presentation_common.model.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_common.network_observer.NetworkConnectivityAbstraction
import com.vlascitchii.presentation_common.ui.screen.CommonVideoViewModel
import com.vlascitchii.presentation_common.ui.state.UiAction
import com.vlascitchii.presentation_common.ui.state.UiState
import com.vlascitchii.presentation_video_list.screen.state.SearchState
import com.vlascitchii.presentation_video_list.screen.state.UiVideoListAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

private const val INITIAL_CURSOR_POSITION = 0

@HiltViewModel
class VideoListViewModel @Inject constructor(
    @param:Named(VIDEO_LIST_USE_CASE)
    private val videoListUseCase: UseCase<VideoListUseCase.VideoListRequest, VideoListUseCase.VideoListResponse>,
    @param:Named(VIDEO_LIST_CONVERTER)
    private val videoListConverter: CommonResultConverter<VideoListUseCase.VideoListResponse, @JvmSuppressWildcards Flow<PagingData<YoutubeVideoUiModel>>>,
    private val networkConnectivityObserver: NetworkConnectivityAbstraction,
    private val customCoroutineScope: CustomCoroutineScope
) : CommonVideoViewModel<YoutubeVideoUiModel, UiState<Flow<PagingData<YoutubeVideoUiModel>>>, UiAction>(
    networkConnectivityObserver, customCoroutineScope
) {

    private var _searchBarState: MutableStateFlow<SearchState> =
        MutableStateFlow(SearchState.CLOSED)
    val searchBarState: StateFlow<SearchState> = _searchBarState

    private val _searchTextState: MutableStateFlow<TextFieldValue> =
        MutableStateFlow(TextFieldValue("", selection = TextRange(INITIAL_CURSOR_POSITION)))
    val searchTextState: StateFlow<TextFieldValue> = _searchTextState

    override fun handleAction(action: UiAction) {
        when (action) {
            is UiVideoListAction.GetVideo -> getVideos(action.query)
            is UiVideoListAction.ChangeSearchBarAppearance -> updateSearchState(action.searchState)
            is UiVideoListAction.TypeInSearchAppBarTextField -> updateSearchTextState(action.query)
        }
    }

    override fun initState(): MutableStateFlow<UiState<Flow<PagingData<YoutubeVideoUiModel>>>> =
        MutableStateFlow<UiState<Flow<PagingData<YoutubeVideoUiModel>>>>(
            UiState.Loading
        )

    private fun getVideos(query: String = "") {
        viewModelScope.launch(customCoroutineScope.coroutineContext) {
            if (query.isEmpty()) {
                videoListUseCase.execute(
                    VideoListUseCase.VideoListRequest.VideoRequest(viewModelScope)
                )
                    .map { videoResult: VideoResult<VideoListUseCase.VideoListResponse> ->
                        videoListConverter.convert(videoResult)
                    }.collect { uiState: UiState<Flow<PagingData<YoutubeVideoUiModel>>> ->
                        submitUiState(uiState)
                    }
            } else {
                videoListUseCase.execute(
                    VideoListUseCase.VideoListRequest.SearchRequest(query, viewModelScope)
                )
                    .map { videoResult: VideoResult<VideoListUseCase.VideoListResponse> ->
                        videoListConverter.convert(videoResult)
                    }.collect { uiState: UiState<Flow<PagingData<YoutubeVideoUiModel>>> ->
                        submitUiState(uiState)
                    }
            }
        }
    }

    //TODO: set private
    fun updateSearchState(newSearchState: SearchState) {
        _searchBarState.value = newSearchState
    }

    //TODO: set private
    fun updateSearchTextState(newSearchText: String) {
        _searchTextState.value =
            TextFieldValue(text = newSearchText, selection = TextRange(newSearchText.length))
    }
}
