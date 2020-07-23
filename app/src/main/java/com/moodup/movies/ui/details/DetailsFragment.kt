package com.moodup.movies.ui.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.movies.R
import com.moodup.movies.model.Movie
import kotlinx.android.synthetic.main.fragment_details.*
import kotlinx.android.synthetic.main.fragment_details.movie_title
import kotlinx.android.synthetic.main.movie_row.*

class DetailsFragment : Fragment() {
    companion object {
        const val MOVIE_KEY = "MOVIE_KEY"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val movie = arguments?.getSerializable(MOVIE_KEY)
        setDataIntoFields(movie as Movie)
    }

    private fun setDataIntoFields(movie: Movie) {
        movie_title.text = if(movie.title == null || movie.title.isEmpty()) getString(R.string.no_title) else  movie.title
        movie_description.text = if(movie.description ==null || movie.description.isEmpty()) getString(R.string.no_description) else movie.description
        movie_page_count.text =  if(movie.pageCount == null || movie.pageCount.toString().isEmpty()) getString(R.string.no_page_count) else movie.pageCount.toString()
        movie_format.text = if(movie.format == null || movie.format.isEmpty()) getString(R.string.no_format) else movie.format


        Glide.with(this)
            .load("${movie.thumbnail.path}.${movie.thumbnail.extension}")
            .into(movie_picture_details)
    }


}