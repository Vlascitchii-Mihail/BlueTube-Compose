package com.vlascitchii.data_repository.paging

import android.net.http.HttpException
import androidx.paging.PagingSource
import com.vlascitchii.data_repository.data_source.local.LocalVideoListDataSource
import com.vlascitchii.data_repository.data_source.remote.RemoteVideoListDataSource
import com.vlascitchii.data_repository.util.DispatcherTestRule
import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideo
import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideoResponse
import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideoResponse.Companion.RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG
import com.vlascitchii.domain.util.UseCaseException
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.OffsetDateTime

class CommonPagingSourceTest {
    @get:Rule
    var dispatcherTestRule = DispatcherTestRule()

    private val initialPageToken = ""
    private val remoteVideoListDataSource: RemoteVideoListDataSource = mock()
    private val localVideoListDataSource: LocalVideoListDataSource = mock()

    private val commonPagingSource: CommonPagingSource = CommonPagingSource { pageToken: String ->
        remoteVideoListDataSource.fetchVideos(nextPageToken = pageToken)
            .onEach { youtubeVideoResponse: YoutubeVideoResponse ->
                localVideoListDataSource.insertVideosToDatabaseWithTimeStamp(
                    youtubeVideoResponse,
                    OffsetDateTime.now()
                )
            }
    }

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
        whenever(remoteVideoListDataSource.fetchVideos(initialPageToken))
            .thenReturn(flowOf(RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG))
    }

    private fun testNegativeCase() {
        whenever(remoteVideoListDataSource.fetchVideos(initialPageToken))
            .thenReturn(flow {
                throw UseCaseException.VideoListLoadException(testUseCaseException)
            })
    }

    @Test
    fun `PagingSource returns ResponseVideoList on refresh request`() = runTest {
        testPositiveCase()
        val actualPageLoadResult = commonPagingSource.load(refreshLoadParamsRequest)

        expectedPageLoadResult.assertExpectedPagingLoadResultWith(actualPageLoadResult)
    }

    @Test
    fun `PagingSource returns ResponseVideoList on append request`() = runTest {
        testPositiveCase()
        val actualPageLoadResult = commonPagingSource.load(appendLoadParamsRequest)

        expectedPageLoadResult.assertExpectedPagingLoadResultWith(actualPageLoadResult)
    }

    @Test
    fun `PagingSource returns LoadResult Error if network layer throws an error`() = runTest {
        testNegativeCase()
        val actualPageLoadResult = commonPagingSource.load(refreshLoadParamsRequest)

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

    @Test
    fun `on each fun fetchVideos() fun insertVideosToDatabaseWithTimeStamp() inserts data to DB`() = runTest {
        testPositiveCase()

        commonPagingSource.load(refreshLoadParamsRequest)

        verify(localVideoListDataSource).insertVideosToDatabaseWithTimeStamp(any<YoutubeVideoResponse>(), any<OffsetDateTime>())
    }
}