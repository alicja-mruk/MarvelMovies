package com.moodup.movies.model

data class Movie(
    val id: Int,
    val thumbnail: Thumbnail,
    val title: String,
    val creators: Creators,
    val description: String,
    val format: String,
    val pageCount: Int,
    val prices: List<Price>
)