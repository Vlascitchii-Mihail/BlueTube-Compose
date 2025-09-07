package com.vlascitchii.bluetubecompose.instrumented

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vlascitchii.data_local.database.YouTubeVideoDao
import com.vlascitchii.data_local.enetity.PageEntity
import com.vlascitchii.data_local.enetity.video_list.ThumbnailAttributesEntity
import com.vlascitchii.data_local.enetity.video_list.ThumbnailsEntity
import com.vlascitchii.data_local.enetity.video_list.videos.ContentDetailsEntity
import com.vlascitchii.data_local.enetity.video_list.videos.VideoSnippetEntity
import com.vlascitchii.data_local.enetity.video_list.videos.VideoStatisticsEntity
import com.vlascitchii.data_local.enetity.video_list.videos.YoutubeVideoEntity
import com.vlascitchii.data_local.enetity.video_list.videos.YoutubeVideoResponseEntity.Companion.TEST_DATABASE_VIDEO_RESPONSE
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
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
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var youTubeVideoDao: YouTubeVideoDao
    private val videoIdList = TEST_DATABASE_VIDEO_RESPONSE.items.map { video: YoutubeVideoEntity ->
        video.id
    }

    @Before
    fun setup() { hiltRule.inject() }

    @Test
    fun fun_insertPages_inserts_and_fun_getFirstPage_gets_first_pages() = runTest {
        val expectedPage = TEST_DATABASE_VIDEO_RESPONSE.pageEntity

        youTubeVideoDao.insertPages(expectedPage)
        youTubeVideoDao.insertPages(TEST_DATABASE_VIDEO_RESPONSE.pageEntity.copy(currentPageToken = "CAoQAA"))

        val actualPage = youTubeVideoDao.getFirstPage().pageEntity

        assertEquals(expectedPage, actualPage)
    }

    @Test
    fun fun_insertPages_inserts_and_fun_getPageByToken_returns_specific_page() = runTest {
        val testToken = "CAoQAA"
        val expectedPage =
            TEST_DATABASE_VIDEO_RESPONSE.pageEntity.copy(currentPageToken = testToken)

        youTubeVideoDao.insertPages(expectedPage)

        val actualPage = youTubeVideoDao.getPageByToken(testToken).pageEntity

        assertEquals(expectedPage, actualPage)
    }

    @Test
    fun fun_insertParticularVideo_inserts_and_fun_getFirstPage_gets_YoutubeVideoEntity() = runTest {
        val expectedVideoList = TEST_DATABASE_VIDEO_RESPONSE.items

        println("Pages = ${TEST_DATABASE_VIDEO_RESPONSE.pageEntity}")
        println("iVideo List = $expectedVideoList")

        insertPage(TEST_DATABASE_VIDEO_RESPONSE.pageEntity)
        insertVideoList(expectedVideoList)

        val actualVideoList = youTubeVideoDao.getFirstPage().items

        val expectedVideoIdList = expectedVideoList.map { video: YoutubeVideoEntity -> video.id }
        val actualVideoIdList = actualVideoList.map { video: YoutubeVideoEntity -> video.id }

        expectedVideoIdList.assertElementsEquals(actualVideoIdList)
    }

    @Test
    fun fun_insertContentDetails_inserts_and_fun_getVideoDetails_gets_VideoStatisticsEntity() = runTest {
            val expectedContentDetailsList =
                TEST_DATABASE_VIDEO_RESPONSE.items.map { it.contentDetailsEntity }

            insertPage(TEST_DATABASE_VIDEO_RESPONSE.pageEntity)
            insertVideoList(TEST_DATABASE_VIDEO_RESPONSE.items)

            expectedContentDetailsList.forEach { videoDetails: ContentDetailsEntity ->
                youTubeVideoDao.insertContentDetails(videoDetails)
            }

            val actualContentDetailsList = videoIdList.map { id: String ->
                youTubeVideoDao.getVideoDetails(id)
            }

            expectedContentDetailsList.assertElementsEquals(actualContentDetailsList)
    }

    @Test
    fun fun_insertVideoStatistics_inserts_and_fun_getVideoStatistics_get_VideoStatisticsEntity() = runTest {
            val expectedVideoStatistics =
                TEST_DATABASE_VIDEO_RESPONSE.items.map { video: YoutubeVideoEntity -> video.statistics }

            insertPage(TEST_DATABASE_VIDEO_RESPONSE.pageEntity)
            insertVideoList(TEST_DATABASE_VIDEO_RESPONSE.items)

            expectedVideoStatistics.forEach { videoStatistics: VideoStatisticsEntity ->
                youTubeVideoDao.insertVideoStatistics(videoStatistics)
            }

            val actualVideoStatistics = videoIdList.map { id: String ->
                youTubeVideoDao.getVideoStatistics(id)
            }

            expectedVideoStatistics.assertElementsEquals(actualVideoStatistics)
    }

    @Test
    fun fun_insertVideoSnippet_inserts_and_fun_getVideoSnippet_gets_VideoSnippetEntity() = runTest {
        val expectedVideoSnippet = TEST_DATABASE_VIDEO_RESPONSE.items.map { video: YoutubeVideoEntity ->
            video.snippet
        }

        insertPage(TEST_DATABASE_VIDEO_RESPONSE.pageEntity)
        insertVideoList(TEST_DATABASE_VIDEO_RESPONSE.items)
        insertVideoSnippet(TEST_DATABASE_VIDEO_RESPONSE.items)

        val actualVideoSnippet = videoIdList.map { id: String ->
            youTubeVideoDao.getVideoSnippet(id)
        }

        expectedVideoSnippet.assertSnippetListEquals(actualVideoSnippet)
    }

    @Test
    fun fun_insertThumbnails_inserts_and_fun_getThumbnails_gets_ThumbnailsEntity() = runTest {
        val expectedVideoThumbnails = TEST_DATABASE_VIDEO_RESPONSE.items.map { video: YoutubeVideoEntity ->
            video.snippet.thumbnailsEntity
        }

        insertPage(TEST_DATABASE_VIDEO_RESPONSE.pageEntity)
        insertVideoList(TEST_DATABASE_VIDEO_RESPONSE.items)
        insertVideoSnippet(TEST_DATABASE_VIDEO_RESPONSE.items)
        insertThumbnails(TEST_DATABASE_VIDEO_RESPONSE.items)

        val snippetIdList = TEST_DATABASE_VIDEO_RESPONSE.items.map { video: YoutubeVideoEntity ->
            video.snippet.snippetId
        }

        val actualVideoThumbnails = snippetIdList.map { snippetId: String ->
            youTubeVideoDao.getThumbnails(snippetId)
        }

        expectedVideoThumbnails.assertThumbnailsEquals(actualVideoThumbnails)
    }

    @Test
    fun fun_insertThumbnailsAttributes_inserts_and_fun_getThumbnailsAttributes_gets_ThumbnailAttributesEntity() = runTest {
        val expectedThumbnailAttributes = TEST_DATABASE_VIDEO_RESPONSE.items.map { video: YoutubeVideoEntity ->
            video.snippet.thumbnailsEntity.medium
        }

        insertPage(TEST_DATABASE_VIDEO_RESPONSE.pageEntity)
        insertVideoList(TEST_DATABASE_VIDEO_RESPONSE.items)
        insertVideoSnippet(TEST_DATABASE_VIDEO_RESPONSE.items)
        insertThumbnails(TEST_DATABASE_VIDEO_RESPONSE.items)
        insertThumbnailAttributes(expectedThumbnailAttributes)

        val thumbnailsIdList = TEST_DATABASE_VIDEO_RESPONSE.items.map { video: YoutubeVideoEntity ->
            video.snippet.thumbnailsEntity.thumbnailsId
        }

        val actualThumbnailAttributesList = thumbnailsIdList.map { thumbnailsId: String ->
            youTubeVideoDao.getThumbnailsAttributes(thumbnailsId)
        }

        expectedThumbnailAttributes.assertElementsEquals(actualThumbnailAttributesList)
    }



    private suspend fun insertPage(pageEntity: PageEntity) {
        youTubeVideoDao.insertPages(pageEntity)
    }

    private suspend fun insertVideoList(expectedVideoList: List<YoutubeVideoEntity>) {
        expectedVideoList.forEach { testVideo: YoutubeVideoEntity ->
            youTubeVideoDao.insertParticularVideo(testVideo)
        }
    }

    private suspend fun insertVideoSnippet(videoItems: List<YoutubeVideoEntity>) {
        val videoSnippetList = videoItems.map { video: YoutubeVideoEntity -> video.snippet }
        videoSnippetList.forEach { videoSnippet: VideoSnippetEntity ->
            youTubeVideoDao.insertVideoSnippet(videoSnippet)
        }
    }

    private suspend fun insertThumbnails(videoItems: List<YoutubeVideoEntity>) {
        val videoThumbnailsList = videoItems.map { video: YoutubeVideoEntity -> video.snippet.thumbnailsEntity }
        videoThumbnailsList.forEach { thumbnails: ThumbnailsEntity ->
            youTubeVideoDao.insertThumbnails(thumbnails)
        }
    }

    private suspend fun insertThumbnailAttributes(expectedThumbnailAttributes: List<ThumbnailAttributesEntity>) {
        expectedThumbnailAttributes.forEach { thumbnailAttribute: ThumbnailAttributesEntity ->
            youTubeVideoDao.insertThumbnailsAttributes(thumbnailAttribute)
        }
    }

    private fun <T> List<T>.assertElementsEquals(actualElements: List<T>) {
        val expectedElements = this
        for (index in expectedElements.indices) {
            assertEquals(expectedElements[index], actualElements[index])
        }
    }

    private fun List<VideoSnippetEntity>.assertSnippetListEquals(actualSnippetList: List<VideoSnippetEntity>) {
        val expectedSnippetList = this
        for (index in expectedSnippetList.indices) {
            assertEquals(expectedSnippetList[index].title, actualSnippetList[index].title)
            assertEquals(expectedSnippetList[index].description, actualSnippetList[index].description)
            assertEquals(expectedSnippetList[index].channelTitle, actualSnippetList[index].channelTitle)
            assertEquals(expectedSnippetList[index].channelId, actualSnippetList[index].channelId)
            assertEquals(expectedSnippetList[index].publishedAt, actualSnippetList[index].publishedAt)
        }
    }

    private fun List<ThumbnailsEntity>.assertThumbnailsEquals(actualThumbnailsList: List<ThumbnailsEntity>) {
        val expectedThumbnailList = this
        for (index in expectedThumbnailList.indices) {
            assertEquals(expectedThumbnailList[index].thumbnailsId, actualThumbnailsList[index].thumbnailsId)
            assertEquals(expectedThumbnailList[index].snippetId, actualThumbnailsList[index].snippetId)
        }
    }

    @Test
    fun fun_insertVideo_inserts_and_fun_getFirstPageFromDb_gets_YoutubeVideoResponseEntity() = runTest {
        val expectedVideoResponse = TEST_DATABASE_VIDEO_RESPONSE

        insertPage(TEST_DATABASE_VIDEO_RESPONSE.pageEntity)
        TEST_DATABASE_VIDEO_RESPONSE.items.forEach { video: YoutubeVideoEntity ->
            youTubeVideoDao.insertVideo(video)
        }

        val actualVideoResponse = youTubeVideoDao.getFirstPageFromDb()

        assertEquals(expectedVideoResponse, actualVideoResponse)
    }

    @Test
    fun fun_insertVideo_inserts_and_fun_getVideosFromPage_gets_YoutubeVideoResponseEntity() = runTest {
        val testPageToken = "CAUQAQ"
        val testPageEntity = TEST_DATABASE_VIDEO_RESPONSE.pageEntity.copy(currentPageToken = testPageToken)
        val videoListCopy = TEST_DATABASE_VIDEO_RESPONSE.items.map { it.copy() }
        val testVideoItems = videoListCopy.map { youTubeVideo: YoutubeVideoEntity ->
            youTubeVideo.pageToken = testPageToken
            youTubeVideo
        }

        val expectedVideoResponse = TEST_DATABASE_VIDEO_RESPONSE.copy(pageEntity = testPageEntity, items = testVideoItems)

        insertPage(testPageEntity)
        expectedVideoResponse.items.forEach { video: YoutubeVideoEntity ->
            youTubeVideoDao.insertVideo(video)
        }

        val actualVideoResponse = youTubeVideoDao.getVideosFromPage(testPageToken)

        assertEquals(expectedVideoResponse, actualVideoResponse)
    }

    @Test
    fun fun_getVideosCount_returns_video_amount_in_the_DB() = runTest {
        val expectedVideoCount = 5
        insertPage(TEST_DATABASE_VIDEO_RESPONSE.pageEntity)
        insertVideoList(TEST_DATABASE_VIDEO_RESPONSE.items)

        assertEquals(expectedVideoCount, youTubeVideoDao.getVideosCount())
    }

    @Test
    fun fun_deleteExtraFiveVideos_deletes_latest_5_videos() = runTest {
        val expectedVideoCount = 5

        insertPage(TEST_DATABASE_VIDEO_RESPONSE.pageEntity)
        insertVideoList(TEST_DATABASE_VIDEO_RESPONSE.items)

        insertPage(TEST_DATABASE_VIDEO_RESPONSE.pageEntity)
        insertVideoList(TEST_DATABASE_VIDEO_RESPONSE.items)

        assertEquals(expectedVideoCount, youTubeVideoDao.getVideosCount())
    }
}