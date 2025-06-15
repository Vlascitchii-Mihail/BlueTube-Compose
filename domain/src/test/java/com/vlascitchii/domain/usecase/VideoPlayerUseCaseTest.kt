package com.vlascitchii.domain.usecase

import androidx.paging.PagingData
import com.vlascitchii.data_remote.rule.DispatcherTestRule
import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideoResponse.Companion.RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG
import com.vlascitchii.domain.repository.PlayerRepository
import com.vlascitchii.domain.usecase.util.Configuration
import com.vlascitchii.domain.util.UseCaseException
import com.vlascitchii.domain.util.VideoResult
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class VideoPlayerUseCaseTest {

    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    private val configuration: Configuration = mock()
    private val playerRepository: PlayerRepository = mock()
    private val videoPlayerUseCase = VideoPlayerUseCase(configuration, playerRepository)

    private val repositoryExpectedResultFlow = PagingData.from((RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG.items))
    private val negativeRepositoryExpectedResultFlow = UseCaseException.SearchLoadException(RuntimeException())

    private val positiveExpectedResult = flowOf(VideoResult.Success(VideoPlayerUseCase.Response(repositoryExpectedResultFlow)))

    @Before
    fun init() {
        whenever(configuration.dispatcher).thenReturn(dispatcherTestRule.testDispatcher)
    }

    @Test
    fun `fun execute() returns Success SearchRelatedVideosResult with pager inside`() = runTest {
        whenever(playerRepository.getSearchRelayedVideos("Test query")).thenReturn(flowOf(repositoryExpectedResultFlow))

        val actualResult = videoPlayerUseCase.execute(VideoPlayerUseCase.Request("Test query")).first()

        assertEquals(positiveExpectedResult.first(), actualResult)
    }

    @Test
    fun `fun execute() returns SearchRelatedVideosResult Error`() = runTest {
        whenever(playerRepository.getSearchRelayedVideos("Test query")).thenReturn(flow { throw negativeRepositoryExpectedResultFlow })

        val actualValue = videoPlayerUseCase.execute(VideoPlayerUseCase.Request("Test query")).first()

        assertTrue((actualValue as VideoResult.Error).exception is UseCaseException.SearchLoadException)
    }
}