package com.moodup.movies.repository.module

import com.moodup.movies.repository.api.call.MovieRepository
import com.moodup.movies.repository.firebase.FirebaseAuthLoginHelper
import com.moodup.movies.repository.firebase.FirebaseAuthRegisterHelper
import org.koin.dsl.module

val registerModule = module {
    single {
        FirebaseAuthRegisterHelper()
    }
}
val loginModule = module{
    single{
        FirebaseAuthLoginHelper()
    }
}