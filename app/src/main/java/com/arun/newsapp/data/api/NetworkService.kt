package com.arun.newsapp.data.api

import com.arun.newsapp.data.model.SourcesResponse
import com.arun.newsapp.data.model.TopHeadlinesResponse
import com.arun.newsapp.utils.AppConstant.API_KEY
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface NetworkService {

    @Headers("X-Api-Key: $API_KEY", "User-Agent: ABC")
    @GET("top-headlines")
    suspend fun getTopHeadlines(@Query("country") country: String): TopHeadlinesResponse

    @Headers("X-Api-Key: $API_KEY", "User-Agent: ABC")
    @GET("top-headlines/sources")
    suspend fun getTopHeadlinesSources(): SourcesResponse

    @Headers("X-Api-Key: $API_KEY", "User-Agent: ABC")
    @GET("top-headlines")
    suspend fun getTopHeadlinesSources(@Query("sources") sources: String): TopHeadlinesResponse
}