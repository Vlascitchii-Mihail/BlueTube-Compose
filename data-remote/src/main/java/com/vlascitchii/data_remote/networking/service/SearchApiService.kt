package com.vlascitchii.data_remote.networking.service

import com.vlascitchii.data_remote.networking.Constants.Companion.RELEVANCE
import com.vlascitchii.data_remote.networking.Constants.Companion.SEARCH
import com.vlascitchii.data_remote.networking.Constants.Companion.SNIPPET
import com.vlascitchii.data_remote.enetity_api_model.video_search_api_model.SearchVideoResponseApiModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApiService : BaseApiService, ParticularVideoApiService {

    @GET(SEARCH)
    suspend fun searchVideo(
        @Query("q") query: String = "",
        @Query("part") part: String = SNIPPET,
        @Query("order") order: String = RELEVANCE,
        @Query("pageToken") nextPageToken: String = ""
    ): Response<SearchVideoResponseApiModel>
}