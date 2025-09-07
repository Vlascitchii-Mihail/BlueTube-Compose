package com.vlascitchii.data_remote.networking.service

import com.vlascitchii.data_remote.model_api.channel_api_model.YoutubeChannelResponseApiModel
import com.vlascitchii.data_remote.model_api.video_api_model.YoutubeVideoResponseApiModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

internal const val CHANNELS = "channels"
internal const val SNIPPET = "snippet"
internal const val CONTENT_DETAILS = "contentDetails"
internal const val STATISTICS = "statistics"
internal const val SINGLE_CHANNEL = 1
internal const val US_REGION_CODE = "US"
internal const val RELEVANCE = "relevance"
internal const val SEARCH = "search"
internal const val LIST_OF_VIDEOS = "videos"

interface BaseApiService {

    @GET(CHANNELS)
    suspend fun fetchChannels(
        @Query("id") idList: List<String>,
        @Query("part") part: String = "$SNIPPET, $CONTENT_DETAILS, $STATISTICS",
        @Query("maxResults") maxResults: Int = SINGLE_CHANNEL
    ): Response<YoutubeChannelResponseApiModel>

    @GET(LIST_OF_VIDEOS)
    suspend fun fetchParticularVideoList(
        @Query("id") idList: List<String>,
        @Query("part") part: String = "$SNIPPET, $CONTENT_DETAILS, $STATISTICS"
    ): Response<YoutubeVideoResponseApiModel>
}
