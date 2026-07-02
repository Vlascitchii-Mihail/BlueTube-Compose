package com.vlascitchii.data_local.instrumented

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vlascitchii.data_local.database.YouTubeVideoDao
import com.vlascitchii.data_local.entity.PageEntity
import com.vlascitchii.data_local.entity.video_list.ThumbnailAttributesEntity
import com.vlascitchii.data_local.entity.video_list.ThumbnailsEntity
import com.vlascitchii.data_local.entity.video_list.videos.ContentDetailsEntity
import com.vlascitchii.data_local.entity.video_list.videos.VideoSnippetEntity
import com.vlascitchii.data_local.entity.video_list.videos.VideoStatisticsEntity
import com.vlascitchii.data_local.entity.video_list.videos.YoutubeVideoEntity
import com.vlascitchii.data_local.entity.video_list.videos.YoutubeVideoResponseEntity
import com.vlascitchii.data_local.mock_model.TEST_DATABASE_VIDEO_RESPONSE
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class YouTubeVideoDaoTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var youTubeVideoDao: YouTubeVideoDao
    private val videoIdList = TEST_DATABASE_VIDEO_RESPONSE.items.map { video: YoutubeVideoEntity ->
        video.id
    }

    @Before
    fun setup() { hiltRule.inject() }

    @Test
    fun fun_insertPages_inserts_and_fun_getFirstPage_gets_first_pages() = runBlocking {
        val expectedPage: PageEntity = TEST_DATABASE_VIDEO_RESPONSE.pageEntity

        youTubeVideoDao.insertPages(expectedPage)
        youTubeVideoDao.insertPages(TEST_DATABASE_VIDEO_RESPONSE.pageEntity.copy(currentPageToken = "CAoQAA"))

        val actualPage: PageEntity = youTubeVideoDao.getFirstPage().first().pageEntity

        assertEquals(expectedPage, actualPage)
    }

    @Test
    fun fun_insertPages_inserts_and_fun_getPageByToken_returns_specific_page() = runBlocking {
        val testToken = "CAoQAA"
        val expectedPage: PageEntity =
            TEST_DATABASE_VIDEO_RESPONSE.pageEntity.copy(currentPageToken = testToken)

        youTubeVideoDao.insertPages(expectedPage)

        val actualPage: PageEntity = youTubeVideoDao.getPageByToken(testToken).first().pageEntity

        assertEquals(expectedPage, actualPage)
    }

    @Test
    fun fun_insertParticularVideo_inserts_and_fun_getFirstPage_gets_YoutubeVideoEntity() = runBlocking {
        val expectedVideoList = TEST_DATABASE_VIDEO_RESPONSE.items

        println("Pages = ${TEST_DATABASE_VIDEO_RESPONSE.pageEntity}")
        println("iVideo List = $expectedVideoList")

        insertPage(TEST_DATABASE_VIDEO_RESPONSE.pageEntity)
        insertVideoList(expectedVideoList)

        val actualVideoList = youTubeVideoDao.getFirstPage().first().items

        val expectedVideoIdList = expectedVideoList.map { video: YoutubeVideoEntity -> video.id }
        val actualVideoIdList = actualVideoList.map { video: YoutubeVideoEntity -> video.id }

        expectedVideoIdList.assertElementsEquals(actualVideoIdList)
    }

    @Test
    fun fun_insertContentDetails_inserts_and_fun_getVideoDetails_gets_VideoStatisticsEntity() = runBlocking {
            val expectedContentDetailsList: List<ContentDetailsEntity> =
                TEST_DATABASE_VIDEO_RESPONSE.items.map { it.contentDetailsEntity }

            insertPage(TEST_DATABASE_VIDEO_RESPONSE.pageEntity)
            insertVideoList(TEST_DATABASE_VIDEO_RESPONSE.items)

            expectedContentDetailsList.forEach { videoDetails: ContentDetailsEntity ->
                youTubeVideoDao.insertContentDetails(videoDetails)
            }

            val actualContentDetailsList: List<ContentDetailsEntity> = videoIdList.map { id: String ->
                youTubeVideoDao.getVideoDetails(id).first()
            }

            expectedContentDetailsList.assertElementsEquals(actualContentDetailsList)
    }

    @Test
    fun fun_insertVideoStatistics_inserts_and_fun_getVideoStatistics_get_VideoStatisticsEntity() = runBlocking {
            val expectedVideoStatistics: List<VideoStatisticsEntity> =
                TEST_DATABASE_VIDEO_RESPONSE.items.map { video: YoutubeVideoEntity -> video.statistics }

            insertPage(TEST_DATABASE_VIDEO_RESPONSE.pageEntity)
            insertVideoList(TEST_DATABASE_VIDEO_RESPONSE.items)

            expectedVideoStatistics.forEach { videoStatistics: VideoStatisticsEntity ->
                youTubeVideoDao.insertVideoStatistics(videoStatistics)
            }

            val actualVideoStatistics: List<VideoStatisticsEntity> = videoIdList.map { id: String ->
                youTubeVideoDao.getVideoStatistics(id).first()
            }

            expectedVideoStatistics.assertElementsEquals(actualVideoStatistics)
    }

    @Test
    fun fun_insertVideoSnippet_inserts_and_fun_getVideoSnippet_gets_VideoSnippetEntity() = runBlocking {
        val expectedVideoSnippet: List<VideoSnippetEntity> =
            TEST_DATABASE_VIDEO_RESPONSE.items.map { video: YoutubeVideoEntity ->
            video.snippet
        }

        insertPage(TEST_DATABASE_VIDEO_RESPONSE.pageEntity)
        insertVideoList(TEST_DATABASE_VIDEO_RESPONSE.items)
        insertVideoSnippet(TEST_DATABASE_VIDEO_RESPONSE.items)

        val actualVideoSnippet: List<VideoSnippetEntity> = videoIdList.map { id: String ->
            youTubeVideoDao.getVideoSnippet(id).first()
        }

        expectedVideoSnippet.assertSnippetListEquals(actualVideoSnippet)
    }

    @Test
    fun fun_insertThumbnails_inserts_and_fun_getThumbnails_gets_ThumbnailsEntity() = runBlocking {
        val expectedVideoThumbnails: List<ThumbnailsEntity> =
            TEST_DATABASE_VIDEO_RESPONSE.items.map { video: YoutubeVideoEntity ->
            video.snippet.thumbnailsEntity
        }

        insertPage(TEST_DATABASE_VIDEO_RESPONSE.pageEntity)
        insertVideoList(TEST_DATABASE_VIDEO_RESPONSE.items)
        insertVideoSnippet(TEST_DATABASE_VIDEO_RESPONSE.items)
        insertThumbnails(TEST_DATABASE_VIDEO_RESPONSE.items)

        val snippetIdList = TEST_DATABASE_VIDEO_RESPONSE.items.map { video: YoutubeVideoEntity ->
            video.snippet.snippetId
        }

        val actualVideoThumbnails: List<ThumbnailsEntity> = snippetIdList.map { snippetId: String ->
            youTubeVideoDao.getThumbnails(snippetId).first()
        }

        expectedVideoThumbnails.assertThumbnailsEquals(actualVideoThumbnails)
    }

    @Test
    fun fun_insertThumbnailsAttributes_inserts_and_fun_getThumbnailsAttributes_gets_ThumbnailAttributesEntity() = runBlocking {
        val expectedThumbnailAttributes: List<ThumbnailAttributesEntity> =
            TEST_DATABASE_VIDEO_RESPONSE.items.map { video: YoutubeVideoEntity ->
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

        val actualThumbnailAttributesList: List<ThumbnailAttributesEntity> = thumbnailsIdList.map { thumbnailsId: String ->
            youTubeVideoDao.getThumbnailsAttributes(thumbnailsId).first()
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
    fun fun_insertVideo_inserts_and_fun_getFirstPageFromDb_gets_YoutubeVideoResponseEntity() = runBlocking {
        val expectedVideoResponse: YoutubeVideoResponseEntity = TEST_DATABASE_VIDEO_RESPONSE

        insertPage(TEST_DATABASE_VIDEO_RESPONSE.pageEntity)
        TEST_DATABASE_VIDEO_RESPONSE.items.forEach { video: YoutubeVideoEntity ->
            youTubeVideoDao.insertVideo(video)
        }

        val actualVideoResponse: YoutubeVideoResponseEntity = youTubeVideoDao.getFirstPageFromDb().first()

        assertEquals(expectedVideoResponse, actualVideoResponse)
    }

    @Test
    fun fun_insertVideo_inserts_and_fun_getVideosFromPage_gets_YoutubeVideoResponseEntity() = runBlocking {
        val testPageToken = "CAUQAQ"
        val testPageEntity = TEST_DATABASE_VIDEO_RESPONSE.pageEntity.copy(currentPageToken = testPageToken)
        val videoListCopy = TEST_DATABASE_VIDEO_RESPONSE.items.map { it.copy() }
        val testVideoItems = videoListCopy.map { youTubeVideo: YoutubeVideoEntity ->
            youTubeVideo.pageToken = testPageToken
            youTubeVideo
        }

        val expectedVideoResponse: YoutubeVideoResponseEntity =
            TEST_DATABASE_VIDEO_RESPONSE.copy(pageEntity = testPageEntity, items = testVideoItems)

        insertPage(testPageEntity)
        expectedVideoResponse.items.forEach { video: YoutubeVideoEntity ->
            youTubeVideoDao.insertVideo(video)
        }

        val actualVideoResponse: YoutubeVideoResponseEntity = youTubeVideoDao.getVideosFromPage(testPageToken).first()

        assertEquals(expectedVideoResponse, actualVideoResponse)
    }

    @Test
    fun fun_getVideosCount_returns_video_amount_in_the_DB() = runBlocking {
        val expectedVideoCount = 5
        insertPage(TEST_DATABASE_VIDEO_RESPONSE.pageEntity)
        insertVideoList(TEST_DATABASE_VIDEO_RESPONSE.items)

        assertEquals(expectedVideoCount, youTubeVideoDao.getVideosCount().first())
    }

    @Test
    fun fun_deleteExtraFiveVideos_deletes_latest_5_videos() = runBlocking {
        val expectedVideoCount = 5

        insertPage(TEST_DATABASE_VIDEO_RESPONSE.pageEntity)
        insertVideoList(TEST_DATABASE_VIDEO_RESPONSE.items)

        insertPage(TEST_DATABASE_VIDEO_RESPONSE.pageEntity)
        insertVideoList(TEST_DATABASE_VIDEO_RESPONSE.items)

        assertEquals(expectedVideoCount, youTubeVideoDao.getVideosCount().first())
    }
}