package com.moodup.movies.model

import com.google.gson.annotations.SerializedName

data class Creators(
    @SerializedName("items")
    val details: List<Creator>
)