package com.appelier.bluetubecompose.screen_video_list.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@Entity(
    tableName = "thumbnails_attributes",
    foreignKeys = [
        ForeignKey(
            entity = Thumbnails::class,
            parentColumns = ["thumbnailsId"],
            childColumns = ["thumbnailsId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
@JsonClass(generateAdapter = true)
data class ThumbnailAttributes(
    val url: String = "",
    val height: Int = 0,
    val width: Int = 0,
    @PrimaryKey(autoGenerate = false)
    var thumbnailAttributesId: String = "",
    var thumbnailsId: String = ""
    )