package com.mekpap.mekPap.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.mekpap.mekPap.AppConstants
import com.mekpap.mekPap.R


object NotificationHelper {
    fun createNotificationHelper(context: Context, importance: Int, showBadge: Boolean, name: String, descriptionText: String, sound: Uri?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val attributes = AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION).build()
            val channelId = "${AppConstants.NOTIFICATIONSUFFIX}-$name"
            val channel = NotificationChannel(channelId, name, importance)
            channel.description = descriptionText
            channel.setShowBadge(showBadge)
            channel.setSound(sound, attributes)
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun createGeneralMekPapNotification(context: Context, title: String?, message: String?, bigText: String?, autoCancel: Boolean, timestamp: Long?,mechanicId:String?,requestId:String?) {
        val channelId = "${AppConstants.NOTIFICATIONSUFFIX}-${context.getString(R.string.app_name)}"

        val intent = Intent(context, mechanicInformation::class.java).apply {
            putExtra(AppConstants.MECHANICID,mechanicId )
            putExtra(AppConstants.REQUESTID,requestId)

        }
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context,0,intent,0)
        val notificationBuilder = NotificationCompat.Builder(context, channelId).apply {
            setSmallIcon(R.drawable.ic_stat_name) //Remember to change this
            setContentTitle(title)
            setContentText(message)
            setAutoCancel(autoCancel)
            setStyle(NotificationCompat.BigTextStyle().bigText(bigText))
            priority = NotificationCompat.PRIORITY_DEFAULT
            setAutoCancel(autoCancel)
            setContentIntent(pendingIntent)
        }
        val notificationManager = NotificationManagerCompat.from(context)
        val id = timestamp?.toInt() ?: 2001
        notificationManager.notify(id, notificationBuilder.build())
    }

    fun createMechanicRequestResponse(
            mekReqId: String?,
            name: String,
            context: Context,
            title: String?,
            message: String?,
            bigText: String?,
            autoCancel: Boolean,
            timestamp: Long?,
            mekId:String?
    ){
        val channelId = "${AppConstants.NOTIFICATIONSUFFIX}-$name"
        val fullScreenIntent = Intent(context, mechanicInformation::class.java).apply {
            putExtra(AppConstants.MEKREQUESTID, mekReqId)
            putExtra("timestamp",timestamp)
            putExtra("mekId",mekId)
        }
        fullScreenIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val fullScreenPendingIntent =
                PendingIntent.getActivity(context, 0, fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val notificationBuilder = NotificationCompat.Builder(context, channelId).apply {
            setSmallIcon(R.drawable.ic_stat_name)
            setContentTitle(title)
            setContentText(message)
            setTicker(title)
            setCategory(NotificationCompat.CATEGORY_CALL)
            setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE))
            setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            priority = NotificationCompat.PRIORITY_HIGH
//            setContentIntent(pendingIntent)

            setFullScreenIntent(fullScreenPendingIntent, true)
            setAutoCancel(autoCancel)

        }

        val notificationManager = NotificationManagerCompat.from(context)
        val id = timestamp?.toInt()!!
        notificationManager.notify(id, notificationBuilder.build())
    }
}