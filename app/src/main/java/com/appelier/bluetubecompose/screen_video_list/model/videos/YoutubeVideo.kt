package com.appelier.bluetubecompose.screen_video_list.model.videos

import android.os.Parcelable
import com.appelier.bluetubecompose.screen_video_list.model.ThumbnailAttributes
import com.appelier.bluetubecompose.screen_video_list.model.Thumbnails
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class YoutubeVideo(
    val id: String,
    val snippet: VideoSnippet,
    val statistics: VideoStatistics,
    val contentDetails: ContentDetails
): Parcelable {

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
            "State of Play | May 30, 2024",
            "",
            "2024-05-30T22:00:12Z",
            "PlayStation",
            "https://yt3.ggpht.com/vIq4vVe_C7zq66KKHAtx89KagpDR1CuKkvgi96KtOaVgSTh67G3yJbTUkZ_o_ivUoG4Jxy9QkA=s240-c-k-c0x00ffffff-no-rj",
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


