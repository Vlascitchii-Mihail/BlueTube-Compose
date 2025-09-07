package com.vlascitchii.data_remote.model_api.search_api_model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchVideoResponseApiModel (
    val nextPageToken: String = "",
    val prevPageToken: String? = null,
    val items: List<SearchVideoItemApiModel> = emptyList()
)
