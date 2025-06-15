package com.vlascitchii.presentation_common.entity.videos

data class YoutubeVideoResponseUiModel(
    val nextPageToken: String? = null,
    var currentPageToken: String = "",
    val prevPageToken: String? = null,
    val items: List<YoutubeVideoUiModel> = emptyList()
)