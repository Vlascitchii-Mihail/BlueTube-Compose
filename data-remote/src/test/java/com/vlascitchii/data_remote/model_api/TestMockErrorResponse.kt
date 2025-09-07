package com.vlascitchii.data_remote.model_api

import com.vlascitchii.data_remote.model_api.error.ErrorApiYouTubeResponse
import com.vlascitchii.data_remote.model_api.error.ErrorVideo

val API_ERROR_YOUTUBE_RESPONSE_INSTANCE = ErrorApiYouTubeResponse(
    error = ErrorVideo(
        code = 403,
        message = "Method doesn't allow unregistered callers (callers without established identity). Please use API Key or other form of API consumer identity to call this API."
    )
)