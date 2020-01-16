package com.learnakantwi.twiguides;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import hotchemi.android.rate.AppRate;

import static android.Manifest.permission.INTERNET;

//import android.support.v7.app.AppCompatActivity;

public class HomeMainActivity extends AppCompatActivity implements PurchasesUpdatedListener {
    //  app:adUnitId="ca-app-pub-6999427576830667~6251296006"ˆ

    BillingClient billingClient;
    String premiumUpgradePrice;
    Button buyButton;
    Toast toast;






    static ArrayList<HomeMainButton> homeMainButtonArrayList;
    public InterstitialAd mInterstitialAd;
    ListView homeListView;
    AdView mAdView;
    MediaPlayer mediaPlayer;
    SharedPreferences subscriptionStatePreference;
    boolean subscriptionState;
   /* FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    DatabaseReference myRef;*/



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //MenuInflater menuInflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.vocabulary_menu, menu);

        final MenuItem item = menu.findItem(R.id.menusearch);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(getApplicationContext(), query, Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                final ArrayList<HomeMainButton> results = new ArrayList<>();
                for (HomeMainButton x: homeMainButtonArrayList ){

                    if(x.getNameofActivity().toLowerCase().contains(newText.toLowerCase())

                    ){
                        results.add(x);
                    }

                    ((HomeMainAdapter)homeListView.getAdapter()).update(results);

                   homeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            String me1 = results.get(position).getNameofActivity();


                            switch (me1){
                                case "Alphabets":
                                    goToAlphabets();
                                    return;
                                case "Proverbs":
                                    goToProverbs();
                                    return;
                                case "Children":
                                    goToChildren();
                                    return;
                                case "Animals":
                                    goToAnimals();
                                    return;
                                case "Body Parts":
                                    goToBodyparts();
                                    return;
                                case "Colours":
                                    goToColours();
                                    return;
                                case "Days of Week":
                                    goToDaysOfWk();
                                    return;
                                case "Expressions":
                                    goToCommonExpressionsa();
                                    return;
                                case "Family":
                                    goToFamily();
                                    return;
                                case "Food":
                                    goToFood();
                                    return;
                                case "Months":
                                    goToMonths();
                                    return;
                                case "Numbers":
                                    goToNumber();
                                    return;
                                case "Pronouns":
                                    goToPronouns();
                                    return;
                                case "Time":
                                    goToTime();
                                    return;
                                case "Weather":
                                    goToWeather();
                                    return;
                                case "Business":
                                    goToBusiness();
                                    return;
                                case "Search":
                                    goToAll();
                            }
                        }
                    });
                }


                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
           /*case R.id.settings:
                Log.i("Menu Item Selected", "Settings");
                playAll();
                return true;
            case R.id.alphabets:
                Log.i("Menu Item Selected", "Alphabets");
                return  true;*/

            case R.id.quiz1:
                goToQuizAll();
                return true;
            case R.id.searchAll:
                goToAll();
                return true;
            case R.id.videoCourse:
                goToWeb();
                return true;
            case R.id.rate:
                rateMe();
                return true;
            case R.id.share:
                shareApp();
                return true;
            case R.id.dailyTwiAlert:
                tunOnDailyTwi();
                return true;
            default:
                return false;
        }
    }

    public void shareApp(){
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        //sharingIntent.setAction("http://play.google.com/store/apps/details?id=" + getPackageName());
        sharingIntent.setType("text/plain");
        String shareBody = "http://play.google.com/store/apps/details?id=" + getPackageName();
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Please install this Nice Android Twi App");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    public void rateMe() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + getPackageName())));
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
        }
    }

    public void goToMain(){
        Intent intent = new Intent(getApplicationContext(), Home.class);
        startActivity(intent);
    }



    public void goToQuizAll() {
        Intent intent = new Intent(getApplicationContext(), QuizAll.class);
        startActivity(intent);
    }

    public void goToQuizHome() {
        Intent intent = new Intent(getApplicationContext(), QuizHome.class);
        startActivity(intent);
    }

    public void goToProverbs() {
        Intent intent = new Intent(getApplicationContext(), ProverbsHome.class);
        startActivity(intent);
    }

    public void goToBusiness(){
        Intent intent = new Intent(getApplicationContext(), BusinessActivity.class);
        startActivity(intent);

    }

    public void goToWeb() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.udemy.com/course/learn-akan-twi/?referralCode=6D321CE6AEE1834CCB0F"));
        startActivity(intent);
    }

    public void goToAlphabets() {
        Intent intent = new Intent(getApplicationContext(), AlphabetsActivity.class);
        startActivity(intent);
    }

    public void goToNumber() {
        Intent intent = new Intent(getApplicationContext(), NumbersActivity.class);
        startActivity(intent);
    }

    public void goToDaysOfWk() {
        Intent intent = new Intent(getApplicationContext(), DaysOfWeekActivity.class);
        startActivity(intent);
    }

    public void goToTime() {
        Intent intent = new Intent(getApplicationContext(), TimeActivity.class);
        startActivity(intent);
    }

    public void goToFamily() {
        Intent intent = new Intent(getApplicationContext(), FamilyActivity.class);
        startActivity(intent);
    }

    public void goToWeather() {
        Intent intent = new Intent(getApplicationContext(), WeatherActivity.class);
        startActivity(intent);
    }

    public void goToMonths() {
        Intent intent = new Intent(getApplicationContext(), MonthsActivity.class);
        startActivity(intent);
    }

    public void goToPronouns() {
        Intent intent = new Intent(getApplicationContext(), PronounsActivity.class);
        startActivity(intent);
    }

    public void goToColours() {
        Intent intent = new Intent(getApplicationContext(), ColoursActivity.class);
        startActivity(intent);
    }

    public void goToAnimals() {
        Intent intent = new Intent(getApplicationContext(), AnimalsActivity.class);
        startActivity(intent);
    }

    public void goToBodyparts() {
        Intent intent = new Intent(getApplicationContext(), BodypartsActivity.class);
        startActivity(intent);
    }

    public void goToFood() {
        Intent intent = new Intent(getApplicationContext(), FoodActivity.class);
        startActivity(intent);
    }

    public void goToCommonExpressionsa() {
        Intent intent = new Intent(getApplicationContext(), CommonExpressionsaActivity.class);
        startActivity(intent);
    }

    public void goToAll() {
        Intent intent = new Intent(getApplicationContext(), AllActivity.class);
        startActivity(intent);
    }

    public void goToChildren() {
       // Intent intent = new Intent(getApplicationContext(), ChildrenNumberCount.class);
        Intent intent = new Intent(getApplicationContext(), ChildrenHome.class);
        startActivity(intent);
    }

    public void tunOnDailyTwi() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.learnakantwi.twiguides", Context.MODE_PRIVATE);
        String dailyTwiPreference = sharedPreferences.getString("DailyTwiPreference", "Yes");
        sharedPreferences.edit().putString("DailyTwiPreference", "Yes").apply();
        Toast.makeText(this, "Daily Twi Alerts Turned On", Toast.LENGTH_SHORT).show();
    }

    public void goToSubscriptionPage (View v){
        Toast.makeText(this, String.valueOf(subscriptionState), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), InAppActivity.class);
        startActivity(intent);
    }

    /*public void advert() {


        final SharedPreferences sharedPreferences = this.getSharedPreferences("com.learnakantwi.twiguides", Context.MODE_PRIVATE);
      //  sharedPreferences.edit().putString("AdvertPreference", "No").apply();
        String advertPreference = sharedPreferences.getString("AdvertPreference", "No");


        assert advertPreference != null;
        if (!advertPreference.equals("Yes")) {
            new AlertDialog.Builder(this)
                    .setIcon(R.drawable.learnakantwiimage)
                    .setTitle("Please support us")
                    .setMessage("Would You Like To View An Advert To Support Us?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sharedPreferences.edit().putString("AdvertPreference", "Yes").apply();
                            if (mInterstitialAd.isLoaded()) {
                                mInterstitialAd.show();
                            } else {
                                Log.d("TAG", "The interstitial wasn't loaded yet.");
                            }
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    sharedPreferences.edit().putString("AdvertPreference", "No").apply();
                                }
                            }
                    )
                    .show();
        } else {
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();

            } else {
                Log.d("TAG", "The interstitial wasn't loaded yet.");

            }
        }

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }
*/

   /* public void checkPermission(String permission, int requestCode)
    {
        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(
                Home.this,
                permission)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat
                    .requestPermissions(
                            Home.this,
                            new String[] {permission },
                            100);
        }
        else {
            Toast
                    .makeText(Home.this,
                            "Permission already granted",
                            Toast.LENGTH_SHORT)
                    .show();
        }
    }*/
//In your case, you do not need the LinearLayout and ImageView at all. Just add android:drawableLeft="@drawable/up_count_big" to your TextView.

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
                                                        int me2 = purchasesList.get(0).getPurchaseState();
                                                        int me3=  purchasesList.size();

                                                        //toast.setText(Integer.toString(me3));
                                                        toast.setText(me1);
                                                        toast.show();

                                                    }
                                                    else{
                                                        toast.setText("nothing subscribed");
                                                        toast.show();
                                                        goToAll();
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
                Toast.makeText(HomeMainActivity.this, "I got disconnected", Toast.LENGTH_SHORT).show();
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                setUpBillingClient();
            }


        });
        //billingClient.endConnection();
    }


    public void deleteDuplicatelDownload(){

        File myFiles = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/");


        File [] files1 = myFiles.listFiles();

        if (files1.length>0){
        for (File file : files1) {

            //for (int j = 0; j < files1.length; j++)
            // toast.setText(String.valueOf(files1.length));
            //toast.show();
            if (file.getName().contains("-")) {

                boolean wasDeleted = file.delete();


                if (!wasDeleted){
                    System.out.println("Was not deleted");
                }


                //toast.setText("Deleted");
                //toast.show();
            }

            /*String bb = allArrayList.get(j).getTwiMain();
            bb= bb.toLowerCase();
            boolean dd = bb.contains("ɔ");
            boolean ee = bb.contains("ɛ");
            if (dd || ee) {
                bb = bb.replace("ɔ", "x");
                bb = bb.replace("ɛ", "q");
            }

            if (bb.contains(" ") || bb.contains("/") || bb.contains(",") || bb.contains("(") || bb.contains(")") || bb.contains("-") || bb.contains("?") || bb.contains("'") | bb.contains("...")) {
                bb = bb.replace(" ", "");
                bb = bb.replace("/", "");
                bb = bb.replace(",", "");
                bb = bb.replace("(", "");
                bb = bb.replace(")", "");
                bb = bb.replace("-", "");
                bb = bb.replace("?", "");
                bb = bb.replace("'", "");
                bb= bb.replace("...","");*/
            }
           /* File myFiles = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/" + bb + ".m4a");
           * if (myFiles.exists()) {
                myFiles.delete();
            }*/



            /*for (File f: myFiles.listFiles()){
                long space= f.getTotalSpace();

                //f.delete();
            }
*/
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home1);

        //firebaseAuth = FirebaseAuth.getInstance();


        toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);

        try {
            deleteDuplicatelDownload();
        }
        catch (NullPointerException e){
            System.out.println("Error Null");
        }


        subscriptionStatePreference = this.getSharedPreferences("com.learnakantwi.twiguides", Context.MODE_PRIVATE);
        subscriptionState = subscriptionStatePreference.getBoolean("Paid",false);


//        String subscriptionState  = subscriptionStatePreference.getString("Subscription", "No");
//
//        SharedPreferences sharedPreferences = this.getSharedPreferences("com.learnakantwi.twiguides", Context.MODE_PRIVATE);
//        String dailyTwiPreference = sharedPreferences.getString("DailyTwiPreference", "Yes");
//
//        SharedPreferences sharedPreferences1 = this.getSharedPreferences("com.learnakantwi.twiguides", Context.MODE_PRIVATE);
//        sharedPreferences1.edit().putString("Downloading", "No").apply();

        // Function to check and request permission
       // checkPermission(INTERNET, 100);

        if (Build.VERSION.SDK_INT > 22) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    INTERNET,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.SET_ALARM
            }, 1);

            if (Build.VERSION.SDK_INT > 27) {
                requestPermissions(new String[]{Manifest.permission.FOREGROUND_SERVICE
                }, 1);
            }
        }

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());


        //ca-app-pub-7384642419407303/9880404420
        //ca-app-pub-3940256099942544/1033173712 test


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        AppRate.with(this)
                .setInstallDays(0)
                .setLaunchTimes(3)
                .setRemindInterval(2)
                .monitor();

        AppRate.showRateDialogIfMeetsConditions(this);

        /////////////
       /* findViewById(R.id.homeAdvertButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                advert();
            }
        });*/
/////////////

        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713

        homeMainButtonArrayList = new ArrayList<>();
        homeListView = findViewById(R.id.homeListView);


        homeMainButtonArrayList.add(new HomeMainButton("Vocabulary", R.drawable.vocabularyimage));
        homeMainButtonArrayList.add(new HomeMainButton("Quiz", R.drawable.quizimage));
        homeMainButtonArrayList.add(new HomeMainButton("Proverbs", R.drawable.proverbsimage));
        homeMainButtonArrayList.add(new HomeMainButton("Children", R.drawable.childrenimage));
       /* homeMainButtonArrayList.add(new HomeMainButton("Food", R.drawable.foodimage));
        homeMainButtonArrayList.add(new HomeMainButton("Alphabets", R.drawable.alphabetsimage));
        homeMainButtonArrayList.add(new HomeMainButton("Time", R.drawable.time));*/


        //Collections.sort(this.homeButtonArrayList);

     //   homeButtonArrayList.add(new HomeButton("Search", R.drawable.allimage));

        HomeMainAdapter homeMainAdapter = new HomeMainAdapter(this, homeMainButtonArrayList);
        homeListView.setAdapter(homeMainAdapter);




        homeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String me1 = homeMainButtonArrayList.get(position).getNameofActivity();


                switch (me1){
                    case "Vocabulary":
                        goToMain();
                        return;
                    case "Children":
                        goToChildren();
                        return;
                    case "Proverbs":
                        goToProverbs();
                        return;
                    case "Quiz":
                        goToQuizHome();
                        return;
                    case "Colours":
                        goToColours();
                        return;
                    case "Days of Week":
                        goToDaysOfWk();
                        return;
                    case "Expressions":
                        goToCommonExpressionsa();
                        return;
                    case "Family":
                        goToFamily();
                        return;
                    case "Food":
                        goToFood();
                        return;
                    case "Months":
                        goToMonths();
                        return;
                    case "Numbers":
                        goToNumber();
                        return;
                    case "Pronouns":
                        goToPronouns();
                        return;
                    case "Time":
                        goToTime();
                        return;
                    case "Weather":
                        goToWeather();
                        return;
                    case "Business":
                        goToBusiness();
                        return;
                    case "Search":
                        goToAll();
                }
            }
        });





        //MobileAds.initialize(this, "ca-app-pub-6999427576830667~6251296006");


    }

    @Override
    public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> purchases) {
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK
                && purchases != null) {
           toast.setText("Okay1");
           toast.show();
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
}



