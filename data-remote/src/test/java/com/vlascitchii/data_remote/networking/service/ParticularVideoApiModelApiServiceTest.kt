package com.vlascitchii.data_remote.networking.service

import com.vlascitchii.data_remote.enetity_api_model.video_list_api_model.videos_api_model.ParticularVideoApiModel
import com.vlascitchii.data_remote.enetity_api_model.video_search_api_model.DEFAULT_SEARCH_VIDEO_RESPONSE
import com.vlascitchii.data_remote.rule.DispatcherTestRule
import com.vlascitchii.data_remote.util.MockWebServerApiProvider
import com.vlascitchii.data_remote.util.MockWebServerScheduler
import com.vlascitchii.data_remote.util.PARTICULAR_VIDEO_RESPONSE_1_PATH
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ParticularVideoApiModelApiServiceTest {

    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    private lateinit var mockWebServerApiProvider: MockWebServerApiProvider
    private lateinit var particularVideoApiService: ParticularVideoApiService
    private lateinit var mockWebServerScheduler: MockWebServerScheduler

    @Before
    fun init() {
        mockWebServerApiProvider = MockWebServerApiProvider()
        particularVideoApiService = mockWebServerApiProvider.provideParticularVideoApiService()
        mockWebServerScheduler = mockWebServerApiProvider.mockWebServerScheduler
    }

    @After
    fun tearDown() {
        mockWebServerScheduler.shutDownMockWebServer()
    }

    @Test
    fun `fetchParticularVideo returns YoutubeVideoApiModel by id`() = runTest {
        mockWebServerScheduler.generateMockResponseFrom(PARTICULAR_VIDEO_RESPONSE_1_PATH)

        val searchedVideoId = DEFAULT_SEARCH_VIDEO_RESPONSE.items.first().id.videoId
        val particularVideo = particularVideoApiService.fetchParticularVideo(searchedVideoId).body()

        assertEquals(ParticularVideoApiModel.particularVideoExample, particularVideo?.items?.first())
    }


    @Test
    fun `fetchParticularVideo returns YoutubeVideoApiModel by id2`() = runTest {
        mockWebServerScheduler.generateMockResponseFrom(PARTICULAR_VIDEO_RESPONSE_1_PATH)

        val searchedVideoId = DEFAULT_SEARCH_VIDEO_RESPONSE.items.first().id.videoId
        val particularVideo = particularVideoApiService.fetchParticularVideo(searchedVideoId).body()

        assertEquals(ParticularVideoApiModel.particularVideoExample, particularVideo?.items?.first())
    }
}