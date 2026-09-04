package com.arun.newsapp.data.repository

import com.arun.newsapp.data.model.Language
import com.arun.newsapp.utils.AppConstant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LanguagesRepository @Inject constructor() {

    fun getLanguages(): Flow<List<Language>> {
        return flow {
            emit(AppConstant.LANGUAGES)
        }
    }
}
