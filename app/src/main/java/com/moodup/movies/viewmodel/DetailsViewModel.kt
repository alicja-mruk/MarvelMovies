package com.moodup.movies.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.moodup.movies.model.Movie
import com.moodup.movies.state.AddedToDatabaseState


class DetailsViewModel : ViewModel() {

    private val userId = FirebaseAuth.getInstance().currentUser?.uid
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    var databaseState = MutableLiveData<AddedToDatabaseState>()
    var isMoviePresent = MutableLiveData<Boolean>()
    var movie: Movie? = null


    fun checkIfMovieIsInDatabase() {

        if (userId != null) {
            val docRef = db.collection("favourites").document(userId)
                .collection("movies").document(movie?.id.toString())

            docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        addOrRemoveMovieFromDatabase()
                    }
                }
                .addOnFailureListener {
                    databaseState.postValue(AddedToDatabaseState.FAILURE)
                }

        }
    }

    private fun addToDatabase() {

        val photoUrl = "${movie?.thumbnail?.path}.${movie?.thumbnail?.extension}"

        val movieItem = hashMapOf(
            "title" to movie?.title,
            "photoUrl" to photoUrl
        )

        if (userId == null) return

        db.collection("favourites").document(userId)
            .collection("movies").document(movie?.id.toString())
            .set(movieItem)
            .addOnSuccessListener {
                databaseState.postValue(AddedToDatabaseState.ADDED_SUCCESS)
            }
            .addOnFailureListener {
                databaseState.postValue(AddedToDatabaseState.FAILURE)
            }
    }

    private fun removeFromDatabase() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()

        if (userId == null) return

        val docRef = db.collection("favourites").document(userId)
            .collection("movies")
            .document(movie?.id.toString())

        docRef.delete()
            .addOnCompleteListener {
                databaseState.postValue(AddedToDatabaseState.REMOVED_SUCCESS)
            }
            .addOnFailureListener {
                databaseState.postValue(AddedToDatabaseState.FAILURE)
            }
    }

    private fun addOrRemoveMovieFromDatabase() {
        val docRef = db.collection("favourites").document(userId!!).collection("movies")
            .document(movie?.id.toString())

        docRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document: DocumentSnapshot = task.result as DocumentSnapshot
                if (document.exists()) {
                    removeFromDatabase()
                } else {
                    addToDatabase()
                }
            } else {
                databaseState.postValue(AddedToDatabaseState.FAILURE)
            }
        }

    }

    fun isMovieInDataBase() {
        val docRef = db.collection("favourites").document(userId!!).collection("movies")
            .document(movie?.id.toString())
        docRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document: DocumentSnapshot = task.result as DocumentSnapshot
                if (document.exists()) {
                    isMoviePresent.postValue(true)
                } else {
                    isMoviePresent.postValue(false)
                }

            } else {
                databaseState.postValue(AddedToDatabaseState.FAILURE)
            }
        }
    }
}