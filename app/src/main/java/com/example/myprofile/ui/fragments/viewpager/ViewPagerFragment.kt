package com.example.myprofile.ui.fragments.viewpager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.myprofile.databinding.FragmentViewPagerBinding

const val SETTINGS_FRAGMENT = 0
const val CONTACTS_FRAGMENT = 1
class ViewPagerFragment : Fragment() {

    private var _binding: FragmentViewPagerBinding? = null
    private val binding get() = _binding!!

    lateinit var viewPager: ViewPager2

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
        viewPager = binding.viewPager
        viewPager.adapter = ViewPagerAdapter(this)
//        val tabLayout = binding.tabLayout
//        TabLayoutMediator(tabLayout, viewPager) {tab, position ->
//            when(position) {
//                SETTINGS_FRAGMENT -> tab.text = resources.getString(R.string.settings)
//                CONTACTS_FRAGMENT -> tab.text = resources.getString(R.string.contacts)
//            }
//        }.attach()
    }

    fun openContactsFragment() {
        viewPager.currentItem = 1
    }



}