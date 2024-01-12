package com.example.myprofile.ui.utils.extentions

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.myprofile.R

fun ImageView.loadImage(url: String?) {
    Glide.with(this)
        .load(url)
        .circleCrop()
        .placeholder(R.drawable.ic_mockup)
        .error(R.drawable.ic_mockup)
        .into(this)
}