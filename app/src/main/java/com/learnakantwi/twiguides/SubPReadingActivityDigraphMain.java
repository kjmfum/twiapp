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

import java.util.ArrayList;

public class SubPReadingActivityDigraphMain extends AppCompatActivity {

    static ArrayList<String> readingDigraphArray;
    CharSequence digraph;
    HorizontalScrollView horizontalScrollView;
    ListView lvReadingAlphabets;
    TextView headerText;

    Toast toast;




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //MenuInflater menuInflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.main_menu_simple, menu);

        final MenuItem item = menu.findItem(R.id.menusearch);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(SubPReadingActivityDigraphMain.this, query, Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                final ArrayList<String> results = new ArrayList<>();
                for (String x: readingDigraphArray ){

                    if(x.toLowerCase().contains(newText.toLowerCase()) ){
                        results.add(x);
                    }

                    ((ReadingMainAdapter)lvReadingAlphabets.getAdapter()).update(results);
                }

                lvReadingAlphabets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        digraph = results.get(position);
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
        Intent intent = new Intent(getApplicationContext(), SubPHomeMainActivity.class);
        startActivity(intent);
    }
    public void goToWeb() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.udemy.com/course/learn-akan-twi/?referralCode=6D321CE6AEE1834CCB0F"));
        startActivity(intent);
    }


    public void goToTwoLetters() {
        // Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.udemy.com/course/learn-akan-twi/?referralCode=6D321CE6AEE1834CCB0F"));
        Intent intent = new Intent(this, SubPReadingDigraphs.class);
        intent.putExtra("vowel", digraph);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_preading_two_letters_main);

        headerText = findViewById(R.id.tvHeading);
        headerText.setText("DIGRAPHS");


        readingDigraphArray = new ArrayList<>();
        readingDigraphArray.add("Ny");
        readingDigraphArray.add("Ky");
        readingDigraphArray.add("Kw");
        readingDigraphArray.add("Hy");
        readingDigraphArray.add("Gy");
        readingDigraphArray.add("Dw");
        readingDigraphArray.add("Tw");
        readingDigraphArray.add("Hw");


//        tvLetterA = findViewById(R.id.tvVowelA);
        //      horizontalScrollView = findViewById(R.id.horizontalScrollView);
        lvReadingAlphabets = findViewById(R.id.lvReadingAlphabet);


        // ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, readingAlphabetArray);
        ReadingMainAdapter readingMainAdapter = new ReadingMainAdapter ( this, readingDigraphArray);
        lvReadingAlphabets.setAdapter(readingMainAdapter);
        //lvReadingAlphabets.setAdapter(arrayAdapter);

        lvReadingAlphabets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                digraph = readingDigraphArray.get(position);
                goToTwoLetters();

            }
        });

    }
}

