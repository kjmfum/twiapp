package com.learnakantwi.twiguides;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;
import java.util.List;

public class ReadingActivityTwoLettersMain extends AppCompatActivity {

    static ArrayList<String> readingAlphabetArray;
    CharSequence vowel;
    HorizontalScrollView horizontalScrollView;
    ListView lvReadingAlphabets;
    AdView mAdView;
    AdView mAdView1;
    Toast toast;
   // int testShared;




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //MenuInflater menuInflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.main_menu_simple, menu);

        final MenuItem item = menu.findItem(R.id.menusearch);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(ReadingActivityTwoLettersMain.this, query, Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                final ArrayList<String> results = new ArrayList<>();
                for (String x: readingAlphabetArray ){

                    if(x.toLowerCase().contains(newText.toLowerCase()) ){
                        results.add(x);
                    }

                    ((ReadingMainAdapter)lvReadingAlphabets.getAdapter()).update(results);
                }

                lvReadingAlphabets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        vowel = results.get(position);
                        goToTwoLetters();

                    }
                });

                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()){
            case R.id.main:
                goToMain();
                return  true;
            /*case R.id.quiz1:
                goToQuizFamily();
                return  true;
            case R.id.downloadAudio:
                downloadClick();
                return true;*/
            case R.id.videoCourse:
                //Log.i("Menu Item Selected", "Alphabets");
                goToWeb();
                return  true;
            default:
                return false;
        }
    }

    public void goToMain(){
        Intent intent = new Intent(getApplicationContext(), HomeMainActivity.class);
        startActivity(intent);
    }

    public void goToWeb() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.udemy.com/course/learn-akan-twi/?referralCode=6D321CE6AEE1834CCB0F"));
        startActivity(intent);
    }


    public void goToTwoLetters() {
       // Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.udemy.com/course/learn-akan-twi/?referralCode=6D321CE6AEE1834CCB0F"));
        Intent intent = new Intent(this, ReadingActivityTwoLetters.class);
        intent.putExtra("vowel", vowel);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading);


        readingAlphabetArray = new ArrayList<>();
        readingAlphabetArray.add("A");
        readingAlphabetArray.add("E");
        readingAlphabetArray.add("Ɛ");
        readingAlphabetArray.add("I");
        readingAlphabetArray.add("O");
        readingAlphabetArray.add("Ɔ");
        readingAlphabetArray.add("U");

//        tvLetterA = findViewById(R.id.tvVowelA);
  //      horizontalScrollView = findViewById(R.id.horizontalScrollView);
        lvReadingAlphabets = findViewById(R.id.lvReadingAlphabet);


       // ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, readingAlphabetArray);
        ReadingMainAdapter readingMainAdapter = new ReadingMainAdapter ( this, readingAlphabetArray);
        lvReadingAlphabets.setAdapter(readingMainAdapter);
        //lvReadingAlphabets.setAdapter(arrayAdapter);

        lvReadingAlphabets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                vowel = readingAlphabetArray.get(position);
                goToTwoLetters();

            }
        });

/*

        final SharedPreferences sharedPreferencesAds = this.getSharedPreferences("AdsDecision", MODE_PRIVATE);
        testShared = sharedPreferencesAds.getInt("Ads", 5);
*/

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView1 = findViewById(R.id.adView1);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView1.loadAd(adRequest);



      /*  if (testShared != 0) {
            MobileAds.initialize(this, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {
                }
            });
            mAdView1 = findViewById(R.id.adView1);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView1.loadAd(adRequest);

            MobileAds.initialize(this, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {
                }
            });


           *//* mAdView1 = findViewById(R.id.adView1);
            AdRequest adRequest1 = new AdRequest.Builder().build();
            mAdView1.loadAd(adRequest1);*//*

        }*/



        /*tvLetterA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vowel = tvLetterA.getText().toString();
                goToTwoLetters();
            }
        });*/



    }
}
