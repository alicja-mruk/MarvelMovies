package com.moodup.movies.repository.firebase

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.moodup.movies.state.AuthLoginState

class FirebaseAuthLoginHelper() {

    var authStateLoginLiveData = MutableLiveData<AuthLoginState>()

    fun performLogin(email : String, password:String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                authStateLoginLiveData.postValue(AuthLoginState.ON_LOGIN_SUCCESS)
            }

            .addOnFailureListener {
                authStateLoginLiveData.postValue(AuthLoginState.ON_LOGIN_FAILURE)
            }
    }

}