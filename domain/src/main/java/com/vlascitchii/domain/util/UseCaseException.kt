package com.vlascitchii.domain.util

import com.vlascitchii.domain.model.ErrorDomain

sealed class UseCaseException(cause: Throwable, val errorDomain: ErrorDomain?): Throwable(cause) {

    class VideoListLoadException(cause: Throwable, errorDomain: ErrorDomain? = ErrorDomain()): UseCaseException(cause, errorDomain)
    class ShortsLoadException(cause: Throwable, errorDomain: ErrorDomain? = ErrorDomain()): UseCaseException(cause, errorDomain)
    class SearchLoadException(cause: Throwable, errorDomain: ErrorDomain? = ErrorDomain()): UseCaseException(cause, errorDomain)
    class LocalStorageException(cause: Throwable, errorDomain: ErrorDomain? = ErrorDomain()): UseCaseException(cause, errorDomain)
    class UnknownException(cause: Throwable, errorDomain: ErrorDomain? = ErrorDomain()): UseCaseException(cause, errorDomain)

    companion object {
        fun createFromThrowable(throwable: Throwable): UseCaseException {
            return if (throwable is UseCaseException) throwable
            else UnknownException(throwable, null)
        }
    }
}
