package com.vlascitchii.data_remote.enetity_api_model.video_list_api_model.videos_api_model

import com.squareup.moshi.JsonClass
import com.vlascitchii.data_remote.enetity_api_model.video_list_api_model.ThumbnailsApiModel

@JsonClass(generateAdapter = true)

data class VideoSnippetApiModel(
    var title: String = "",
    var description: String = "",
    var publishedAt: String = "",
    var channelTitle: String = "",
    var channelImgUrl: String = "",
    var channelId: String = "",
    var thumbnails: ThumbnailsApiModel = ThumbnailsApiModel()
)
