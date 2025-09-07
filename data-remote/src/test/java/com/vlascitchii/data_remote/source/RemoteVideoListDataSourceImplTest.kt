package com.vlascitchii.data_remote.source

import com.vlascitchii.common_test.rule.DispatcherTestRule
import com.vlascitchii.common_test.util.assertListEqualsTo
import com.vlascitchii.data_remote.model_api.DOMAIN_RESPONSE_VIDEO_WITH_CHANNEL_IMG
import com.vlascitchii.data_remote.networking.service.CONTENT_DETAILS
import com.vlascitchii.data_remote.networking.service.MOST_POPULAR
import com.vlascitchii.data_remote.networking.service.SNIPPET
import com.vlascitchii.data_remote.networking.service.STATISTICS
import com.vlascitchii.data_remote.networking.service.US_REGION_CODE
import com.vlascitchii.data_remote.networking.service.VideoListApiService
import com.vlascitchii.data_remote.util.CHANNEL_RESPONSE_PATH
import com.vlascitchii.data_remote.util.ERROR_CODE
import com.vlascitchii.data_remote.util.MockWebServerApiProvider
import com.vlascitchii.data_remote.util.MockWebServerScheduler
import com.vlascitchii.data_remote.util.VIDEO_LIST_RESPONSE_PATH
import com.vlascitchii.data_repository.data_source.remote.RemoteVideoListDataSource
import com.vlascitchii.domain.model.videos.YoutubeVideoResponseDomain
import com.vlascitchii.domain.util.UseCaseException
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.eq
import org.mockito.kotlin.spy
import org.mockito.kotlin.verify
import kotlin.test.Test

@RunWith(MockitoJUnitRunner::class)
class RemoteVideoListDataSourceImplTest {

    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    private lateinit var mockWebServerApiProvider: MockWebServerApiProvider
    private lateinit var videoListApiServiceMock: VideoListApiService
    private lateinit var mockWebServerScheduler: MockWebServerScheduler
    private lateinit var remoteVideoListDataSource: RemoteVideoListDataSource
    val testPageToken = ""

    @Before
    fun init() {
        mockWebServerApiProvider = MockWebServerApiProvider()
        videoListApiServiceMock = spy(mockWebServerApiProvider.provideMockVideoListApiService())
        mockWebServerScheduler = mockWebServerApiProvider.mockWebServerScheduler
        remoteVideoListDataSource = RemoteVideoListDataSourceImpl(
            videoListApiServiceMock,
            MockWebServerApiProvider.staticMoshiParser
        )
    }

    @After
    fun tearDown() {
        mockWebServerScheduler.shutDownMockWebServer()
    }

    private fun initMockApiResponse() {
        mockWebServerScheduler.generateMockResponseFrom(VIDEO_LIST_RESPONSE_PATH)
        mockWebServerScheduler.generateMockResponseFrom(CHANNEL_RESPONSE_PATH)
    }

    @Test
    fun `fetchVideos() returns YoutubeVideoResponseDomain`() = runTest {
        initMockApiResponse()

        val actualResult: YoutubeVideoResponseDomain = remoteVideoListDataSource.fetchVideos(testPageToken)

        DOMAIN_RESPONSE_VIDEO_WITH_CHANNEL_IMG.items.assertListEqualsTo(actualResult.items)
        assertEquals(DOMAIN_RESPONSE_VIDEO_WITH_CHANNEL_IMG.nextPageToken, actualResult.nextPageToken)
        assertEquals(DOMAIN_RESPONSE_VIDEO_WITH_CHANNEL_IMG.prevPageToken, actualResult.prevPageToken)
    }

    @Test(expected = UseCaseException.VideoListLoadException::class)
    fun `fetchVideos() throws UseCaseException VideoListLoadException`() = runTest {
        mockWebServerScheduler.generateMockResponseFrom("", ERROR_CODE)

        remoteVideoListDataSource.fetchVideos(testPageToken)
    }

    @Test
    fun `verify VideoListApiService fetchVideos() was called with correct arguments`() = runTest {
        initMockApiResponse()

        remoteVideoListDataSource.fetchVideos(testPageToken)

        verify(videoListApiServiceMock).fetchVideos(
            part = eq("$SNIPPET, $CONTENT_DETAILS, $STATISTICS"),
            chart = eq(MOST_POPULAR),
            regionCode = eq(US_REGION_CODE),
            nextPageToken = eq(testPageToken)
        )
    }
}
