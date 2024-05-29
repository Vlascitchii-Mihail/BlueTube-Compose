package com.appelier.bluetubecompose.screen_video_list.model.single_cnannel

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Channel(
    val id: String = "",
    val snippet: ChannelContent = ChannelContent()
)
