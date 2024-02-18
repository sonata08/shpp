package com.example.myprofile.ui.utils.extentions

import android.view.View


/**
 * Extension function to hide a View by setting its visibility to [View.GONE].
 */
fun View.hide() {
    visibility = View.GONE
}

/**
 * Extension function to show a View by setting its visibility to [View.VISIBLE].
 */
fun View.show() {
    visibility = View.VISIBLE
}