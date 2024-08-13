package com.example.myprofile.utils.extentions

import com.example.myprofile.data.network.INVALID_ID

fun Long.isInvalidId() : Boolean  {
    return this == INVALID_ID
}