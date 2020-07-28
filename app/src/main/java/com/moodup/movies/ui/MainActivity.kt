package com.moodup.movies.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.movies.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)

        val appBarConfiguration = AppBarConfiguration(setOf(R.id.homeFragment))
        val navController = findNavController(R.id.homeFragment)
        setupActionBarWithNavController(navController, appBarConfiguration)

        bottom_nav_menu.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        if (!findNavController(R.id.homeFragment).navigateUp()) finish()
        return true
    }

}