package com.moodup.movies.ui

import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.movies.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.moodup.movies.ui.favourites.FavouritesFragment
import com.moodup.movies.ui.home.HomeFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val appBarConfiguration = AppBarConfiguration(setOf(R.id.navFragment))
        val navController = findNavController(R.id.navFragment)
        setupActionBarWithNavController(navController, appBarConfiguration)

    }



    override fun onSupportNavigateUp(): Boolean {
        if (!findNavController(R.id.navFragment).navigateUp()) finish()
        return true
    }


}