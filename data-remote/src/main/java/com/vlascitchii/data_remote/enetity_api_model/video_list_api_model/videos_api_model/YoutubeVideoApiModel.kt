package com.vlascitchii.data_remote.enetity_api_model.video_list_api_model.videos_api_model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class YoutubeVideoApiModel(
    val id: String = "",
    var pageToken: String = "",
    var snippet: VideoSnippetApiModel = VideoSnippetApiModel(),
    var statistics: VideoStatisticsApiModel = VideoStatisticsApiModel(),
    var contentDetails: ContentDetailsApiModel = ContentDetailsApiModel(),
)