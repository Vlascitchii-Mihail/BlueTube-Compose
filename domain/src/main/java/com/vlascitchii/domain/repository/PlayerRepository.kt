package com.vlascitchii.domain.repository

import androidx.paging.PagingData
import com.vlascitchii.domain.model.videos.YoutubeVideoDomain
import kotlinx.coroutines.flow.Flow

interface PlayerRepository {

    fun getSearchRelayedVideos(query: String): Flow<PagingData<YoutubeVideoDomain>>
}