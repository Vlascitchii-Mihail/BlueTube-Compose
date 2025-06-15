package com.vlascitchii.domain.usecase

import androidx.paging.PagingData
import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideo
import com.vlascitchii.domain.repository.PlayerRepository
import com.vlascitchii.domain.usecase.util.Configuration
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class VideoPlayerUseCase(
    configuration: Configuration,
    private val playerRepository: PlayerRepository
) : UseCase<VideoPlayerUseCase.Request, VideoPlayerUseCase.Response>(configuration){

    data class Request(val query: String) : UseCase.Request
    data class Response(val relatedVideoPagingData: PagingData<YoutubeVideo>) : UseCase.Response

    override fun process(request: Request): Flow<Response> {

        return playerRepository.getSearchRelayedVideos(request.query)
            .map { youTubeVideoResponse: PagingData<YoutubeVideo> ->
                Response(youTubeVideoResponse)
            }
    }
}