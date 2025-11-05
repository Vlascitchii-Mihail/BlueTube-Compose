package com.vlascitchii.domain.usecase

import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.vlascitchii.domain.model.videos.YoutubeVideoDomain
import com.vlascitchii.domain.repository.ShortsRepository
import com.vlascitchii.domain.usecase.util.DispatcherConfiguration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class ShortsUseCase(
    configuration: DispatcherConfiguration,
    private val shortsRepository: ShortsRepository
) : UseCase<ShortsUseCase.ShortsRequest, ShortsUseCase.ShortsResponse>(configuration) {

    data class ShortsRequest(val coroutineScope: CoroutineScope): UseCase.CommonRequest
    data class ShortsResponse(val shortsPopularVideoPagingData: Flow<PagingData<YoutubeVideoDomain>>) : UseCase.CommonResponse

    override fun process(request: ShortsRequest): Flow<ShortsResponse> = flow {
        emit(
            ShortsResponse(
                shortsRepository.getShorts()
                    .cachedIn(request.coroutineScope)
            )
        )
    }
}
