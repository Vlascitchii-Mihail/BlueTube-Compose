package com.vlascitchii.bluetubecompose.di

import android.content.Context
import android.net.ConnectivityManager
import com.vlascitchii.domain.repository.PlayerRepository
import com.vlascitchii.domain.repository.ShortsRepository
import com.vlascitchii.domain.repository.VideoListRepository
import com.vlascitchii.domain.usecase.ShortsUseCase
import com.vlascitchii.domain.usecase.VideoListUseCase
import com.vlascitchii.domain.usecase.VideoPlayerUseCase
import com.vlascitchii.domain.usecase.util.DispatcherConfiguration
import com.vlascitchii.presentation_common.network_observer.NetworkConnectivityObserver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    @Provides
    fun provideUseCaseConfiguration() = DispatcherConfiguration(Dispatchers.IO)

    @Provides
    fun provideVideoListUseCase(
        configuration: DispatcherConfiguration,
        videoListRepository: VideoListRepository
    ): VideoListUseCase = VideoListUseCase(configuration, videoListRepository)

    @Provides
    fun providesShortsUseCase(
        configuration: DispatcherConfiguration,
        shortsRepository: ShortsRepository
    ): ShortsUseCase = ShortsUseCase(configuration, shortsRepository)

    @Provides
    fun provideVideoPlayerUseCase(
        configuration: DispatcherConfiguration,
        playerRepository: PlayerRepository
    ): VideoPlayerUseCase = VideoPlayerUseCase(configuration, playerRepository)

    @Provides
    @Singleton
    fun provideConnectivityObserver(@ApplicationContext context : Context) = NetworkConnectivityObserver(
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    )
}