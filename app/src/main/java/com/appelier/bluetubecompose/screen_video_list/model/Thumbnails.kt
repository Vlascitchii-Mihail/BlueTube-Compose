package com.appelier.bluetubecompose.screen_video_list.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.appelier.bluetubecompose.screen_video_list.model.videos.VideoSnippet
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Entity(
    tableName = "thumbnails",
    foreignKeys = [
        ForeignKey(
            entity = VideoSnippet::class,
            parentColumns = ["snippetId"],
            childColumns = ["snippetId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
@JsonClass(generateAdapter = true)
@Parcelize
@Serializable
data class Thumbnails(
    @Ignore
    var medium: ThumbnailAttributes = ThumbnailAttributes(),
    @PrimaryKey(autoGenerate = false)
    var thumbnailsId: String = "",
    var snippetId: String = ""
    )
    : Parcelable