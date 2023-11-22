package com.example.myprofile.ui.fragments.settings

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.myprofile.data.datastore.DataStorePreferences
import com.example.myprofile.databinding.FragmentSettingsBinding
import com.example.myprofile.ui.fragments.viewpager.CONTACTS_FRAGMENT
import com.example.myprofile.ui.fragments.viewpager.ViewPagerFragment
import com.example.myprofile.utils.extentions.dataStore
import kotlinx.coroutines.launch

const val TAG = "FAT_SettingsFragment"
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val dataStorePreferences: DataStorePreferences by lazy { DataStorePreferences(requireContext().dataStore) }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "onCreateView")
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setName()
        Log.d(TAG, "onViewCreated")
        binding.btnContacts.setOnClickListener {
            val viewPagerFragment = requireParentFragment() as ViewPagerFragment
            viewPagerFragment.goToFragment(CONTACTS_FRAGMENT)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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