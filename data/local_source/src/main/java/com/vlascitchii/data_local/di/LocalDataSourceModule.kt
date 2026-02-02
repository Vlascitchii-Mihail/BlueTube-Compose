package com.vlascitchii.data_local.di

import com.vlascitchii.data_local.source.DatabaseVideoSourceImpl
import com.vlascitchii.data_repository.data_source.local.LocalVideoListDataSource
import com.vlascitchii.domain.di_common.DATABASE_SOURCE
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalDataSourceModule {

    @Binds
    @Named(DATABASE_SOURCE)
    abstract fun bindDatabaseDataSource(databaseVideoSourceImpl: DatabaseVideoSourceImpl)
    : LocalVideoListDataSource
}
