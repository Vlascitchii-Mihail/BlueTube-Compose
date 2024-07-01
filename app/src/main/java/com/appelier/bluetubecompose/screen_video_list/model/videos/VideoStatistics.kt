package com.appelier.bluetubecompose.screen_video_list.model.videos

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@Entity(
    tableName = "video_statistics",
    foreignKeys = [
        ForeignKey(
            entity = YoutubeVideo::class,
            parentColumns = ["id"],
            childColumns = ["videoId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
@JsonClass(generateAdapter = true)
data class VideoStatistics(
    val viewCount: Long = 0,
    val likeCount: Long = 0,
    @PrimaryKey(autoGenerate = false)
    var statisticsId: String = "",
    var videoId: String = ""
    )