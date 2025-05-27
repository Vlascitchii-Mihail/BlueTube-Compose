package com.vlascitchii.domain.paging

import android.net.http.HttpException
import androidx.paging.PagingSource
import com.vlascitchii.data_remote.rule.DispatcherTestRule
import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideo
import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideoResponse
import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideoResponse.Companion.RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG
import com.vlascitchii.domain.repository.VideoListRepository
import com.vlascitchii.domain.usecase.VideoListUseCase
import com.vlascitchii.domain.util.UseCaseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class CommonPagerTest {

    @get:Rule
    var dispatcherTestRule = DispatcherTestRule()

    private val initialPageToken = ""
    private val request: VideoListUseCase.Request = VideoListUseCase.Request(initialPageToken)
    private val repository: VideoListRepository = mock()
    private val executeUseCase: (String) -> Flow<YoutubeVideoResponse> = { pageToken: String ->
        request.pageToken = pageToken
        repository.getVideos(request.pageToken)
    }
    private val commonPager: CommonPager = CommonPager(executeUseCase)

    private val refreshLoadParamsRequest = PagingSource.LoadParams.Refresh(
        key = "",
        loadSize = 5,
        placeholdersEnabled = false
    )
    private val appendLoadParamsRequest = PagingSource.LoadParams.Append(
        key = "",
        loadSize =  5,
        placeholdersEnabled = false
    )
    private val expectedPageLoadResult = PagingSource.LoadResult.Page(
        data = RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG.items,
        nextKey = RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG.nextPageToken,
        prevKey = RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG.prevPageToken
    )

    private val testUseCaseException =
        UseCaseException.VideoListLoadException(HttpException("400", RuntimeException()))

    private fun testPositiveCase() {
        whenever(repository.getVideos(initialPageToken))
            .thenReturn(flowOf(RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG))
    }

    private fun testNegativeCase() {
        whenever(repository.getVideos(initialPageToken))
            .thenReturn(flow {
                throw UseCaseException.VideoListLoadException(testUseCaseException)
            })
    }

    @Test
    fun `PagingSource returns ResponseVideoList on refresh request`() = runTest {
        testPositiveCase()
        val actualPageLoadResult = commonPager.load(refreshLoadParamsRequest)

        expectedPageLoadResult.assertExpectedPagingLoadResultWith(actualPageLoadResult)
    }

    @Test
    fun `PagingSource returns ResponseVideoList on append request`() = runTest {
        testPositiveCase()
        val actualPageLoadResult = commonPager.load(appendLoadParamsRequest)

        expectedPageLoadResult.assertExpectedPagingLoadResultWith(actualPageLoadResult)
    }

    @Test
    fun `PagingSource returns LoadResult Error if network layer throws an error`() = runTest {
            testNegativeCase()
            val actualPageLoadResult = commonPager.load(refreshLoadParamsRequest)

            assertErrorLoadResult(actualPageLoadResult)
    }

    private fun PagingSource.LoadResult<String, YoutubeVideo>.assertExpectedPagingLoadResultWith(
        actualLoadResult: PagingSource.LoadResult<String, YoutubeVideo>
    ) {
        val expectedLoadResult = this

        assertTrue(actualLoadResult is PagingSource.LoadResult.Page)
        assertEquals(
            (expectedLoadResult as PagingSource.LoadResult.Page).prevKey,
            (actualLoadResult as PagingSource.LoadResult.Page).prevKey
        )
        assertEquals(expectedLoadResult.nextKey, actualLoadResult.nextKey)
        assertEquals(expectedLoadResult.data, actualLoadResult.data)
    }

    private fun assertErrorLoadResult(actualLoadResult: PagingSource.LoadResult<String, YoutubeVideo>) {
        assertTrue(actualLoadResult is PagingSource.LoadResult.Error)
        assertTrue((actualLoadResult as PagingSource.LoadResult.Error).throwable is UseCaseException)
    }
}