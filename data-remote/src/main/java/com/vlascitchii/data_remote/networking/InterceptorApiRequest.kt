package com.vlascitchii.data_remote.networking

import com.vlascitchii.data_remote.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

private const val API_KEY = BuildConfig.API_KEY

class InterceptorApiRequest @Inject constructor(): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val originalRequest = chain.request()
        val newRequest = originalRequest.newBuilder()
            .addHeader("X-Goog-Api-Key", API_KEY)
            .build()

        return chain.proceed(newRequest)
    }
}
