package com.vlascitchii.presentation_video_list.screen_video_list.repository

//import androidx.paging.Pager
//import androidx.paging.PagingConfig
//import androidx.paging.PagingData
//import com.vlascitchii.data_remote.networking.VideoApiService
//import com.vlascitchii.data_local.enetity.video_list.videos.YoutubeVideoEntity
//import com.appelier.bluetubecompose.utils.VideoType
//import com.vlascitchii.presentation_common.enetity.video_list.core_paging.YoutubeVideoSource
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.flow.Flow
//import javax.inject.Inject
//import javax.inject.Named
//
//interface VideoListRepository {
//
//    fun fetchVideos(videoType: VideoType, viewModelScope: CoroutineScope)
//    : Flow<PagingData<YoutubeVideoEntity>>
//}
//
//class VideoListRepositoryImpl @Inject constructor(
//    private val apiVideoListService: com.vlascitchii.data_remote.networking.VideoApiService,
//    @Named("OriginalDatabase")
//    private val youTubeDatabase: com.vlascitchii.data_local.database.YouTubeDatabase
//): VideoListRepository {
//
//    override fun fetchVideos(videoType: VideoType, viewModelScope: CoroutineScope)
//    : Flow<PagingData<YoutubeVideoEntity>> {
//
//        return Pager(
//            config = PagingConfig(
//                pageSize = 5,
//                prefetchDistance = 15,
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
