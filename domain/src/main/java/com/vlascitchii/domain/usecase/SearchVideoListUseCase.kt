package com.vlascitchii.domain.usecase

import androidx.paging.PagingData
import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideo
import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideoResponse
import com.vlascitchii.domain.repository.VideoListRepository
import com.vlascitchii.domain.usecase.util.Configuration
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchVideoListUseCase(
    configuration: Configuration,
    private val videoListRepository: VideoListRepository
) : UseCase<SearchVideoListUseCase.Request, SearchVideoListUseCase.Response>(configuration) {

    data class Request(val query: String) : UseCase.Request
    data class Response(val youTubeSearchVideoPagingData: PagingData<YoutubeVideo>) : UseCase.Response

    override fun process(request: Request): Flow<Response> {

        return videoListRepository.getSearchVideos(request.query)
            .map { youTubeVideoResponse: PagingData<YoutubeVideo> ->
                Response(youTubeVideoResponse)
            }
    }
}