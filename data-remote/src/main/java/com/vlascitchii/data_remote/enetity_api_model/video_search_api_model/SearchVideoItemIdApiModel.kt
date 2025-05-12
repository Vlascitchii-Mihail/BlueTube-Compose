package com.vlascitchii.data_remote.enetity_api_model.video_search_api_model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchVideoItemIdApiModel(
    val videoId: String = ""
)
