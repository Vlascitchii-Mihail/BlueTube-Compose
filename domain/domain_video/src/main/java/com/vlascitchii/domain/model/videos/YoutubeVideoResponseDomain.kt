package com.vlascitchii.domain.model.videos

data class YoutubeVideoResponseDomain(
    val nextPageToken: String? = null,
    var currentPageToken: String = "",
    val prevPageToken: String? = null,
    val items: List<YoutubeVideoDomain> = emptyList()
)
