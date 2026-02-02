package com.vlascitchii.presentation_common.di

import com.vlascitchii.presentation_common.network_observer.NetworkConnectivityAbstraction
import com.vlascitchii.presentation_common.network_observer.NetworkConnectivityObserver
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CommonDiModule {

    @Binds
    @Singleton
    abstract fun provideConnectivityObserver(networkConnectivityObserver: NetworkConnectivityObserver): NetworkConnectivityAbstraction
}
