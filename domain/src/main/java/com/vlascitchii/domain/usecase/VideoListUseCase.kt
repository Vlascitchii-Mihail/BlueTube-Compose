package com.vlascitchii.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideo
import com.vlascitchii.domain.paging.CommonPagerPager
import com.vlascitchii.domain.repository.VideoListRepository
import com.vlascitchii.domain.usecase.util.Configuration
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf


class VideoListUseCase(
    configuration: Configuration,
    private val videoListRepository: VideoListRepository
) : UseCase<VideoListUseCase.Request, VideoListUseCase.Response>(configuration) {

    data class Request(var pageToken: String = "") : UseCase.Request
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
                    videoListRepository.getVideos(request.pageToken)
                }
            }
        )

        return flowOf(Response(pager))
    }
}