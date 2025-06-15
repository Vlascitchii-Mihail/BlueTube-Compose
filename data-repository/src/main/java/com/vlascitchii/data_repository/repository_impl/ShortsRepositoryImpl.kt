package com.vlascitchii.data_repository.repository_impl

import androidx.paging.PagingData
import com.vlascitchii.data_repository.data_source.local.LocalVideoListDataSource
import com.vlascitchii.data_repository.data_source.remote.RemoteShortsDataSource
import com.vlascitchii.data_repository.paging.PagingDataBuilder
import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideo
import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideoResponse
import com.vlascitchii.domain.repository.ShortsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import java.time.OffsetDateTime

class ShortsRepositoryImpl(
    private val remoteShortsDataSource: RemoteShortsDataSource,
    private val localVideoListDataSource: LocalVideoListDataSource
): ShortsRepository, PagingDataBuilder {

    override fun getShorts(): Flow<PagingData<YoutubeVideo>> {

        return buildPagerWithPagingSourceFactory { nextPageToken: String ->
            remoteShortsDataSource.fetchShorts(nextPageToken)
                .onEach { youtubeVideoResponse: YoutubeVideoResponse ->
                    localVideoListDataSource.insertVideosToDatabaseWithTimeStamp(
                        youtubeVideoResponse,
                        OffsetDateTime.now()
                    )
                }
        }
    }
}