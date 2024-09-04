package com.example.myprofile.data.notifications

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.myprofile.R
import com.example.myprofile.ui.main.MainActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class NotificationBuilder @Inject constructor(
    @ApplicationContext val context: Context,
    private val notificationManager: NotificationManager,
    private val permissionUtils: PermissionUtils,
) {

    fun sendNotification() {

        val notificationIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context, 0,
            notificationIntent,
            FLAG_IMMUTABLE
        )


        val notificationBuilder = NotificationCompat.Builder(context, LOCAL_NOTIFICATIONS_CHANNEL)
            .setContentTitle(context.getString(R.string.search))
            .setContentText(context.getString(R.string.go_to_search))
            .setSmallIcon(R.drawable.ic_search)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)

        if (permissionUtils.isNotificationPermissionGranted()) {
            notificationManager.notify(0, notificationBuilder.build())
        }
    }
}