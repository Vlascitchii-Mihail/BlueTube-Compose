package com.appelier.bluetubecompose.screen_shorts.di

import com.appelier.bluetubecompose.screen_shorts.repository.ShortsRepository
import com.appelier.bluetubecompose.screen_shorts.repository.ShortsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ModuleShorts {

    @Binds
    @Singleton
    abstract fun bindShortsRepository(shortsRepository: ShortsRepositoryImpl): ShortsRepository
}