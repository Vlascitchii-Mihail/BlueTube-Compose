package com.appelier.bluetubecompose.screen_video_list.model.single_cnannel

import com.squareup.moshi.JsonClass
import com.appelier.bluetubecompose.screen_video_list.model.Thumbnails

@JsonClass(generateAdapter = true)
data class ChannelContent(
    val title: String = "",
    val description: String = "",
    val publishedAt: String = "",
    val thumbnails: Thumbnails = Thumbnails()
)
