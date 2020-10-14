package com.learnakantwi.twiguides;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import static com.learnakantwi.twiguides.AppStartClass.CHANNEL_2_ID;

public class Notification_receiver_Proverbs extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent1 = new Intent(context, RepeatingActivityProverbs.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,100, intent1, PendingIntent.FLAG_UPDATE_CURRENT);


        Notification notification = new NotificationCompat.Builder(context, CHANNEL_2_ID)
                .setSmallIcon(R.drawable.proverbsimage)
                .setContentIntent(pendingIntent)
                .setContentTitle("Learn Akan Twi")
                .setContentText("It's time for an Akan Proverb")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setAutoCancel(true)
                .build();

        notificationManager.notify(2, notification);

        /*Notification notification = new NotificationCompat.Builder(context,"Channel_1").
                setContentIntent(pendingIntent).setContentTitle("Hi")
                .setContentText("Hi toast a notification")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        notificationManager.notify(1, notification);*/


    }
}

