package com.vlascitchii.data_remote.networking.service

import com.vlascitchii.data_remote.networking.Constants.Companion.CONTENT_VIDEO_TYPE
import com.vlascitchii.data_remote.networking.Constants.Companion.REGION_CODE
import com.vlascitchii.data_remote.networking.Constants.Companion.RELEVANCE
import com.vlascitchii.data_remote.networking.Constants.Companion.SEARCH
import com.vlascitchii.data_remote.networking.Constants.Companion.SHORTS_VIDEO_DURATION
import com.vlascitchii.data_remote.networking.Constants.Companion.SNIPPET
import com.vlascitchii.data_remote.enetity_api_model.video_search_api_model.SearchVideoResponseApiModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ShortsApiService : BaseApiService, ParticularVideoApiService {

    @GET(SEARCH)
    suspend fun fetchShorts(
        @Query("part") part: String = SNIPPET,
        @Query("type") type: String = CONTENT_VIDEO_TYPE,
        @Query("videoDuration") videoDuration: String = SHORTS_VIDEO_DURATION,
        @Query("regionCode") regionCode: String = REGION_CODE,
        @Query("order") order: String = RELEVANCE,
        @Query("pageToken") nextPageToken: String = ""
    ): Response<SearchVideoResponseApiModel>
}