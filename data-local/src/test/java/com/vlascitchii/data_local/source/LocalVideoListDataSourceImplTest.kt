package com.vlascitchii.data_local.source

import com.vlascitchii.data_local.database.YouTubeVideoDao
import com.vlascitchii.data_local.database.convertToDomainYoutubeVideoResponse
import com.vlascitchii.data_local.source.utils.rule.DispatcherTestRule
import com.vlascitchii.data_local.enetity.PageEntity
import com.vlascitchii.data_local.enetity.video_list.videos.YoutubeVideoEntity
import com.vlascitchii.data_local.enetity.video_list.videos.YoutubeVideoResponseEntity
import com.vlascitchii.data_local.enetity.video_list.videos.YoutubeVideoResponseEntity.Companion.TEST_DATABASE_VIDEO_RESPONSE
import com.vlascitchii.data_local.source.utils.assertListEqualsTo
import com.vlascitchii.data_repository.data_source.local.LocalVideoListDataSource
import com.vlascitchii.domain.custom_coroutine_scopes.VideoCoroutineScope
import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideoResponse.Companion.RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class LocalVideoListDataSourceImplTest {

    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()
    private val youTubeVideoDao: YouTubeVideoDao = mock()
    private val videoCoroutineScope = VideoCoroutineScope(dispatcher = dispatcherTestRule.testDispatcher)
    private val localVideoListDataSource: LocalVideoListDataSource =
        LocalVideoListDataSourceImpl(youTubeVideoDao, videoCoroutineScope)

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `insertVideosToDatabaseWithTimeStamp() calls specific Dao functions`() = runTest {
        whenever(youTubeVideoDao.getVideosCount()).thenReturn(flowOf(115))

        val pageEntity: PageEntity = PageEntity(
            nextPageToken = RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG.nextPageToken,
            currentPageToken = RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG.currentPageToken,
            prevPageToken = RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG.prevPageToken,
        )

        localVideoListDataSource.insertVideosToDatabaseWithTimeStamp(
            RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG,
            YoutubeVideoResponseEntity.testDateTime
        )

        advanceUntilIdle()
        verify(youTubeVideoDao).insertPages(eq(pageEntity))

        TEST_DATABASE_VIDEO_RESPONSE.items.forEach { videoEntity: YoutubeVideoEntity ->
            verify(youTubeVideoDao).insertVideo(videoEntity)
        }

        verify(youTubeVideoDao).getVideosCount()
        verify(youTubeVideoDao).deleteExtraFiveVideos()
    }

    @Test
    fun `getVideosFromDatabase() returns a Flow with videos using empty page token`() = runTest {
        whenever(youTubeVideoDao.getFirstPageFromDb()).thenReturn(flowOf(TEST_DATABASE_VIDEO_RESPONSE))

        val result = localVideoListDataSource.getVideosFromDatabase(YoutubeVideoResponseEntity.INITIAL_PAGE_TOKEN)

        verify(youTubeVideoDao).getFirstPageFromDb()
        assertEquals(RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG.nextPageToken, result.first().nextPageToken)
        assertEquals(RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG.prevPageToken, result.first().prevPageToken)
        assertEquals(RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG.currentPageToken, result.first().currentPageToken)

        TEST_DATABASE_VIDEO_RESPONSE.convertToDomainYoutubeVideoResponse().items.assertListEqualsTo(result.first().items)
    }

    @Test
    fun `getVideosFromDatabase() returns a Flow with videos using a real page token`() = runTest {
        val pageToken = "CAoQAA"
        whenever(youTubeVideoDao.getVideosFromPage(pageToken)).thenReturn(flowOf(TEST_DATABASE_VIDEO_RESPONSE))

        val result = localVideoListDataSource.getVideosFromDatabase(pageToken)

        verify(youTubeVideoDao).getVideosFromPage(eq(pageToken))
        assertEquals(RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG.nextPageToken, result.first().nextPageToken)
        assertEquals(RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG.prevPageToken, result.first().prevPageToken)
        assertEquals(RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG.currentPageToken, result.first().currentPageToken)

        TEST_DATABASE_VIDEO_RESPONSE.convertToDomainYoutubeVideoResponse().items.assertListEqualsTo(result.first().items)
    }
}