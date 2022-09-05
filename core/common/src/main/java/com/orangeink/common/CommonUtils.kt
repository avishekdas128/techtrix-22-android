package com.orangeink.common

import androidx.annotation.DrawableRes

@DrawableRes
fun findIcon(input: String): Int {
    return when (input.lowercase()) {
        "automata" -> R.drawable.ic_baseline_code_24
        "gaming" -> R.drawable.ic_outline_sports_esports_24
        "flagship" -> R.drawable.ic_round_outlined_flag_24
        "robotics" -> R.drawable.ic_robot
        "out of the box" -> R.drawable.ic_box
        "art facts" -> R.drawable.ic_round_color_lens_24
        else -> R.drawable.ic_outline_sports_esports_24
    }
}