package com.vlascitchii.presentation_shorts.screen_shorts

import androidx.paging.PagingData
import com.vlascitchii.domain.di_common.SHORTS_CONVERTER
import com.vlascitchii.domain.usecase.ShortsUseCase
import com.vlascitchii.presentation_common.model.util.CommonResultConverter
import com.vlascitchii.presentation_common.model.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_shorts.screen_shorts.utils.ShortsConverter
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
abstract class ShortsDiModule {

    @Binds
    @Named(SHORTS_CONVERTER)
    abstract fun bindShortsConverter(shortsConverter: ShortsConverter)
            : CommonResultConverter<ShortsUseCase.ShortsResponse, @JvmSuppressWildcards Flow<PagingData<YoutubeVideoUiModel>>>
}
