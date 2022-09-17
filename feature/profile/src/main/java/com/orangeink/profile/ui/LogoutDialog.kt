package com.orangeink.profile.ui

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import com.orangeink.profile.databinding.LayoutLogoutDialogBinding

class LogoutDialog(
    context: Context,
    private val dialogInterface: DialogInterface
) : Dialog(context) {

    private lateinit var binding: LayoutLogoutDialogBinding

    interface DialogInterface {
        fun onLogoutClicked()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = LayoutLogoutDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setListeners()
    }

    private fun setListeners() {
        binding.btnConfirm.setOnClickListener {
            dialogInterface.onLogoutClicked()
            dismiss()
        }
        binding.btnCancel.setOnClickListener { dismiss() }
    }
}
