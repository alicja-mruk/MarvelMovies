package com.moodup.movies.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.moodup.movies.model.Movie
import com.moodup.movies.repository.api.MovieRepository

class MovieViewModel : ViewModel() {
    private var movieRepository: MovieRepository = MovieRepository()
    private var moviesResponseLiveData = movieRepository.getMoviesLiveData()

    fun getMoviesResponseLiveData() : LiveData<List<Movie>>{
        return moviesResponseLiveData
    }
}