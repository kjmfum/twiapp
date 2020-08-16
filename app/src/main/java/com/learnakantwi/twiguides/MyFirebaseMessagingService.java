package com.learnakantwi.twiguides;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static com.learnakantwi.twiguides.ProverbsActivity.proverbsArrayList;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public static final String TAG = "MyFirebaseService";

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
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
                Toast.makeText(this, "Received", Toast.LENGTH_SHORT).show();
            } else {
                // Handle message within 10 seconds
                Toast.makeText(this, "Received Quick", Toast.LENGTH_SHORT).show();
               // handleNow();
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            Intent intent = new Intent(getApplicationContext(), SubPProverbsActivity.class);
            //startActivity(intent);
            proverbsArrayList.add(new Proverbs(remoteMessage.getNotification().getBody(),"Notification","Added"));
           //Toast.makeText(getApplicationContext(), "Received Quick: "+ remoteMessage.getNotification().getBody(), Toast.LENGTH_SHORT).show();
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    private void scheduleJob() {

       // Toast.makeText(this, "Received Quick", Toast.LENGTH_SHORT).show();
        // [START dispatch_job]
  /*      OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(MyWorker.class)
                .build();
        WorkManager.getInstance().beginWith(work).enqueue();*/
        // [END dispatch_job]
    }
}
