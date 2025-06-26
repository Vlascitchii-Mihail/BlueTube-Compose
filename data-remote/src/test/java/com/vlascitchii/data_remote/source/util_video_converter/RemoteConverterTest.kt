package com.vlascitchii.data_remote.source.util_video_converter

import com.vlascitchii.common_test.rule.DispatcherTestRule
import com.vlascitchii.data_remote.enetity_api_model.util.convertToYouTubeVideoResponseApiModel
import com.vlascitchii.data_remote.enetity_api_model.util.convertToYoutubeVideoApiModelList
import com.vlascitchii.data_remote.enetity_api_model.video_search_api_model.DEFAULT_SEARCH_VIDEO_RESPONSE
import com.vlascitchii.data_remote.networking.service.ParticularVideoApiService
import com.vlascitchii.data_remote.util.MockWebServerApiProvider
import com.vlascitchii.data_remote.util.MockWebServerScheduler
import com.vlascitchii.data_remote.util.PARTICULAR_VIDEO_RESPONSE_1_PATH
import com.vlascitchii.data_remote.util.PARTICULAR_VIDEO_RESPONSE_2_PATH
import com.vlascitchii.data_remote.util.PARTICULAR_VIDEO_RESPONSE_3_PATH
import com.vlascitchii.data_remote.util.PARTICULAR_VIDEO_RESPONSE_4_PATH
import com.vlascitchii.data_remote.util.PARTICULAR_VIDEO_RESPONSE_5_PATH
import com.vlascitchii.domain.custom_coroutine_scopes.AppCoroutineScope
import com.vlascitchii.domain.custom_coroutine_scopes.VideoCoroutineScope
import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideoResponse.Companion.RESPONSE_VIDEO_LIST_NO_CHANNEL_IMG_URL
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RemoteConverterTest {

    @get:Rule
    var dispatcherTestRule = DispatcherTestRule()

    private lateinit var mockWebServerApiProvider: MockWebServerApiProvider
    private lateinit var particularVideoApiMock: ParticularVideoApiService
    private lateinit var mockWebServerScheduler: MockWebServerScheduler
    private lateinit var testRemoteConverterDataScope: AppCoroutineScope
    private lateinit var remoteConverter: RemoteConverter


    @Before
    fun init() {
        mockWebServerApiProvider = MockWebServerApiProvider()
        particularVideoApiMock = mockWebServerApiProvider.provideMockSearchApiService()
        mockWebServerScheduler = mockWebServerApiProvider.mockWebServerScheduler
        testRemoteConverterDataScope = VideoCoroutineScope(dispatcher = dispatcherTestRule.testDispatcher)
        remoteConverter = object : RemoteConverter {
            override val particularVideoApi: ParticularVideoApiService = particularVideoApiMock
            override val remoteConverterDataScope: AppCoroutineScope = testRemoteConverterDataScope
        }
    }

    @After
    fun tearDown() {
        mockWebServerScheduler.shutDownMockWebServer()
    }

    private fun initMockResponse() {
        mockWebServerScheduler.generateMockResponseFrom(PARTICULAR_VIDEO_RESPONSE_1_PATH)
        mockWebServerScheduler.generateMockResponseFrom(PARTICULAR_VIDEO_RESPONSE_2_PATH)
        mockWebServerScheduler.generateMockResponseFrom(PARTICULAR_VIDEO_RESPONSE_3_PATH)
        mockWebServerScheduler.generateMockResponseFrom(PARTICULAR_VIDEO_RESPONSE_4_PATH)
        mockWebServerScheduler.generateMockResponseFrom(PARTICULAR_VIDEO_RESPONSE_5_PATH)
    }

    @Test
    fun `convert SearchVideoList to YouTubeVideoList`() = runTest {
        initMockResponse()
        val expectedVideoList = RESPONSE_VIDEO_LIST_NO_CHANNEL_IMG_URL.convertToYouTubeVideoResponseApiModel().items

        with(remoteConverter) {
            val convertedVideos = DEFAULT_SEARCH_VIDEO_RESPONSE.items.convertToApiVideosList()

            assertTrue(expectedVideoList.containsAll(convertedVideos))
        }
    }

    @Test
    fun `convertToVideo() converts SearchVideoAoiModel to YouTubeVideoApiModel`() = runTest {
        mockWebServerScheduler.generateMockResponseFrom(PARTICULAR_VIDEO_RESPONSE_1_PATH)
        val expectedVideo = RESPONSE_VIDEO_LIST_NO_CHANNEL_IMG_URL.items.convertToYoutubeVideoApiModelList().first()

        with(remoteConverter) {
            val convertedVideo = DEFAULT_SEARCH_VIDEO_RESPONSE.items.first().convertToVideo()
            assertEquals(expectedVideo, convertedVideo)
        }
    }
}