package com.moodup.movies.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.movies.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.moodup.movies.repository.module.repositoryModule
import com.moodup.movies.repository.module.retrofitModule
import com.moodup.movies.ui.authentication.AuthenticationActivity
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import java.util.logging.Level

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        val user = FirebaseAuth.getInstance().currentUser
        routeToAppropriatePage(user)
        finish()
    }

    private fun routeToAppropriatePage(user : FirebaseUser?){
        when (user) {
            null -> startActivity(Intent(this, AuthenticationActivity::class.java))
            else -> {
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
    }
}