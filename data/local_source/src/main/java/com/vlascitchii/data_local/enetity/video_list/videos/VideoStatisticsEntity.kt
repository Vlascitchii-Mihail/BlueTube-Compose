package com.vlascitchii.data_local.enetity.video_list.videos

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "video_statistics",
    foreignKeys = [
        ForeignKey(
            entity = YoutubeVideoEntity::class,
            parentColumns = ["id"],
            childColumns = ["videoId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["videoId"])]
)
data class VideoStatisticsEntity(
    val viewCount: Long = 0,
    val likeCount: Long = 0,
    @PrimaryKey(autoGenerate = false)
    var statisticsId: String = "",
    var videoId: String = "",
    )
