package com.orangeink.techtrix.profile.ui.fragment

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.orangeink.techtrix.R
import com.orangeink.techtrix.common.MainActivity
import com.orangeink.techtrix.databinding.FragmentProfileBinding
import com.orangeink.techtrix.login.ui.bottomsheet.LoginBottomSheet
import com.orangeink.techtrix.login.ui.bottomsheet.ProfileBottomSheet
import com.orangeink.techtrix.misc.ui.ListActivity
import com.orangeink.techtrix.preferences.Prefs
import com.orangeink.techtrix.profile.ui.bottomsheet.ProfileEditBottomSheet
import com.orangeink.techtrix.profile.ui.dialog.LogoutDialog
import com.orangeink.techtrix.util.constants.Identifier
import com.orangeink.techtrix.util.constants.ScreenType
import com.orangeink.utils.loadImage
import com.orangeink.utils.setData
import timber.log.Timber

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var auth: FirebaseAuth

    private var user: FirebaseUser? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        setListeners()
    }

    private fun initViews() {
        auth = Firebase.auth
        setupUI()
    }

    private fun setupUI() {
        auth.currentUser?.let {
            Prefs(requireContext()).user?.let { participant ->
                binding.ivLogout.visibility = View.VISIBLE
                binding.ivEdit.visibility = View.VISIBLE

                it.photoUrl?.let { photo ->
                    binding.ivProfile.loadImage(photo, R.drawable.dummy_avatar)
                }
                binding.tvName.text = participant.name

                binding.tvEmail.setData(participant.email)
                binding.tvInstitution.setData(participant.institution)

                binding.btnLogin.visibility = View.GONE
                binding.progressProfile.visibility = View.GONE
            }
            user = it
        }
        binding.switchEventReminder.isChecked = Prefs(requireContext()).notificationEventReminder
        binding.switchAnnouncements.isChecked = Prefs(requireContext()).notificationAnnouncements
        setupVersion()
    }

    private fun setupGuestMode() {
        binding.ivLogout.visibility = View.GONE
        binding.ivEdit.visibility = View.GONE

        binding.tvName.text = getString(R.string.guest)

        binding.tvEmail.visibility = View.GONE
        binding.tvInstitution.visibility = View.GONE

        binding.btnLogin.visibility = View.VISIBLE
        binding.progressProfile.visibility = View.GONE

        binding.ivProfile.setImageResource(R.drawable.dummy_avatar)
        (requireActivity() as MainActivity).removeProfileImage()
    }

    private fun setListeners() {
        binding.switchEventReminder.setOnCheckedChangeListener { _, b ->
            Prefs(requireContext()).notificationEventReminder = b
            if(b)
                Firebase.messaging.subscribeToTopic("event_reminder")
            else
                Firebase.messaging.unsubscribeFromTopic("event_reminder")
        }
        binding.switchAnnouncements.setOnCheckedChangeListener { _, b ->
            Prefs(requireContext()).notificationAnnouncements = b
            if(b)
                Firebase.messaging.subscribeToTopic("announcements")
            else
                Firebase.messaging.unsubscribeFromTopic("announcements")
        }
        binding.ivEdit.setOnClickListener {
            val bottomSheet = ProfileEditBottomSheet()
            bottomSheet.setData(object : ProfileEditBottomSheet.ProfileInterface {
                override fun profileEditCompleted() {
                    setupUI()
                }
            })
            bottomSheet.show(childFragmentManager, ProfileEditBottomSheet::javaClass.name)
        }
        binding.ivLogout.setOnClickListener {
            LogoutDialog(requireActivity(), object : LogoutDialog.DialogInterface {
                override fun onLogoutClicked() {
                    binding.progressProfile.visibility = View.VISIBLE
                    auth.signOut()
                    Prefs(requireContext()).logout()
                    setupGuestMode()
                }
            }).show()
        }
        binding.tvSponsors.setOnClickListener {
            Intent(requireActivity(), ListActivity::class.java).apply {
                putExtra(Identifier.SCREEN_TYPE, ScreenType.VIEW_SPONSOR)
                startActivity(this)
            }
        }
        binding.tvTeam.setOnClickListener {
            Intent(requireActivity(), ListActivity::class.java).apply {
                putExtra(Identifier.SCREEN_TYPE, ScreenType.VIEW_TEAM)
                startActivity(this)
            }
        }
        binding.ivFacebook.setOnClickListener {
            try {
                requireActivity().packageManager.getPackageInfo("com.facebook.katana", 0)
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("fb://page/techtrix.rcciit")
                    )
                )
            } catch (e: Exception) {
                Timber.e(e)
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://www.facebook.com/techtrix.rcciit")
                    )
                )
            }
        }
        binding.ivWeb.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://techtrixrcciit.tech")
            )
            startActivity(intent)
        }
        binding.ivInstagram.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.instagram.com/techtrix.rcciit/")
            )
            startActivity(intent)
        }
        binding.btnLogin.setOnClickListener {
            auth.currentUser?.let { openProfileSetup(it) } ?: openLogin()
        }
    }

    private fun openLogin() {
        val bottomSheet = LoginBottomSheet()
        bottomSheet.setData(object : LoginBottomSheet.LoginInterface {
            override fun onLoginCompleted() {
                auth.currentUser?.photoUrl?.let {
                    (requireActivity() as MainActivity).loadProfileImage(it)
                }
                setupUI()
            }

            override fun profileSetupNeeded(user: FirebaseUser) {
                openProfileSetup(user)
            }
        })
        bottomSheet.show(childFragmentManager, LoginBottomSheet::class.java.name)
    }

    private fun openProfileSetup(user: FirebaseUser) {
        val bottomSheet = ProfileBottomSheet()
        bottomSheet.setData(user, object : ProfileBottomSheet.ProfileInterface {
            override fun profileSetupCompleted() {
                auth.currentUser?.photoUrl?.let {
                    (requireActivity() as MainActivity).loadProfileImage(it)
                }
                setupUI()
            }
        })
        bottomSheet.show(childFragmentManager, ProfileBottomSheet::class.java.name)
    }

    private fun setupVersion() {
        try {
            val pInfo =
                requireActivity().packageManager.getPackageInfo(requireActivity().packageName, 0)
            binding.tvVersion.text = pInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }

}