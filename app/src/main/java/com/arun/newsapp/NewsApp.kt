package com.arun.newsapp

import android.app.Application
import com.arun.newsapp.di.component.ApplicationComponent
import com.arun.newsapp.di.component.DaggerApplicationComponent
import com.arun.newsapp.di.module.ApplicationModule

class NewsApp: Application() {
    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        injectDependencies()
    }

    private fun injectDependencies() {
        applicationComponent = DaggerApplicationComponent
            .builder()
            .applicationModule(ApplicationModule(this))
            .build()
        applicationComponent.inject(this)
    }
}