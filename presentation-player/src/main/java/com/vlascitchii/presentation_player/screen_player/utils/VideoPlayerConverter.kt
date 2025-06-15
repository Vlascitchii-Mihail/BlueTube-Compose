package com.vlascitchii.presentation_player.screen_player.utils

import androidx.paging.PagingData
import com.vlascitchii.domain.usecase.VideoPlayerUseCase
import com.vlascitchii.presentation_common.entity.util.CommonResultConverter
import com.vlascitchii.presentation_common.entity.videos.YoutubeVideoUiModel
import javax.inject.Inject

class VideoPlayerConverter @Inject constructor()
    : CommonResultConverter<VideoPlayerUseCase.Response, PagingData<YoutubeVideoUiModel>>() {

    override fun convertSuccess(data: VideoPlayerUseCase.Response): PagingData<YoutubeVideoUiModel> {
        return convertPager(data.relatedVideoPagingData)
    }
}


