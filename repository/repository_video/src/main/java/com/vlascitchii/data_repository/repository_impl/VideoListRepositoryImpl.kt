package com.vlascitchii.data_repository.repository_impl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.vlascitchii.data_repository.data_source.local.LocalVideoListDataSource
import com.vlascitchii.data_repository.data_source.remote.RemoteSearchDataSource
import com.vlascitchii.data_repository.data_source.remote.RemoteVideoListDataSource
import com.vlascitchii.data_repository.paging.VideoPagingSource
import com.vlascitchii.domain.custom_scope.CustomCoroutineScope
import com.vlascitchii.domain.model.videos.YoutubeVideoDomain
import com.vlascitchii.domain.repository.VideoListRepository
import kotlinx.coroutines.flow.Flow

internal const val PAGING_DEFAULT_PAGE_SIZE = 5
internal const val PREPARE_EXTRA_PAGES_MODIFIER = 3

class VideoListRepositoryImpl(
    private val remoteVideoListDataSource: RemoteVideoListDataSource,
    private val remoteSearchDataSource: RemoteSearchDataSource,
    private val localVideoListDataSource: LocalVideoListDataSource,
    private val customCoroutineScope: CustomCoroutineScope
) : VideoListRepository {

    private val pagerConfig = PagingConfig(
        pageSize = PAGING_DEFAULT_PAGE_SIZE,
        prefetchDistance = PAGING_DEFAULT_PAGE_SIZE * PREPARE_EXTRA_PAGES_MODIFIER
    )

    override fun getPopularVideos(): Flow<PagingData<YoutubeVideoDomain>> =
        Pager(
            config = pagerConfig,
            pagingSourceFactory = {
                VideoPagingSource(
                    localDataSource = localVideoListDataSource,
                    customCoroutineScope = customCoroutineScope
                ) { page: String ->
                    remoteVideoListDataSource.fetchVideos(page)
                }
            }
        ).flow

    override fun getSearchVideos(query: String): Flow<PagingData<YoutubeVideoDomain>> =
        Pager(
            config = pagerConfig,
            pagingSourceFactory = {
                VideoPagingSource(
                    localDataSource = localVideoListDataSource,
                    customCoroutineScope = customCoroutineScope
                ) { page: String ->
                    remoteSearchDataSource.searchVideos(query = query, nextPageToken = page)
                }
            }
        ).flow
}
