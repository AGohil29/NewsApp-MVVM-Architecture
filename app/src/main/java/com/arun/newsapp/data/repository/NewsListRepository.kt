package com.arun.newsapp.data.repository

import com.arun.newsapp.data.api.NetworkService
import com.arun.newsapp.data.model.Article
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NewsListRepository @Inject constructor(private val networkService: NetworkService) {

    fun getNewsByCountry(country: String): Flow<List<Article>> {
        return flow {
            emit(networkService.getTopHeadlines(country))
        }.map {
            it.articles
        }
    }

    fun getNewsBySources(sources: String): Flow<List<Article>> {
        return flow {
            emit(networkService.getTopHeadlinesSources(sources))
        }.map {
            it.articles
        }
    }
}