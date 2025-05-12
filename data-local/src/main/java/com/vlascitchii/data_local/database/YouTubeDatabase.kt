package com.vlascitchii.data_local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vlascitchii.data_local.enetity.video_list.videos.ContentDetailsEntity
import com.vlascitchii.data_local.enetity.video_list.videos.VideoSnippetEntity
import com.vlascitchii.data_local.enetity.video_list.videos.VideoStatisticsEntity
import com.vlascitchii.data_local.enetity.video_list.videos.YoutubeVideoEntity
import com.vlascitchii.data_local.enetity.PageEntity
import com.vlascitchii.data_local.enetity.video_list.ThumbnailAttributesEntity
import com.vlascitchii.data_local.enetity.video_list.ThumbnailsEntity

@Database(
    version = 1,
    entities = [
        YoutubeVideoEntity::class,
        PageEntity::class,
        VideoSnippetEntity::class,
        VideoStatisticsEntity::class,
        ContentDetailsEntity::class,
        ThumbnailsEntity::class,
        ThumbnailAttributesEntity::class
    ],
    exportSchema = false
)
@TypeConverters(Converter::class)
abstract class YouTubeDatabase: RoomDatabase() {

    abstract val youTubeVideoDao: YouTubeVideoDao

    companion object {
        const val ROOM_DATABASE = "YouTubeDatabase"
    }
}