package com.vlascitchii.presentation_player.screen.utils

import androidx.paging.PagingData
import com.vlascitchii.domain.model.videos.YoutubeVideoDomain
import com.vlascitchii.domain.usecase.VideoPlayerUseCase
import com.vlascitchii.domain.util.VideoResult
import com.vlascitchii.presentation_common.model.util.CommonResultConverter
import com.vlascitchii.presentation_common.model.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_common.ui.state_common.UiState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class VideoPlayerConverter @Inject constructor()
    : CommonResultConverter<VideoPlayerUseCase.PlayerResponse, @JvmSuppressWildcards Flow<PagingData<YoutubeVideoUiModel>>>() {

    override fun convertSuccess(
        data: VideoPlayerUseCase.PlayerResponse,
    ): Flow<PagingData<YoutubeVideoUiModel>> {
        return convertPager(data.relatedVideoPagingData)
    }

    override fun convertSuccessVideo(data: Flow<PagingData<YoutubeVideoDomain>>): UiState<@JvmSuppressWildcards Flow<PagingData<YoutubeVideoUiModel>>> {
        return UiState.Success(convertPager(data))
    }

    override fun unpack(
        result: VideoResult<VideoPlayerUseCase.PlayerResponse>,
    ): Flow<PagingData<YoutubeVideoDomain>>? {
        return if (result is VideoResult.Success) result.data.relatedVideoPagingData else null
    }
}
