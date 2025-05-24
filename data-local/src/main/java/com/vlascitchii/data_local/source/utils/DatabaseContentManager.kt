package com.vlascitchii.data_local.source.utils

import com.vlascitchii.data_local.database.YouTubeVideoDao
import com.vlascitchii.data_local.enetity.INITIAL_PAGE_TOKEN
import com.vlascitchii.data_local.enetity.video_list.videos.YoutubeVideoEntity
import com.vlascitchii.data_local.enetity.video_list.videos.YoutubeVideoResponseEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import java.time.OffsetDateTime

const val VIDEO_LIMIT = 100

class DatabaseContentManager(private val youTubeVideoDao: YouTubeVideoDao) {

    var sourceCurrentPageToken = INITIAL_PAGE_TOKEN
    private var videoCount: Flow<Int> = flowOf(0)

    fun YoutubeVideoResponseEntity.setCurrentPageTokenToVideos(currentPageToken: String): YoutubeVideoResponseEntity {
        this.pageEntity.currentPageToken = currentPageToken
        this.items.forEach { videoItem ->
            videoItem.pageToken = currentPageToken
        }

        return this
    }

    suspend fun insertPageFrom(youtubeVideoResponseEntity: YoutubeVideoResponseEntity) {
        youTubeVideoDao.insertPages(youtubeVideoResponseEntity.pageEntity)
    }

    suspend fun YoutubeVideoResponseEntity.bindAndInsertVideoWith(loadDate: OffsetDateTime) {
        this.items.forEach { video: YoutubeVideoEntity ->
            video.bindVideosFromResponseWithData(loadDate)
            youTubeVideoDao.insertVideo(video)
        }
    }

    fun YoutubeVideoEntity.bindVideosFromResponseWithData(loadDate: OffsetDateTime) {
        val videoId = this.id
        this.setTimeStamp(loadDate)
        this.videoSnippetAddId(videoId)
        this.thumbnailsAddId(videoId)
        this.thumbnailsAttributesAddId(videoId)
        this.videoStatisticsAddId(videoId)
        this.contentDetailsAddId(videoId)

    }

    fun YoutubeVideoEntity.setTimeStamp(loadDate: OffsetDateTime) {
        this.loadedDate = loadDate
    }

    fun YoutubeVideoEntity.videoSnippetAddId(videoId: String) {
        this.snippet.videoId = videoId
        this.snippet.snippetId = videoId
    }

    fun YoutubeVideoEntity.thumbnailsAddId(videoId: String) {
        this.snippet.thumbnailsEntity.thumbnailsId = videoId
        this.snippet.thumbnailsEntity.snippetId = videoId
    }

    fun YoutubeVideoEntity.thumbnailsAttributesAddId(videoId: String) {
        this.snippet.thumbnailsEntity.medium.thumbnailAttributesId = videoId
        this.snippet.thumbnailsEntity.medium.thumbnailsId = videoId
    }

    fun YoutubeVideoEntity.videoStatisticsAddId(videoId: String) {
        this.statistics.statisticsId = videoId
        this.statistics.videoId = videoId
    }

    fun YoutubeVideoEntity.contentDetailsAddId(videoId: String) {
        this.contentDetailsEntity.contentDetailsId = videoId
        this.contentDetailsEntity.videoId = videoId
    }

    suspend fun deleteExtraVideos() {
        videoCount = youTubeVideoDao.getVideosCount()

        if (videoCount.first() > VIDEO_LIMIT) {
            youTubeVideoDao.deleteExtraFiveVideos()
        }
    }

    fun updateCurrentPageToken(videoResponseEntity:  YoutubeVideoResponseEntity?) {
        videoResponseEntity?.pageEntity?.nextPageToken?.let { sourceCurrentPageToken = it }
    }

    fun getFirstPageFromDatabase(): Flow<YoutubeVideoResponseEntity> {
        return youTubeVideoDao.getFirstPageFromDb()
    }

    fun getParticularPageFromDatabase(nextPageToken: String): Flow<YoutubeVideoResponseEntity> {
        return youTubeVideoDao.getVideosFromPage(nextPageToken)
    }
}