package com.vlascitchii.data_remote.di

import com.vlascitchii.data_remote.source.RemoteSearchDataSourceImpl
import com.vlascitchii.data_remote.source.RemoteShortsDataSourceImpl
import com.vlascitchii.data_remote.source.RemoteVideoListDataSourceImpl
import com.vlascitchii.data_repository.data_source.remote.RemoteSearchDataSource
import com.vlascitchii.data_repository.data_source.remote.RemoteShortsDataSource
import com.vlascitchii.data_repository.data_source.remote.RemoteVideoListDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteVideoDataSourceModule {

    @Binds
    abstract fun bindRemoteVideoListDataSource(remoteVideoListDataSourceImpl: RemoteVideoListDataSourceImpl): RemoteVideoListDataSource

    @Binds
    abstract fun bindRemoteSearchListDataSource(remoteSearchDataSourceImpl: RemoteSearchDataSourceImpl): RemoteSearchDataSource

    @Binds
    abstract fun bindRemoteShortsDataSource(remoteShortsDataSourceImpl: RemoteShortsDataSourceImpl): RemoteShortsDataSource
}