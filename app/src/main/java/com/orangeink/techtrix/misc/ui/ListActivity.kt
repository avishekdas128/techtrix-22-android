package com.orangeink.techtrix.misc.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.orangeink.techtrix.R
import com.orangeink.techtrix.databinding.ActivityListBinding
import com.orangeink.techtrix.event.data.model.Event
import com.orangeink.techtrix.event.ui.EventActivity
import com.orangeink.techtrix.home.adapter.EventAdapter
import com.orangeink.techtrix.misc.adapter.SponsorAdapter
import com.orangeink.techtrix.misc.adapter.TeamAdapter
import com.orangeink.techtrix.misc.viewmodel.ListViewModel
import com.orangeink.techtrix.util.constants.Identifier
import com.orangeink.techtrix.util.constants.ScreenType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListBinding
    private val viewModel: ListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
        setListeners()
        subscribeToLiveData()
    }

    private fun initViews() {
        intent?.let {
            when (it.getIntExtra(Identifier.SCREEN_TYPE, -1)) {
                ScreenType.VIEW_ALL_EVENTS -> setupAllEvent()
                ScreenType.VIEW_SPONSOR -> setupSponsor()
                ScreenType.VIEW_TEAM -> setupTeam()
            }
        }
    }

    private fun setupAllEvent() {
        viewModel.getAllEvents()
        binding.tvHeading.text = getString(R.string.all_events)
    }

    private fun setupSponsor() {
        viewModel.getAllSponsors()
        binding.tvHeading.text = getString(R.string.sponsors)
    }

    private fun setupTeam() {
        viewModel.getAllTeam()
        binding.tvHeading.text = getString(R.string.team)
    }

    private fun setListeners() {
        binding.ivBack.setOnClickListener { finish() }
        binding.swList.setOnRefreshListener { initViews() }
    }

    private fun subscribeToLiveData() {
        viewModel.events.observe(this) {
            binding.progressList.visibility = View.GONE
            binding.layoutEmpty.root.visibility = View.GONE
            binding.swList.isRefreshing = false
            if (it.isNotEmpty()) {
                val eventAdapter = EventAdapter(it, object : EventAdapter.EventInterface {
                    override fun onClick(item: Event) {
                        Intent(this@ListActivity, EventActivity::class.java).apply {
                            putExtra(Identifier.EVENT_ID, item.id)
                            putExtra(Identifier.EVENT_TITLE, item.name)
                            putExtra(Identifier.EVENT_CATEGORY, item.category)
                            startActivity(this)
                        }
                    }
                })
                binding.rvList.apply {
                    layoutManager = LinearLayoutManager(this@ListActivity)
                    adapter = eventAdapter
                    setHasFixedSize(true)
                }
            } else setupNoData()
        }
        viewModel.sponsors.observe(this) {
            binding.progressList.visibility = View.GONE
            binding.layoutEmpty.root.visibility = View.GONE
            binding.swList.isRefreshing = false
            if (it.isNotEmpty()) {
                val sponsorAdapter = SponsorAdapter(it)
                binding.rvList.apply {
                    layoutManager = LinearLayoutManager(this@ListActivity)
                    adapter = sponsorAdapter
                    setHasFixedSize(true)
                }
            } else setupNoData()
        }
        viewModel.teams.observe(this) {
            binding.progressList.visibility = View.GONE
            binding.layoutEmpty.root.visibility = View.GONE
            binding.swList.isRefreshing = false
            if (it.isNotEmpty()) {
                val teamAdapter = TeamAdapter(it)
                binding.rvList.apply {
                    layoutManager = LinearLayoutManager(this@ListActivity)
                    adapter = teamAdapter
                    setHasFixedSize(true)
                }
            } else setupNoData()
        }
        viewModel.error.observe(this) {
            binding.progressList.visibility = View.GONE
            binding.layoutEmpty.root.visibility = View.GONE
            binding.swList.isRefreshing = false
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupNoData() {
        binding.layoutEmpty.btnEmptyIllustration.visibility = View.GONE
        binding.layoutEmpty.tvEmptyIllustration.text = getString(R.string.coming_soon)
        binding.layoutEmpty.ivEmptyIllustration.setImageResource(R.drawable.illustration_no_reg)

        binding.layoutEmpty.root.visibility = View.VISIBLE
    }

}