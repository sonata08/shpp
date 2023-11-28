package com.example.myprofile.ui.fragments.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.example.myprofile.data.datastore.DataStorePreferences
import com.example.myprofile.databinding.FragmentSettingsBinding
import com.example.myprofile.ui.activities.AuthActivity
import com.example.myprofile.ui.fragments.BaseFragment
import com.example.myprofile.ui.fragments.viewpager.ViewPagerFragment
import com.example.myprofile.ui.fragments.viewpager.ViewPagerFragment.Companion.CONTACTS_FRAGMENT
import com.example.myprofile.utils.extentions.dataStore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

const val TAG = "FAT_SettingsFragment"

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding>(FragmentSettingsBinding::inflate) {

    private val dataStorePreferences: DataStorePreferences by lazy { DataStorePreferences(requireContext().dataStore) }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setName()
        Log.d(TAG, "onViewCreated")
        Log.d(TAG, "datastorPref = $dataStorePreferences")
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




    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "onAttach")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(TAG, "onDetach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
    }
}