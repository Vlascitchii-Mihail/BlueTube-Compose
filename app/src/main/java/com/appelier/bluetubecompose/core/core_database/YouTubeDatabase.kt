package com.appelier.bluetubecompose.core.core_database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.appelier.bluetubecompose.screen_video_list.model.Page
import com.appelier.bluetubecompose.screen_video_list.model.ThumbnailAttributes
import com.appelier.bluetubecompose.screen_video_list.model.Thumbnails
import com.appelier.bluetubecompose.screen_video_list.model.videos.ContentDetails
import com.appelier.bluetubecompose.screen_video_list.model.videos.VideoSnippet
import com.appelier.bluetubecompose.screen_video_list.model.videos.VideoStatistics
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideo

@Database(
    version = 1,
    entities = [
        YoutubeVideo::class,
        Page::class,
        VideoSnippet::class,
        VideoStatistics::class,
        ContentDetails::class,
        Thumbnails::class,
        ThumbnailAttributes::class
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