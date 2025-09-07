package com.vlascitchii.data_remote.model_api.error

import com.squareup.moshi.JsonClass
import com.vlascitchii.domain.model.ErrorDomain

@JsonClass(generateAdapter = true)
data class ErrorApiYouTubeResponse(
    val error: ErrorVideo = ErrorVideo()
)

@JsonClass(generateAdapter = true)
data class ErrorVideo(
    val code: Int = 0,
    val message: String = ""
)

internal fun ErrorApiYouTubeResponse.convertErrorApiYouTubeResponseToErrorDomainYouTubeResponse(): ErrorDomain {
    return ErrorDomain(
        code = this.error.code,
        message = this.error.message
    )
}
