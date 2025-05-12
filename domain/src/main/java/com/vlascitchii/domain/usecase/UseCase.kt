package com.vlascitchii.domain.usecase

import com.vlascitchii.domain.usecase.util.Configuration
import com.vlascitchii.domain.util.UseCaseException
import com.vlascitchii.domain.util.VideoResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

abstract class UseCase<I : UseCase.Request, O : UseCase.Response>(
    private val configuration: Configuration
) {

    interface Request
    interface Response

    internal abstract fun process(request: I): Flow<O>

     fun execute(request: I): Flow<VideoResult<O>> = process(request)
        .map { VideoResult.Success(it) as VideoResult<O> }
        .flowOn(configuration.dispatcher)
        .catch { emit(VideoResult.Error(UseCaseException.createFromThrowable(it))) }
}