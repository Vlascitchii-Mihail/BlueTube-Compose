package com.vlascitchii.data_remote.model_api.video_api_model

import com.squareup.moshi.JsonClass
import com.vlascitchii.domain.model.videos.YoutubeVideoDomain
import com.vlascitchii.domain.model.videos.YoutubeVideoResponseDomain

@JsonClass(generateAdapter = true)
data class YoutubeVideoResponseApiModel(
    val nextPageToken: String? = null,
    val currentPageToken: String = "",
    val prevPageToken: String? = null,
    val items: List<YoutubeVideoApiModel> = emptyList()
)
