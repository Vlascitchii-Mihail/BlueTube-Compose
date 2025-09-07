package com.vlascitchii.domain.repository

import androidx.paging.PagingData
import com.vlascitchii.domain.model.videos.YoutubeVideoDomain
import kotlinx.coroutines.flow.Flow

interface VideoListRepository {

    fun getPopularVideos(): Flow<PagingData<YoutubeVideoDomain>>
    fun getSearchVideos(query: String): Flow<PagingData<YoutubeVideoDomain>>
}