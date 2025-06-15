package com.vlascitchii.domain.repository

import androidx.paging.PagingData
import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideo
import kotlinx.coroutines.flow.Flow

interface VideoListRepository {

    fun getPopularVideos(): Flow<PagingData<YoutubeVideo>>
    fun getSearchVideos(query: String): Flow<PagingData<YoutubeVideo>>
}