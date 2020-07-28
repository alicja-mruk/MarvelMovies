package com.moodup.movies.model


import java.io.Serializable

data class Movie(
    val id: Int?,
    val thumbnail: Thumbnail,
    val title: String?,
    val description: String?,
    val format: String?,
    val pageCount: Int?
) : Serializable{
    constructor() : this (0, Thumbnail("",""), "","","",0)
}