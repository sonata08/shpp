package com.example.myprofile.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myprofile.R

class MainActivity : AppCompatActivity() {

//    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
//    private val dataStorePreferences: DataStorePreferences by lazy { DataStorePreferences(dataStore) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


}