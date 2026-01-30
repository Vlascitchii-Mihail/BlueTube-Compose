package com.vlascitchii.domain.repository

import androidx.paging.PagingData
import com.vlascitchii.domain.model.videos.YoutubeVideoDomain
import kotlinx.coroutines.flow.Flow

interface ShortsRepository {

    fun getShorts(): Flow<PagingData<YoutubeVideoDomain>>
}