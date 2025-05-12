package com.vlascitchii.domain.enetity.video_list.videos

import com.vlascitchii.domain.enetity.video_list.Thumbnails

data class VideoSnippet(
    var title: String = "",
    var description: String = "",
    var publishedAt: String = "",
    var channelTitle: String = "",
    var channelImgUrl: String = "",
    var channelId: String = "",
    var thumbnails: Thumbnails = Thumbnails(),
)
