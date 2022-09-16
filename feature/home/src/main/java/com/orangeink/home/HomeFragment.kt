package com.orangeink.home

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aemerse.slider.listener.CarouselListener
import com.aemerse.slider.model.CarouselItem
import com.orangeink.common.constants.Constants
import com.orangeink.common.navigator.IAppNavigator
import com.orangeink.common.constants.ScreenType
import com.orangeink.common.preferences.Prefs
import com.orangeink.home.databinding.FragmentHomeBinding
import com.orangeink.home.adapter.CategoryAdapter
import com.orangeink.common.adapter.EventAdapter
import com.orangeink.network.model.Event
import com.orangeink.network.model.HomeResponse
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    @Inject
    lateinit var appNavigator: IAppNavigator

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    private var versionCode: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        setListeners()
        subscribeToLiveData()
    }

    @Suppress("DEPRECATION")
    private fun initViews() {
        try {
            val pInfo =
                requireActivity().packageManager.getPackageInfo(requireActivity().packageName, 0)
            versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                pInfo.longVersionCode.toInt()
            } else {
                pInfo.versionCode
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        Prefs(requireContext()).home?.let { setupUI(it) } ?: viewModel.loadHome(versionCode)
        setupCountDown()
    }

    private fun setListeners() {
        binding.tvShowAllEvents.setOnClickListener {
            startActivity(
                appNavigator.provideListActivity(requireContext(), ScreenType.VIEW_ALL_EVENTS)
            )
        }
        binding.swHome.setOnRefreshListener { viewModel.loadHome(versionCode) }
    }

    private fun subscribeToLiveData() {
        viewModel.homeResponse.observe(viewLifecycleOwner) { setupUI(it) }
        viewModel.error.observe(viewLifecycleOwner) {
            binding.progressHome.visibility = View.GONE
            binding.swHome.isRefreshing = false
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupUI(homeResponse: HomeResponse) {
        binding.progressHome.visibility = View.GONE
        binding.swHome.isRefreshing = false
        Prefs(requireContext()).home = homeResponse
        Prefs(requireContext()).trending = homeResponse.trending
        setupCategories(homeResponse.categories)
        setupPopularEvents(homeResponse.popular)
        setupCarousel(homeResponse.flagship)
        //if (homeResponse.updateRequired) ForceUpdateDialog(requireContext()).show()
    }

    private fun setupCountDown() {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        sdf.parse(getString(com.orangeink.common.R.string.event_start_date))?.time?.let {
            val timeLeft = it - Calendar.getInstance().timeInMillis
            if (timeLeft < 0) {
                binding.tvCountdown.visibility = View.GONE
                binding.countdownTimer.visibility = View.GONE
            } else binding.countdownTimer.start(timeLeft)
        }
        binding.countdownTimer.setOnCountdownEndListener {
            binding.tvCountdown.visibility = View.GONE
            binding.countdownTimer.visibility = View.GONE
        }
    }

    private fun setupCarousel(list: List<Event>) {
        if (list.isNotEmpty()) {
            val carouselList = mutableListOf<CarouselItem>()
            list.forEach {
                carouselList.add(
                    CarouselItem(
                        imageUrl = it.poster,
                        caption = it.id.toString()
                    )
                )
            }
            binding.carousel.setData(carouselList)
            binding.carousel.carouselListener = object : CarouselListener {
                override fun onClick(position: Int, carouselItem: CarouselItem) {
                    super.onClick(position, carouselItem)
                    carouselItem.caption?.toInt()?.let { id ->
                        startActivity(appNavigator.provideEventActivity(requireContext(), id))
                    }
                }
            }
        } else {
            binding.tvFlagship.visibility = View.GONE
            binding.carousel.visibility = View.GONE
        }
    }

    private fun setupCategories(list: List<String>) {
        binding.llLoaderCategory.visibility = View.GONE
        val categoryAdapter = CategoryAdapter(list, object : CategoryAdapter.CategoryInterface {
            override fun onClick(item: String) {
                startActivity(appNavigator.provideCategoryActivity(requireContext(), item))
            }
        })
        binding.rvCategories.apply {
            layoutManager = LinearLayoutManager(
                requireActivity(),
                RecyclerView.HORIZONTAL,
                false
            )
            setHasFixedSize(true)
            adapter = categoryAdapter
        }
    }

    private fun setupPopularEvents(list: List<Event>) {
        binding.llLoaderPopular.visibility = View.GONE
        val eventAdapter = EventAdapter(list, object : EventAdapter.EventInterface {
            override fun onClick(item: Event) {
                item.id?.let { id ->
                    startActivity(
                        appNavigator.provideEventActivity(
                            requireContext(),
                            id,
                            item.name,
                            item.category
                        )
                    )
                }
            }
        })
        eventAdapter.setViewType(Constants.VIEW_GRID)
        binding.rvPopularEvents.apply {
            layoutManager = GridLayoutManager(requireActivity(), 2)
            isNestedScrollingEnabled = false
            setHasFixedSize(true)
            adapter = eventAdapter
        }
    }

}