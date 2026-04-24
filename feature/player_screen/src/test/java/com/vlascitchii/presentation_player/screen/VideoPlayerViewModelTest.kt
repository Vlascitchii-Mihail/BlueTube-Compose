package com.vlascitchii.presentation_player.screen

import androidx.paging.PagingData
import com.vlascitchii.common_test.rule.DispatcherTestRule
import com.vlascitchii.domain.model.videos.YoutubeVideoDomain
import com.vlascitchii.domain.usecase.VideoPlayerUseCase
import com.vlascitchii.domain.util.UseCaseException
import com.vlascitchii.domain.util.VideoResult
import com.vlascitchii.presentation_common.model.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_common.network_observer.NetworkConnectivityObserver
import com.vlascitchii.presentation_common.network_observer.NetworkConnectivityStatus
import com.vlascitchii.presentation_common.ui.state_common.UiState
import com.vlascitchii.presentation_player.screen.screen.VideoPlayerViewModel
import com.vlascitchii.presentation_player.screen.state.PlayerActionState
import com.vlascitchii.presentation_player.screen.utils.VideoPlayerConverter
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class VideoPlayerViewModelTest {

    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    @Mock
    lateinit var videoPlayerUseCase: VideoPlayerUseCase

    @Mock
    lateinit var videoPlayerConverter: VideoPlayerConverter

    @Mock
    lateinit var networkConnectivityObserver: NetworkConnectivityObserver
    private lateinit var viewModelPlayer: VideoPlayerViewModel

    @Before
    fun setup() {
        whenever(networkConnectivityObserver.observe()).thenReturn(flowOf(NetworkConnectivityStatus.Available))

        viewModelPlayer = VideoPlayerViewModel(
            videoPlayerUseCase,
            videoPlayerConverter,
            networkConnectivityObserver,
            dispatcherTestRule.testDispatcher
        )
    }

    private val testQuery = "Test query"

    private val pagingData: Flow<PagingData<YoutubeVideoDomain>> =
        flowOf(PagingData.Companion.from(emptyList()))
    private val pagingUiData: Flow<PagingData<YoutubeVideoUiModel>> =
        flowOf(PagingData.Companion.from(emptyList()))

    private val expectedRelatedVideosUseCaseResponse: VideoResult<VideoPlayerUseCase.PlayerResponse> =
        VideoResult.Success(VideoPlayerUseCase.PlayerResponse(pagingData))
    private val positiveConvertResult: UiState<Flow<PagingData<YoutubeVideoUiModel>>> =
        UiState.Success(pagingUiData)

    private fun positiveCase() {
        whenever(videoPlayerUseCase.execute(any<VideoPlayerUseCase.PlayerRequest>()))
            .thenReturn(flowOf(expectedRelatedVideosUseCaseResponse))

        whenever(
            videoPlayerConverter
                .convert(any<VideoResult<VideoPlayerUseCase.PlayerResponse>>())
        )
            .thenReturn(positiveConvertResult)
    }

    private val testErrorMessage = "Test Error Message"
    private val runtimeRelatedVideosException = RuntimeException(testErrorMessage)
    private val expectedNegativeRelatedVideosUseCaseResponse: VideoResult<VideoPlayerUseCase.PlayerResponse> =
        VideoResult.Error(UseCaseException.VideoListLoadException(runtimeRelatedVideosException))
    private val negativeConvertResult: UiState<Flow<PagingData<YoutubeVideoUiModel>>> =
        UiState.Error(testErrorMessage)

    private fun negativeCase() {
        whenever(videoPlayerUseCase.execute(any<VideoPlayerUseCase.PlayerRequest>()))
            .thenReturn(flowOf(expectedNegativeRelatedVideosUseCaseResponse))

        whenever(
            videoPlayerConverter
                .convert(any<VideoResult<VideoPlayerUseCase.PlayerResponse>>())
        )
            .thenReturn(negativeConvertResult)
    }

    @Test
    fun `fun updatePlaybackPosition() current updates viewModel's videoPlaybackPosition`() {
        val initialPlaybackPosition = 0F
        val newPlaybackPosition = 5F

        Assert.assertEquals(initialPlaybackPosition, viewModelPlayer.videoPlaybackPosition)
        viewModelPlayer.updatePlaybackPosition(newPlaybackPosition)
        Assert.assertEquals(newPlaybackPosition, viewModelPlayer.videoPlaybackPosition)
    }

    @Test
    fun `fun getSearchedRelatedVideos() gets UiState Success and assigns it to the ViewModel state`() =
        runTest {
            positiveCase()

            viewModelPlayer.getSearchedRelatedVideos(testQuery)
            advanceUntilIdle()

            val actualResult: UiState<Flow<PagingData<YoutubeVideoUiModel>>> =
                viewModelPlayer.playerStateFlow.first().relatedVideoState

            verify(videoPlayerUseCase).execute(any<VideoPlayerUseCase.PlayerRequest>())
            verify(videoPlayerConverter)
                .convert(any<VideoResult<VideoPlayerUseCase.PlayerResponse>>())
            Assert.assertEquals(positiveConvertResult, actualResult)
        }

    @Test
    fun `fun getSearchedRelatedVideos() gets UiState Error and assigns it to the ViewModel state`() =
        runTest {
            negativeCase()

            viewModelPlayer.getSearchedRelatedVideos(testQuery)
            advanceUntilIdle()

            val actualResult: UiState<Flow<PagingData<YoutubeVideoUiModel>>> =
                viewModelPlayer.playerStateFlow.first().relatedVideoState

            verify(videoPlayerUseCase).execute(any<VideoPlayerUseCase.PlayerRequest>())
            verify(videoPlayerConverter)
                .convert(any<VideoResult<VideoPlayerUseCase.PlayerResponse>>())
            Assert.assertEquals(negativeConvertResult, actualResult)
        }

    @Test
    fun `fun updateVideoPlayState updates video play state`() = runTest {
        Assert.assertTrue(viewModelPlayer.playerStateFlow.first().isVideoPlaying)

        viewModelPlayer.updateVideoPlayState(isPlaying = false)
        advanceUntilIdle()

        Assert.assertFalse(viewModelPlayer.playerStateFlow.first().isVideoPlaying)
    }

    @Test
    fun `fun updatePlayerOrientationState() updates player orientation state`() = runTest {
        Assert.assertEquals(
            OrientationState.PORTRAIT,
            viewModelPlayer.playerStateFlow.first().playerOrientationState
        )

        viewModelPlayer.updatePlayerOrientationState(OrientationState.FULL_SCREEN)
        advanceUntilIdle()

        Assert.assertEquals(
            OrientationState.FULL_SCREEN,
            viewModelPlayer.playerStateFlow.first().playerOrientationState
        )
    }

    @Test
    fun `setInitialScreenLaunchRotationController updates _initialScreenLaunchRotationController field`() =
        runTest {
            Assert.assertFalse(viewModelPlayer.playerStateFlow.first().isOrientationApproved)

            viewModelPlayer.setInitialScreenLaunchRotationController(true)
            advanceUntilIdle()

            Assert.assertTrue(viewModelPlayer.playerStateFlow.first().isOrientationApproved)
        }

    @Test
    fun `submitAction(GetRelatedVideosAction) calls handleAction then getSearchedRelatedVideos()`() =
        runTest {
            positiveCase()

            val testTitle = "TestTitle"
            val testAction = PlayerActionState.GetRelatedVideosAction(testTitle)

            viewModelPlayer.submitAction(testAction)
            advanceUntilIdle()

            verify(videoPlayerUseCase).execute(VideoPlayerUseCase.PlayerRequest(testTitle))
        }

    @Test
    fun `submitAction(UpdatePlayStateAction) calls handleAction then updateVideoPlayState()`() =
        runTest {
            val expectedTestState = false
            val testAction = PlayerActionState.UpdatePlayStateAction(expectedTestState)

            viewModelPlayer.submitAction(testAction)
            advanceUntilIdle()

            assertEquals(expectedTestState, viewModelPlayer.playerStateFlow.value.isVideoPlaying)
        }

    @Test
    fun `submitAction(UpdatePlaybackPositionAction) calls handleAction then updatePlaybackPosition()`() =
        runTest {
            val testPlaybackPosition: Float = 2.5F
            val testAction = PlayerActionState.UpdatePlaybackPositionAction(testPlaybackPosition)

            viewModelPlayer.submitAction(testAction)
            advanceUntilIdle()

            assertEquals(testPlaybackPosition, viewModelPlayer.videoPlaybackPosition)
        }

    @Test
    fun `submitAction(UpdatePlayerOrientationStateAction) calls handleAction then updatePlayerOrientationState()`() = runTest {
        val testOrientationState = OrientationState.FULL_SCREEN
        val testAction = PlayerActionState.UpdatePlayerOrientationStateAction(testOrientationState)

        viewModelPlayer.submitAction(testAction)
        advanceUntilIdle()

        assertEquals(testOrientationState, viewModelPlayer.playerStateFlow.value.playerOrientationState)
    }

    @Test
    fun `submitAction(ApproveOrientationChange) calls handleAction then setInitialScreenLaunchRotationController()`() = runTest {
        val testApproval = false
        val testAction = PlayerActionState.ApproveOrientationChange(testApproval)

        viewModelPlayer.submitAction(testAction)
        advanceUntilIdle()

        assertEquals(testApproval, viewModelPlayer.playerStateFlow.value.isOrientationApproved)
    }
}
