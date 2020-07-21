package com.moodup.movies.repository.api


import com.moodup.movies.model.Movies
import retrofit2.Call
import retrofit2.Callback

object Request {
    fun makeRequest() {

        val request = ServiceBuilder.buildService(MoviesService::class.java)
        val call = request.getMovies(setAndReturnDataQueries())

        call.enqueue(object : Callback<Movies> {

            override fun onResponse(call: Call<Movies>, response: retrofit2.Response<Movies>) {
                if (response.isSuccessful) {

                }
            }

            override fun onFailure(call: Call<Movies>, t: Throwable) {

            }

        })
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


    private const val api_key = "3d3ce5daa8ec0f7c17afc52bb68f15f7&"
    private const val hash = "a45bdb0bf57b06e72ad4c2c5854e2843"
    private const val limit = "50"
    private const val offset = "0"
    private const val orderBy = "-onsaleDate"


}