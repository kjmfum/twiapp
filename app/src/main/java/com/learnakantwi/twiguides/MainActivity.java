package com.learnakantwi.twiguides;

import android.content.Intent;
import android.os.Handler;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    int SPLASH_TIME_OUT = 1000;

    public void goToAll() {
        Intent intent = new Intent(getApplicationContext(), AllActivity.class);
        startActivity(intent);
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
                    // skuList.add("gas");
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
                                            for (SkuDetails skuDetails : skuDetailsList) {
                                                String sku = skuDetails.getSku();
                                                String price = skuDetails.getPrice();
                                                if ("reading_club".equals(sku)) {
                                                    premiumUpgradePrice = price;


                                                    Purchase.PurchasesResult purchasesResult = billingClient.queryPurchases(BillingClient.SkuType.SUBS);
                                                    List<Purchase> purchasesList = purchasesResult.getPurchasesList();
                                                    //


                                                    if (purchasesList !=null && !purchasesList.isEmpty()){
                                                        String me1 = purchasesList.get(0).getOrderId();
                                                        for (Purchase purchase : purchasesList) {
                                                            String skuName = purchase.getSku();
                                                             me1 = purchasesList.get(0).getOrderId();
                                                            int me2 = purchasesList.get(0).getPurchaseState();
                                                            int me3 = purchasesList.size();
                                                            toast.setText(skuName);
                                                            toast.show();
                                                            if (skuName.equals("reading_club")){
                                                                Intent homeIntent = new Intent(getApplicationContext(), SubPHomeMainActivity.class);
                                                                startActivity(homeIntent);
                                                            }
                                                        }
                                                        //toast.setText(Integer.toString(me3));
                                                    }
                                                    else{
                                                        //toast.setText("nothing subscribed");
                                                        //toast.show();
                                                        //Intent homeIntent = new Intent(getApplicationContext(), SignInActivity.class);
                                                        Intent homeIntent = new Intent(getApplicationContext(), HomeMainActivity.class);
                                                        startActivity(homeIntent);
                                                        finish();
                                                    }


                                                    //purchase.getOrderId();

//                                                    billingClient.queryPurchases(sku);
//                                                    Purchase.PurchasesResult purchasesResult = billingClient.queryPurchases(BillingClient.SkuType.SUBS);
//                                                    purchasesResult.getPurchasesList();

                                                } /*else if ("gas".equals(sku)) {
                                                gasPrice = price;
                                            }*/
                                            }
                                        }
                                    }



                                }
                            });
                }
            }
            @Override
            public void onBillingServiceDisconnected() {
                //Toast.makeText(MainActivity.this, "I got disconnected", Toast.LENGTH_SHORT).show();
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
               // setUpBillingClient();
                Intent homeIntent = new Intent(getApplicationContext(), HomeMainActivity.class);
                startActivity(homeIntent);
                finish();
            }


        });
        //billingClient.endConnection();
    }


    public void goToDaysOfWk (View v){
        Intent intent = new Intent(getApplicationContext(), DaysOfWeekActivity.class);
        startActivity(intent);
    }

    public void goToAlphabets(View view){
        Intent intent = new Intent(getApplicationContext(), AlphabetsActivity.class);
        startActivity(intent);
    }

    public void goToTime(View view){
        Intent intent = new Intent(getApplicationContext(), TimeActivity.class);
        startActivity(intent);
    }

    public void goToFamily(View view){
        Intent intent = new Intent(getApplicationContext(), FamilyActivity.class);
        startActivity(intent);
    }

    public void goToWeather(View view){
        Intent intent = new Intent(getApplicationContext(), WeatherActivity.class);
        startActivity(intent);
    }

    public void goToMonths(View view) {
        Intent intent = new Intent(getApplicationContext(), WeatherActivity.class);
        startActivity(intent);
    }

    public void goToPronouns (View view) {
        Intent intent = new Intent(getApplicationContext(), PronounsActivity.class);
        startActivity(intent);
    }

    public void goToColours (View view) {
        Intent intent = new Intent(getApplicationContext(), ColoursActivity.class);
        startActivity(intent);
    }

    public void goToAnimals (View view) {
        Intent intent = new Intent(getApplicationContext(), AnimalsActivity.class);
        startActivity(intent);
    }

    public void goToBodyparts (View view) {
        Intent intent = new Intent(getApplicationContext(), AnimalsActivity.class);
        startActivity(intent);
    }

    public void goToFood (View view) {
        Intent intent = new Intent(getApplicationContext(), FoodActivity.class);
        startActivity(intent);
    }

    public void goToCommonExpressionsa (View view) {
        Intent intent = new Intent(getApplicationContext(), CommonExpressionsaActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

               setUpBillingClient();
               // Intent homeIntent = new Intent(getApplicationContext(), HomeMainActivity.class);
                //startActivity(homeIntent);
                //finish();
            }
        }, SPLASH_TIME_OUT);

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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                setUpBillingClient();
                // Intent homeIntent = new Intent(getApplicationContext(), HomeMainActivity.class);
                //startActivity(homeIntent);
                //finish();
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
