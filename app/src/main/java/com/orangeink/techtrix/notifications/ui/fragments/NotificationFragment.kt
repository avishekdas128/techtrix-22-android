package com.orangeink.techtrix.notifications.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.orangeink.common.constants.Identifier
import com.orangeink.techtrix.R
import com.orangeink.techtrix.databinding.FragmentNotificationBinding
import com.orangeink.techtrix.misc.ui.ListActivity
import com.orangeink.techtrix.notifications.adapter.NotificationAdapter
import com.orangeink.network.model.Notification
import com.orangeink.techtrix.notifications.viewmodel.NotificationViewModel
import com.orangeink.common.preferences.Prefs
import com.orangeink.common.constants.ScreenType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationFragment : Fragment() {

    private lateinit var binding: FragmentNotificationBinding
    private val viewModel: NotificationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        setListeners()
        subscribeToLiveData()
    }

    private fun initViews() {
        Prefs(requireContext()).notifications?.let { setupUI(it) } ?: viewModel.getNotifications()
    }

    private fun subscribeToLiveData() {
        viewModel.notifications.observe(viewLifecycleOwner) { setupUI(it) }
        viewModel.error.observe(viewLifecycleOwner) {
            binding.progressNotifications.visibility = View.GONE
            binding.layoutEmpty.root.visibility = View.GONE
            binding.swNotifications.isRefreshing = false
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupUI(list: List<Notification>) {
        binding.progressNotifications.visibility = View.GONE
        binding.layoutEmpty.root.visibility = View.GONE
        binding.swNotifications.isRefreshing = false
        if (list.isNotEmpty()) {
            val notificationAdapter = NotificationAdapter(list)
            val lm = LinearLayoutManager(requireActivity())
            binding.rvNotifications.apply {
                layoutManager = lm
                adapter = notificationAdapter
                setHasFixedSize(true)
            }
            binding.rvNotifications.addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    lm.orientation
                )
            )
            binding.swNotifications.visibility = View.VISIBLE
        } else setupNoData()
    }

    private fun setListeners() {
        binding.swNotifications.setOnRefreshListener { viewModel.getNotifications() }
        binding.layoutEmpty.btnEmptyIllustration.setOnClickListener {
            Intent(requireActivity(), ListActivity::class.java).apply {
                putExtra(Identifier.SCREEN_TYPE, ScreenType.VIEW_ALL_EVENTS)
                startActivity(this)
            }
        }
    }

    private fun setupNoData() {
        binding.swNotifications.visibility = View.GONE
        binding.layoutEmpty.tvEmptyIllustration.text = getString(R.string.no_notifications)
        binding.layoutEmpty.btnEmptyIllustration.text = getString(R.string.check_all_events)
        binding.layoutEmpty.ivEmptyIllustration.setImageResource(R.drawable.no_notifications)

        binding.layoutEmpty.root.visibility = View.VISIBLE
    }

}