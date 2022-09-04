package com.orangeink.techtrix.search.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.orangeink.techtrix.R
import com.orangeink.techtrix.databinding.FragmentSearchBinding
import com.orangeink.techtrix.event.ui.EventActivity
import com.orangeink.techtrix.event.data.model.Event
import com.orangeink.techtrix.home.adapter.EventAdapter
import com.orangeink.techtrix.home.ui.fragments.HomeFragment
import com.orangeink.techtrix.preferences.Prefs
import com.orangeink.techtrix.search.adapter.TrendingSearchAdapter
import com.orangeink.techtrix.search.viewmodel.SearchViewModel
import com.orangeink.techtrix.util.constants.Identifier
import com.orangeink.techtrix.util.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTrendingSearch()
        subscribeToLiveData()
    }

    private fun setupTrendingSearch() {
        val list = Prefs(requireContext()).trending
        list?.let {
            val trendingAdapter =
                TrendingSearchAdapter(list, object : TrendingSearchAdapter.TrendingInterface {
                    override fun onClick(item: String) {
                        (requireParentFragment() as HomeFragment).searchQuery(item)
                        search(item)
                    }
                })
            binding.rvSearch.apply {
                layoutManager = LinearLayoutManager(requireActivity())
                setHasFixedSize(true)
                adapter = trendingAdapter
            }
        }
    }

    private fun setupSearchResults(list: List<Event>) {
        if (list.isNotEmpty()) {
            val eventAdapter =
                EventAdapter(list, object : EventAdapter.EventInterface {
                    override fun onClick(item: Event) {
                        Intent(requireActivity(), EventActivity::class.java).apply {
                            putExtra(Identifier.EVENT_ID, item.id)
                            putExtra(Identifier.EVENT_TITLE, item.name)
                            putExtra(Identifier.EVENT_CATEGORY, item.category)
                            startActivity(this)
                        }
                    }
                })
            binding.rvSearch.apply {
                layoutManager = LinearLayoutManager(requireActivity())
                setHasFixedSize(true)
                adapter = eventAdapter
                visibility = View.VISIBLE
            }
        } else {
            binding.rvSearch.visibility = View.GONE
            binding.layoutEmpty.btnEmptyIllustration.visibility = View.GONE
            binding.layoutEmpty.tvEmptyIllustration.text = getString(R.string.no_result_found)
            binding.layoutEmpty.ivEmptyIllustration.setImageResource(R.drawable.illustration_no_result_found)
            binding.layoutEmpty.root.visibility = View.VISIBLE
        }
    }

    private fun subscribeToLiveData() {
        viewModel.search.observe(viewLifecycleOwner) {
            binding.progressSearch.visibility = View.GONE
            binding.layoutEmpty.root.visibility = View.GONE
            setupSearchResults(it)
        }
        viewModel.error.observe(viewLifecycleOwner) {
            binding.progressSearch.visibility = View.GONE
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
    }

    fun search(query: String) {
        hideKeyboard(requireActivity())
        binding.progressSearch.visibility = View.VISIBLE
        binding.tvTrending.visibility = View.GONE
        binding.rvSearch.visibility = View.GONE
        viewModel.search(query)
    }

}