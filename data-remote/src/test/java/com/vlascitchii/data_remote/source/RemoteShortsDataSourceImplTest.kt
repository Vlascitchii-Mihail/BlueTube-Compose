package com.vlascitchii.data_remote.source

import com.vlascitchii.data_remote.enetity_api_model.video_search_api_model.DEFAULT_SEARCH_VIDEO_RESPONSE
import com.vlascitchii.data_remote.enetity_api_model.video_search_api_model.DEFAULT_SEARCH_VIDEO_RESPONSE_WITH_CHANNEL_IMG_URL
import com.vlascitchii.data_remote.networking.service.ShortsApiService
import com.vlascitchii.data_remote.rule.DispatcherTestRule
import com.vlascitchii.data_remote.util.CHANNEL_RESPONSE_1_PATH
import com.vlascitchii.data_remote.util.CHANNEL_RESPONSE_2_PATH
import com.vlascitchii.data_remote.util.CHANNEL_RESPONSE_3_PATH
import com.vlascitchii.data_remote.util.CHANNEL_RESPONSE_4_PATH
import com.vlascitchii.data_remote.util.CHANNEL_RESPONSE_5_PATH
import com.vlascitchii.data_remote.util.MockWebServerApiProvider
import com.vlascitchii.data_remote.util.MockWebServerScheduler
import com.vlascitchii.data_remote.util.PARTICULAR_VIDEO_RESPONSE_1_PATH
import com.vlascitchii.data_remote.util.PARTICULAR_VIDEO_RESPONSE_2_PATH
import com.vlascitchii.data_remote.util.PARTICULAR_VIDEO_RESPONSE_3_PATH
import com.vlascitchii.data_remote.util.PARTICULAR_VIDEO_RESPONSE_4_PATH
import com.vlascitchii.data_remote.util.PARTICULAR_VIDEO_RESPONSE_5_PATH
import com.vlascitchii.data_remote.util.SEARCH_RESPONSE_PATH
import com.vlascitchii.data_repository.data_source.remote.RemoteShortsDataSource
import com.vlascitchii.domain.custom_coroutine_scopes.AppCoroutineScope
import com.vlascitchii.domain.custom_coroutine_scopes.VideoCoroutineScope
import com.vlascitchii.domain.util.UseCaseException
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RemoteShortsDataSourceImplTest {

    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()
    private val mockWebServerApiProvider: MockWebServerApiProvider = MockWebServerApiProvider()

    private lateinit var shortsApiService: ShortsApiService
    private lateinit var videoCoroutineScope: AppCoroutineScope
    private lateinit var remoteShortsDatsSource: RemoteShortsDataSource
    private lateinit var mockWebServerScheduler: MockWebServerScheduler

    @Before
    fun initResponse() {
        shortsApiService = mockWebServerApiProvider.provideMockShortsApiService()
        videoCoroutineScope = VideoCoroutineScope(dispatcher = dispatcherTestRule.testDispatcher)
        remoteShortsDatsSource = RemoteShortsDataSourceImpl(shortsApiService, videoCoroutineScope)
        mockWebServerScheduler = mockWebServerApiProvider.mockWebServerScheduler
    }

    private fun initMockWebResponse() {
        val mockResponseList = listOf(
            SEARCH_RESPONSE_PATH,
            PARTICULAR_VIDEO_RESPONSE_1_PATH,
            PARTICULAR_VIDEO_RESPONSE_2_PATH,
            PARTICULAR_VIDEO_RESPONSE_3_PATH,
            PARTICULAR_VIDEO_RESPONSE_4_PATH,
            PARTICULAR_VIDEO_RESPONSE_5_PATH,
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
    fun `fetchShorts() returns YoutubeVideo List with proper ID`() = runTest {
        initMockWebResponse()
        val shortsResponse = remoteShortsDatsSource.fetchShorts("").first()

        val expectedVideoIdList = DEFAULT_SEARCH_VIDEO_RESPONSE.items.map { video -> video.id.videoId }
        val actualVideoIdList = shortsResponse.items.map { video -> video.id }

        assertTrue(expectedVideoIdList.containsAll(actualVideoIdList))
    }

    @Test
    fun `fetchShorts() returns YoutubeVideo List with proper UrlID`() = runTest {
        initMockWebResponse()
        val shortsResponse = remoteShortsDatsSource.fetchShorts("").first()

        val expectedChannelURLList = DEFAULT_SEARCH_VIDEO_RESPONSE_WITH_CHANNEL_IMG_URL.items.map { video -> video.snippet.channelImgUrl }
        val actualChannelURLList = shortsResponse.items.map { video -> video.snippet.channelImgUrl }

        assertTrue(expectedChannelURLList.containsAll(actualChannelURLList))
    }

    @Test
    fun `fun fetchShorts() returns UseCaseException`() = runTest {
        mockWebServerScheduler.enqueueError()

        remoteShortsDatsSource.fetchShorts("Test page token").catch { error: Throwable ->
            assertTrue(error is UseCaseException.ShortsLoadException)
        }.collect()
    }
}