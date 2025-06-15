package com.vlascitchii.domain.usecase

import androidx.paging.PagingData
import com.vlascitchii.data_remote.rule.DispatcherTestRule
import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideoResponse.Companion.RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG
import com.vlascitchii.domain.repository.ShortsRepository
import com.vlascitchii.domain.usecase.util.Configuration
import com.vlascitchii.domain.util.UseCaseException
import com.vlascitchii.domain.util.VideoResult
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class ShortsUseCaseTest {

    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    private val configuration: Configuration = mock()
    private val shortsRepository: ShortsRepository = mock()
    private val searchVideoListUseCase = ShortsUseCase(configuration, shortsRepository)

    private val repositoryExpectedResultFlow = PagingData.from((RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG.items))
    private val negativeRepositoryExpectedResultFlow = UseCaseException.ShortsLoadException(RuntimeException())

    private val positiveExpectedResult = flowOf(VideoResult.Success(ShortsUseCase.Response(repositoryExpectedResultFlow)))

    @Before
    fun init() {
        whenever(configuration.dispatcher).thenReturn(dispatcherTestRule.testDispatcher)
    }

    @Test
    fun `fun execute() returns Success SearchVideoResult with pager inside`() = runTest {
        whenever(shortsRepository.getShorts()).thenReturn(flowOf(repositoryExpectedResultFlow))

        val actualResult = searchVideoListUseCase.execute(ShortsUseCase.Request).first()

        assertEquals(positiveExpectedResult.first(), actualResult)
    }

    @Test
    fun `fun execute() returns SearchVideoResult Error`() = runTest {
        whenever(shortsRepository.getShorts())
            .thenReturn(flow { throw negativeRepositoryExpectedResultFlow })

        val actualResult = searchVideoListUseCase.execute(ShortsUseCase.Request).first()

        assertTrue((actualResult as VideoResult.Error).exception is UseCaseException.ShortsLoadException)
    }
}