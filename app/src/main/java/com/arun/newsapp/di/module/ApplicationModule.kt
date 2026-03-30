package com.arun.newsapp.di.module

import com.arun.newsapp.BuildConfig
import com.arun.newsapp.data.api.HeaderInterceptor
import com.arun.newsapp.data.api.NetworkService
import com.arun.newsapp.di.BaseUrl
import com.arun.newsapp.di.NetworkApiKey
import com.arun.newsapp.di.UserAgent
import com.arun.newsapp.utils.AppConstant
import com.arun.newsapp.utils.DefaultDispatcherProvider
import com.arun.newsapp.utils.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @BaseUrl
    @Provides
    fun provideBaseUrl(): String = "https://newsapi.org/v2/"

    @NetworkApiKey
    @Provides
    fun provideNetworkApiKey(): String = AppConstant.API_KEY

    @UserAgent
    @Provides
    fun provideUserAgent(): String = "ABC"

    @Provides
    @Singleton
    fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    @Singleton
    fun provideHeaderInterceptor(
        @NetworkApiKey apiKey: String,
        @UserAgent userAgent: String,
    ): HeaderInterceptor {
        return HeaderInterceptor(apiKey, userAgent)
    }

    @Provides
    @Singleton
    fun provideOkhttpClient(headerInterceptor: HeaderInterceptor): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG)
                HttpLoggingInterceptor.Level.BODY
            else
                HttpLoggingInterceptor.Level.NONE
        }
        return OkHttpClient.Builder()
            .addInterceptor(headerInterceptor)
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideNetworkService(
        @BaseUrl baseUrl: String,
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
    ): NetworkService {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
            .create(NetworkService::class.java)
    }

    @Provides
    @Singleton
    fun provideDispatcherProvider(): DispatcherProvider = DefaultDispatcherProvider()
}