package com.moodup.movies.ui.authentication


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.movies.R
import com.example.movies.databinding.FragmentPhoneLoginBinding
import com.moodup.movies.state.AuthPhone
import com.moodup.movies.ui.MainActivity
import com.moodup.movies.viewmodel.authentication.AuthenticationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class LoginWithPhoneFragment : Fragment() {
    private lateinit var binding: FragmentPhoneLoginBinding
    private val viewModel: AuthenticationViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPhoneLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setOnClickListeners()
        observeLiveData()
    }

    private fun setOnClickListeners() {
        binding.sendVerificationCodeBtn.setOnClickListener {
            val prefix: String = binding.prefix.text.toString()
            val phoneNumber: String = binding.phoneNumber.text.toString()
            if (phoneNumber.isNotEmpty()) {
                sendVerificationCode(prefix, phoneNumber)
            } else {
                onEmptyPhoneField()
            }
        }
        binding.confirmVerificationCodeBtn.setOnClickListener {
            val code = binding.verificationCode.text.toString()
            viewModel.codeMutableLiveData.postValue(code)
            viewModel.verifyVerificationCode(code)
        }

    }

    private fun onEmptyPhoneField() {
        Toast.makeText(
            context,
            context?.resources?.getString(R.string.enter_correct_data),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun sendVerificationCode(prefix: String, phoneNumber: String) {
        viewModel.loginWithPhone(prefix, phoneNumber)
    }

    private fun observeLiveData() {
        viewModel.phoneAuthMutableLiveData.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                AuthPhone.INITIALIZED -> {
                    showPhoneFields()
                }
                AuthPhone.SMS_RECEIVED -> {
                    showConfirmationCodeFields()
                }
                AuthPhone.WRONG_CONFIRMATION_CODE -> {
                    makeToast(context?.resources?.getString(R.string.wrong_confirmation_code))
                }
                AuthPhone.INVALID_REQUEST -> {
                    makeToast(context?.resources?.getString(R.string.invalid_request))
                }
                AuthPhone.TOO_MANY_REQUESTS -> {
                    makeToast(context?.resources?.getString(R.string.too_many_requests))
                }
                AuthPhone.LOGIN_SUCCESS -> {
                    login()
                }

                else -> {

                }
            }
        })
        viewModel.codeMutableLiveData.observe(viewLifecycleOwner, Observer { code ->
            code?.let{
                binding.verificationCode.setText(code)
                viewModel.verifyVerificationCode(code)
            }
        })
    }

    private fun makeToast(message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun showPhoneFields() {
        hideConfirmationCodeFields()
        binding.prefix.visibility = View.VISIBLE
        binding.enterPhoneNumberTitle.visibility = View.VISIBLE
        binding.sendVerificationCodeBtn.visibility = View.VISIBLE
        binding.phoneNumber.visibility = View.VISIBLE
    }

    private fun hidePhoneNumberFields() {
        binding.prefix.visibility = View.GONE
        binding.enterPhoneNumberTitle.visibility = View.GONE
        binding.sendVerificationCodeBtn.visibility = View.GONE
        binding.phoneNumber.visibility = View.GONE
    }

    private fun showConfirmationCodeFields() {
        hidePhoneNumberFields()
        binding.verificationCode.visibility = View.VISIBLE
        binding.enterConfirmationNumberTitle.visibility = View.VISIBLE
        binding.confirmVerificationCodeBtn.visibility = View.VISIBLE
    }

    private fun hideConfirmationCodeFields() {
        binding.verificationCode.visibility = View.GONE
        binding.enterConfirmationNumberTitle.visibility = View.GONE
        binding.confirmVerificationCodeBtn.visibility = View.GONE
    }

    private fun login() {
        makeToast(context?.resources?.getString(R.string.login_success))
        startActivity(Intent(activity, MainActivity::class.java))
    }

}
