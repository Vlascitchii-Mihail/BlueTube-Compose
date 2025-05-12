package com.vlascitchii.data_local.enetity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "pages")
data class PageEntity(
    val nextPageToken: String? = null,
    @PrimaryKey(autoGenerate = false)
    var currentPageToken: String = "",
    val prevPageToken: String? = null,
)
