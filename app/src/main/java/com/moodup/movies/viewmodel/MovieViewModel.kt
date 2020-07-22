package com.moodup.movies.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moodup.movies.model.Movie
import com.moodup.movies.model.Result
import com.moodup.movies.repository.api.MovieRepository
import retrofit2.Call
import retrofit2.Callback

class MovieViewModel : ViewModel() {
    private var movieRepository: MovieRepository = MovieRepository()

    var allMovieLiveData = MutableLiveData<List<Movie>>()

    fun getMovies(query : String) {
            movieRepository.getAllMovies(query).enqueue(object : Callback<Result> {
                override fun onResponse(
                    call: Call<Result>,
                    response: retrofit2.Response<Result>
                ) {
                    if (response.isSuccessful) {
                        allMovieLiveData.postValue(response.body()?.movies?.moviesList)
                    }
                }

                override fun onFailure(call: Call<Result>, t: Throwable) {
                    allMovieLiveData.postValue(null)
                }
            })
        }

}