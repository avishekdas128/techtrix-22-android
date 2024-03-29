package com.orangeink.registration.ui.bottomsheet

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.children
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.orangeink.common.preferences.Prefs
import com.orangeink.design.RoundedBottomSheet
import com.orangeink.network.model.Registration
import com.orangeink.registration.R
import com.orangeink.registration.RegistrationViewModel
import com.orangeink.registration.databinding.BottomsheetRegisterBinding
import com.orangeink.utils.pxToDp
import com.orangeink.utils.showKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterBottomSheet : RoundedBottomSheet() {

    companion object {
        const val TAG = "RegisterBottomSheet"

        private const val EVENT_ID = "event_id"
        private const val EVENT_MIN_PARTICIPANT = "event_min_participant"
        private const val EVENT_MAX_PARTICIPANT = "event_max_participant"

        fun newInstance(
            eventId: Int,
            minParticipant: Int,
            maxParticipant: Int
        ): RegisterBottomSheet = RegisterBottomSheet().apply {
            arguments = Bundle().apply {
                putInt(EVENT_ID, eventId)
                putInt(EVENT_MIN_PARTICIPANT, minParticipant)
                putInt(EVENT_MAX_PARTICIPANT, maxParticipant)
            }
        }
    }

    private lateinit var binding: BottomsheetRegisterBinding
    private val viewModel: RegistrationViewModel by viewModels()

    private var eventId: Int = -1
    private var minParticipant: Int = 1
    private var maxParticipant: Int = 1

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener { mDialog: DialogInterface ->
            val d = mDialog as BottomSheetDialog
            val containerLayout =
                d.findViewById<FrameLayout>(com.google.android.material.R.id.container)
            val parent = binding.rlBottom.parent as ViewGroup
            parent.removeView(binding.rlBottom)
            val layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                requireContext().pxToDp(48f)
            )
            layoutParams.gravity = Gravity.BOTTOM
            containerLayout?.addView(binding.rlBottom, containerLayout.childCount)
            val bottomSheet =
                d.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet!!)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            bottomSheetBehavior.isDraggable = false
        }
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomsheetRegisterBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initArguments()
        initViews()
        setListeners()
        subscribeToLiveData()
    }

    private fun initArguments() {
        arguments?.let {
            eventId = it.getInt(EVENT_ID, -1)
            minParticipant = it.getInt(EVENT_MIN_PARTICIPANT, 1)
            maxParticipant = it.getInt(EVENT_MAX_PARTICIPANT, 1)
        }
    }

    private fun initViews() {
        val email = Prefs(requireContext()).user?.email
        addMember(email, binding.llMember)
        repeat(minParticipant - 1) { addMember(null, binding.llMember) }
        showKeyboard(requireContext(), binding.etTeamName)
        binding.etTeamName.requestFocus()
    }

    private fun setListeners() {
        binding.btnSave.setOnClickListener {
            if (validate()) {
                binding.progressProfile.visibility = View.VISIBLE
                viewModel.register(createRegistration())
            }
        }
        binding.ivAdd.setOnClickListener {
            if (binding.llMember.childCount < maxParticipant)
                addMember(null, binding.llMember)
            else
                Toast.makeText(
                    requireContext(),
                    "Maximum team size reached!",
                    Toast.LENGTH_SHORT
                ).show()
        }
        binding.ivRemove.setOnClickListener {
            if (binding.llMember.childCount > minParticipant)
                removeMember(binding.llMember)
            else
                Toast.makeText(
                    requireContext(),
                    "Minimum team size reached!",
                    Toast.LENGTH_SHORT
                ).show()
        }
    }

    @SuppressLint("InflateParams")
    private fun addMember(email: String?, parent: LinearLayout) {
        val view = layoutInflater.inflate(R.layout.layout_member_details, null)
        (view.findViewById<View>(R.id.tv_heading) as TextView).text =
            String.format("Member %d Email ID", parent.childCount + 1)
        email?.let { (view.findViewById<View>(R.id.et_member_email) as EditText).setText(it) }
        parent.addView(view)
    }

    private fun removeMember(parent: LinearLayout) {
        parent.removeViewAt(parent.childCount - 1)
    }

    private fun subscribeToLiveData() {
        viewModel.createdRegistration.observe(viewLifecycleOwner) {
            if (it.success!!) {
                binding.progressProfile.visibility = View.GONE
                Toast.makeText(
                    requireContext(),
                    getString(com.orangeink.common.R.string.reg_success),
                    Toast.LENGTH_SHORT
                ).show()
                dismiss()
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(com.orangeink.common.R.string.reg_failure),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        viewModel.error.observe(viewLifecycleOwner) {
            binding.progressProfile.visibility = View.GONE
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun validate(): Boolean {
        binding.llMember.children.forEach {
            if (it.findViewById<EditText>(R.id.et_member_email).text.isNullOrBlank()) {
                val member = it.findViewById<TextView>(R.id.tv_heading).text
                Toast.makeText(
                    requireContext(),
                    String.format("%s cannot be empty!", member),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }
        }
        return true
    }

    private fun createRegistration(): Registration {
        val participant = arrayListOf<String>()
        binding.llMember.children.forEach {
            participant.add(it.findViewById<EditText>(R.id.et_member_email).text.toString())
        }
        val teamName: String = if (!binding.etTeamName.text.isNullOrBlank())
            binding.etTeamName.text.toString()
        else
            participant.first()
        return Registration(id.toString(), participant, false, teamName)
    }
}