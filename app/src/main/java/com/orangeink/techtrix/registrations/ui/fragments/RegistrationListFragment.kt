package com.orangeink.techtrix.registrations.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.orangeink.common.constants.Identifier
import com.orangeink.techtrix.R
import com.orangeink.techtrix.databinding.FragmentRegistrationListBinding
import com.orangeink.techtrix.misc.ui.ListActivity
import com.orangeink.techtrix.registrations.adapter.RegistrationAdapter
import com.orangeink.network.model.Registration
import com.orangeink.common.constants.ScreenType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistrationListFragment : Fragment() {

    private lateinit var binding: FragmentRegistrationListBinding
    private lateinit var data: List<Registration>
    private var page: Int? = null

    fun setData(page: Int, data: List<Registration>) {
        this.page = page
        this.data = data
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegistrationListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        page?.let {
            val list = when (page) {
                0 -> data
                1 -> data.filter { value -> value.paid!! }
                2 -> data.filter { value -> !value.paid!! }
                else -> data
            }
            if (list.isNotEmpty()) {
                val registrationAdapter =
                    RegistrationAdapter(list, object : RegistrationAdapter.RegistrationInterface {
                        override fun onClick(item: Registration) {
                            //TODO
                        }
                    })
                binding.rvRegistration.apply {
                    layoutManager = LinearLayoutManager(requireActivity())
                    adapter = registrationAdapter
                    setHasFixedSize(true)
                    visibility = View.VISIBLE
                }
            } else setupNoData()
        }
    }

    private fun setupNoData() {
        binding.rvRegistration.visibility = View.GONE
        when (page) {
            0 -> binding.emptyLayout.tvEmptyIllustration.text = getString(R.string.no_reg_done)
            1 -> binding.emptyLayout.tvEmptyIllustration.text = getString(R.string.empty_paid_reg)
            2 -> binding.emptyLayout.tvEmptyIllustration.text = getString(R.string.empty_unpaid_reg)
        }
        binding.emptyLayout.btnEmptyIllustration.text = getString(R.string.check_all_events)
        binding.emptyLayout.btnEmptyIllustration.setOnClickListener {
            Intent(requireActivity(), ListActivity::class.java).apply {
                putExtra(Identifier.SCREEN_TYPE, ScreenType.VIEW_ALL_EVENTS)
                startActivity(this)
            }
        }
        binding.emptyLayout.ivEmptyIllustration.setImageResource(R.drawable.illustration_no_reg)
        binding.emptyLayout.root.visibility = View.VISIBLE
    }

}