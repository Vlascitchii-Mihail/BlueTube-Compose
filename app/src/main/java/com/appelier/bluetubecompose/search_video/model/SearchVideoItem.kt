package com.appelier.bluetubecompose.search_video.model

import com.appelier.bluetubecompose.screen_video_list.model.videos.VideoSnippet
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchVideoItem(
    val id: SearchVideoItemId,
    val snippet: VideoSnippet
)
