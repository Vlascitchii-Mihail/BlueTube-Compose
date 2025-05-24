package com.vlascitchii.data_local.enetity.video_list

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import com.vlascitchii.data_local.enetity.video_list.videos.VideoSnippetEntity

@Entity(
    tableName = "thumbnailsEntity",
    foreignKeys = [
        ForeignKey(
            entity = VideoSnippetEntity::class,
            parentColumns = ["snippetId"],
            childColumns = ["snippetId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["snippetId"])]
)
data class ThumbnailsEntity(
    @Ignore
    var medium: ThumbnailAttributesEntity = ThumbnailAttributesEntity(),
    @PrimaryKey(autoGenerate = false)
    var thumbnailsId: String = "",
    var snippetId: String = ""
    )
