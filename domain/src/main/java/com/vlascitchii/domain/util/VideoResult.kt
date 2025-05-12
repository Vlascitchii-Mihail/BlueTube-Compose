package com.vlascitchii.domain.util

sealed class VideoResult<out T : Any> {

    data class Success<out T : Any>(val data: T): VideoResult<T>()
    class Error(val exception: UseCaseException): VideoResult<Nothing>()
}