package com.vlascitchii.presentation_common.model.videos

import kotlinx.serialization.Serializable

@Serializable
data class VideoSnippetUiModel(
    var title: String = "",
    var description: String = "",
    var publishedAt: String = "",
    var channelTitle: String = "",
    var channelImgUrl: String = "",
    var channelId: String = "",
    var thumbnailsUiModel: ThumbnailsUiModel = ThumbnailsUiModel(),
)
