package com.moodup.movies.repository.api


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

        call.enqueue(object : Callback<List<Movie>> {
            override fun onResponse(call: Call<List<Movie>>, response: retrofit2.Response<List<Movie>>) {
                if (response.isSuccessful) {
                    moviesResponseLiveData.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<List<Movie>>, t: Throwable) {
                moviesResponseLiveData.postValue(null)
            }

        })
    }

    fun getMoviesLiveData(): LiveData<List<Movie>> {
        return moviesResponseLiveData
    }

    private fun setAndReturnDataQueries(): MutableMap<String, String> {
        val data: MutableMap<String, String> = HashMap()
        data["api_key"] = api_key
        data["hash"] = hash
        data["limit"] = limit
        data["offset"] = offset
        data["orderBy"] = orderBy
        return data
    }


    private val api_key = "3d3ce5daa8ec0f7c17afc52bb68f15f7&"
    private val hash = "a45bdb0bf57b06e72ad4c2c5854e2843"
    private val limit = "50"
    private val offset = "0"
    private val orderBy = "-onsaleDate"


}