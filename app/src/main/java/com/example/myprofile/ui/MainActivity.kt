package com.example.myprofile.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.myprofile.datastore.DataStorePreferences
import com.example.myprofile.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

/*
    Activity for user's profile
 */
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private val dataStorePreferences: DataStorePreferences by lazy { DataStorePreferences(dataStore) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setName()
    }

    /*
        Gets name from dataStore and binds it to the corresponding view
     */
    private fun setName() {
        lifecycleScope.launch {
            dataStorePreferences.getCredentialsFlow.collect{
                binding.name.text = it.name
            }
        }
    }
}