package com.vlascitchii.data_remote.service

import com.vlascitchii.common_test.rule.DispatcherTestRule
import com.vlascitchii.common_test.util.assertListEqualsTo
import com.vlascitchii.data_remote.model_api.API_VIDEO_RESPONSE_NO_CHANNEL_IMG_URL
import com.vlascitchii.data_remote.model_api.API_RESPONSE_SEARCH_VIDEO_NO_CHANNEL_URL
import com.vlascitchii.data_remote.model_api.CHANNELS
import com.vlascitchii.data_remote.model_api.CHANNELS_LIST_ID
import com.vlascitchii.data_remote.model_api.search_api_model.SearchVideoItemApiModel
import com.vlascitchii.data_remote.networking.service.BaseApiService
import com.vlascitchii.data_remote.util.CHANNEL_RESPONSE_PATH
import com.vlascitchii.data_remote.util.MockWebServerApiProvider
import com.vlascitchii.data_remote.util.MockWebServerScheduler
import com.vlascitchii.data_remote.util.VIDEO_LIST_RESPONSE_PATH
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class BaseApiServiceTest {

    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    private lateinit var baseApiService: BaseApiService
    private lateinit var mockWebServerScheduler: MockWebServerScheduler
    private lateinit var mockWebServerApiProvider: MockWebServerApiProvider

    @Before
    fun init() {
        mockWebServerApiProvider = MockWebServerApiProvider()
        baseApiService = mockWebServerApiProvider.provideMockBaseApiService()
        mockWebServerScheduler = mockWebServerApiProvider.mockWebServerScheduler
    }

    @After
    fun tearDown() {
        mockWebServerScheduler.shutDownMockWebServer()
    }

    @Test
    fun fetchChannelsReturnsChannelsById() = runTest {
        mockWebServerScheduler.generateMockResponseFrom(CHANNEL_RESPONSE_PATH)

        val fetchedChannel = baseApiService.fetchChannels(CHANNELS_LIST_ID).body()

        CHANNELS.assertListEqualsTo(fetchedChannel?.items)
    }

    @Test
    fun `fetch videoList from searched videos id list and returns YoutubeVideoApiModel`() = runTest {
        mockWebServerScheduler.generateMockResponseFrom(VIDEO_LIST_RESPONSE_PATH)

        val searchedVideoIdList: List<String> = API_RESPONSE_SEARCH_VIDEO_NO_CHANNEL_URL.items
            .map { searchedVideo: SearchVideoItemApiModel ->
            searchedVideo.id.videoId
        }

        val videoList = baseApiService.fetchParticularVideoList(searchedVideoIdList).body()
        API_VIDEO_RESPONSE_NO_CHANNEL_IMG_URL.items.assertListEqualsTo(videoList?.items)
    }
}
