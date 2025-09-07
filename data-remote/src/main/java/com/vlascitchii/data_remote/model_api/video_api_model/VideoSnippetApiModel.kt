package com.vlascitchii.data_remote.model_api.video_api_model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)

data class VideoSnippetApiModel(
    val title: String = "",
    val description: String = "",
    val publishedAt: String = "",
    val channelTitle: String = "",
    val channelImgUrl: String = "",
    val channelId: String = "",
    val thumbnails: ThumbnailsApiModel = ThumbnailsApiModel()
)
