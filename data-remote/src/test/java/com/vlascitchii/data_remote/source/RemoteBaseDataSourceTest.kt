package com.vlascitchii.data_remote.source

import com.vlascitchii.data_remote.enetity_api_model.util.convertToYouTubeVideoResponseApiModel
import com.vlascitchii.data_remote.enetity_api_model.util.convertToYoutubeVideoApiModelList
import com.vlascitchii.data_remote.enetity_api_model.video_channel_api_model.ChannelApiModel
import com.vlascitchii.data_remote.enetity_api_model.video_channel_api_model.YoutubeChannelResponseApiModel
import com.vlascitchii.data_remote.enetity_api_model.video_channel_api_model.YoutubeChannelResponseApiModel.Companion.DEFAULT_YOUTUBE_CHANNEL_RESPONSE_LIST
import com.vlascitchii.data_remote.networking.service.BaseApiService
import com.vlascitchii.data_remote.rule.DispatcherTestRule
import com.vlascitchii.data_remote.util.CHANNEL_RESPONSE_1_PATH
import com.vlascitchii.data_remote.util.CHANNEL_RESPONSE_2_PATH
import com.vlascitchii.data_remote.util.CHANNEL_RESPONSE_3_PATH
import com.vlascitchii.data_remote.util.CHANNEL_RESPONSE_4_PATH
import com.vlascitchii.data_remote.util.CHANNEL_RESPONSE_5_PATH
import com.vlascitchii.data_remote.util.MockWebServerApiProvider
import com.vlascitchii.data_remote.util.MockWebServerScheduler
import com.vlascitchii.data_remote.util.assertListEqualsTo
import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideoResponse.Companion.RESPONSE_VIDEO_LIST_NO_CHANNEL_IMG_URL
import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideoResponse.Companion.RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.HttpException
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@RunWith(MockitoJUnitRunner::class)
class RemoteBaseDataSourceTest {

    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    private lateinit var mockWebServerApiProvider: MockWebServerApiProvider
    private lateinit var baseApiService: BaseApiService
    private lateinit var mockWebServerScheduler: MockWebServerScheduler
    private lateinit var remoteBaseDataSource: RemoteBaseDataSource<YoutubeChannelResponseApiModel>

    @Before
    fun init() {
        mockWebServerApiProvider = MockWebServerApiProvider()
        baseApiService = mockWebServerApiProvider.provideMockBaseApiService()
        mockWebServerScheduler = mockWebServerApiProvider.mockWebServerScheduler
        remoteBaseDataSource = object : RemoteBaseDataSource<YoutubeChannelResponseApiModel>(baseApiService) {

            override fun checkResponseBodyItemsIsNoteEmpty(responseBody: YoutubeChannelResponseApiModel): Boolean {
                return responseBody.items.isNotEmpty()
            }
        }
    }

    @After
    fun tearDown() {
        mockWebServerScheduler.shutDownMockWebServer()
    }

    private fun initialiseMockResponse() {
        val channelImageURLPathList = listOf(
            CHANNEL_RESPONSE_1_PATH,
            CHANNEL_RESPONSE_2_PATH,
            CHANNEL_RESPONSE_3_PATH,
            CHANNEL_RESPONSE_4_PATH,
            CHANNEL_RESPONSE_5_PATH
        )
        channelImageURLPathList.forEach { path: String ->
            mockWebServerScheduler.generateMockResponseFrom(path)
        }
    }

    @Test
    fun `fillChannelUrl() fills with URL the videoList without URL`() = runTest {
        initialiseMockResponse()
        val actualYouTubeVideoList =
            RESPONSE_VIDEO_LIST_NO_CHANNEL_IMG_URL.convertToYouTubeVideoResponseApiModel().items
        val expectedYouTubeVideoList =
            RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG.convertToYouTubeVideoResponseApiModel().items.map { video -> video.snippet.channelImgUrl }

        with(remoteBaseDataSource) {
            actualYouTubeVideoList.fillChannelUrl()
        }

        assertTrue(expectedYouTubeVideoList.containsAll(actualYouTubeVideoList.map { video -> video.snippet.channelImgUrl }))
    }

    @Test
    fun `getChannelImgUrlList() returns a list with channels URL`() = runTest {
        initialiseMockResponse()
        val actualChannelListURL = remoteBaseDataSource.getChannelImgUrlList(
                RESPONSE_VIDEO_LIST_NO_CHANNEL_IMG_URL.items.convertToYoutubeVideoApiModelList()
        )
        val expectedChannelListURL =
            RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG.items.map { video -> video.snippet.channelImgUrl }

        assertTrue(expectedChannelListURL.containsAll(actualChannelListURL))
    }

    @Test
    fun `addChannelUrl() adds URLs to YouTubeVideoList`() {
        val youTubeVideoList =
            RESPONSE_VIDEO_LIST_NO_CHANNEL_IMG_URL.convertToYouTubeVideoResponseApiModel()
        val channelUrlList =
            RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG.items.map { video -> video.snippet.channelImgUrl }
        val expectedYouTubeVideoList =
            RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG.convertToYouTubeVideoResponseApiModel().items

        with(remoteBaseDataSource) {
            youTubeVideoList.items.addChannelUrl(channelUrlList)
        }

        youTubeVideoList.items.assertListEqualsTo(expectedYouTubeVideoList)
    }

    @Test
    fun `getVideoOnSuccessOrThrowHttpExceptionOnError throws error if Retrofit response is not success`() = runTest {
        mockWebServerScheduler.enqueueError()
        val retrofitResponse = baseApiService.fetchChannels("Test channel Id")

        assertFailsWith<HttpException> {
            remoteBaseDataSource.getDataOnSuccessOrThrowHttpExceptionOnError(retrofitResponse)
        }
    }

    @Test
    fun `getVideoOnSuccessOrThrowHttpExceptionOnError returns data if Retrofit response is success`() = runTest {
        initialiseMockResponse()

        val retrofitResponse = baseApiService.fetchChannels("Test channel Id")
        val youTubeChannels: YoutubeChannelResponseApiModel = remoteBaseDataSource.getDataOnSuccessOrThrowHttpExceptionOnError(retrofitResponse)

        assertEquals(youTubeChannels.items.first(), ChannelApiModel.channels.first())
    }
}
