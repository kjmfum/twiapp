package com.learnakantwi.twiguides;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.Purchase.PurchasesResult;
import com.android.billingclient.api.PurchaseHistoryRecord;
import com.android.billingclient.api.PurchaseHistoryResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.android.billingclient.util.*;

import java.util.ArrayList;
import java.util.List;

import static com.learnakantwi.twiguides.FamilyActivity.familyArrayList;

public class InAppActivity extends AppCompatActivity implements PurchasesUpdatedListener, PurchaseHistoryResponseListener {


    BillingClient billingClient;
    String premiumUpgradePrice;
    Button buyButton;
    SkuDetails skuDetails;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    AcknowledgePurchaseResponseListener acknowledgePurchaseResponseListener;
    SharedPreferences subscriptionStatePreference;
    Toast toast;
    private AppBarConfiguration mAppBarConfiguration;




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

    @Override
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
            /*case R.id.quiz1:
                goToQuizFamily();
                return  true;
            case R.id.downloadAudio:
                downloadClick();
                return true;
            case R.id.videoCourse:
                //Log.i("Menu Item Selected", "Alphabets");
                goToWeb();
                return  true;*/
            default:
                return false;
        }
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

                        toast.setText("Acknowleged");
                        toast.show();
                    }

                };


                billingClient.acknowledgePurchase(acknowledgePurchaseParams, acknowledgePurchaseResponseListener);

            }
        }
    }


//    void queryPurchaseHistoryAsync( String skuType, PurchaseHistoryResponseListener listener){
//
//        skuType = billingClient.querySkuDetailsAsync(listener);
//    }

    public void buyMe() {
        setUpBillingClient();
    }

        /*BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                .setSkuDetails(skuDetails)
                .build();
        BillingResult responseCode = billingClient.launchBillingFlow(InAppActivity.this, flowParams);
       // int responseCode = billingClient.launchBillingFlow(flowParams);
    }*/

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
                                                BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                                                        .setSkuDetails(skuDetails)
                                                        .build();
                                                    billingClient.launchBillingFlow(InAppActivity.this, flowParams);
                                                    // BillingResult responseCode = billingClient.launchBillingFlow(InAppActivity.this,flowParams);
                                                    Toast.makeText(InAppActivity.this, "I got it done", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(InAppActivity.this, "I got disconnected", Toast.LENGTH_SHORT).show();
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                setUpBillingClient();
            }


        });
        //billingClient.endConnection();
    }


    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_app);


       toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);


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
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
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


        buyButton= findViewById(R.id.buy);

        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buyMe();
            }
        });

    }


    @Override
    public void onPurchaseHistoryResponse(BillingResult billingResult, List<PurchaseHistoryRecord> list) {

    }
}
