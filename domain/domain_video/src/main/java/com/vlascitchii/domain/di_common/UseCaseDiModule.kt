package com.vlascitchii.domain.di_common

import android.content.Context
import android.net.ConnectivityManager
import com.vlascitchii.domain.custom_scope.CustomCoroutineScope
import com.vlascitchii.domain.usecase.ShortsUseCase
import com.vlascitchii.domain.usecase.UseCase
import com.vlascitchii.domain.usecase.VideoListUseCase
import com.vlascitchii.domain.usecase.VideoPlayerUseCase
import com.vlascitchii.domain.usecase.util.DispatcherConfiguration
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BindUseCaseDiModule {

    @Binds
    @Named(VIDEO_LIST_USE_CASE)
    abstract fun bindVideoListUseCase(videoListUseCase: VideoListUseCase)
    : UseCase<VideoListUseCase.VideoListRequest, VideoListUseCase.VideoListResponse>

    @Binds
    @Named(SHORTS_USE_CASE)
    abstract fun bindShortsUseCase(shortsUseCase: ShortsUseCase)
    : UseCase<ShortsUseCase.ShortsRequest, ShortsUseCase.ShortsResponse>

    @Binds
    @Named(VIDEO_PLAYER_USE_CASE)
    abstract fun bindVideoPlayerUseCase(videoPlayerUseCase: VideoPlayerUseCase)
    : UseCase<VideoPlayerUseCase.PlayerRequest, VideoPlayerUseCase.PlayerResponse>
}


@Module
@InstallIn(SingletonComponent::class)
class ProvideUseCaseDiModule {

    @Provides
    fun provideDispatcherConfigurationWithIODispatcher() = DispatcherConfiguration()

    @Provides
    fun provideCoroutineDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    fun provideConnectivityManager(@ApplicationContext context: Context): ConnectivityManager
    = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
}
