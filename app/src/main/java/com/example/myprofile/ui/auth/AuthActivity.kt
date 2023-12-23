package com.example.myprofile.ui.auth


import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.myprofile.R
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        Log.d("FAT_AuthAct", "AuthActivity is created")
    }


    override fun onStop() {
        super.onStop()
        Log.d("FAT_AuthAct", "AuthActivity is stopped")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.d("FAT_AuthAct", "AuthActivity is destroyed")
    }
}


