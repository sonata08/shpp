package com.example.myprofile.ui.main.viewpager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.myprofile.databinding.FragmentViewPagerBinding
import com.example.myprofile.ui.base.BaseFragment

class ViewPagerFragment : BaseFragment<FragmentViewPagerBinding>(FragmentViewPagerBinding::inflate) {

    private lateinit var viewPager: ViewPager2

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPager = binding.viewPager
        viewPager.adapter = ViewPagerAdapter(this)
//        val tabLayout = binding.tabLayout
//        TabLayoutMediator(tabLayout, viewPager) {tab, position ->
//            when(position) {
//                TabFragments.SETTINGS_FRAGMENT.ordinal -> tab.text = resources.getString(R.string.settings)
//                TabFragments.CONTACTS_FRAGMENT.ordinal -> tab.text = resources.getString(R.string.contacts)
//            }
//        }.attach()
    }

    fun goToFragment(name: Int) {
        viewPager.currentItem = name
    }
}