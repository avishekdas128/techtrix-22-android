package com.orangeink.registration.ui.bottomsheet

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.zxing.WriterException
import com.orangeink.common.preferences.Prefs
import com.orangeink.design.RoundedBottomSheet
import com.orangeink.registration.databinding.BottomsheetQrCodeBinding
import com.orangeink.registration.qr.QRContents
import com.orangeink.registration.qr.QREncoder
import timber.log.Timber

class QRBottomSheet : RoundedBottomSheet() {

    private lateinit var binding: BottomsheetQrCodeBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener { mDialog: DialogInterface ->
            val d = mDialog as BottomSheetDialog
            val bottomSheet =
                d.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet!!)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomsheetQrCodeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        generateQR()
    }

    private fun generateQR() {
        Prefs(requireContext()).user?.email.let {
            val qrgEncoder = QREncoder(it, null, QRContents.Type.EMAIL, 500)
            qrgEncoder.colorBlack =
                ContextCompat.getColor(requireContext(), com.orangeink.design.R.color.purple_400)
            qrgEncoder.colorWhite = Color.TRANSPARENT
            try {
                binding.ivQrCode.setImageBitmap(qrgEncoder.bitmap)
            } catch (e: WriterException) {
                Timber.e(e)
            }
        }
    }

}