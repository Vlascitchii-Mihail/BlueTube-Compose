package com.appelier.bluetubecompose.screen_video_list.model.single_cnannel

import com.appelier.bluetubecompose.screen_video_list.model.single_cnannel.Channel.Companion.DEFAULT_CHANNEL_1
import com.appelier.bluetubecompose.screen_video_list.model.single_cnannel.Channel.Companion.DEFAULT_CHANNEL_2
import com.appelier.bluetubecompose.screen_video_list.model.single_cnannel.Channel.Companion.DEFAULT_CHANNEL_3
import com.appelier.bluetubecompose.screen_video_list.model.single_cnannel.Channel.Companion.DEFAULT_CHANNEL_4
import com.appelier.bluetubecompose.screen_video_list.model.single_cnannel.Channel.Companion.DEFAULT_CHANNEL_5
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class YoutubeChannelResponse(
    val items: List<Channel> = emptyList()
) {

    companion object {

        private val DEFAULT_YOUTUBE_CHANNEL_RESPONSE_1 = YoutubeChannelResponse(listOf(DEFAULT_CHANNEL_1))
        private val DEFAULT_YOUTUBE_CHANNEL_RESPONSE_2 = YoutubeChannelResponse(listOf(DEFAULT_CHANNEL_2))
        private val DEFAULT_YOUTUBE_CHANNEL_RESPONSE_3 = YoutubeChannelResponse(listOf(DEFAULT_CHANNEL_3))
        private val DEFAULT_YOUTUBE_CHANNEL_RESPONSE_4 = YoutubeChannelResponse(listOf(DEFAULT_CHANNEL_4))
        private val DEFAULT_YOUTUBE_CHANNEL_RESPONSE_5 = YoutubeChannelResponse(listOf(DEFAULT_CHANNEL_5))

        val DEFAULT_YOUTUBE_CHANNEL_RESPONSE_LIST = listOf(
            DEFAULT_YOUTUBE_CHANNEL_RESPONSE_1,
            DEFAULT_YOUTUBE_CHANNEL_RESPONSE_2,
            DEFAULT_YOUTUBE_CHANNEL_RESPONSE_3,
            DEFAULT_YOUTUBE_CHANNEL_RESPONSE_4,
            DEFAULT_YOUTUBE_CHANNEL_RESPONSE_5,
        )
    }
}
