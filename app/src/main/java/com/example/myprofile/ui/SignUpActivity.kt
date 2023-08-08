package com.example.myprofile.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import com.example.myprofile.data.datastore.DataStorePreferences
import kotlinx.coroutines.launch
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.lifecycleScope
import com.example.myprofile.R
import com.example.myprofile.databinding.ActivitySignUpBinding

const val DATA_STORE_NAME = "settings"
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_NAME)

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var email: String // TODO: remove from global
    private lateinit var password: String
    private var ifRegisterButtonClicked = false // TODO: bad
    private val dataStorePreferences: DataStorePreferences by lazy { DataStorePreferences(dataStore) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        autologinIfCredentialsSaved()
        listenToUserActions()// TODO: setListeners()
    }

    /*
        Redirects to MyProfile activity if email and password are saved
     */
    private fun autologinIfCredentialsSaved() {
        lifecycleScope.launch {
            dataStorePreferences.getCredentialsFlow.collect {
                email = it.email
                password = it.password
                if (email.isNotEmpty() && password != "") {
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

    /*
        Listens to user's actions on password field.
        Shows error only if Register button has already been clicked.
     */
    private fun setupPasswordListener() {
        binding.passwordEdit.doAfterTextChanged {
            if (ifRegisterButtonClicked) {
                binding.passwordLayout.error = if (!isValidPassword(it.toString())) {
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

    /*
        Gets email and password from user's input then starts MyProfile activity
        or shows errors.
     */
    private fun setupRegisterButtonClickListener() {
        binding.registerButton.setOnClickListener {
            email = binding.emailEdit.text.toString() // TODO: not that view
            password = binding.passwordLayout.editText?.text.toString()
            ifRegisterButtonClicked = true
            startActivityOrShowError(email, password)
        }
    }

    /*
        Checks if email and password are valid. If so, saves credentials to datastore
        if checkbox is checked, saves name to datastore and goes to MyProfile activity.
        Otherwise shows an error.
     */
    private fun startActivityOrShowError(email: String, password: String) {
        if (isValidEmail(email) && isValidPassword(password)) {
            if (binding.checkboxRememberMe.isChecked) saveUserCredentials()
            saveName()
            goToProfile()
        } else {
            if (!isValidEmail(this.email)) binding.emailLayout.error =
                getString(R.string.error_email)
            if (!isValidPassword(this.password)) binding.passwordLayout.error =
                getString(R.string.error_password)
        }
    }

    /*
        Checks email validity
     */
    private fun isValidEmail(email: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    /*
        Checks password validity
     */
    private fun isValidPassword(password: String): Boolean {
        return password.length in 8..16 // TODO: to constants
    }

    /*
        Sets MainActivity as start of a new task on this history stack, clears existing task
        and starts MainActivity
     */
    private fun goToProfile() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun saveUserCredentials() {
        lifecycleScope.launch {
            dataStorePreferences.saveCredentials(email, password)
        }
    }

    private fun saveName() {
        lifecycleScope.launch {
            dataStorePreferences.saveName(email)
        }
    }
    companion object {

    }
}


