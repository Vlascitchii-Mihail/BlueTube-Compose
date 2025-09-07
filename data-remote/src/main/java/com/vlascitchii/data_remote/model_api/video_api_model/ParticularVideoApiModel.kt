package com.vlascitchii.data_remote.model_api.video_api_model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ParticularVideoApiModel(
    val items: List<YoutubeVideoApiModel> = emptyList()
)
