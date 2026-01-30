package com.vlascitchii.domain.usecase

import com.vlascitchii.domain.usecase.util.DispatcherConfiguration
import com.vlascitchii.domain.util.UseCaseException
import com.vlascitchii.domain.util.VideoResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

abstract class UseCase<REQUEST : UseCase.CommonRequest, RESPONSE : UseCase.CommonResponse>(
    private val configuration: DispatcherConfiguration
) {

    interface CommonRequest
    interface CommonResponse

    internal abstract fun process(request: REQUEST): Flow<RESPONSE>

     fun execute(request: REQUEST): Flow<VideoResult<RESPONSE>> = process(request)
        .map { VideoResult.Success(it) as VideoResult<RESPONSE> }
        .flowOn(configuration.dispatcher)
        .catch { emit(VideoResult.Error(UseCaseException.createFromThrowable(it))) }
}
