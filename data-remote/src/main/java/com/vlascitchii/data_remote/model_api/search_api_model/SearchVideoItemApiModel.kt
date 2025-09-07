package com.vlascitchii.data_remote.model_api.search_api_model

import com.vlascitchii.data_remote.model_api.video_api_model.VideoSnippetApiModel
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchVideoItemApiModel(
    val id: SearchVideoItemIdApiModel,
    val snippet: VideoSnippetApiModel
)
