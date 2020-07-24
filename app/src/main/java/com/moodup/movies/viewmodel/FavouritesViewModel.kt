package com.moodup.movies.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moodup.movies.model.Movie

class FavouritesViewModel : ViewModel() {
    var favouritesLiveData = MutableLiveData<List<Movie>>()
}