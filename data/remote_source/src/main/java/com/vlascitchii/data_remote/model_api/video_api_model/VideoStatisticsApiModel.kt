package com.vlascitchii.data_remote.model_api.video_api_model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)

data class VideoStatisticsApiModel(
    val viewCount: Long = 0,
    val likeCount: Long = 0
)
