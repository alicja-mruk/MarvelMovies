package com.moodup.movies.ui.configuration

import android.content.Intent
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
import com.moodup.movies.ui.authentication.AuthenticationActivity
import com.moodup.movies.viewmodel.authentication.AuthenticationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
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

        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel.logoutState.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                LogoutState.LOGOUT_SUCCESS -> {
                    logoutSuccess()
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

        startActivity(Intent(activity, AuthenticationActivity::class.java))
    }
}