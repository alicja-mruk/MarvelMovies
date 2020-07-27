package com.moodup.movies.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moodup.movies.model.Movie
import com.moodup.movies.state.AddedItemState

class FavouritesViewModel : ViewModel() {
    var favouritesLiveData = MutableLiveData<List<Movie>>()
    var addedState = MutableLiveData<AddedItemState>()

    private var favouritesList = ArrayList<Movie>()

    init{
        favouritesLiveData.value  = favouritesList
    }

    fun addOrRemoveFromFavouritesList(movie : Movie){
        if (!favouritesList.contains(movie)) {
            addToFavourites(movie)
            favouritesLiveData.value  = favouritesList

        } else {
            removeFromFavourites(movie)
            favouritesLiveData.value  = favouritesList
        }
    }

    private fun addToFavourites(movie : Movie){
        favouritesList.add(movie)
        addedState.postValue(AddedItemState.ON_ADDED)
    }

    private fun removeFromFavourites(movie : Movie){
        favouritesList.remove(movie)
        addedState.postValue(AddedItemState.ON_REMOVED)
    }

}