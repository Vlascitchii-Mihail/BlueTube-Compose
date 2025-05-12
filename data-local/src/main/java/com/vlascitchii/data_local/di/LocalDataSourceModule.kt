package com.vlascitchii.data_local.di

import com.vlascitchii.data_local.source.LocalVideoListDataSourceImpl
import com.vlascitchii.data_repository.data_source.local.LocalVideoListDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalDataSourceModule {

    @Binds
    abstract fun bindLocalVideoListDataSource(localVideoListDataSourceImpl: LocalVideoListDataSourceImpl)
            : LocalVideoListDataSource
}