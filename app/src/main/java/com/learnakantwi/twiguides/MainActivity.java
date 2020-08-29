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

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchaseHistoryRecord;
import com.android.billingclient.api.PurchaseHistoryResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.appodeal.ads.Appodeal;
import com.appodeal.ads.utils.Log;
import com.google.android.gms.ads.AdView;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;

import static com.learnakantwi.twiguides.ProverbsActivity.proverbsArrayList;


public class MainActivity extends AppCompatActivity implements PurchasesUpdatedListener, PurchaseHistoryResponseListener {

    static float smallFont = 15;
    static float largeFont = 40;
    static int textLength = 15;
    static long longDelay = 6000;
    static long shortDelay = 3000;


    private NotificationManagerCompat notificationManager;
    private EditText editTextTitle;
    private EditText editTextMessage;
    BillingClient billingClient;
    Toast toast;
    String premiumUpgradePrice;
    SharedPreferences sharedPreferencesAds;
    static final String APPODEAL_KEY = "f70e5b095b1ab8de93dd6851fa87fe8589490b16f10a92ea";


    int SPLASH_TIME_OUT = 3000;
    int times =0;
    static int Lifetime;
    static  int Subscribed;

    private FirebaseAnalytics mFirebaseAnalytics;

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
    public void onPurchaseHistoryResponse(BillingResult billingResult, List<PurchaseHistoryRecord> list) {

    }
    void handlePurchase(Purchase purchase) {
        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged()) {
                AcknowledgePurchaseParams acknowledgePurchaseParams =
                        AcknowledgePurchaseParams.newBuilder()
                                .setPurchaseToken(purchase.getPurchaseToken())
                                .build();
                AcknowledgePurchaseResponseListener acknowledgePurchaseResponseListener = new AcknowledgePurchaseResponseListener() {
                    @Override
                    public void onAcknowledgePurchaseResponse(BillingResult billingResult) {

                        toast.setText("Acknowleged");
                        toast.show();

                        /*Intent intent = new Intent(getApplicationContext(), SubPHomeMainActivity.class);
                        startActivity(intent);*/
                    }

                };


                billingClient.acknowledgePurchase(acknowledgePurchaseParams, acknowledgePurchaseResponseListener);
                Intent intent = new Intent(getApplicationContext(), SubPHomeMainActivity.class);
                startActivity(intent);

            }
        }
    }

    @Override
    public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> purchases) {

        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK
                && purchases != null) {
            for (Purchase purchase : purchases) {
                handlePurchase(purchase);
            }
        } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
            // Handle an error caused by a user cancelling the purchase flow.
            Toast.makeText(this, "You cancelled the Purchase", Toast.LENGTH_SHORT).show();
            billingClient.endConnection();}
        else if(billingResult.getResponseCode()== BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED){
            Intent homeIntent = new Intent(getApplicationContext(), SubPHomeMainActivity.class);
            startActivity(homeIntent);
            finish();
            Toast.makeText(this, "Already Purchased", Toast.LENGTH_SHORT).show();
        } else {
            // Handle any other error codes.
            Toast.makeText(this,"Could not complete purchase", Toast.LENGTH_LONG).show();
        }


    }

    public void setUpBillingClient() {

        billingClient = BillingClient.newBuilder(this)
                .setListener(this)
                .enablePendingPurchases()
                .build();
        //setUpBillingInApp();
        setUpBilling();
    }

    public void setUpBilling(){

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {

        List<String> skuList1 = new ArrayList<>();

        //skuList1.add("likoio");
        //skuList1.add("lifetime_full_access");
        skuList1.add("lifetime_full_access1");
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList1).setType(BillingClient.SkuType.INAPP);



////////OneTimeProducts
            billingClient.querySkuDetailsAsync(params.build(),
                    new SkuDetailsResponseListener() {
                        @Override
                        public void onSkuDetailsResponse(BillingResult billingResult, List<SkuDetails> skuDetailsList) {
                            // Process the result.
                            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
                                toast.setText("Already Purchased");
                                toast.show();

                            } else{
                                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && skuDetailsList != null) {
                                    for (SkuDetails skuDetails : skuDetailsList) {
                                        String sku = skuDetails.getSku();
                                        String price = skuDetails.getPrice();
                                        //
                                        if ("lifetime_full_access1".equals(sku)) {
                                       //     if ("likoio".equals(sku)) {
                                            premiumUpgradePrice = price;
                                            Purchase.PurchasesResult purchasesResult = billingClient.queryPurchases(BillingClient.SkuType.INAPP);
                                            List<Purchase> purchasesList = purchasesResult.getPurchasesList();
                                            //
                                           // Toast.makeText(MainActivity.this, "Hi2 "+ purchasesList.size(), Toast.LENGTH_SHORT).show();

                                          if (purchasesList !=null && !purchasesList.isEmpty()){
                                          // if (purchasesList !=null && purchasesList.size()>1){
                                               //Toast.makeText(MainActivity.this, "Hi "+ sku +" "+ purchasesList.size(), Toast.LENGTH_SHORT).show();
                                                for (Purchase purchase : purchasesList) {
                                                    String skuName = purchase.getSku();
                                                   // if (Lifetime == 6) {

                                                       if (skuName.equals("lifetime_full_access1")) {
                                                       //     if (skuName.equals("likoio")) {

                                                            sharedPreferencesAds = getSharedPreferences("AdsDecision", MODE_PRIVATE);
                                                            SharedPreferences.Editor editor = sharedPreferencesAds.edit();
                                                            editor.putInt("Lifetime", 1);
                                                            editor.putInt("Sub", 1);
                                                            editor.apply();
                                                            Lifetime = sharedPreferencesAds.getInt("Lifetime", 5);
                                                           Subscribed = sharedPreferencesAds.getInt("Sub", 5);

                                                           mFirebaseAnalytics.setUserProperty("Monthly_Premium", "Premium");
                                                           mFirebaseAnalytics.setUserProperty("Monthly_Premium", "Lifetime");
                                                          // addProverbs();
                                                            Intent homeIntent = new Intent(getApplicationContext(), SubPHomeMainActivity.class);
                                                           //Intent homeIntent = new Intent(getApplicationContext(), HomeMainActivity.class);
                                                            //Intent homeIntent = new Intent(getApplicationContext(), InAppActivity.class);
                                                            startActivity(homeIntent);
                                                        } else {
                                                           Lifetime=4;
                                                           mFirebaseAnalytics.setUserProperty("Monthly_Premium", "Regular");
                                                           //Toast.makeText(MainActivity.this, "I don't have access5: "+skuName, Toast.LENGTH_SHORT).show();
                                                        }
                                                   // }
                                                }
                                                //toast.setText(Integer.toString(me3));
                                            }/*else {
                                                Toast.makeText(MainActivity.this, "Hi "+Lifetime, Toast.LENGTH_SHORT).show();
                                            }*/

                                        }
                                    }
                                }

                            }

                        }
                    });



        ///////


   /*     billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {*/
                    // The BillingClient is ready. You can query purchases here.
                    //

////////Subscriptions
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(!(Lifetime==1)) {

                               // Toast.makeText(MainActivity.this, "Inside Here 3: "+ Lifetime , Toast.LENGTH_SHORT).show();
                                List<String> skuList = new ArrayList<>();
                                skuList.add("reading_club");
                                skuList.add("monthly_subscription");
                                skuList.add("6months_subscription");
                                skuList.add("year_subscription");
                                skuList.add("premium_annually");
                                skuList.add("premium_6months");
                                // SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
                                params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS);


                                billingClient.querySkuDetailsAsync(params.build(),
                                        new SkuDetailsResponseListener() {
                                            @Override
                                            public void onSkuDetailsResponse(BillingResult billingResult, List<SkuDetails> skuDetailsList) {
                                                // Process the result.

                                                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
                                                    toast.setText("Already Purchased");
                                                    toast.show();

                                                } else {
                                                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && skuDetailsList != null) {

                                            /*toast.setText(Integer.toString(times));
                                            toast.show();*/

                                                        for (SkuDetails skuDetails : skuDetailsList) {
                                                            String sku = skuDetails.getSku();
                                                            String price = skuDetails.getPrice();
                                                            if ("year_subscription".equals(sku) ||"6months_subscription".equals(sku) ||"monthly_subscription".equals(sku) || "reading_club".equals(sku) || "premium_annually".equals(sku) || "premium_6months".equals(sku)) {
                                                                premiumUpgradePrice = price;
                                                                Purchase.PurchasesResult purchasesResult = billingClient.queryPurchases(BillingClient.SkuType.SUBS);
                                                                List<Purchase> purchasesList = purchasesResult.getPurchasesList();
                                                                //


                                                                if (purchasesList != null && !purchasesList.isEmpty()) {
                                                                    for (Purchase purchase : purchasesList) {
                                                                        String skuName = purchase.getSku();
                                                            /*String ptoken = purchase.getPurchaseToken();
                                                             me1 = purchasesList.get(0).getOrderId();
                                                            int me2 = purchasesList.get(0).getPurchaseState();
                                                            int me3 = purchasesList.size();*/
                                                            /*toast.setText(skuName);
                                                            toast.show();*/
                                                                        if ("year_subscription".equals(sku) ||"6months_subscription".equals(sku) ||"monthly_subscription".equals(sku) || skuName.equals("reading_club") || skuName.equals("premium_6months") || skuName.equals("premium_annually")) {
                                                                            // Intent homeIntent = new Intent(getApplicationContext(), SubPHomeMainActivity.class);
                                                                            //Intent homeIntent = new Intent(getApplicationContext(), HomeMainActivity.class);

                                                                            sharedPreferencesAds = getSharedPreferences("AdsDecision", MODE_PRIVATE);
                                                                            SharedPreferences.Editor editor = sharedPreferencesAds.edit();
                                                                            editor.putInt("Ads", 0);
                                                                            editor.putInt("Sub", 1);
                                                                            editor.apply();
                                                                            Subscribed = sharedPreferencesAds.getInt("Sub", 5);
                                                                            //addProverbs();

                                                                            mFirebaseAnalytics.setUserProperty("Monthly_Premium", "Premium");

                                                                            Intent homeIntent = new Intent(getApplicationContext(), SubPHomeMainActivity.class);
                                                                            startActivity(homeIntent);

                                                                        }

                                                                    }
                                                                    //toast.setText(Integer.toString(me3));
                                                                } else {

                                                                    mFirebaseAnalytics.setUserProperty("Monthly_Premium", "Regular");
                                                                    Intent homeIntent = new Intent(getApplicationContext(), HomeMainActivity.class);
                                                                    // Intent homeIntent = new Intent(getApplicationContext(), SubPHomeMainActivity.class);
                                                                    // Intent homeIntent = new Intent(getApplicationContext(), RealTimeDatabase.class);
                                                                    startActivity(homeIntent);
                                                                    finish();
                                                                }

                                                            } /*else if ("gas".equals(sku))
                                                gasPrice = price;
                                            }*/
                                                        }
                                                    } else {
                                                        times++;
                                                        if (times < 5) {
                                                            toast.setText("No Internet Connection");
                                                            toast.show();
                                                            if (!isNetworkAvailable()) {
                                                                Intent homeIntent = new Intent(getApplicationContext(), HomeMainActivity.class);
                                                                startActivity(homeIntent);
                                                                finish();
                                                            } else {
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
                    },1500);
                    ///In App Purchase
                }
            }
            @Override
            public void onBillingServiceDisconnected() {

                times++;
                times++;
                if (times < 2){
                    toast.setText("Internet Disconnected");
                    toast.show();

                    setUpBillingClient();
                }
                else {
                    Intent homeIntent = new Intent(getApplicationContext(), HomeMainActivity.class);
                    //Toast.makeText(MainActivity.this, "Disconnected O", Toast.LENGTH_SHORT).show();
                    startActivity(homeIntent);
                    finish();
                }

            }
        });
        //billingClient.endConnection();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Appodeal.initialize(this, APPODEAL_KEY, Appodeal.BANNER | Appodeal.INTERSTITIAL, true);

       // Appodeal.setLogLevel(Log.LogLevel.verbose);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        //Toast.makeText(this, "Before add: "+ proverbsArrayList.size(), Toast.LENGTH_SHORT).show();

        sharedPreferencesAds = getSharedPreferences("AdsDecision",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferencesAds.edit();
        editor.putInt("Ads",1);
        editor.putInt("Sub",5);  // not subscribed
        //editor.putInt("Sub",1);  //subscribed
       // editor.putInt("Lifetime",5); //not subscribed
       // editor.putInt("Lifetime",1); //subscribed
        editor.apply();

        Lifetime = sharedPreferencesAds.getInt("Lifetime",5); //runtime
        Subscribed = sharedPreferencesAds.getInt("Sub", 5);
       Lifetime = 1;  //Subscribed
      //  Lifetime = 5;

        //Toast.makeText(this, "My: "+ Lifetime, Toast.LENGTH_SHORT).show();

        toast = Toast.makeText(this, "", Toast.LENGTH_LONG);

       // Intent homeIntent = new Intent(getApplicationContext(), SubPHomeMainActivity.class);
       // Intent homeIntent = new Intent(getApplicationContext(), HomeMainActivity.class);
        //Intent homeIntent = new Intent(getApplicationContext(), QuizTimedAll.class);
        //Intent homeIntent = new Intent(getApplicationContext(), InAppActivity.class);
        //startActivity(homeIntent);


        ////////

   new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Lifetime==1){

                    sharedPreferencesAds = getSharedPreferences("AdsDecision",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferencesAds.edit();
                    editor.putInt("Sub",1);
                    editor.putInt("Ads",0);
                    editor.apply();
                    Subscribed =1;
                   // Subscribed = sharedPreferencesAds.getInt("Sub", 5);

                    //Intent homeIntent = new Intent(getApplicationContext(), InAppActivity.class);
                   Intent homeIntent = new Intent(getApplicationContext(), SubPHomeMainActivity.class);
                    startActivity(homeIntent);
                    mFirebaseAnalytics.setUserProperty("Monthly_Premium", "Premium");
                    mFirebaseAnalytics.setUserProperty("Monthly_Premium", "Lifetime");
                   // Toast.makeText(MainActivity.this, "You ", Toast.LENGTH_SHORT).show();
                }else{
                   // Toast.makeText(MainActivity.this, "Hi "+Lifetime, Toast.LENGTH_SHORT).show();
                    setUpBillingClient();
                }
            }
        }, SPLASH_TIME_OUT);

       ///////////////////

        //int sub = sharedPreferencesAds.getInt("Sub",10);
/*if(sub!=0){
    proverbsArrayList.add( 0, new Proverbs( "Seious o", "If it comes it affects mother", "If trouble comes it affects your closest relatives"));
}*/



    }

   @Override
    protected void onResume() {

   // Intent homeIntent = new Intent(getApplicationContext(), SubPHomeMainActivity.class);
   // Intent homeIntent = new Intent(getApplicationContext(), QuizTimedAll.class);
      // Intent homeIntent = new Intent(getApplicationContext(), HomeMainActivity.class);
       // Intent homeIntent = new Intent(getApplicationContext(), InAppActivity.class);
      //  startActivity(homeIntent);
        super.onResume();

        /////////////

  new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (Lifetime==1){
                    sharedPreferencesAds = getSharedPreferences("AdsDecision",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferencesAds.edit();
                    editor.putInt("Sub",1);
                    editor.putInt("Ads",0);
                    editor.apply();
                    Subscribed = 1;
                    //Subscribed = sharedPreferencesAds.getInt("Sub", 5);
                    //addProverbs();
                    //Toast.makeText(MainActivity.this, "Me1 Only ", Toast.LENGTH_SHORT).show();
                    Intent homeIntent = new Intent(getApplicationContext(), SubPHomeMainActivity.class);
                   // Intent homeIntent = new Intent(getApplicationContext(), InAppActivity.class);
                    startActivity(homeIntent);
                }else{
                  //  Toast.makeText(MainActivity.this, "Me1 "+Lifetime, Toast.LENGTH_SHORT).show();
                    setUpBillingClient();
                }
            }
        }, SPLASH_TIME_OUT);



        ///////////////
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
