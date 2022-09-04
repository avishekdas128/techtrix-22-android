package com.orangeink.techtrix.registrations.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.orangeink.techtrix.registrations.data.model.Registration
import com.orangeink.techtrix.registrations.ui.fragments.RegistrationListFragment

class RegistrationPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val tabsList: List<String>,
    private val list: List<Registration>
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun createFragment(position: Int): Fragment {
        val fragment = RegistrationListFragment()
        fragment.setData(position, list)
        return fragment
    }

    override fun getItemCount(): Int = tabsList.size

    fun getPageTitle(position: Int): String = tabsList[position]
}

