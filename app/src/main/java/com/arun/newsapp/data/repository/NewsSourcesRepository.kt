package com.arun.newsapp.data.repository

import com.arun.newsapp.data.api.NetworkService
import com.arun.newsapp.data.model.SourceData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NewsSourcesRepository @Inject constructor(private val networkService: NetworkService) {

    fun getNewsSources(): Flow<List<SourceData>> {
        return flow {
            emit(networkService.getTopHeadlinesSources())
        }.map {
            it.sources
        }
    }
}