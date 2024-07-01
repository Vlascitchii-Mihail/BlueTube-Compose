package com.appelier.bluetubecompose.search_video.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchVideoItemId(
    val videoId: String = ""
)
