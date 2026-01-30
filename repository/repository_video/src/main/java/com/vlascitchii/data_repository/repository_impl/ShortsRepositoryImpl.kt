package com.vlascitchii.data_repository.repository_impl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.vlascitchii.data_repository.data_source.local.LocalVideoListDataSource
import com.vlascitchii.data_repository.data_source.remote.RemoteShortsDataSource
import com.vlascitchii.data_repository.paging.VideoPagingSource
import com.vlascitchii.domain.custom_scope.CustomCoroutineScope
import com.vlascitchii.domain.model.videos.YoutubeVideoDomain
import com.vlascitchii.domain.repository.ShortsRepository
import kotlinx.coroutines.flow.Flow

class ShortsRepositoryImpl(
    private val remoteShortsDataSource: RemoteShortsDataSource,
    private val localVideoListDataSource: LocalVideoListDataSource,
    private val customCoroutineScope: CustomCoroutineScope
) : ShortsRepository {

    private val pagerConfig = PagingConfig(
        pageSize = PAGING_DEFAULT_PAGE_SIZE,
        prefetchDistance = PAGING_DEFAULT_PAGE_SIZE * PREPARE_EXTRA_PAGES_MODIFIER
    )

    override fun getShorts(): Flow<PagingData<YoutubeVideoDomain>> =
        Pager(
            config = pagerConfig,
            pagingSourceFactory = {
                VideoPagingSource(
                    localDataSource = localVideoListDataSource,
                    customCoroutineScope = customCoroutineScope
                ) { page: String ->
                    remoteShortsDataSource.fetchShorts(page)
                }
            }
        ).flow
}
