package com.arun.newsapp.di.module

import com.arun.newsapp.ui.countries.CountriesAdapter
import com.arun.newsapp.ui.languages.LanguagesAdapter
import com.arun.newsapp.ui.sources.NewsSourcesAdapter
import com.arun.newsapp.ui.topheadline.TopHeadlineAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
class ActivityModule {

    @ActivityScoped
    @Provides
    fun provideTopHeadlineAdapter() = TopHeadlineAdapter(ArrayList())

    @ActivityScoped
    @Provides
    fun provideCountriesAdapter() = CountriesAdapter(ArrayList())

    @ActivityScoped
    @Provides
    fun provideLanguagesAdapter() = LanguagesAdapter(ArrayList())

    @ActivityScoped
    @Provides
    fun provideNewsSourcesAdapter() = NewsSourcesAdapter(ArrayList())
}