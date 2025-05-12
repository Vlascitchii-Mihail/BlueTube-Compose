package com.vlascitchii.data_repository.data_source.local

import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideoResponse
import kotlinx.coroutines.flow.Flow
import java.time.OffsetDateTime

interface LocalVideoListDataSource {

    fun insertVideosToDatabaseWithTimeStamp(
        youTubeVideoResponse: YoutubeVideoResponse,
        loadDate: OffsetDateTime
    )

    fun  getVideosFromDatabase(pageToken: String): Flow<YoutubeVideoResponse>
}