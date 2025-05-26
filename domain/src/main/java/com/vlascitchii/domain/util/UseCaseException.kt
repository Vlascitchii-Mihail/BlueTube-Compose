package com.vlascitchii.domain.util

sealed class UseCaseException(cause: Throwable): Throwable(cause) {

    class VideoListLoadException(cause: Throwable): UseCaseException(cause)
    class ShortsLoadException(cause: Throwable): UseCaseException(cause)
    class SearchLoadException(cause: Throwable): UseCaseException(cause)
    class LocalStorageException(cause: Throwable): UseCaseException(cause)
    class UnknownException(cause: Throwable): UseCaseException(cause)

    companion object {
        fun createFromThrowable(throwable: Throwable): UseCaseException {
            return if (throwable is UseCaseException) throwable
            else UnknownException(throwable)
        }
    }
}