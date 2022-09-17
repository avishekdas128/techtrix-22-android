package com.orangeink.event

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
import com.orangeink.common.constants.Identifier
import com.orangeink.common.findIcon
import com.orangeink.common.navigator.IAppNavigator
import com.orangeink.common.preferences.Prefs
import com.orangeink.event.databinding.ActivityEventBinding
import com.orangeink.network.model.Event
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EventActivity : AppCompatActivity() {

    @Inject
    lateinit var appNavigator: IAppNavigator

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
        eventCategory?.let {
            binding.ivCategoryIcon.setImageDrawable(
                ContextCompat.getDrawable(this@EventActivity, findIcon(it))
            )
        }
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
        binding.tvEventInfo.text = getString(com.orangeink.common.R.string.no_reg_allowed)
        binding.tvEventInfo.visibility = View.VISIBLE
    }

    private fun setListeners() {
        binding.ivBack.setOnClickListener { finish() }
        binding.btnRegistration.setOnClickListener {
            data?.let {
                it.id?.let { id ->
                    appNavigator.provideRegisterBottomSheet(
                        supportFragmentManager,
                        id,
                        it.minParticipant,
                        it.maxParticipant
                    )
                }
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
        data.category?.let {
            binding.ivCategoryIcon.setImageDrawable(
                ContextCompat.getDrawable(this@EventActivity, findIcon(it))
            )
        }
        binding.tvEventDescription.movementMethod = LinkMovementMethod.getInstance()
        binding.tvEventRules.movementMethod = LinkMovementMethod.getInstance()
        binding.tvEventContact.movementMethod = LinkMovementMethod.getInstance()
        binding.tvEventDescription.setLinkTextColor(
            ContextCompat.getColor(this, com.orangeink.design.R.color.purple_400)
        )
        binding.tvEventRules.setLinkTextColor(
            ContextCompat.getColor(this, com.orangeink.design.R.color.purple_400)
        )
        binding.tvEventContact.setLinkTextColor(
            ContextCompat.getColor(this, com.orangeink.design.R.color.purple_400)
        )
        binding.tvEventDescription.text = Html.fromHtml(data.desc, Html.FROM_HTML_MODE_COMPACT)
        if (data.rules.isNullOrBlank()) {
            binding.tvRulesHeading.visibility = View.GONE
            binding.tvEventRules.visibility = View.GONE
        } else {
            binding.tvEventRules.text = Html.fromHtml(data.rules, Html.FROM_HTML_MODE_COMPACT)
            binding.tvRulesHeading.visibility = View.VISIBLE
            binding.tvEventRules.visibility = View.VISIBLE
        }
        binding.tvEventContact.text = Html.fromHtml(data.contact, Html.FROM_HTML_MODE_COMPACT)
        binding.tvTagOne.text = data.tags.first()
        binding.tvTagTwo.text = data.tags[1]
        binding.tvTagThree.text = data.category
        checkLoginStatus(data)
    }
}