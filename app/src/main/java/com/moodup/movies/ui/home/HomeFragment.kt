package com.moodup.movies.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.movies.R
import com.moodup.movies.model.Movie
import com.moodup.movies.utils.adapter.MoviesAdapter
import com.moodup.movies.viewmodel.MovieViewModel
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {
    private var viewModel: MovieViewModel? = null
    private var adapter: MoviesAdapter? = null
    private var movies: List<Movie>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter = movies?.let { MoviesAdapter(it) }

        activity?.let {
            viewModel = ViewModelProvider(it).get(MovieViewModel::class.java)
        }

        viewModel?.getMoviesResponseLiveData()?.observe(viewLifecycleOwner, Observer {
//            movies = it as List<Movie>?
            movies_recycler_view.adapter = adapter
        })
    }
}