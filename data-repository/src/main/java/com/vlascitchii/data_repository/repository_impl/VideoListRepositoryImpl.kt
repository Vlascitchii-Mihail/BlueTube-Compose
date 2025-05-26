package com.vlascitchii.data_repository.repository_impl

import com.vlascitchii.data_repository.data_source.local.LocalVideoListDataSource
import com.vlascitchii.data_repository.data_source.remote.RemoteSearchDataSource
import com.vlascitchii.data_repository.data_source.remote.RemoteVideoListDataSource
import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideoResponse
import com.vlascitchii.domain.repository.VideoListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import java.time.OffsetDateTime

class VideoListRepositoryImpl(
    private val remoteVideoListDataSource: RemoteVideoListDataSource,
    private val remoteSearchDataSource: RemoteSearchDataSource,
    private val localVideoListDataSource: LocalVideoListDataSource
) : VideoListRepository {

    override fun getVideos(nextPageToken: String): Flow<YoutubeVideoResponse> {
        return remoteVideoListDataSource.fetchVideos(nextPageToken)
            .onEach { youtubeVideoResponse: YoutubeVideoResponse ->
                localVideoListDataSource.insertVideosToDatabaseWithTimeStamp(
                    youtubeVideoResponse,
                    OffsetDateTime.now()
                )
            }
    }

    override fun getSearchVideos(
        query: String,
        nextPageToken: String
    ): Flow<YoutubeVideoResponse> {
        return remoteSearchDataSource.searchVideos(query, nextPageToken)
            .onEach { youtubeVideoResponse: YoutubeVideoResponse ->
                localVideoListDataSource.insertVideosToDatabaseWithTimeStamp(
                    youtubeVideoResponse,
                    OffsetDateTime.now()
                )
            }
    }
}