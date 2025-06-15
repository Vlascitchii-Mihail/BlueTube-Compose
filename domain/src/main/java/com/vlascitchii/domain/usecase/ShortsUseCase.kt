package com.vlascitchii.domain.usecase

import androidx.paging.PagingData
import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideo
import com.vlascitchii.domain.repository.ShortsRepository
import com.vlascitchii.domain.usecase.util.Configuration
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ShortsUseCase(
    configuration: Configuration,
    private val shortsRepository: ShortsRepository
) : UseCase<ShortsUseCase.Request, ShortsUseCase.Response>(configuration) {

    object Request: UseCase.Request
    data class Response(val shortsPopularVideoPagingData: PagingData<YoutubeVideo>) : UseCase.Response

    override fun process(request: Request): Flow<Response>{

        return shortsRepository.getShorts().map { youTubeVideoResponse: PagingData<YoutubeVideo> ->
            Response(youTubeVideoResponse)
        }
    }
}