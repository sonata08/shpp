package com.example.myprofile.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.myprofile.databinding.ActivityMainBinding
import com.example.myprofile.data.datastore.DataStorePreferences
import kotlinx.coroutines.launch

/*
    Activity for user's profile
 */
class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val dataStorePreferences: DataStorePreferences by lazy { DataStorePreferences(dataStore) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setName()
    }

    /*
        Gets name from dataStore and binds it to the corresponding view
     */
    private fun setName() {
        lifecycleScope.launch {
            dataStorePreferences.getCredentialsFlow.collect{
                binding.tvName.text = it.name
            }
        }
    }
}