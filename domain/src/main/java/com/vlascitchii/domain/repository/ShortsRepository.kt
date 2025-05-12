package com.vlascitchii.domain.repository

import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideoResponse
import kotlinx.coroutines.flow.Flow

//TODO: add repository functions
interface ShortsRepository {

    fun getShorts(nextPageToken: String): Flow<YoutubeVideoResponse>
}