package com.vlascitchii.data_remote.networking.service

import com.vlascitchii.data_remote.enetity_api_model.util.convertToYouTubeVideoResponseApiModel
import com.vlascitchii.data_remote.rule.DispatcherTestRule
import com.vlascitchii.data_remote.util.MockWebServerApiProvider
import com.vlascitchii.data_remote.util.MockWebServerScheduler
import com.vlascitchii.data_remote.util.VIDEO_LIST_RESPONSE_PATH
import com.vlascitchii.data_remote.util.assertListEqualsTo
import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideoResponse.Companion.RESPONSE_VIDEO_LIST_NO_CHANNEL_IMG_URL
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

        RESPONSE_VIDEO_LIST_NO_CHANNEL_IMG_URL.convertToYouTubeVideoResponseApiModel().items.assertListEqualsTo(
            fetchedVideoList?.items
        )

        assertEquals(RESPONSE_VIDEO_LIST_NO_CHANNEL_IMG_URL.nextPageToken, fetchedVideoList?.nextPageToken)
        assertEquals(RESPONSE_VIDEO_LIST_NO_CHANNEL_IMG_URL.prevPageToken, fetchedVideoList?.prevPageToken)
    }
}