package com.vlascitchii.domain.usecase

import androidx.paging.PagingData
import com.vlascitchii.data_remote.rule.DispatcherTestRule
import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideoResponse.Companion.RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG
import com.vlascitchii.domain.repository.VideoListRepository
import com.vlascitchii.domain.usecase.util.Configuration
import com.vlascitchii.domain.util.UseCaseException.SearchLoadException
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

class SearchVideoListUseCaseTest {

    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    private val configuration: Configuration = mock()
    private val videoListRepository: VideoListRepository = mock()
    private val searchVideoListUseCase = SearchVideoListUseCase(configuration, videoListRepository)

    private val repositoryExpectedResultFlow = PagingData.from((RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG.items))
    private val negativeRepositoryExpectedResultFlow = SearchLoadException(RuntimeException())

    private val positiveExpectedResult = flowOf(VideoResult.Success(SearchVideoListUseCase.Response(repositoryExpectedResultFlow)))

    @Before
    fun init() {
        whenever(configuration.dispatcher).thenReturn(dispatcherTestRule.testDispatcher)
    }

    @Test
    fun `fun execute() returns Success SearchVideoResult with pager inside`() = runTest {
        whenever(videoListRepository.getSearchVideos("Test query")).thenReturn(flowOf(repositoryExpectedResultFlow))

        val actualResult = searchVideoListUseCase.execute(SearchVideoListUseCase.Request("Test query")).first()

        assertEquals(positiveExpectedResult.first(), actualResult)
    }

    @Test
    fun `fun execute() returns SearchVideoResult Error`() = runTest {
        whenever(videoListRepository.getSearchVideos("Test query"))
            .thenReturn(flow { throw negativeRepositoryExpectedResultFlow })

        val actualResult = searchVideoListUseCase.execute(SearchVideoListUseCase.Request("Test query")).first()

        assertTrue((actualResult as VideoResult.Error).exception is SearchLoadException)
    }

}