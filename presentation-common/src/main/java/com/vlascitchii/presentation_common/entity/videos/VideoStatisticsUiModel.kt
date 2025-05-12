package com.vlascitchii.presentation_common.entity.videos

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable


@Parcelize
@Serializable
data class VideoStatisticsUiModel(
    val viewCount: Long = 0,
    val likeCount: Long = 0
)
    : Parcelable