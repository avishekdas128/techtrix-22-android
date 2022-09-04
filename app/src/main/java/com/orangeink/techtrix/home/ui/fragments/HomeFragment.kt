package com.orangeink.techtrix.home.ui.fragments

import android.animation.Animator
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.orangeink.techtrix.R
import com.orangeink.techtrix.databinding.FragmentHomeBinding
import com.orangeink.techtrix.preferences.Prefs
import com.orangeink.techtrix.search.ui.fragments.SearchFragment
import com.orangeink.techtrix.util.addFragmentFromBottom
import com.orangeink.techtrix.util.hideKeyboard
import com.orangeink.techtrix.util.loadFragment
import com.orangeink.techtrix.util.showKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

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
        setupUI()
        setListeners()
    }

    private fun initViews() {
        loadFragment(childFragmentManager, R.id.content_fragment, DashboardFragment())
    }

    private fun setListeners() {
        binding.ivSearch.setOnClickListener { openSearch() }
        binding.ivCloseSearch.setOnClickListener { closeSearch() }
        binding.ivMap.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.map_location)))
            startActivity(intent)
        }
    }

    fun setupUI() {
        Firebase.auth.currentUser?.let {
            Prefs(requireContext()).user?.let { participant ->
                val firstName =
                    if (participant.name!!.contains(" "))
                        participant.name!!.split(" ")[0]
                    else
                        participant.name
                binding.tvHeading.text = String.format("Hello, %s!", firstName)
            }
        } ?: setupGuestMode()
    }

    private fun setupGuestMode() {
        binding.tvHeading.text = getString(R.string.hello_guest)
    }

    private fun openSearch() {
        binding.searchInputText.setText("")
        binding.searchInputText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val fragment = childFragmentManager.findFragmentById(R.id.content_fragment)
                if (fragment is SearchFragment)
                    fragment.search(binding.searchInputText.text.toString())
                return@setOnEditorActionListener true
            }
            false
        }
        binding.searchOpenView.visibility = View.VISIBLE
        val circularReveal = ViewAnimationUtils.createCircularReveal(
            binding.searchOpenView,
            (binding.ivSearch.right + binding.ivSearch.left) / 2,
            (binding.ivSearch.top + binding.ivSearch.bottom) / 2,
            0f, binding.rootFrameLayout.width.toFloat()
        )
        circularReveal.duration = 200
        circularReveal.start()
        addFragmentFromBottom(
            childFragmentManager,
            R.id.content_fragment,
            SearchFragment()
        )
        showKeyboard(requireActivity(), binding.searchInputText)
    }

    private fun closeSearch() {
        val circularConceal = ViewAnimationUtils.createCircularReveal(
            binding.searchOpenView,
            (binding.ivSearch.right + binding.ivSearch.left) / 2,
            (binding.ivSearch.top + binding.ivSearch.bottom) / 2,
            binding.rootFrameLayout.width.toFloat(), 0f
        )
        circularConceal.duration = 200
        circularConceal.start()
        circularConceal.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator) = Unit
            override fun onAnimationCancel(animation: Animator) = Unit
            override fun onAnimationStart(animation: Animator) = Unit
            override fun onAnimationEnd(animation: Animator) {
                hideKeyboard(requireActivity())
                binding.searchOpenView.visibility = View.INVISIBLE
                binding.searchInputText.setText("")
                childFragmentManager.popBackStack()
                circularConceal.removeAllListeners()
            }
        })
    }

    fun searchQuery(text: String) {
        binding.searchInputText.setText(text)
    }

}