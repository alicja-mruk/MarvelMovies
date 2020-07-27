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
import com.moodup.movies.state.AuthRegisterState
import com.moodup.movies.viewmodel.AuthenticationViewModel
import kotlinx.android.synthetic.main.fragment_signup.*

class SignupFragment : Fragment(){
    private var viewModel : AuthenticationViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_signup, container, false)
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
        signup_btn.setOnClickListener { register() }

        already_have_account.setOnClickListener {
            findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
        }
    }
    private fun observeLiveData(){
        viewModel?.authenticationRegisterState?.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                AuthRegisterState.EMPTY_EMAIL_OR_PASSWORD_FIELD->{
                    onEmptyEmailOrPasswordField()
                }
                AuthRegisterState.ON_REGISTER_SUCCESS->{
                    onRegisterSuccess()
                }
                AuthRegisterState.ON_REGISTER_FAILURE->{
                    onRegisterFailure()
                }
            }
        })
    }

    private fun register() {
        val email = email_register_text.text.toString()
        val password = password_register_text.text.toString()
        viewModel?.register(email, password)

    }

    private fun onRegisterSuccess(){
        findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
        Toast.makeText(
            context,
            context?.resources?.getString(R.string.register_success),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun onRegisterFailure(){
        Toast.makeText(
            context,
            context?.resources?.getString(R.string.register_failure),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun onEmptyEmailOrPasswordField(){
        Toast.makeText(
            context,
            context?.resources?.getString(R.string.enter_correct_data),
            Toast.LENGTH_SHORT
        ).show()
    }
}