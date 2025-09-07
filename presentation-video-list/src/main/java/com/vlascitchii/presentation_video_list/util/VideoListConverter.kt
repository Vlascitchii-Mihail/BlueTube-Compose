package com.vlascitchii.presentation_video_list.util

import androidx.paging.PagingData
import com.vlascitchii.domain.usecase.VideoListUseCase
import com.vlascitchii.presentation_common.model.util.CommonResultConverter
import com.vlascitchii.presentation_common.model.videos.YoutubeVideoUiModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class VideoListConverter @Inject constructor()
    : CommonResultConverter<VideoListUseCase.VideoListResponse, Flow<PagingData<YoutubeVideoUiModel>>>() {

    override fun convertSuccess(useCaseResponse: VideoListUseCase.VideoListResponse): Flow<PagingData<YoutubeVideoUiModel>> {
        return convertPager(useCaseResponse.youTubePopularVideoPagingData)
    }
}
