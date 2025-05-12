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
//@Parcelize
//@Serializable
data class VideoSnippetEntity(
    var title: String = "",
//    @Serializable(with = com.vlascitchii.data_local.database.URLSanitizer::class)
    var description: String = "",
    var publishedAt: String = "",
    var channelTitle: String = "",
//    @Serializable(with = com.vlascitchii.data_local.database.URLSanitizer::class)
    var channelImgUrl: String = "",
    var channelId: String = "",
    @Ignore
    var thumbnailsEntity: ThumbnailsEntity = ThumbnailsEntity(),
    @PrimaryKey(autoGenerate = false)
    var snippetId: String = "",
    var videoId: String = ""
)
//    : Parcelable
