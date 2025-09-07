package com.vlascitchii.domain.model.videos

import com.vlascitchii.domain.model.ThumbnailsDomain

data class VideoSnippetDomain(
    var title: String = "",
    var description: String = "",
    var publishedAt: String = "",
    var channelTitle: String = "",
    var channelImgUrl: String = "",
    var channelId: String = "",
    var thumbnails: ThumbnailsDomain = ThumbnailsDomain(),
)
