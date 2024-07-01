package com.appelier.bluetubecompose.screen_video_list.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pages")
data class Page(
    val nextPageToken: String? = null,
    @PrimaryKey(autoGenerate = false)
    var currentPageToken: String = "",
    val prevPageToken: String? = null,
)
