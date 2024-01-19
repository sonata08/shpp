package com.example.myprofile.ui.auth.singup

import android.os.Bundle
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
import com.example.myprofile.databinding.FragmentSignUpBinding
import com.example.myprofile.ui.base.BaseFragment
import com.example.myprofile.utils.Validation
import com.example.myprofile.utils.showError
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SignUpFragment : BaseFragment<FragmentSignUpBinding>(FragmentSignUpBinding::inflate) {

    private val viewModel: SignUpViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    private fun setListeners() {
        observeUiState()
        setupRegisterButtonClickListener()
        setupSignInBtnListener()
        setupEmailListener()
        setupPasswordListener()
    }

    private fun setupSignInBtnListener() {
        binding.signIn.setOnClickListener {
            findNavController().navigateUp()
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

    private fun setupRegisterButtonClickListener() {
        binding.btnRegister.setOnClickListener {
            val email = binding.emailEdit.text.toString()
            val password = binding.passwordEdit.text.toString()
            val credentials = UserCredentials(email, password)

            if (Validation.isValidEmail(email) && Validation.isValidPassword(password)) {
                viewModel.createUser(credentials)
            }
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.authStateFlow.collect {
                    when (it) {
                        is UiState.Initial -> {}
                        is UiState.Success -> {
                            saveUserData(it.data)
                            goToSignUpExtended()
                        }
                        is UiState.Loading -> showProgressBar()
                        is UiState.Error -> showError(binding.root, binding.progressBar, it.message)
                    }
                }
            }
        }
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun saveUserData(data: LoginResponse) {
        binding.progressBar.visibility = View.GONE
        viewModel.rememberUser(binding.checkboxRememberMe.isChecked)
        viewModel.saveUserIdTokens(data)
    }

    private fun goToSignUpExtended() {
        val action = SignUpFragmentDirections.actionSignUpFragmentToSingUpExtendedFragment()
        findNavController().navigate(action)
    }
}