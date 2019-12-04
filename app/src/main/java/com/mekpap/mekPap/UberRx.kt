package com.mekpap.mekPap

import android.app.Application
import android.media.RingtoneManager
import androidx.core.app.NotificationManagerCompat
import com.mekpap.mekPap.notification.NotificationHelper

class UberRx:Application(){
    companion object {
        lateinit var instance: UberRx
            private set
    }

    override fun onCreate() {
        super.onCreate()
        NotificationHelper.createNotificationHelper(
                this,
                NotificationManagerCompat.IMPORTANCE_HIGH,
                false,
                getString(R.string.app_name),
                "General notifications from MekPap.",
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        )
    }
}