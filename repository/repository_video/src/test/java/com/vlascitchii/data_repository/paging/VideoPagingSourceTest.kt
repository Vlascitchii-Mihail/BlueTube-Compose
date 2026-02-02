package com.vlascitchii.data_repository.paging

import android.net.http.HttpException
import androidx.paging.PagingSource
import com.vlascitchii.common_test.rule.DispatcherTestRule
import com.vlascitchii.data_repository.data_source.local.LocalVideoListDataSource
import com.vlascitchii.data_repository.mock_model.DOMAIN_RESPONSE_VIDEO_WITH_CHANNEL_IMG
import com.vlascitchii.domain.custom_scope.CustomCoroutineScope
import com.vlascitchii.domain.model.ErrorDomain
import com.vlascitchii.domain.model.videos.YoutubeVideoDomain
import com.vlascitchii.domain.model.videos.YoutubeVideoResponseDomain
import com.vlascitchii.domain.util.UseCaseException
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import java.time.OffsetDateTime

class VideoPagingSourceTest {

    @get:Rule
    var dispatcherTestRule = DispatcherTestRule()

    private val initialPageToken = ""

    private val localVideoListDataSource: LocalVideoListDataSource = mock()
    private lateinit var mockApiFetchLambda: suspend (page: String) -> YoutubeVideoResponseDomain
    private lateinit var videoPagingSource: VideoPagingSource
    private val customCoroutineScope = CustomCoroutineScope(dispatcherTestRule.testDispatcher)

    private val refreshLoadParamsRequest = PagingSource.LoadParams.Refresh(
        key = initialPageToken,
        loadSize = 5,
        placeholdersEnabled = false
    )
    private val appendLoadParamsRequest = PagingSource.LoadParams.Append(
        key = initialPageToken,
        loadSize = 5,
        placeholdersEnabled = false
    )
    private val expectedPageLoadResult = PagingSource.LoadResult.Page(
        data = DOMAIN_RESPONSE_VIDEO_WITH_CHANNEL_IMG.items,
        nextKey = DOMAIN_RESPONSE_VIDEO_WITH_CHANNEL_IMG.nextPageToken,
        prevKey = DOMAIN_RESPONSE_VIDEO_WITH_CHANNEL_IMG.prevPageToken
    )

    private val testUseCaseException: UseCaseException.VideoListLoadException =
        UseCaseException.VideoListLoadException(
            HttpException("400", RuntimeException()),
            ErrorDomain()
        )

    private fun testPositiveCase() {
        mockApiFetchLambda = { DOMAIN_RESPONSE_VIDEO_WITH_CHANNEL_IMG }
        videoPagingSource = VideoPagingSource(
            localDataSource = localVideoListDataSource,
            customCoroutineScope = customCoroutineScope,
            fetch = mockApiFetchLambda
        )
    }

    private fun testNegativeCase() {
        mockApiFetchLambda = { throw testUseCaseException }
        videoPagingSource = VideoPagingSource(
            localDataSource = localVideoListDataSource,
            customCoroutineScope = customCoroutineScope,
            fetch = mockApiFetchLambda
        )
    }

    @Test
    fun `PagingSource returns YoutubeVideoDomain on refresh request`() = runTest {
        testPositiveCase()
        val actualPageLoadResult = videoPagingSource.load(refreshLoadParamsRequest)

        expectedPageLoadResult.assertExpectedPagingLoadResultWith(actualPageLoadResult)
    }

    @Test
    fun `PagingSource returns YoutubeVideoDomain on append request`() = runTest {
        testPositiveCase()
        val actualPageLoadResult = videoPagingSource.load(appendLoadParamsRequest)

        expectedPageLoadResult.assertExpectedPagingLoadResultWith(actualPageLoadResult)
    }

    @Test
    fun `negative PagingSource returns LoadResult Error if network layer throws an error`() = runTest {
        testNegativeCase()
        val actualPageLoadResult = videoPagingSource.load(refreshLoadParamsRequest)

        assertTrue((actualPageLoadResult as PagingSource.LoadResult.Error).throwable is UseCaseException)
    }

    private fun PagingSource.LoadResult<String, YoutubeVideoDomain>.assertExpectedPagingLoadResultWith(
        actualLoadResult: PagingSource.LoadResult<String, YoutubeVideoDomain>
    ) {
        val expectedLoadResult = this as PagingSource.LoadResult.Page
        val actualLoadResult = actualLoadResult as PagingSource.LoadResult.Page

        assertEquals(expectedLoadResult.prevKey, actualLoadResult.prevKey)
        assertEquals(expectedLoadResult.nextKey, actualLoadResult.nextKey)
        assertEquals(expectedLoadResult.data, actualLoadResult.data)
    }

    @Test
    fun `on each fun mockApiFetchLambda invoke() data is inserted to DB`() = runTest {
        testPositiveCase()

        videoPagingSource.load(refreshLoadParamsRequest)

        verify(localVideoListDataSource).insertVideosWithTimeStamp(
            any<YoutubeVideoResponseDomain>(),
            any<OffsetDateTime>()
        )
    }
}
