package com.moodup.movies.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.movies.R
import com.example.movies.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.getDefaultNightMode())
        super.onCreate(savedInstanceState)

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        binding = ActivityMainBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)

        val appBarConfiguration = AppBarConfiguration(setOf(R.id.homeFragment))
        val navController = findNavController(R.id.homeFragment)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.bottomNavMenu.setupWithNavController(navController)


    }

    override fun onSupportNavigateUp(): Boolean {
        if (!findNavController(R.id.homeFragment).navigateUp()) finish()
        return true
    }

    override fun onPause() {
        super.onPause()

        if (isFinishing) {
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }

}