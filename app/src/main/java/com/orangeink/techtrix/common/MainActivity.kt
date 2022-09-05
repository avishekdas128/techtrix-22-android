package com.orangeink.techtrix.common

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.orangeink.techtrix.R
import com.orangeink.techtrix.databinding.ActivityMainBinding
import com.orangeink.techtrix.home.ui.fragments.HomeFragment
import com.orangeink.techtrix.login.viewmodel.LoginViewModel
import com.orangeink.techtrix.preferences.Prefs
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navHostFragment: NavHostFragment
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
        subscribeToLiveData()
    }

    override fun onResume() {
        super.onResume()
        Firebase.auth.currentUser?.let {
            Prefs(this).user?.let { participant ->
                it.photoUrl?.let { uri -> loadProfileImage(uri) }
                viewModel.getProfile(participant.email!!)
            }
        }
    }

    private fun initViews() {
        setupBottomNav()
        Firebase.messaging.subscribeToTopic("event_reminder").addOnCompleteListener {
            Prefs(this).notificationEventReminder = it.isSuccessful
        }
        Firebase.messaging.subscribeToTopic("announcements").addOnCompleteListener {
            Prefs(this).notificationAnnouncements = it.isSuccessful
        }
    }

    private fun subscribeToLiveData() {
        viewModel.generalFees.observe(this) {
            Prefs(this).user = it
            val fragment = navHostFragment.childFragmentManager.fragments.first()
            if (fragment is HomeFragment)
                fragment.setupUI()
        }
    }

    private fun setupBottomNav() {
        binding.bottomNavigation.itemIconTintList = null
        navHostFragment = supportFragmentManager.findFragmentById(R.id.main_fragment_container)
                as NavHostFragment
        setupWithNavController(binding.bottomNavigation, navHostFragment.navController)
    }

    fun loadProfileImage(uri: Uri) {
        Glide.with(this)
            .asBitmap()
            .load(uri)
            .apply(
                RequestOptions
                    .circleCropTransform()
                    .placeholder(R.drawable.dummy_avatar)
                    .error(R.drawable.dummy_avatar)
            )
            .into((object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    val menuItem = binding.bottomNavigation.menu.findItem(R.id.navigation_profile)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        menuItem?.iconTintList = null
                        menuItem?.iconTintMode = null
                    }
                    menuItem?.icon = BitmapDrawable(resources, resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            }))
    }

    fun removeProfileImage() {
        val menuItem = binding.bottomNavigation.menu.findItem(R.id.navigation_profile)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            menuItem?.iconTintList = null
            menuItem?.iconTintMode = null
        }
        menuItem?.icon = ContextCompat.getDrawable(this, R.drawable.dummy_avatar)
    }

}