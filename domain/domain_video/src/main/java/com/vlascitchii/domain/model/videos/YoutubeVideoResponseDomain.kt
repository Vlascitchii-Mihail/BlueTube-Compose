package com.vlascitchii.domain.model.videos

data class YoutubeVideoResponseDomain(
    val nextPageToken: String = "",
    var currentPageToken: String = "",
    val prevPageToken: String = "",
    val items: List<YoutubeVideoDomain> = emptyList()
)
