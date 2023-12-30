package com.example.myprofile.ui.main.edit_profile

import androidx.lifecycle.ViewModel
import com.example.myprofile.data.repository.AuthRepository
import com.example.myprofile.data.repository.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val dataStoreRepository: DataStoreRepository,
) : ViewModel() {

    fun updateUser() {
        
    }
}