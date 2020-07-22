package com.moodup.movies.model

import android.os.Parcelable
import java.io.Serializable

data class Movie(
    val id: Int,
    val thumbnail: Thumbnail,
    val title: String,
    val description: String,
    val format: String,
    val pageCount: Int
) : Serializable