package com.vlascitchii.presentation_shorts.screen_shorts.screen

import androidx.paging.PagingData
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.vlascitchii.common_test.rule.DispatcherTestRule
import com.vlascitchii.domain.model.videos.YoutubeVideoDomain
import com.vlascitchii.domain.usecase.ShortsUseCase
import com.vlascitchii.domain.util.UseCaseException
import com.vlascitchii.domain.util.VideoResult
import com.vlascitchii.presentation_common.model.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_common.network_observer.NetworkConnectivityObserver
import com.vlascitchii.presentation_common.network_observer.NetworkConnectivityStatus
import com.vlascitchii.presentation_common.ui.state_common.UiState
import com.vlascitchii.presentation_shorts.screen_shorts.state.ShortsAction
import com.vlascitchii.presentation_shorts.screen_shorts.utils.ShortsConverter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class ShortsViewModelTest {

    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    private val shortsUseCase: ShortsUseCase = mock()
    private val shortsConverter: ShortsConverter = mock()
    private val networkConnectivityObserver: NetworkConnectivityObserver = mock()
    private lateinit var shortsViewModel: ShortsViewModel

    private val testErrorMessage = "Test Error Message"

    private val pagingData: Flow<PagingData<YoutubeVideoDomain>> = flowOf(PagingData.from(emptyList()))
    private val pagingUiData: Flow<PagingData<YoutubeVideoUiModel>> = flowOf(PagingData.from(emptyList()))

    private val expectedShortsUseCaseResponse: VideoResult<ShortsUseCase.ShortsResponse> =
        VideoResult.Success(ShortsUseCase.ShortsResponse(pagingData))
    private val positiveConvertResult: UiState<Flow<PagingData<YoutubeVideoUiModel>>> =
        UiState.Success(pagingUiData)

    private fun positiveCase() {
        whenever(shortsUseCase.execute(ShortsUseCase.ShortsRequest))
            .thenReturn(flowOf(expectedShortsUseCaseResponse))
        whenever(shortsConverter.convert(expectedShortsUseCaseResponse))
            .thenReturn(positiveConvertResult)
    }


    private val runtimeShortsException = RuntimeException("VideoList Exception")
    private val expectedNegativeShortsUseCaseResponse: VideoResult<ShortsUseCase.ShortsResponse> = VideoResult
        .Error(UseCaseException.ShortsLoadException(runtimeShortsException))
    private val negativeConvertResultShorts: UiState<Flow<PagingData<YoutubeVideoUiModel>>> =
        UiState.Error(testErrorMessage)

    private fun negativeCase() {
        whenever(shortsUseCase.execute(ShortsUseCase.ShortsRequest))
            .thenReturn(flowOf(expectedNegativeShortsUseCaseResponse))
        whenever(shortsConverter.convert(expectedNegativeShortsUseCaseResponse))
            .thenReturn(negativeConvertResultShorts)
    }

    @Before
    fun setup() {
        whenever(networkConnectivityObserver.observe())
            .thenReturn(flowOf(NetworkConnectivityStatus.Available))

        shortsViewModel = ShortsViewModel(
            shortsUseCase,
            shortsConverter,
            networkConnectivityObserver,
            dispatcherTestRule.testDispatcher
        )
    }

    @Test
    fun `fun fetchShorts() gets UiState Success and assigns it to the ViewModel state`() = runTest {
        positiveCase()

        shortsViewModel.fetchShorts()
        advanceUntilIdle()

        val actualResult = shortsViewModel.shortsUiStateFlow.first().shortsState

        verify(shortsUseCase).execute(ShortsUseCase.ShortsRequest)
        verify(shortsConverter).convert(expectedShortsUseCaseResponse)
        assertEquals(positiveConvertResult, actualResult)
    }

    @Test
    fun `fun fetchShorts() gets UiState Error and assigns it to the ViewModel state`() = runTest {
        negativeCase()

        shortsViewModel.fetchShorts()
        advanceUntilIdle()

        val actualErrorResult = shortsViewModel.shortsUiStateFlow.first().shortsState

        verify(shortsUseCase).execute(ShortsUseCase.ShortsRequest)
        verify(shortsConverter).convert(expectedNegativeShortsUseCaseResponse)
        assertEquals(negativeConvertResultShorts, actualErrorResult)
    }

    @Test
    fun `fun listenToVideoQueue() caches the last 3 videoPlayers`() = runTest {
        val replyCacheExpectedSize = 3
        val youTubePLayer: YouTubePlayer = mock()
        shortsViewModel.listenToVideoQueue()
        advanceUntilIdle()

        shortsViewModel.videoQueue.emit(youTubePLayer)
        shortsViewModel.videoQueue.emit(youTubePLayer)
        shortsViewModel.videoQueue.emit(youTubePLayer)
        shortsViewModel.videoQueue.emit(youTubePLayer)

        assertEquals(replyCacheExpectedSize, shortsViewModel.videoQueue.replayCache.size)
    }

    @Test
    fun `fun listenToVideoQueue() plays first video`() = runTest {
        val youTubePLayer0: YouTubePlayer = mock()

        shortsViewModel.listenToVideoQueue()
        advanceUntilIdle()

        shortsViewModel.videoQueue.emit(youTubePLayer0)

        advanceUntilIdle()
        verify(youTubePLayer0).play()
    }

    @Test
    fun `fun listenToVideoQueue() `() = runTest {
        val youTubePLayer0: YouTubePlayer = mock()
        val youTubePLayer1: YouTubePlayer = mock()
        val youTubePLayer2: YouTubePlayer = mock()

        shortsViewModel.listenToVideoQueue()
        advanceUntilIdle()

        shortsViewModel.videoQueue.emit(youTubePLayer0)
        shortsViewModel.videoQueue.emit(youTubePLayer1)
        shortsViewModel.videoQueue.emit(youTubePLayer2)

        advanceUntilIdle()
        verify(youTubePLayer1).play()
    }

    @Test
    fun`submitAction(FetchShortsAction) calls ShortsViewModel handleAction, then fetchShorts()`() = runTest {
        positiveCase()

        val action = ShortsAction.FetchShortsAction

        shortsViewModel.submitAction(action)
        advanceUntilIdle()

        verify(shortsUseCase).execute(ShortsUseCase.ShortsRequest)
    }
}
