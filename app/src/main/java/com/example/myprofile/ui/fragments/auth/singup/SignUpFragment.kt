package com.example.myprofile.ui.fragments.auth.singup

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.myprofile.R
import com.example.myprofile.data.datastore.DataStorePreferences
import com.example.myprofile.databinding.FragmentSignUpBinding
import com.example.myprofile.ui.activities.MainActivity
import com.example.myprofile.ui.fragments.BaseFragment
import com.example.myprofile.utils.Validation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class SignUpFragment : BaseFragment<FragmentSignUpBinding>(FragmentSignUpBinding::inflate) {

    @Inject
    lateinit var dataStorePreferences: DataStorePreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        autologinIfCredentialsSaved()
        setListeners()
    }

    /**
    Redirects to MyProfile activity if email and password are saved
     */
    private fun autologinIfCredentialsSaved() {
        lifecycleScope.launch {
            dataStorePreferences.getCredentialsFlow.collect {
                if (it.email.isNotEmpty() && it.password != "") {
                    goToProfile()
                }
            }
        }
    }

    private fun setListeners() {
        setupRegisterButtonClickListener()
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
            startActivityOrShowError(email, password)
        }
    }

    /**
    Checks if email and password are valid. If so, saves credentials to datastore
    if checkbox is checked, saves name to datastore and goes to MyProfile activity.
    Otherwise shows an error.
     */
    private fun startActivityOrShowError(email: String, password: String) {
        if (Validation.isValidEmail(email) && Validation.isValidPassword(password)) {
            if (binding.checkboxRememberMe.isChecked) {
                saveUserCredentials(email, password)
            }
            saveName(email)
            goToProfile()
        } else {
            if (!Validation.isValidEmail(email)) binding.emailLayout.error =
                getString(R.string.error_email)
            if (!Validation.isValidPassword(password)) binding.passwordLayout.error =
                getString(
                    R.string.error_password,
                    Validation.MIN_PASSWORD_LENGTH,
                    Validation.MAX_PASSWORD_LENGTH
                )
        }
    }

    /**
    If singUp is successful -> go to SingUpExtendedFragment
     */
    private fun goToProfile() {
        // TODO:  If singUp is successful -> go to SingUpExtendedFragment
        val action = SignUpFragmentDirections.actionSignUpFragmentToSingUpExtendedFragment()
        findNavController().navigate(action)
//        val intent = Intent(this, MainActivity::class.java)
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        startActivity(intent)
    }

    private fun saveUserCredentials(email: String, password: String) {
        lifecycleScope.launch {
            dataStorePreferences.saveCredentials(email, password)
        }
    }

    private fun saveName(email: String) {
        lifecycleScope.launch {
            dataStorePreferences.saveName(email)
        }
    }
}