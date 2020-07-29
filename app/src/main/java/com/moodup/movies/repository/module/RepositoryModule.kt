package com.moodup.movies.repository.module

import com.moodup.movies.repository.api.call.MovieRepository
import org.koin.dsl.module


val repositoryModule = module {
    single {
        MovieRepository()
    }
}


