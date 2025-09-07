package com.vlascitchii.data_repository.data_source.remote

import com.vlascitchii.domain.model.videos.YoutubeVideoResponseDomain

interface RemoteShortsDataSource {

    suspend fun fetchShorts(nextPageToken: String): YoutubeVideoResponseDomain
}
