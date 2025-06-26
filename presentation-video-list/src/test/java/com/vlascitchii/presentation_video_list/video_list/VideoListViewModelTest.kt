package com.vlascitchii.presentation_video_list.video_list

import androidx.paging.PagingData
import com.vlascitchii.common_test.rule.DispatcherTestRule
import com.vlascitchii.domain.custom_coroutine_scopes.AppCoroutineScope
import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideo
import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideoResponse.Companion.RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG
import com.vlascitchii.domain.usecase.SearchVideoListUseCase
import com.vlascitchii.domain.usecase.VideoListUseCase
import com.vlascitchii.domain.util.UseCaseException
import com.vlascitchii.domain.util.VideoResult
import com.vlascitchii.presentation_common.entity.util.convertToYoutubeVideoUiMode
import com.vlascitchii.presentation_common.entity.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_common.network_observer.NetworkConnectivityObserver
import com.vlascitchii.presentation_common.ui.state.UiState
import com.vlascitchii.presentation_video_list.screen.VideoListViewModel
import com.vlascitchii.presentation_video_list.util.SearchVideoListConverter
import com.vlascitchii.presentation_video_list.util.VideoListConverter
import com.vlascitchii.presentation_video_list.util.state.SearchState
import com.vlascitchii.presentation_video_list.util.state.VideoType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

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

    private val testQuery = "TestQuery"
    private val testErrorMessage = "Test Error Message"

    private val pagingData: PagingData<YoutubeVideo> = PagingData.from(RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG.items)
    private val pagingUiData: PagingData<YoutubeVideoUiModel> = PagingData.from(
        RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG.items.map { video: YoutubeVideo ->
            video.convertToYoutubeVideoUiMode()
        }
    )

    private val expectedVideoListUseCaseResponse: VideoResult<VideoListUseCase.Response> =
        VideoResult.Success(VideoListUseCase.Response(pagingData))
    private val expectedSearchVideoUseCaseResponse: VideoResult<SearchVideoListUseCase.Response> =
        VideoResult.Success(SearchVideoListUseCase.Response(pagingData))
    private val positiveConvertResult: UiState<PagingData<YoutubeVideoUiModel>> =
        UiState.Success(pagingUiData)
    private fun positiveCase() {
        whenever(videoListUseCase.execute(VideoListUseCase.Request))
            .thenReturn(flowOf(expectedVideoListUseCaseResponse))
        whenever(searchVideoListUseCase.execute(SearchVideoListUseCase.Request(testQuery)))
            .thenReturn(flowOf(expectedSearchVideoUseCaseResponse))

        whenever(videoListConverter.convert(expectedVideoListUseCaseResponse))
            .thenReturn(positiveConvertResult)
        whenever(searchVideoListConverter.convert(expectedSearchVideoUseCaseResponse))
            .thenReturn(positiveConvertResult)
    }



    private val runtimeVideoListException = RuntimeException("VideoList Exception")
    private val expectedNegativeVideoListUseCaseResponse: VideoResult<VideoListUseCase.Response> = VideoResult
        .Error(UseCaseException.VideoListLoadException(runtimeVideoListException))
    private val runtimeSearchException = RuntimeException("Search Exception")
    private val expectedNegativeSearchVideoUseCaseResponse: VideoResult<SearchVideoListUseCase.Response> = VideoResult
        .Error(UseCaseException.SearchLoadException(runtimeSearchException))
    private val negativeConvertResult: UiState<PagingData<YoutubeVideoUiModel>> =
        UiState.Error(testErrorMessage)

    private fun negativeCase() {
        whenever(videoListUseCase.execute(VideoListUseCase.Request))
             .thenReturn(flowOf(expectedNegativeVideoListUseCaseResponse))
        whenever(searchVideoListUseCase.execute(SearchVideoListUseCase.Request(testQuery)))
            .thenReturn(flowOf(expectedNegativeSearchVideoUseCaseResponse))

        whenever(videoListConverter.convert(expectedNegativeVideoListUseCaseResponse))
            .thenReturn(negativeConvertResult)
        whenever(searchVideoListConverter.convert(expectedNegativeSearchVideoUseCaseResponse))
            .thenReturn(negativeConvertResult)
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
    fun `fetchVideoPagingData() gets UiState Error when VideoType is PopularVideo and assigns it to the ViewModel state`() = runTest {
        negativeCase()

        videoListViewModel.fetchVideoPagingData(VideoType.PopularVideo)
        advanceUntilIdle()

        val actualErrorResult = videoListViewModel.videoStateFlow.first() as UiState.Error

        verify(videoListUseCase).execute(VideoListUseCase.Request)
        verify(videoListConverter).convert(expectedNegativeVideoListUseCaseResponse)
        assertEquals(negativeConvertResult, actualErrorResult)
    }

    @Test
    fun `fetchVideoPagingData() gets UiState Error when VideoType is SearchVideo and assigns it to the ViewModel state`() = runTest {
        negativeCase()

        videoListViewModel.fetchVideoPagingData(VideoType.SearchVideo(testQuery))
        advanceUntilIdle()

        val actualErrorResult = videoListViewModel.videoStateFlow.first() as UiState.Error

        verify(searchVideoListUseCase).execute(SearchVideoListUseCase.Request(testQuery))
        verify(searchVideoListConverter).convert(expectedNegativeSearchVideoUseCaseResponse)
        assertEquals(negativeConvertResult, actualErrorResult)
    }

    @Test
    fun `fetchVideoPagingData() gets UiState Success when VideoType is PopularVideo and assigns it to the ViewModel state`() = runTest {
        positiveCase()

        videoListViewModel.fetchVideoPagingData(VideoType.PopularVideo)
        advanceUntilIdle()

        val actualResult = videoListViewModel.videoStateFlow.first()

        verify(videoListUseCase).execute(VideoListUseCase.Request)
        verify(videoListConverter).convert(expectedVideoListUseCaseResponse)
        assertEquals(positiveConvertResult, actualResult)
    }

    @Test
    fun `fetchVideoPagingData() gets UiState Success when VideoType is SearchVideo and assigns it to the ViewModel state`() = runTest {
        positiveCase()

        videoListViewModel.fetchVideoPagingData(VideoType.SearchVideo(testQuery))
        advanceUntilIdle()

        val actualResult = videoListViewModel.videoStateFlow.first()

        verify(searchVideoListUseCase).execute(SearchVideoListUseCase.Request(testQuery))
        verify(searchVideoListConverter).convert(expectedSearchVideoUseCaseResponse)
        assertEquals(positiveConvertResult, actualResult)
    }
}