package com.arun.newsapp.data.repository

import com.arun.newsapp.data.model.Country
import com.arun.newsapp.utils.AppConstant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CountriesRepository @Inject constructor() {

    fun getCountries(): Flow<List<Country>> {
        return flow {
            emit(AppConstant.COUNTRIES)
        }
    }
}