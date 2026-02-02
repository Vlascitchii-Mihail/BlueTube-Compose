package com.vlascitchii.bluetubecompose.di

import androidx.paging.PagingData
import com.vlascitchii.domain.di_common.PLAYER_CONVERTER
import com.vlascitchii.domain.di_common.SHORTS_CONVERTER
import com.vlascitchii.domain.di_common.VIDEO_LIST_CONVERTER
import com.vlascitchii.domain.usecase.ShortsUseCase
import com.vlascitchii.domain.usecase.VideoListUseCase
import com.vlascitchii.domain.usecase.VideoPlayerUseCase
import com.vlascitchii.presentation_common.model.util.CommonResultConverter
import com.vlascitchii.presentation_common.model.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_player.screen_player.utils.VideoPlayerConverter
import com.vlascitchii.presentation_shorts.screen_shorts.utils.ShortsConverter
import com.vlascitchii.presentation_video_list.util.VideoListConverter
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
abstract class ConverterDiModule {

    @Binds
    @Named(VIDEO_LIST_CONVERTER)
    abstract fun bindVideoListConverter(videoListConverter: VideoListConverter)
    : CommonResultConverter<VideoListUseCase.VideoListResponse, @JvmSuppressWildcards Flow<PagingData<YoutubeVideoUiModel>>>

    @Binds
    @Named(PLAYER_CONVERTER)
    abstract fun bindPlayerConverter(playerConverter: VideoPlayerConverter)
    : CommonResultConverter<VideoPlayerUseCase.PlayerResponse, @JvmSuppressWildcards Flow<PagingData<YoutubeVideoUiModel>>>

    @Binds
    @Named(SHORTS_CONVERTER)
    abstract fun bindShortsConverter(shortsConverter: ShortsConverter)
    : CommonResultConverter<ShortsUseCase.ShortsResponse, @JvmSuppressWildcards Flow<PagingData<YoutubeVideoUiModel>>>
}
