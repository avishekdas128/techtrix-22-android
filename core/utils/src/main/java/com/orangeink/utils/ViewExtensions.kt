package com.orangeink.utils

import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide

fun TextView.setData(text: String?) {
    if (text.isNullOrBlank())
        this.visibility = View.GONE
    else {
        this.text = text
        this.visibility = View.VISIBLE
    }
}

fun ImageView.loadImage(url: String, @DrawableRes errorDrawable: Int) {
    Glide.with(this.context)
        .load(url)
        .placeholder(errorDrawable)
        .error(errorDrawable)
        .into(this)
}

fun ImageView.loadImage(uri: Uri, @DrawableRes errorDrawable: Int) {
    Glide.with(this.context)
        .load(uri)
        .placeholder(errorDrawable)
        .error(errorDrawable)
        .into(this)
}

