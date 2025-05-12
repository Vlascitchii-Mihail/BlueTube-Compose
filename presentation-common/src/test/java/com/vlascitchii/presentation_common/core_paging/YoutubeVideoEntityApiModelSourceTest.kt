package com.vlascitchii.presentation_common.core_paging

import androidx.paging.PagingSource
import com.vlascitchii.data_remote.networking.VideoApiService
import com.vlascitchii.data_remote.enetity_api_model.video_list_api_model.videos_api_model.ParticularVideoApiModel
import com.vlascitchii.data_local.enetity.video_list.videos.YoutubeVideoEntity
import com.vlascitchii.data_remote.enetity_api_model.video_list_api_model.videos_api_model.YoutubeVideoResponseApiModel.Companion.RESPONSE_VIDEO_LIST_NO_CHANNEL_IMG_URL
import com.vlascitchii.data_remote.enetity_api_model.video_list_api_model.videos_api_model.YoutubeVideoResponseApiModel.Companion.RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG
import com.appelier.bluetubecompose.search_video.model.DEFAULT_SEARCH_VIDEO_RESPONSE
import com.appelier.bluetubecompose.search_video.model.SearchVideoItem
import com.vlascitchii.presentation_common.utils.VideoType
import com.vlascitchii.presentation_common.enetity.video_list.core_paging.YoutubeVideoSource
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.eq
import org.mockito.kotlin.spy
import org.mockito.kotlin.whenever
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class YoutubeVideoEntityApiModelSourceTest {

    @Mock
    private lateinit var apiService: com.vlascitchii.data_remote.networking.VideoApiService
    private val standardTestDispatcher = StandardTestDispatcher()
    private val testCoroutineScope = TestScope(standardTestDispatcher)
    private val youTubeVideoDao: com.vlascitchii.data_local.database.YouTubeVideoDao = mock()
    private lateinit var youtubeVideoSource: com.vlascitchii.presentation_common.enetity.video_list.core_paging.YoutubeVideoSource

    @Before
    fun initSource() {
        youtubeVideoSource =
            com.vlascitchii.presentation_common.enetity.video_list.core_paging.YoutubeVideoSource(
                apiService,
                testCoroutineScope,
                com.vlascitchii.presentation_common.utils.VideoType.Videos,
                youTubeVideoDao
            )
    }
//
//    @Test
//    fun `fetchVideos() adds channel image URL to a video`() {
//        testCoroutineScope.runTest {
//
//            //you can enqueue 2 OR MORE calls with MockWebServer
//            whenever(
//                apiService.fetchVideos(anyString(), anyString(), anyString(), anyString())
//            ).thenReturn(Response.success(RESPONSE_VIDEO_LIST_NO_CHANNEL_IMG_URL))
//
//            stubFetchChannels(RESPONSE_VIDEO_LIST_NO_CHANNEL_IMG_URL.items)
//
//            val videos = apiService.fetchVideos().body()
//            videos?.items?.let { youtubeVideoSource.addChannelImgUrl(it) }
//
//            assertEquals(RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG, videos)
//        }
//    }
//
//    private suspend fun stubFetchChannels(videos: List<YoutubeVideoEntity>) {
//        for (index in videos.indices) {
//            whenever(
//                apiService.fetchChannels(
//                    eq(videos[index].snippet.channelId),
//                    anyString(),
//                    anyInt()
//                )
//            )
//                .thenReturn(Response.success(DEFAULT_YOUTUBE_CHANNEL_RESPONSE_LIST[index]))
//        }
//    }


    @Test
    fun `PagingSource returns list of video review on refresh or append state`() {
        val spyYoutubeVideoSource = spy(youtubeVideoSource)

        testCoroutineScope.runTest {
            doReturn(RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG)
                .whenever(spyYoutubeVideoSource).getLoadData(eq(""), eq(com.vlascitchii.presentation_common.utils.VideoType.Videos))

            val refreshLoadParams = PagingSource.LoadParams.Refresh(
                key = "",
                loadSize = 5,
                placeholdersEnabled = false
            )

            val appendLoadParams = PagingSource.LoadParams.Append(
                key = "",
                loadSize = 5,
                placeholdersEnabled = false
            )

            val actualLoadRefreshResult = spyYoutubeVideoSource.load(refreshLoadParams)
            val actualLoadAppendResult = spyYoutubeVideoSource.load(appendLoadParams)

            val expectedLoadResult = PagingSource.LoadResult.Page(
                data = RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG.items,
                nextKey = RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG.nextPageToken,
                prevKey = RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG.prevPageToken
            )

            testRefreshResult(expectedLoadResult, actualLoadRefreshResult)
            testRefreshResult(expectedLoadResult, actualLoadAppendResult)
        }
    }

    private fun testRefreshResult(
        expectedLoadResult: PagingSource.LoadResult.Page<String, YoutubeVideoEntity>,
        actualLoadRefreshResult: PagingSource.LoadResult<String, YoutubeVideoEntity>
    ) {
        assertTrue(actualLoadRefreshResult is PagingSource.LoadResult.Page)
        assertEquals(
            expectedLoadResult.prevKey,
            (actualLoadRefreshResult as PagingSource.LoadResult.Page).prevKey
        )
        assertEquals(expectedLoadResult.nextKey, actualLoadRefreshResult.nextKey)
        assertEquals(expectedLoadResult.data, actualLoadRefreshResult.data)
    }

    @Test
    fun `Paging source converts SearchVideoItem to YoutubeVideo`() {
        testCoroutineScope.runTest {
            whenever(
                apiService.searchVideo()
            ).thenReturn(Response.success(DEFAULT_SEARCH_VIDEO_RESPONSE))

            val searchedVideos = apiService.searchVideo().body() ?: DEFAULT_SEARCH_VIDEO_RESPONSE
            searchedVideos.items.mockConvertToVideoList()

            val convertedVideoList =
                youtubeVideoSource.convertSearchVideoToVideosList(searchedVideos.items)

            assertEquals(RESPONSE_VIDEO_LIST_NO_CHANNEL_IMG_URL.items, convertedVideoList)
        }
    }

    private suspend fun List<SearchVideoItem>.mockConvertToVideoList() {
        for (index in this.indices) {
            whenever(
                apiService.fetchParticularVideo(this@mockConvertToVideoList[index].id.videoId)
            ).thenReturn(Response.success(ParticularVideoApiModel(listOf(RESPONSE_VIDEO_LIST_NO_CHANNEL_IMG_URL.items[index]))))
        }
    }
}