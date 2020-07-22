package com.moodup.movies.model

import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("data")
    val movies: Movies
)