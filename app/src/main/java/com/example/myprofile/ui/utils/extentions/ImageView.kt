package com.example.myprofile.ui.utils.extentions

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.myprofile.R

/**
 * Extension function to load an image into an ImageView using Glide with circular cropping.
 *
 * @param url The URL or path of the image to be loaded.
 */
fun ImageView.loadImage(url: String?) {
    Glide.with(this)
        .load(url)
        .circleCrop()
        .placeholder(R.drawable.ic_mockup)
        .error(R.drawable.ic_mockup)
        .into(this)
}