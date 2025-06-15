package com.vlascitchii.presentation_common.entity.util

import androidx.paging.PagingData
import androidx.paging.map
import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideo
import com.vlascitchii.domain.util.VideoResult
import com.vlascitchii.presentation_common.entity.videos.ContentDetailsUiModel
import com.vlascitchii.presentation_common.entity.videos.ThumbnailAttributesUiModel
import com.vlascitchii.presentation_common.entity.videos.ThumbnailsUiModel
import com.vlascitchii.presentation_common.entity.videos.VideoSnippetUiModel
import com.vlascitchii.presentation_common.entity.videos.VideoStatisticsUiModel
import com.vlascitchii.presentation_common.entity.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_common.ui.state.UiState

abstract class CommonResultConverter<T: Any, R: Any> {

    abstract fun convertSuccess(data: T): R

    fun convert(result: VideoResult<T>): UiState<R> {
        return when(result) {
            is VideoResult.Error -> {
                UiState.Error(result.exception.localizedMessage.orEmpty())
            }

            is VideoResult.Success -> {
                UiState.Success(convertSuccess(result.data))
            }
        }
    }

    fun convertPager(youTubeVideoPagingData: PagingData<YoutubeVideo>): PagingData<YoutubeVideoUiModel> {
        return youTubeVideoPagingData.map { youTubeVideo: YoutubeVideo ->
            youTubeVideo.convertToYoutubeVideoUiMode()
        }
    }
}



fun YoutubeVideo.convertToYoutubeVideoUiMode(): YoutubeVideoUiModel {
    val youTubeVideo: YoutubeVideo = this

    val thumbnailsAttributesUiModel = ThumbnailAttributesUiModel(
        url = youTubeVideo.snippet.thumbnails.medium.url,
        height = youTubeVideo.snippet.thumbnails.medium.height,
        width = youTubeVideo.snippet.thumbnails.medium.width
    )

    val thumbnailsUiModel = ThumbnailsUiModel(
        medium = thumbnailsAttributesUiModel
    )

    val snippetUiModel = VideoSnippetUiModel(
        title = youTubeVideo.id,
        description = youTubeVideo.snippet.description,
        publishedAt = youTubeVideo.snippet.publishedAt,
        channelTitle = youTubeVideo.snippet.channelTitle,
        channelImgUrl = youTubeVideo.snippet.channelImgUrl,
        channelId = youTubeVideo.snippet.channelId,
        thumbnailsUiModel = thumbnailsUiModel
    )

    val videoStaticsUiModel = VideoStatisticsUiModel(
        viewCount = youTubeVideo.statistics.viewCount,
        likeCount = youTubeVideo.statistics.likeCount
    )
    val contentDetailsUiModel = ContentDetailsUiModel(
        duration = youTubeVideo.contentDetails.duration
    )

    return YoutubeVideoUiModel(
        id = youTubeVideo.id,
        pageToken = youTubeVideo.pageToken,
        snippet = snippetUiModel,
        statistics = videoStaticsUiModel,
        contentDetails = contentDetailsUiModel
    )
}