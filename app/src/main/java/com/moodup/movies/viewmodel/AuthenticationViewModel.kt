package com.moodup.movies.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.moodup.movies.repository.delegators.StartActivityHelper
import com.moodup.movies.repository.firebase.FirebaseAuthLoginHelper
import com.moodup.movies.repository.firebase.FirebaseAuthRegisterHelper
import com.moodup.movies.state.AuthLoginState
import com.moodup.movies.state.AuthRegisterState
import com.moodup.movies.ui.MainActivity


class AuthenticationViewModel : ViewModel() {
    var authenticationRegisterState = MutableLiveData<AuthRegisterState>()
    var authenticationLoginState = MutableLiveData<AuthLoginState>()
    val firebaseLoginHelper = FirebaseAuthLoginHelper()
    val firebaseRegisterHelper = FirebaseAuthRegisterHelper()


    val observerLoginState = Observer<AuthLoginState> { loginState ->
        authenticationLoginState.postValue(loginState)
    }

    val observerRegisterState = Observer<AuthRegisterState> { registerState ->
        authenticationRegisterState.postValue(registerState)
    }


    init {
        checkIfUserLoggedIn()
        firebaseLoginHelper.authStateLoginLiveData.observeForever(observerLoginState)
        firebaseRegisterHelper.authStateRegisterLiveData.observeForever(observerRegisterState)
    }

    override fun onCleared() {
        super.onCleared()
        firebaseLoginHelper.authStateLoginLiveData.removeObserver(observerLoginState)
        firebaseRegisterHelper.authStateRegisterLiveData.removeObserver(observerRegisterState)
    }


    fun login(email: String, password: String) {

        if (!isEmailOrPasswordEmpty(email, password)) {
            firebaseLoginHelper.performLogin(email, password)
        } else {
            authenticationLoginState.postValue(AuthLoginState.EMPTY_EMAIL_OR_PASSWORD_FIELD)
        }

    }

    fun register(email: String, password: String) {

        if (!isEmailOrPasswordEmpty(email, password)) {
            firebaseRegisterHelper.performRegister(email, password)

        } else {
            authenticationRegisterState.postValue(AuthRegisterState.EMPTY_EMAIL_OR_PASSWORD_FIELD)
        }

    }

    fun startMainActivity(context: Context) {
        StartActivityHelper(context, MainActivity::class.java).startActivityWithClearTaskFlag()
    }

    private fun checkIfUserLoggedIn() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            authenticationLoginState.postValue(AuthLoginState.ON_ALREADY_LOGGED_IN)
        }
    }

    private fun isEmailOrPasswordEmpty(email: String, password: String): Boolean {
        return email.isEmpty() || password.isEmpty()
    }

}