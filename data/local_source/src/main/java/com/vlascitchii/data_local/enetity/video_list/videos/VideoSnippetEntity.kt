package com.vlascitchii.data_local.enetity.video_list.videos

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import com.vlascitchii.data_local.enetity.video_list.ThumbnailsEntity

@Entity(
    tableName = "video_snippet",
    foreignKeys = [
        ForeignKey(
            entity = YoutubeVideoEntity::class,
            parentColumns = ["id"],
            childColumns = ["videoId"],
            onDelete = ForeignKey.CASCADE,
        )
    ],
    indices = [Index(value = ["videoId"])]
)
data class VideoSnippetEntity(
    var title: String = "",
    var description: String = "",
    var publishedAt: String = "",
    var channelTitle: String = "",
    var channelImgUrl: String = "",
    var channelId: String = "",
    @Ignore
    var thumbnailsEntity: ThumbnailsEntity = ThumbnailsEntity(),
    @PrimaryKey(autoGenerate = false)
    var snippetId: String = "",
    var videoId: String = ""
)
