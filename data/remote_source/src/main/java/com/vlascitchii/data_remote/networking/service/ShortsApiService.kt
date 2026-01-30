package com.vlascitchii.data_remote.networking.service

import com.vlascitchii.data_remote.model_api.search_api_model.SearchVideoResponseApiModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

internal const val CONTENT_VIDEO_TYPE = "video"
internal const val SHORTS_VIDEO_DURATION = "short"
internal const val SHORTS = "shorts"

interface ShortsApiService : BaseApiService {

    @GET(SEARCH)
    suspend fun fetchShorts(
        @Query("part") part: String = SNIPPET,
        @Query("type") type: String = CONTENT_VIDEO_TYPE,
        @Query("videoDuration") videoDuration: String = SHORTS_VIDEO_DURATION,
        @Query("regionCode") regionCode: String = US_REGION_CODE,
        @Query("q") query: String = SHORTS,
        @Query("order") order: String = RELEVANCE,
        @Query("pageToken") nextPageToken: String = ""
    ): Response<SearchVideoResponseApiModel>
}
