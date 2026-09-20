package com.arun.newsapp.data.repository

import com.arun.newsapp.data.api.NetworkService
import com.arun.newsapp.data.model.Article
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepository @Inject constructor(private val networkService: NetworkService) {

    fun getNews(query: String): Flow<List<Article>> {
        return flow {
            emit(networkService.getNews(query))
        }.map {
            it.articles
        }
    }

}