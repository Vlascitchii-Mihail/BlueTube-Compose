package com.vlascitchii.data_local.source

import com.vlascitchii.common_test.rule.DispatcherTestRule
import com.vlascitchii.common_test.util.assertListEqualsTo
import com.vlascitchii.data_local.database.YouTubeVideoDao
import com.vlascitchii.data_local.database.convertToLocalYoutubeVideoResponseEntity
import com.vlascitchii.data_local.enetity.INITIAL_PAGE_TOKEN
import com.vlascitchii.data_local.enetity.video_list.videos.YoutubeVideoEntity
import com.vlascitchii.data_local.enetity.video_list.videos.YoutubeVideoResponseEntity
import com.vlascitchii.data_local.enetity.video_list.videos.YoutubeVideoResponseEntity.Companion.TEST_DATABASE_VIDEO_RESPONSE
import com.vlascitchii.data_local.source.utils.DatabaseContentManager
import com.vlascitchii.domain.model.videos.YoutubeVideoResponseDomain.Companion.DOMAIN_RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG
import com.vlascitchii.domain.model.videos.YoutubeVideoResponseDomain.Companion.testDateTime
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class DatabaseContentManagerTest {

    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    private val youTubeDao: YouTubeVideoDao = mock()
    private lateinit var databaseContentManager: DatabaseContentManager
    private lateinit var testVideoResponseEntity: YoutubeVideoResponseEntity

    @Before
    fun init() {
        databaseContentManager = DatabaseContentManager(youTubeDao)
        testVideoResponseEntity = DOMAIN_RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG.convertToLocalYoutubeVideoResponseEntity()
    }

    @Test
    fun `setCurrentPageTokenToVideos() sets the currentPageToken to YoutubeVideoResponseEntity and to each VideoEntity`() {
        val videoResponseWithCurrentPageToken = with(databaseContentManager) {
            testVideoResponseEntity.setCurrentPageTokenToVideos(INITIAL_PAGE_TOKEN)
        }

        assertEquals(
            TEST_DATABASE_VIDEO_RESPONSE.pageEntity.currentPageToken,
            videoResponseWithCurrentPageToken.pageEntity.currentPageToken
        )

        TEST_DATABASE_VIDEO_RESPONSE.items.map { it.pageToken }
            .assertListEqualsTo(videoResponseWithCurrentPageToken.items.map { it.pageToken })
    }

    @Test
    fun `insertPageFrom() calls the Dao insertPages() function`() = runTest {
        databaseContentManager.insertPageFrom(testVideoResponseEntity)

        verify(youTubeDao, times(1)).insertPages(testVideoResponseEntity.pageEntity)
    }

    @Test
    fun `bindVideosFromResponseWithData() binds videos in response and inserts to the DB`() = runTest {
        with(databaseContentManager) {
            testVideoResponseEntity.bindAndInsertVideoWith(testDateTime)
        }

        testVideoResponseEntity.items.forEach { video: YoutubeVideoEntity ->
            verify(youTubeDao).insertVideo(video)
        }
        assertEquals(TEST_DATABASE_VIDEO_RESPONSE, testVideoResponseEntity)
    }

    @Test
    fun `bindVideosFromResponseWithData() sets the video key values for relation database`() {
        with(databaseContentManager) {
            testVideoResponseEntity.items.first().bindVideosFromResponseWithData(testDateTime)

            assertEquals(TEST_DATABASE_VIDEO_RESPONSE.items.first(), testVideoResponseEntity.items.first())
        }
    }

    @Test
    fun `setTimeStamp() sets a TimeStamp`() {
        with(databaseContentManager) {
            testVideoResponseEntity.items.forEach { videoEntity: YoutubeVideoEntity ->
                videoEntity.setTimeStamp(testDateTime)
            }
        }

        TEST_DATABASE_VIDEO_RESPONSE.items.map { it.loadedDate }
            .assertListEqualsTo(testVideoResponseEntity.items.map { it.loadedDate })
    }

    @Test
    fun `videoSnippetAddId() inserts snippetId into the YouTubeVideoEntity`() {
        val videoSnippetIdList = TEST_DATABASE_VIDEO_RESPONSE.items.map { it.snippet.videoId }
        with(databaseContentManager) {
            videoSnippetIdList.executeFunction{ videoId: String -> videoSnippetAddId(videoId) }
        }

        TEST_DATABASE_VIDEO_RESPONSE.items.map { it.snippet.videoId }
            .assertListEqualsTo(testVideoResponseEntity.items.map { it.snippet.videoId })
    }

    @Test
    fun `thumbnailsAddId() inserts thumbnailsId into the YouTubeVideoEntity`() {
        val videoThumbnailIdList = TEST_DATABASE_VIDEO_RESPONSE.items.map { it.snippet.thumbnailsEntity.thumbnailsId }
        with(databaseContentManager) {
            videoThumbnailIdList.executeFunction { videoId: String -> thumbnailsAddId(videoId) }
        }

        TEST_DATABASE_VIDEO_RESPONSE.items.map { it.snippet.thumbnailsEntity.thumbnailsId }
            .assertListEqualsTo(testVideoResponseEntity.items.map { it.snippet.thumbnailsEntity.thumbnailsId })
    }

    @Test
    fun `thumbnailsAttributesAddId() inserts thumbnailsAttributesId into the YouTubeVideoEntity`() {
        val videoThumbnailAttributesIdList = TEST_DATABASE_VIDEO_RESPONSE.items.map { it.snippet.thumbnailsEntity.medium.thumbnailAttributesId }
        with(databaseContentManager) {
            videoThumbnailAttributesIdList.executeFunction { videoId: String -> thumbnailsAttributesAddId(videoId) }
        }

        TEST_DATABASE_VIDEO_RESPONSE.items.map { it.snippet.thumbnailsEntity.medium.thumbnailAttributesId }
            .assertListEqualsTo(testVideoResponseEntity.items.map { it.snippet.thumbnailsEntity.medium.thumbnailAttributesId })
    }

    @Test
    fun `videoStatisticsAddId() inserts statisticsId into the YouTubeVideoEntity`() {
        val statisticsIdList = TEST_DATABASE_VIDEO_RESPONSE.items.map { it.statistics.statisticsId }
        with(databaseContentManager) {
            statisticsIdList.executeFunction { videoId: String -> videoStatisticsAddId(videoId) }
        }

        TEST_DATABASE_VIDEO_RESPONSE.items.map { it.statistics.statisticsId }
            .assertListEqualsTo(testVideoResponseEntity.items.map { it.statistics.statisticsId })
    }

    @Test
    fun `contentDetailsAddId() inserts contentDetailsId into the YouTubeVideoEntity`() {
        val contentDetailsIdList = TEST_DATABASE_VIDEO_RESPONSE.items.map { it.contentDetailsEntity.contentDetailsId }
        with(databaseContentManager) {
            contentDetailsIdList.executeFunction { videoId: String -> contentDetailsAddId(videoId) }
        }

        TEST_DATABASE_VIDEO_RESPONSE.items.map { it.contentDetailsEntity.contentDetailsId }
            .assertListEqualsTo(testVideoResponseEntity.items.map { it.contentDetailsEntity.contentDetailsId })
    }

    private fun List<String>.executeFunction(method: YoutubeVideoEntity.(String) -> Unit) {
        for (index in testVideoResponseEntity.items.indices)
        method.invoke(testVideoResponseEntity.items[index], this[index])
    }

    @Test
    fun `deleteExtraVideos() calls youTubeDao deleteExtraFiveVideos if overall size is greater than 100`() = runTest {
        whenever(youTubeDao.getVideosCount()).thenReturn(flowOf(110))

        databaseContentManager.deleteExtraVideos()

        verify(youTubeDao, times(1)).deleteExtraFiveVideos()
    }

    @Test
    fun `updateCurrentPageToken() updates the sourceCurrentPageToken`() {
        val testToken = "Test token"
        val testPage = TEST_DATABASE_VIDEO_RESPONSE.pageEntity.copy(nextPageToken = testToken)
        val testDatabaseResponse = TEST_DATABASE_VIDEO_RESPONSE.copy(pageEntity = testPage)
        databaseContentManager.updateCurrentPageToken(testDatabaseResponse)

        assertEquals(databaseContentManager.sourceCurrentPageToken, testToken)
    }

    @Test
    fun `updateCurrentPageToken() doesn't updates the sourceCurrentPageToken if we pass null`() {
        val testToken = null
        val testPage = TEST_DATABASE_VIDEO_RESPONSE.pageEntity.copy(nextPageToken = testToken)
        val testDatabaseResponse = TEST_DATABASE_VIDEO_RESPONSE.copy(pageEntity = testPage)
        databaseContentManager.updateCurrentPageToken(testDatabaseResponse)

        assertNotEquals(databaseContentManager.sourceCurrentPageToken, testToken)
    }

    @Test
    fun `getFirstPageFromDatabase() calls youTubeDao getFirstPageFromDb()`() = runTest {
        databaseContentManager.getFirstPageFromDatabase()

        verify(youTubeDao, times(1)).getFirstPageFromDb()
    }

    @Test
    fun `getParticularPageFromDatabase() calls youTubeDao getVideosFromPage()`() = runTest {
        val testToken = "Test token"
        databaseContentManager.getParticularPageFromDatabase(testToken)

        verify(youTubeDao, times(1)).getVideosFromPage(eq(testToken))
    }
}
