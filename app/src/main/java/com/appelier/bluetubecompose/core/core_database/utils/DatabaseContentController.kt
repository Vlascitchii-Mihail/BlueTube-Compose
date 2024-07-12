package com.appelier.bluetubecompose.core.core_database.utils

import com.appelier.bluetubecompose.core.core_database.YouTubeVideoDao
import com.appelier.bluetubecompose.screen_video_list.model.Page
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideo
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideoResponse
import java.time.OffsetDateTime

class DatabaseContentController(private val youTubeVideoDao: YouTubeVideoDao) {

    suspend fun YoutubeVideoResponse.insertVideosToDb(
        loadDate: OffsetDateTime = OffsetDateTime.now()
    ) {
        this.insertPage()

        this.items.forEach { video ->
            val videoId = video.id
            prepareVideo(video, loadDate)
            video.prepareVideoSnippet(videoId)
            video.insertThumbnails(videoId)
            video.insertThumbnailsAttributes(videoId)
            video.insertVideoStatistics(videoId)
            video.insertContentDetails(videoId)

            youTubeVideoDao.insertData(video)
        }
        deleteExtraVideos()
    }

    fun YoutubeVideoResponse.setCurrentPageTokenToVideos(currentPageToken: String): YoutubeVideoResponse {
        this.currentPageToken = currentPageToken
        this.items.forEach { videoItem ->
            videoItem.pageToken = currentPageToken
        }

        return this
    }

    private fun prepareVideo(video: YoutubeVideo, loadDate: OffsetDateTime) {
        video.loadedDate = loadDate
    }

    private fun YoutubeVideo.prepareVideoSnippet(videoId: String) {
        this.snippet.videoId = videoId
        this.snippet.snippetId = videoId
    }

    private fun YoutubeVideo.insertThumbnails(videoId: String) {
        this.snippet.thumbnails.thumbnailsId = videoId
        this.snippet.thumbnails.snippetId = videoId
    }

    private fun YoutubeVideo.insertThumbnailsAttributes(videoId: String) {
        this.snippet.thumbnails.medium.thumbnailAttributesId = videoId
        this.snippet.thumbnails.medium.thumbnailsId = videoId
    }

    private suspend fun YoutubeVideoResponse.insertPage() {
        val page = Page(this.nextPageToken, this.currentPageToken, this.prevPageToken)
        youTubeVideoDao.insertPages(page)
    }

    private fun YoutubeVideo.insertVideoStatistics(videoId: String) {
        this.statistics.statisticsId = videoId
        this.statistics.videoId = videoId
    }

    private fun YoutubeVideo.insertContentDetails(videoId: String) {
        this.contentDetails.contentDetailsId = videoId
        this.contentDetails.videoId = videoId
    }

    private suspend fun deleteExtraVideos() {
        val videosCount = youTubeVideoDao.getVideosCount()

        if (videosCount > 100) {
            youTubeVideoDao.deleteExtraFiveVideos()
        }
    }

//    suspend fun getVideosFromDatabase(pageToken: String): YoutubeVideoResponse {
//        return if (pageToken == "") getFirstPageFromDatabase()
//        else getParticularPageFromDatabase(pageToken)
//    }

//    private suspend fun getParticularPageFromDatabase(nextPageToken: String): YoutubeVideoResponse {
//        val pageWithVideos = youTubeVideoDao.getPageWithVideos(nextPageToken)
//        return convertPageWithVideosToYoutubeVideoResponse(pageWithVideos)
//    }
//
//    private suspend fun getFirstPageFromDatabase(): YoutubeVideoResponse {
//        val firstPageWithVideos = youTubeVideoDao.getFirstPageFromDb()
//        return convertPageWithVideosToYoutubeVideoResponse(firstPageWithVideos)
//    }
}