package com.vlascitchii.data_remote.enetity_api_model.video_search_api_model

import com.vlascitchii.data_remote.enetity_api_model.video_list_api_model.videos_api_model.VideoSnippetApiModel
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchVideoItemApiModel(
    val id: SearchVideoItemIdApiModel,
    val snippet: VideoSnippetApiModel
)
