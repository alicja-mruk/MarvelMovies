package com.moodup.movies.repository.api

import com.moodup.movies.model.Result
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap


interface MoviesService {
    @GET("v1/public/comics?ts=1&apikey=3d3ce5daa8ec0f7c17afc52bb68f15f7&hash=a45bdb0bf57b06e72ad4c2c5854e2843&limit=50")
    fun getMovies(): Call<Result>

    @GET("v1/public/comics?ts=1&apikey=3d3ce5daa8ec0f7c17afc52bb68f15f7&hash=a45bdb0bf57b06e72ad4c2c5854e2843&limit=50")
    fun getMoviesByTitle(@Query("title") title : String ): Call<Result>

}