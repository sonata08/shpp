package com.example.myprofile.ui.auth.singup

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
import com.example.myprofile.databinding.FragmentSignUpBinding
import com.example.myprofile.ui.base.BaseFragment
import com.example.myprofile.utils.Validation
import com.example.myprofile.utils.extentions.showShortToast
import com.example.myprofile.utils.localizeError
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private const val TOKEN = "eyJpdiI6IitISVhMa21oODlHalpRamJjQU5nZVE9PSIsInZhbHVlIjoiaVpjdDdJVk1OTmZPMSs1NVhuQ29qckNJNnFWazlLaEZ3aUpUUUxVTFMwT3RSQmUzQjBSUDhFZFlwK1lpakNkVlV5ZzUxMW8vZkhYQkVuVTRnNERoVjY2dmFPSU5FNjBNa2hhU1ZWc1o1b3M9IiwibWFjIjoiMjQ5YzAzZmMyOTUwNWM5MzA5NTBmOGZiNmMxMDdhOTNlYWYzMzNiMjYxZjk0YjAwZGJlOTYzYTcyYTc2OTBhMyJ9"
private const val REFRESH_TOKEN = "eyJpdiI6IlZkNTlLY3N6V3FiUFpISU5abHZKZXc9PSIsInZhbHVlIjoiTjdMUUV1ZnRvZDBWdEpMb3Fpdm00NGpKU2UzVWYrZnpLR0t3dUtWOUZxMytjUWRFc2ZYb0hIMkxMSEZHYzllaUxxdHhwcXVxSEtHS3gyaTYxRXNLSHJiZnZzdFRYclFad3ZxRHdhNnU3Q289IiwibWFjIjoiYjM1MWUwMGFiNDJhMWVkYjIwMzQ2MzJlNjA0NjUyZTFmYjFlOTljNWEzZjEyMzk3YmQ2MDdjMmM4ZDgwMDg2OSJ9"
private const val USER_ID = 560L

@AndroidEntryPoint
class SignUpFragment : BaseFragment<FragmentSignUpBinding>(FragmentSignUpBinding::inflate) {

    private val viewModel: SignUpViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        autologinIfCredentialsSaved()
        setListeners()


        with(binding) {
            emailEdit.setText("t@t.t")
            passwordEdit.setText("123")
        }
    }


    /**
    Redirects to MyProfile activity if email and password are saved
     */
    private fun autologinIfCredentialsSaved() {
        lifecycleScope.launch {
//            dataStorePreferences.getCredentialsFlow.collect {
//                if (it.email.isNotEmpty() && it.password != "") {
//                    goToSignUpExtended()
//                }
//            }
        }
    }

    private fun setListeners() {
        setupRegisterButtonClickListener()
        setupSignInBtnListener()
        setupEmailListener()
        setupPasswordListener()
    }

    /**
    Listens to user's actions on password field.
    Shows error only if Register button has already been clicked.
     */
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

    private fun setupSignInBtnListener() {
        binding.signIn.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    /**
    Listens to user's actions on email field.
    Shows error only if Register button has already been clicked.
     */
    private fun setupEmailListener() {
        binding.emailEdit.doAfterTextChanged {
            if (!Validation.isValidEmail(it.toString())) {
                binding.emailLayout.error = getString(R.string.error_email)
            } else {
                binding.emailLayout.error = null
            }
        }
    }

    /**
    Gets email and password from user's input then starts MyProfile activity
    or shows errors.
     */
    private fun setupRegisterButtonClickListener() {
        binding.btnRegister.setOnClickListener {
            val email = binding.emailEdit.text.toString()
            val password = binding.passwordEdit.text.toString()
            val credentials = UserCredentialsAuth(email, password)

            if(isValidCredentials(credentials)) {
                viewModel.createUser(credentials)
                setUpAuthStateListener()
            }
        }
    }

    private fun setUpAuthStateListener() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.authStateFlow.collect {
                    when (it) {
                        is AuthUiState.Success -> {
                            if (binding.checkboxRememberMe.isChecked) {
                                viewModel.saveUserIdTokens(it.data.data)
                            }
                            goToSignUpExtended(it.data.data)
                        }
                        is AuthUiState.Loading -> {
                            Log.d("FAT_SignInFr", "AuthUiState.Loading")
                        } //TODO
                        is AuthUiState.Error -> {
                            requireContext().showShortToast(localizeError(it.message, requireContext()))
                            Log.d("FAT_SignInFr", "error = ${it.message}")
                            goToSignUpTest()
                        }
                    }
                }
            }
        }
    }

    /**
    Checks if email and password are valid. If so, saves credentials to datastore
    if checkbox is checked, saves name to datastore and goes to MyProfile activity.
    Otherwise shows an error.
     */
    private fun isValidCredentials(credentials: UserCredentialsAuth): Boolean {
        return if (Validation.isValidEmail(credentials.email) && Validation.isValidPassword(credentials.password)) {
            true
        } else {
            if (!Validation.isValidEmail(credentials.email)) binding.emailLayout.error =
                getString(R.string.error_email)
            if (!Validation.isValidPassword(credentials.password)) binding.passwordLayout.error =
                getString(
                    R.string.error_password,
                    Validation.MIN_PASSWORD_LENGTH,
                    Validation.MAX_PASSWORD_LENGTH
                )
            false
        }
    }

    /**
    If singUp is successful -> go to SingUpExtendedFragment
     */
    private fun goToSignUpExtended(response: LoginResponseData) {
        val action = SignUpFragmentDirections.actionSignUpFragmentToSingUpExtendedFragment(
            userId = response.user.id,
            accessToken = response.accessToken,
            refreshToken = response.refreshToken
            )
        findNavController().navigate(action)
    }

    private fun goToSignUpTest() {
        val action = SignUpFragmentDirections.actionSignUpFragmentToSingUpExtendedFragment(
            userId = USER_ID,
            accessToken = TOKEN,
            refreshToken = REFRESH_TOKEN
        )
        findNavController().navigate(action)
    }

    private fun saveUserCredentials(email: String, password: String) {
        lifecycleScope.launch {
//            dataStorePreferences.saveCredentials(email, password)
        }
    }

//    private fun saveName(email: String) {
//        lifecycleScope.launch {
//            dataStorePreferences.saveName(email)
//        }
//    }
}