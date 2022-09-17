package com.orangeink.category

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.orangeink.category.databinding.ActivityCategoryBinding
import com.orangeink.common.adapter.EventAdapter
import com.orangeink.common.constants.Identifier
import com.orangeink.common.findIcon
import com.orangeink.common.navigator.IAppNavigator
import com.orangeink.network.model.Event
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CategoryActivity : AppCompatActivity() {

    @Inject
    lateinit var appNavigator: IAppNavigator

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
                        item.id?.let { id ->
                            appNavigator.provideEventActivity(
                                this@CategoryActivity,
                                id,
                                item.name,
                                item.category
                            )
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
            binding.layoutEmpty.tvEmptyIllustration.text =
                getString(com.orangeink.common.R.string.coming_soon)
            binding.layoutEmpty.ivEmptyIllustration.setImageResource(com.orangeink.common.R.drawable.illustration_no_reg)
            binding.layoutEmpty.root.visibility = View.VISIBLE
        }
    }

    private fun setListeners() {
        binding.ivBack.setOnClickListener { finish() }
        binding.swCategory.setOnRefreshListener { viewModel.eventsByCategory(categoryName) }
    }
}