package com.vlascitchii.data_remote.networking.service

import com.vlascitchii.data_remote.enetity_api_model.video_list_api_model.videos_api_model.YoutubeVideoResponseApiModel
import com.vlascitchii.data_remote.networking.Constants.Companion.CONTENT_DETAILS
import com.vlascitchii.data_remote.networking.Constants.Companion.LIST_OF_VIDEOS
import com.vlascitchii.data_remote.networking.Constants.Companion.MOST_POPULAR
import com.vlascitchii.data_remote.networking.Constants.Companion.REGION_CODE
import com.vlascitchii.data_remote.networking.Constants.Companion.SNIPPET
import com.vlascitchii.data_remote.networking.Constants.Companion.STATISTICS
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface VideoListApiService : BaseApiService {

    @GET(LIST_OF_VIDEOS)
    suspend fun fetchVideos(
        @Query("part") part: String = "$SNIPPET, $CONTENT_DETAILS, $STATISTICS",
        @Query("chart") chart: String = MOST_POPULAR,
        @Query("regionCode") regionCode: String = REGION_CODE,
        @Query("pageToken") nextPageToken: String = ""
    ): Response<YoutubeVideoResponseApiModel>
}