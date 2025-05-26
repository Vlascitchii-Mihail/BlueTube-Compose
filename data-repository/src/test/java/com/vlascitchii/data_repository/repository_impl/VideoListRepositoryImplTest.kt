package com.vlascitchii.data_repository.repository_impl

import com.vlascitchii.data_repository.data_source.local.LocalVideoListDataSource
import com.vlascitchii.data_repository.data_source.remote.RemoteSearchDataSource
import com.vlascitchii.data_repository.data_source.remote.RemoteVideoListDataSource
import com.vlascitchii.data_repository.rule.DispatcherTestRule
import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideoResponse
import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideoResponse.Companion.RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class VideoListRepositoryImplTest {

    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    private val remoteVideoListDataSource: RemoteVideoListDataSource = mock()
    private val remoteSearchDataSource: RemoteSearchDataSource = mock()
    private val localVideoListDataSource: LocalVideoListDataSource = mock()

    private val videoListRepositoryImpl = VideoListRepositoryImpl(
        remoteVideoListDataSource,
        remoteSearchDataSource,
        localVideoListDataSource
    )

    private val initialPageToken = ""
    private val testQuery = "Test query"
    private val expectedResult = flowOf(RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG)

    @Before
    fun init() {
        whenever(remoteVideoListDataSource.fetchVideos(initialPageToken))
            .thenReturn(expectedResult)

        whenever(remoteSearchDataSource.searchVideos(testQuery, initialPageToken))
            .thenReturn(expectedResult)
    }

    @Test
    fun `fun getVideos() returns correct Flow with YoutubeVideoResponse`() = runTest {
        val actualResult = videoListRepositoryImpl.getVideos(initialPageToken)
        assertEquals(expectedResult.first(), actualResult.first())
    }

    @Test
    fun `fun getVideos() inserts each received videos into DB`() = runTest {
        videoListRepositoryImpl.getVideos(initialPageToken).collect()

        verify(localVideoListDataSource).insertVideosToDatabaseWithTimeStamp(any<YoutubeVideoResponse>(), any())
    }

    @Test
    fun `fun getSearchVideos() returns videos by query`() = runTest {
        val actualResult = videoListRepositoryImpl.getSearchVideos(testQuery, initialPageToken)
        assertEquals(expectedResult.first(), actualResult.first())
    }

    @Test
    fun `fun getSearchVideos() inserts each received videos into DB`() = runTest {
        videoListRepositoryImpl.getSearchVideos(testQuery, initialPageToken).collect()

        verify(localVideoListDataSource).insertVideosToDatabaseWithTimeStamp(any<YoutubeVideoResponse>(), any())
    }
}