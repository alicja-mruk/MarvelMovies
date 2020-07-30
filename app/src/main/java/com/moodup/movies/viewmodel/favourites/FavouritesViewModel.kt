@file:Suppress("UNCHECKED_CAST")

package com.moodup.movies.viewmodel.favourites

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.moodup.movies.model.Movie
import com.moodup.movies.model.Movies
import com.moodup.movies.state.AddedToDatabaseState
import com.moodup.movies.state.FavouritesCallbackState

class FavouritesViewModel : ViewModel() {
    private val userId = FirebaseAuth.getInstance().currentUser?.uid
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val docRef = userId?.let {
        db.collection("favourites").document(it).collection("movies")
    }

    val favouritesMovies = MutableLiveData<List<Movie>>()
    val favouritesCallbackState = MutableLiveData<FavouritesCallbackState>(FavouritesCallbackState.INITIALIZED)

    fun getFavouriteMoviesFromDatabase() {
        docRef?.get()?.addOnSuccessListener { result ->
            val resultMovies: List<Movie> = result.toObjects(Movie::class.java)

            if (resultMovies.isNotEmpty()) {
                favouritesCallbackState.postValue(FavouritesCallbackState.ON_SUCCESS)
                favouritesMovies.postValue(resultMovies)

            } else {
                favouritesCallbackState.postValue(FavouritesCallbackState.EMPTY_LIST)
                favouritesMovies.postValue(emptyList())

            }

        }
            ?.addOnFailureListener {
                favouritesCallbackState.postValue(FavouritesCallbackState.ON_FAILURE)
                favouritesMovies.postValue(null)

            }

    }

    fun removeFromFavourite(movie: Movie) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()

        if (userId == null) return

        val dbRef = db.collection("favourites").document(userId)
            .collection("movies")
            .document(movie.id.toString())

        dbRef.delete()
            .addOnSuccessListener {
                favouritesCallbackState.postValue(FavouritesCallbackState.LOADING)
            }

            .addOnCompleteListener {
                getFavouriteMoviesFromDatabase()
                favouritesCallbackState.postValue(FavouritesCallbackState.ON_SUCCESS)
            }
            .addOnFailureListener {
                favouritesCallbackState.postValue(FavouritesCallbackState.ON_FAILURE)
            }

    }

}
