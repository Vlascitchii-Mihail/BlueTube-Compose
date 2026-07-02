package com.vlascitchii.data_repository.paging

import androidx.paging.PagingState
import com.vlascitchii.domain.model.videos.YoutubeVideoDomain
import com.vlascitchii.domain.model.videos.YoutubeVideoResponseDomain

class VideoPagingSource(
    private val fetch: suspend (page: String) -> YoutubeVideoResponseDomain
) : CommonPagingSource<String, YoutubeVideoDomain>() {

    private var youTubeVideoResponseDomain: YoutubeVideoResponseDomain = YoutubeVideoResponseDomain()

    override fun calculateRefreshKey(state: PagingState<String, YoutubeVideoDomain>): String? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.pageToken
        }
    }

    override suspend fun getLoadedDataList(params: LoadParams<String>): List<YoutubeVideoDomain> {
        youTubeVideoResponseDomain = fetch.invoke(params.key ?: "")
        return youTubeVideoResponseDomain.items
    }

    override fun getLoadedDataPrevKey(): String? =
        youTubeVideoResponseDomain.prevPageToken.takeIf { it.isNotBlank() }

    override fun getLoadedDataNextKey(): String? =
        youTubeVideoResponseDomain.nextPageToken.takeIf { it.isNotBlank() }
}
