package com.orangeink.techtrix.category.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.orangeink.common.findIcon
import com.orangeink.techtrix.R
import com.orangeink.techtrix.category.viewmodel.CategoryViewModel
import com.orangeink.techtrix.databinding.ActivityCategoryBinding
import com.orangeink.techtrix.event.data.model.Event
import com.orangeink.techtrix.event.ui.EventActivity
import com.orangeink.techtrix.home.adapter.EventAdapter
import com.orangeink.techtrix.util.constants.Identifier
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoryBinding
    private lateinit var categoryName: String

    private val viewModel: CategoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getIntentData()
        initViews()
        setListeners()
        subscribeToLiveData()
    }

    private fun getIntentData() {
        intent?.let {
            categoryName = it.getStringExtra(Identifier.CATEGORY_ID).toString()
        }
    }

    private fun initViews() {
        binding.tvHeading.text = categoryName
        binding.ivCategoryIcon.setImageDrawable(
            ContextCompat.getDrawable(this, findIcon(categoryName))
        )
        viewModel.eventsByCategory(categoryName)
    }

    private fun subscribeToLiveData() {
        viewModel.events.observe(this) {
            binding.progressCategory.visibility = View.GONE
            binding.layoutEmpty.root.visibility = View.GONE
            binding.swCategory.isRefreshing = false
            setupAdapter(it)
        }
        viewModel.error.observe(this) {
            binding.progressCategory.visibility = View.GONE
            binding.layoutEmpty.root.visibility = View.GONE
            binding.swCategory.isRefreshing = false
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupAdapter(list: List<Event>) {
        if (list.isNotEmpty()) {
            val eventAdapter =
                EventAdapter(list, object : EventAdapter.EventInterface {
                    override fun onClick(item: Event) {
                        Intent(this@CategoryActivity, EventActivity::class.java).apply {
                            putExtra(Identifier.EVENT_ID, item.id)
                            putExtra(Identifier.EVENT_TITLE, item.name)
                            putExtra(Identifier.EVENT_CATEGORY, item.category)
                            startActivity(this)
                        }
                    }
                })
            binding.rvCategoryEvents.apply {
                layoutManager = LinearLayoutManager(this@CategoryActivity)
                setHasFixedSize(true)
                adapter = eventAdapter
                visibility = View.VISIBLE
            }
        } else {
            binding.rvCategoryEvents.visibility = View.GONE
            binding.layoutEmpty.btnEmptyIllustration.visibility = View.GONE
            binding.layoutEmpty.tvEmptyIllustration.text = getString(R.string.coming_soon)
            binding.layoutEmpty.ivEmptyIllustration.setImageResource(R.drawable.illustration_no_reg)
            binding.layoutEmpty.root.visibility = View.VISIBLE
        }
    }

    private fun setListeners() {
        binding.ivBack.setOnClickListener { finish() }
        binding.swCategory.setOnRefreshListener { viewModel.eventsByCategory(categoryName) }
    }
}