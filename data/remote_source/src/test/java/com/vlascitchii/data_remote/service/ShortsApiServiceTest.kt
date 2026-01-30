package com.vlascitchii.data_remote.service

import com.vlascitchii.common_test.rule.DispatcherTestRule
import com.vlascitchii.common_test.util.assertListEqualsTo
import com.vlascitchii.data_remote.model_api.API_RESPONSE_SEARCH_VIDEO_NO_CHANNEL_URL
import com.vlascitchii.data_remote.networking.service.ShortsApiService
import com.vlascitchii.data_remote.util.MockWebServerApiProvider
import com.vlascitchii.data_remote.util.MockWebServerScheduler
import com.vlascitchii.data_remote.util.SEARCH_RESPONSE_PATH
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ShortsApiServiceTest {

    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    private lateinit var shortsApiService: ShortsApiService
    private lateinit var mockWebServerApiProvider: MockWebServerApiProvider
    private lateinit var mockWebServerScheduler: MockWebServerScheduler

    @Before
    fun init() {
        mockWebServerApiProvider = MockWebServerApiProvider()
        shortsApiService = mockWebServerApiProvider.provideMockShortsApiService()
        mockWebServerScheduler = mockWebServerApiProvider.mockWebServerScheduler
    }

    @After
    fun tearDown() {
        mockWebServerScheduler.shutDownMockWebServer()
    }

    @Test
    fun fetchShortsVideos() = runTest {
        mockWebServerScheduler.generateMockResponseFrom(SEARCH_RESPONSE_PATH)

        val shorts = shortsApiService.fetchShorts().body()!!

        API_RESPONSE_SEARCH_VIDEO_NO_CHANNEL_URL.items.assertListEqualsTo(shorts.items)
        assertEquals(API_RESPONSE_SEARCH_VIDEO_NO_CHANNEL_URL.nextPageToken, shorts.nextPageToken)
        assertEquals(API_RESPONSE_SEARCH_VIDEO_NO_CHANNEL_URL.prevPageToken, shorts.prevPageToken)
    }
}
