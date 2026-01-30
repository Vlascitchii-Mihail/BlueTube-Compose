package com.vlascitchii.data_remote.model_api.video_api_model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ThumbnailAttributesApiModel(
    val url: String = "",
    val height: Int = 0,
    val width: Int = 0
)
