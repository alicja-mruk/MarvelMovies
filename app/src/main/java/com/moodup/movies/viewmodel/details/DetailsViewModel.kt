package com.moodup.movies.viewmodel.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.moodup.movies.model.Movie
import com.moodup.movies.model.Thumbnail
import com.moodup.movies.state.AddedToDatabaseState


class DetailsViewModel : ViewModel() {

    private val userId = FirebaseAuth.getInstance().currentUser?.uid
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    var databaseState = MutableLiveData<AddedToDatabaseState>()
    var isMoviePresent = MutableLiveData<Boolean>()
    var movie: Movie? = null


    fun checkIfMovieIsInDatabase() {

        userId?.let {
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

    fun addToDatabase() {

        val movieItem = hashMapOf(
            "id" to movie?.id,
            "thumbnail" to movie?.thumbnail,
            "title" to movie?.title,
            "description" to movie?.description,
            "format" to movie?.format,
            "pageCount" to movie?.pageCount
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

    fun removeFromDatabase() {
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

    fun addOrRemoveMovieFromDatabase() {
        val docRef = userId?.let {
            db.collection("favourites").document(it).collection("movies")
                .document(movie?.id.toString())
        }

        docRef?.get()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                if (isDocumentExistsInDatabase(task)) {
                    removeFromDatabase()
                } else {
                    addToDatabase()
                }
            } else {
                databaseState.postValue(AddedToDatabaseState.FAILURE)
            }
        }

    }

    private fun isDocumentExistsInDatabase(task: Task<DocumentSnapshot>): Boolean {
        val document: DocumentSnapshot = task.result as DocumentSnapshot
        return document.exists()
    }

    fun isMovieInDataBase() {
        val docRef = userId?.let {
            db.collection("favourites").document(it).collection("movies")
                .document(movie?.id.toString())
        }
        docRef?.get()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                if (isDocumentExistsInDatabase(task)) {
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