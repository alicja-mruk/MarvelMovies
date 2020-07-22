package com.moodup.movies.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moodup.movies.model.Movie
import com.moodup.movies.repository.api.MovieRepository

class MovieViewModel : ViewModel() {
    private var movieRepository: MovieRepository = MovieRepository()
    private var moviesResponseLiveData = movieRepository.getMoviesLiveData()
    private var moviesFilteredResponseLiveData = movieRepository.getFilteredMoviesLiveData()

    fun getMoviesResponseLiveData() : LiveData<List<Movie>>{
        return moviesResponseLiveData
    }

    fun getFilteredMoviesResponseLiveData(query: String): LiveData<List<Movie>>{
        movieRepository.getFilteredMovies(query)
        return moviesFilteredResponseLiveData
    }
}