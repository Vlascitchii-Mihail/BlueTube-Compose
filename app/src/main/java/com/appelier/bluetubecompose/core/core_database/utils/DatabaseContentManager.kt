package com.appelier.bluetubecompose.core.core_database.utils

import android.util.Log
import com.appelier.bluetubecompose.core.core_database.YouTubeVideoDao
import com.appelier.bluetubecompose.screen_video_list.model.Page
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideo
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideoResponse
import com.appelier.bluetubecompose.utils.convertToYoutubeVideoResponse
import java.time.OffsetDateTime

const val VIDEO_LIMIT = 100

class DatabaseContentManager(private val youTubeVideoDao: YouTubeVideoDao) {

    suspend fun insertVideosToDatabaseWithTimeStamp(
        loadDate: OffsetDateTime = OffsetDateTime.now(),
        youTubeVideoResponse: YoutubeVideoResponse
    ) {
        youTubeVideoResponse.insertPage()

        youTubeVideoResponse.items.forEach { video ->
            val videoId = video.id
            video.setTimeStamp(loadDate)
            video.videoSnippetAddId(videoId)
            video.thumbnailsAddId(videoId)
            video.thumbnailsAttributesAddId(videoId)
            video.videoStatisticsAddId(videoId)
            video.contentDetailsAddId(videoId)

            youTubeVideoDao.insertVideo(video)
        }
        deleteExtraVideos()
    }

    fun setCurrentPageTokenToVideos(currentPageToken: String, videoResponse: YoutubeVideoResponse): YoutubeVideoResponse {
        videoResponse.currentPageToken = currentPageToken
        videoResponse.items.forEach { videoItem ->
            videoItem.pageToken = currentPageToken
        }

        return videoResponse
    }

    suspend fun getVideosFromDatabase(pageToken: String): YoutubeVideoResponse {
        return if (pageToken == "") getFirstPageFromDatabase()
        else getParticularPageFromDatabase(pageToken)
    }

    private fun YoutubeVideo.setTimeStamp(loadDate: OffsetDateTime) {
        this.loadedDate = loadDate
    }

    private fun YoutubeVideo.videoSnippetAddId(videoId: String) {
        this.snippet.videoId = videoId
        this.snippet.snippetId = videoId
    }

    private fun YoutubeVideo.thumbnailsAddId(videoId: String) {
        this.snippet.thumbnails.thumbnailsId = videoId
        this.snippet.thumbnails.snippetId = videoId
    }

    private fun YoutubeVideo.thumbnailsAttributesAddId(videoId: String) {
        this.snippet.thumbnails.medium.thumbnailAttributesId = videoId
        this.snippet.thumbnails.medium.thumbnailsId = videoId
    }

    private suspend fun YoutubeVideoResponse.insertPage() {
        val page = Page(this.nextPageToken, this.currentPageToken, this.prevPageToken)
        youTubeVideoDao.insertPages(page)
    }

    private fun YoutubeVideo.videoStatisticsAddId(videoId: String) {
        this.statistics.statisticsId = videoId
        this.statistics.videoId = videoId
    }

    private fun YoutubeVideo.contentDetailsAddId(videoId: String) {
        this.contentDetails.contentDetailsId = videoId
        this.contentDetails.videoId = videoId
    }

    private suspend fun deleteExtraVideos() {
        val videosCount = youTubeVideoDao.getVideosCount()

        if (videosCount > VIDEO_LIMIT) {
            youTubeVideoDao.deleteExtraFiveVideos()
        }
    }

    private suspend fun getParticularPageFromDatabase(nextPageToken: String): YoutubeVideoResponse {
        Log.d("Database", "getParticularPageFromDatabase() called with: nextPageToken = $nextPageToken")
        val pageWithVideos = youTubeVideoDao.getVideosFromPage(nextPageToken)
        return pageWithVideos.convertToYoutubeVideoResponse()
    }

    private suspend fun getFirstPageFromDatabase(): YoutubeVideoResponse {
        Log.d("Database", "getFirstPageFromDatabase() called")
        val firstPageWithVideos = youTubeVideoDao.getFirstPageFromDb()
        return firstPageWithVideos.convertToYoutubeVideoResponse()
    }
}