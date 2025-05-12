package com.vlascitchii.domain.repository

import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideoResponse
import kotlinx.coroutines.flow.Flow

interface PlayerRepository {

    fun getSearchRelayedVideos(query: String, nextPageToken: String): Flow<YoutubeVideoResponse>
}