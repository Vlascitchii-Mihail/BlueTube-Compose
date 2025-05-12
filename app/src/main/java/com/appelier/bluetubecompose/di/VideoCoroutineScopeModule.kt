package com.appelier.bluetubecompose.di

import com.vlascitchii.domain.custom_coroutine_scopes.AppCoroutineScope
import com.vlascitchii.domain.custom_coroutine_scopes.VideoCoroutineScope
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
abstract class VideoCoroutineScopeModule {
    
    @Binds
    @Named("video")
    abstract fun bindRemoteVideoListDataScope(videoCoroutineScope: VideoCoroutineScope): AppCoroutineScope
}