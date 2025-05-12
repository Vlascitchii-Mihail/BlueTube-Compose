package com.vlascitchii.data_remote.networking.service

import com.vlascitchii.data_remote.enetity_api_model.video_list_api_model.videos_api_model.ParticularVideoApiModel
import com.vlascitchii.data_remote.networking.Constants.Companion.CONTENT_DETAILS
import com.vlascitchii.data_remote.networking.Constants.Companion.LIST_OF_VIDEOS
import com.vlascitchii.data_remote.networking.Constants.Companion.SNIPPET
import com.vlascitchii.data_remote.networking.Constants.Companion.STATISTICS
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ParticularVideoApiService {

    @GET(LIST_OF_VIDEOS)
    suspend fun fetchParticularVideo(
        @Query("id") id: String,
        @Query("part") part: String = "$SNIPPET, $CONTENT_DETAILS, $STATISTICS"
    ): Response<ParticularVideoApiModel>
}