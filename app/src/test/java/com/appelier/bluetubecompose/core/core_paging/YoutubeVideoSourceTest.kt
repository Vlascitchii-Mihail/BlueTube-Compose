package com.appelier.bluetubecompose.core.core_paging

import androidx.paging.PagingSource
import com.appelier.bluetubecompose.core.core_api.VideoApiService
import com.appelier.bluetubecompose.core.core_database.YouTubeVideoDao
import com.appelier.bluetubecompose.screen_video_list.model.single_cnannel.YoutubeChannelResponse.Companion.DEFAULT_YOUTUBE_CHANNEL_RESPONSE_LIST
import com.appelier.bluetubecompose.screen_video_list.model.videos.ParticularVideo
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideo
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideoResponse.Companion.DEFAULT_VIDEO_RESPONSE
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideoResponse.Companion.DEFAULT_VIDEO_RESPONSE_WITH_CHANNEL_IMG
import com.appelier.bluetubecompose.search_video.model.DEFAULT_SEARCH_VIDEO_RESPONSE
import com.appelier.bluetubecompose.search_video.model.SearchVideoItem
import com.appelier.bluetubecompose.utils.VideoType
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.eq
import org.mockito.kotlin.spy
import org.mockito.kotlin.whenever
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class YoutubeVideoSourceTest {

    @Mock
    private lateinit var apiService: VideoApiService
    private val standardTestDispatcher = StandardTestDispatcher()
    private val testCoroutineScope = TestScope(standardTestDispatcher)
    private val youTubeVideoDao: YouTubeVideoDao = mock()
    private lateinit var youtubeVideoSource: YoutubeVideoSource

    @Before
    fun initSource() {
        youtubeVideoSource = YoutubeVideoSource(
            apiService,
            testCoroutineScope,
            VideoType.Videos,
            youTubeVideoDao
        )
    }

    @Test
    fun `fetchVideos() adds channel image URL and subscription count to a video`() {
        testCoroutineScope.runTest {
            whenever(
                apiService.fetchVideos(anyString(), anyString(), anyString(), anyString())
            ).thenReturn(Response.success(DEFAULT_VIDEO_RESPONSE))

            stubFetchChannels(DEFAULT_VIDEO_RESPONSE.items)

            val videos = apiService.fetchVideos("","","","").body()
            with(youtubeVideoSource) {
                videos?.items.addChannelImgUrl()
            }

            assertEquals(DEFAULT_VIDEO_RESPONSE_WITH_CHANNEL_IMG, videos)
        }
    }

    private suspend fun stubFetchChannels(videos: List<YoutubeVideo>) {
        for (index in videos.indices) {
            whenever(apiService.fetchChannels(eq(videos[index].snippet.channelId), anyString(), anyInt()))
                .thenReturn(Response.success(DEFAULT_YOUTUBE_CHANNEL_RESPONSE_LIST[index]))
        }
    }


    @Test
    fun `PagingSource returns list of video review on refresh or append state`() {
        val spyYoutubeVideoSource = spy(youtubeVideoSource)

        testCoroutineScope.runTest {
            doReturn(DEFAULT_VIDEO_RESPONSE_WITH_CHANNEL_IMG)
                .whenever(spyYoutubeVideoSource).getLoadData(eq(""), eq(VideoType.Videos))

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
                data = DEFAULT_VIDEO_RESPONSE_WITH_CHANNEL_IMG.items,
                nextKey = DEFAULT_VIDEO_RESPONSE_WITH_CHANNEL_IMG.nextPageToken,
                prevKey = DEFAULT_VIDEO_RESPONSE_WITH_CHANNEL_IMG.prevPageToken
            )

            testRefreshResult(expectedLoadResult, actualLoadRefreshResult)
            testRefreshResult(expectedLoadResult, actualLoadAppendResult)
        }
    }

    private fun testRefreshResult(
        expectedLoadResult: PagingSource.LoadResult.Page<String, YoutubeVideo>,
        actualLoadRefreshResult: PagingSource.LoadResult<String, YoutubeVideo>
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
            var convertedVideoList: List<YoutubeVideo>
            whenever(
                apiService.searchVideo(anyString(), anyString(), anyString(), anyString())
            ).thenReturn(Response.success(DEFAULT_SEARCH_VIDEO_RESPONSE))

            val searchedVideos = apiService.searchVideo(anyString(), anyString(), anyString(), anyString()).body()!!
            searchedVideos.items.prepareConvertAnswers()

            with(youtubeVideoSource) {
                convertedVideoList = searchedVideos.items.convertToVideosList()

            }

            assertEquals(DEFAULT_VIDEO_RESPONSE.items, convertedVideoList)
        }
    }

    private suspend fun List<SearchVideoItem>.prepareConvertAnswers() {
        for (index in this.indices) {
            whenever(
                apiService.fetchParticularVideo(this@prepareConvertAnswers[index].id.videoId)
            ).thenReturn(Response.success(ParticularVideo(listOf(DEFAULT_VIDEO_RESPONSE.items[index]))))
        }
    }
}