package com.arun.newsapp.di.component

import android.content.Context
import com.arun.newsapp.NewsApp
import com.arun.newsapp.data.api.NetworkService
import com.arun.newsapp.data.repository.CountriesRepository
import com.arun.newsapp.data.repository.TopHeadlineRepository
import com.arun.newsapp.di.ApplicationContext
import com.arun.newsapp.di.module.ApplicationModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {

    fun inject(application: NewsApp)

    @ApplicationContext
    fun getContext(): Context

    fun getNetworkService(): NetworkService

    fun getTopHeadlineRepository(): TopHeadlineRepository

    fun getCountriesRepository(): CountriesRepository
}