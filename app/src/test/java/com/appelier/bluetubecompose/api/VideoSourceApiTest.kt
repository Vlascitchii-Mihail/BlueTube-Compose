package com.appelier.bluetubecompose.api

import com.appelier.bluetubecompose.core.core_api.VideoApiService
import com.appelier.bluetubecompose.rule.DispatcherTestRule
import com.appelier.bluetubecompose.screen_video_list.model.single_cnannel.Channel.Companion.DEFAULT_CHANNEL_1
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideoResponse.Companion.DEFAULT_VIDEO_RESPONSE
import com.appelier.bluetubecompose.search_video.model.DEFAULT_SEARCH_VIDEO_RESPONSE
import com.appelier.bluetubecompose.search_video.model.DEFAULT_SHORTS_RESPONSE
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File

const val SUCCESS_CODE = 200
const val MOCK_BASE_URL = "/"
private const val ROOT_PATH = "C:\\MyPrograms\\Android\\BlueTubeCompose\\app\\src\\test\\java\\"

class VideoSourceApiTest {

    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    private val mockWebServer: MockWebServer = MockWebServer()
    private val apiService: VideoApiService = provideMockVideoApiService(mockWebServer)

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    private fun provideMockVideoApiService(mockWebServer: MockWebServer): VideoApiService {
        val client = OkHttpClient.Builder().build()
        val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(mockWebServer.url(MOCK_BASE_URL))
                .addConverterFactory(MoshiConverterFactory.create())
                .client(client)
                .build()
        }

        return retrofit.create(VideoApiService::class.java)
    }

    @Test
    fun fetchVideosReturnsVideoList() = runTest {
        generateMockResponseFrom("${ROOT_PATH}raw/video_list.json")

        val fetchedVideoList = apiService.fetchVideos()

        assertEquals(DEFAULT_VIDEO_RESPONSE, fetchedVideoList.body())
    }

    @Test
    fun fetchChannelsReturnsChannelsListWithOneVideoById() = runTest {
        generateMockResponseFrom("${ROOT_PATH}raw\\channel_response_1.json")

        val fetchedChannel = apiService.fetchChannels(DEFAULT_CHANNEL_1.id)

        assertEquals(DEFAULT_CHANNEL_1, fetchedChannel.body()?.items?.first())
    }

    @Test
    fun fetchSearchVideosReturnsSearchVideos() = runTest {
        generateMockResponseFrom("${ROOT_PATH}raw\\search_response.json")

        val searchedVideos = apiService.searchVideo(query = "steam deck")

        assertEquals(DEFAULT_SEARCH_VIDEO_RESPONSE, searchedVideos.body())
    }

    @Test
    fun fetchParticularVideoReturnsParticularVideo() = runTest {
        generateMockResponseFrom("${ROOT_PATH}raw\\particular_video_response.json")

        val particularVideo = apiService.fetchParticularVideo("Test video id").body()!!

        assertEquals(DEFAULT_VIDEO_RESPONSE.items.first(), particularVideo.items.first())
    }

    @Test
    fun fetchShortsVideos() = runTest {
        generateMockResponseFrom("${ROOT_PATH}raw\\shorts_response.json")

        val shorts = apiService.fetchShorts().body()!!

        assertEquals(DEFAULT_SHORTS_RESPONSE.items[4], shorts.items[4])
    }

    private fun generateMockResponseFrom(jsonFilePath: String) {
        val videoResponse = MockResponse().setBody(
            readJson(jsonFilePath)
        ).setResponseCode(SUCCESS_CODE)

        mockWebServer.enqueue(videoResponse)
    }

    private fun readJson(jsonFilePath: String): String {
        return File(jsonFilePath).readText(Charsets.UTF_8)
    }
}
