package com.vlascitchii.data_remote.enetity_api_model.video_list_api_model.videos_api_model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)

data class VideoStatisticsApiModel(
    val viewCount: Long = 0,
    val likeCount: Long = 0
)