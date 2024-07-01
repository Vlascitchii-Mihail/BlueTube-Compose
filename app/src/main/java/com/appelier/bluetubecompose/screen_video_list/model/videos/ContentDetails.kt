package com.appelier.bluetubecompose.screen_video_list.model.videos

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@Entity(
    tableName = "content_details",
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
data class ContentDetails(
    val duration: String = "",
    @PrimaryKey(autoGenerate = false)
    var contentDetailsId: String = "",
    var videoId: String = ""
)