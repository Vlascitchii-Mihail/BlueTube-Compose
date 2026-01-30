package com.vlascitchii.data_remote.networking.service

import com.vlascitchii.data_remote.model_api.search_api_model.SearchVideoResponseApiModel
import com.vlascitchii.data_remote.model_api.video_api_model.YoutubeVideoResponseApiModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

internal const val MOST_POPULAR = "mostPopular"

interface VideoListApiService : BaseApiService {

    @GET(LIST_OF_VIDEOS)
    suspend fun fetchVideos(
        @Query("part") part: String = "$SNIPPET, $CONTENT_DETAILS, $STATISTICS",
        @Query("chart") chart: String = MOST_POPULAR,
        @Query("regionCode") regionCode: String = US_REGION_CODE,
        @Query("pageToken") nextPageToken: String = ""
    ): Response<YoutubeVideoResponseApiModel>

    @GET(SEARCH)
    suspend fun searchVideo(
        @Query("q") query: String = "",
        @Query("part") part: String = SNIPPET,
        @Query("order") order: String = RELEVANCE,
        @Query("pageToken") nextPageToken: String = ""
    ): Response<SearchVideoResponseApiModel>
}
