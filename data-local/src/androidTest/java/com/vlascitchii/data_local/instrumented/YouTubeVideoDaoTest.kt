package com.vlascitchii.data_local.instrumented

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vlascitchii.data_local.database.YouTubeVideoDao
import com.vlascitchii.data_local.enetity.video_list.videos.YoutubeVideoEntity
import com.vlascitchii.data_local.enetity.video_list.videos.YoutubeVideoResponseEntity
import com.vlascitchii.data_local.enetity.video_list.videos.YoutubeVideoResponseEntity.Companion.TEST_DATABASE_VIDEO_RESPONSE
import com.vlascitchii.data_local.source.utils.DatabaseContentManager
import com.vlascitchii.data_local.util.assertListEqualsTo
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class YouTubeVideoDaoTest {

    //TODO: Redundant
//    @get:Rule
//    val dispatcherTestRule = DispatcherTestRule(StandardTestDispatcher())

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
    fun insert_and_get_firstPage() = runBlockingTest {
        val expectedResult = TEST_DATABASE_VIDEO_RESPONSE

        insertTestVideos()
        val actualResult = youTubeVideoDao.getFirstPageFromDb().first()

        assertEquals(expectedResult.pageEntity, actualResult.pageEntity)
        expectedResult.items.assertListEqualsTo(actualResult.items)
    }

    @Test
    fun insert_and_get_videos_from_particular_page() = runTest {
        val testToken = "Test token"
        val expectedResult = TEST_DATABASE_VIDEO_RESPONSE.copy()
        expectedResult.pageEntity.currentPageToken = testToken

        insertTestVideos(expectedResult)
        val actualResult = youTubeVideoDao.getVideosFromPage(testToken).first()

        assertEquals(expectedResult.pageEntity, actualResult.pageEntity)
        expectedResult.items.assertListEqualsTo(actualResult.items)
    }

    private suspend fun insertTestVideos(youTubeVideoResponseEntity: YoutubeVideoResponseEntity = TEST_DATABASE_VIDEO_RESPONSE) {
        youTubeVideoResponseEntity.items.forEach { videoEntity: YoutubeVideoEntity ->
            youTubeVideoDao.insertVideo(videoEntity)
        }
    }

        //TODO: check if it possible to get videos from DB at once with annotations, without initializing fields of the result
}