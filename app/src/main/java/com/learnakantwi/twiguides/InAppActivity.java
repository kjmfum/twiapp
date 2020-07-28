package com.learnakantwi.twiguides;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchaseHistoryRecord;
import com.android.billingclient.api.PurchaseHistoryResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;

import java.util.ArrayList;
import java.util.List;

import static com.learnakantwi.twiguides.FamilyActivity.familyArrayList;

public class InAppActivity extends AppCompatActivity implements PurchasesUpdatedListener, PurchaseHistoryResponseListener {


    BillingClient billingClient;
    String premiumUpgradePrice;
    Button btBuyMonthly;
    Button btBuyHalfYear;
    Button btLifeTimeAccess;

    int Lifetime=0;

    Button btBuyAnnual;
    SkuDetails skuDetails;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    AcknowledgePurchaseResponseListener acknowledgePurchaseResponseListener;
    SharedPreferences subscriptionStatePreference;
    Toast toast;
    private AppBarConfiguration mAppBarConfiguration;
    SharedPreferences sharedPreferencesAds;
    int times =0;
    int times1 =0;


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //MenuInflater menuInflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.main_menu, menu);

        final MenuItem item = menu.findItem(R.id.menusearch);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
               // Toast.makeText(FamilyActivity.this, query, Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<Family> results = new ArrayList<>();
                for (Family x: familyArrayList ){

                    if(x.getEnglishFamily().toLowerCase().contains(newText.toLowerCase()) || x.getTwiFamily().toLowerCase().contains(newText.toLowerCase())){
                        results.add(x);
                    }

                    //((FamilyAdapter)listView.getAdapter()).update(results);
                }


                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);


    }

  /*  @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

     if (actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        else{
        switch (item.getItemId()){
            case R.id.main:
                goToMain();
                return  true;
            *//*case R.id.quiz1:
                goToQuizFamily();
                return  true;
            case R.id.downloadAudio:
                downloadClick();
                return true;
            case R.id.videoCourse:
                //Log.i("Menu Item Selected", "Alphabets");
                goToWeb();
                return  true;*//*
            default:
                return false;
        }
        }
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()){
            case R.id.main:
                goToMain();
                return  true;
            /*case R.id.videoCourse:
                goToWeb();
            case R.id.downloadAudio:
                downloadClick();
                return true;
            case R.id.quiz1:
                goToQuizFood();*/
            default:
                return false;
        }
    }

    public void goToMain(){
        Intent intent = new Intent(getApplicationContext(), HomeMainActivity.class);
        startActivity(intent);
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

    void handlePurchase(Purchase purchase) {
        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
            // Grant entitlement to the user.
            //Toast.makeText(this, "You bought It", Toast.LENGTH_SHORT).show();
            subscriptionStatePreference.edit().putBoolean("Paid", true).apply();
            // Acknowledge the purchase if it hasn't already been acknowledged.
            if (!purchase.isAcknowledged()) {
                AcknowledgePurchaseParams acknowledgePurchaseParams =
                        AcknowledgePurchaseParams.newBuilder()
                                .setPurchaseToken(purchase.getPurchaseToken())
                                .build();
                AcknowledgePurchaseResponseListener acknowledgePurchaseResponseListener = new AcknowledgePurchaseResponseListener() {
                    @Override
                    public void onAcknowledgePurchaseResponse(BillingResult billingResult) {

                        sharedPreferencesAds = getSharedPreferences("AdsDecision",MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferencesAds.edit();
                        editor.putInt("Ads",0);
                        editor.putInt("Sub",1);
                        editor.apply();
                        toast.setText("Acknowledged");
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

    public void setUpBillingClient(String subName) {
        if (subName.contains("access1")){
           // if (subName.contains("likoio")){
            billingClient = BillingClient.newBuilder(this)
                    .setListener(this)
                    .enablePendingPurchases()
                    .build();
            setUpBillingLifetime(subName);
        }else{
            // Toast.makeText(this, "This " +subName, Toast.LENGTH_SHORT).show();
            billingClient = BillingClient.newBuilder(this)
                    .setListener(this)
                    .enablePendingPurchases()
                    .build();
            setUpBilling(subName);
        }


    }

        public void setUpBilling(final String subName){

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.


                    List<String> skuList = new ArrayList<>();
                     skuList.add("reading_club");
                    skuList.add("monthly_subscription");
                    skuList.add("year_subscription");
                    skuList.add("6months_subscription");
                    skuList.add("premium_6months");
                    skuList.add("premium_annually");
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
                                           // if ("reading_club".equals(sku))
                                               // if ("premium_annually".equals(sku))
                                                    if (subName.equals(sku))
                                                {
                                                premiumUpgradePrice = price;

                                                BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                                                        .setSkuDetails(skuDetails)
                                                        .build();
                                                    billingClient.launchBillingFlow(InAppActivity.this, flowParams);
                                                    // BillingResult responseCode = billingClient.launchBillingFlow(InAppActivity.this,flowParams);
                                                   // Toast.makeText(InAppActivity.this, "I got it done "+ subName , Toast.LENGTH_SHORT).show();
                                                } /*else if ("premium_annually".equals(sku)) {
                                                premiumUpgradePrice = price;
                                            }*/
                                            }
                                        }
                                    else
                                    {

                                    }
                                    }



                                }
                            });
                }
            }
            @Override
            public void onBillingServiceDisconnected() {
                Toast.makeText(InAppActivity.this, "I got disconnected", Toast.LENGTH_SHORT).show();
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                setUpBillingClient(subName);
            }


        });
        //billingClient.endConnection();
    }

    public void setUpBillingLifetime(final String subName){

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.


                    List<String> skuList = new ArrayList<>();
                    skuList.add("lifetime_full_access1");
                   // skuList.add("lifetime_full_access");
                   //skuList.add("likoio");
                    // skuList.add("gas");
                    SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
                    params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
                    billingClient.querySkuDetailsAsync(params.build(),
                            new SkuDetailsResponseListener() {
                                @Override
                                public void onSkuDetailsResponse(BillingResult billingResult, List<SkuDetails> skuDetailsList) {
                                    // Process the result.

                                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
                                        toast.setText("Already Purchased 1");
                                        toast.show();

                                    }
                                    else if (btLifeTimeAccess.getText().toString().toLowerCase().contains("restore")){
                                        Intent homeIntent = new Intent(getApplicationContext(), SubPHomeMainActivity.class);
                                        startActivity(homeIntent);
                                    }
                                    else{
                                        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && skuDetailsList != null) {
                                            for (SkuDetails skuDetails : skuDetailsList) {
                                                String sku = skuDetails.getSku();
                                                String price = skuDetails.getPrice();
                                               // Toast.makeText(InAppActivity.this, "Price: "+ price+" " + sku, Toast.LENGTH_SHORT).show();
                                                // if ("reading_club".equals(sku))
                                                // if ("premium_annually".equals(sku))
                                               if (subName.equals(sku))

                                                //if ("likoio".equals(sku))
                                                {
                                                    premiumUpgradePrice = price;

                                                    BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                                                            .setSkuDetails(skuDetails)
                                                            .build();
                                                    billingClient.launchBillingFlow(InAppActivity.this, flowParams);
                                                    // BillingResult responseCode = billingClient.launchBillingFlow(InAppActivity.this,flowParams);
                                                    // Toast.makeText(InAppActivity.this, "I got it done "+ subName , Toast.LENGTH_SHORT).show();
                                                } /*else if ("premium_annually".equals(sku)) {
                                                premiumUpgradePrice = price;
                                            }*/
                                            }
                                        }
                                        else
                                        {

                                        }
                                    }



                                }
                            });
                }
            }
            @Override
            public void onBillingServiceDisconnected() {
                Toast.makeText(InAppActivity.this, "I got disconnected", Toast.LENGTH_SHORT).show();
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.

                //setUpBillingClient(subName);
            }


        });
        //billingClient.endConnection();
    }


    public void getPricesOnButton(){


        billingClient = BillingClient.newBuilder(this)
                .setListener(this)
                .enablePendingPurchases()
                .build();

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    List<String> skuList = new ArrayList<>();
                    List<String> skuList1 = new ArrayList<>();
                    skuList1.add("lifetime_full_access");
                    skuList1.add("lifetime_full_access1");
                   skuList1.add("likoio");


                    skuList.add("monthly_subscription");
                    skuList.add("year_subscription");
                    skuList.add("6months_subscription");

/*
                    skuList.add("reading_club");
                    skuList.add("premium_6months");
                    skuList.add("premium_annually");*/
                    // skuList.add("gas");
                    SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();


                    params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS);
                    billingClient.querySkuDetailsAsync(params.build(),
                            new SkuDetailsResponseListener() {
                                @Override
                                public void onSkuDetailsResponse(BillingResult billingResult, List<SkuDetails> skuDetailsList) {
                                    // Process the result.

                                        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && skuDetailsList != null) {
                                            for (SkuDetails skuDetails : skuDetailsList) {
                                                String sku = skuDetails.getSku();
                                                String premiumUpgradePrice = skuDetails.getPrice();
                                                // if ("reading_club".equals(sku))
                                                // if ("premium_annually".equals(sku))

                                                    if (sku.contains("monthly_subscription")){
                                                        btBuyMonthly.setText(premiumUpgradePrice+ "\nBilled Monthly " );
                                                    }
                                                    else if (sku.contains("6months_subscription")){
                                                        btBuyHalfYear.setText(premiumUpgradePrice+"\nBilled every 6 Months");
                                                }

                                                    else if (sku.contains("year_subscription")){
                                                        btBuyAnnual.setText(premiumUpgradePrice+ "\nBilled Annually");
                                                    }
                                                }
                                            }
                                        }
                            });

                    params.setSkusList(skuList1).setType(BillingClient.SkuType.INAPP);
                    billingClient.querySkuDetailsAsync(params.build(),
                            new SkuDetailsResponseListener() {
                                @Override
                                public void onSkuDetailsResponse(BillingResult billingResult, List<SkuDetails> skuDetailsList) {
                                    // Process the result.

                                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && skuDetailsList != null) {
                                        for (SkuDetails skuDetails : skuDetailsList) {
                                            String sku = skuDetails.getSku();
                                            //Toast.makeText(InAppActivity.this, "Hi "+ sku, Toast.LENGTH_SHORT).show();
                                            String premiumUpgradePrice = skuDetails.getPrice();
                                          /*  if (sku.contains("access")){
                                                btLifeTimeAccess.setText("Restore Lifetime \nAccess Content");
                                            }*/
                                          if (sku.contains("access1") && Lifetime ==1){
                                                btLifeTimeAccess.setText("Restore Lifetime \nAccess Content");
                                            }
                                            else if (sku.contains("access1")){
                                                    btLifeTimeAccess.setText(premiumUpgradePrice+"\n Lifetime - One Time Purchase");
                                                }
                                            /*else if (sku.contains("liko")){
                                                btBuyAnnual.setText(premiumUpgradePrice+"\n Try - One Time Purchase");
                                            }*/
                                            /*if (sku.contains("likoio")){
                                                btLifeTimeAccess.setText(premiumUpgradePrice+"\n Lifetime - Two Time Purchase");
                                            }*/
                                        }
                                    }
                                }
                            });
                }
            }
            @Override
            public void onBillingServiceDisconnected() {
                Toast.makeText(InAppActivity.this, "Internet disconnected", Toast.LENGTH_SHORT).show();
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }


        });
        //billingClient.endConnection();
    }

    public void getPricesOnButtonLifetime(){


        billingClient = BillingClient.newBuilder(this)
                .setListener(this)
                .enablePendingPurchases()
                .build();

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    List<String> skuList = new ArrayList<>();
                    skuList.add("lifetime_full_access");
                    // skuList.add("gas");
                    SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
                    params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
                    billingClient.querySkuDetailsAsync(params.build(),
                            new SkuDetailsResponseListener() {
                                @Override
                                public void onSkuDetailsResponse(BillingResult billingResult, List<SkuDetails> skuDetailsList) {
                                    // Process the result.

                                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && skuDetailsList != null) {
                                        for (SkuDetails skuDetails : skuDetailsList) {
                                            String sku = skuDetails.getSku();
                                            String premiumUpgradePrice = skuDetails.getPrice();
                                            // if ("reading_club".equals(sku))
                                            // if ("premium_annually".equals(sku))

                                            if (sku.contains("lifetime_full_access")){
                                                btLifeTimeAccess.setText(premiumUpgradePrice+ "\nLifetime " );
                                            }
                                            else if (sku.contains("6")){
                                                btBuyHalfYear.setText(premiumUpgradePrice+"\n 6 Months");
                                            }
                                            else if (sku.contains("annually")){
                                                btBuyAnnual.setText(premiumUpgradePrice+ "\n Annual");
                                            }
                                        }
                                    }
                                }
                            });
                }
            }
            @Override
            public void onBillingServiceDisconnected() {
                Toast.makeText(InAppActivity.this, "Internet disconnected", Toast.LENGTH_SHORT).show();
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }


        });
        //billingClient.endConnection();
    }

    public void BuyMe(View v){

        int idview = v.getId();

        Button blabla = v.findViewById(idview);

        String ButtonText = blabla.getText().toString().toLowerCase();


       // Toast.makeText(InAppActivity.this, "Ready "+ ButtonText, Toast.LENGTH_SHORT).show();

        if (ButtonText.contains("monthly")){
           // setUpBillingClient("reading_club");
            setUpBillingClient("monthly_subscription");

           // buyMeMonthly();
        }
        else if (ButtonText.contains("6")){
            //setUpBillingClient("premium_6months");
            setUpBillingClient("6months_subscription");
        }
        else if (ButtonText.contains("annual")){
            //setUpBillingClient("premium_annually");
            setUpBillingClient("year_subscription");
            //buyMeAnnually();
        }
        else if (ButtonText.contains("lifetime")){
            setUpBillingClient("lifetime_full_access1");
            //setUpBillingClient("likoio");
            //buyMeAnnually();
        }

        else{
            if (isNetworkAvailable()){
                setUpBillingClientCheck();
                times1=5;
                Toast.makeText(this, "Checking Subscriptions", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "Error: "+"\nPlease Connect to the Internet", Toast.LENGTH_LONG).show();
            }

        }
    }


    public void setUpBillingClientCheck() {
       // Toast.makeText(this, "I'm here", Toast.LENGTH_SHORT).show();
        billingClient = BillingClient.newBuilder(this)
                .setListener(this)
                .enablePendingPurchases()
                .build();
        setUpBillingCheck();
    }

    public void setUpBillingCheck(){

        //Toast.makeText(this, "Got Here", Toast.LENGTH_SHORT).show();
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    //
                    SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();

                    //In App


                    List<String> skuList = new ArrayList<>();
                    skuList.add("reading_club");
                    skuList.add("monthly_subscription");
                    skuList.add("year_subscription");
                    skuList.add("6months_subscription");
                    skuList.add("premium_6months");
                    skuList.add("premium_annually");

                    /*skuList.add("reading_club");
                    skuList.add("premium_annually");
                    skuList.add("premium_6months");*/
                    //SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
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
                                                if ("year_subscription".equals(sku) ||"6months_subscription".equals(sku) ||"monthly_subscription".equals(sku) || "reading_club".equals(sku) || "premium_annually".equals(sku) || "premium_6months".equals(sku)) {
                                                    premiumUpgradePrice = price;


                                                    Purchase.PurchasesResult purchasesResult = billingClient.queryPurchases(BillingClient.SkuType.SUBS);
                                                    List<Purchase> purchasesList = purchasesResult.getPurchasesList();

                                                    if (purchasesList !=null && !purchasesList.isEmpty()){
                                                        String me1;
                                                        //String me1 = purchasesList.get(0).getOrderId();
                                                        for (Purchase purchase : purchasesList) {
                                                            String skuName = purchase.getSku();
                                                            if ("year_subscription".equals(sku) ||"6months_subscription".equals(sku) ||"monthly_subscription".equals(sku) ||skuName.equals("reading_club") || skuName.equals("premium_6months") || skuName.equals("premium_annually")){

                                                                switch(skuName){
                                                                    case "reading_club":
                                                                        me1 = "\n\t AN ACTIVE MONTHLY SUBSCRIPTION";
                                                                        Toast.makeText(InAppActivity.this, "\t\t\t\t YOU ALREADY HAVE "+ me1 , Toast.LENGTH_SHORT).show();
                                                                        Intent homeIntent = new Intent(getApplicationContext(), SubPHomeMainActivity.class);
                                                                        startActivity(homeIntent);
                                                                        return;

                                                                    case "monthly_subscription":
                                                                        me1 = "\n\t AN ACTIVE MONTHLY SUBSCRIPTION";
                                                                        Toast.makeText(InAppActivity.this, "\t\t\t\t YOU ALREADY HAVE "+ me1 , Toast.LENGTH_SHORT).show();
                                                                         homeIntent = new Intent(getApplicationContext(), SubPHomeMainActivity.class);
                                                                        startActivity(homeIntent);
                                                                        return;

                                                                    case "premium_6months":
                                                                        me1 = "\n\t AN ACTIVE 6 MONTHS SUBSCRIPTION";
                                                                        Toast.makeText(InAppActivity.this, "\t\t\t\t YOU ALREADY HAVE "+ me1 , Toast.LENGTH_SHORT).show();
                                                                        homeIntent = new Intent(getApplicationContext(), SubPHomeMainActivity.class);
                                                                        startActivity(homeIntent);
                                                                        return;
                                                                    case "6months_subscription":
                                                                        me1 = "\n\t AN ACTIVE 6 MONTHS SUBSCRIPTION";
                                                                        Toast.makeText(InAppActivity.this, "\t\t\t\t YOU ALREADY HAVE "+ me1 , Toast.LENGTH_SHORT).show();
                                                                        homeIntent = new Intent(getApplicationContext(), SubPHomeMainActivity.class);
                                                                        startActivity(homeIntent);
                                                                        return;
                                                                    case "premium_annually":
                                                                        me1 = "\n\t AN ACTIVE ANNUAL SUBSCRIPTION";
                                                                        Toast.makeText(InAppActivity.this, "\t\t\t\t YOU ALREADY HAVE "+ me1 , Toast.LENGTH_SHORT).show();
                                                                        homeIntent = new Intent(getApplicationContext(), SubPHomeMainActivity.class);
                                                                        startActivity(homeIntent);
                                                                        return;

                                                                    case "year_subscription":
                                                                        me1 = "\n\t AN ACTIVE ANNUAL SUBSCRIPTION";
                                                                        Toast.makeText(InAppActivity.this, "\t\t\t\t YOU ALREADY HAVE "+ me1 , Toast.LENGTH_SHORT).show();
                                                                        homeIntent = new Intent(getApplicationContext(), SubPHomeMainActivity.class);
                                                                        startActivity(homeIntent);
                                                                        return;
                                                                }


                                                                sharedPreferencesAds = getSharedPreferences("AdsDecision",MODE_PRIVATE);
                                                                SharedPreferences.Editor editor = sharedPreferencesAds.edit();
                                                                editor.putInt("Ads", 0);
                                                                editor.apply();

                                                               /* Intent homeIntent = new Intent(getApplicationContext(), SubPHomeMainActivity.class);
                                                                startActivity(homeIntent);*/

                                                            }
                                                        }
                                                        //toast.setText(Integer.toString(me3));
                                                    }
                                                    else{
                                                        if(times1==5){
                                                            toast.setText("You Do Not Have Any Active Subscriptions");
                                                            toast.show();
                                                            times1=0;
                                                        }


                                                    }


                                                } /*else if ("gas".equals(sku)) {
                                                gasPrice = price;
                                            }*/
                                            }
                                        }
                                        else {
                                            times++;
                                            if (times < 5) {
                                                toast.setText("No Internet Connection");
                                                toast.show();
                                                setUpBillingClientCheck();
                                            }
                                            else{
                                                toast.setText("You Do Not Have Any Active Subscriptions");
                                                toast.show();
                                            }
                                        }

                                    }
                              }
                            });

            }
            }
            @Override
            public void onBillingServiceDisconnected() {
                times++;
                if (times < 5){
                    toast.setText("Internet Disconnected");
                    toast.show();
                    setUpBillingClientCheck();
                }
            }


        });
        //billingClient.endConnection();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_app);

        sharedPreferencesAds = getSharedPreferences("AdsDecision",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferencesAds.edit();
        editor.putInt("Ads",1);
        editor.apply();

        Lifetime = sharedPreferencesAds.getInt("Lifetime",5);

        btBuyMonthly =findViewById(R.id.buyMonthly);
        btBuyHalfYear =findViewById(R.id.buyHalfYear);
        btBuyAnnual =findViewById(R.id.buyAnnual);
        btLifeTimeAccess = findViewById(R.id.btLifetimeAccess);



        toast = Toast.makeText(this, "", Toast.LENGTH_LONG);

        getPricesOnButton();
        //getPricesOnButtonLifetime();


        setUpBillingClientCheck();


        Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
  /*      FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

  //////Temporary
    /*    DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();*/




/*        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);*/

        subscriptionStatePreference = this.getSharedPreferences("com.learnakantwi.twiguides", Context.MODE_PRIVATE);



/*
        toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
       getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       */


        /*buyButton= findViewById(R.id.buy);

        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ButtonText = buyButton.getText().toString();

                Toast.makeText(InAppActivity.this, "Ready", Toast.LENGTH_SHORT).show();

                if (ButtonText.contains("Monthly")){
                    Toast.makeText(InAppActivity.this, "Monthly", Toast.LENGTH_SHORT).show();
                    buyMeMonthly();
                }
                else if (ButtonText.contains("6")){
                    Toast.makeText(InAppActivity.this, " 6 Monthly", Toast.LENGTH_SHORT).show();
                    buyMeHalfYear();
                }
                else if (ButtonText.contains("Annual")){
                    Toast.makeText(InAppActivity.this, "Annual", Toast.LENGTH_SHORT).show();
                    buyMeAnnually();
                }
                else{
                    buyMeMonthly();
                }

            }
        });*/

    }


    @Override
    public void onPurchaseHistoryResponse(BillingResult billingResult, List<PurchaseHistoryRecord> list) {

    }

    @Override
    protected void onResume() {
        //Toast.makeText(this, "Resumed", Toast.LENGTH_SHORT).show();

        setUpBillingClientCheck();
        super.onResume();
    }
}
