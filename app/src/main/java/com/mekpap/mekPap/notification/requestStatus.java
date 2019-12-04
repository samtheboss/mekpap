package com.mekpap.mekPap.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import androidx.core.app.NotificationCompat;


import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mekpap.mekPap.R;
import java.util.Map;
import java.util.Random;

public class requestStatus extends FirebaseMessagingService {
    Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if(remoteMessage.getData().isEmpty()) {
            showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        }
        else {
            showNotification(remoteMessage.getData());
        }
    }

    private void showNotification(Map<String, String> data) {
        String title = data.get("title");
        String body = data.get("body");

        NotificationManager notifaictionManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "com.smart.uber.notification";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,"NOTIFICATION",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("Request Status");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(R.color.colorPrimaryDark);
            notificationChannel.setVibrationPattern(new long[]{0,1000,500,1000});
            // creatring notification channel using notification manager
            notifaictionManager.createNotificationChannel(notificationChannel);


        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true).
                setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle(title).setContentText(body)
                .setSound(notificationSound)
                .setContentInfo("received");
        notifaictionManager.notify(new Random().nextInt(),notificationBuilder.build());

    }

    private void showNotification(String title, String body) {
        NotificationManager notifaictionManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "com.smart.uber.notification";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,"NOTIFICATION",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("Request Status");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(R.color.colorPrimaryDark);
            notificationChannel.setVibrationPattern(new long[]{0,1000,500,1000});
          // creatring notification channel using notification manager
            notifaictionManager.createNotificationChannel(notificationChannel);

        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true).
                setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle(title).setContentText(body)
                .setSound(notificationSound)
                .setContentInfo("received");
        notifaictionManager.notify(new Random().nextInt(),notificationBuilder.build());


    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        s = FirebaseInstanceId.getInstance().getToken();
        Log.d("TOKENFIREBASE",s);
    }
}
