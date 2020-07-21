package com.moodup.movies.repository.api

import retrofit2.Call
import retrofit2.http.GET




interface MoviesService {
    @GET("public/comics/")
    fun getMovies() : Call<List<Movie>>
    //https://gateway.marvel.com/v1/public/comics?ts=1&apikey=3d3ce5daa8ec0f7c17afc52bb68f15f7&hash=a45bdb0bf57b06e72ad4c2c5854e2843&limit=25&offset=0&orderBy=-onsaleDate
}