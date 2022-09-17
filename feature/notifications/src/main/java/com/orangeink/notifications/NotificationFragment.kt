package com.orangeink.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.orangeink.common.constants.ScreenType
import com.orangeink.common.navigator.IAppNavigator
import com.orangeink.common.preferences.Prefs
import com.orangeink.network.model.Notification
import com.orangeink.notifications.adapter.NotificationAdapter
import com.orangeink.notifications.databinding.FragmentNotificationBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NotificationFragment : Fragment() {

    @Inject
    lateinit var appNavigator: IAppNavigator

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
            startActivity(
                appNavigator.provideListActivity(requireContext(), ScreenType.VIEW_ALL_EVENTS)
            )
        }
    }

    private fun setupNoData() {
        binding.swNotifications.visibility = View.GONE
        binding.layoutEmpty.tvEmptyIllustration.text =
            getString(com.orangeink.common.R.string.no_notifications)
        binding.layoutEmpty.btnEmptyIllustration.text =
            getString(com.orangeink.common.R.string.check_all_events)
        binding.layoutEmpty.ivEmptyIllustration.setImageResource(R.drawable.no_notifications)

        binding.layoutEmpty.root.visibility = View.VISIBLE
    }

}