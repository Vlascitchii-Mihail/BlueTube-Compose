package com.vlascitchii.presentation_common.model.videos

import kotlinx.serialization.Serializable

@Serializable
data class YoutubeVideoUiModel(
    val id: String = "",
    var pageToken: String = "",
    var snippet: VideoSnippetUiModel = VideoSnippetUiModel(),
    var statistics: VideoStatisticsUiModel = VideoStatisticsUiModel(),
    var contentDetails: ContentDetailsUiModel = ContentDetailsUiModel()
)
{

    companion object {
        private val thumbnailAttributes = ThumbnailAttributesUiModel(
            "https://i.ytimg.com/vi/m-4ZM3jxhdE/mqdefault.jpg",
            120,
            90
        )

        private val thumbnailsUiModel = ThumbnailsUiModel(
            thumbnailAttributes
        )

        private val snippet = VideoSnippetUiModel(
            title = "State of Play | May 30, 2024",
            description = "",
            publishedAt = "2024-05-30T22:00:12Z",
            channelTitle = "PlayStation",
            channelImgUrl = "https://yt3.ggpht.com/vIq4vVe_C7zq66KKHAtx89KagpDR1CuKkvgi96KtOaVgSTh67G3yJbTUkZ_o_ivUoG4Jxy9QkA=s240-c-k-c0x00ffffff-no-rj",
            channelId = "UC-2Y8dQb0S6DtpxNgAKoJKA",
            thumbnailsUiModel = thumbnailsUiModel
        )

        private val statistics = VideoStatisticsUiModel(
            1372560,
            48971
        )

        private val contentDetails = ContentDetailsUiModel(
            "PT35M32S"
        )

        val DEFAULT_VIDEO = YoutubeVideoUiModel(
            id = "m-4ZM3jxhdE",
            snippet = snippet,
            statistics = statistics,
            contentDetails = contentDetails
        )

        val PREVIEW_VIDEO_LIST = listOf(DEFAULT_VIDEO, DEFAULT_VIDEO, DEFAULT_VIDEO, DEFAULT_VIDEO)
    }
}
