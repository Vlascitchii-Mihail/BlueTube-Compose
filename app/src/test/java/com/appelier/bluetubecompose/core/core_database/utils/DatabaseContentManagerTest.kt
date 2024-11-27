package com.appelier.bluetubecompose.core.core_database.utils

import com.appelier.bluetubecompose.core.core_database.YouTubeVideoDao
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideoResponse
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideoResponse.Companion.DEFAULT_VIDEO_RESPONSE_WITH_CHANNEL_IMG
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideoResponse.Companion.INITIAL_PAGE_TOKEN
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
class DatabaseContentManagerTest {

    @Mock
    private lateinit var youTubeDao: YouTubeVideoDao
    private lateinit var databaseContentManager: DatabaseContentManager

    @Before
    fun init() {
        databaseContentManager = DatabaseContentManager(youTubeDao)
    }

    @Test
    fun `check setCurrentPageTokenToVideos() to VideoResponse`() {
        val videoResponse =
            databaseContentManager.setCurrentPageTokenToVideos(
                INITIAL_PAGE_TOKEN,
                DEFAULT_VIDEO_RESPONSE_WITH_CHANNEL_IMG
            )

        assertEquals(
            TEST_DATABASE_VIDEO_RESPONSE.currentPageToken,
            videoResponse.currentPageToken
        )

        for (index in RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG.indices) {
            assertEquals(
                TEST_DATABASE_VIDEO_LIST[index].pageToken,
                RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG[index].pageToken
            )
        }

        videoResponse.resetResponsePageToken()
    }

    private fun YoutubeVideoResponse.resetResponsePageToken() {
        databaseContentManager.setCurrentPageTokenToVideos("", this)
    }
}
