package com.vlascitchii.presentation_video_list.video_list

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import com.vlascitchii.domain.custom_coroutine_scopes.AppCoroutineScope
import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideoResponse.Companion.RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG
import com.vlascitchii.domain.usecase.SearchVideoListUseCase
import com.vlascitchii.domain.usecase.VideoListUseCase
import com.vlascitchii.domain.util.UseCaseException
import com.vlascitchii.domain.util.VideoResult
import com.vlascitchii.presentation_common.entity.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_common.network_observer.NetworkConnectivityObserver
import com.vlascitchii.presentation_common.ui.state.UiState
import com.vlascitchii.presentation_video_list.rule.DispatcherTestRule
import com.vlascitchii.presentation_video_list.screen.VideoListViewModel
import com.vlascitchii.presentation_video_list.util.SearchVideoListConverter
import com.vlascitchii.presentation_video_list.util.TestPagingDataDiffer
import com.vlascitchii.presentation_video_list.util.VideoListConverter
import com.vlascitchii.presentation_video_list.util.state.SearchState
import com.vlascitchii.presentation_video_list.util.state.VideoType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class VideoListViewModelTest {

    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    private val videoListUseCase: VideoListUseCase = mock()
    private val searchVideoListUseCase: SearchVideoListUseCase = mock()
    private val videoListConverter: VideoListConverter = VideoListConverter()
    private val searchVideoListConverter: SearchVideoListConverter = SearchVideoListConverter()
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

    private val pagingData = PagingData.from(RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG.items)
    private val testQuery = "TestQuery"

    private val expectedVideoListUseCaseResponse = VideoResult.Success(VideoListUseCase.Response(pagingData))
    private val expectedSearchVideoUseCaseResponse = VideoResult.Success(SearchVideoListUseCase.Response(pagingData))

    private val runtimeVideoListException = RuntimeException("VideoList Exception")
    private val expectedNegativeVideoListUseCaseResponse = VideoResult.Error(
        UseCaseException.VideoListLoadException(runtimeVideoListException)
    )
    private val runtimeSearchException = RuntimeException("Search Exception")
    private val expectedNegativeSearchVideoUseCaseResponse = VideoResult.Error(
        UseCaseException.SearchLoadException(runtimeSearchException)
    )

    private lateinit var forExpectedPagingDataDiffer: AsyncPagingDataDiffer<YoutubeVideoUiModel>
    private lateinit var forActualPagingDataDiffer: AsyncPagingDataDiffer<YoutubeVideoUiModel>

    @Before
    fun init() {
        forExpectedPagingDataDiffer = TestPagingDataDiffer(dispatcherTestRule.testDispatcher).pagingDiffer
        forActualPagingDataDiffer = TestPagingDataDiffer(dispatcherTestRule.testDispatcher).pagingDiffer
    }

    private fun positiveCase() {
        whenever(videoListUseCase.execute(VideoListUseCase.Request))
            .thenReturn(flowOf(expectedVideoListUseCaseResponse))
        whenever(searchVideoListUseCase.execute(SearchVideoListUseCase.Request(testQuery)))
            .thenReturn(flowOf(expectedSearchVideoUseCaseResponse))
    }

    private fun negativeCase() {
         whenever(videoListUseCase.execute(VideoListUseCase.Request)).thenReturn(
             flowOf(expectedNegativeVideoListUseCaseResponse)
         )
        whenever(searchVideoListUseCase.execute(SearchVideoListUseCase.Request(testQuery)))
            .thenReturn(flowOf(expectedNegativeSearchVideoUseCaseResponse))
    }

    @Test
    fun `fun updateSearchTextState() updates text in SearchAppBar`() {
        positiveCase()

        val testText = "TestText"
        videoListViewModel.updateSearchTextState(testText)
        assertEquals(testText, videoListViewModel.searchTextState.value)
    }

    @Test
    fun `fun updateSearchState() changes the state of the SearchAppBar `() {
        positiveCase()

        assertTrue(videoListViewModel.searchState.value == SearchState.CLOSED)

        videoListViewModel.updateSearchState(SearchState.OPENED)
        assertTrue(videoListViewModel.searchState.value == SearchState.OPENED)

        videoListViewModel.updateSearchState(SearchState.CLOSED)
        assertTrue(videoListViewModel.searchState.value == SearchState.CLOSED)
    }

    @Test
    fun `fetchVideoPagingData() returns popular video if VideoType is PopularVideo`() = runTest {
        positiveCase()

        videoListViewModel.fetchVideoPagingData(VideoType.PopularVideo)
        advanceUntilIdle()

        val actualPagingData = (videoListViewModel.videoStateFlow.first() as UiState.Success).data
        val expectedPagingData = (videoListConverter.convert(expectedVideoListUseCaseResponse) as UiState.Success).data

        val testJob = launch {
            forExpectedPagingDataDiffer.submitData(expectedPagingData)
            forActualPagingDataDiffer.submitData(actualPagingData)
        }

        advanceUntilIdle()
        testJob.cancel()

        assertEquals(forExpectedPagingDataDiffer.snapshot(), forActualPagingDataDiffer.snapshot())
    }

    @Test
    fun `fetchVideoPagingData() returns popular video if VideoType is SearchVideo`() = runTest {
        positiveCase()

        videoListViewModel.fetchVideoPagingData(VideoType.SearchVideo(testQuery))
        advanceUntilIdle()

        val actualPagingData = (videoListViewModel.videoStateFlow.first() as UiState.Success).data
        val expectedPagingData = (searchVideoListConverter.convert(expectedSearchVideoUseCaseResponse) as UiState.Success).data

        val testJob = launch {
            forExpectedPagingDataDiffer.submitData(expectedPagingData)
            forActualPagingDataDiffer.submitData(actualPagingData)
        }

        advanceUntilIdle()
        testJob.cancel()

        assertEquals(forExpectedPagingDataDiffer.snapshot(), forActualPagingDataDiffer.snapshot())
    }

    @Test
    fun `fetchVideoPagingData() returns UiState Error when VideoType is PopularVideo`() = runTest {
        negativeCase()

        videoListViewModel.fetchVideoPagingData(VideoType.PopularVideo)
        advanceUntilIdle()

        val actualErrorResult = videoListViewModel.videoStateFlow.first() as UiState.Error
        val expectedErrorResult = videoListConverter.convert(expectedNegativeVideoListUseCaseResponse) as UiState.Error

        assertEquals(expectedErrorResult, actualErrorResult)
    }

    @Test
    fun `fetchVideoPagingData() returns UiState Error when VideoType is SearchVideo`() = runTest {
        negativeCase()

        videoListViewModel.fetchVideoPagingData(VideoType.SearchVideo(testQuery))
        advanceUntilIdle()

        val actualErrorResult = videoListViewModel.videoStateFlow.first() as UiState.Error
        val expectedErrorResult = searchVideoListConverter.convert(expectedNegativeSearchVideoUseCaseResponse) as UiState.Error

        assertEquals(expectedErrorResult, actualErrorResult)
    }
}