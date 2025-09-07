package com.appelier.bluetubecompose.di

import android.content.Context
import android.net.ConnectivityManager
import com.vlascitchii.presentation_common.network_observer.NetworkConnectivityObserver
import com.vlascitchii.domain.repository.PlayerRepository
import com.vlascitchii.domain.repository.ShortsRepository
import com.vlascitchii.domain.repository.VideoListRepository
import com.vlascitchii.domain.usecase.SearchVideoListUseCase
import com.vlascitchii.domain.usecase.ShortsUseCase
import com.vlascitchii.domain.usecase.VideoListUseCase
import com.vlascitchii.domain.usecase.VideoPlayerUseCase
import com.vlascitchii.domain.usecase.util.Configuration
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    @Provides
    fun provideUseCaseConfiguration() = Configuration(Dispatchers.IO)

    @Provides
    fun provideVideoListUseCase(
        configuration: Configuration,
        videoListRepository: VideoListRepository
    ): VideoListUseCase = VideoListUseCase(configuration, videoListRepository)

    @Provides
    fun provideSearchVideoListUseCase(
        configuration: Configuration,
        videoListRepository: VideoListRepository
    ): SearchVideoListUseCase = SearchVideoListUseCase(configuration, videoListRepository)

    @Provides
    fun providesShortsUseCase(
        configuration: Configuration,
        shortsRepository: ShortsRepository
    ): ShortsUseCase = ShortsUseCase(configuration, shortsRepository)

    @Provides
    fun provideVideoPlayerUseCase(
        configuration: Configuration,
        playerRepository: PlayerRepository
    ): VideoPlayerUseCase = VideoPlayerUseCase(configuration, playerRepository)

    @Provides
    fun provideConnectivityObserver(@ApplicationContext context : Context) = NetworkConnectivityObserver(
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    )
}