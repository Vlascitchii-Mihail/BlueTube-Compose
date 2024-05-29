package com.appelier.bluetubecompose.screen_video_list.model.videos

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ParticularVideo(
    val items: List<YoutubeVideo> = emptyList()
)
