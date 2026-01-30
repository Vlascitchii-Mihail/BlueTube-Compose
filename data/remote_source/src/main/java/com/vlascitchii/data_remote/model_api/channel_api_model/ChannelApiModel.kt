package com.vlascitchii.data_remote.model_api.channel_api_model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChannelApiModel(
    val id: String = "",
    val snippet: ChannelContentApiModel = ChannelContentApiModel()
)
