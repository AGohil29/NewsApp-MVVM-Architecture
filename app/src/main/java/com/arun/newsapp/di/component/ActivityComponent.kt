package com.arun.newsapp.di.component

import com.arun.newsapp.di.ActivityScope
import com.arun.newsapp.di.module.ActivityModule
import com.arun.newsapp.ui.countries.CountriesActivity
import com.arun.newsapp.ui.topheadline.TopHeadlineActivity
import dagger.Component

@ActivityScope
@Component(dependencies = [ApplicationComponent::class], modules = [ActivityModule::class])
interface ActivityComponent {
    fun inject(activity: TopHeadlineActivity)

    fun inject(activity: CountriesActivity)
}