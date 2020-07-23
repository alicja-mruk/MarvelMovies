package com.moodup.movies.repository.api


import com.moodup.movies.model.Result
import retrofit2.Call

class MovieRepository {

    fun getAllMovies(query : String): Call<Result> {
        val request = ServiceBuilder.buildService(MoviesService::class.java)
        var call : Call<Result>? = null

        call = if(query ==""){
            request.getAllMovies()
        }else{
            request.getMoviesByTitle(query)
        }

        return call
    }
}