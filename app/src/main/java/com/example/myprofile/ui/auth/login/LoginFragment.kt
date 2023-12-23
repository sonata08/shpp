package com.example.myprofile.ui.auth.login

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.myprofile.R
import com.example.myprofile.data.model.UserCredentialsAuth
import com.example.myprofile.data.network.dto.AuthUiState
import com.example.myprofile.data.network.dto.LoginResponseData
import com.example.myprofile.databinding.FragmentLoginBinding
import com.example.myprofile.ui.base.BaseFragment
import com.example.myprofile.utils.Validation
import com.example.myprofile.utils.extentions.showShortToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {

    private val viewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        autoLoginUser()
        setListeners()
        setAuthStateObserver()
        viewModel.autoLoginUser()
        with(binding) {
            emailEdit.setText("t@t.t")
            passwordEdit.setText("123")
        }
    }

    private fun setListeners() {
        binding.signUp.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToSignUpFragment()
            findNavController().navigate(action)
        }



        binding.btnLogin.setOnClickListener {
            val email = binding.emailEdit.text.toString()
            val password = binding.passwordEdit.text.toString()
            val userCredentials = UserCredentialsAuth(email, password)
            viewModel.loginUser(userCredentials)

        }
    }

    private fun autoLoginUser() {
        // TODO: GET /users/:userId

    }


    private fun setAuthStateObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.authStateFlow.collect {
                    Log.d("FAT_LoginFragment", "uiState = $it")
                    when (it) {
                        is AuthUiState.Success -> singInUser(it.data.data)
                        is AuthUiState.Loading -> {

                        } //TODO
                        is AuthUiState.Error -> showAuthError(it.message)
                    }


                }
            }
        }
    }

    private fun singInUser(data: LoginResponseData) {
        viewModel.rememberUser(binding.checkboxRememberMe.isChecked)
        viewModel.saveUserIdTokens(data)
        gotoProfile()

    }

    private fun showAuthError(message: String) {
        Log.d("FAT_LoginFragment", "message = $message")
        requireContext().showShortToast(message)
    }

    private fun setupPasswordListener() {
        binding.passwordEdit.doAfterTextChanged {
            binding.passwordLayout.error = if (!Validation.isValidPassword(it.toString())) {
                getString(
                    R.string.error_password,
                    Validation.MIN_PASSWORD_LENGTH,
                    Validation.MAX_PASSWORD_LENGTH
                )
            } else {
                null
            }
        }
    }

    private fun setupEmailListener() {
        binding.emailEdit.doAfterTextChanged {
            if (!Validation.isValidEmail(it.toString())) {
                binding.emailLayout.error = getString(R.string.error_email)
            } else {
                binding.emailLayout.error = null
            }
        }
    }

    private fun gotoProfile() {
//        val intent = Intent(requireContext(), MainActivity::class.java)
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        startActivity(intent)

        val action = LoginFragmentDirections.actionLoginFragmentToMainActivity()
        findNavController().navigate((action))
    }
}