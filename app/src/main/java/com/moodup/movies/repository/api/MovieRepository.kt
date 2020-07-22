package com.moodup.movies.repository.api


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.moodup.movies.model.Movie
import com.moodup.movies.model.Result
import retrofit2.Call
import retrofit2.Callback

class MovieRepository {
    private var moviesResponseLiveData = MutableLiveData<List<Movie>>()
    private var moviesFilteredResponseLiveData = MutableLiveData<List<Movie>>()

    init {
        getMovies()
    }

    private fun getMovies() {
        val request = ServiceBuilder.buildService(MoviesService::class.java)
        val call = request.getMovies()

        call.enqueue(object : Callback<Result> {
            override fun onResponse(
                call: Call<Result>,
                response: retrofit2.Response<Result>
            ) {
                if (response.isSuccessful) {
                   moviesResponseLiveData.postValue(response.body()?.movies?.moviesList)
                }
            }

            override fun onFailure(call: Call<Result>, t: Throwable) {
                moviesResponseLiveData.postValue(null)
            }

        })

    }

     fun getFilteredMovies(query:String){
        val request = ServiceBuilder.buildService(MoviesService::class.java)
        val call = request.getMoviesByTitle(query)

        call.enqueue(object : Callback<Result> {
            override fun onResponse(
                call: Call<Result>,
                response: retrofit2.Response<Result>
            ) {
                if (response.isSuccessful) {
                    moviesFilteredResponseLiveData.postValue(response.body()?.movies?.moviesList)
                }
            }

            override fun onFailure(call: Call<Result>, t: Throwable) {
                moviesFilteredResponseLiveData.postValue(null)
            }

        })
    }

    fun getMoviesLiveData(): LiveData<List<Movie>> {
        return moviesResponseLiveData
    }
    fun getFilteredMoviesLiveData() :LiveData<List<Movie>> {
        return moviesFilteredResponseLiveData
    }

}