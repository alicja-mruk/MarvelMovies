package com.moodup.movies.model

data class Movie (
    val id: Int,
    val title: String,
    val modified: String,
    val pageCount: Int,
    val description : String,
    val creators: List<Creator>,
    val prices: List<Price>,
    val thumbnail: Thumbnail
)


