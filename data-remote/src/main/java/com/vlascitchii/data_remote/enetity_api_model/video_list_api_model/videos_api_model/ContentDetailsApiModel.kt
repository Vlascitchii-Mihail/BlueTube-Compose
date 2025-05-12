package com.vlascitchii.data_remote.enetity_api_model.video_list_api_model.videos_api_model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ContentDetailsApiModel(
    val duration: String = ""
)