package com.moodup.movies.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.moodup.movies.model.Movie
import com.moodup.movies.state.AddedToDatabaseState
import com.moodup.movies.state.DatabaseCallback

class DetailsViewModel : ViewModel() {
    var databaseCallback = MutableLiveData<DatabaseCallback>()
    var addedToDatabaseState = MutableLiveData<AddedToDatabaseState>()

    fun checkIfMovieIsInDatabase(movie: Movie) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()

        if (userId != null) {
            val docRef = db.collection("favourites").document(userId).collection("movies")
                .document(movie.id.toString())
            docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        databaseCallback.postValue(DatabaseCallback.DOCUMENT_SUCCESS)
 //todo sprawdzi czy jest w bazie

                    } else {
                        databaseCallback.postValue(DatabaseCallback.NO_DOCUMENT)

                    }
                }
                .addOnFailureListener {
                    databaseCallback.postValue(DatabaseCallback.DOCUMENT_FAILURE)
                }

        }
    }

    fun addToDatabase(movie: Movie) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val photoUrl = "${movie.thumbnail.path}.${movie.thumbnail.extension}"

        val movieItem = hashMapOf(
            "title" to movie.title,
            "photoUrl" to photoUrl
        )

        if (userId == null) return

        db.collection("favourites").document(userId).collection("movies")
            .document(movie.id.toString())
            .set(movieItem)
            .addOnSuccessListener { addedToDatabaseState.postValue(AddedToDatabaseState.SUCCESS) }
            .addOnFailureListener { addedToDatabaseState.postValue(AddedToDatabaseState.FAILURE) }
    }

    fun removeFromDatabase(movie: Movie) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()

        if (userId == null) return

        val docRef = db.collection("favourites").document(userId)
        val deletes = hashMapOf<String, Any>(
            movie.id.toString() to FieldValue.delete()
        )
        docRef.update(deletes).addOnCompleteListener {

        }
    }

}