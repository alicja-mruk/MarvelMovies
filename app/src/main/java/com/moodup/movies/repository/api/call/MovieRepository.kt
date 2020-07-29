package com.moodup.movies.repository.api.call


import com.moodup.movies.model.Result
import org.koin.core.KoinComponent
import retrofit2.Call

class MovieRepository : KoinComponent {

    fun getAllMovies(totalItemCount : Int, query: String): Call<Result> {
        val request =
            ServiceBuilder.buildService(
                MoviesService::class.java
            )
        lateinit var call: Call<Result>

        call = if (query == "") {
            request.getAllMovies(totalItemCount)
        } else {
            request.getMoviesByTitle(totalItemCount, query)
        }

        return call
    }
}