package com.vlascitchii.data_remote.source

import com.vlascitchii.common_test.rule.DispatcherTestRule
import com.vlascitchii.data_remote.networking.service.VideoListApiService
import com.vlascitchii.data_remote.util.CHANNEL_RESPONSE_1_PATH
import com.vlascitchii.data_remote.util.CHANNEL_RESPONSE_2_PATH
import com.vlascitchii.data_remote.util.CHANNEL_RESPONSE_3_PATH
import com.vlascitchii.data_remote.util.CHANNEL_RESPONSE_4_PATH
import com.vlascitchii.data_remote.util.CHANNEL_RESPONSE_5_PATH
import com.vlascitchii.data_remote.util.MockWebServerApiProvider
import com.vlascitchii.data_remote.util.MockWebServerScheduler
import com.vlascitchii.data_remote.util.VIDEO_LIST_RESPONSE_PATH
import com.vlascitchii.data_repository.data_source.remote.RemoteVideoListDataSource
import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideoResponse
import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideoResponse.Companion.RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG
import com.vlascitchii.domain.util.UseCaseException
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.spy

@RunWith(MockitoJUnitRunner::class)
class RemoteVideoListDataSourceImplTest {

    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    private lateinit var mockWebServerApiProvider: MockWebServerApiProvider
    private lateinit var videoListApiServiceMock: VideoListApiService
    private lateinit var remoteVideoListDataSource: RemoteVideoListDataSource
    private lateinit var mockWebServerScheduler: MockWebServerScheduler

    @Before
    fun init() {
        mockWebServerApiProvider = MockWebServerApiProvider()
        videoListApiServiceMock = spy(mockWebServerApiProvider.provideMockVideoListApiService())
        remoteVideoListDataSource = RemoteVideoListDataSourceImpl(videoListApiServiceMock)
        mockWebServerScheduler = mockWebServerApiProvider.mockWebServerScheduler
    }

    private fun initMockWebResponse() {
        val mockResponseList = listOf(
            VIDEO_LIST_RESPONSE_PATH,
            CHANNEL_RESPONSE_1_PATH,
            CHANNEL_RESPONSE_2_PATH,
            CHANNEL_RESPONSE_3_PATH,
            CHANNEL_RESPONSE_4_PATH,
            CHANNEL_RESPONSE_5_PATH
        )

        mockResponseList.forEach { path: String ->
            mockWebServerScheduler.generateMockResponseFrom(path)
        }
    }

    @Test
    fun `fetchVideos() returns YoutubeVideo List with proper ID`() = runTest {
        initMockWebResponse()
        val response: YoutubeVideoResponse = remoteVideoListDataSource.fetchVideos("").first()

        val expectedVideoIdList = RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG.items.map { video -> video.id }
        val actualVideoIdList = response.items.map { video -> video.id }

        assertTrue(expectedVideoIdList.containsAll(actualVideoIdList))
    }

    @Test
    fun `fetchVideos() returns YoutubeVideo List with proper UrlID`() = runTest {
        initMockWebResponse()
        val response: YoutubeVideoResponse = remoteVideoListDataSource.fetchVideos("").first()

        val expectedChannelURLList = RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG.items.map { video -> video.snippet.channelImgUrl }
        val actualChannelURLList = response.items.map { video -> video.snippet.channelImgUrl }

        assertTrue(expectedChannelURLList.containsAll(actualChannelURLList))
    }

    @Test
    fun `fun fetchVideos() returns UseCaseException`() = runTest {
        mockWebServerScheduler.enqueueError()

        remoteVideoListDataSource.fetchVideos("").catch { error: Throwable ->
            assertTrue(error is UseCaseException.VideoListLoadException)
        }.collect()
    }
}
