package com.appelier.bluetubecompose.integration.core.core_database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.appelier.bluetubecompose.core.core_database.YouTubeVideoDao
import com.appelier.bluetubecompose.core.core_database.utils.DatabaseContentController
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideoResponse
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideoResponse.Companion.DEFAULT_VIDEO_RESPONSE_WITH_CHANNEL_IMG
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideoResponse.Companion.INITIAL_PAGE
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideoResponse.Companion.TEST_DATABASE_VIDEO_LIST
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideoResponse.Companion.TEST_DATABASE_VIDEO_RESPONSE
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideoResponse.Companion.testDateTime
import com.appelier.bluetubecompose.utils.convertPageWithVideosToYoutubeVideoResponse
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
    private val testCoroutineDispatcher = StandardTestDispatcher()
    private val testCoroutineScope = TestScope(testCoroutineDispatcher)

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun checkDatabaseDataInsertion() {
        testCoroutineScope.runTest {
            insertVideosInDatabase(DatabaseContentController(youTubeVideoDao))

            val pageWithVideos = youTubeVideoDao.getPageWithVideos(INITIAL_PAGE)
            val databaseActualResponse = convertPageWithVideosToYoutubeVideoResponse(pageWithVideos)

            assertEquals(TEST_DATABASE_VIDEO_RESPONSE, databaseActualResponse)
        }
    }

    @Test
    fun checkGetVideoCountInDatabase() {
        testCoroutineScope.runTest {
            insertVideosInDatabase(DatabaseContentController(youTubeVideoDao))

            val videoCount = youTubeVideoDao.getVideosCount()
            assertEquals(TEST_DATABASE_VIDEO_LIST.size, videoCount)
        }
    }

    private suspend fun insertVideosInDatabase(
        databaseContentController: DatabaseContentController,
        initialPage: String = INITIAL_PAGE,
        videoResponse: YoutubeVideoResponse = DEFAULT_VIDEO_RESPONSE_WITH_CHANNEL_IMG
    ) {
        with(databaseContentController) {
            videoResponse.setPageTokenToVideos(initialPage)
            videoResponse.insertVideosToDb(testDateTime)
        }
    }
}