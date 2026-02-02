package com.vlascitchii.presentation_player.screen_player.utils

import androidx.paging.PagingData
import com.vlascitchii.domain.usecase.VideoPlayerUseCase
import com.vlascitchii.presentation_common.model.util.CommonResultConverter
import com.vlascitchii.presentation_common.model.videos.YoutubeVideoUiModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class VideoPlayerConverter @Inject constructor()
    : CommonResultConverter<VideoPlayerUseCase.PlayerResponse, @JvmSuppressWildcards Flow<PagingData<YoutubeVideoUiModel>>>() {

    override fun convertSuccess(data: VideoPlayerUseCase.PlayerResponse): Flow<PagingData<YoutubeVideoUiModel>> {
        return convertPager(data.relatedVideoPagingData)
    }
}
