package com.vlascitchii.data_remote.source

import com.vlascitchii.common_test.rule.DispatcherTestRule
import com.vlascitchii.data_remote.networking.service.SearchApiService
import com.vlascitchii.data_remote.util.CHANNEL_RESPONSE_1_PATH
import com.vlascitchii.data_remote.util.CHANNEL_RESPONSE_2_PATH
import com.vlascitchii.data_remote.util.CHANNEL_RESPONSE_3_PATH
import com.vlascitchii.data_remote.util.CHANNEL_RESPONSE_4_PATH
import com.vlascitchii.data_remote.util.CHANNEL_RESPONSE_5_PATH
import com.vlascitchii.data_remote.util.MockWebServerApiProvider
import com.vlascitchii.data_remote.util.MockWebServerScheduler
import com.vlascitchii.data_remote.util.PARTICULAR_VIDEO_RESPONSE_1_PATH
import com.vlascitchii.data_remote.util.PARTICULAR_VIDEO_RESPONSE_2_PATH
import com.vlascitchii.data_remote.util.PARTICULAR_VIDEO_RESPONSE_3_PATH
import com.vlascitchii.data_remote.util.PARTICULAR_VIDEO_RESPONSE_4_PATH
import com.vlascitchii.data_remote.util.PARTICULAR_VIDEO_RESPONSE_5_PATH
import com.vlascitchii.data_remote.util.SEARCH_RESPONSE_PATH
import com.vlascitchii.data_repository.data_source.remote.RemoteSearchDataSource
import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideoResponse.Companion.RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG
import com.vlascitchii.domain.util.UseCaseException
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RemoteSearchDataSourceImplTest {

    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    private lateinit var mockWebServerApiProvider: MockWebServerApiProvider
    private lateinit var searchApiService: SearchApiService
    private lateinit var remoteSearchDataSource: RemoteSearchDataSource
    private lateinit var mockWebServerScheduler: MockWebServerScheduler

    @Before
    fun initResponse() {
        mockWebServerApiProvider = MockWebServerApiProvider()
        searchApiService = mockWebServerApiProvider.provideMockSearchApiService()
        remoteSearchDataSource = RemoteSearchDataSourceImpl(searchApiService)
        mockWebServerScheduler = mockWebServerApiProvider.mockWebServerScheduler
    }

    @After
    fun tearDown() {
        mockWebServerScheduler.shutDownMockWebServer()
    }

    private fun initMockWebResponse() {
        val mockResponseList = listOf(
            SEARCH_RESPONSE_PATH,
            PARTICULAR_VIDEO_RESPONSE_1_PATH,
            PARTICULAR_VIDEO_RESPONSE_2_PATH,
            PARTICULAR_VIDEO_RESPONSE_3_PATH,
            PARTICULAR_VIDEO_RESPONSE_4_PATH,
            PARTICULAR_VIDEO_RESPONSE_5_PATH,
            CHANNEL_RESPONSE_1_PATH,
            CHANNEL_RESPONSE_2_PATH,
            CHANNEL_RESPONSE_3_PATH,
            CHANNEL_RESPONSE_4_PATH,
            CHANNEL_RESPONSE_5_PATH
        )

        mockResponseList.forEach { path: String ->
            mockWebServerScheduler.generateMockResponseFrom(path)
        }
    }

    @Test
    fun `search() returns a YoutubeVideoResponse with proper video URL and ID`() = runTest {
        initMockWebResponse()

        val searchVideoResponse = remoteSearchDataSource.search("Test query", "Test page token").first()

        val expectedChannelURLList = RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG.items.map { video -> video.snippet.channelImgUrl }
        val actualChannelURLList = searchVideoResponse.items.map { video -> video.snippet.channelImgUrl }

        val expectedVideoIdList = RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG.items.map { video -> video.id }
        val actualVideoIdList = searchVideoResponse.items.map { video -> video.id }

        assertTrue(expectedChannelURLList.containsAll(actualChannelURLList))
        assertTrue(expectedVideoIdList.containsAll(actualVideoIdList))
    }

    @Test
    fun `searchRelatedVideos() deletes the first same video from the result`() = runTest {
        initMockWebResponse()

        val searchVideoResponse =
            remoteSearchDataSource.searchRelatedVideos("Test query", "TEst page token").first()
        val actualItemSize = searchVideoResponse.items.size
        val expectedItemsSize = 4

        assertEquals(expectedItemsSize, actualItemSize)
    }

    @Test
    fun `searchVideos() doesn't delete the first the same video from the result`() = runTest {
        initMockWebResponse()

        val searchVideoResponse =
            remoteSearchDataSource.searchVideos("Test query", "TEst page token")
        val actualItemSize = searchVideoResponse.first().items.size
        val expectedItemsSize = 5

        assertEquals(expectedItemsSize, actualItemSize)
    }

    @Test
    fun `fun search() returns UseCaseException`() = runTest {
        mockWebServerScheduler.enqueueError()

        remoteSearchDataSource.search("Test query", "Test page token").catch { error: Throwable ->
            assertTrue(error is UseCaseException.SearchLoadException)
        }.collect()
    }
}