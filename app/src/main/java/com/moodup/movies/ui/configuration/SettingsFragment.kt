package com.moodup.movies.ui.configuration

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.movies.R
import com.example.movies.databinding.FragmentSettingsBinding
import com.moodup.movies.state.LogoutState
import com.moodup.movies.themehelper.ThemeHelper
import com.moodup.movies.ui.authentication.AuthenticationActivity
import com.moodup.movies.viewmodel.authentication.AuthenticationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var sharedPref: SharedPreferences
    private val viewModel: AuthenticationViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.viewModel = viewModel
        setSwitchState()
        observeLiveData()
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        binding.darkmodeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                applyDarkTheme()
            } else {
                applyLightTheme()
            }
        }
    }

    private fun setSwitchState() {
        sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)!!
        val theme = sharedPref.getString(getString(R.string.darkmode), null)
        binding.darkmodeSwitch.isChecked = theme.equals("dark")
    }

    private fun applyLightTheme() {
        ThemeHelper.applyTheme("light")
        with(sharedPref.edit()) {
            this?.putString(getString(R.string.darkmode), "light")
            this?.commit()
        }
    }

    private fun applyDarkTheme() {
        ThemeHelper.applyTheme("dark")
        with(sharedPref.edit()) {
            this?.putString(getString(R.string.darkmode), "dark")
            this?.commit()
        }
    }

    private fun observeLiveData() {
        viewModel.logoutState.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                LogoutState.LOGOUT_SUCCESS -> {
                    logoutSuccess()
                }
                else -> {

                }
            }
        })
    }

    private fun logoutSuccess() {
        Toast.makeText(
            context,
            context?.resources?.getString(R.string.logout_success),
            Toast.LENGTH_SHORT
        ).show()
        startAuthActivity()
    }

    private fun startAuthActivity() {

        val i = Intent(context, AuthenticationActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        i.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
        startActivity(i)
        requireActivity().finish()
    }
}