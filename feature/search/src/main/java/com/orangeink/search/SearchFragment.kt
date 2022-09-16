package com.orangeink.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.orangeink.common.navigator.IAppNavigator
import com.orangeink.common.adapter.EventAdapter
import com.orangeink.common.preferences.Prefs
import com.orangeink.network.model.Event
import com.orangeink.search.adapter.TrendingSearchAdapter
import com.orangeink.search.databinding.FragmentSearchBinding
import com.orangeink.utils.hideKeyboard
import com.orangeink.utils.tryCast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment() {

    @Inject
    lateinit var appNavigator: IAppNavigator

    private lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchViewModel by viewModels()

    private var searchInterface: SearchInterface? = null

    interface SearchInterface {
        fun updateSearchQuery(query: String)
    }

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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        tryCast<SearchInterface>(context) {
            searchInterface = this
        }
    }

    private fun setupTrendingSearch() {
        val list = Prefs(requireContext()).trending
        list?.let {
            val trendingAdapter =
                TrendingSearchAdapter(list, object : TrendingSearchAdapter.TrendingInterface {
                    override fun onClick(item: String) {
                        searchInterface?.updateSearchQuery(item)
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
            val eventAdapter = EventAdapter(list, object : EventAdapter.EventInterface {
                override fun onClick(item: Event) {
                    item.id?.let { id ->
                        appNavigator.provideEventActivity(
                            requireContext(),
                            id,
                            item.name,
                            item.category
                        )
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
            binding.layoutEmpty.tvEmptyIllustration.text =
                getString(com.orangeink.common.R.string.no_result_found)
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