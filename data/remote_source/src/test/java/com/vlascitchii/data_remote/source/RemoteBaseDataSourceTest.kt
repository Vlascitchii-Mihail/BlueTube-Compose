package com.vlascitchii.data_remote.source

import com.vlascitchii.common_test.rule.DispatcherTestRule
import com.vlascitchii.common_test.util.assertListEqualsTo
import com.vlascitchii.data_remote.model_api.API_ERROR_YOUTUBE_RESPONSE_INSTANCE
import com.vlascitchii.data_remote.model_api.API_RESPONSE_SEARCH_VIDEO_NO_CHANNEL_URL
import com.vlascitchii.data_remote.model_api.API_RESPONSE_VIDEO_WITH_CHANNEL_IMG
import com.vlascitchii.data_remote.model_api.API_VIDEO_RESPONSE_NO_CHANNEL_IMG_URL
import com.vlascitchii.data_remote.model_api.CHANNELS
import com.vlascitchii.data_remote.model_api.CHANNELS_LIST_ID
import com.vlascitchii.data_remote.model_api.DOMAIN_RESPONSE_VIDEO_WITH_CHANNEL_IMG
import com.vlascitchii.data_remote.model_api.channel_api_model.YoutubeChannelResponseApiModel
import com.vlascitchii.data_remote.model_api.error.convertErrorApiYouTubeResponseToErrorDomainYouTubeResponse
import com.vlascitchii.data_remote.model_api.video_api_model.YoutubeVideoApiModel
import com.vlascitchii.data_remote.model_api.video_api_model.YoutubeVideoResponseApiModel
import com.vlascitchii.data_remote.model_api.video_api_model.convertToDomainYouTubeVideoResponse
import com.vlascitchii.data_remote.networking.service.BaseApiService
import com.vlascitchii.data_remote.util.CHANNEL_RESPONSE_PATH
import com.vlascitchii.data_remote.util.ERROR_CODE
import com.vlascitchii.data_remote.util.ERROR_YOUTUBE_RESPONSE
import com.vlascitchii.data_remote.util.MockWebServerApiProvider
import com.vlascitchii.data_remote.util.MockWebServerScheduler
import com.vlascitchii.data_remote.util.VIDEO_LIST_FROM_SEARCH_VIDEO_LIST_RESPONSE
import com.vlascitchii.data_remote.util.VIDEO_LIST_RESPONSE_PATH
import com.vlascitchii.domain.model.ErrorDomain
import com.vlascitchii.domain.model.videos.YoutubeVideoResponseDomain
import com.vlascitchii.domain.util.UseCaseException
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import retrofit2.HttpException
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class RemoteBaseDataSourceTest {

    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    private lateinit var mockWebServerApiProvider: MockWebServerApiProvider
    private lateinit var baseApiService: BaseApiService
    private lateinit var mockWebServerScheduler: MockWebServerScheduler
    private lateinit var remoteBaseVideoDataSource: RemoteBaseVideoDataSource<YoutubeVideoResponseApiModel>

    private val mockRetrofitResponse: Response<YoutubeVideoResponseApiModel> = mock()
    private val errorJsonAsString: String = MockWebServerApiProvider.staticJsonHandler
        .readJsonAsStringFromPath(ERROR_YOUTUBE_RESPONSE)
    private val mockErrorResponseBody: ResponseBody = errorJsonAsString.toResponseBody(
        "application/json".toMediaType()
    )

    @Before
    fun init() {
        mockWebServerApiProvider = MockWebServerApiProvider()
        baseApiService = mockWebServerApiProvider.provideMockBaseApiService()
        mockWebServerScheduler = mockWebServerApiProvider.mockWebServerScheduler

        remoteBaseVideoDataSource =
            object : RemoteBaseVideoDataSource<YoutubeVideoResponseApiModel>(
                baseApiService, MockWebServerApiProvider.staticMoshiParser
            ) {
                override fun checkResponseBodyItemsIsNoteEmpty(responseBody: YoutubeVideoResponseApiModel): Boolean {
                    return responseBody.items.isNotEmpty()
                }

                override suspend fun returnHandledVideoResult(successResponse: YoutubeVideoResponseApiModel): YoutubeVideoResponseDomain {
                    return fillChannelUrlFields(successResponse).convertToDomainYouTubeVideoResponse()
                }
            }
    }

    @After
    fun tearDown() {
        mockWebServerScheduler.shutDownMockWebServer()
    }

    private fun setNegativeResponseResult() {
        whenever(mockRetrofitResponse.body()).thenReturn(null)
        whenever(mockRetrofitResponse.isSuccessful).thenReturn(false)
        whenever(mockRetrofitResponse.errorBody()).thenReturn(mockErrorResponseBody)
    }

    @Test
    fun `fillChannelUrl() returns  YoutubeVideoResponseApiModel with channel image URL`() =
        runTest {
            mockWebServerScheduler.generateMockResponseFrom(CHANNEL_RESPONSE_PATH)

            val actualYouTubeVideo: YoutubeVideoResponseApiModel = remoteBaseVideoDataSource
                .fillChannelUrlFields(API_VIDEO_RESPONSE_NO_CHANNEL_IMG_URL)

            API_RESPONSE_VIDEO_WITH_CHANNEL_IMG.items.assertListEqualsTo(actualYouTubeVideo.items)
        }

    @Test
    fun `getChannelImgUrlList() returns a list with channels image URL`() = runTest {
        mockWebServerScheduler.generateMockResponseFrom(CHANNEL_RESPONSE_PATH)

        val actualChannelListURL: List<String> = remoteBaseVideoDataSource.getChannelImgUrlList(
            API_VIDEO_RESPONSE_NO_CHANNEL_IMG_URL.items
        )
        val expectedChannelListURL: List<String> =
            API_RESPONSE_VIDEO_WITH_CHANNEL_IMG.items.map { video -> video.snippet.channelImgUrl }

        expectedChannelListURL.assertListEqualsTo(actualChannelListURL)
    }

    @Test
    fun `sortChannelListLike() sorts the order of List ChannelApiModel`() {
        val expectedChannelIdList: List<String> = CHANNELS_LIST_ID.shuffled()
        val channelApiResponse = YoutubeChannelResponseApiModel(items = CHANNELS)

        with(remoteBaseVideoDataSource) {
            val newChannelApiResponse = channelApiResponse.sortChannelListLike(expectedChannelIdList)
            val actualCChannelIdList = newChannelApiResponse.items.map { it.id }

            expectedChannelIdList.assertListEqualsTo(actualCChannelIdList)
        }
    }

    @Test
    fun `addChannelUrl() gets new YouTubeVideoList with channel URL`() {
        val youTubeVideoList: YoutubeVideoResponseApiModel = API_VIDEO_RESPONSE_NO_CHANNEL_IMG_URL
        val channelUrlList: List<String> =
            API_RESPONSE_VIDEO_WITH_CHANNEL_IMG.items.map { video -> video.snippet.channelImgUrl }
        val expectedYouTubeVideoList: List<YoutubeVideoApiModel> =
            API_RESPONSE_VIDEO_WITH_CHANNEL_IMG.items

        with(remoteBaseVideoDataSource) {
            val actualVideoListResult = youTubeVideoList.addChannelUrl(channelUrlList).items

            actualVideoListResult.assertListEqualsTo(expectedYouTubeVideoList)
        }
    }

    @Test
    fun `convertToVideoResponseApiModel() converts SearchVideoList to YouTubeVideoList`() = runTest {
        mockWebServerScheduler.generateMockResponseFrom(VIDEO_LIST_FROM_SEARCH_VIDEO_LIST_RESPONSE)

        val expectedVideoList: List<YoutubeVideoApiModel> =
            API_VIDEO_RESPONSE_NO_CHANNEL_IMG_URL.items

        with(remoteBaseVideoDataSource) {
            val convertedVideoResponse: YoutubeVideoResponseApiModel =
                API_RESPONSE_SEARCH_VIDEO_NO_CHANNEL_URL.convertToVideoResponseApiModel()

            expectedVideoList.assertListEqualsTo(convertedVideoResponse.items)
            assertEquals(API_VIDEO_RESPONSE_NO_CHANNEL_IMG_URL.prevPageToken, convertedVideoResponse.prevPageToken)
            assertEquals(API_VIDEO_RESPONSE_NO_CHANNEL_IMG_URL.nextPageToken, convertedVideoResponse.nextPageToken)
        }
    }

    @Test(expected = HttpException::class)
    fun `negative getVideoOnSuccessOrThrowHttpExceptionOnError() throws error if Retrofit response is not success`() =
        runTest {
            mockWebServerScheduler.generateMockResponseFrom(VIDEO_LIST_RESPONSE_PATH, ERROR_CODE)

            val retrofitResponse = baseApiService.fetchParticularVideoList(emptyList())

            remoteBaseVideoDataSource.getDataOnSuccessOrThrowHttpExceptionOnError(retrofitResponse)
        }

    @Test
    fun `positive getVideoOnSuccessOrThrowHttpExceptionOnError returns data if Retrofit response is success`() =
        runTest {
            mockWebServerScheduler.generateMockResponseFrom(VIDEO_LIST_RESPONSE_PATH)

            val retrofitResponse: Response<YoutubeVideoResponseApiModel> =
                baseApiService.fetchParticularVideoList(emptyList())
            val youTubeResponse: YoutubeVideoResponseApiModel = remoteBaseVideoDataSource
                .getDataOnSuccessOrThrowHttpExceptionOnError(retrofitResponse)

            API_VIDEO_RESPONSE_NO_CHANNEL_IMG_URL.items.assertListEqualsTo(youTubeResponse.items)
        }

    @Test
    fun `positive getYouTubeDomainErrorFromErrorBody() converts errorBody to ErrorApiYouTubeResponse instance`() {
        setNegativeResponseResult()

        try {
            remoteBaseVideoDataSource
                .getDataOnSuccessOrThrowHttpExceptionOnError(mockRetrofitResponse)
            throw AssertionError("getDataOnSuccessOrThrowHttpExceptionOnError() should throw HttpException")
        } catch (exception: HttpException) {

            val expectedErrorResult: ErrorDomain = API_ERROR_YOUTUBE_RESPONSE_INSTANCE
                .convertErrorApiYouTubeResponseToErrorDomainYouTubeResponse()
            val actualErrorResult: ErrorDomain? = remoteBaseVideoDataSource
                .getYouTubeDomainErrorFromErrorBody()

            assertEquals(expectedErrorResult, actualErrorResult)
        }
    }

    @Test
    fun `getYouTubeDomainErrorFromErrorBody() returns null on Retrofit success`() {
        val actualErrorResult: ErrorDomain? = remoteBaseVideoDataSource
            .getYouTubeDomainErrorFromErrorBody()

        assertNull(actualErrorResult)
    }

    @Test
    fun `positive fetch() returns YoutubeVideoResponseDomain`() = runTest {
        mockWebServerScheduler.generateMockResponseFrom(VIDEO_LIST_RESPONSE_PATH)
        mockWebServerScheduler.generateMockResponseFrom(CHANNEL_RESPONSE_PATH)

        val expectedResult: YoutubeVideoResponseDomain = DOMAIN_RESPONSE_VIDEO_WITH_CHANNEL_IMG

        val actualResult: YoutubeVideoResponseDomain =
            remoteBaseVideoDataSource.fetch { baseApiService.fetchParticularVideoList(emptyList()) }

        expectedResult.items.assertListEqualsTo(actualResult.items)
    }

    @Test(expected = UseCaseException::class)
    fun `negative fetch() throws UseCaseException`() = runTest {
        mockWebServerScheduler.generateMockResponseFrom(VIDEO_LIST_RESPONSE_PATH, ERROR_CODE)

        remoteBaseVideoDataSource.fetch { baseApiService.fetchParticularVideoList(emptyList()) }
    }
}
