package com.vlascitchii.data_remote.service

import com.vlascitchii.common_test.rule.DispatcherTestRule
import com.vlascitchii.common_test.util.assertListEqualsTo
import com.vlascitchii.data_remote.model_api.API_RESPONSE_SEARCH_VIDEO_NO_CHANNEL_URL
import com.vlascitchii.data_remote.model_api.API_VIDEO_RESPONSE_NO_CHANNEL_IMG_URL
import com.vlascitchii.data_remote.networking.service.VideoListApiService
import com.vlascitchii.data_remote.util.MockWebServerApiProvider
import com.vlascitchii.data_remote.util.MockWebServerScheduler
import com.vlascitchii.data_remote.util.SEARCH_RESPONSE_PATH
import com.vlascitchii.data_remote.util.VIDEO_LIST_RESPONSE_PATH
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class VideoListApiServiceTest {

    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    private lateinit var videoListApiService: VideoListApiService
    private lateinit var mockWebServerApiProvider: MockWebServerApiProvider
    private lateinit var mockWebServerScheduler: MockWebServerScheduler

    @Before
    fun init() {
        mockWebServerApiProvider = MockWebServerApiProvider()
        videoListApiService = mockWebServerApiProvider.provideMockVideoListApiService()
        mockWebServerScheduler = mockWebServerApiProvider.mockWebServerScheduler
    }

    @After
    fun tearDown() {
        mockWebServerScheduler.shutDownMockWebServer()
    }

    @Test
    fun fetchVideos_returns_videoList() = runTest {
        mockWebServerScheduler.generateMockResponseFrom(VIDEO_LIST_RESPONSE_PATH)

        val fetchedVideoList = videoListApiService.fetchVideos().body()

        API_VIDEO_RESPONSE_NO_CHANNEL_IMG_URL.items.assertListEqualsTo(
            fetchedVideoList?.items
        )

        assertEquals(API_RESPONSE_SEARCH_VIDEO_NO_CHANNEL_URL.nextPageToken, fetchedVideoList?.nextPageToken)
        assertEquals(API_RESPONSE_SEARCH_VIDEO_NO_CHANNEL_URL.prevPageToken, fetchedVideoList?.prevPageToken)
    }

    @Test
    fun fetchSearchVideosReturnsSearchVideos() = runTest {
        mockWebServerScheduler.generateMockResponseFrom(SEARCH_RESPONSE_PATH)

        val searchedVideos = videoListApiService.searchVideo(query = "steam deck").body()

        API_RESPONSE_SEARCH_VIDEO_NO_CHANNEL_URL.items.assertListEqualsTo(searchedVideos?.items)

        assertEquals(API_RESPONSE_SEARCH_VIDEO_NO_CHANNEL_URL.nextPageToken, searchedVideos?.nextPageToken)
        assertEquals(API_RESPONSE_SEARCH_VIDEO_NO_CHANNEL_URL.prevPageToken, searchedVideos?.prevPageToken)
    }
}
