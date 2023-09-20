package com.example.myprofile.ui.fragments.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.myprofile.data.datastore.DataStorePreferences
import com.example.myprofile.databinding.FragmentSettingsBinding
import com.example.myprofile.utils.extentions.dataStore
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 */
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val dataStorePreferences: DataStorePreferences by lazy { DataStorePreferences(requireContext().dataStore) }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setName()
        binding.btnContacts.setOnClickListener {
            val action = SettingsFragmentDirections.actionSettingsFragmentToContactsFragment()
            findNavController().navigate(action)
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
    }
}