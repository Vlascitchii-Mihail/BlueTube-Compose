package com.appelier.bluetubecompose.screen_video_list.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.appelier.bluetubecompose.core.core_database.URLSanitizer
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

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
@Parcelize
@Serializable
data class ThumbnailAttributes(
    @Serializable(with = URLSanitizer::class)
    val url: String = "",
    val height: Int = 0,
    val width: Int = 0,
    @PrimaryKey(autoGenerate = false)
    var thumbnailAttributesId: String = "",
    var thumbnailsId: String = ""
    )
    : Parcelable