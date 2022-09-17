package com.orangeink.profile

import android.content.Context
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
import com.orangeink.common.IEventHandler
import com.orangeink.common.UIEvent
import com.orangeink.common.constants.ScreenType
import com.orangeink.common.navigator.IAppNavigator
import com.orangeink.common.preferences.Prefs
import com.orangeink.profile.databinding.FragmentProfileBinding
import com.orangeink.utils.loadImage
import com.orangeink.utils.setData
import com.orangeink.utils.tryCast
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    @Inject
    lateinit var appNavigator: IAppNavigator

    private lateinit var binding: FragmentProfileBinding
    private lateinit var auth: FirebaseAuth

    private var user: FirebaseUser? = null
    private var listener: IEventHandler? = null

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
        auth = Firebase.auth
        setupUI()
        setListeners()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        tryCast<IEventHandler>(context) {
            listener = this
        }
    }

    fun setupUI() {
        auth.currentUser?.let {
            Prefs(requireContext()).user?.let { participant ->
                it.photoUrl?.let { photo ->
                    binding.ivProfile.loadImage(photo, com.orangeink.common.R.drawable.dummy_avatar)
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

    fun setupGuestMode() {
        binding.tvName.text = getString(com.orangeink.common.R.string.guest)

        binding.tvEmail.visibility = View.GONE
        binding.tvInstitution.visibility = View.GONE

        binding.btnLogin.visibility = View.VISIBLE
        binding.progressProfile.visibility = View.GONE

        binding.ivProfile.setImageResource(com.orangeink.common.R.drawable.dummy_avatar)
    }

    private fun setListeners() {
        binding.switchEventReminder.setOnCheckedChangeListener { _, b ->
            Prefs(requireContext()).notificationEventReminder = b
            if (b)
                Firebase.messaging.subscribeToTopic("event_reminder")
            else
                Firebase.messaging.unsubscribeFromTopic("event_reminder")
        }
        binding.switchAnnouncements.setOnCheckedChangeListener { _, b ->
            Prefs(requireContext()).notificationAnnouncements = b
            if (b)
                Firebase.messaging.subscribeToTopic("announcements")
            else
                Firebase.messaging.unsubscribeFromTopic("announcements")
        }

        binding.tvSponsors.setOnClickListener {
            startActivity(
                appNavigator.provideListActivity(requireContext(), ScreenType.VIEW_SPONSOR)
            )
        }
        binding.tvTeam.setOnClickListener {
            startActivity(
                appNavigator.provideListActivity(requireContext(), ScreenType.VIEW_TEAM)
            )
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
            auth.currentUser?.let {
                listener?.handleEvent(UIEvent.OpenProfileSetupBottomSheet(it))
            } ?: listener?.handleEvent(UIEvent.OpenLoginBottomSheet)
        }
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