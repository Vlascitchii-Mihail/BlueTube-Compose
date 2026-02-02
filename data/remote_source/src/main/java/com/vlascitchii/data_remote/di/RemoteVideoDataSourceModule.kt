package com.vlascitchii.data_remote.di

import com.vlascitchii.data_remote.source.RemoteSearchVideoDataSourceImpl
import com.vlascitchii.data_remote.source.RemoteShortsDataSourceImpl
import com.vlascitchii.data_remote.source.RemoteVideoListDataSourceImpl
import com.vlascitchii.data_repository.data_source.remote.RemoteSearchDataSource
import com.vlascitchii.data_repository.data_source.remote.RemoteVideoListDataSource
import com.vlascitchii.domain.di_common.REMOTE_SEARCH_VIDEO_OR_SHORTS_LIST_SOURCE
import com.vlascitchii.domain.di_common.REMOTE_SHORTS_LIST_SOURCE
import com.vlascitchii.domain.di_common.REMOTE_VIDEO_LIST_SOURCE
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteVideoDataSourceModule {

    @Binds
    @Named(REMOTE_VIDEO_LIST_SOURCE)
    abstract fun bindRemoteVideoListDataSource(remoteVideoListDataSourceImpl: RemoteVideoListDataSourceImpl): RemoteVideoListDataSource

    @Binds
    @Named(REMOTE_SEARCH_VIDEO_OR_SHORTS_LIST_SOURCE)
    abstract fun bindRemoteSearchListDataSource(remoteSearchVideoDataSourceImpl: RemoteSearchVideoDataSourceImpl): RemoteSearchDataSource

    @Binds
    @Named(REMOTE_SHORTS_LIST_SOURCE)
    abstract fun bindRemoteShortsDataSource(remoteShortsDataSourceImpl: RemoteShortsDataSourceImpl): RemoteVideoListDataSource
}
