package com.vlascitchii.data_remote.model_api.video_api_model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class YoutubeVideoResponseApiModel(
    val nextPageToken: String = "",
    val currentPageToken: String = "",
    val prevPageToken: String = "",
    val items: List<YoutubeVideoApiModel> = emptyList()
)
