package com.moodup.movies.viewmodel.authentication

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.moodup.movies.repository.firebase.FirebaseAuthLoginHelper
import com.moodup.movies.repository.firebase.FirebaseAuthRegisterHelper
import com.moodup.movies.state.*
import org.koin.java.KoinJavaComponent.inject
import java.util.concurrent.TimeUnit


class AuthenticationViewModel : ViewModel() {
    companion object {
        const val TAG = "PHONE_VERIFICATION"
    }

    val auth = FirebaseAuth.getInstance()
    var verificationId = MutableLiveData<String?>()
    var token = MutableLiveData<String?>()
    var codeMutableLiveData = MutableLiveData<String?>()

    var phoneAuthMutableLiveData = MutableLiveData<AuthPhone>(AuthPhone.INITIALIZED)
    var authenticationRegisterState = MutableLiveData<AuthRegisterState>()
    var authenticationLoginState = MutableLiveData<AuthLoginState>()
    val resetPasswordState = MutableLiveData<ResetPasswordState>()
    val logoutState = MutableLiveData<LogoutState>()
    val firebaseLoginHelper by inject(FirebaseAuthLoginHelper::class.java)
    val firebaseRegisterHelper by inject(FirebaseAuthRegisterHelper::class.java)


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

    fun loginWithPhone(prefix:String, phoneNumber: String) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            "$prefix$phoneNumber",
            60,
            TimeUnit.SECONDS,
            TaskExecutors.MAIN_THREAD,
            phoneAuthenticationCallback
        )
        phoneAuthMutableLiveData.postValue(AuthPhone.SMS_RECEIVED)

    }


    fun isEmailOrPasswordEmpty(email: String, password: String): Boolean {
        return email.isEmpty() || password.isEmpty()
    }

    private fun isEmailEmpty(email: String): Boolean {
        return email.isEmpty()
    }


    private val phoneAuthenticationCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
            val code = phoneAuthCredential.smsCode

            if (code!= null) {
                verifyVerificationCode(codeMutableLiveData.value!!)
            }

        }

        override fun onVerificationFailed(e: FirebaseException) {
            if (e is FirebaseAuthInvalidCredentialsException) {
                phoneAuthMutableLiveData.postValue(AuthPhone.INVALID_REQUEST)
            } else if (e is FirebaseTooManyRequestsException) {
                phoneAuthMutableLiveData.postValue(AuthPhone.TOO_MANY_REQUESTS)
            }
        }

        override fun onCodeSent(
            _verificationId: String,
            _token: PhoneAuthProvider.ForceResendingToken
        ) {
            super.onCodeSent(_verificationId, _token)

            verificationId.postValue(_verificationId)
            token.postValue(_token.toString())
        }

    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    phoneAuthMutableLiveData.postValue(AuthPhone.LOGIN_SUCCESS)

                } else {
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        phoneAuthMutableLiveData.postValue(AuthPhone.WRONG_CONFIRMATION_CODE)
                    }
                }
            }
    }

    fun verifyVerificationCode(code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId.value.toString(), code)
        signInWithPhoneAuthCredential(credential)
    }


}



