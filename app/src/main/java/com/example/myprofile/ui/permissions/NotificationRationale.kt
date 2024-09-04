package com.example.myprofile.ui.permissions

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.myprofile.R
import com.example.myprofile.utils.extentions.openAppSettings

class NotificationRationale: DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it, R.style.CustomAlertDialogStyle)
            builder.setMessage(resources.getString(R.string.rational_dialog_text))
                .setPositiveButton(resources.getString(R.string.settings)) { _, _ ->
                    it.openAppSettings()
                }
                .setNegativeButton(resources.getString(R.string.dismiss), null)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}