package com.vlascitchii.data_repository.data_source.di

import com.vlascitchii.data_repository.repository_impl.PlayerRepositoryImpl
import com.vlascitchii.data_repository.repository_impl.ShortsListRepositoryImpl
import com.vlascitchii.data_repository.repository_impl.VideoListRepositoryImpl
import com.vlascitchii.domain.repository.PlayerRepository
import com.vlascitchii.domain.repository.ShortsRepository
import com.vlascitchii.domain.repository.VideoListRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryDiModule {

    @Binds
    abstract fun bindVideoListRepository(videoListRepositoryImpl: VideoListRepositoryImpl): VideoListRepository

    @Binds
    abstract fun bindShortsListRepository(shortsListRepositoryImpl: ShortsListRepositoryImpl): ShortsRepository

    @Binds
    abstract fun bindPlayerRepository(playerRepositoryImpl: PlayerRepositoryImpl): PlayerRepository
}
