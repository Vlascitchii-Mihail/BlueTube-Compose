package com.appelier.bluetubecompose.screen_video_list.model.videos

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.appelier.bluetubecompose.core.core_database.URLSanitizer
import com.appelier.bluetubecompose.screen_video_list.model.Thumbnails
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Entity(
    tableName = "video_snippet",
    foreignKeys = [
        ForeignKey(
            entity = YoutubeVideo::class,
            parentColumns = ["id"],
            childColumns = ["videoId"],
            onDelete = ForeignKey.CASCADE,
        )
    ]
)
@JsonClass(generateAdapter = true)
@Parcelize
@Serializable
data class VideoSnippet(
    var title: String = "",
    @Serializable(with = URLSanitizer::class)
    var description: String = "",
    var publishedAt: String = "",
    var channelTitle: String = "",
    @Serializable(with = URLSanitizer::class)
    var channelImgUrl: String = "",
    var channelId: String = "",
    @Ignore
    var thumbnails: Thumbnails = Thumbnails(),
    @PrimaryKey(autoGenerate = false)
    var snippetId: String = "",
    var videoId: String = ""
)
    : Parcelable
