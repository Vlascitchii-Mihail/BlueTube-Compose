package com.vlascitchii.data_repository.repository_impl

import com.vlascitchii.data_repository.data_source.local.LocalVideoListDataSource
import com.vlascitchii.data_repository.data_source.remote.RemoteShortsDataSource
import com.vlascitchii.data_repository.rule.DispatcherTestRule
import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideoResponse
import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideoResponse.Companion.RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class ShortsRepositoryImplTest {

    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    private val remoteShortsDataSource: RemoteShortsDataSource = mock()
    private val localVideoListDataSource: LocalVideoListDataSource = mock()
    private val shortsRepositoryImpl = ShortsRepositoryImpl(remoteShortsDataSource, localVideoListDataSource)

    private val initialPageToken = ""
    private val expectedResult = flowOf(RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG)

    @Before
    fun init() {
       whenever(remoteShortsDataSource.fetchShorts(initialPageToken))
           .thenReturn(expectedResult)
    }

    @Test
    fun `fun getShorts() returns correct Flow with YoutubeVideoResponse`() = runTest {
        val actualValue = shortsRepositoryImpl.getShorts(initialPageToken)
        assertEquals(expectedResult.first(), actualValue.first())
    }

    @Test
    fun `fun getShorts() inserts each received videos into DB`() = runTest {
        shortsRepositoryImpl.getShorts(initialPageToken).collect()
        verify(localVideoListDataSource).insertVideosToDatabaseWithTimeStamp(any<YoutubeVideoResponse>(), any())
    }
}