package com.vlascitchii.domain.usecase

import androidx.paging.PagingData
import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideo
import com.vlascitchii.domain.repository.VideoListRepository
import com.vlascitchii.domain.usecase.util.Configuration
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class VideoListUseCase(
    configuration: Configuration,
    private val videoListRepository: VideoListRepository
) : UseCase<VideoListUseCase.Request, VideoListUseCase.Response>(configuration) {

    object Request : UseCase.Request
    data class Response(val youTubePopularVideoPagingData: PagingData<YoutubeVideo>) : UseCase.Response

    override fun process(request: Request): Flow<Response> {

        return videoListRepository.getPopularVideos()
            .map { youTubeVideoResponse: PagingData<YoutubeVideo> ->
                Response(youTubeVideoResponse)
            }
    }
}