package com.vlascitchii.presentation_video_list.screen

import androidx.paging.PagingData
import com.vlascitchii.common_test.rule.DispatcherTestRule
import com.vlascitchii.domain.model.videos.YoutubeVideoDomain
import com.vlascitchii.domain.usecase.VideoListUseCase
import com.vlascitchii.domain.util.UseCaseException
import com.vlascitchii.domain.util.VideoResult
import com.vlascitchii.presentation_common.model.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_common.network_observer.NetworkConnectivityObserver
import com.vlascitchii.presentation_common.network_observer.NetworkConnectivityStatus
import com.vlascitchii.presentation_common.ui.state_common.UiState
import com.vlascitchii.presentation_common.ui.video_list.state.SearchState
import com.vlascitchii.presentation_common.ui.video_list.state.UiVideoListAction
import com.vlascitchii.presentation_video_list.util.VideoListConverter
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.junit.Before

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class VideoListViewModelTest {

    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    private val videoListUseCase: VideoListUseCase = mock()
    private val videoListConverter: VideoListConverter = mock()
    private val networkConnectivityObserver: NetworkConnectivityObserver = mock()
    private lateinit var videoListViewModel: VideoListViewModel

    private val testQuery = "TestQuery"
    private val testErrorMessage = "Test Error Message"

    private val pagingData: Flow<PagingData<YoutubeVideoDomain>> =
        flowOf(PagingData.Companion.from(emptyList()))
    private val pagingUiData: Flow<PagingData<YoutubeVideoUiModel>> =
        flowOf(PagingData.Companion.from(emptyList()))

    private val expectedVideoListUseCaseResponse: VideoResult<VideoListUseCase.VideoListResponse> =
        VideoResult.Success(VideoListUseCase.VideoListResponse(pagingData))
    private val positiveConvertResult: UiState<Flow<PagingData<YoutubeVideoUiModel>>> =
        UiState.Success(pagingUiData)

    private fun positiveCase() {
        whenever(videoListUseCase.execute(any<VideoListUseCase.VideoListRequest>()))
            .thenReturn(flowOf(expectedVideoListUseCaseResponse))

        whenever(videoListConverter.convert(expectedVideoListUseCaseResponse))
            .thenReturn(positiveConvertResult)
    }

    private val runtimeVideoListException = RuntimeException("VideoList Exception")
    private val expectedNegativeVideoListUseCaseResponse: VideoResult<VideoListUseCase.VideoListResponse> = VideoResult
        .Error(UseCaseException.VideoListLoadException(runtimeVideoListException))

    private val negativeConvertResult: UiState<Flow<PagingData<YoutubeVideoUiModel>>> =
        UiState.Error(testErrorMessage)

    private fun negativeCase() {
        whenever(videoListUseCase.execute(any<VideoListUseCase.VideoListRequest>()))
             .thenReturn(flowOf(expectedNegativeVideoListUseCaseResponse))

        whenever(videoListConverter.convert(expectedNegativeVideoListUseCaseResponse))
            .thenReturn(negativeConvertResult)
    }

    @Before
    fun setup() {
        whenever(networkConnectivityObserver.observe())
            .thenReturn(flowOf(NetworkConnectivityStatus.Available))

        videoListViewModel = VideoListViewModel(
                videoListUseCase,
                videoListConverter,
                networkConnectivityObserver,
                dispatcherTestRule.testDispatcher
        )
    }

    @Test
    fun `fun updateSearchTextState() updates text in SearchAppBar`() {
        positiveCase()

        videoListViewModel.updateSearchTextState(testQuery)
        Assert.assertEquals(testQuery, videoListViewModel.searchTextState.value.text)
    }

    @Test
    fun `fun updateSearchState() changes the state of the SearchAppBar `() {
        positiveCase()

        Assert.assertTrue(videoListViewModel.searchBarState.value == SearchState.CLOSED)

        videoListViewModel.updateSearchState(SearchState.OPENED)
        Assert.assertTrue(videoListViewModel.searchBarState.value == SearchState.OPENED)

        videoListViewModel.updateSearchState(SearchState.CLOSED)
        Assert.assertTrue(videoListViewModel.searchBarState.value == SearchState.CLOSED)
    }

    @Test
    fun `getVideos() VideoRequest gets UiState Error and assigns it to the ViewModel state`() =
        runTest {
            negativeCase()

            videoListViewModel.getVideos("")
            advanceUntilIdle()
            val actualErrorResult = videoListViewModel.videoListUIStateFlow.first().videoListState

            verify(videoListConverter).convert(expectedNegativeVideoListUseCaseResponse)
            Assert.assertEquals(negativeConvertResult, actualErrorResult)
        }

    @Test
    fun `getVideos() SearchRequest gets UiState Error and assigns it to the ViewModel state`() =
        runTest {
            negativeCase()

            videoListViewModel.getVideos(testQuery)
            advanceUntilIdle()

            val actualErrorResult: UiState<Flow<PagingData<YoutubeVideoUiModel>>> =
                videoListViewModel.videoListUIStateFlow.first().videoListState

            verify(videoListUseCase).execute(any<VideoListUseCase.VideoListRequest>())
            verify(videoListConverter).convert(expectedNegativeVideoListUseCaseResponse)
            Assert.assertEquals(negativeConvertResult, actualErrorResult)
        }

    @Test
    fun `getVideos() VideoRequest gets UiState Success and assigns it to the ViewModel state`() =
        runTest {
            positiveCase()

            videoListViewModel.getVideos("")
            advanceUntilIdle()

            val actualResult = videoListViewModel.videoListUIStateFlow.first().videoListState

            verify(videoListUseCase).execute(any<VideoListUseCase.VideoListRequest>())
            verify(videoListConverter).convert(expectedVideoListUseCaseResponse)
            Assert.assertEquals(positiveConvertResult, actualResult)
        }

    @Test
    fun `getVideos() SearchRequest gets UiState Success and assigns it to the ViewModel state`() =
        runTest {
            positiveCase()

            videoListViewModel.getVideos(testQuery)
            advanceUntilIdle()

            val actualResult = videoListViewModel.videoListUIStateFlow.first().videoListState

            verify(videoListUseCase).execute(any<VideoListUseCase.VideoListRequest>())
            verify(videoListConverter).convert(expectedVideoListUseCaseResponse)
            Assert.assertEquals(positiveConvertResult, actualResult)
        }

    @Test
    fun `submitAction(GetVideo) triggers SearchRequest with the same query`() = runTest {
        positiveCase()

        val expectedTestData = "Test title"
        val testAction = UiVideoListAction.GetVideo(expectedTestData)

        videoListViewModel.submitAction(testAction)
        advanceUntilIdle()

        verify(videoListUseCase).execute(
            VideoListUseCase.VideoListRequest.SearchRequest(expectedTestData)
        )
    }

    @Test
    fun `submitAction(ChangeSearchBarAppearance) updates search bar state`() = runTest {
        val expectedState = SearchState.OPENED
        val testAction = UiVideoListAction.ChangeSearchBarAppearance(expectedState)

        videoListViewModel.submitAction(testAction)
        advanceUntilIdle()

        assertEquals(expectedState, videoListViewModel.searchBarState.value)
    }

    @Test
    fun `submitAction(TypeInSearchAppBarTextField) updates search text state`() = runTest {
        val expectedTypedText = "Test typed text"
        val testAction = UiVideoListAction.TypeInSearchAppBarTextField(expectedTypedText)

        videoListViewModel.submitAction(testAction)
        advanceUntilIdle()

        assertEquals(expectedTypedText, videoListViewModel.searchTextState.value.text)
    }
}
