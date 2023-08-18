package com.example.myprofile.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import com.example.myprofile.R
import com.example.myprofile.data.datastore.DataStorePreferences
import com.example.myprofile.databinding.ActivitySignUpBinding
import com.example.myprofile.utils.Validation
import com.example.myprofile.utils.ext.dataStore
import kotlinx.coroutines.launch

const val DATA_STORE_NAME = "settings"

class SignUpActivity : AppCompatActivity() {

    private val binding: ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(
            layoutInflater
        )
    }
    private val dataStorePreferences: DataStorePreferences by lazy { DataStorePreferences(dataStore) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        autologinIfCredentialsSaved()
        setListeners()
    }

    /*
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

    /*
        Listens to user's actions on password field.
        Shows error only if Register button has already been clicked.
     */
    private fun setupPasswordListener() {
        // binding.run
        binding.run {
            passwordEdit.doAfterTextChanged {
                passwordLayout.error = if (!Validation.isValidPassword(it.toString())) {
                    getString(R.string.error_password)
                } else {
                    null
                }
            }
        }
    }

    /*
        Listens to user's actions on email field.
        Shows error only if Register button has already been clicked.
     */
    private fun setupEmailListener() {
        // binding.run
        binding.emailEdit.doAfterTextChanged {
            if (!Validation.isValidEmail(it.toString())) {
                binding.emailLayout.error = getString(R.string.error_email)
            } else {
                binding.emailLayout.error = null
            }
        }
    }

    /*
        Gets email and password from user's input then starts MyProfile activity
        or shows errors.
     */
    private fun setupRegisterButtonClickListener() {
        binding.registerButton.setOnClickListener {
            val email = binding.emailEdit.text.toString()
            val password = binding.passwordEdit.text.toString()
            startActivityOrShowError(email, password)
        }
    }

    /*
        Checks if email and password are valid. If so, saves credentials to datastore
        if checkbox is checked, saves name to datastore and goes to MyProfile activity.
        Otherwise shows an error.
     */
    private fun startActivityOrShowError(email: String, password: String) {
        if (Validation.isValidEmail(email) && Validation.isValidPassword(password)) {
            if (binding.checkboxRememberMe.isChecked) {
                saveUserCredentials(
                    email,
                    password
                )
            }
            saveName(email)
            goToProfile()
        } else {
            if (!Validation.isValidEmail(email)) binding.emailLayout.error =
                getString(R.string.error_email)
            if (!Validation.isValidPassword(password)) binding.passwordLayout.error =
                getString(R.string.error_password)
        }
    }

    /*
        Sets MainActivity as start of a new task on this history stack, clears existing task
        and starts MainActivity
     */
    private fun goToProfile() {
        Intent(this, MainActivity::class.java).run {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(this)
        }
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


