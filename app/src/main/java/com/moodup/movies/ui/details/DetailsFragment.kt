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
companion object{
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

    private fun setDataIntoFields(movie: Movie){
        movie_title.text = movie.title
        movie_description.text = movie.description
        movie_page_count.text = movie.pageCount.toString()
        movie_format.text = movie.format

         Glide.with(this)
                .load("${movie.thumbnail.path}.${movie.thumbnail.extension}")
                .into(movie_picture_details)
    }


}