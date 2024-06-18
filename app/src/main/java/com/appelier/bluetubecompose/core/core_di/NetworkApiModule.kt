package com.appelier.bluetubecompose.core.core_di

import com.appelier.bluetubecompose.core.core_api.Constants.Companion.BASE_URL
import com.appelier.bluetubecompose.core.core_api.InterceptorApiRequest
import com.appelier.bluetubecompose.core.core_api.VideoApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkApiModule {

    private val interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY

    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(InterceptorApiRequest())
        .addInterceptor(interceptor)
        .build()

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .client(client)
        .build()

    @Singleton
    @Provides

    fun provideVideoListApi(retrofit: Retrofit): VideoApiService =
        retrofit.create(VideoApiService::class.java)
}
