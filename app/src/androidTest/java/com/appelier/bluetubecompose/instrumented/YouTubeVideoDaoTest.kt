package com.appelier.bluetubecompose.instrumented

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.appelier.bluetubecompose.core.core_database.YouTubeVideoDao
import com.appelier.bluetubecompose.core.core_database.utils.DatabaseContentManager
import com.appelier.bluetubecompose.rule.DispatcherTestRule
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideoResponse
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideoResponse.Companion.DEFAULT_VIDEO_RESPONSE_WITH_CHANNEL_IMG
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideoResponse.Companion.INITIAL_PAGE_TOKEN
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideoResponse.Companion.TEST_DATABASE_VIDEO_LIST
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideoResponse.Companion.TEST_DATABASE_VIDEO_RESPONSE
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideoResponse.Companion.testDateTime
import com.appelier.bluetubecompose.utils.convertToYoutubeVideoResponse
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
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
    val dispatcherTestRule = DispatcherTestRule(StandardTestDispatcher())

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var youTubeVideoDao: YouTubeVideoDao
    private lateinit var databaseContentManager: DatabaseContentManager

    @Before
    fun setup() {
        hiltRule.inject()
        databaseContentManager = DatabaseContentManager(youTubeVideoDao)
    }

    @Test
    fun checkInsertedVideosEqualsToSourceVideoList() = runTest {
        val initialVideoResponse = DEFAULT_VIDEO_RESPONSE_WITH_CHANNEL_IMG
        val expectedDatabaseVideoResponse = TEST_DATABASE_VIDEO_RESPONSE

        insertVideoPageToDatabase(initialVideoResponse)
        val pageWithVideos = youTubeVideoDao.getVideosFromPage(INITIAL_PAGE_TOKEN)
        val databaseVideoResponse = pageWithVideos.convertToYoutubeVideoResponse()

        assertEquals(expectedDatabaseVideoResponse, databaseVideoResponse)
    }

    @Test
    fun checkAddedToDatabaseAmountOfVideosEqualsToSourceVideoListSize() = runTest {
        val initialVideoResponse = DEFAULT_VIDEO_RESPONSE_WITH_CHANNEL_IMG
        val sourceVideoList = TEST_DATABASE_VIDEO_LIST

        insertVideoPageToDatabase(initialVideoResponse)

        val videoCount = youTubeVideoDao.getVideosCount()
        assertEquals(sourceVideoList.size, videoCount)
    }

    private suspend fun insertVideoPageToDatabase(initialVideoResponse: YoutubeVideoResponse) {
        databaseContentManager.setCurrentPageTokenToVideos(
            INITIAL_PAGE_TOKEN,
            initialVideoResponse
        )
        databaseContentManager.insertVideosToDatabaseWithTimeStamp(
            testDateTime,
            initialVideoResponse
        )
    }
}