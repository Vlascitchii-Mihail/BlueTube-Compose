package com.appelier.bluetubecompose.screen_video_list.di

import com.appelier.bluetubecompose.screen_video_list.repository.VideoListRepository
import com.appelier.bluetubecompose.screen_video_list.repository.VideoListRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ModuleVideoList {

    @Binds
    @Singleton
    abstract fun bindVideoRepository(videoRepository: VideoListRepositoryImpl)
    : VideoListRepository
}