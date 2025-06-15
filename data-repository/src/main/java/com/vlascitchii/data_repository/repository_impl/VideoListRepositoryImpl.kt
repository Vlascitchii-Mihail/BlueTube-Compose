package com.vlascitchii.data_repository.repository_impl

import androidx.paging.PagingData
import com.vlascitchii.data_repository.data_source.local.LocalVideoListDataSource
import com.vlascitchii.data_repository.data_source.remote.RemoteSearchDataSource
import com.vlascitchii.data_repository.data_source.remote.RemoteVideoListDataSource
import com.vlascitchii.data_repository.paging.PagingDataBuilder
import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideo
import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideoResponse
import com.vlascitchii.domain.repository.VideoListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import java.time.OffsetDateTime

class VideoListRepositoryImpl(
    private val remoteVideoListDataSource: RemoteVideoListDataSource,
    private val remoteSearchDataSource: RemoteSearchDataSource,
    private val localVideoListDataSource: LocalVideoListDataSource
) : VideoListRepository, PagingDataBuilder {

    override fun getPopularVideos(): Flow<PagingData<YoutubeVideo>> {

        return buildPagerWithPagingSourceFactory { nextPageToken: String ->
            remoteVideoListDataSource.fetchVideos(nextPageToken)
                .onEach { youtubeVideoResponse: YoutubeVideoResponse ->
                    localVideoListDataSource.insertVideosToDatabaseWithTimeStamp(
                        youtubeVideoResponse,
                        OffsetDateTime.now()
                    )
                }
        }
    }

    override fun getSearchVideos(
        query: String,
    ): Flow<PagingData<YoutubeVideo>> {

        return buildPagerWithPagingSourceFactory { nextPageToken: String ->
            remoteSearchDataSource.searchVideos(query, nextPageToken)
                .onEach { youtubeVideoResponse: YoutubeVideoResponse ->
                    localVideoListDataSource.insertVideosToDatabaseWithTimeStamp(
                        youtubeVideoResponse,
                        OffsetDateTime.now()
                    )
                }
        }
    }
}