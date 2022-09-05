package com.orangeink.design

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

open class RoundedBottomSheet : BottomSheetDialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.customBottomSheetDialog)
    }
}