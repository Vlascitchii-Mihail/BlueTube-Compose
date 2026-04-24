package com.vlascitchii.presentation_shorts.screen_shorts.utils

import androidx.paging.PagingData
import com.vlascitchii.domain.model.videos.YoutubeVideoDomain
import com.vlascitchii.domain.usecase.ShortsUseCase
import com.vlascitchii.domain.usecase.VideoListUseCase
import com.vlascitchii.domain.util.VideoResult
import com.vlascitchii.presentation_common.model.util.CommonResultConverter
import com.vlascitchii.presentation_common.model.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_common.ui.state_common.UiState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ShortsConverter @Inject constructor()
    : CommonResultConverter<ShortsUseCase.ShortsResponse, @JvmSuppressWildcards Flow<PagingData<YoutubeVideoUiModel>>>() {

    override fun convertSuccess(
        data: ShortsUseCase.ShortsResponse,
    ): Flow<PagingData<YoutubeVideoUiModel>> {
        return convertPager(data.shortsPopularVideoPagingData)
    }

    override fun convertSuccessVideo(data: Flow<PagingData<YoutubeVideoDomain>>): UiState<@JvmSuppressWildcards Flow<PagingData<YoutubeVideoUiModel>>> {
        return UiState.Success(convertPager(data))
    }

    override fun unpack(
        result: VideoResult<ShortsUseCase.ShortsResponse>,
    ): Flow<PagingData<YoutubeVideoDomain>>? {
        return if (result is VideoResult.Success) result.data.shortsPopularVideoPagingData else null
    }
}
