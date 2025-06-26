package com.vlascitchii.data_remote.networking.service

import com.vlascitchii.common_test.rule.DispatcherTestRule
import com.vlascitchii.common_test.util.assertListEqualsTo
import com.vlascitchii.data_remote.enetity_api_model.video_search_api_model.DEFAULT_SEARCH_VIDEO_RESPONSE
import com.vlascitchii.data_remote.util.MockWebServerApiProvider
import com.vlascitchii.data_remote.util.MockWebServerScheduler
import com.vlascitchii.data_remote.util.SEARCH_RESPONSE_PATH
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SearchApiServiceTest {

    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    private lateinit var searchApiService: SearchApiService
    private lateinit var mockWebServerApiProvider: MockWebServerApiProvider
    private lateinit var mockWebServerScheduler: MockWebServerScheduler

    @Before
    fun init() {
        mockWebServerApiProvider = MockWebServerApiProvider()
        searchApiService = mockWebServerApiProvider.provideMockSearchApiService()
        mockWebServerScheduler = mockWebServerApiProvider.mockWebServerScheduler
    }

    @After
    fun tearDown() {
        mockWebServerScheduler.shutDownMockWebServer()
    }

    @Test
    fun fetchSearchVideosReturnsSearchVideos() = runTest {
        mockWebServerScheduler.generateMockResponseFrom(SEARCH_RESPONSE_PATH)

        val searchedVideos = searchApiService.searchVideo(query = "steam deck").body()

        DEFAULT_SEARCH_VIDEO_RESPONSE.items.assertListEqualsTo(searchedVideos?.items)

        assertEquals(DEFAULT_SEARCH_VIDEO_RESPONSE.nextPageToken, searchedVideos?.nextPageToken)
        assertEquals(DEFAULT_SEARCH_VIDEO_RESPONSE.prevPageToken, searchedVideos?.prevPageToken)
    }

}