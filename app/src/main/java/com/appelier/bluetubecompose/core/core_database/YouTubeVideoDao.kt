package com.appelier.bluetubecompose.core.core_database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.appelier.bluetubecompose.core.core_database.relations.PageWithVideos
import com.appelier.bluetubecompose.screen_video_list.model.Page
import com.appelier.bluetubecompose.screen_video_list.model.ThumbnailAttributes
import com.appelier.bluetubecompose.screen_video_list.model.Thumbnails
import com.appelier.bluetubecompose.screen_video_list.model.videos.ContentDetails
import com.appelier.bluetubecompose.screen_video_list.model.videos.VideoSnippet
import com.appelier.bluetubecompose.screen_video_list.model.videos.VideoStatistics
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideo

@Dao
interface YouTubeVideoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPages(page: Page)

    @Transaction
    @Query("")
    suspend fun insertData(video: YoutubeVideo) {
        insertVideo(video)
        insertVideoStatistics(video.statistics)
        insertContentDetails(video.contentDetails)
        insertVideoSnippet(video.snippet)
        insertThumbnails(video.snippet.thumbnails)
        insertThumbnailsAttributes(video.snippet.thumbnails.medium)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertThumbnailsAttributes(thumbnailsAttributes: ThumbnailAttributes)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertThumbnails(thumbnailsAttributes: Thumbnails)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideoSnippet(videoSnippet: VideoSnippet)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideoStatistics(videoStatistics: VideoStatistics)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContentDetails(contentDetails: ContentDetails)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideo(video: YoutubeVideo)



    @Transaction
    @Query("")
    suspend fun getPageWithVideos(currentPageToken: String): PageWithVideos {
        val pageWithVideos = getPageWithEmptyVideos(currentPageToken)
        pageWithVideos.videos.initializeVideos()
        return pageWithVideos
    }

    @Transaction
    @Query("SELECT * FROM pages WHERE currentPageToken = :currentPageToken")
    suspend fun getPageWithEmptyVideos(currentPageToken: String): PageWithVideos

    @Transaction
    suspend fun List<YoutubeVideo>.initializeVideos() {
        this.forEach { video ->
            val videoId = video.id
            video.statistics = getVideoStatistics(videoId)
            video.contentDetails = getVideoDetails(videoId)
            video.snippet = getVideoSnippet(videoId)
            video.snippet.thumbnails = getThumbnails(video.snippet.snippetId)
            video.snippet.thumbnails.medium = getThumbnailsAttributes(video.snippet.thumbnails.thumbnailsId)
        }
    }

    @Query("SELECT * FROM video_statistics WHERE videoId = :videoId")
    suspend fun getVideoStatistics(videoId: String): VideoStatistics

    @Query("SELECT * FROM content_details WHERE videoId = :videoId")
    suspend fun getVideoDetails(videoId: String): ContentDetails

    @Query("SELECT * FROM video_snippet WHERE videoId = :videoId")
    suspend fun getVideoSnippet(videoId: String): VideoSnippet

    @Query("SELECT * FROM thumbnails WHERE snippetId = :snippetId")
    suspend fun getThumbnails(snippetId: String): Thumbnails

    @Query("SELECT * FROM thumbnails_attributes WHERE thumbnailsId = :thumbnailsId")
    suspend fun getThumbnailsAttributes(thumbnailsId: String): ThumbnailAttributes



    @Query("DELETE FROM pages WHERE currentPageToken IN(SELECT pageToken FROM youtube_video ORDER BY loadedDate LIMIT 5)")
    suspend fun deleteExtraFiveVideos()

    @Query("SELECT COUNT(id) FROM youtube_video")
    suspend fun getVideosCount(): Int
}