package com.orangeink.techtrix.profile.ui.bottomsheet

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.orangeink.design.RoundedBottomSheet
import com.orangeink.techtrix.databinding.BottomsheetProfileEditBinding
import com.orangeink.techtrix.login.data.model.Participant
import com.orangeink.techtrix.login.data.model.UpdateParticipant
import com.orangeink.techtrix.login.viewmodel.LoginViewModel
import com.orangeink.techtrix.preferences.Prefs
import com.orangeink.utils.pxToDp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileEditBottomSheet : RoundedBottomSheet() {

    private lateinit var binding: BottomsheetProfileEditBinding
    private val viewModel: LoginViewModel by viewModels()

    private lateinit var participant: Participant
    private lateinit var profileInterface: ProfileInterface

    interface ProfileInterface {
        fun profileEditCompleted()
    }

    fun setData(profileInterface: ProfileInterface) {
        this.profileInterface = profileInterface
    }

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
        binding = BottomsheetProfileEditBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        setListeners()
        subscribeToLiveData()
    }

    private fun initViews() {
        participant = Prefs(requireContext()).user!!
        binding.etEmail.setText(participant.email)
        binding.etName.setText(participant.name)
        binding.etPhone.setText(participant.phone.toString())
        val alt = participant.altPhone
        binding.etAltPhone.setText(if (alt == 0L) "" else alt.toString())
        binding.etInstitution.setText(participant.institution)
    }

    private fun setListeners() {
        binding.btnSave.setOnClickListener {
            if (validate()) {
                binding.progressProfile.visibility = View.VISIBLE
                val altPhone = binding.etAltPhone.text.toString()
                val updatedParticipant = UpdateParticipant(
                    binding.etName.text.toString(),
                    binding.etPhone.text.toString().toLong(),
                    if (altPhone.isNotEmpty()) altPhone.toLong() else 0,
                    binding.etInstitution.text.toString()
                )
                viewModel.updateProfile(updatedParticipant, participant.email!!)
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
        viewModel.update.observe(viewLifecycleOwner) {
            binding.progressProfile.visibility = View.GONE
            if (it.success!!) {
                val altPhone = binding.etAltPhone.text.toString()
                participant.name = binding.etName.text.toString()
                participant.phone = binding.etPhone.text.toString().toLong()
                participant.altPhone = if (altPhone.isNotEmpty()) altPhone.toLong() else 0
                participant.institution = binding.etInstitution.text.toString()
                Prefs(requireContext()).user = participant
                profileInterface.profileEditCompleted()
                dismiss()
            }
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
        return true
    }
}