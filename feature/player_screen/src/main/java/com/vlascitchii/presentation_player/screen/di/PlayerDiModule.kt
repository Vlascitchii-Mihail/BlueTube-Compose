package com.vlascitchii.presentation_player.screen.di

import androidx.paging.PagingData
import com.vlascitchii.domain.di_common.PLAYER_CONVERTER
import com.vlascitchii.domain.usecase.VideoPlayerUseCase
import com.vlascitchii.presentation_common.model.util.CommonResultConverter
import com.vlascitchii.presentation_common.model.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_player.screen.utils.VideoPlayerConverter
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
abstract class PlayerDiModule {

    @Binds
    @Named(PLAYER_CONVERTER)
    abstract fun bindPlayerConverter(playerConverter: VideoPlayerConverter)
            : CommonResultConverter<VideoPlayerUseCase.PlayerResponse, @JvmSuppressWildcards Flow<PagingData<YoutubeVideoUiModel>>>

}
