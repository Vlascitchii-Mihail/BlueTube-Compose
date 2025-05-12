package com.vlascitchii.data_local.enetity.video_list

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "thumbnails_attributes",
    foreignKeys = [
        ForeignKey(
            entity = ThumbnailsEntity::class,
            parentColumns = ["thumbnailsId"],
            childColumns = ["thumbnailsId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["thumbnailsId"])]
)
//@Parcelize
//@Serializable
data class ThumbnailAttributesEntity(
//    @Serializable(with = com.vlascitchii.data_local.database.URLSanitizer::class)
    val url: String = "",
    val height: Int = 0,
    val width: Int = 0,
    @PrimaryKey(autoGenerate = false)
    var thumbnailAttributesId: String = "",
    var thumbnailsId: String = ""
    )
//    : Parcelable