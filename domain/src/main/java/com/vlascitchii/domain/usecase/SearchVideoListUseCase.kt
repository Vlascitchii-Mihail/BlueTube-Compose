package com.vlascitchii.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideo
import com.vlascitchii.domain.paging.CommonPagerPager
import com.vlascitchii.domain.repository.VideoListRepository
import com.vlascitchii.domain.usecase.util.Configuration
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class SearchVideoListUseCase(
    configuration: Configuration,
    private val videoListRepository: VideoListRepository
) : UseCase<SearchVideoListUseCase.Request, SearchVideoListUseCase.Response>(configuration) {

    data class Request(val query: String, var pageToken: String) : UseCase.Request
    data class Response(val pager: Pager<String, YoutubeVideo>) : UseCase.Response

    override fun process(request: Request): Flow<Response> {
        val pager = Pager(
            config = PagingConfig(
                pageSize = 5,
                prefetchDistance = 15
            ),
            pagingSourceFactory = {
                CommonPagerPager { pageToken: String ->
                    request.pageToken = pageToken
                    videoListRepository.getSearchVideos(request.query, request.pageToken)
                }
            }
        )

        return flowOf(Response(pager))
    }
}