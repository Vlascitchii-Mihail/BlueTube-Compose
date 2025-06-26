package com.vlascitchii.domain.usecase

import androidx.paging.PagingData
import com.vlascitchii.common_test.rule.DispatcherTestRule
import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideoResponse.Companion.RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG
import com.vlascitchii.domain.repository.VideoListRepository
import com.vlascitchii.domain.usecase.util.Configuration
import com.vlascitchii.domain.util.UseCaseException.VideoListLoadException
import com.vlascitchii.domain.util.VideoResult
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class VideoListUseCaseTest {

    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    private val configuration: Configuration = mock()
    private val videoListRepository: VideoListRepository = mock()
    private val videoListUseCase: VideoListUseCase = VideoListUseCase(configuration, videoListRepository)

    private val repositoryExpectedResultFlow = PagingData.from((RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG.items))
    private val negativeRepositoryExpectedResultFlow = VideoListLoadException(RuntimeException())

    private val positiveExpectedResult = flowOf(VideoResult.Success(VideoListUseCase.Response(repositoryExpectedResultFlow)))

    @Before
    fun init() {
        whenever(configuration.dispatcher).thenReturn(dispatcherTestRule.testDispatcher)
    }

    @Test
    fun `fun execute() returns Success PopularVideoResult with pager inside`() = runTest {
        whenever(videoListRepository.getPopularVideos()).thenReturn(flowOf(repositoryExpectedResultFlow))

        val actualResult = videoListUseCase.execute(VideoListUseCase.Request)

        assertEquals(positiveExpectedResult.first(), actualResult.first())
    }

    @Test
    fun `fun execute() returns PopularVideoResult Error`() = runTest {
        whenever(videoListRepository.getPopularVideos())
            .thenReturn(flow { throw negativeRepositoryExpectedResultFlow })

        val actualResult = videoListUseCase.execute(VideoListUseCase.Request).first()

        assertTrue((actualResult as VideoResult.Error).exception is VideoListLoadException)
    }
}