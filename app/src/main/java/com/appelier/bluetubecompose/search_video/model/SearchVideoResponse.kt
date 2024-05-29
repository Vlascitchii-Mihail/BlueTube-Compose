package com.appelier.bluetubecompose.search_video.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchVideoResponse (
    val nextPageToken: String = "",
    val prevPageToken: String? = null,
    val items: List<SearchVideoItem> = emptyList()
)