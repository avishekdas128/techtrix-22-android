package com.orangeink.techtrix.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.orangeink.techtrix.R

fun loadFragment(fragmentManager: FragmentManager, frame_id: Int, fragment: Fragment?) {
    if (fragment != null) {
        fragmentManager.beginTransaction()
            .replace(frame_id, fragment)
            .commit()
    }
}

fun addFragment(fragmentManager: FragmentManager, frame_id: Int, fragment: Fragment?) {
    if (fragment != null) {
        fragmentManager.beginTransaction()
            .add(frame_id, fragment)
            .addToBackStack(fragment.javaClass.name)
            .commit()
    }
}

fun addFragmentFromBottom(fragmentManager: FragmentManager, frame_id: Int, fragment: Fragment?) {
    if (fragment != null) {
        fragmentManager.beginTransaction()
            .add(frame_id, fragment)
            .setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_down)
            .addToBackStack(fragment.javaClass.name)
            .commit()
    }
}