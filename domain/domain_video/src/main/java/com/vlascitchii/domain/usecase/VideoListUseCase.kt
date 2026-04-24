package com.vlascitchii.domain.usecase

import androidx.paging.PagingData
import com.vlascitchii.domain.model.videos.YoutubeVideoDomain
import com.vlascitchii.domain.repository.VideoListRepository
import com.vlascitchii.domain.usecase.util.DispatcherConfiguration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class VideoListUseCase @Inject constructor(
    dispatcherConfiguration: DispatcherConfiguration,
    private val videoListRepository: VideoListRepository
) : UseCase<VideoListUseCase.VideoListRequest, VideoListUseCase.VideoListResponse>(dispatcherConfiguration) {

    sealed class VideoListRequest() : UseCase.CommonRequest {
        data object VideoRequest : VideoListRequest()
        data class SearchRequest(val query: String) : VideoListRequest()
    }

    data class VideoListResponse(val youTubeVideoPagingData: Flow<PagingData<YoutubeVideoDomain>>) : UseCase.CommonResponse

    override fun process(request: VideoListRequest): Flow<VideoListResponse> = flow {
        emit(
            when (request) {
                is VideoListRequest.VideoRequest -> {
                    VideoListResponse(
                        youTubeVideoPagingData = videoListRepository.getPopularVideos()
                    )
                }

                is VideoListRequest.SearchRequest -> {
                    VideoListResponse(
                        youTubeVideoPagingData = videoListRepository.getSearchVideos(request.query)
                    )
                }
            }
        )
    }
}
