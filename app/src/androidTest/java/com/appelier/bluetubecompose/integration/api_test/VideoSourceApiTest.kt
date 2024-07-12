package com.appelier.bluetubecompose.integration.api_test

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.appelier.bluetubecompose.core.core_api.Constants.Companion.CONTENT_DETAILS
import com.appelier.bluetubecompose.core.core_api.Constants.Companion.MOST_POPULAR
import com.appelier.bluetubecompose.core.core_api.Constants.Companion.REGION_CODE
import com.appelier.bluetubecompose.core.core_api.Constants.Companion.RELEVANCE
import com.appelier.bluetubecompose.core.core_api.Constants.Companion.SINGLE_CHANNEL
import com.appelier.bluetubecompose.core.core_api.Constants.Companion.SNIPPET
import com.appelier.bluetubecompose.core.core_api.Constants.Companion.STATISTICS
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
            val videoResponse = MockResponse().setBody(
                readJsonFileAsString(this.javaClass.classLoader, "raw/video_list.json")
            ).setResponseCode(200)

            mockWebServer.enqueue(videoResponse)

            val fetchedVideoList = apiService.fetchVideos(
                "$SNIPPET, $CONTENT_DETAILS, $STATISTICS",
                MOST_POPULAR,
                REGION_CODE,
                ""
            )

            assertEquals(DEFAULT_VIDEO_RESPONSE, fetchedVideoList.body())
        }
    }

    @Test
    fun fetchChannelsReturnsChannelsListWithOneVideoById() {
        testCoroutineScope.runTest {
            val mockChannelResponse = MockResponse().setBody(
                readJsonFileAsString(this.javaClass.classLoader, "raw/channel_response_1.json")
            )
            mockWebServer.enqueue(mockChannelResponse)

            val fetchedChannel = apiService.fetchChannels(
                DEFAULT_CHANNEL_1.id,
                "$SNIPPET, $CONTENT_DETAILS, $STATISTICS",
                SINGLE_CHANNEL
            )

            assertEquals(DEFAULT_CHANNEL_1, fetchedChannel.body()?.items?.first())
        }
    }

    @Test
    fun fetchSearchVideosReturnsSearchVideos() {
        testCoroutineScope.runTest {
            val mockSearchVideosResponse = MockResponse().setBody(
                readJsonFileAsString(this.javaClass.classLoader, "raw/search_response.json")
            )
            mockWebServer.enqueue(mockSearchVideosResponse)

            val searchedVideos = apiService.searchVideo(
                query = "steam deck",
                SNIPPET,
                RELEVANCE,
                ""
            )

            assertEquals(DEFAULT_SEARCH_VIDEO_RESPONSE, searchedVideos.body())
        }
    }

    @Test
    fun fetchParticularVideoReturnsParticularVideo() {
        testCoroutineScope.runTest {
            val mockParticularVideoResponse = MockResponse().setBody(
                readJsonFileAsString(this.javaClass.classLoader, "raw/particular_video_response.json")
            )
            mockWebServer.enqueue(mockParticularVideoResponse)

            val particularVideo = apiService.fetchParticularVideo(any()).body()!!

            assertEquals(DEFAULT_VIDEO_RESPONSE.items.first(), particularVideo.items.first())
        }
    }
}