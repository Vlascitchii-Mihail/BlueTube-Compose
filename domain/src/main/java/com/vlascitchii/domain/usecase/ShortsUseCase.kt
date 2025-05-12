package com.vlascitchii.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideo
import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideoResponse
import com.vlascitchii.domain.paging.CommonPagerPager
import com.vlascitchii.domain.repository.ShortsRepository
import com.vlascitchii.domain.usecase.VideoPlayerUseCase.Response
import com.vlascitchii.domain.usecase.util.Configuration
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class ShortsUseCase(
    configuration: Configuration,
    private val shortsRepository: ShortsRepository
) : UseCase<ShortsUseCase.Request, ShortsUseCase.Response>(configuration) {

    data class Request(var pageToken: String) : UseCase.Request
    data class Response(val pager: Pager<String, YoutubeVideo>) : UseCase.Response

    override fun process(request: Request): Flow<Response>{
        val pager = Pager(
            config = PagingConfig(
                pageSize = 5,
                prefetchDistance = 15
            ),
            pagingSourceFactory = {
                CommonPagerPager { pageToken: String ->
                    request.pageToken = pageToken
                    shortsRepository.getShorts(request.pageToken)
                }
            }
        )

        return flowOf(Response(pager))
    }
}