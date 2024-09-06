package com.example.myprofile.data.notifications

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
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

        val pendingIntent = NavDeepLinkBuilder(context)
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.searchContactsFragment)
            .setComponentName(MainActivity::class.java)
            .createPendingIntent()

        val notificationBuilder = NotificationCompat.Builder(context, LOCAL_NOTIFICATIONS_CHANNEL)
            .setContentTitle(context.getString(R.string.search))
            .setContentText(context.getString(R.string.go_to_search))
            .setSmallIcon(R.drawable.ic_search)
            .setContentIntent(pendingIntent)
            .addAction(R.drawable.ic_search, context.getString(R.string.search),
                pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)

        if (permissionUtils.isNotificationPermissionGranted()) {
            notificationManager.notify(0, notificationBuilder.build())
        }
    }
}