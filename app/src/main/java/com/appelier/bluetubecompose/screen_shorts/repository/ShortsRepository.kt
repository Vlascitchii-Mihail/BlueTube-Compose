package com.appelier.bluetubecompose.screen_shorts.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.appelier.bluetubecompose.core.core_api.VideoApiService
import com.appelier.bluetubecompose.core.core_database.YouTubeDatabase
import com.appelier.bluetubecompose.core.core_paging.YoutubeVideoSource
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideo
import com.appelier.bluetubecompose.utils.VideoType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Named

interface ShortsRepository {
    fun fetchShorts(videoType: VideoType, viewModelScope: CoroutineScope)
    : Flow<PagingData<YoutubeVideo>>
}
class ShortsRepositoryImpl @Inject constructor(
    private val apiVideoListService: VideoApiService,
    @Named("OriginalDatabase")
    private val youTubeDatabase: YouTubeDatabase
): ShortsRepository {

    override fun fetchShorts(videoType: VideoType, viewModelScope: CoroutineScope)
    : Flow<PagingData<YoutubeVideo>> {
        return Pager(
            config = PagingConfig(
                pageSize = 1,
                maxSize = 3,
                prefetchDistance = 1,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                YoutubeVideoSource(
                    apiVideoListService,
                    viewModelScope,
                    videoType,
                    youTubeDatabase.youTubeVideoDao
                )
            }
        ).flow
    }
}