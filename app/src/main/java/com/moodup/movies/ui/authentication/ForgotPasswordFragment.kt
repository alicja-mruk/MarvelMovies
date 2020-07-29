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
import com.example.movies.databinding.FragmentForgotPasswordBinding
import com.example.movies.databinding.FragmentHomeBinding
import com.moodup.movies.state.ResetPasswordState
import com.moodup.movies.viewmodel.authentication.AuthenticationViewModel
import kotlinx.android.synthetic.main.fragment_forgot_password.*


class ForgotPasswordFragment : Fragment() {
    private lateinit var binding : FragmentForgotPasswordBinding
    private var viewModel: AuthenticationViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        return binding.root
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

        binding.resetPasswordBtn.setOnClickListener {
            val email = binding.resetPasswordEmail.text.toString()
            viewModel?.resetPassword(email)
        }

    }

    private fun observeLiveData() {
        viewModel?.resetPasswordState?.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                ResetPasswordState.RESET_SUCCESS -> {
                    resetPasswordSuccess()
                }
                ResetPasswordState.RESET_FAILURE -> {
                    resetPasswordFailure()
                }
                ResetPasswordState.EMPTY_EMAIL -> {
                    emptyEmail()
                }
            }
        })
    }

    private fun resetPasswordSuccess() {
        Toast.makeText(
            context,
            context?.resources?.getString(R.string.reset_password_success),
            Toast.LENGTH_SHORT
        ).show()

       findNavController().navigate(R.id.action_forgotPasswordFragment_to_authNavFragment)
    }

    private fun resetPasswordFailure() {
        Toast.makeText(
            context,
            context?.resources?.getString(R.string.reset_password_failure),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun emptyEmail() {
        Toast.makeText(
            context,
            context?.resources?.getString(R.string.empty_email_field),
            Toast.LENGTH_SHORT
        ).show()
    }

}
