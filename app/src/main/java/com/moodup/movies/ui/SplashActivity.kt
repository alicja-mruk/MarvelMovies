package com.moodup.movies.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.movies.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.moodup.movies.ui.authentication.AuthenticationActivity

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