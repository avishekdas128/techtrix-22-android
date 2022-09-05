package com.orangeink.techtrix.registrations.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.orangeink.techtrix.R
import com.orangeink.techtrix.common.MainActivity
import com.orangeink.techtrix.databinding.FragmentRegistrationsBinding
import com.orangeink.techtrix.login.ui.bottomsheet.LoginBottomSheet
import com.orangeink.techtrix.login.ui.bottomsheet.ProfileBottomSheet
import com.orangeink.techtrix.preferences.Prefs
import com.orangeink.techtrix.registrations.adapter.RegistrationPagerAdapter
import com.orangeink.techtrix.registrations.data.model.Registration
import com.orangeink.techtrix.registrations.ui.bottomsheet.QRBottomSheet
import com.orangeink.techtrix.registrations.viewmodel.RegistrationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistrationsFragment : Fragment() {

    private lateinit var binding: FragmentRegistrationsBinding

    private val viewModel: RegistrationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegistrationsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setListeners()
        subscribeToLiveData()
    }

    private fun setupUI() {
        Firebase.auth.currentUser?.let {
            Prefs(requireContext()).user?.let { participant ->
                participant.email?.let {
                    viewModel.getRegistrations(it)
                }
                binding.ivQr.visibility = View.VISIBLE
                binding.rlRegistrations.visibility = View.VISIBLE
                binding.tvPaid.text =
                    if (participant.generalFees!!)
                        getString(R.string.paid)
                    else
                        getString(R.string.unpaid)

                binding.layoutEmpty.root.visibility = View.GONE
            }
        } ?: setupNoLogin()
    }

    private fun setupNoLogin() {
        binding.progressRegistrations.visibility = View.GONE
        binding.layoutEmpty.btnEmptyIllustration.text = getString(R.string.login)
        binding.layoutEmpty.tvEmptyIllustration.text = getString(R.string.no_login)

        binding.layoutEmpty.root.visibility = View.VISIBLE
    }

    private fun setupViewPager(list: List<Registration>) {
        val pagerAdapter = RegistrationPagerAdapter(
            childFragmentManager,
            lifecycle,
            mutableListOf("All", "Paid", "Unpaid"),
            list
        )
        binding.registrationsVp.apply {
            adapter = pagerAdapter
            offscreenPageLimit = 2
        }
        binding.tabLayout.setupMediator(binding.registrationsVp, pagerAdapter.getPageTitle)
    }

    private fun subscribeToLiveData() {
        viewModel.registrations.observe(viewLifecycleOwner) {
            binding.progressRegistrations.visibility = View.GONE
            setupViewPager(it)
        }
        viewModel.error.observe(viewLifecycleOwner) {
            binding.progressRegistrations.visibility = View.GONE
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setListeners() {
        binding.layoutEmpty.btnEmptyIllustration.setOnClickListener { openLogin() }
        binding.ivQr.setOnClickListener {
            val bottomSheet = QRBottomSheet()
            bottomSheet.show(childFragmentManager, QRBottomSheet::class.java.name)
        }
    }

    private fun openLogin() {
        val bottomSheet = LoginBottomSheet()
        bottomSheet.setData(object : LoginBottomSheet.LoginInterface {
            override fun onLoginCompleted() {
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
                user.photoUrl?.let {
                    (requireActivity() as MainActivity).loadProfileImage(it)
                }
                setupUI()
            }
        })
        bottomSheet.show(childFragmentManager, ProfileBottomSheet::class.java.name)
    }

}