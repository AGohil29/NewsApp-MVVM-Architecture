package com.arun.newsapp.di.module

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.arun.newsapp.data.repository.CountriesRepository
import com.arun.newsapp.data.repository.TopHeadlineRepository
import com.arun.newsapp.di.ActivityContext
import com.arun.newsapp.di.ActivityScope
import com.arun.newsapp.ui.base.ViewModelProviderFactory
import com.arun.newsapp.ui.countries.CountriesAdapter
import com.arun.newsapp.ui.countries.CountriesViewModel
import com.arun.newsapp.ui.topheadline.TopHeadlineAdapter
import com.arun.newsapp.ui.topheadline.TopHeadlineViewModel
import dagger.Module
import dagger.Provides

@Module
class ActivityModule(private val activity: AppCompatActivity) {

    @ActivityContext
    @Provides
    fun provideContext(): Context {
        return activity
    }

    @Provides
    fun provideNewsListViewModel(topHeadlineRepository: TopHeadlineRepository): TopHeadlineViewModel {
        return ViewModelProvider(activity,
            ViewModelProviderFactory(TopHeadlineViewModel::class){
                TopHeadlineViewModel(topHeadlineRepository)
            })[TopHeadlineViewModel::class.java]
    }

    @ActivityScope
    @Provides
    fun provideTopHeadlineAdapter() = TopHeadlineAdapter(ArrayList())

    @Provides
    fun provideCountriesViewModel(countriesRepository: CountriesRepository): CountriesViewModel {
        return ViewModelProvider(activity,
            ViewModelProviderFactory(CountriesViewModel::class) {
                CountriesViewModel(countriesRepository)
        })[CountriesViewModel::class.java]
    }

    @ActivityScope
    @Provides
    fun provideCountriesAdapter() = CountriesAdapter(ArrayList())
}