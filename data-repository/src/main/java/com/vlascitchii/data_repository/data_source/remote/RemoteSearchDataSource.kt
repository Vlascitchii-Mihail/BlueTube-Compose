package com.vlascitchii.data_repository.data_source.remote

import com.vlascitchii.domain.model.videos.YoutubeVideoResponseDomain

interface RemoteSearchDataSource {

    var isRelated: Boolean

    suspend fun searchRelatedVideos(query: String, nextPageToken: String): YoutubeVideoResponseDomain
    suspend fun searchVideos(query: String, nextPageToken: String): YoutubeVideoResponseDomain
}
