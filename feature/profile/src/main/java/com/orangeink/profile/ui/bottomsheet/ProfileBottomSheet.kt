package com.orangeink.profile.ui.bottomsheet

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.orangeink.common.IEventHandler
import com.orangeink.common.UIEvent
import com.orangeink.common.preferences.Prefs
import com.orangeink.design.RoundedBottomSheet
import com.orangeink.network.model.Participant
import com.orangeink.profile.ProfileViewModel
import com.orangeink.profile.databinding.BottomsheetProfileBinding
import com.orangeink.utils.isValidPhoneNumber
import com.orangeink.utils.pxToDp
import com.orangeink.utils.tryCast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileBottomSheet : RoundedBottomSheet() {

    companion object {
        const val TAG = "ProfileBottomSheet"

        private const val USER_UID = "user_uid"
        private const val USER_EMAIL = "user_email"
        private const val USER_NAME = "user_name"

        fun newInstance(uid: String, email: String, name: String): ProfileBottomSheet =
            ProfileBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(USER_UID, uid)
                    putString(USER_EMAIL, email)
                    putString(USER_NAME, name)
                }
            }
    }

    private lateinit var binding: BottomsheetProfileBinding
    private val viewModel: ProfileViewModel by viewModels()

    private var uid: String = ""
    private var email: String = ""
    private var name: String = ""
    private var listener: IEventHandler? = null

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
        binding = BottomsheetProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initArguments()
        initViews()
        setListeners()
        subscribeToLiveData()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        tryCast<IEventHandler>(context) {
            listener = this
        }
    }

    private fun initArguments() {
        arguments?.let {
            uid = it.getString(USER_UID, "")
            email = it.getString(USER_EMAIL, "")
            name = it.getString(USER_NAME, "")
        }
    }

    private fun initViews() {
        isCancelable = false
        binding.etEmail.setText(email)
        binding.etName.setText(name)
    }

    private fun setListeners() {
        binding.btnSave.setOnClickListener {
            if (validate()) {
                binding.progressProfile.visibility = View.VISIBLE
                val altPhone = binding.etAltPhone.text.toString()
                val participant = Participant(
                    id = uid,
                    name = binding.etName.text.toString(),
                    email = email,
                    phone = binding.etPhone.text.toString().toLong(),
                    altPhone = if (altPhone.isNotEmpty()) altPhone.toLong() else 0,
                    institution = binding.etInstitution.text.toString(),
                    gender = binding.root
                        .findViewById<RadioButton>(binding.radioGroup.checkedRadioButtonId)
                        .text.toString(),
                    generalFees = false
                )
                viewModel.createParticipant(participant)
            }
        }
        binding.etPhone.doAfterTextChanged {
            if (binding.cbSameAlt.isChecked) binding.etAltPhone.text = it
        }
        binding.cbSameAlt.setOnCheckedChangeListener { _, b ->
            if (b) {
                binding.etAltPhone.setText(binding.etPhone.text.toString())
                binding.etAltPhone.isEnabled = false
            } else {
                binding.etAltPhone.isEnabled = true
            }
        }
    }

    private fun subscribeToLiveData() {
        viewModel.participant.observe(viewLifecycleOwner) {
            binding.progressProfile.visibility = View.GONE
            Prefs(requireContext()).user = it
            listener?.handleEvent(UIEvent.ProfileSetupCompleted)
            dismiss()
        }
        viewModel.error.observe(viewLifecycleOwner) {
            binding.progressProfile.visibility = View.GONE
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun validate(): Boolean {
        if (binding.etName.text.isNullOrBlank()) {
            Toast.makeText(requireContext(), "Name cannot be empty!", Toast.LENGTH_SHORT).show()
            return false
        }
        if (binding.etPhone.text.isNullOrBlank()) {
            Toast.makeText(requireContext(), "Phone cannot be empty!", Toast.LENGTH_SHORT).show()
            return false
        }
        if (binding.etInstitution.text.isNullOrBlank()) {
            Toast.makeText(
                requireContext(),
                "Institution/Organization cannot be empty!",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }
        if (!binding.etPhone.text.toString().isValidPhoneNumber()) {
            Toast.makeText(requireContext(), "Phone number is not valid!", Toast.LENGTH_SHORT)
                .show()
            return false
        }
        return true
    }
}