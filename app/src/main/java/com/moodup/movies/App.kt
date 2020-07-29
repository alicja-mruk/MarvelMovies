package com.moodup.movies

import android.app.Application
import com.moodup.movies.repository.module.*

import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application(){
    override fun onCreate() {
        super.onCreate()
        initKoin()
    }
    private fun initKoin(){
        startKoin {
            androidContext(this@App)
            modules(listOf(repositoryModule, retrofitModule, homeViewModelModule, favouritesViewModelModule,
            detailsViewModelModule, authenticationViewModelModule))
        }
    }
}