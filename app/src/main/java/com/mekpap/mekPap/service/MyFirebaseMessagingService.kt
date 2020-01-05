package com.mekpap.mekPap.service

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mekpap.mekPap.AppConstants
import com.mekpap.mekPap.notification.NotificationHelper

class MyFirebaseMessagingService:FirebaseMessagingService(){
    private var TAG = "FCM"
    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        Log.d(TAG,"token $p0")
        if(p0 == null) throw NullPointerException("FCM token is null")
        ServiceUtil.getFCMRegistrationTokens {
            if(it.contains(p0))
                return@getFCMRegistrationTokens
            it.add(p0)
            ServiceUtil.setFCMRegistrationTokens(it)
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if(FirebaseAuth.getInstance().currentUser?.uid != null){
            if (remoteMessage.data.isNotEmpty()){
                Log.d(TAG,"$remoteMessage")
                Log.d(TAG,"${remoteMessage.notification}")
                Log.d(TAG,"${remoteMessage.data}")
                when(remoteMessage.data["channelId"]){
                    "${AppConstants.NOTIFICATIONSUFFIX}-Mek"->{
                        val title = remoteMessage.data["title"]
                        val message = remoteMessage.data["body"]
                        val timestamp = remoteMessage.data["timestamp"]?.toLong()
                        val mechanicId = remoteMessage.data["mechanicId"]
                        val requestId = remoteMessage.data["requestId"]
                        NotificationHelper.createGeneralMekPapNotification(baseContext,title,message,message,true,timestamp,mechanicId,requestId)
                    }
                    "${AppConstants.NOTIFICATIONSUFFIX}-MechanicRequests"->{
                        val title = remoteMessage.data["title"]
                        val message = remoteMessage.data["body"]
                        val timestamp = remoteMessage.data["timestamp"]?.toLong()
                        val mechanicId = remoteMessage.data["mechanicId"]
                        val requestId = remoteMessage.data["requestId"]
                        NotificationHelper.createGeneralMekPapNotification(baseContext,title,message,message,true,timestamp,mechanicId,requestId)
                    }
                    "${AppConstants.NOTIFICATIONSUFFIX}-Appointments"->{
                        val title = remoteMessage.data["title"]
                        val message = remoteMessage.data["body"]
                        val timestamp = remoteMessage.data["timestamp"]?.toLong()
                        val mechanicId = remoteMessage.data["mechanicId"]
                        val requestId = remoteMessage.data["requestId"]
                        NotificationHelper.createGeneralMekPapNotification(baseContext,title,message,message,true,timestamp,mechanicId,requestId)
                    }
                }
            }
        }
    }
}