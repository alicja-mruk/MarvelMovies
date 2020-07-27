package com.moodup.movies.ui.authentication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.movies.R

class AuthenticationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)
        supportActionBar?.hide()

        val appBarConfiguration = AppBarConfiguration(setOf(R.id.navFragment))
        val authNavController = findNavController(R.id.authNavFragment)
        setupActionBarWithNavController(authNavController, appBarConfiguration)
    }


    override fun onSupportNavigateUp(): Boolean {
        if (!findNavController(R.id.navFragment).navigateUp()) finish()
        return true
    }
}