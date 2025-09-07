package com.vlascitchii.domain.model.videos

data class YoutubeVideoDomain(
    val id: String,
    var pageToken: String = "",
    var snippet: VideoSnippetDomain = VideoSnippetDomain(),
    var statistics: VideoStatisticsDomain = VideoStatisticsDomain(),
    var contentDetails: ContentDetailsDomain = ContentDetailsDomain(),
)
