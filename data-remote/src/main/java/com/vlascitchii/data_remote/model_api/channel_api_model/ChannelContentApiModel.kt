package com.vlascitchii.data_remote.model_api.channel_api_model

import com.squareup.moshi.JsonClass
import com.vlascitchii.data_remote.model_api.video_api_model.ThumbnailsApiModel

@JsonClass(generateAdapter = true)
data class ChannelContentApiModel(
    val title: String = "",
    val description: String = "",
    val publishedAt: String = "",
    val thumbnails: ThumbnailsApiModel = ThumbnailsApiModel()
)
