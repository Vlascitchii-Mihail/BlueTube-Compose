package com.vlascitchii.data_repository.repository_impl

import com.vlascitchii.data_repository.data_source.local.LocalVideoListDataSource
import com.vlascitchii.data_repository.data_source.remote.RemoteSearchDataSource
import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideoResponse
import com.vlascitchii.domain.repository.PlayerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import java.time.OffsetDateTime

class PlayerRepositoryImpl(
    private val remoteSearchDataSource: RemoteSearchDataSource,
    private val localVideoListDataSource: LocalVideoListDataSource

) : PlayerRepository {

    override fun getSearchRelayedVideos(query: String, nextPageToken: String): Flow<YoutubeVideoResponse> {
        return remoteSearchDataSource.searchRelatedVideos(query, nextPageToken)
            .onEach { youtubeVideoResponse: YoutubeVideoResponse ->
                localVideoListDataSource.insertVideosToDatabaseWithTimeStamp(
                    youtubeVideoResponse,
                    OffsetDateTime.now()
                )
            }
    }
}