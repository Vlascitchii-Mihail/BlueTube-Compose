package com.vlascitchii.presentation_common.model.videos

import kotlinx.serialization.Serializable

@Serializable
data class ThumbnailAttributesUiModel(
    val url: String = "",
    val height: Int = 0,
    val width: Int = 0
)
