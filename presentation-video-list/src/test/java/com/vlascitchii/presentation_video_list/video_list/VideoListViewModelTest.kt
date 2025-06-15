package com.vlascitchii.presentation_video_list.video_list

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.vlascitchii.domain.custom_coroutine_scopes.AppCoroutineScope
import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideo
import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideoResponse.Companion.RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG
import com.vlascitchii.presentation_common.paging.CommonPager
import com.vlascitchii.domain.usecase.SearchVideoListUseCase
import com.vlascitchii.domain.usecase.VideoListUseCase
import com.vlascitchii.domain.util.VideoResult
import com.vlascitchii.presentation_common.entity.util.convertToYoutubeVideoUiMode
import com.vlascitchii.presentation_common.entity.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_common.network_observer.NetworkConnectivityObserver
import com.vlascitchii.presentation_common.ui.state.UiState
import com.vlascitchii.presentation_video_list.rule.DispatcherTestRule
import com.vlascitchii.presentation_video_list.screen.VideoListViewModel
import com.vlascitchii.presentation_video_list.util.state.SearchState
import com.vlascitchii.presentation_video_list.util.SearchVideoListConverter
import com.vlascitchii.presentation_video_list.util.VideoListConverter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.mock

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class VideoListViewModelTest {

    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    private val videoListUseCase: VideoListUseCase = mock()
    private val searchVideoListUseCase: SearchVideoListUseCase = mock()
    private val videoListConverter: VideoListConverter = mock()
    private val searchVideoListConverter: SearchVideoListConverter = mock()
    private val networkConnectivityObserver: NetworkConnectivityObserver = mock()
    private val videoCoroutineScope: AppCoroutineScope = mock()
    private val videoListViewModel = VideoListViewModel(
        videoListUseCase,
        searchVideoListUseCase,
        videoListConverter,
        searchVideoListConverter,
        networkConnectivityObserver,
        videoCoroutineScope
    )

    private val initialPageToken = ""
    private val testRequest = VideoListUseCase.Request(initialPageToken)
    private val pager = Pager(
        config = PagingConfig(
            pageSize = 5,
            prefetchDistance = 15
        ),
        pagingSourceFactory = {
            CommonPager { pageToken: String ->
                flowOf(RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG)
            }
        }
    )
//    private val useCaseResponse = flowOf(VideoResult.Success(VideoListUseCase.Response(pager)))
//    private val convertQuery = VideoResult.Success(VideoListUseCase.Response(pager))
//
//    private val videoUiItems = RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG.items.map { item: YoutubeVideo ->
//        item.copy().convertToYoutubeVideoUiMode()
//    }
//    private val pagingData = PagingData.from(videoUiItems)
//    val videoUiStateFlow: UiState<StateFlow<PagingData<YoutubeVideoUiModel>>> =
//        UiState.Success(MutableStateFlow(pagingData))

    @Before
    fun init() {
//        whenever(videoListUseCase.execute(testRequest)).thenReturn(useCaseResponse)
//        whenever(videoListConverter.convert(convertQuery)).thenReturn(videoUiStateFlow)
    }

    //TODO: use in Integration test
//    @Test
//    fun `fun fetchPopularVideos() receives Flow with Pager fom VideoListUseCase, converts PagingData content to YoutubeVideoUiModel`() = runTest {
//        videoListViewModel.fetchPopularVideos()
//
//        advanceUntilIdle()
//        assertTrue(videoListViewModel.getVideos().value is UiState.Success)
//        assertEquals(videoUiStateFlow, videoListViewModel.getVideos().value)
//
//        //Todo:
////        TODO("Use in another test to check the real data inside PagingData ")
////        (videoUiStateFlow as UiState.Success).data.value.map { it }
//    }

    @Test
    fun `fun updateSearchTextState() updates text in SearchAppBar`() {
        val testText = "TestText"
        videoListViewModel.updateSearchTextState(testText)
        assertEquals(testText, videoListViewModel.searchTextState.value)
    }

    @Test
    fun `fun updateSearchState() changes the state of the SearchAppBar `() {
        assertTrue(videoListViewModel.searchState.value == SearchState.CLOSED)

        videoListViewModel.updateSearchState(SearchState.OPENED)
        assertTrue(videoListViewModel.searchState.value == SearchState.OPENED)

        videoListViewModel.updateSearchState(SearchState.CLOSED)
        assertTrue(videoListViewModel.searchState.value == SearchState.CLOSED)
    }
}