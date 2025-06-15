package com.vlascitchii.presentation_video_list.util

import androidx.paging.PagingData
import com.vlascitchii.domain.usecase.SearchVideoListUseCase
import com.vlascitchii.presentation_common.entity.util.CommonResultConverter
import com.vlascitchii.presentation_common.entity.videos.YoutubeVideoUiModel
import javax.inject.Inject

class SearchVideoListConverter @Inject constructor() :
    CommonResultConverter<SearchVideoListUseCase.Response, PagingData<YoutubeVideoUiModel>>() {

    override fun convertSuccess(useCaseResponse: SearchVideoListUseCase.Response): PagingData<YoutubeVideoUiModel> {
        return convertPager(useCaseResponse.youTubeSearchVideoPagingData)
    }
}