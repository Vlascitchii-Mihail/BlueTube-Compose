package com.vlascitchii.presentation_common.model.videos

import kotlinx.serialization.Serializable

@Serializable
data class VideoStatisticsUiModel(
    val viewCount: Long = 0,
    val likeCount: Long = 0
)
