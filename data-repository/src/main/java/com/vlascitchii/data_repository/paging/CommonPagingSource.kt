package com.vlascitchii.data_repository.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideo
import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideoResponse
import com.vlascitchii.domain.util.UseCaseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class CommonPagingSource(
    val executeUseCase: (String) -> Flow<YoutubeVideoResponse>
) : PagingSource<String, YoutubeVideo>() {

    override fun getRefreshKey(state: PagingState<String, YoutubeVideo>): String? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.pageToken
        }
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, YoutubeVideo> {
        val nextPageToken = params.key ?: ""

        return try {
            val youTubeVideoResponse = executeUseCase.invoke(nextPageToken).first()
             LoadResult.Page(
                    data = youTubeVideoResponse.items,
                    prevKey = youTubeVideoResponse.prevPageToken,
                    nextKey = youTubeVideoResponse.nextPageToken
             )
        } catch (ex: UseCaseException) {
            LoadResult.Error(ex)
        }
    }
}