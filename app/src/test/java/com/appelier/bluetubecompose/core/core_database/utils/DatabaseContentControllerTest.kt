package com.appelier.bluetubecompose.core.core_database.utils

import com.appelier.bluetubecompose.core.core_database.YouTubeVideoDao
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideoResponse
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideoResponse.Companion.DEFAULT_VIDEO_RESPONSE_WITH_CHANNEL_IMG
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideoResponse.Companion.INITIAL_PAGE
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideoResponse.Companion.RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideoResponse.Companion.TEST_DATABASE_VIDEO_LIST
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideoResponse.Companion.TEST_DATABASE_VIDEO_RESPONSE
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DatabaseContentControllerTest {

    @Mock
    private lateinit var youTubeDao: YouTubeVideoDao
    private lateinit var databaseContentController: DatabaseContentController

    @Before
    fun init() {
        databaseContentController = DatabaseContentController(youTubeDao)
    }

    @Test
    fun `check setting page token to VideoResponse`() {
        var videoResponse: YoutubeVideoResponse
        with(databaseContentController) {

            //creating a new object doesn't affect to the next tests
            videoResponse = DEFAULT_VIDEO_RESPONSE_WITH_CHANNEL_IMG.copy()
            videoResponse.setPageTokenToVideos(INITIAL_PAGE)
        }

        assertEquals(TEST_DATABASE_VIDEO_RESPONSE.currentPageToken, videoResponse.currentPageToken)
        for (index in RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG.indices) {
            assertEquals(TEST_DATABASE_VIDEO_LIST[index].pageToken, RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG[index].pageToken)
        }
    }
}