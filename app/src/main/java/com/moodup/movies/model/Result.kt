package com.moodup.movies.model

data class Result(
    val id: Int,
    val characters: Characters,
    val creators: Creators,
    val description: String,
    val format: String,
    val pageCount: Int,
    val prices: List<Price>,
    val thumbnail: Thumbnail,
    val title: String
)