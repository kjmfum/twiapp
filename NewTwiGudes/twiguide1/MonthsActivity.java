package com.learnakantwi.twiguides;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MonthsActivity extends AppCompatActivity {

    ArrayList <Months> monthsArrayList ;
    ListView monthsListView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

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
                ArrayList<Months> results = new ArrayList<>();

                for (Months x: monthsArrayList){


                    if(x.getEnglishMonths().toLowerCase().contains(newText.toLowerCase()) || x.getTwiMonths().contains(newText)){
                        results.add(x);
                    }

                    ((MonthsAdapter)monthsListView.getAdapter()).update(results);
                }


                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()){
           /* case R.id.settings:
                Log.i("Menu Item Selected", "Settings");
                playAll();
                return true;
            case R.id.alphabets:
                Log.i("Menu Item Selected", "Alphabets");
                return  true;*/

            case R.id.main:
                //Log.i("Menu Item Selected", "Alphabets");
                goToMain();
                return  true;
            default:
                return false;
        }
    }


    public void goToMain(){
        Intent intent = new Intent(getApplicationContext(), Home.class);
        startActivity(intent);
    }


    public void log2(View view) {
        int idview = view.getId();

        TextView blabla = (TextView) view.findViewById(idview);
        String a = (String) blabla.getText();

        Toast.makeText(this, a, Toast.LENGTH_SHORT).show();
    }

    public void timeClick(View view){

        int idview= view.getId();

        TextView blabla = (TextView) view.findViewById(idview);
        String a = (String) blabla.getText();
        Toast.makeText(this, a, Toast.LENGTH_SHORT).show();

        String b = a.toLowerCase();

        boolean d = b.contains("ɔ");
        boolean e = b.contains("ɛ");

        if (d || e ){
            b= b.replace("ɔ","x");
            b= b.replace("ɛ","q");
        }


        if (b.contains(" ") || b.contains("/") || b.contains(",") || b.contains("(") || b.contains(")") || b.contains("-") | b.contains("?")) {
            b = b.replace(" ", "");
            b = b.replace("/", "");
            b= b.replace(",","");
            b= b.replace("(","");
            b= b.replace(")","");
            b= b.replace("-","");
            b= b.replace("?","");
        }

        int resourceId = getResources().getIdentifier(b, "raw", "com.learnakantwi.twiguide");


        final MediaPlayer player = MediaPlayer.create(this, resourceId);
        player.start();

        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                player.release();
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_months);

        monthsListView = findViewById(R.id.monthsListView);

        monthsArrayList = new ArrayList<>();

        monthsArrayList.add(new Months("January","Ɔpɛpɔn"));
        monthsArrayList.add(new Months("We are in the month of January","Yɛwɔ Ɔpɛpɔn bosome mu"));
        monthsArrayList.add(new Months("February","Ɔgyefoɔ"));
        monthsArrayList.add(new Months("We will go to Ghana in February","Yɛbɛkɔ Ghana Ɔgyefoɔ bosome no mu"));
        monthsArrayList.add(new Months("March","Ɔbɛnem"));
        monthsArrayList.add(new Months("I will see you in March","Mehu wo wɔ Ɔbɛnem bosome no mu"));
        monthsArrayList.add(new Months("April","Oforisuo"));
        monthsArrayList.add(new Months("It often rains in April","Osu taa tɔ wɔ Oforisuo bosome no mu"));
        monthsArrayList.add(new Months("May","Kotonimaa"));
        monthsArrayList.add(new Months("June","Ayɛwohomumɔ"));
        monthsArrayList.add(new Months("July","Kitawonsa"));
        monthsArrayList.add(new Months("August","Ɔsanaa"));
        monthsArrayList.add(new Months("I was born in the month of August","Wɔwoo me Ɔsanaa bosome no mu"));
        monthsArrayList.add(new Months("September","Ɛbɔ"));
        monthsArrayList.add(new Months("October","Ahinime"));
        monthsArrayList.add(new Months("November","Obubuo"));
        monthsArrayList.add(new Months("December","Ɔpɛnimma"));
        monthsArrayList.add(new Months("It is often cold in December","Awɔw taa ba wɔ Ɔpɛnimma bosome no mu"));

        monthsArrayList.add(new Months("Which month?","Bosome bɛn?"));

        MonthsAdapter monthsAdapter = new MonthsAdapter(this, monthsArrayList);
        monthsListView.setAdapter(monthsAdapter);


    }
}
