package com.example.myprofile.ui.fragments.viewpager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myprofile.R
import com.example.myprofile.databinding.FragmentViewPagerBinding
import com.google.android.material.tabs.TabLayoutMediator

class ViewPagerFragment : Fragment() {

    private var _binding: FragmentViewPagerBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewPagerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewPager = binding.viewPager
//        val tabLayout = binding.tabLayout
        viewPager.adapter = ViewPagerAdapter(this)
//        TabLayoutMediator(tabLayout, viewPager) {tab, position ->
//            when(position) {
//                0 -> tab.text = resources.getString(R.string.settings)
//                1 -> tab.text = resources.getString(R.string.contacts)
//            }
//        }.attach()
    }

}