package com.vlascitchii.data_repository.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideo
import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideoResponse
import kotlinx.coroutines.flow.Flow

interface PagingDataBuilder {

    fun buildPagerWithPagingSourceFactory(
        pagingFactory: (String) -> Flow<YoutubeVideoResponse>
    ): Flow<PagingData<YoutubeVideo>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5,
                prefetchDistance = 15
            ),
            pagingSourceFactory = {
                CommonPagingSource { nextPageToken: String ->
                    pagingFactory.invoke(nextPageToken)
                }
            }
        ).flow
    }
}