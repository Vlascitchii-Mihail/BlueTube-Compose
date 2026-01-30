package com.vlascitchii.data_remote.model_api.video_api_model

import com.squareup.moshi.JsonClass
import com.vlascitchii.domain.model.ThumbnailAttributesDomain
import com.vlascitchii.domain.model.ThumbnailsDomain
import com.vlascitchii.domain.model.videos.ContentDetailsDomain
import com.vlascitchii.domain.model.videos.VideoSnippetDomain
import com.vlascitchii.domain.model.videos.VideoStatisticsDomain
import com.vlascitchii.domain.model.videos.YoutubeVideoDomain
import com.vlascitchii.domain.model.videos.YoutubeVideoResponseDomain

@JsonClass(generateAdapter = true)
data class YoutubeVideoApiModel(
    val id: String = "",
    val pageToken: String = "",
    val snippet: VideoSnippetApiModel = VideoSnippetApiModel(),
    val statistics: VideoStatisticsApiModel = VideoStatisticsApiModel(),
    val contentDetails: ContentDetailsApiModel = ContentDetailsApiModel(),
)

internal fun List<YoutubeVideoApiModel>.convertToYoutubeVideoDomainList(): List<YoutubeVideoDomain> {
    return this.map { apiModelVideo: YoutubeVideoApiModel ->
        val thumbnailsAttributes = ThumbnailAttributesDomain(
            url = apiModelVideo.snippet.thumbnails.medium.url,
            height = apiModelVideo.snippet.thumbnails.medium.height,
            width = apiModelVideo.snippet.thumbnails.medium.width
        )

        val thumbnails = ThumbnailsDomain(
            medium = thumbnailsAttributes
        )

        val snippet = VideoSnippetDomain(
            title = apiModelVideo.snippet.title,
            description = apiModelVideo.snippet.description,
            publishedAt = apiModelVideo.snippet.publishedAt,
            channelTitle = apiModelVideo.snippet.channelTitle,
            channelImgUrl = apiModelVideo.snippet.channelImgUrl,
            channelId = apiModelVideo.snippet.channelId,
            thumbnails = thumbnails
        )

        val videoStatics = VideoStatisticsDomain(
            viewCount = apiModelVideo.statistics.viewCount,
            likeCount = apiModelVideo.statistics.likeCount
        )
        val contentDetails = ContentDetailsDomain(
            duration = apiModelVideo.contentDetails.duration
        )

        YoutubeVideoDomain(
            id = apiModelVideo.id,
            pageToken = apiModelVideo.pageToken,
            snippet = snippet,
            statistics = videoStatics,
            contentDetails = contentDetails
        )
    }
}

internal fun YoutubeVideoResponseApiModel.convertToDomainYouTubeVideoResponse(): YoutubeVideoResponseDomain {
    return YoutubeVideoResponseDomain(
        nextPageToken = this.nextPageToken,
        prevPageToken = this.prevPageToken,
        items = this.items.convertToYoutubeVideoDomainList()
    )
}
