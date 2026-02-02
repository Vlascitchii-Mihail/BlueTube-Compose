package com.vlascitchii.presentation_shorts.screen_shorts.utils

import androidx.paging.PagingData
import com.vlascitchii.domain.usecase.ShortsUseCase
import com.vlascitchii.presentation_common.model.util.CommonResultConverter
import com.vlascitchii.presentation_common.model.videos.YoutubeVideoUiModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ShortsConverter @Inject constructor()
    : CommonResultConverter<ShortsUseCase.ShortsResponse, @JvmSuppressWildcards Flow<PagingData<YoutubeVideoUiModel>>>() {

    override fun convertSuccess(data: ShortsUseCase.ShortsResponse): Flow<PagingData<YoutubeVideoUiModel>> {
        return convertPager(data.shortsPopularVideoPagingData)
    }
}
