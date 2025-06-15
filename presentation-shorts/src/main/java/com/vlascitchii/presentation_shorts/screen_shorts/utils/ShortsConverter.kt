package com.vlascitchii.presentation_shorts.screen_shorts.utils

import androidx.paging.PagingData
import com.vlascitchii.domain.usecase.ShortsUseCase
import com.vlascitchii.presentation_common.entity.util.CommonResultConverter
import com.vlascitchii.presentation_common.entity.videos.YoutubeVideoUiModel
import javax.inject.Inject

class ShortsConverter @Inject constructor()
    : CommonResultConverter<ShortsUseCase.Response, PagingData<YoutubeVideoUiModel>>() {

    override fun convertSuccess(data: ShortsUseCase.Response): PagingData<YoutubeVideoUiModel> {
        return convertPager(data.shortsPopularVideoPagingData)
    }
}