package com.appelier.bluetubecompose.di

import com.vlascitchii.data_repository.data_source.local.LocalVideoListDataSource
import com.vlascitchii.data_repository.data_source.remote.RemoteSearchDataSource
import com.vlascitchii.data_repository.data_source.remote.RemoteShortsDataSource
import com.vlascitchii.data_repository.data_source.remote.RemoteVideoListDataSource
import com.vlascitchii.data_repository.repository_impl.PlayerRepositoryImpl
import com.vlascitchii.data_repository.repository_impl.ShortsRepositoryImpl
import com.vlascitchii.data_repository.repository_impl.VideoListRepositoryImpl
import com.vlascitchii.domain.repository.PlayerRepository
import com.vlascitchii.domain.repository.ShortsRepository
import com.vlascitchii.domain.repository.VideoListRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    fun provideVideoListRepository(
        remoteVideoListDataSource: RemoteVideoListDataSource,
        remoteSearchDataSource: RemoteSearchDataSource,
        localVideoListDataSource: LocalVideoListDataSource
    ): VideoListRepository =
        VideoListRepositoryImpl(remoteVideoListDataSource, remoteSearchDataSource, localVideoListDataSource)

    @Provides
    fun provideShortsRepository(
        remoteShortsDataSource: RemoteShortsDataSource,
        localVideoListDataSource: LocalVideoListDataSource
    ): ShortsRepository = ShortsRepositoryImpl(remoteShortsDataSource, localVideoListDataSource)

    @Provides
    fun providePlayerRepository(
       remoteSearchDataSource: RemoteSearchDataSource,
       localVideoListDataSource: LocalVideoListDataSource
    ): PlayerRepository = PlayerRepositoryImpl(remoteSearchDataSource, localVideoListDataSource)
}