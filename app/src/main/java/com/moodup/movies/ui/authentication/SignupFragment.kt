package com.moodup.movies.ui.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.movies.R
import com.example.movies.databinding.FragmentSignupBinding
import com.moodup.movies.state.AuthRegisterState
import com.moodup.movies.viewmodel.authentication.AuthenticationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignupFragment : Fragment(){
    private  lateinit var binding : FragmentSignupBinding
    private val viewModel : AuthenticationViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setOnClickListeners()
        observeLiveData()
    }


    private fun setOnClickListeners() {
        binding.signupBtn.setOnClickListener { register() }

        binding.alreadyHaveAccount.setOnClickListener {
            findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
        }
    }
    private fun observeLiveData(){
        viewModel.authenticationRegisterState.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                AuthRegisterState.EMPTY_EMAIL_OR_PASSWORD_FIELD->{
                    onEmptyEmailOrPasswordField()
                }
                AuthRegisterState.ON_REGISTER_SUCCESS->{
                    onRegisterSuccess()
                }
                AuthRegisterState.ON_REGISTER_FAILURE->{
                    onRegisterFailure()
                }else->{

            }
            }
        })
    }

    private fun register() {
        val email = binding.emailRegisterText.text.toString()
        val password = binding.passwordRegisterText.text.toString()
        viewModel.register(email, password)

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