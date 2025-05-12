package com.vlascitchii.data_remote.enetity_api_model.video_list_api_model.videos_api_model

import com.squareup.moshi.JsonClass
import com.vlascitchii.data_remote.enetity_api_model.video_list_api_model.ThumbnailAttributesApiModel
import com.vlascitchii.data_remote.enetity_api_model.video_list_api_model.ThumbnailsApiModel
import com.vlascitchii.domain.enetity.video_list.ThumbnailAttributes
import com.vlascitchii.domain.enetity.video_list.Thumbnails
import com.vlascitchii.domain.enetity.video_list.videos.ContentDetails
import com.vlascitchii.domain.enetity.video_list.videos.VideoSnippet
import com.vlascitchii.domain.enetity.video_list.videos.VideoStatistics
import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideo

@JsonClass(generateAdapter = true)
data class ParticularVideoApiModel(
    val items: List<YoutubeVideoApiModel> = emptyList()
) {

    companion object {
        val particularVideoExample =  YoutubeVideoApiModel(
            id = "bbM6aSB6iMQ",
            "",
            snippet = VideoSnippetApiModel(
                "State of Origin 2024 | Blues v Maroons | Match Highlights",
                "The New South Wales Blues and the Queensland Maroons face off in Game 1 of the 2024 Ampol State of Origin series\n\n🏉 SUBSCRIBE FOR MORE NRL ACTION 🏉\nhttps://www.youtube.com/channel/UC33-OkQ6VCYk8xtml8Pk4-g?sub_confirmation=1\n\n🏉 Subscribe to the official NRLW Channel 🏉https://www.youtube.com/channel/UCME0EifJ3Xm3mGmLdBz1Kyw?sub_confirmation=1\n\nTo keep up to date with all the latest NRL content head to:\nNRL Watch: https://www.nrl.com/watch/\nNRL Tickets: https://tickets.nrl.com/\nNRL Draw: https://www.nrl.com/draw/",
                "2024-06-05T12:22:55Z",
                "NRL - National Rugby League",
                "",
                channelId = "UC33-OkQ6VCYk8xtml8Pk4-g",
                thumbnails = ThumbnailsApiModel(
                    ThumbnailAttributesApiModel(
                        "https://i.ytimg.com/vi/bbM6aSB6iMQ/mqdefault.jpg",
                        180,
                        320
                    )
                )
            ),
            statistics = VideoStatisticsApiModel(
                400242,
                3593
            ),
            contentDetails = ContentDetailsApiModel(
                "PT5M"
            )
        )
    }
}


