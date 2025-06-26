package com.vlascitchii.data_remote.networking.service

import com.vlascitchii.common_test.rule.DispatcherTestRule
import com.vlascitchii.common_test.util.assertListEqualsTo
import com.vlascitchii.data_remote.enetity_api_model.video_channel_api_model.ChannelApiModel
import com.vlascitchii.data_remote.enetity_api_model.video_channel_api_model.ChannelApiModel.Companion.DEFAULT_CHANNEL_1
import com.vlascitchii.data_remote.util.CHANNEL_RESPONSE_1_PATH
import com.vlascitchii.data_remote.util.MockWebServerApiProvider
import com.vlascitchii.data_remote.util.MockWebServerScheduler
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
        mockWebServerScheduler.generateMockResponseFrom(CHANNEL_RESPONSE_1_PATH)

        val fetchedChannel = baseApiService.fetchChannels(DEFAULT_CHANNEL_1.id).body()

        ChannelApiModel.channels.assertListEqualsTo(fetchedChannel?.items)
    }

    @Test
    fun fetchChannelsReturnsChannelsById2() = runTest {
        mockWebServerScheduler.generateMockResponseFrom(CHANNEL_RESPONSE_1_PATH)

        val fetchedChannel = baseApiService.fetchChannels(DEFAULT_CHANNEL_1.id).body()

        ChannelApiModel.channels.assertListEqualsTo(fetchedChannel?.items)
    }
}
