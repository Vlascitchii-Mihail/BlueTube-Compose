package com.vlascitchii.presentation_video_list.video_list

import androidx.paging.PagingData
import com.vlascitchii.common_test.rule.DispatcherTestRule
import com.vlascitchii.domain.custom_scope.CustomCoroutineScope
import com.vlascitchii.domain.model.videos.YoutubeVideoDomain
import com.vlascitchii.domain.usecase.VideoListUseCase
import com.vlascitchii.domain.util.UseCaseException
import com.vlascitchii.domain.util.VideoResult
import com.vlascitchii.presentation_common.model.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_common.network_observer.NetworkConnectivityObserver
import com.vlascitchii.presentation_common.ui.state.UiState
import com.vlascitchii.presentation_video_list.screen.VideoListViewModel
import com.vlascitchii.presentation_video_list.screen.state.SearchState
import com.vlascitchii.presentation_video_list.screen.state.UiVideoListAction
import com.vlascitchii.presentation_video_list.util.VideoListConverter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
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
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class VideoListViewModelTest {

    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    private val customCoroutineScope: CustomCoroutineScope = CustomCoroutineScope(dispatcherTestRule.testDispatcher)

    private val videoListUseCase: VideoListUseCase = mock()
    private val videoListConverter: VideoListConverter = mock()
    private val networkConnectivityObserver: NetworkConnectivityObserver = mock()
    private val videoListViewModel = VideoListViewModel(
        videoListUseCase,
        videoListConverter,
        networkConnectivityObserver,
        customCoroutineScope
    )

    private val testQuery = "TestQuery"
    private val testErrorMessage = "Test Error Message"

    private val pagingData: Flow<PagingData<YoutubeVideoDomain>> = flowOf(PagingData.from(emptyList()))
    private val pagingUiData: Flow<PagingData<YoutubeVideoUiModel>> = flowOf(PagingData.from(emptyList()))

    private val expectedVideoListUseCaseResponse: VideoResult<VideoListUseCase.VideoListResponse> =
        VideoResult.Success(VideoListUseCase.VideoListResponse(pagingData))

    private val positiveConvertResult: UiState<Flow<PagingData<YoutubeVideoUiModel>>> =
        UiState.Success(pagingUiData)

    private fun positiveCase() {
        whenever(videoListUseCase.execute(any()))
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
        whenever(videoListUseCase.execute(any()))
             .thenReturn(flowOf(expectedNegativeVideoListUseCaseResponse))

        whenever(videoListConverter.convert(expectedNegativeVideoListUseCaseResponse))
            .thenReturn(negativeConvertResult)
    }

    @Test
    fun `fun updateSearchTextState() updates text in SearchAppBar`() {
        positiveCase()

        videoListViewModel.updateSearchTextState(testQuery)
        assertEquals(testQuery, videoListViewModel.searchTextState.value.text)
    }

    @Test
    fun `fun updateSearchState() changes the state of the SearchAppBar `() {
        positiveCase()

        assertTrue(videoListViewModel.searchBarState.value == SearchState.CLOSED)

        videoListViewModel.updateSearchState(SearchState.OPENED)
        assertTrue(videoListViewModel.searchBarState.value == SearchState.OPENED)

        videoListViewModel.updateSearchState(SearchState.CLOSED)
        assertTrue(videoListViewModel.searchBarState.value == SearchState.CLOSED)
    }

    @Test
    fun `fetchVideoPagingData() gets UiState Error and assigns it to the ViewModel state`() = runTest {
        negativeCase()

        videoListViewModel.submitAction(UiVideoListAction.GetVideo(""))
        advanceUntilIdle()
        val actualErrorResult = videoListViewModel.uiStateFlow.first()

        verify(videoListConverter).convert(expectedNegativeVideoListUseCaseResponse)
        assertEquals(negativeConvertResult, actualErrorResult)
    }

    @Test
    fun `fetchVideoPagingData() gets UiState Error when VideoType is SearchVideo and assigns it to the ViewModel state`() = runTest {
        negativeCase()

        videoListViewModel.submitAction(UiVideoListAction.GetVideo(testQuery))
        advanceUntilIdle()

        val actualErrorResult = videoListViewModel.uiStateFlow.first()

        verify(videoListConverter).convert(expectedNegativeVideoListUseCaseResponse)
        assertEquals(negativeConvertResult, actualErrorResult)
    }

    @Test
    fun `fetchVideoPagingData() gets UiState Success and assigns it to the ViewModel state`() = runTest {
        positiveCase()

        videoListViewModel.submitAction(UiVideoListAction.GetVideo(""))
        advanceUntilIdle()

        val actualResult = videoListViewModel.uiStateFlow.first()

        verify(videoListConverter).convert(expectedVideoListUseCaseResponse)
        assertEquals(positiveConvertResult, actualResult)
    }

    @Test
    fun `fetchVideoPagingData() search gets UiState Success and assigns it to the ViewModel state`() = runTest {
        positiveCase()

        videoListViewModel.submitAction(UiVideoListAction.GetVideo(testQuery))
        advanceUntilIdle()

        val actualResult = videoListViewModel.uiStateFlow.first()

        verify(videoListConverter).convert(expectedVideoListUseCaseResponse)
        assertEquals(positiveConvertResult, actualResult)
    }
}
