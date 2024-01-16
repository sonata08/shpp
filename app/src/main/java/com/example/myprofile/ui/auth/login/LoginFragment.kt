package com.example.myprofile.ui.auth.login

import android.content.Intent
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
import com.example.myprofile.data.model.UserCredentials
import com.example.myprofile.data.network.model.UiState
import com.example.myprofile.data.network.model.LoginResponse
import com.example.myprofile.databinding.FragmentLoginBinding
import com.example.myprofile.ui.base.BaseFragment
import com.example.myprofile.ui.main.MainActivity
import com.example.myprofile.utils.Validation
import com.example.myprofile.utils.localizeError
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {

    private val viewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("FAT_Login", "login Fragment onViewCreated")

//        autoLoginUser()
        setListeners()
        observeUiState()

        with(binding) {
            emailEdit.setText("t@mail.com")
            passwordEdit.setText("123")
        }
    }

    private fun autoLoginUser() {
        viewModel.autoLoginUser()
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.autoLoginFlow.collect {
                    when (it) {
                        is UiState.Initial -> {}
                        is UiState.Success -> gotoProfile()
                        is UiState.Loading -> showProgressBar()
                        is UiState.Error -> {binding.progressBar.visibility = View.GONE}
                    }
                }
            }
        }
    }


    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.authStateFlow.collect {
                    when (it) {
                        is UiState.Initial -> {}
                        is UiState.Success -> singInUser(it.data)
                        is UiState.Loading -> showProgressBar()
                        is UiState.Error -> showError(it.message)
                    }
                }
            }
        }
    }

    private fun singInUser(data: LoginResponse) {
        binding.progressBar.visibility = View.GONE
        viewModel.rememberUser(binding.checkboxRememberMe.isChecked)
        viewModel.saveUserIdTokens(data)
        gotoProfile()
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun showError(error: String) {
        val localizedError = localizeError(error, requireContext())
        binding.progressBar.visibility = View.GONE
        Snackbar.make(binding.root, localizedError, Snackbar.LENGTH_LONG)
            .show()
    }

    private fun setListeners() {
        setupSignUpListener()
        setupLoginListener()
        setupEmailListener()
        setupPasswordListener()
    }

    private fun setupLoginListener() {
        binding.btnLogin.setOnClickListener {
            val email = binding.emailEdit.text.toString()
            val password = binding.passwordEdit.text.toString()
            val userCredentials = UserCredentials(email, password)
            if (Validation.isValidEmail(email) && Validation.isValidPassword(password)) {
                viewModel.loginUser(userCredentials)
            }
        }
    }

    private fun setupSignUpListener() {
        binding.signUp.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToSignUpFragment()
            findNavController().navigate(action)
        }
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
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}