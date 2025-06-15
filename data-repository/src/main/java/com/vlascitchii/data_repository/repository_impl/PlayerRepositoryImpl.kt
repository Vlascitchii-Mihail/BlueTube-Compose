package com.vlascitchii.data_repository.repository_impl

import androidx.paging.PagingData
import com.vlascitchii.data_repository.data_source.local.LocalVideoListDataSource
import com.vlascitchii.data_repository.data_source.remote.RemoteSearchDataSource
import com.vlascitchii.data_repository.paging.PagingDataBuilder
import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideo
import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideoResponse
import com.vlascitchii.domain.repository.PlayerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import java.time.OffsetDateTime

class PlayerRepositoryImpl(
    private val remoteSearchDataSource: RemoteSearchDataSource,
    private val localVideoListDataSource: LocalVideoListDataSource

) : PlayerRepository, PagingDataBuilder {

    override fun getSearchRelayedVideos(query: String): Flow<PagingData<YoutubeVideo>> {

        return buildPagerWithPagingSourceFactory { nextPageToken: String ->
            remoteSearchDataSource.searchRelatedVideos(query, nextPageToken)
                .onEach { youtubeVideoResponse: YoutubeVideoResponse ->
                    localVideoListDataSource.insertVideosToDatabaseWithTimeStamp(
                        youtubeVideoResponse,
                        OffsetDateTime.now()
                    )
                }
        }
    }
}