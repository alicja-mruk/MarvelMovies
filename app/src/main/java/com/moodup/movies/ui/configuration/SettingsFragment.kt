package com.moodup.movies.ui.configuration

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.movies.R
import com.moodup.movies.state.LogoutState
import com.moodup.movies.ui.authentication.AuthenticationActivity
import com.moodup.movies.viewmodel.authentication.AuthenticationViewModel
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {
    private lateinit var viewModel: AuthenticationViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.let {
            viewModel = ViewModelProvider(it).get(AuthenticationViewModel::class.java)
        }

        setOnClickListeners()
        observeLiveData()
    }

    private fun setOnClickListeners() {
        logout_btn.setOnClickListener {
            viewModel.logout()
        }
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