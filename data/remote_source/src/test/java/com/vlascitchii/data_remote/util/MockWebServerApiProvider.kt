package com.vlascitchii.data_remote.util

import com.squareup.moshi.Moshi
import com.vlascitchii.data_remote.networking.service.BaseApiService
import com.vlascitchii.data_remote.networking.service.ShortsApiService
import com.vlascitchii.data_remote.networking.service.VideoListApiService
import com.vlascitchii.data_remote.source.util.MoshiParser
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val MOCK_BASE_URL = "/"

class MockWebServerApiProvider {

    private var mockWebServer: MockWebServer = MockWebServer()
    private val retrofit = Retrofit.Builder()
        .baseUrl(mockWebServer.url(MOCK_BASE_URL))
        .addConverterFactory(staticMoshiConverterFactory)
        .client(staticClient)
        .build()

    val mockWebServerScheduler = MockWebServerScheduler(mockWebServer, staticJsonHandler)

    fun provideMockBaseApiService(): BaseApiService {
        return retrofit.create(BaseApiService::class.java)
    }

    fun provideMockShortsApiService(): ShortsApiService {
        return retrofit.create(ShortsApiService::class.java)
    }

    fun provideMockVideoListApiService(): VideoListApiService {
        return retrofit.create(VideoListApiService::class.java)
    }

    companion object {
        private val staticClient = OkHttpClient.Builder().build()
        private val staticMoshi: Moshi = Moshi.Builder().build()
        private val staticMoshiConverterFactory = MoshiConverterFactory.create(staticMoshi)
        val staticMoshiParser: MoshiParser = MoshiParser(staticMoshi)
        val staticJsonHandler: JsonHandler = JsonHandler()
    }
}
