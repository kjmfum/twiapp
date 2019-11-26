package com.learnakantwi.twiguides;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;

import static android.Manifest.permission.INTERNET;

public class Home extends AppCompatActivity {
    //  app:adUnitId="ca-app-pub-6999427576830667~6251296006"

    static ArrayList<HomeButton> homeButtonArrayList;
    ListView homeListView;
    AdView mAdView;
    AdView mAdView1;
    public InterstitialAd mInterstitialAd;
    MediaPlayer mediaPlayer;


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
                final ArrayList<HomeButton> results = new ArrayList<>();
                for (HomeButton x: homeButtonArrayList ){

                    if(x.getNameofActivity().toLowerCase().contains(newText.toLowerCase())

                    ){
                        results.add(x);
                    }

                    ((HomeAdapter)homeListView.getAdapter()).update(results);

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

            case R.id.main:
                goToMain();
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
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        //sharingIntent.setAction("http://play.google.com/store/apps/details?id=" + getPackageName());
        sharingIntent.setType("text/plain");
        String shareBody = "http://play.google.com/store/apps/details?id=" + getPackageName();
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Please install this Nice Android Twi App");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
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
        Intent intent = new Intent(getApplicationContext(), HomeMainActivity.class);
        startActivity(intent);
    }

    public void goToProverbs() {
        Intent intent = new Intent(getApplicationContext(), ProverbsActivity.class);
        startActivity(intent);
    }

    public void goToBusiness(){
        Intent intent = new Intent(getApplicationContext(), BusinessActivity.class);
        startActivity(intent);

    }

    public void goToWeb() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.udemy.com/course/learn-akan-twi/?couponCode=FDISCOUNT1"));
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

    public void advert() {


        final SharedPreferences sharedPreferences = this.getSharedPreferences("com.learnakantwi.twiguides", Context.MODE_PRIVATE);
      //  sharedPreferences.edit().putString("AdvertPreference", "No").apply();
        String advertPreference = sharedPreferences.getString("AdvertPreference", "No");


        assert advertPreference != null;
        if (!advertPreference.equals("Yes")) {
            new AlertDialog.Builder(this)
                    .setIcon(R.drawable.learnakantwiimage)
                    .setTitle("We need your support")
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizhome);

        ImageView topImage = findViewById(R.id.homeAdvertButton);
        topImage.setImageResource(R.drawable.vocabularyimage);

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
        mInterstitialAd.setAdUnitId("ca-app-pub-7384642419407303/9880404420");
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

      /*  MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView1 = findViewById(R.id.adView1);
        AdRequest adRequest1 = new AdRequest.Builder().build();
        mAdView1.loadAd(adRequest1);
*/


        /*AppRate.with(this)
                .setInstallDays(0)
                .setLaunchTimes(3)
                .setRemindInterval(2)
                .monitor();

        AppRate.showRateDialogIfMeetsConditions(this);*/

      /*  findViewById(R.id.homeAdvertButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                advert();
            }
        });*/


        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713

        homeButtonArrayList = new ArrayList<>();
        homeListView = findViewById(R.id.homeListView);

        homeButtonArrayList.add(new HomeButton("Family", R.drawable.familyimage));
       // homeButtonArrayList.add(new HomeButton("Proverbs", R.drawable.proverbsimage));
        //homeButtonArrayList.add(new HomeButton("Children", R.drawable.childrenimage));
        homeButtonArrayList.add(new HomeButton("Food", R.drawable.foodimage));
        homeButtonArrayList.add(new HomeButton("Alphabets", R.drawable.alphabetsimage));
        homeButtonArrayList.add(new HomeButton("Time", R.drawable.time));
        homeButtonArrayList.add(new HomeButton("Days of Week", R.drawable.monday));
        homeButtonArrayList.add(new HomeButton("Numbers", R.drawable.numbers));
        homeButtonArrayList.add(new HomeButton("Pronouns", R.drawable.pronounsimage));
        homeButtonArrayList.add(new HomeButton("Weather", R.drawable.weathersunimage));
        homeButtonArrayList.add(new HomeButton("Body Parts", R.drawable.lungsimage));
        homeButtonArrayList.add(new HomeButton("Months", R.drawable.calendar));
        homeButtonArrayList.add(new HomeButton("Expressions", R.drawable.expressionsimage));
        homeButtonArrayList.add(new HomeButton("Colours", R.drawable.colourimage));
        homeButtonArrayList.add(new HomeButton("Animals", R.drawable.animalsimage));
        homeButtonArrayList.add(new HomeButton("Business", R.drawable.businessimage));

        //Collections.sort(this.homeButtonArrayList);

        homeButtonArrayList.add(new HomeButton("Search", R.drawable.allimage));

        HomeAdapter homeAdapter = new HomeAdapter(this, homeButtonArrayList);
        homeListView.setAdapter(homeAdapter);




        homeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String me1 = homeButtonArrayList.get(position).getNameofActivity();


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





        //MobileAds.initialize(this, "ca-app-pub-6999427576830667~6251296006");


    }
}



