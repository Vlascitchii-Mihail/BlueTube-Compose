package com.vlascitchii.data_remote.enetity_api_model.util

import com.vlascitchii.data_remote.enetity_api_model.video_list_api_model.ThumbnailAttributesApiModel
import com.vlascitchii.data_remote.enetity_api_model.video_list_api_model.ThumbnailsApiModel
import com.vlascitchii.data_remote.enetity_api_model.video_list_api_model.videos_api_model.ContentDetailsApiModel
import com.vlascitchii.data_remote.enetity_api_model.video_list_api_model.videos_api_model.VideoSnippetApiModel
import com.vlascitchii.data_remote.enetity_api_model.video_list_api_model.videos_api_model.VideoStatisticsApiModel
import com.vlascitchii.data_remote.enetity_api_model.video_list_api_model.videos_api_model.YoutubeVideoApiModel
import com.vlascitchii.data_remote.enetity_api_model.video_list_api_model.videos_api_model.YoutubeVideoResponseApiModel
import com.vlascitchii.domain.enetity.video_list.ThumbnailAttributes
import com.vlascitchii.domain.enetity.video_list.Thumbnails
import com.vlascitchii.domain.enetity.video_list.videos.ContentDetails
import com.vlascitchii.domain.enetity.video_list.videos.VideoSnippet
import com.vlascitchii.domain.enetity.video_list.videos.VideoStatistics
import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideo
import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideoResponse

internal fun List<YoutubeVideoApiModel>.convertToYoutubeVideoList(): List<YoutubeVideo> {
    return this.map { apiModelVideo: YoutubeVideoApiModel ->
        val thumbnailsAttributes = ThumbnailAttributes(
            url = apiModelVideo.snippet.thumbnails.medium.url,
            height = apiModelVideo.snippet.thumbnails.medium.height,
            width = apiModelVideo.snippet.thumbnails.medium.width
        )

        val thumbnails = Thumbnails(
            medium = thumbnailsAttributes
        )

        val snippet = VideoSnippet(
            title = apiModelVideo.snippet.title,
            description = apiModelVideo.snippet.description,
            publishedAt = apiModelVideo.snippet.publishedAt,
            channelTitle = apiModelVideo.snippet.channelTitle,
            channelImgUrl = apiModelVideo.snippet.channelImgUrl,
            channelId = apiModelVideo.snippet.channelId,
            thumbnails = thumbnails
        )

        val videoStatics = VideoStatistics(
            viewCount = apiModelVideo.statistics.viewCount,
            likeCount = apiModelVideo.statistics.likeCount
        )
        val contentDetails = ContentDetails(
            duration = apiModelVideo.contentDetails.duration
        )

        YoutubeVideo(
            id = apiModelVideo.id,
            pageToken = apiModelVideo.pageToken,
            snippet = snippet,
            statistics = videoStatics,
            contentDetails = contentDetails
        )
    }
}

internal fun YoutubeVideoResponseApiModel.convertToYouTubeVideoResponse(): YoutubeVideoResponse {
    return YoutubeVideoResponse(
        nextPageToken = this.nextPageToken,
        prevPageToken = this.prevPageToken,
        items = this.items.convertToYoutubeVideoList()
    )
}



internal fun List<YoutubeVideo>.convertToYoutubeVideoApiModelList(): List<YoutubeVideoApiModel> {
    return this.map { youTubeVideo: YoutubeVideo ->
        val thumbnailsAttributes = ThumbnailAttributesApiModel(
            url = youTubeVideo.snippet.thumbnails.medium.url,
            height = youTubeVideo.snippet.thumbnails.medium.height,
            width = youTubeVideo.snippet.thumbnails.medium.width
        )

        val thumbnails = ThumbnailsApiModel(
            medium = thumbnailsAttributes
        )

        val snippet = VideoSnippetApiModel(
            title = youTubeVideo.snippet.title,
            description = youTubeVideo.snippet.description,
            publishedAt = youTubeVideo.snippet.publishedAt,
            channelTitle = youTubeVideo.snippet.channelTitle,
            channelImgUrl = youTubeVideo.snippet.channelImgUrl,
            channelId = youTubeVideo.snippet.channelId,
            thumbnails = thumbnails
        )

        val videoStatics = VideoStatisticsApiModel(
            viewCount = youTubeVideo.statistics.viewCount,
            likeCount = youTubeVideo.statistics.likeCount
        )
        val contentDetails = ContentDetailsApiModel(
            duration = youTubeVideo.contentDetails.duration
        )

        YoutubeVideoApiModel(
            id = youTubeVideo.id,
            pageToken = youTubeVideo.pageToken,
            snippet = snippet,
            statistics = videoStatics,
            contentDetails = contentDetails
        )
    }
}

internal fun YoutubeVideoResponse.convertToYouTubeVideoResponseApiModel(): YoutubeVideoResponseApiModel {
    return YoutubeVideoResponseApiModel(
        nextPageToken = this.nextPageToken,
        prevPageToken = this.prevPageToken,
        items = this.items.convertToYoutubeVideoApiModelList()
    )
}
