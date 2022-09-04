package com.orangeink.techtrix.event.ui

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.orangeink.techtrix.R
import com.orangeink.techtrix.databinding.ActivityEventBinding
import com.orangeink.techtrix.event.data.model.Event
import com.orangeink.techtrix.event.viewmodel.EventViewModel
import com.orangeink.techtrix.preferences.Prefs
import com.orangeink.techtrix.registrations.ui.bottomsheet.RegisterBottomSheet
import com.orangeink.techtrix.util.constants.Identifier
import com.orangeink.techtrix.util.findIcon
import dagger.hilt.android.AndroidEntryPoint

@Suppress("DEPRECATION")
@AndroidEntryPoint
class EventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEventBinding
    private val viewModel: EventViewModel by viewModels()

    private var eventId: Int? = null
    private var eventTile: String? = null
    private var eventCategory: String? = null

    private var data: Event? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getIntentData()
        initViews()
        setListeners()
        subscribeToLiveData()
    }

    private fun getIntentData() {
        intent.let {
            eventId = it.getIntExtra(Identifier.EVENT_ID, -1)
            eventTile = it.getStringExtra(Identifier.EVENT_TITLE)
            eventCategory = it.getStringExtra(Identifier.EVENT_CATEGORY)
        }
    }

    private fun initViews() {
        eventId?.let { viewModel.getEventDetails(it) }
        binding.tvHeading.text = eventTile
        binding.ivCategoryIcon.setImageDrawable(eventCategory?.findIcon(this))
    }

    private fun checkLoginStatus(data: Event) {
        Firebase.auth.currentUser?.let {
            Prefs(this).user?.let {
                binding.btnRegistration.visibility =
                    if (data.regEnabled!!) View.VISIBLE else View.GONE
                binding.tvEventInfo.text = data.info
                binding.tvEventInfo.visibility =
                    if (data.info.isNullOrBlank()) View.GONE else View.VISIBLE
            }
        } ?: setupGuestMode()
    }

    private fun setupGuestMode() {
        binding.btnRegistration.visibility = View.GONE
        binding.tvEventInfo.text = getString(R.string.no_reg_allowed)
        binding.tvEventInfo.visibility = View.VISIBLE
    }

    private fun setListeners() {
        binding.ivBack.setOnClickListener { finish() }
        binding.btnRegistration.setOnClickListener {
            data?.let {
                val bottomSheet = RegisterBottomSheet()
                bottomSheet.setData(it, object : RegisterBottomSheet.RegisterInterface {
                    override fun onRegistrationCompleted() {
                        Toast.makeText(
                            this@EventActivity,
                            getString(R.string.reg_success),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
                bottomSheet.show(supportFragmentManager, RegisterBottomSheet::javaClass.name)
            }
        }
    }

    private fun subscribeToLiveData() {
        viewModel.eventDetails.observe(this) {
            binding.progressEvent.visibility = View.GONE
            binding.loaderEventDescription.visibility = View.GONE
            binding.loaderEventRules.visibility = View.GONE
            binding.loaderEventContact.visibility = View.GONE
            setupUI(it)
            data = it
        }
        viewModel.error.observe(this) {
            binding.progressEvent.visibility = View.GONE
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupUI(data: Event) {
        binding.tvHeading.text = data.name
        binding.ivCategoryIcon.setImageDrawable(data.category?.findIcon(this))
        binding.tvEventDescription.movementMethod = LinkMovementMethod.getInstance()
        binding.tvEventRules.movementMethod = LinkMovementMethod.getInstance()
        binding.tvEventContact.movementMethod = LinkMovementMethod.getInstance()
        binding.tvEventDescription.setLinkTextColor(
            ContextCompat.getColor(this, R.color.purple_400)
        )
        binding.tvEventRules.setLinkTextColor(
            ContextCompat.getColor(this, R.color.purple_400)
        )
        binding.tvEventContact.setLinkTextColor(
            ContextCompat.getColor(this, R.color.purple_400)
        )
        binding.tvEventDescription.text =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(data.desc, Html.FROM_HTML_MODE_COMPACT)
            } else {
                Html.fromHtml(data.desc)
            }
        if (data.rules.isNullOrBlank()) {
            binding.tvRulesHeading.visibility = View.GONE
            binding.tvEventRules.visibility = View.GONE
        } else {
            binding.tvEventRules.text =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Html.fromHtml(data.rules, Html.FROM_HTML_MODE_COMPACT)
                } else {
                    Html.fromHtml(data.rules)
                }
            binding.tvRulesHeading.visibility = View.VISIBLE
            binding.tvEventRules.visibility = View.VISIBLE
        }
        binding.tvEventContact.text =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(data.contact, Html.FROM_HTML_MODE_COMPACT)
            } else {
                Html.fromHtml(data.contact)
            }
        binding.tvTagOne.text = data.tags.first()
        binding.tvTagTwo.text = data.tags[1]
        binding.tvTagThree.text = data.category
        checkLoginStatus(data)
    }
}