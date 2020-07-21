package com.moodup.movies.repository.api

import com.moodup.movies.model.Movie
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap


interface MoviesService {
    @GET("v1/public/comics?ts=1")
    fun getMovies(@QueryMap options :Map<String, String>): Call<List<Movie>>
}