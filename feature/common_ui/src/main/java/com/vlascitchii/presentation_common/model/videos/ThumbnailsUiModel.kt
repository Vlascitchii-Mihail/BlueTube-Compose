package com.vlascitchii.presentation_common.model.videos

import kotlinx.serialization.Serializable

@Serializable
data class ThumbnailsUiModel(
    var medium: ThumbnailAttributesUiModel = ThumbnailAttributesUiModel()
)