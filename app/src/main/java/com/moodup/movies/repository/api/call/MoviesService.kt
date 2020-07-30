package com.moodup.movies.repository.api.call

import com.example.movies.BuildConfig
import com.moodup.movies.model.Result
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query



interface MoviesService {
    @GET("v1/public/comics?ts=1&apikey=${BuildConfig.API_KEY}&hash=${BuildConfig.HASH}&limit=25")
    fun getAllMovies(
        @Query("offset") pageNumber :Int
    ): Call<Result>

    @GET("v1/public/comics?ts=1&apikey=${BuildConfig.API_KEY}&hash=${BuildConfig.HASH}&limit=25")
    fun getMoviesByTitle(
        @Query("offset") pageNumber :Int,
        @Query("title") title: String
    ): Call<Result>


}