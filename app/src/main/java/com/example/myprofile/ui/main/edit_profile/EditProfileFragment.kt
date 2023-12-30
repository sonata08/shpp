package com.example.myprofile.ui.main.edit_profile


import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.myprofile.R
import com.example.myprofile.data.model.User
import com.example.myprofile.databinding.FragmentEditProfileBinding
import com.example.myprofile.ui.base.BaseFragment
import com.example.myprofile.ui.main.settings.SettingsViewModel

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditProfileFragment : BaseFragment<FragmentEditProfileBinding>(FragmentEditProfileBinding::inflate) {

//    private val viewModel: EditProfileViewModel by viewModels()
    private val viewModel: SettingsViewModel by hiltNavGraphViewModels(R.id.viewPagerFragment)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        Log.d("FAT_EditProf", "viewModel = ${viewModel.hashCode()}")

        setUserData()
        setListeners()

    }

    private fun setUserData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.user.collect {user ->
                    Log.d("FAT_EditProf", "user id = ${user.id}")
                    if (user.id != -1L) {
                        bindData(user)
                    }
                }
            }
        }
    }

    private fun bindData(user: User) {
        with(binding) {
            usernameEdit.setText(user.name)
            careerEdit.setText(user.career)
            phoneEdit.setText(user.phone)
            addressEdit.setText(user.address)
            birthdateEdit.setText(user.birthday)
        }
    }

    private fun setListeners() {
        binding.btnSave.setOnClickListener {
            viewModel.updateUser(makeUser())
            findNavController().popBackStack()
        }
    }

    private fun makeUser(): User {
        return with(binding) {
           User(
               name = usernameEdit.text.toString(),
               career = careerEdit.text.toString(),
               phone = phoneEdit.text.toString(),
               address = addressEdit.text.toString(),
               birthday = birthdateEdit.text.toString(),
               image = ""
           )
        }
    }
}