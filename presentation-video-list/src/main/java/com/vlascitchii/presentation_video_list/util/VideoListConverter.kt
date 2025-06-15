package com.vlascitchii.presentation_video_list.util

import androidx.paging.PagingData
import com.vlascitchii.domain.custom_coroutine_scopes.AppCoroutineScope
import com.vlascitchii.domain.usecase.VideoListUseCase
import com.vlascitchii.presentation_common.entity.util.CommonResultConverter
import com.vlascitchii.presentation_common.entity.videos.YoutubeVideoUiModel
import javax.inject.Inject
import javax.inject.Named


class VideoListConverter @Inject constructor()
    : CommonResultConverter<VideoListUseCase.Response,  PagingData<YoutubeVideoUiModel>>() {

    override fun convertSuccess(useCaseResponse: VideoListUseCase.Response):  PagingData<YoutubeVideoUiModel> {
        return convertPager(useCaseResponse.youTubePopularVideoPagingData)
    }
}