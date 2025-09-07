package com.vlascitchii.presentation_common.model.util

import androidx.paging.PagingData
import androidx.paging.map
import com.vlascitchii.domain.model.videos.YoutubeVideoDomain
import com.vlascitchii.domain.util.VideoResult
import com.vlascitchii.presentation_common.model.videos.ContentDetailsUiModel
import com.vlascitchii.presentation_common.model.videos.ThumbnailAttributesUiModel
import com.vlascitchii.presentation_common.model.videos.ThumbnailsUiModel
import com.vlascitchii.presentation_common.model.videos.VideoSnippetUiModel
import com.vlascitchii.presentation_common.model.videos.VideoStatisticsUiModel
import com.vlascitchii.presentation_common.model.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_common.ui.state.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

abstract class CommonResultConverter<RECEIVED: Any, CONVERTED: Any> {

    abstract fun convertSuccess(data: RECEIVED): CONVERTED

    fun convert(result: VideoResult<RECEIVED>): UiState<CONVERTED> {
        return when(result) {
            is VideoResult.Error -> {
                UiState.Error(result.exception.errorDomain?.message.orEmpty())
            }

            is VideoResult.Success -> {
                UiState.Success(convertSuccess(result.data))
            }
        }
    }

    fun convertPager(youTubeVideoPagingData: Flow<PagingData<YoutubeVideoDomain>>): Flow<PagingData<YoutubeVideoUiModel>> {
        return youTubeVideoPagingData.map { youTubePagingData: PagingData<YoutubeVideoDomain> ->
            youTubePagingData.map { youTubeVideo ->
                youTubeVideo.convertToYoutubeVideoUiModel()
            }
        }
    }
}

fun YoutubeVideoDomain.convertToYoutubeVideoUiModel(): YoutubeVideoUiModel {
    val youTubeVideo: YoutubeVideoDomain = this

    val thumbnailsAttributesUiModel = ThumbnailAttributesUiModel(
        url = youTubeVideo.snippet.thumbnails.medium.url,
        height = youTubeVideo.snippet.thumbnails.medium.height,
        width = youTubeVideo.snippet.thumbnails.medium.width
    )

    val thumbnailsUiModel = ThumbnailsUiModel(
        medium = thumbnailsAttributesUiModel
    )

    val snippetUiModel = VideoSnippetUiModel(
        title = youTubeVideo.snippet.title,
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
