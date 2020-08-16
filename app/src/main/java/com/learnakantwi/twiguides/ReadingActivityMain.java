package com.learnakantwi.twiguides;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.appodeal.ads.Appodeal;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;

public class ReadingActivityMain extends AppCompatActivity {

    static ArrayList<String> readingMainArray;
    TextView tvHeading;
   String me;
    ListView lvReadingAlphabets;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //MenuInflater menuInflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.main_menu_simple, menu);

        final MenuItem item = menu.findItem(R.id.menusearch);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(ReadingActivityMain.this, query, Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                final ArrayList<String> results = new ArrayList<>();
                for (String x: readingMainArray ){

                    if(x.toLowerCase().contains(newText.toLowerCase()) ){
                        results.add(x);
                    }

                    ((ReadingMainAdapter)lvReadingAlphabets.getAdapter()).update(results);
                }

                lvReadingAlphabets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // vowel = readingAlphabetArray.get(position);

                        me = results.get(position);

                        switch (me){
                            case "Two Letter Words":
                                goToTwoLetters();
                                return;
                            case "Digraphs (Three Letter Sounds)":
                                goToPleaseSubPage();
                                return;
                        }
                        //goToTwoLetters();

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
        Intent intent = new Intent(this, ReadingActivityTwoLettersMain.class);
        //intent.putExtra("vowel", vowel);
        startActivity(intent);
    }

    public void goToDigraph() {
        // Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.udemy.com/course/learn-akan-twi/?referralCode=6D321CE6AEE1834CCB0F"));
        Intent intent = new Intent(this, SubPReadingActivityDigraphMain.class);
        //intent.putExtra("vowel", vowel);
        startActivity(intent);
    }

    public void comingSoon() {
        String coming = "ads";
        // Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.udemy.com/course/learn-akan-twi/?referralCode=6D321CE6AEE1834CCB0F"));
        Intent intent = new Intent(this, PleaseSubscribePage.class);
        intent.putExtra("coming", coming);
        startActivity(intent);
    }

    public void goToPleaseSubPage(){
        Intent intent = new Intent(getApplicationContext(), PleaseSubscribePage.class);
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading);

        Appodeal.show(this, Appodeal.BANNER_BOTTOM);

        tvHeading = findViewById(R.id.tvHeading);

        tvHeading.setText("Learn To Read Twi");

        readingMainArray = new ArrayList<>();
        readingMainArray.add("Two Letter Words");
        readingMainArray.add("Digraphs (Three Letter Sounds)");
        readingMainArray.add("Reading Lesson 1");
        readingMainArray.add("Reading Lesson 2");


//        tvLetterA = findViewById(R.id.tvVowelA);
  //      horizontalScrollView = findViewById(R.id.horizontalScrollView);
        lvReadingAlphabets = findViewById(R.id.lvReadingAlphabet);


       // ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, readingAlphabetArray);
        ReadingMainAdapter readingMainAdapter = new ReadingMainAdapter ( this, readingMainArray);
        lvReadingAlphabets.setAdapter(readingMainAdapter);
        //lvReadingAlphabets.setAdapter(arrayAdapter);

        lvReadingAlphabets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // vowel = readingAlphabetArray.get(position);

                me = readingMainArray.get(position);

                switch (me){
                    case "Two Letter Words":
                        goToTwoLetters();
                        return;
                    case "Digraphs (Three Letter Sounds)":
                        goToPleaseSubPage();
                        return;
                    default: goToPleaseSubPage();
                }
               // goToTwoLetters();

            }
        });



    }
}
