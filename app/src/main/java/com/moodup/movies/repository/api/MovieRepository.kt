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

    init {
        makeRequest()
    }

    private fun makeRequest() {
        val request = ServiceBuilder.buildService(MoviesService::class.java)
        val call = request.getMovies(setAndReturnDataQueries())

        call.enqueue(object : Callback<Result> {
            override fun onResponse(
                call: Call<Result>,
                response: retrofit2.Response<Result>
            ) {
                if (response.isSuccessful) {
                   Log.d("Response JSON", response.body().toString())
                   moviesResponseLiveData.postValue(response.body()?.movies?.moviesList)
                }
            }

            override fun onFailure(call: Call<Result>, t: Throwable) {
                moviesResponseLiveData.postValue(null)
            }

        })
    }

    fun getMoviesLiveData(): LiveData<List<Movie>> {
        return moviesResponseLiveData
    }

    private fun setAndReturnDataQueries(): MutableMap<String, String> {
        val data: MutableMap<String, String> = HashMap()
        data["limit"]= limit
        return data
    }

    companion object {
        private const val limit = "50"
    }


}