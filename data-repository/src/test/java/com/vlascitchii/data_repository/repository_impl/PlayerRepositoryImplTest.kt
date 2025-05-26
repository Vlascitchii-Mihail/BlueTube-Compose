package com.vlascitchii.data_repository.repository_impl

import com.vlascitchii.data_repository.data_source.local.LocalVideoListDataSource
import com.vlascitchii.data_repository.data_source.remote.RemoteSearchDataSource
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

class PlayerRepositoryImplTest {

    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    private val remoteSearchDataSource: RemoteSearchDataSource = mock()
    private val localVideoListDataSource: LocalVideoListDataSource = mock()
    private val playerRepositoryImpl = PlayerRepositoryImpl(remoteSearchDataSource, localVideoListDataSource)

    private val initialPageToken = ""
    private val testQuery = "Test query"
    private val expectedResult = flowOf(RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG)

    @Before
    fun init() {
        whenever(remoteSearchDataSource.searchRelatedVideos(testQuery, initialPageToken))
            .thenReturn(expectedResult)
    }

    @Test
    fun `fun getSearchRelayedVideos() returns correct Flow with YouTubeVideoResponse`() = runTest {
        val actualValue = playerRepositoryImpl.getSearchRelayedVideos(testQuery, initialPageToken)
        assertEquals(expectedResult.first(), actualValue.first())
    }

    @Test
    fun `fim getSearchRelayedVideos() inserts each received videos into DB`() = runTest {
        playerRepositoryImpl.getSearchRelayedVideos(testQuery, initialPageToken).collect()

        verify(localVideoListDataSource).insertVideosToDatabaseWithTimeStamp(any<YoutubeVideoResponse>(), any())
    }
}