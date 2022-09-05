package com.orangeink.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

fun loadFragment(fragmentManager: FragmentManager, frame_id: Int, fragment: Fragment?) {
    if (fragment != null) {
        fragmentManager.beginTransaction()
            .replace(frame_id, fragment)
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