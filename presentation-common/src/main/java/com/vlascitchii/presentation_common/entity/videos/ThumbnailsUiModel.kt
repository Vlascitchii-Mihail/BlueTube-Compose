package com.vlascitchii.presentation_common.entity.videos

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class ThumbnailsUiModel(
    var medium: ThumbnailAttributesUiModel = ThumbnailAttributesUiModel()
)
    : Parcelable