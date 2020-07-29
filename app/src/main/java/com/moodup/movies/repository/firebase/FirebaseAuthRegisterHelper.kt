package com.moodup.movies.repository.firebase

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.moodup.movies.state.AuthRegisterState


class FirebaseAuthRegisterHelper(){

    var authStateRegisterLiveData = MutableLiveData<AuthRegisterState>()

    fun performRegister(email:String,password:String) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                authStateRegisterLiveData.postValue(AuthRegisterState.ON_REGISTER_SUCCESS)
            }

            .addOnFailureListener {
                authStateRegisterLiveData.postValue(AuthRegisterState.ON_REGISTER_FAILURE)
            }
    }


}