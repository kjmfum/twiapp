package com.learnakantwi.twiguides;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;

import static com.learnakantwi.twiguides.AppStartClass.CHANNEL_3_ID;
import static com.learnakantwi.twiguides.ProverbsActivity.proverbsArrayList;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public static final String TAG = "MyFirebaseService";
    FirebaseAuth mAuth  = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String remoteMessageTitle = "";
    String remoteMessageBody ="";



    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Map<String, Object> FCMToken = new HashMap<>();
        FCMToken.put("FCMToken", token);
        if (mAuth.getCurrentUser() != null && mAuth.getCurrentUser().getEmail() != null) {
            db.collection("users").document(mAuth.getCurrentUser().getEmail())
                    .set(FCMToken, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.i("Debugged", "No Error adding document");
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("Debugged", "Error adding document", e);
                        }
                    });
        }
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use WorkManager.
               scheduleJob();
                //Toast.makeText(this, "Received", Toast.LENGTH_SHORT).show();
            } else {
                // Handle message within 10 seconds
                //Toast.makeText(this, "Received Quick", Toast.LENGTH_SHORT).show();
               handleNow();
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            remoteMessageBody = remoteMessage.getNotification().getBody();

            remoteMessageTitle = remoteMessage.getNotification().getTitle();

          //  proverbsArrayList.add(new Proverbs(remoteMessage.getNotification().getBody(),"Notification","Added"));
           //Toast.makeText(getApplicationContext(), "Received Quick: "+ remoteMessage.getNotification().getBody(), Toast.LENGTH_SHORT).show();
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        sendNotification(remoteMessage.getNotification().getBody(), remoteMessageTitle );


    }

    private void scheduleJob() {

       // Toast.makeText(this, "Received Quick", Toast.LENGTH_SHORT).show();
        // [START dispatch_job]
  /*      OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(MyWorker.class)
                .build();
        WorkManager.getInstance().beginWith(work).enqueue();*/
        // [END dispatch_job]
    }

    private void handleNow() {
        Intent intent = new Intent(getApplicationContext(), SubPProverbsActivity.class);
        startActivity(intent);
    }

    private void sendNotification(String messageBody, String title) {
//        Intent intent = new Intent(this, MainActivity.class);
        Intent intent;
        if(title.contains("Offer")){
            intent = new Intent(this, InAppActivity.class);
        }
        else if (title.contains("Question")){
            intent = new Intent(this, NoticeBoardActivity.class);
        }
        else{
            intent = new Intent(this, MainActivity.class);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = CHANNEL_3_ID;
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.learnakantwiimage)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_3_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }


}
