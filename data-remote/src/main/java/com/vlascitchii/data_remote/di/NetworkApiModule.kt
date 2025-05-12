package com.vlascitchii.data_remote.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.vlascitchii.data_remote.networking.Constants
import com.vlascitchii.data_remote.networking.InterceptorApiRequest
import com.vlascitchii.data_remote.networking.service.SearchApiService
import com.vlascitchii.data_remote.networking.service.ShortsApiService
import com.vlascitchii.data_remote.networking.service.VideoListApiService
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
        level = okhttp3.logging.HttpLoggingInterceptor.Level.BODY

    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(InterceptorApiRequest())
        .addInterceptor(interceptor)
        .build()

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Singleton
    @Provides
    fun provideRetrofit(moshi: Moshi): Retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(client)
        .build()

    @Singleton
    @Provides
    fun provideVideoListApi(retrofit: Retrofit): VideoListApiService =
        retrofit.create(VideoListApiService::class.java)

    @Singleton
    @Provides
    fun provideShortsApiService(retrofit: Retrofit): ShortsApiService =
        retrofit.create(ShortsApiService::class.java)

    @Singleton
    @Provides
    fun provideSearchApiService(retrofit: Retrofit): SearchApiService =
        retrofit.create(SearchApiService::class.java)
}
