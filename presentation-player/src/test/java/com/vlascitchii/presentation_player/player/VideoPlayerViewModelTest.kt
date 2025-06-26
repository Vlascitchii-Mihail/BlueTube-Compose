package com.vlascitchii.presentation_player.player

import androidx.paging.PagingData
import com.vlascitchii.common_test.rule.DispatcherTestRule
import com.vlascitchii.domain.custom_coroutine_scopes.AppCoroutineScope
import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideo
import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideoResponse.Companion.RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG
import com.vlascitchii.domain.usecase.VideoPlayerUseCase
import com.vlascitchii.domain.util.UseCaseException
import com.vlascitchii.domain.util.VideoResult
import com.vlascitchii.presentation_common.entity.util.convertToYoutubeVideoUiMode
import com.vlascitchii.presentation_common.entity.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_common.network_observer.NetworkConnectivityObserver
import com.vlascitchii.presentation_common.ui.state.UiState
import com.vlascitchii.presentation_player.screen_player.OrientationState
import com.vlascitchii.presentation_player.screen_player.screen.VideoPlayerViewModel
import com.vlascitchii.presentation_player.screen_player.utils.VideoPlayerConverter
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever


private const val INITIAL_VIDEO_PLAYBACK_POSITION = 0F
private const val NEW_VIDEO_PLAYBACK_POSITION = 5F

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class VideoPlayerViewModelTest {

    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    private val videoPlayerUseCase: VideoPlayerUseCase = mock()
    private val videoPlayerConverter: VideoPlayerConverter = mock()
    private val networkConnectivityObserver: NetworkConnectivityObserver = mock()
    private val videoCoroutineScope: AppCoroutineScope = mock()
    private val relatedVideosViewModel = VideoPlayerViewModel(
        videoPlayerUseCase,
        videoPlayerConverter,
        networkConnectivityObserver,
        videoCoroutineScope
    )

    private val testQuery = "Test query"
    private val testErrorMessage = "Test Error Message"
    private val orientationStatePortrait = OrientationState.PORTRAIT
    private val orientationStateLandscape = OrientationState.FULL_SCREEN

    private val pagingData: PagingData<YoutubeVideo> = PagingData.from(RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG.items)
    private val pagingUiData: PagingData<YoutubeVideoUiModel> = PagingData.from(
        RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG.items.map { video: YoutubeVideo ->
            video.convertToYoutubeVideoUiMode()
        }
    )

    private val expectedRelatedVideosUseCaseResponse: VideoResult<VideoPlayerUseCase.Response> =
        VideoResult.Success(VideoPlayerUseCase.Response(pagingData))
    private val positiveConvertResult: UiState<PagingData<YoutubeVideoUiModel>> =
        UiState.Success(pagingUiData)

    private fun positiveCase() {
        whenever(videoPlayerUseCase.execute(VideoPlayerUseCase.Request(testQuery)))
            .thenReturn(flowOf(expectedRelatedVideosUseCaseResponse))
        whenever(videoPlayerConverter.convert(expectedRelatedVideosUseCaseResponse))
            .thenReturn(positiveConvertResult)
    }


    private val runtimeRelatedVideosException = RuntimeException("VideoList Exception")
    private val expectedNegativeRelatedVideosUseCaseResponse: VideoResult<VideoPlayerUseCase.Response> = VideoResult
        .Error(UseCaseException.VideoListLoadException(runtimeRelatedVideosException))
    private val negativeConvertResultPlayer: UiState<PagingData<YoutubeVideoUiModel>> =
        UiState.Error(testErrorMessage)

    private fun negativeCase() {
        whenever(videoPlayerUseCase.execute(VideoPlayerUseCase.Request(testQuery)))
            .thenReturn(flowOf(expectedNegativeRelatedVideosUseCaseResponse))
        whenever(videoPlayerConverter.convert(expectedNegativeRelatedVideosUseCaseResponse))
            .thenReturn(negativeConvertResultPlayer)
    }

    @Test
    fun `fun updatePlaybackPosition() current updates viewModel's videoPlaybackPosition`() {
        assertEquals(INITIAL_VIDEO_PLAYBACK_POSITION, relatedVideosViewModel.videoPlaybackPosition)
        relatedVideosViewModel.updatePlaybackPosition(NEW_VIDEO_PLAYBACK_POSITION)
        assertEquals(NEW_VIDEO_PLAYBACK_POSITION, relatedVideosViewModel.videoPlaybackPosition)
    }

    @Test
    fun `fun getSearchedRelatedVideos() gets UiState Success and assigns it to the ViewModel state`() = runTest {
        positiveCase()

        relatedVideosViewModel.getSearchedRelatedVideos(testQuery)
        advanceUntilIdle()

        val actualResult = relatedVideosViewModel.relatedVideoStateFlow.first()

        verify(videoPlayerUseCase).execute(VideoPlayerUseCase.Request(testQuery))
        verify(videoPlayerConverter).convert(expectedRelatedVideosUseCaseResponse)
        assertEquals(positiveConvertResult, actualResult)
    }

    @Test
    fun `fun updatePlaybackPosition() gets UiState Error and assigns it to the ViewModel state`() = runTest {
        negativeCase()

        relatedVideosViewModel.getSearchedRelatedVideos(testQuery)
        advanceUntilIdle()

        val actualResult = relatedVideosViewModel.relatedVideoStateFlow.first()

        verify(videoPlayerUseCase).execute(VideoPlayerUseCase.Request(testQuery))
        verify(videoPlayerConverter).convert(expectedNegativeRelatedVideosUseCaseResponse)
        assertEquals(negativeConvertResultPlayer, actualResult)
    }

    @Test
    fun `fun updateVideoPlayState updates video play state`() = runTest {
        assertTrue(relatedVideosViewModel.isVideoPlaysFlow.first())

        val falseVideoPlayStet = false
        relatedVideosViewModel.updateVideoPlayState(falseVideoPlayStet)

        assertFalse(relatedVideosViewModel.isVideoPlaysFlow.first())
    }

    @Test
    fun `fun updatePlayerOrientationState() updates player orientation state`() = runTest {
        assertEquals(orientationStatePortrait, relatedVideosViewModel.playerOrientationState.first())

        relatedVideosViewModel.updatePlayerOrientationState(orientationStateLandscape)

        assertEquals(orientationStateLandscape, relatedVideosViewModel.playerOrientationState.first())
    }

    @Test
    fun `setFullscreenWidgetIsClicked() gets full screen widget state`() = runTest {
        assertFalse(relatedVideosViewModel.fullscreenWidgetIsClicked.first())

        val trueClickedFullscreenState = true
        relatedVideosViewModel.setFullscreenWidgetIsClicked(trueClickedFullscreenState)

        assertTrue(relatedVideosViewModel.fullscreenWidgetIsClicked.first())
    }
}
