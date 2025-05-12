package com.vlascitchii.data_repository.data_source.remote

import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideoResponse
import kotlinx.coroutines.flow.Flow

interface RemoteVideoListDataSource {

    fun fetchVideos(nextPageToken: String): Flow<YoutubeVideoResponse>
}