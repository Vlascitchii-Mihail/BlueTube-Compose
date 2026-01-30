package com.vlascitchii.presentation_common.model.videos

import kotlinx.serialization.Serializable

@Serializable
data class ContentDetailsUiModel(
    val duration: String = "",
    var contentDetailsId: String = "",
    var videoId: String = ""
)
