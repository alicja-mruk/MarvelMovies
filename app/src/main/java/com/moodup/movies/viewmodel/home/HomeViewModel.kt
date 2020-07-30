package com.moodup.movies.viewmodel.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moodup.movies.model.Movie
import com.moodup.movies.model.Result
import com.moodup.movies.repository.api.call.MovieRepository
import com.moodup.movies.state.UIState
import retrofit2.Call
import retrofit2.Callback

class HomeViewModel (private val movieRepository: MovieRepository): ViewModel() {

    var isDataLoading: Boolean = false
    var totalResults: Int = 9999
    var movieLiveData = MutableLiveData<List<Movie>>(listOf())
    var UIstateLiveData = MutableLiveData<UIState>(UIState.INITIALIZED)


    fun getMovies(currentPageNumber: Int, query: String) {
        movieRepository.getAllMovies(currentPageNumber, query)
            .enqueue(object : Callback<Result> {
                override fun onResponse(
                    call: Call<Result>,
                    response: retrofit2.Response<Result>
                ) {
                    if (response.isSuccessful) {
                        totalResults = response.body()?.movies!!.total
                        response.body()?.movies?.moviesList?.let {
                            if (it.isEmpty() && currentPageNumber == 0) {
                                UIstateLiveData.postValue(UIState.ON_EMPTY_RESULTS)
                            } else {
                                val movieList = response.body()?.movies?.moviesList
                                updateMovieList(movieList)
                                UIstateLiveData.postValue(UIState.ON_RESULT)
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<Result>, t: Throwable) {
                    movieLiveData.postValue(null)
                    UIstateLiveData.postValue(UIState.ON_ERROR)
                }
            })

    }

    fun updateMovieList(newMovieList: List<Movie>?) {
        newMovieList?.let { movies ->
            movieLiveData.value?.let { currentMovieList ->
                val updatedMovieList = ArrayList(currentMovieList)
                updatedMovieList.addAll(movies)
                movieLiveData.postValue(updatedMovieList)
            }
        }
    }

    fun checkIfThereIsScrollingPossible(totalItemCount: Int): Boolean {
        return (totalResults - 1 > totalItemCount)
    }

    fun onSearchQueryChanged(query: String) {
        movieLiveData.postValue(listOf())
        getMovies(0, query)
    }


}