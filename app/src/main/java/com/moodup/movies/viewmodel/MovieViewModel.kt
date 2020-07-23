package com.moodup.movies.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moodup.movies.model.Movie
import com.moodup.movies.model.Result
import com.moodup.movies.repository.api.call.MovieRepository
import com.moodup.movies.state.UIState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.Query

class MovieViewModel : ViewModel() {
    private var movieRepository: MovieRepository = MovieRepository()

    var totalItemCount : Int = 0
    var isDataLoading:Boolean = false
    var movieLiveData = MutableLiveData<List<Movie>>()
    var UIstateLiveData = MutableLiveData<UIState>(UIState.INITIALIZED)

    fun getMovies(totalPageNumber: Int , query : String) {

            movieRepository.getAllMovies(totalPageNumber, query).enqueue(object : Callback<Result> {
                override fun onResponse(
                    call: Call<Result>,
                    response: retrofit2.Response<Result>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.movies?.moviesList?.let{
                            if(it.isEmpty()){
                                UIstateLiveData.postValue(UIState.ON_EMPTY_RESULTS)
                            }else{
                                movieLiveData.postValue(response.body()?.movies?.moviesList)
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

}