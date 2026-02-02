package com.vlascitchii.data_repository.repository_impl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.vlascitchii.data_repository.data_source.local.LocalVideoListDataSource
import com.vlascitchii.data_repository.data_source.remote.RemoteSearchDataSource
import com.vlascitchii.data_repository.paging.VideoPagingSource
import com.vlascitchii.domain.custom_scope.CustomCoroutineScope
import com.vlascitchii.domain.di_common.DATABASE_SOURCE
import com.vlascitchii.domain.di_common.REMOTE_SEARCH_VIDEO_OR_SHORTS_LIST_SOURCE
import com.vlascitchii.domain.model.videos.YoutubeVideoDomain
import com.vlascitchii.domain.repository.PlayerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Named

class PlayerRepositoryImpl @Inject constructor(
    @param:Named(REMOTE_SEARCH_VIDEO_OR_SHORTS_LIST_SOURCE)
    private val remoteSearchDataSource: RemoteSearchDataSource,
    @param:Named(DATABASE_SOURCE)
    private val databaseVideoSourceImpl: LocalVideoListDataSource,
    private val customCoroutineScope: CustomCoroutineScope
) : PlayerRepository {

    private val pagerConfig = PagingConfig(
        pageSize = PAGING_DEFAULT_PAGE_SIZE,
        prefetchDistance = PAGING_DEFAULT_PAGE_SIZE * PREPARE_EXTRA_PAGES_MODIFIER
    )

    override fun getSearchRelayedVideos(query: String): Flow<PagingData<YoutubeVideoDomain>> =
        Pager(
            config = pagerConfig,
            pagingSourceFactory = {
                VideoPagingSource(
                    localDataSource = databaseVideoSourceImpl,
                    customCoroutineScope = customCoroutineScope
                ) { page: String ->
                    remoteSearchDataSource.searchRelatedVideos(query = query, nextPageToken = page)
                }
            }
        ).flow
}
