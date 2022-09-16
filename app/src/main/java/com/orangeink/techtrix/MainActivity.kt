package com.orangeink.techtrix

import android.animation.Animator
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewAnimationUtils
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.orangeink.common.preferences.Prefs
import com.orangeink.registration.RegistrationsFragment
import com.orangeink.registration.ui.bottomsheet.QRBottomSheet
import com.orangeink.search.SearchFragment
import com.orangeink.techtrix.databinding.ActivityMainBinding
import com.orangeink.techtrix.login.ui.bottomsheet.LoginBottomSheet
import com.orangeink.techtrix.login.ui.bottomsheet.ProfileBottomSheet
import com.orangeink.techtrix.login.viewmodel.LoginViewModel
import com.orangeink.utils.hideKeyboard
import com.orangeink.utils.showKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), SearchFragment.SearchInterface,
    RegistrationsFragment.RegistrationInterface {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navHostFragment: NavHostFragment
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
        setListeners()
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
            navHostFragment.navController.currentDestination?.let { dest -> setupToolbar(dest) }
        }
    }

    private fun setupToolbar(destination: NavDestination) {
        when (destination.id) {
            R.id.navigation_home -> {
                binding.searchOpenView.visibility = View.INVISIBLE
                binding.ivMap.visibility = View.VISIBLE
                binding.ivSearch.visibility = View.VISIBLE
                binding.ivQr.visibility = View.GONE
                binding.llIcons.visibility = View.VISIBLE
                Firebase.auth.currentUser?.let {
                    Prefs(this).user?.let { participant ->
                        val firstName =
                            if (participant.name!!.contains(" "))
                                participant.name!!.split(" ")[0]
                            else
                                participant.name
                        binding.tvHeading.text = String.format("Hello, %s!", firstName)
                    }
                } ?: kotlin.run {
                    binding.tvHeading.text = getString(R.string.hello_guest)
                }
            }
            R.id.navigation_registrations -> {
                binding.searchOpenView.visibility = View.INVISIBLE
                Firebase.auth.currentUser?.let {
                    Prefs(this).user?.let {
                        binding.ivMap.visibility = View.GONE
                        binding.ivSearch.visibility = View.GONE
                        binding.ivQr.visibility = View.VISIBLE
                        binding.llIcons.visibility = View.VISIBLE
                    }
                } ?: kotlin.run { binding.llIcons.visibility = View.INVISIBLE }
                binding.tvHeading.text = getString(R.string.registrations)
            }
            R.id.navigation_notifications -> {
                binding.searchOpenView.visibility = View.INVISIBLE
                binding.llIcons.visibility = View.INVISIBLE
                binding.tvHeading.text = getString(R.string.notifications)
            }
            R.id.navigation_profile -> {
                binding.searchOpenView.visibility = View.INVISIBLE
                binding.llIcons.visibility = View.INVISIBLE
                binding.tvHeading.text = getString(R.string.profile)
            }
            R.id.navigation_search -> {
                binding.searchOpenView.visibility = View.VISIBLE
            }
        }
    }

    private fun setupBottomNav() {
        navHostFragment = supportFragmentManager.findFragmentById(R.id.main_fragment_container)
                as NavHostFragment
        navHostFragment.navController.addOnDestinationChangedListener { _, destination, _ ->
            setupToolbar(destination)
        }
        binding.bottomNavigation.apply {
            itemIconTintList = null
            setupWithNavController(this, navHostFragment.navController)
            setOnItemSelectedListener { item ->
                NavigationUI.onNavDestinationSelected(item, navHostFragment.navController)
                true
            }
            setOnItemReselectedListener {
                navHostFragment.navController.popBackStack(
                    destinationId = it.itemId,
                    inclusive = false
                )
                closeSearch()
            }
        }
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

    private fun setListeners() {
        binding.ivSearch.setOnClickListener { openSearch() }
        binding.ivCloseSearch.setOnClickListener { closeSearch() }
        binding.ivQr.setOnClickListener {
            val bottomSheet = QRBottomSheet()
            bottomSheet.show(supportFragmentManager, QRBottomSheet::class.java.name)
        }
        binding.ivMap.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(getString(com.orangeink.common.R.string.map_location))
            )
            startActivity(intent)
        }
    }

    private fun openSearch() {
        binding.searchInputText.setText("")
        binding.searchInputText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val fragment = supportFragmentManager.findFragmentById(R.id.main_fragment_container)
                if (fragment is SearchFragment)
                    fragment.search(binding.searchInputText.text.toString())
                return@setOnEditorActionListener true
            }
            false
        }
        binding.searchOpenView.visibility = View.VISIBLE
        val circularReveal = ViewAnimationUtils.createCircularReveal(
            binding.searchOpenView,
            (binding.ivSearch.right + binding.ivSearch.left) / 2,
            (binding.ivSearch.top + binding.ivSearch.bottom) / 2,
            0f, binding.rootFrameLayout.width.toFloat()
        )
        circularReveal.duration = 200
        circularReveal.start()
        navHostFragment.navController.navigate(R.id.navigation_search)
        showKeyboard(this, binding.searchInputText)
    }

    private fun closeSearch() {
        if (binding.searchOpenView.visibility == View.VISIBLE) {
            val circularConceal = ViewAnimationUtils.createCircularReveal(
                binding.searchOpenView,
                (binding.ivSearch.right + binding.ivSearch.left) / 2,
                (binding.ivSearch.top + binding.ivSearch.bottom) / 2,
                binding.rootFrameLayout.width.toFloat(), 0f
            )
            circularConceal.duration = 200
            circularConceal.start()
            circularConceal.addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator) = Unit
                override fun onAnimationCancel(animation: Animator) = Unit
                override fun onAnimationStart(animation: Animator) = Unit
                override fun onAnimationEnd(animation: Animator) {
                    hideKeyboard(this@MainActivity)
                    binding.searchOpenView.visibility = View.INVISIBLE
                    binding.searchInputText.setText("")
                    navHostFragment.navController.navigateUp()
                    circularConceal.removeAllListeners()
                }
            })
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        closeSearch()
    }

    override fun updateSearchQuery(query: String) {
        binding.searchInputText.setText(query)
    }

    override fun openLogin() {
        val bottomSheet = LoginBottomSheet()
        bottomSheet.setData(object : LoginBottomSheet.LoginInterface {
            override fun onLoginCompleted() {
                val fragment = supportFragmentManager.findFragmentById(R.id.main_fragment_container)
                if (fragment is RegistrationsFragment)
                    fragment.setupUI()
            }

            override fun profileSetupNeeded(user: FirebaseUser) {
                openProfileSetup(user)
            }
        })
        bottomSheet.show(supportFragmentManager, LoginBottomSheet::class.java.name)
    }

    private fun openProfileSetup(user: FirebaseUser) {
        val bottomSheet = ProfileBottomSheet()
        bottomSheet.setData(user, object : ProfileBottomSheet.ProfileInterface {
            override fun profileSetupCompleted() {
                user.photoUrl?.let {
                    loadProfileImage(it)
                }
                val fragment = supportFragmentManager.findFragmentById(R.id.main_fragment_container)
                if (fragment is RegistrationsFragment)
                    fragment.setupUI()
            }
        })
        bottomSheet.show(supportFragmentManager, ProfileBottomSheet::class.java.name)
    }

}