package com.vlascitchii.data_remote.enetity_api_model.video_channel_api_model

import com.squareup.moshi.JsonClass
import com.vlascitchii.data_remote.enetity_api_model.video_channel_api_model.ChannelApiModel.Companion.DEFAULT_CHANNEL_1
import com.vlascitchii.data_remote.enetity_api_model.video_channel_api_model.ChannelApiModel.Companion.DEFAULT_CHANNEL_2
import com.vlascitchii.data_remote.enetity_api_model.video_channel_api_model.ChannelApiModel.Companion.DEFAULT_CHANNEL_3
import com.vlascitchii.data_remote.enetity_api_model.video_channel_api_model.ChannelApiModel.Companion.DEFAULT_CHANNEL_4
import com.vlascitchii.data_remote.enetity_api_model.video_channel_api_model.ChannelApiModel.Companion.DEFAULT_CHANNEL_5

@JsonClass(generateAdapter = true)
data class YoutubeChannelResponseApiModel(
    val items: List<ChannelApiModel> = emptyList()
) {

    companion object {

        private val DEFAULT_YOUTUBE_CHANNEL_RESPONSE_1 = YoutubeChannelResponseApiModel(listOf(DEFAULT_CHANNEL_1))
        private val DEFAULT_YOUTUBE_CHANNEL_RESPONSE_2 = YoutubeChannelResponseApiModel(listOf(DEFAULT_CHANNEL_2))
        private val DEFAULT_YOUTUBE_CHANNEL_RESPONSE_3 = YoutubeChannelResponseApiModel(listOf(DEFAULT_CHANNEL_3))
        private val DEFAULT_YOUTUBE_CHANNEL_RESPONSE_4 = YoutubeChannelResponseApiModel(listOf(DEFAULT_CHANNEL_4))
        private val DEFAULT_YOUTUBE_CHANNEL_RESPONSE_5 = YoutubeChannelResponseApiModel(listOf(DEFAULT_CHANNEL_5))

        val DEFAULT_YOUTUBE_CHANNEL_RESPONSE_LIST = listOf(
            DEFAULT_YOUTUBE_CHANNEL_RESPONSE_1,
            DEFAULT_YOUTUBE_CHANNEL_RESPONSE_2,
            DEFAULT_YOUTUBE_CHANNEL_RESPONSE_3,
            DEFAULT_YOUTUBE_CHANNEL_RESPONSE_4,
            DEFAULT_YOUTUBE_CHANNEL_RESPONSE_5,
        )
    }
}
