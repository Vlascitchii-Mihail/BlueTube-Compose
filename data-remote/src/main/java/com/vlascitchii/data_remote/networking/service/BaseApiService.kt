package com.vlascitchii.data_remote.networking.service

import com.vlascitchii.data_remote.enetity_api_model.video_channel_api_model.YoutubeChannelResponseApiModel
import com.vlascitchii.data_remote.networking.Constants.Companion.CHANNELS
import com.vlascitchii.data_remote.networking.Constants.Companion.CONTENT_DETAILS
import com.vlascitchii.data_remote.networking.Constants.Companion.SINGLE_CHANNEL
import com.vlascitchii.data_remote.networking.Constants.Companion.SNIPPET
import com.vlascitchii.data_remote.networking.Constants.Companion.STATISTICS
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BaseApiService {

    @GET(CHANNELS)
    suspend fun fetchChannels(
        @Query("id") id: String,
        @Query("part") part: String = "$SNIPPET, $CONTENT_DETAILS, $STATISTICS",
        @Query("maxResults") maxResults: Int = SINGLE_CHANNEL
    ): Response<YoutubeChannelResponseApiModel>
}