package com.vlascitchii.data_repository.data_source.local

import com.vlascitchii.domain.model.videos.YoutubeVideoResponseDomain
import kotlinx.coroutines.flow.Flow
import java.time.OffsetDateTime

interface LocalVideoListDataSource {

    suspend fun insertVideosWithTimeStamp(
        youTubeVideoResponse: YoutubeVideoResponseDomain,
        loadDate: OffsetDateTime
    )

    fun  getVideosFromStore(pageToken: String): Flow<YoutubeVideoResponseDomain>
}
