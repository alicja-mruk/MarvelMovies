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
    private var isAdded: Boolean = false
    var databaseState = MutableLiveData<AddedToDatabaseState>()

    fun checkIfMovieIsInDatabase(movie: Movie) {

        if (userId != null) {
            val docRef = db.collection("favourites").document(userId)
                .collection("movies").document(movie.id.toString())

            docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        addOrRemoveMovieFromDatabase(movie)
                    }
                }
                .addOnFailureListener {
                    databaseState.postValue(AddedToDatabaseState.FAILURE)
                }

        }
    }

    private fun addToDatabase(movie: Movie) {

        val photoUrl = "${movie.thumbnail.path}.${movie.thumbnail.extension}"

        val movieItem = hashMapOf(
            "title" to movie.title,
            "photoUrl" to photoUrl
        )

        if (userId == null) return

        db.collection("favourites").document(userId)
            .collection("movies").document(movie.id.toString())
            .set(movieItem)
            .addOnSuccessListener {
                databaseState.postValue(AddedToDatabaseState.ADDED_SUCCESS)
            }
            .addOnFailureListener {
                databaseState.postValue(AddedToDatabaseState.FAILURE)
            }
    }

    private fun removeFromDatabase(movie: Movie) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()

        if (userId == null) return

        val docRef = db.collection("favourites").document(userId)
            .collection("movies")
            .document(movie.id.toString())

        docRef.delete()
            .addOnCompleteListener {
                databaseState.postValue(AddedToDatabaseState.REMOVED_SUCCESS)
            }
            .addOnFailureListener {
                databaseState.postValue(AddedToDatabaseState.FAILURE)
            }
    }

    private fun addOrRemoveMovieFromDatabase(movie: Movie) {
        val docRef = db.collection("favourites").document(userId!!).collection("movies")
            .document(movie.id.toString())

        docRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document: DocumentSnapshot = task.result as DocumentSnapshot
                if (document.exists()) {
                    Log.d(TAG, "Document exists!")
                    removeFromDatabase(movie)
                } else {
                    Log.d(TAG, "Document does not exist!")
                    addToDatabase(movie)
                }
            } else {
                Log.d(TAG, "Failed with: ", task.exception)
            }
        }

    }

    val TAG = "getdatabase"
}