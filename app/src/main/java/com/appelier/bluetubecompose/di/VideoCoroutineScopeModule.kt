package com.appelier.bluetubecompose.di

import com.vlascitchii.domain.custom_coroutine_scopes.AppCoroutineScope
import com.vlascitchii.domain.custom_coroutine_scopes.VideoCoroutineScope
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
class VideoCoroutineScopeModule {
    
    @Provides
    @Named("video")
    fun provideRemoteVideoListDataScope(): AppCoroutineScope = VideoCoroutineScope()
}