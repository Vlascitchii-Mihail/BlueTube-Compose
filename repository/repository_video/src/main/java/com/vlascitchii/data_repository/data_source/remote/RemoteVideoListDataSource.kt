package com.vlascitchii.data_repository.data_source.remote

import com.vlascitchii.domain.model.videos.YoutubeVideoResponseDomain

interface RemoteVideoListDataSource {

    suspend fun fetchVideos(nextPageToken: String): YoutubeVideoResponseDomain
}