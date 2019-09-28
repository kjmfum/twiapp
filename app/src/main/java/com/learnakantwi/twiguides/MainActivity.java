package com.learnakantwi.twiguides;

import android.content.Intent;
import android.os.Handler;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 600;


    public void goToAlphabets(){
        Intent intent = new Intent(getApplicationContext(), AlphabetsActivity.class);
        startActivity(intent);
    }

    public void goToNumber(View v){
        Intent intent = new Intent(getApplicationContext(), NumbersActivity.class);
        startActivity(intent);
    }

    public void goToDaysOfWk (View v){
        Intent intent = new Intent(getApplicationContext(), DaysOfWeekActivity.class);
        startActivity(intent);
    }

    public void goToAlphabets(View view){
        Intent intent = new Intent(getApplicationContext(), AlphabetsActivity.class);
        startActivity(intent);
    }

    public void goToTime(View view){
        Intent intent = new Intent(getApplicationContext(), TimeActivity.class);
        startActivity(intent);
    }

    public void goToFamily(View view){
        Intent intent = new Intent(getApplicationContext(), FamilyActivity.class);
        startActivity(intent);
    }

    public void goToWeather(View view){
        Intent intent = new Intent(getApplicationContext(), WeatherActivity.class);
        startActivity(intent);
    }

    public void goToMonths(View view) {
        Intent intent = new Intent(getApplicationContext(), WeatherActivity.class);
        startActivity(intent);
    }

    public void goToPronouns (View view) {
        Intent intent = new Intent(getApplicationContext(), PronounsActivity.class);
        startActivity(intent);
    }

    public void goToColours (View view) {
        Intent intent = new Intent(getApplicationContext(), ColoursActivity.class);
        startActivity(intent);
    }

    public void goToAnimals (View view) {
        Intent intent = new Intent(getApplicationContext(), AnimalsActivity.class);
        startActivity(intent);
    }

    public void goToBodyparts (View view) {
        Intent intent = new Intent(getApplicationContext(), AnimalsActivity.class);
        startActivity(intent);
    }

    public void goToFood (View view) {
        Intent intent = new Intent(getApplicationContext(), FoodActivity.class);
        startActivity(intent);
    }

    public void goToCommonExpressionsa (View view) {
        Intent intent = new Intent(getApplicationContext(), CommonExpressionsaActivity.class);
        startActivity(intent);
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent = new Intent(getApplicationContext(), Home.class);
                startActivity(homeIntent);
                finish();
            }
        },SPLASH_TIME_OUT);

    }

}
