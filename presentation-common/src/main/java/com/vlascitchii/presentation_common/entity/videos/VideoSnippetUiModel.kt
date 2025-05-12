package com.vlascitchii.presentation_common.entity.videos

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable


@Parcelize
@Serializable
data class VideoSnippetUiModel(
    var title: String = "",
//    @Serializable(with = com.vlascitchii.data_local.database.URLSanitizer::class)
    var description: String = "",
    var publishedAt: String = "",
    var channelTitle: String = "",
//    @Serializable(with = com.vlascitchii.data_local.database.URLSanitizer::class)
    var channelImgUrl: String = "",
    var channelId: String = "",
    var thumbnailsUiModel: ThumbnailsUiModel = ThumbnailsUiModel(),
)
    : Parcelable
