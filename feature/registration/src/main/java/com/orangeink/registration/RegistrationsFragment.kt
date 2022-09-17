package com.orangeink.registration

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.orangeink.common.IEventHandler
import com.orangeink.common.UIEvent
import com.orangeink.common.preferences.Prefs
import com.orangeink.network.model.Registration
import com.orangeink.registration.adapter.RegistrationPagerAdapter
import com.orangeink.registration.databinding.FragmentRegistrationsBinding
import com.orangeink.utils.tryCast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistrationsFragment : Fragment() {

    private lateinit var binding: FragmentRegistrationsBinding
    private val viewModel: RegistrationViewModel by viewModels()

    private var listener: IEventHandler? = null

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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        tryCast<IEventHandler>(context) {
            listener = this
        }
    }

    fun setupUI() {
        Firebase.auth.currentUser?.let {
            Prefs(requireContext()).user?.let { participant ->
                participant.email?.let {
                    viewModel.getRegistrations(it)
                }
                binding.rlRegistrations.visibility = View.VISIBLE
                binding.tvPaid.text =
                    if (participant.generalFees!!)
                        getString(com.orangeink.common.R.string.paid)
                    else
                        getString(com.orangeink.common.R.string.unpaid)

                binding.layoutEmpty.root.visibility = View.GONE
            }
        } ?: setupNoLogin()
    }

    private fun setupNoLogin() {
        binding.progressRegistrations.visibility = View.GONE
        binding.layoutEmpty.ivEmptyIllustration
            .setImageResource(com.orangeink.common.R.drawable.illustration_login)
        binding.layoutEmpty.btnEmptyIllustration.text =
            getString(com.orangeink.common.R.string.login)
        binding.layoutEmpty.tvEmptyIllustration.text =
            getString(com.orangeink.common.R.string.no_login)

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
        binding.layoutEmpty.btnEmptyIllustration.setOnClickListener {
            listener?.handleEvent(UIEvent.OpenLoginBottomSheet)
        }
    }

}