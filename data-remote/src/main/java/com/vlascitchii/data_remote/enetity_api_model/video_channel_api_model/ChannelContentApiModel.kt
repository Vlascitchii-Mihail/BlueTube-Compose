package com.vlascitchii.data_remote.enetity_api_model.video_channel_api_model

import com.squareup.moshi.JsonClass
import com.vlascitchii.data_remote.enetity_api_model.video_list_api_model.ThumbnailsApiModel

@JsonClass(generateAdapter = true)
data class ChannelContentApiModel(
    val title: String = "",
    val description: String = "",
    val publishedAt: String = "",
    val thumbnails: ThumbnailsApiModel = ThumbnailsApiModel()
)
