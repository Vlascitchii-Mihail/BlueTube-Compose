package com.vlascitchii.data_repository.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.vlascitchii.domain.util.UseCaseException

abstract class CommonPagingSource <KEY: Any, VALUE: Any>: PagingSource<KEY, VALUE>() {

    abstract fun calculateRefreshKey(state: PagingState<KEY, VALUE>): KEY?

    override fun getRefreshKey(state: PagingState<KEY, VALUE>): KEY? {
        return calculateRefreshKey(state)
    }

    abstract suspend fun getLoadedDataList(params: LoadParams<KEY>): List<VALUE>
    abstract fun getLoadedDataPrevKey(): KEY?
    abstract fun getLoadedDataNextKey(): KEY?

    override suspend fun load(params: LoadParams<KEY>): LoadResult<KEY, VALUE> {
        return try {
            LoadResult.Page(
                data = getLoadedDataList(params),
                prevKey = getLoadedDataPrevKey(),
                nextKey = getLoadedDataNextKey()
            )
        } catch (ex: UseCaseException) {
            LoadResult.Error(ex)
        }
    }
}
