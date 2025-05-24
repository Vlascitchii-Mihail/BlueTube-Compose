package com.vlascitchii.data_local.enetity.video_list.videos

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "content_details",
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
data class ContentDetailsEntity(
    val duration: String = "",
    @PrimaryKey(autoGenerate = false)
    var contentDetailsId: String = "",
    var videoId: String = ""
)