package com.moodup.movies.viewmodel.authentication

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.moodup.movies.repository.firebase.FirebaseAuthLoginHelper
import com.moodup.movies.repository.firebase.FirebaseAuthRegisterHelper
import com.moodup.movies.state.AuthLoginState
import com.moodup.movies.state.AuthRegisterState
import com.moodup.movies.state.LogoutState
import com.moodup.movies.state.ResetPasswordState
import org.koin.java.KoinJavaComponent.inject


class AuthenticationViewModel : ViewModel() {
    var authenticationRegisterState = MutableLiveData<AuthRegisterState>()
    var authenticationLoginState = MutableLiveData<AuthLoginState>()
    val resetPasswordState = MutableLiveData<ResetPasswordState>()
    val logoutState = MutableLiveData<LogoutState>()
    val firebaseLoginHelper by inject(FirebaseAuthLoginHelper::class.java)
    val firebaseRegisterHelper by inject (FirebaseAuthRegisterHelper::class.java)


    val observerLoginState = Observer<AuthLoginState> { loginState ->
        authenticationLoginState.postValue(loginState)
    }

    val observerRegisterState = Observer<AuthRegisterState> { registerState ->
        authenticationRegisterState.postValue(registerState)
    }


    init {
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

    fun resetPassword(email: String) {
        if (isEmailEmpty(email)) {
            resetPasswordState.postValue(ResetPasswordState.EMPTY_EMAIL)
        } else {
            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        resetPasswordState.postValue(ResetPasswordState.RESET_SUCCESS)
                    } else {
                        resetPasswordState.postValue(ResetPasswordState.RESET_FAILURE)
                    }
                }
        }

    }

    fun logout() {
        FirebaseAuth.getInstance().signOut()
        logoutState.postValue(LogoutState.LOGOUT_SUCCESS)
    }

    fun isEmailOrPasswordEmpty(email: String, password: String): Boolean {
        return email.isEmpty() || password.isEmpty()
    }

    private fun isEmailEmpty(email: String): Boolean {
        return email.isEmpty()
    }
}