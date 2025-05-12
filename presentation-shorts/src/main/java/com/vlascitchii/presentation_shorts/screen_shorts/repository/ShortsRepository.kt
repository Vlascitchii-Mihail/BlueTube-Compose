package com.vlascitchii.presentation_shorts.screen_shorts.repository

//import androidx.paging.Pager
//import androidx.paging.PagingConfig
//import androidx.paging.PagingData
//import com.vlascitchii.data_remote.networking.VideoApiService
//import com.vlascitchii.data_local.enetity.video_list.videos.YoutubeVideoEntity
//import com.vlascitchii.presentation_common.utils.VideoType
//import com.vlascitchii.presentation_common.enetity.video_list.core_paging.YoutubeVideoSource
//import com.vlascitchii.presentation_common.entity.videos.YoutubeVideoUiModel
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.flow.Flow
//import javax.inject.Inject
//import javax.inject.Named
//
//interface ShortsRepository {
//    fun fetchShorts(videoType: com.vlascitchii.presentation_common.utils.VideoType, viewModelScope: CoroutineScope)
//    : Flow<PagingData<YoutubeVideoUiModel>>
//}
//
//class ShortsRepositoryImpl @Inject constructor(
//    private val apiVideoListService: com.vlascitchii.data_remote.networking.VideoApiService,
//    @Named("OriginalDatabase")
//    private val youTubeDatabase: com.vlascitchii.data_local.database.YouTubeDatabase
//): ShortsRepository {
//
//    override fun fetchShorts(videoType: com.vlascitchii.presentation_common.utils.VideoType, viewModelScope: CoroutineScope)
//    : Flow<PagingData<YoutubeVideoUiModel>> {
//        return Pager(
//            config = PagingConfig(
//                pageSize = 1,
//                maxSize = 3,
//                prefetchDistance = 1,
//                enablePlaceholders = false
//            ),
//            pagingSourceFactory = {
//                com.vlascitchii.presentation_common.enetity.video_list.core_paging.YoutubeVideoSource(
//                    apiVideoListService,
//                    viewModelScope,
//                    videoType,
//                    youTubeDatabase.youTubeVideoDao
//                )
//            }
//        ).flow
//    }
//}