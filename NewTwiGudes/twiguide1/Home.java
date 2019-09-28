package com.learnakantwi.twiguides;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;

//import android.support.v7.app.AppCompatActivity;

public class Home extends AppCompatActivity {

ArrayList<HomeButton> homeButtonArrayList;
ListView homeListView;

    public void log2(View view) {
        int idview = view.getId();

        TextView blabla = (TextView) view.findViewById(idview);
        String a = (String) blabla.getText();

        Toast.makeText(this, a, Toast.LENGTH_SHORT).show();

        switch (a){
            case "Alphabets": goToAlphabets();
            return;
            case "Animals": goToAnimals();
            return;
            case  "Body Parts": goToBodyparts();
            return;
            case "Colours": goToColours();
            return;
            case "Days of Week": goToDaysOfWk();
            return;
            case "Expressions" : goToCommonExpressionsa();
            return;
            case "Family" : goToFamily();
            return;
            case "Food" : goToFood();
            return;
            case "Months" : goToMonths();
            return;
            case "Numbers" : goToNumber();
            return;
            case "Pronouns" : goToPronouns();
            return;
            case "Time" : goToTime();
            return;
            case "Weather" : goToWeather();

        }
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
                Toast.makeText(getApplicationContext(), query, Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<HomeButton> results = new ArrayList<>();
                for (HomeButton x: homeButtonArrayList ){

                    if(x.getNameofActivity().toLowerCase().contains(newText.toLowerCase()) ){
                        results.add(x);
                    }

                    ((HomeAdapter)homeListView.getAdapter()).update(results);
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
               // goToMain();
                return  true;
            default:
                return false;
        }
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);


        MenuItem item = menu.findItem(R.id.menusearch);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(Home.this, query, Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);


        // SearchView searchView = (SearchView) menuItem.getActionView();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()){
            case R.id.settings:
                Log.i("Menu Item Selected", "Settings");
                return true;
            case R.id.alphabets:
                Log.i("Menu Item Selected", "Alphabets");
                //goToAlphabets();
                return  true;
            default:
                return false;
        }
    }*/

    public void goToAlphabets(){
        Intent intent = new Intent(getApplicationContext(), AlphabetsActivity.class);
        startActivity(intent);
    }

    public void goToNumber(){
        Intent intent = new Intent(getApplicationContext(), NumbersActivity.class);
        startActivity(intent);
    }

    public void goToDaysOfWk (){
        Intent intent = new Intent(getApplicationContext(), DaysOfWeekActivity.class);
        startActivity(intent);
    }


    public void goToTime(){
        Intent intent = new Intent(getApplicationContext(), TimeActivity.class);
        startActivity(intent);
    }

    public void goToFamily(){
        Intent intent = new Intent(getApplicationContext(), FamilyActivity.class);
        startActivity(intent);
    }

    public void goToWeather(){
        Intent intent = new Intent(getApplicationContext(), WeatherActivity.class);
        startActivity(intent);
    }

    public void goToMonths(){
        Intent intent = new Intent(getApplicationContext(), MonthsActivity.class);
        startActivity(intent);
    }

    public void goToPronouns () {
        Intent intent = new Intent(getApplicationContext(), PronounsActivity.class);
        startActivity(intent);
    }

    public void goToColours () {
        Intent intent = new Intent(getApplicationContext(), ColoursActivity.class);
        startActivity(intent);
    }

    public void goToAnimals () {
        Intent intent = new Intent(getApplicationContext(), AnimalsActivity.class);
        startActivity(intent);
    }

    public void goToBodyparts () {
        Intent intent = new Intent(getApplicationContext(), BodypartsActivity.class);
        startActivity(intent);
    }

    public void goToFood() {
        Intent intent = new Intent(getApplicationContext(), FoodActivity.class);
        startActivity(intent);
    }

    public void goToCommonExpressionsa () {
        Intent intent = new Intent(getApplicationContext(), CommonExpressionsaActivity.class);
        startActivity(intent);
    }

//In your case, you do not need the LinearLayout and ImageView at all. Just add android:drawableLeft="@drawable/up_count_big" to your TextView.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        homeButtonArrayList = new ArrayList<>();
        homeListView = findViewById(R.id.homeListView);

        homeButtonArrayList.add(new HomeButton("Alphabets",R.drawable.alphabetsimage));
        homeButtonArrayList.add(new HomeButton("Food",R.drawable.foodimage));
        homeButtonArrayList.add(new HomeButton("Family",R.drawable.familyimage));
        homeButtonArrayList.add(new HomeButton("Time",R.drawable.time));
        homeButtonArrayList.add(new HomeButton("Days of Week",R.drawable.monday));
        homeButtonArrayList.add(new HomeButton("Numbers",R.drawable.numbers));
        homeButtonArrayList.add(new HomeButton("Pronouns",R.drawable.pronounsimage));
        homeButtonArrayList.add(new HomeButton("Weather",R.drawable.weathersunimage));
        homeButtonArrayList.add(new HomeButton("Body Parts",R.drawable.lungsimage));
        homeButtonArrayList.add(new HomeButton("Months",R.drawable.calendar));
        homeButtonArrayList.add(new HomeButton("Expressions",R.drawable.expressionsimage));
        homeButtonArrayList.add(new HomeButton("Colours",R.drawable.colourimage));
        homeButtonArrayList.add(new HomeButton("Animals",R.drawable.animalsimage));

        Collections.sort(this.homeButtonArrayList);

        HomeAdapter homeAdapter = new HomeAdapter(this, homeButtonArrayList);
        homeListView.setAdapter(homeAdapter);
    }
}

