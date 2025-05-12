package com.vlascitchii.presentation_video_list.util

import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.vlascitchii.domain.custom_coroutine_scopes.AppCoroutineScope
import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideo
import com.vlascitchii.domain.usecase.SearchVideoListUseCase
import com.vlascitchii.domain.usecase.VideoPlayerUseCase
import com.vlascitchii.presentation_common.entity.util.CommonResultConverter
import com.vlascitchii.presentation_common.entity.videos.YoutubeVideoUiModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Named

class SearchVideoListConverter @Inject constructor(
    @Named("video")
    private val videoCoroutineScope: AppCoroutineScope
) :
    CommonResultConverter<SearchVideoListUseCase.Response, StateFlow<PagingData<YoutubeVideoUiModel>>>() {

    override fun convertSuccess(data: SearchVideoListUseCase.Response): StateFlow<PagingData<YoutubeVideoUiModel>> {
        return convertResultPager(data.pager)
    }

    private fun convertResultPager(pager: Pager<String, YoutubeVideo>): StateFlow<PagingData<YoutubeVideoUiModel>> {
//        var uiStatePagingData: PagingData<YoutubeVideoUiModel> = PagingData.empty()
//        videoCoroutineScope.launch {
//            val awaitPagingData = async {
//                pagingDataFlow.flow.map { pagingData: PagingData<YoutubeVideo> ->
//                    pagingData.map { youTubeVideo: YoutubeVideo ->
//                        youTubeVideo.convertToYoutubeVideoUiMode()
//                    }
//                }.first()
//            }
//
//            uiStatePagingData = awaitPagingData.await()
//
//        }
        return convertPager(pager).cachedIn(videoCoroutineScope)
            .stateIn(videoCoroutineScope, SharingStarted.Lazily, PagingData.empty())
    }
}