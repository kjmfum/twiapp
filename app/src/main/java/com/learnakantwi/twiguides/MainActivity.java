package com.learnakantwi.twiguides;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements PurchasesUpdatedListener {

    private NotificationManagerCompat notificationManager;
    private EditText editTextTitle;
    private EditText editTextMessage;
    BillingClient billingClient;
    Toast toast;
    String premiumUpgradePrice;
    SharedPreferences sharedPreferencesAds;



    int SPLASH_TIME_OUT = 3000;
    int times =0;

    public void goToAll() {
        Intent intent = new Intent(getApplicationContext(), AllActivity.class);
        startActivity(intent);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    @Override
    public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> list) {

    }

    public void setUpBillingClient() {

        billingClient = BillingClient.newBuilder(this)
                .setListener(this)
                .enablePendingPurchases()
                .build();
        setUpBilling();
    }

    public void setUpBilling(){


        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    //


                   List<String> skuList = new ArrayList<>();
                   skuList.add("reading_club");
                    skuList.add("premium_annually");
                    skuList.add("premium_6months");
                    SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
                    params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS);



                    billingClient.querySkuDetailsAsync(params.build(),
                            new SkuDetailsResponseListener() {
                                @Override
                                public void onSkuDetailsResponse(BillingResult billingResult, List<SkuDetails> skuDetailsList) {
                                    // Process the result.

                                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
                                        toast.setText("Already Purchased 1");
                                        toast.show();

                                    } else{
                                        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && skuDetailsList != null) {

                                            /*toast.setText(Integer.toString(times));
                                            toast.show();*/

                                            for (SkuDetails skuDetails : skuDetailsList) {
                                                String sku = skuDetails.getSku();
                                                String price = skuDetails.getPrice();
                                                if ("reading_club".equals(sku) || "premium_annually".equals(sku) || "premium_6months".equals(sku)) {
                                                    premiumUpgradePrice = price;


                                                    Purchase.PurchasesResult purchasesResult = billingClient.queryPurchases(BillingClient.SkuType.SUBS);
                                                    List<Purchase> purchasesList = purchasesResult.getPurchasesList();
                                                    //


                                                    if (purchasesList !=null && !purchasesList.isEmpty()){
                                                        String me1 = purchasesList.get(0).getOrderId();
                                                        for (Purchase purchase : purchasesList) {
                                                            String skuName = purchase.getSku();
                                                            /*String ptoken = purchase.getPurchaseToken();
                                                             me1 = purchasesList.get(0).getOrderId();
                                                            int me2 = purchasesList.get(0).getPurchaseState();
                                                            int me3 = purchasesList.size();*/
                                                            /*toast.setText(skuName);
                                                            toast.show();*/
                                                            if (skuName.equals("reading_club") || skuName.equals("premium_6months") || skuName.equals("premium_annually")){
                                                               // Intent homeIntent = new Intent(getApplicationContext(), SubPHomeMainActivity.class);
                                                                //Intent homeIntent = new Intent(getApplicationContext(), HomeMainActivity.class);

                                                                sharedPreferencesAds = getSharedPreferences("AdsDecision",MODE_PRIVATE);
                                                                SharedPreferences.Editor editor = sharedPreferencesAds.edit();
                                                                editor.putInt("Ads", 0);
                                                                editor.apply();

                                                                Intent homeIntent = new Intent(getApplicationContext(), SubPHomeMainActivity.class);
                                                                startActivity(homeIntent);

                                                            }
                                                       /*     if (skuName.equals("premium_annually" )){
                                                                Intent homeIntent = new Intent(getApplicationContext(), SubPHomeMainActivity.class);
                                                                startActivity(homeIntent);
                                                            }*/
                                                        }
                                                        //toast.setText(Integer.toString(me3));
                                                    }
                                                    else{
                                                       /* SharedPreferences.Editor editor = sharedPreferencesAds.edit();
                                                        editor.putString("Ads","Yes");
                                                        editor.apply();*/


                                                        //toast.setText("nothing subscribed");
                                                        //toast.show();
                                                       // Intent homeIntent = new Intent(getApplicationContext(), SignInActivity.class);
                                                        //Intent homeIntent = new Intent(getApplicationContext(), SignUpActivity.class);
                                                        Intent homeIntent = new Intent(getApplicationContext(), HomeMainActivity.class);
                                                       // Intent homeIntent = new Intent(getApplicationContext(), SubPHomeMainActivity.class);
                                                       // Intent homeIntent = new Intent(getApplicationContext(), RealTimeDatabase.class);
                                                        startActivity(homeIntent);
                                                        finish();
                                                    }

                                                } /*else if ("gas".equals(sku)) {
                                                gasPrice = price;
                                            }*/
                                            }
                                        }
                                        else {
                                            times++;
                                            /*if (isNetworkAvailable()){
                                                if (times < 10){
                                                    toast.setText("No Internet Connection 5");
                                                    toast.show();

                                               *//*toast.setText(Integer.toString(times));
                                               toast.show();*//*
                                                    setUpBillingClient();
                                                }
                                                else {
                                                    Intent homeIntent = new Intent(getApplicationContext(), HomeMainActivity.class);
                                                    startActivity(homeIntent);
                                                    finish();
                                                }
                                            }
                                            else {*/
                                                if (times < 5) {
                                                    toast.setText("No Internet Connection");
                                                    toast.show();
                                                    if (!isNetworkAvailable()){
                                                        Intent homeIntent = new Intent(getApplicationContext(), HomeMainActivity.class);
                                                        startActivity(homeIntent);
                                                        finish();
                                                    }else {
                                                        setUpBillingClient();
                                                    }

                                                } else {
                                                    Intent homeIntent = new Intent(getApplicationContext(), HomeMainActivity.class);
                                                    startActivity(homeIntent);
                                                    finish();
                                                }

                                            }

                                        }


                                    }



                               // }
                            });
                }
            }
            @Override
            public void onBillingServiceDisconnected() {

                times++;
                times++;
                if (times < 2){
                    toast.setText("Internet Disconnected 1");
                    toast.show();

                                               /*toast.setText(Integer.toString(times));
                                               toast.show();*/
                    setUpBillingClient();
                }
                else {
                    Intent homeIntent = new Intent(getApplicationContext(), HomeMainActivity.class);

                    startActivity(homeIntent);
                    finish();
                }

                //Toast.makeText(MainActivity.this, "I got disconnected", Toast.LENGTH_SHORT).show();
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
               // setUpBillingClient();
                /*Intent homeIntent = new Intent(getApplicationContext(), HomeMainActivity.class);
                startActivity(homeIntent);
                finish();*/
            }


        });
        //billingClient.endConnection();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferencesAds = getSharedPreferences("AdsDecision",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferencesAds.edit();


        editor.putInt("Ads",1);
        editor.apply();

        toast = Toast.makeText(this, "", Toast.LENGTH_LONG);

       //Intent homeIntent = new Intent(getApplicationContext(), SubPHomeMainActivity.class);
       // Intent homeIntent = new Intent(getApplicationContext(), HomeMainActivity.class);
        //startActivity(homeIntent);

      new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

               setUpBillingClient();
            }
        }, SPLASH_TIME_OUT);

       ///////////////////

        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent = new Intent(getApplicationContext(), InAppActivity.class);
                startActivity(homeIntent);
                finish();
            }
        },SPLASH_TIME_OUT);*/


    }

    @Override
    protected void onResume() {

     // Intent homeIntent = new Intent(getApplicationContext(), SubPHomeMainActivity.class);
       // Intent homeIntent = new Intent(getApplicationContext(), SettingsActivity.class);
      //  Intent homeIntent = new Intent(getApplicationContext(), HomeMainActivity.class);
        //startActivity(homeIntent);
        //super.onResume();

     new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                setUpBillingClient();
            }
        }, SPLASH_TIME_OUT);
        super.onResume();
    }

    public void sendOnChannel1(View v) {
        String title = editTextTitle.getText().toString();
        String message = editTextMessage.getText().toString();

       /* Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_one)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        notificationManager.notify(1, notification);
    }

    public void sendOnChannel2(View v) {
        String title = editTextTitle.getText().toString();
        String message = editTextMessage.getText().toString();

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_2_ID)
                .setSmallIcon(R.drawable.ic_two)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();

        notificationManager.notify(2, notification); */


        /*Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, 10);
        //calendar.set(Calendar.HOUR_OF_DAY, 9);
        //calendar.set(Calendar.MINUTE, 45);

        Intent intent = new Intent(getApplicationContext(), Notification_receiver.class);
        //Intent intent = new Intent(getApplicationContext(), Home.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),100,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        //PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),100,intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        //alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);*/

    }

}
