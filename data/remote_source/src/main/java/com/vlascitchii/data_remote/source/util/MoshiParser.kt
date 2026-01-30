package com.vlascitchii.data_remote.source.util

import com.squareup.moshi.Moshi
import com.vlascitchii.data_remote.model_api.error.ErrorApiYouTubeResponse
import okio.IOException
import javax.inject.Inject
import kotlin.jvm.java

class MoshiParser @Inject constructor(val moshi: Moshi) {

    fun parseJsonIntoNewsApiErrorResponse(jsonBody: String): ErrorApiYouTubeResponse? {
        return try {
            val jsonMoshiAdapter = moshi.adapter(ErrorApiYouTubeResponse::class.java)
            jsonMoshiAdapter.fromJson(jsonBody)
        } catch (exception: IOException) {
            exception.printStackTrace()
            null
        }
    }
}
