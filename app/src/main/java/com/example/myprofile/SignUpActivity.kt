package com.example.myprofile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.example.myprofile.databinding.ActivitySignUpBinding
import kotlinx.coroutines.launch

const val DATA_STORE_NAME = "settings"
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_NAME)

class SignUpActivity: AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var email: String
    private lateinit var password: String
    private var ifRegisterButtonClicked = false
    private val dataStorePreferences: DataStorePreferences by lazy { DataStorePreferences(dataStore) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        autologinIfCredentialsSaved()
        listenToUserActions()
    }

    private fun autologinIfCredentialsSaved() {
        lifecycleScope.launch{
            dataStorePreferences.getCredentialsFlow.collect {
                email = it.email
                password = it.password
                if (email != "" && password != "") {
                    goToProfile()
                }
            }
        }
    }

    private fun listenToUserActions() {
        setupRegisterButtonClickListener()
        setupEmailListener()
        setupPasswordListener()
    }

    private fun setupPasswordListener() {
        binding.passwordEdit.doAfterTextChanged {
            if (ifRegisterButtonClicked) {
                if (!isValidPassword(it.toString())) {
                    binding.passwordLayout.error = getString(R.string.error_password)
                } else {
                    binding.passwordLayout.error = null
                }
            }
        }
    }

    private fun setupEmailListener() {
        binding.emailEdit.doAfterTextChanged {
            if (ifRegisterButtonClicked) {
                if (!isValidEmail(it.toString())) {
                    binding.emailLayout.error = getString(R.string.error_email)
                } else {
                    binding.emailLayout.error = null
                }
            }
        }
    }

    private fun setupRegisterButtonClickListener() {
        binding.registerButton.setOnClickListener {
            email = binding.emailLayout.editText?.text.toString()
            password = binding.passwordLayout.editText?.text.toString()
            ifRegisterButtonClicked = true
            startActivityOrShowError()
        }
    }

    private fun startActivityOrShowError() {
        if (isValidEmail(email) && isValidPassword(password)) {
            if (binding.checkboxRememberMe.isChecked) saveUserCredentials()
            goToProfile()
        } else {
            if (!isValidEmail(email)) binding.emailLayout.error = getString(R.string.error_email)
            if (!isValidPassword(password)) binding.passwordLayout.error = getString(R.string.error_password)
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length in 8..16
    }

    // Sets MainActivity as start of a new task on this history stack, clears existing task
    // and starts MainActivity
    private fun goToProfile() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun saveUserCredentials(){
        lifecycleScope.launch{
            dataStorePreferences.saveCredentials(email, password)
        }
    }



}


