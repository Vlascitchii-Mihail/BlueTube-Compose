package com.vlascitchii.data_repository.data_source.remote

import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideoResponse
import kotlinx.coroutines.flow.Flow

interface RemoteSearchDataSource {

    var isRelated: Boolean

    fun searchRelatedVideos(query: String, nextPageToken: String): Flow<YoutubeVideoResponse>
    fun searchVideos(query: String, nextPageToken: String): Flow<YoutubeVideoResponse>
    fun search(query: String, nextPageToken: String): Flow<YoutubeVideoResponse>
}