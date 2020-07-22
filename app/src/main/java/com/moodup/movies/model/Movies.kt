package com.moodup.movies.model

import com.google.gson.annotations.SerializedName

data class Movies(
    @SerializedName("results")
    val moviesList: List<Movie>
)