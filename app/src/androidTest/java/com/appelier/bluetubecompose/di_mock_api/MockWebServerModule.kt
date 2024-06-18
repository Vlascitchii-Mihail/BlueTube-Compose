package com.appelier.bluetubecompose.di_mock_api

import com.appelier.bluetubecompose.core.core_api.VideoApiService
import com.appelier.bluetubecompose.core.core_di.NetworkApiModule
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton


private const val BASE_URL  = "/"

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [NetworkApiModule::class]
)
object MockWebServerModule {

    @Singleton
    @Provides
    fun provideMockWebServer() = MockWebServer()

    @Singleton
    @Provides
    fun provideMockVideoApiService(mockWebServer: MockWebServer): VideoApiService {
        val client = OkHttpClient.Builder().build()
        val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(mockWebServer.url(BASE_URL))
                .addConverterFactory(MoshiConverterFactory.create())
                .client(client)
                .build()
        }

        return retrofit.create(VideoApiService::class.java)
    }
}