package com.appelier.bluetubecompose.integration.api_test

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.appelier.bluetubecompose.core.core_api.VideoApiService
import com.appelier.bluetubecompose.screen_video_list.model.single_cnannel.Channel.Companion.DEFAULT_CHANNEL_1
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideoResponse.Companion.DEFAULT_VIDEO_RESPONSE
import com.appelier.bluetubecompose.search_video.model.DEFAULT_SEARCH_VIDEO_RESPONSE
import com.appelier.bluetubecompose.utils.readJsonFileAsString
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.any
import javax.inject.Inject

const val SUCCESS_CODE = 200

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class VideoSourceApiTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var apiService: VideoApiService
    @Inject
    lateinit var mockWebServer: MockWebServer
    private val standardTestDispatcher = StandardTestDispatcher()
    private val testCoroutineScope = TestScope(standardTestDispatcher)

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun fetchVideosReturnsVideoList() {
        testCoroutineScope.runTest {
            generateMockResponseFrom("raw/video_list.json")

            val fetchedVideoList = apiService.fetchVideos()

            assertEquals(DEFAULT_VIDEO_RESPONSE, fetchedVideoList.body())
        }
    }

    @Test
    fun fetchChannelsReturnsChannelsListWithOneVideoById() {
        testCoroutineScope.runTest {
            generateMockResponseFrom("raw/channel_response_1.json")

            val fetchedChannel = apiService.fetchChannels(DEFAULT_CHANNEL_1.id)

            assertEquals(DEFAULT_CHANNEL_1, fetchedChannel.body()?.items?.first())
        }
    }

    @Test
    fun fetchSearchVideosReturnsSearchVideos() {
        testCoroutineScope.runTest {
            generateMockResponseFrom("raw/search_response.json")

            val searchedVideos = apiService.searchVideo(query = "steam deck")

            assertEquals(DEFAULT_SEARCH_VIDEO_RESPONSE, searchedVideos.body())
        }
    }

    @Test
    fun fetchParticularVideoReturnsParticularVideo() {
        testCoroutineScope.runTest {
            generateMockResponseFrom("raw/particular_video_response.json")

            val particularVideo = apiService.fetchParticularVideo(any()).body()!!

            assertEquals(DEFAULT_VIDEO_RESPONSE.items.first(), particularVideo.items.first())
        }
    }

    private fun generateMockResponseFrom(jsonFilePath: String) {
        val videoResponse = MockResponse().setBody(
            readJsonFileAsString(this.javaClass.classLoader, jsonFilePath)
        ).setResponseCode(SUCCESS_CODE)

        mockWebServer.enqueue(videoResponse)
    }
}