package com.moodup.movies.repository.api.call


import com.moodup.movies.model.Result
import retrofit2.Call

class MovieRepository {

    fun getAllMovies(totalItemCount : Int, query: String): Call<Result> {
        val request =
            ServiceBuilder.buildService(
                MoviesService::class.java
            )
        var call: Call<Result>? = null

        call = if (query == "") {
            request.getAllMovies(totalItemCount)
        } else {
            request.getMoviesByTitle(totalItemCount, query)
        }

        return call
    }
}