package com.vlascitchii.data_remote.networking.service

import com.vlascitchii.data_remote.enetity_api_model.video_search_api_model.DEFAULT_SEARCH_VIDEO_RESPONSE
import com.vlascitchii.data_remote.rule.DispatcherTestRule
import com.vlascitchii.data_remote.util.MockWebServerApiProvider
import com.vlascitchii.data_remote.util.MockWebServerScheduler
import com.vlascitchii.data_remote.util.SEARCH_RESPONSE_PATH
import com.vlascitchii.data_remote.util.assertListEqualsTo
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

        DEFAULT_SEARCH_VIDEO_RESPONSE.items.assertListEqualsTo(shorts.items)
        assertEquals(DEFAULT_SEARCH_VIDEO_RESPONSE.nextPageToken, shorts.nextPageToken)
        assertEquals(DEFAULT_SEARCH_VIDEO_RESPONSE.prevPageToken, shorts.prevPageToken)
    }
}