package com.vlascitchii.data_local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.vlascitchii.data_local.enetity.PageEntity
import com.vlascitchii.data_local.enetity.video_list.ThumbnailAttributesEntity
import com.vlascitchii.data_local.enetity.video_list.ThumbnailsEntity
import com.vlascitchii.data_local.enetity.video_list.videos.ContentDetailsEntity
import com.vlascitchii.data_local.enetity.video_list.videos.VideoSnippetEntity
import com.vlascitchii.data_local.enetity.video_list.videos.VideoStatisticsEntity
import com.vlascitchii.data_local.enetity.video_list.videos.YoutubeVideoEntity
import com.vlascitchii.data_local.enetity.video_list.videos.YoutubeVideoResponseEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

@Dao
interface YouTubeVideoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPages(pageEntity: PageEntity)

    //TODO: unite / join with fun insertPages(pageEntity: PageEntity)
    @Transaction
    @Query("")
    suspend fun insertVideo(video: YoutubeVideoEntity) {
        insertParticularVideo(video)
        insertVideoStatistics(video.statistics)
        insertContentDetails(video.contentDetailsEntity)
        insertVideoSnippet(video.snippet)
        insertThumbnails(video.snippet.thumbnailsEntity)
        insertThumbnailsAttributes(video.snippet.thumbnailsEntity.medium)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertThumbnailsAttributes(thumbnailsAttributes: ThumbnailAttributesEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertThumbnails(thumbnailsEntityAttributes: ThumbnailsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideoSnippet(videoSnippet: VideoSnippetEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideoStatistics(videoStatistics: VideoStatisticsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContentDetails(contentDetailsEntity: ContentDetailsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParticularVideo(video: YoutubeVideoEntity)






    @Transaction
    @Query("")
    fun getVideosFromPage(currentPageToken: String): Flow<YoutubeVideoResponseEntity> {
        val videoResponseEntity = getPageByToken(currentPageToken)
        return videoResponseEntity.map { videoResponse: YoutubeVideoResponseEntity ->
            videoResponse.items.initializeVideos()
            videoResponse
        }
    }

    @Transaction
    @Query("SELECT * FROM pages WHERE currentPageToken = :currentPageToken")
    fun getPageByToken(currentPageToken: String): Flow<YoutubeVideoResponseEntity>




    @Transaction
    @Query("")
    fun getFirstPageFromDb(): Flow<YoutubeVideoResponseEntity> {
        val firstPage = getFirstPage()
        return firstPage.map { responseEntity: YoutubeVideoResponseEntity ->
            responseEntity.items.initializeVideos()
            responseEntity
        }
    }

    @Transaction
    @Query("SELECT * FROM pages WHERE currentPageToken = ''")
    fun getFirstPage(): Flow<YoutubeVideoResponseEntity>






    //TODO: check if it possible to get videos from DB at once with annotations, without initializing fields of the result
    @Transaction
    suspend fun List<YoutubeVideoEntity>.initializeVideos() {
        this.forEach { video ->
            val videoId = video.id
            video.statistics = getVideoStatistics(videoId).first()
            video.contentDetailsEntity = getVideoDetails(videoId).first()
            video.snippet = getVideoSnippet(videoId).first()
            video.snippet.thumbnailsEntity = getThumbnails(video.snippet.snippetId).first()
            video.snippet.thumbnailsEntity.medium = getThumbnailsAttributes(video.snippet.thumbnailsEntity.thumbnailsId).first()
        }
    }

    @Query("SELECT * FROM video_statistics WHERE videoId = :videoId")
    fun getVideoStatistics(videoId: String): Flow<VideoStatisticsEntity>

    @Query("SELECT * FROM content_details WHERE videoId = :videoId")
    fun getVideoDetails(videoId: String): Flow<ContentDetailsEntity>

    @Query("SELECT * FROM video_snippet WHERE videoId = :videoId")
    fun getVideoSnippet(videoId: String): Flow<VideoSnippetEntity>

    @Query("SELECT * FROM thumbnailsEntity WHERE snippetId = :snippetId")
    fun getThumbnails(snippetId: String): Flow<ThumbnailsEntity>

    @Query("SELECT * FROM thumbnails_attributes WHERE thumbnailsId = :thumbnailsId")
    fun getThumbnailsAttributes(thumbnailsId: String): Flow<ThumbnailAttributesEntity>



    @Query("DELETE FROM pages WHERE currentPageToken IN(SELECT pageToken FROM youtube_video ORDER BY loadedDate LIMIT 5)")
    suspend fun deleteExtraFiveVideos()

    @Query("SELECT COUNT(id) FROM youtube_video")
    fun getVideosCount(): Flow<Int>
}