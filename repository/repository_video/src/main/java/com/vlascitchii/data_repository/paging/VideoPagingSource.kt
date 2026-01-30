package com.vlascitchii.data_repository.paging

import androidx.paging.PagingState
import com.vlascitchii.data_repository.data_source.local.LocalVideoListDataSource
import com.vlascitchii.domain.custom_scope.CustomCoroutineScope
import com.vlascitchii.domain.model.videos.YoutubeVideoDomain
import com.vlascitchii.domain.model.videos.YoutubeVideoResponseDomain
import com.vlascitchii.domain.util.UseCaseException
import kotlinx.coroutines.withContext

class VideoPagingSource(
    private val localDataSource: LocalVideoListDataSource,
    private val customCoroutineScope: CustomCoroutineScope,
    private val fetch: suspend (page: String) -> YoutubeVideoResponseDomain
) : CommonPagingSource<String, YoutubeVideoDomain>() {

    private var youTubeVideoResponseDomain: YoutubeVideoResponseDomain = YoutubeVideoResponseDomain()

    override fun calculateRefreshKey(state: PagingState<String, YoutubeVideoDomain>): String? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.pageToken
        }
    }

    override suspend fun getLoadedDataList(params: LoadParams<String>): List<YoutubeVideoDomain> {

        try {
            withContext(customCoroutineScope.coroutineContext) {
                youTubeVideoResponseDomain = fetch.invoke(params.key ?: "")
            }
            //TODO: refactor database
//            localDataSource.insertVideosToDatabaseWithTimeStamp(
//                youTubeVideoResponseDomain,
//                OffsetDateTime.now()
//            )
        } catch(firstEx: UseCaseException) {
            //TODO: refactor database
//            youTubeVideoResponseDomain = localDataSource.getVideosFromDatabase(params.key ?: "")
            throw firstEx
        }

        return youTubeVideoResponseDomain.items
    }

    override fun getLoadedDataPrevKey(): String? = youTubeVideoResponseDomain.prevPageToken

    override fun getLoadedDataNextKey(): String? = youTubeVideoResponseDomain.nextPageToken
}
