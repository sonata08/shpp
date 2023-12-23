package com.example.myprofile.ui.main.edit_profile


import androidx.fragment.app.viewModels
import com.example.myprofile.databinding.FragmentEditProfileBinding
import com.example.myprofile.ui.base.BaseFragment

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileFragment : BaseFragment<FragmentEditProfileBinding>(FragmentEditProfileBinding::inflate) {

    private val viewModel: EditProfileViewModel by viewModels()

}