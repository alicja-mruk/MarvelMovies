package com.moodup.movies.ui.authentication


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.movies.R
import com.moodup.movies.repository.delegators.StartActivityHelper
import com.moodup.movies.state.AuthLoginState
import com.moodup.movies.ui.MainActivity
import com.moodup.movies.viewmodel.AuthenticationViewModel
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : Fragment() {
    private var viewModel: AuthenticationViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_login, container, false)
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
        login_btn.setOnClickListener {
            val email = email_login_text.text.toString()
            val password = password_login_text.text.toString()
            viewModel?.login(email, password)
        }

        forgot_password_btn.setOnClickListener {
            //todo : nav controller to change password fragment
        }
    }
    private fun observeLiveData(){
        viewModel?.authenticationLoginState?.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                AuthLoginState.EMPTY_EMAIL_OR_PASSWORD_FIELD->{
                    onEmptyEmailOrPasswordField()
                }
                AuthLoginState.ON_LOGIN_SUCCESS->{
                    onLoginSuccess()
                }
                AuthLoginState.ON_LOGIN_FAILURE->{
                    onLoginFailure()
                }

                AuthLoginState.ON_ALREADY_LOGGED_IN->{
                    context?.let { viewModel?.startMainActivity(it) }
                }

            }
        })
    }

    private fun onLoginSuccess() {

        Toast.makeText(
            context,
            context?.resources?.getString(R.string.login_success),
            Toast.LENGTH_LONG
        ).show()

        context?.let {
            StartActivityHelper(
                it,
                MainActivity::class.java
            ).startActivityWithClearTaskFlag()
        }
    }

    private fun onLoginFailure() {
        Toast.makeText(
            context,
            context?.resources?.getString(R.string.login_failure),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun onEmptyEmailOrPasswordField() {
        Toast.makeText(
            context,
            context?.resources?.getString(R.string.enter_correct_data),
            Toast.LENGTH_SHORT
        ).show()
    }
}
