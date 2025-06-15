package com.vlascitchii.domain.repository

import androidx.paging.PagingData
import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideo
import kotlinx.coroutines.flow.Flow

interface PlayerRepository {

    fun getSearchRelayedVideos(query: String): Flow<PagingData<YoutubeVideo>>
}