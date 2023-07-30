package com.example.myprofile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.myprofile.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private val dataStorePreferences: DataStorePreferences by lazy { DataStorePreferences(dataStore) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setName()





    }

    private fun setName() {
        lifecycleScope.launch {
            dataStorePreferences.getCredentialsFlow.collect{

                binding.name.text = it.name
            }
        }
    }

}