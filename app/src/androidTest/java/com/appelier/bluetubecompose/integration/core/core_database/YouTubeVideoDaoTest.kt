package com.appelier.bluetubecompose.integration.core.core_database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.appelier.bluetubecompose.core.core_database.YouTubeVideoDao
import com.appelier.bluetubecompose.core.core_database.utils.DatabaseContentManager
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideoResponse.Companion.DEFAULT_VIDEO_RESPONSE_WITH_CHANNEL_IMG
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideoResponse.Companion.INITIAL_PAGE_TOKEN
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideoResponse.Companion.TEST_DATABASE_VIDEO_LIST
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideoResponse.Companion.TEST_DATABASE_VIDEO_RESPONSE
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideoResponse.Companion.testDateTime
import com.appelier.bluetubecompose.utils.convertToYoutubeVideoResponse
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class YouTubeVideoDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var youTubeVideoDao: YouTubeVideoDao
    private val standardTestDispatcher = StandardTestDispatcher()
    private val testCoroutineScope = TestScope(standardTestDispatcher)
    private lateinit var databaseContentManager: DatabaseContentManager

    @Before
    fun setup() {
        hiltRule.inject()
        databaseContentManager = DatabaseContentManager(youTubeVideoDao)
    }

    @Test
    fun checkInsertedVideosEqualsToSourceVideoList() {
        testCoroutineScope.runTest {
            insertVideoPageToDatabase()

            val pageWithVideos = youTubeVideoDao.getVideosFromPage(INITIAL_PAGE_TOKEN)
            val databaseVideoResponse = pageWithVideos.convertToYoutubeVideoResponse()

            assertEquals(TEST_DATABASE_VIDEO_RESPONSE, databaseVideoResponse)
        }
    }

    @Test
    fun checkAddedToDatabaseAmountOfVideosEqualsToSourceVideoListSize() {
        testCoroutineScope.runTest {
            insertVideoPageToDatabase()

            val videoCount = youTubeVideoDao.getVideosCount()
            assertEquals(TEST_DATABASE_VIDEO_LIST.size, videoCount)
        }
    }

    private suspend fun insertVideoPageToDatabase() {
        databaseContentManager.setCurrentPageTokenToVideos(
            INITIAL_PAGE_TOKEN,
            DEFAULT_VIDEO_RESPONSE_WITH_CHANNEL_IMG
        )
        databaseContentManager.insertVideosToDatabaseWithTimeStamp(
            testDateTime,
            DEFAULT_VIDEO_RESPONSE_WITH_CHANNEL_IMG
        )
    }
}