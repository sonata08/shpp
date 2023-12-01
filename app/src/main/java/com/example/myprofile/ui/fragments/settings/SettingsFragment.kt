package com.example.myprofile.ui.fragments.settings

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.example.myprofile.data.datastore.DataStorePreferences
import com.example.myprofile.databinding.FragmentSettingsBinding
import com.example.myprofile.ui.activities.AuthActivity
import com.example.myprofile.ui.fragments.BaseFragment
import com.example.myprofile.ui.fragments.viewpager.ViewPagerFragment
import com.example.myprofile.ui.fragments.viewpager.ViewPagerFragment.Companion.CONTACTS_FRAGMENT
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding>(FragmentSettingsBinding::inflate) {

    @Inject lateinit var dataStorePreferences: DataStorePreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setName()
        setListeners()
    }

    private fun setListeners() {
        binding.btnContacts.setOnClickListener {
            val viewPagerFragment = requireParentFragment() as ViewPagerFragment
            viewPagerFragment.goToFragment(CONTACTS_FRAGMENT)
        }
        binding.btnLogout.setOnClickListener {
            logOut()
        }
    }

    /**
    Gets name from dataStore and binds it to the corresponding view
    */
    private fun setName() {
        lifecycleScope.launch {
            dataStorePreferences.getCredentialsFlow.collect{
                binding.tvName.text = it.name
            }
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
}