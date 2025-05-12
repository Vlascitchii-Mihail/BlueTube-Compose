package com.vlascitchii.data_repository.repository_impl

import com.vlascitchii.data_repository.data_source.local.LocalVideoListDataSource
import com.vlascitchii.data_repository.data_source.remote.RemoteShortsDataSource
import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideoResponse
import com.vlascitchii.domain.repository.ShortsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import java.time.OffsetDateTime

class ShortsRepositoryImpl(
    private val remoteShortsDataSource: RemoteShortsDataSource,
    private val localVideoListDataSource: LocalVideoListDataSource
): ShortsRepository {

    override fun getShorts(nextPageToken: String): Flow<YoutubeVideoResponse> {
        return remoteShortsDataSource.fetchShorts(nextPageToken)
            .onEach { youtubeVideoResponse: YoutubeVideoResponse ->
            localVideoListDataSource.insertVideosToDatabaseWithTimeStamp(
                youtubeVideoResponse,
                OffsetDateTime.now()
            )
        }.catch {
            localVideoListDataSource.getVideosFromDatabase(nextPageToken)
            //TODO: check if it is a correct solution
            throw it
        }
    }
}