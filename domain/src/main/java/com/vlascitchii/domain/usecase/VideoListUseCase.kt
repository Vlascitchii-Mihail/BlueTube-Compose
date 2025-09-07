package com.vlascitchii.domain.usecase

import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.vlascitchii.domain.model.videos.YoutubeVideoDomain
import com.vlascitchii.domain.repository.VideoListRepository
import com.vlascitchii.domain.usecase.util.DispatcherConfiguration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class VideoListUseCase(
    dispatcherConfiguration: DispatcherConfiguration,
    private val videoListRepository: VideoListRepository
) : UseCase<VideoListUseCase.VideoListRequest, VideoListUseCase.VideoListResponse>(dispatcherConfiguration) {

    sealed class VideoListRequest() : UseCase.CommonRequest {
        data class VideoRequest(val coroutineScope: CoroutineScope) : VideoListRequest()
        data class SearchRequest(val query: String, val coroutineScope: CoroutineScope) : VideoListRequest()
    }

    data class VideoListResponse(val youTubePopularVideoPagingData: Flow<PagingData<YoutubeVideoDomain>>) : UseCase.CommonResponse

    override fun process(request: VideoListRequest): Flow<VideoListResponse> = flow {
        emit(
            when (request) {
                is VideoListRequest.VideoRequest -> {
                    VideoListResponse(
                        youTubePopularVideoPagingData = videoListRepository.getPopularVideos()
                            .cachedIn(request.coroutineScope)
                    )
                }

                is VideoListRequest.SearchRequest -> {
                    VideoListResponse(
                        videoListRepository.getSearchVideos(request.query)
                            .cachedIn(request.coroutineScope)
                    )
                }
            }
        )
    }
}
