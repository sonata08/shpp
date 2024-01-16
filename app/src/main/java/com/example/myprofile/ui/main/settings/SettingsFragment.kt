package com.example.myprofile.ui.main.settings

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.myprofile.data.datastore.DataStorePreferences
import com.example.myprofile.data.model.User
import com.example.myprofile.data.network.model.UiState
import com.example.myprofile.databinding.FragmentSettingsBinding
import com.example.myprofile.ui.auth.AuthActivity
import com.example.myprofile.ui.base.BaseFragment
import com.example.myprofile.ui.main.viewpager.ViewPagerFragment
import com.example.myprofile.ui.main.viewpager.ViewPagerFragment.Companion.CONTACTS_FRAGMENT
import com.example.myprofile.ui.main.viewpager.ViewPagerFragmentDirections
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding>(FragmentSettingsBinding::inflate) {

    @Inject
    lateinit var dataStorePreferences: DataStorePreferences
    private val viewModel: SettingsViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeUiState()
        setListeners()
        viewModel.getUser()
//        Log.d("FAT_SettingsFrag", "viewModel = ${viewModel.hashCode()}")
    }

    private fun setListeners() {
        binding.btnContacts.setOnClickListener {
            val viewPagerFragment = requireParentFragment() as ViewPagerFragment
            viewPagerFragment.goToFragment(CONTACTS_FRAGMENT)
        }
        binding.btnLogout.setOnClickListener {
            logOut()
        }
        binding.btnEditProfile.setOnClickListener {
//            val string = Json.encodeToString(user)
            val action = ViewPagerFragmentDirections.actionViewPagerFragmentToEditProfileFragment()
            findNavController().navigate(action)
        }
    }


    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.authStateFlow.collect {
                    when (it) {
                        is UiState.Initial -> {}
                        is UiState.Success -> showUserData(it.data)
                        is UiState.Loading -> showProgressBar()
                        is UiState.Error -> showError(it.message)
                    }
                }
            }
        }
    }

    private fun showUserData(user: User) {
        binding.progressBar.visibility = View.GONE
        bindUsersData(user)
    }
    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun showError(error: String) {
        // TODO: think what to do in case of error
        binding.progressBar.visibility = View.GONE
        Log.d("FAT_SettingsFrag", "UiState.Error = $error")
        Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG)
            .show()
    }

    private fun bindUsersData(user: User) {
        with(binding) {
            tvName.text = user.name
            tvCareer.text = user.career
            tvHomeAddress.text = user.address
        }
    }

    private fun logOut() {
        lifecycleScope.launch {
            dataStorePreferences.clear()
        }
        activity?.finish()
        val intent = Intent(requireContext(), AuthActivity::class.java)
        startActivity(intent)
    }

    private fun goToLoginFragment() {
        val action = ViewPagerFragmentDirections.actionViewPagerFragmentToAuthActivity()
        findNavController().navigate(action)
    }
}