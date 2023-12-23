package com.example.myprofile.ui.main.viewpager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myprofile.ui.main.contacts.ContactsFragment
import com.example.myprofile.ui.main.settings.SettingsFragment

private const val NUM_TABS = 2
class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        when(position) {
            0 -> return SettingsFragment()
        }
        return ContactsFragment()
    }
}