package com.vlascitchii.presentation_video_list.di

import androidx.paging.PagingData
import com.vlascitchii.domain.di_common.VIDEO_LIST_CONVERTER
import com.vlascitchii.domain.usecase.VideoListUseCase
import com.vlascitchii.presentation_common.model.util.CommonResultConverter
import com.vlascitchii.presentation_common.model.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_video_list.util.VideoListConverter
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
abstract class VideoListDiModule {

    @Binds
    @Named(VIDEO_LIST_CONVERTER)
    abstract fun bindVideoListConverter(videoListConverter: VideoListConverter)
            : CommonResultConverter<VideoListUseCase.VideoListResponse, @JvmSuppressWildcards Flow<PagingData<YoutubeVideoUiModel>>>

}
