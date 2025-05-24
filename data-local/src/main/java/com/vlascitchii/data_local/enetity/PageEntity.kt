package com.vlascitchii.data_local.enetity

import androidx.room.Entity
import androidx.room.PrimaryKey

const val INITIAL_PAGE_TOKEN = ""

@Entity(tableName = "pages")
data class PageEntity(
    val nextPageToken: String? = null,
    @PrimaryKey(autoGenerate = false)
    var currentPageToken: String = INITIAL_PAGE_TOKEN,
    val prevPageToken: String? = null,
)
