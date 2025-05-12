package com.vlascitchii.domain.enetity.video_list.videos

import com.vlascitchii.domain.enetity.video_list.ThumbnailAttributes
import com.vlascitchii.domain.enetity.video_list.Thumbnails
import java.time.OffsetDateTime

data class YoutubeVideo(
    val id: String,
    var pageToken: String = "",
    var snippet: VideoSnippet = VideoSnippet(),
    var statistics: VideoStatistics = VideoStatistics(),
    var contentDetails: ContentDetails = ContentDetails(),
//    var loadedDate: OffsetDateTime? = null
) {

    companion object {
        private val thumbnailAttributes = ThumbnailAttributes(
            "https://i.ytimg.com/vi/m-4ZM3jxhdE/mqdefault.jpg",
            120,
            90
        )

        private val thumbnails = Thumbnails(
            thumbnailAttributes
        )

        private val snippet = VideoSnippet(
            title = "State of Play | May 30, 2024",
            description = "",
            publishedAt = "2024-05-30T22:00:12Z",
            channelTitle = "PlayStation",
            channelImgUrl = "https://yt3.ggpht.com/vIq4vVe_C7zq66KKHAtx89KagpDR1CuKkvgi96KtOaVgSTh67G3yJbTUkZ_o_ivUoG4Jxy9QkA=s240-c-k-c0x00ffffff-no-rj",
            channelId = "UC-2Y8dQb0S6DtpxNgAKoJKA",
            thumbnails = thumbnails
        )

        private val statistics = VideoStatistics(
            1372560,
            48971
        )

        private val contentDetails = ContentDetails(
            "PT35M32S"
        )

        val DEFAULT_VIDEO = YoutubeVideo(
            id = "m-4ZM3jxhdE",
            snippet = snippet,
            statistics = statistics,
            contentDetails = contentDetails
        )

        val DEFAULT_VIDEO_LIST = listOf(DEFAULT_VIDEO, DEFAULT_VIDEO, DEFAULT_VIDEO, DEFAULT_VIDEO)
    }
}
