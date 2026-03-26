package com.arun.newsapp.data.api

import com.arun.newsapp.di.NetworkApiKey
import com.arun.newsapp.di.UserAgent
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor(
    @NetworkApiKey private val apiKey: String,
    @UserAgent private val userAgent: String,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()
            .header("X-Api-Key", apiKey)
            .header("User-Agent", userAgent)
        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}