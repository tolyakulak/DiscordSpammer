package com.github.tolyakulak.discordspammer.core.framework

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.github.tolyakulak.discordspammer.R
import com.github.tolyakulak.discordspammer.core.framework.util.NotificationChannelsId
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.createNotificationChannel(
            NotificationChannel(
                NotificationChannelsId.FOREGROUND_SERVICE,
                getString(R.string.foreground_channel_name),
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = getString(R.string.foreground_channel_description)
            }
        )
    }
}