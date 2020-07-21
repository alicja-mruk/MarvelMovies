package com.moodup.movies.repository.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap


interface MoviesService {
    @GET("v1/public/comics/?ts=1")
    fun getMovies(@QueryMap options :Map<String, String>): Call<Movies>
}