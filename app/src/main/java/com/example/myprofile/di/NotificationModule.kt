package com.example.myprofile.di

import android.app.NotificationManager
import android.content.Context
import com.example.myprofile.data.notifications.NotificationBuilder
import com.example.myprofile.data.notifications.PermissionUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {

    @Singleton
    @Provides
    fun provideNotificationManager(
        @ApplicationContext context: Context
    ): NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    @Singleton
    @Provides
    fun providePermissionUtils(
        @ApplicationContext context: Context,
    ): PermissionUtils = PermissionUtils(context)

    @Singleton
    @Provides
    fun provideNotificationBuilder(
        @ApplicationContext context: Context,
        notificationManager: NotificationManager,
        permissionUtils: PermissionUtils
    ): NotificationBuilder = NotificationBuilder(context, notificationManager, permissionUtils)
}