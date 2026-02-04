package com.vlascitchii.bluetubecompose.fake.converter

import androidx.paging.PagingData
import com.vlascitchii.bluetubecompose.fake.data.UI_MODEL_VIDEO_LIST_WITH_CHANNEL_IMG_URL
import com.vlascitchii.domain.usecase.ShortsUseCase
import com.vlascitchii.domain.usecase.VideoListUseCase
import com.vlascitchii.domain.usecase.VideoPlayerUseCase
import com.vlascitchii.presentation_common.model.util.CommonResultConverter
import com.vlascitchii.presentation_common.model.videos.YoutubeVideoUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

private val pagingData: Flow<PagingData<YoutubeVideoUiModel>> = flowOf(PagingData.from(UI_MODEL_VIDEO_LIST_WITH_CHANNEL_IMG_URL))

class FakeVideoListConverter @Inject constructor()
    : CommonResultConverter<VideoListUseCase.VideoListResponse, @JvmSuppressWildcards Flow<PagingData<YoutubeVideoUiModel>>>(){

        override fun convertSuccess(data: VideoListUseCase.VideoListResponse): @JvmSuppressWildcards Flow<PagingData<YoutubeVideoUiModel>> = pagingData
}

class FakeShortsConverter @Inject constructor()
    : CommonResultConverter<ShortsUseCase.ShortsResponse, @JvmSuppressWildcards Flow<PagingData<YoutubeVideoUiModel>>>() {

        override fun convertSuccess(data: ShortsUseCase.ShortsResponse): @JvmSuppressWildcards Flow<PagingData<YoutubeVideoUiModel>> = pagingData
}

class FakePlayerConverter @Inject constructor()
    : CommonResultConverter<VideoPlayerUseCase.PlayerResponse, @JvmSuppressWildcards Flow<PagingData<YoutubeVideoUiModel>>>() {

    override fun convertSuccess(data: VideoPlayerUseCase.PlayerResponse): @JvmSuppressWildcards Flow<PagingData<YoutubeVideoUiModel>> = pagingData
}
