package com.orangeink.techtrix.login.ui.bottomsheet

import android.app.Dialog
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
import com.google.firebase.auth.FirebaseUser
import com.orangeink.design.RoundedBottomSheet
import com.orangeink.techtrix.databinding.BottomsheetProfileBinding
import com.orangeink.network.model.Participant
import com.orangeink.techtrix.login.viewmodel.LoginViewModel
import com.orangeink.common.preferences.Prefs
import com.orangeink.utils.isValidPhoneNumber
import com.orangeink.utils.pxToDp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileBottomSheet : RoundedBottomSheet() {

    private lateinit var binding: BottomsheetProfileBinding
    private val viewModel: LoginViewModel by viewModels()

    private lateinit var user: FirebaseUser
    private lateinit var profileInterface: ProfileInterface

    interface ProfileInterface {
        fun profileSetupCompleted()
    }

    fun setData(user: FirebaseUser, profileInterface: ProfileInterface) {
        this.user = user
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
        binding = BottomsheetProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        setListeners()
        subscribeToLiveData()
    }

    private fun initViews() {
        isCancelable = false
        binding.etEmail.setText(user.email)
        binding.etName.setText(user.displayName)
    }

    private fun setListeners() {
        binding.btnSave.setOnClickListener {
            if (validate()) {
                binding.progressProfile.visibility = View.VISIBLE
                val altPhone = binding.etAltPhone.text.toString()
                val participant = Participant(
                    id = user.uid,
                    name = binding.etName.text.toString(),
                    email = user.email,
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
            profileInterface.profileSetupCompleted()
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