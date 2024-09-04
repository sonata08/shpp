package com.example.myprofile

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import com.example.myprofile.data.notifications.LOCAL_NOTIFICATIONS_CHANNEL
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    @Inject
    lateinit var notificationManager: NotificationManager

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            LOCAL_NOTIFICATIONS_CHANNEL,
            resources.getString(R.string.search_channel_name),
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)
    }
}