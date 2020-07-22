package com.moodup.movies.repository.api


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.moodup.movies.model.Movie
import com.moodup.movies.model.Result
import retrofit2.Call
import retrofit2.Callback

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