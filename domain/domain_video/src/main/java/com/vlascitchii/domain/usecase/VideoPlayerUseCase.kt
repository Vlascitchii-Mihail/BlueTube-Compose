package com.vlascitchii.domain.usecase

import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.vlascitchii.domain.model.videos.YoutubeVideoDomain
import com.vlascitchii.domain.repository.PlayerRepository
import com.vlascitchii.domain.usecase.util.DispatcherConfiguration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class VideoPlayerUseCase @Inject constructor(
    configuration: DispatcherConfiguration,
    private val playerRepository: PlayerRepository
) : UseCase<VideoPlayerUseCase.PlayerRequest, VideoPlayerUseCase.PlayerResponse>(configuration){

    data class PlayerRequest(val query: String, val coroutineScope: CoroutineScope) : UseCase.CommonRequest
    data class PlayerResponse(val relatedVideoPagingData: Flow<PagingData<YoutubeVideoDomain>>) : UseCase.CommonResponse

    override fun process(request: PlayerRequest): Flow<PlayerResponse> = flow {
        emit(
            PlayerResponse(
                playerRepository.getSearchRelayedVideos(request.query)
                    .cachedIn(request.coroutineScope)
            )
        )
    }
}
