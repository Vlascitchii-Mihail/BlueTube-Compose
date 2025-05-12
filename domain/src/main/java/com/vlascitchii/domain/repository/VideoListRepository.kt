package com.vlascitchii.domain.repository

import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideoResponse
import kotlinx.coroutines.flow.Flow

interface VideoListRepository {

    fun getVideos(nextPageToken: String): Flow<YoutubeVideoResponse>
    fun getSearchVideos(query: String, nextPageToken: String): Flow<YoutubeVideoResponse>
}