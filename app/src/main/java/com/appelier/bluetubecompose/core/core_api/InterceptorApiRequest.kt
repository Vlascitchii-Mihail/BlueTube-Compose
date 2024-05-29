package com.appelier.bluetubecompose.core.core_api

import com.appelier.bluetubecompose.core.core_api.Constants.Companion.API_KEY
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class InterceptorApiRequest: Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val originalRequest = chain.request()
        val newUrl: HttpUrl = originalRequest.url.newBuilder()
            .addQueryParameter("key", API_KEY)
            .build()

        val newRequest: Request = originalRequest.newBuilder().url(newUrl).build()

        return chain.proceed(newRequest)
    }
}