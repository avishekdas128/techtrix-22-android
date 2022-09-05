package com.orangeink.techtrix.util

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import com.orangeink.techtrix.databinding.LayoutForceUpdateDialogBinding
import com.orangeink.utils.openPlayStore

class ForceUpdateDialog(
    context: Context
) : Dialog(context) {

    private lateinit var binding: LayoutForceUpdateDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = LayoutForceUpdateDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCancelable(false)
        setListeners()
    }

    private fun setListeners() {
        binding.btnConfirm.setOnClickListener {
            context.openPlayStore()
            dismiss()
        }
    }
}