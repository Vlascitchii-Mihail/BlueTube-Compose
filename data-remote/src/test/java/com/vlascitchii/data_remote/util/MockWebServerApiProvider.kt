package com.vlascitchii.data_remote.util

import com.vlascitchii.data_remote.networking.service.BaseApiService
import com.vlascitchii.data_remote.networking.service.ParticularVideoApiService
import com.vlascitchii.data_remote.networking.service.SearchApiService
import com.vlascitchii.data_remote.networking.service.ShortsApiService
import com.vlascitchii.data_remote.networking.service.VideoListApiService
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val MOCK_BASE_URL = "/"

class MockWebServerApiProvider {

    private var mockWebServer: MockWebServer = MockWebServer()
    private val client = OkHttpClient.Builder().build()
    private val moshiConverterFactory = MoshiConverterFactory.create()
    private val retrofit = Retrofit.Builder()
        .baseUrl(mockWebServer.url(MOCK_BASE_URL))
        .addConverterFactory(moshiConverterFactory)
        .client(client)
        .build()
    val mockWebServerScheduler = MockWebServerScheduler(mockWebServer)

    fun provideMockBaseApiService(): BaseApiService {
        return retrofit.create(BaseApiService::class.java)
    }
    fun provideMockSearchApiService(): SearchApiService {
        return retrofit.create(SearchApiService::class.java)
    }

    fun provideMockShortsApiService(): ShortsApiService {
        return retrofit.create(ShortsApiService::class.java)
    }

    fun provideMockVideoListApiService(): VideoListApiService {
        return retrofit.create(VideoListApiService::class.java)
    }

    fun provideParticularVideoApiService(): ParticularVideoApiService {
        return retrofit.create(ParticularVideoApiService::class.java)
    }
}