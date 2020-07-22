package com.moodup.movies.repository.api


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.moodup.movies.model.Movie
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

        call.enqueue(object : Callback<Movie> {
            override fun onResponse(
                call: Call<Movie>,
                response: retrofit2.Response<Movie>
            ) {
                if (response.isSuccessful) {
                   Log.d("Response", response.body().toString())
//                   moviesResponseLiveData.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<Movie>, t: Throwable) {
//                moviesResponseLiveData.postValue(null)
            }

        })
    }

    fun getMoviesLiveData(): LiveData<List<Movie>> {
        return moviesResponseLiveData
    }

    private fun setAndReturnDataQueries(): MutableMap<String, String> {
        val data: MutableMap<String, String> = HashMap()
        data["limit"]= limit
//        data["limit"] = limit
        return data
    }

    companion object {
        private const val limit = "50"
    }


}