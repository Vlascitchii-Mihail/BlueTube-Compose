package com.vlascitchii.data_remote.source

import com.vlascitchii.common_test.rule.DispatcherTestRule
import com.vlascitchii.common_test.util.assertListEqualsTo
import com.vlascitchii.data_remote.model_api.API_RESPONSE_SEARCH_VIDEO_NO_CHANNEL_URL
import com.vlascitchii.data_remote.model_api.DOMAIN_RESPONSE_VIDEO_WITH_CHANNEL_IMG
import com.vlascitchii.data_remote.networking.service.CONTENT_VIDEO_TYPE
import com.vlascitchii.data_remote.networking.service.RELEVANCE
import com.vlascitchii.data_remote.networking.service.SHORTS
import com.vlascitchii.data_remote.networking.service.SHORTS_VIDEO_DURATION
import com.vlascitchii.data_remote.networking.service.SNIPPET
import com.vlascitchii.data_remote.networking.service.US_REGION_CODE
import com.vlascitchii.data_remote.networking.service.VideoListApiService
import com.vlascitchii.data_remote.util.CHANNEL_RESPONSE_PATH
import com.vlascitchii.data_remote.util.ERROR_CODE
import com.vlascitchii.data_remote.util.MockWebServerApiProvider
import com.vlascitchii.data_remote.util.MockWebServerScheduler
import com.vlascitchii.data_remote.util.SEARCH_RESPONSE_PATH
import com.vlascitchii.data_remote.util.VIDEO_LIST_RESPONSE_PATH
import com.vlascitchii.domain.model.videos.YoutubeVideoResponseDomain
import com.vlascitchii.domain.util.UseCaseException
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.spy
import org.mockito.kotlin.verify
import kotlin.test.Test
import kotlin.test.assertEquals

@RunWith(MockitoJUnitRunner::class)
class RemoteSearchVideoDataSourceImplTest {

    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    private lateinit var mockWebServerApiProvider: MockWebServerApiProvider
    private lateinit var mockSearchApiService: VideoListApiService
    private lateinit var mockWebServerScheduler: MockWebServerScheduler
    private lateinit var remoteSearchDataSource: RemoteSearchVideoDataSourceImpl
    private val testQuery = ""
    private val testPageToken = ""

    @Before
    fun init() {
        mockWebServerApiProvider = MockWebServerApiProvider()
        mockSearchApiService = spy(mockWebServerApiProvider.provideMockVideoListApiService())
        mockWebServerScheduler = mockWebServerApiProvider.mockWebServerScheduler
        remoteSearchDataSource = RemoteSearchVideoDataSourceImpl(
            mockSearchApiService,
            MockWebServerApiProvider.staticMoshiParser
        )
    }

    @After
    fun tearDown() {
        mockWebServerScheduler.shutDownMockWebServer()
    }

    private fun initMockApiResponse() {
        mockWebServerScheduler.generateMockResponseFrom(SEARCH_RESPONSE_PATH)
        mockWebServerScheduler.generateMockResponseFrom(VIDEO_LIST_RESPONSE_PATH)
        mockWebServerScheduler.generateMockResponseFrom(CHANNEL_RESPONSE_PATH)
    }

    @Test
    fun `fetchShorts() returns YoutubeVideoResponseDomain`() = runTest {
        initMockApiResponse()

        val actualResult: YoutubeVideoResponseDomain =
            remoteSearchDataSource.searchVideos(query = testQuery, nextPageToken = testPageToken)

        DOMAIN_RESPONSE_VIDEO_WITH_CHANNEL_IMG.items.assertListEqualsTo(actualResult.items)
        assertEquals(
            DOMAIN_RESPONSE_VIDEO_WITH_CHANNEL_IMG.nextPageToken,
            actualResult.nextPageToken
        )
        assertEquals(
            DOMAIN_RESPONSE_VIDEO_WITH_CHANNEL_IMG.prevPageToken,
            actualResult.prevPageToken
        )
    }

    @Test(expected = UseCaseException.VideoListLoadException::class)
    fun `fetchShorts() throws UseCaseException VideoListLoadException`() = runTest {
        mockWebServerScheduler.generateMockResponseFrom("", ERROR_CODE)

        remoteSearchDataSource.searchVideos(query = testQuery, nextPageToken = testPageToken)
    }

    @Test
    fun `searchRelatedVideos() deletes the first same video`() = runTest {
        val expectedListSize = 4

        with(remoteSearchDataSource) {
            assertEquals(
                expectedListSize,
                API_RESPONSE_SEARCH_VIDEO_NO_CHANNEL_URL.deleteFirstSameVideo().items.size
            )
        }
    }

    @Test
    fun `verify VideoListApiService searchVideos() was called with correct arguments`() = runTest {
        mockWebServerScheduler.generateMockResponseFrom(SEARCH_RESPONSE_PATH)
        mockWebServerScheduler.generateMockResponseFrom(VIDEO_LIST_RESPONSE_PATH)
        mockWebServerScheduler.generateMockResponseFrom(CHANNEL_RESPONSE_PATH)

        remoteSearchDataSource.searchVideos(query = testQuery, nextPageToken = testPageToken)

        verify(mockSearchApiService).searchVideo(
            query = testQuery,
            part = SNIPPET,
            order = RELEVANCE,
            nextPageToken = testPageToken
        )
    }
}
