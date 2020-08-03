package com.moodup.movies.ui.authentication


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.movies.R
import com.example.movies.databinding.FragmentLoginBinding
import com.moodup.movies.state.AuthLoginState
import com.moodup.movies.ui.MainActivity
import com.moodup.movies.viewmodel.authentication.AuthenticationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel: AuthenticationViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setOnClickListeners()
        observeLiveData()

    }

    private fun setOnClickListeners() {
        binding.loginBtn.setOnClickListener {
            val email = binding.emailLoginText.text.toString()
            val password = binding.passwordLoginText.text.toString()
            viewModel.login(email, password)
        }

        binding.forgotPasswordBtn.setOnClickListener {
            findNavController().navigate(R.id.action_authNavFragment_to_forgotPasswordFragment)
        }

        binding.loginSignupBtn.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }
        binding.loginWithPhoneBtn.setOnClickListener {
            findNavController().navigate(R.id.action_authNavFragment_to_loginWithPhoneFragment)
        }
    }

    private fun observeLiveData() {
        viewModel.authenticationLoginState.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                AuthLoginState.EMPTY_EMAIL_OR_PASSWORD_FIELD -> {
                    makeToast(context?.resources?.getString(R.string.enter_correct_data))
                }
                AuthLoginState.ON_LOGIN_SUCCESS -> {
                    onLoginSuccess()
                }
                AuthLoginState.ON_LOGIN_FAILURE -> {
                    makeToast(context?.resources?.getString(R.string.login_failure))
                }
                else -> {

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

        startActivity(Intent(activity, MainActivity::class.java))

    }

    private fun makeToast(message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

}
