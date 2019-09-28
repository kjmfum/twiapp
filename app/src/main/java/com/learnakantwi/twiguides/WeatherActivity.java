package com.learnakantwi.twiguides;

import android.content.Intent;
import android.media.MediaPlayer;
//import android.support.v7.app.AppCompatActivity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class WeatherActivity extends AppCompatActivity {

    ListView weatherListView;
    static ArrayList<Weather> weatherArray;

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

        if (b.contains(" ") || b.contains("/") || b.contains(",") || b.contains("?") ){
            b = b.replace(" ", "");
            b = b.replace("/", "");
            b= b.replace(",","");
            b= b.replace("?","");
        }

        int resourceId = getResources().getIdentifier(b, "raw", "com.learnakantwi.twiguides");


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
    public boolean onCreateOptionsMenu(Menu menu) {
        //MenuInflater menuInflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.main_menu, menu);

        final MenuItem item = menu.findItem(R.id.menusearch);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(WeatherActivity.this, query, Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<Weather> results = new ArrayList<>();
                for (Weather x: weatherArray ){

                    if(x.getWeatherEnglish().toLowerCase().contains(newText.toLowerCase()) || x.getWeatherTwi().toLowerCase().contains(newText.toLowerCase())){
                        results.add(x);
                    }

                    ((WeatherAdapter)weatherListView.getAdapter()).update(results);
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
            case R.id.videoCourse:
                //Log.i("Menu Item Selected", "Alphabets");
                goToWeb();
                return  true;
            case R.id.quiz1:
                //Log.i("Menu Item Selected", "Alphabets");
                goToQuizWeather();
                return  true;
            default:
                return false;
        }
    }


    public void goToMain(){
        Intent intent = new Intent(getApplicationContext(), Home.class);
        startActivity(intent);
    }

    public void goToQuizWeather(){
        Intent intent = new Intent(getApplicationContext(), QuizWeather.class);
        startActivity(intent);
    }

    public void goToWeb() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.udemy.com/course/learn-akan-twi/?couponCode=FDISCOUNT1"));
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        weatherListView = findViewById(R.id.weatherListView);
 /*       weatherArray= new ArrayList<>();

        weatherArray.add(new Weather("Rain","Osu"));
        weatherArray.add(new Weather("It is raining","Osu retɔ"));
        weatherArray.add(new Weather("Will it rain today?","Osu bɛtɔ nnɛ anaa?"));
        weatherArray.add(new Weather("Did it rain yesterday?","Osu tɔɔ ɛnnora anaa?"));
        weatherArray.add(new Weather("It will rain today","Osu bɛtɔ nnɛ"));
        weatherArray.add(new Weather("Umbrella","Kyinneɛ"));
        weatherArray.add(new Weather("Take the umbrella","Fa kyinneɛ no"));
        weatherArray.add(new Weather("It is drizzling","Osu repetepete"));

        weatherArray.add(new Weather("Snow","Sukyerɛmma"));


        weatherArray.add(new Weather("Sun","Awia"));
        weatherArray.add(new Weather("It is sunny","Awia abɔ"));
        weatherArray.add(new Weather("It is very sunny","Awia abɔ paa"));
        weatherArray.add(new Weather("It is hot today","Ewiem ayɛ hye nnɛ"));
        weatherArray.add(new Weather("Sunset","Owitɔe"));
        weatherArray.add(new Weather("The sun has set","Owia no akɔtɔ"));
        weatherArray.add(new Weather("Sunrise","Owipue"));
        weatherArray.add(new Weather("The sun is rising","Owia no repue"));
        weatherArray.add(new Weather("The sun has risen","Owia no apue"));

        weatherArray.add(new Weather("It is warm","Ahohuru wom"));
        weatherArray.add(new Weather("I am feeling hot","Ɔhyew de me"));
        weatherArray.add(new Weather("I am feeling warm","Ahohuru de me"));
        weatherArray.add(new Weather("It is cold","Awɔw wom"));
        weatherArray.add(new Weather("It is very cold","Awɔw wom paa"));
        weatherArray.add(new Weather("I am feeling cold","Awɔw de me"));
        weatherArray.add(new Weather("It is very chilly","Awɔw wom paa"));

        weatherArray.add(new Weather("Lightning","Anyinam"));
        weatherArray.add(new Weather("Thunder","Apranaa"));
        weatherArray.add(new Weather("Cloud","Mununkum"));
        weatherArray.add(new Weather("Rain clouds have formed","Osu amuna"));
        weatherArray.add(new Weather("It is cloudy","Ewiem ayɛ kusuu"));
        weatherArray.add(new Weather("Wind","Mframa"));
        weatherArray.add(new Weather("It is windy","Mframa rebɔ"));
        weatherArray.add(new Weather("It is very windy","Mframa rebɔ paa"));

        weatherArray.add(new Weather("Sky (1)","Soro"));
        weatherArray.add(new Weather("Sky (2)","Ewiem"));
        weatherArray.add(new Weather("Weather","Ewiem tebea"));
        weatherArray.add(new Weather("How is the weather like?","Ewiem tebea te sɛn?"));
        weatherArray.add(new Weather("What will the weather be like today?","Ɛnnɛ ewiem tebea bɛyɛ sɛn?"));
        weatherArray.add(new Weather("Weather changes","Ewiem nsakrae"));
        weatherArray.add(new Weather("Stars","Nsoromma"));
        weatherArray.add(new Weather("The stars are glittering","Nsoromma no rehyerɛn"));

        weatherArray.add(new Weather("Misty","Ɛbɔ"));
        weatherArray.add(new Weather("It is misty","Ɛbɔ asi"));

        weatherArray.add(new Weather("Harmattan","Ɔpɛ"));
        weatherArray.add(new Weather("Harmattan is here","Ɔpɛ asi"));

        weatherArray.add(new Weather("Shade","Onwunu"));
        weatherArray.add(new Weather("Storm","Ahum"));
        weatherArray.add(new Weather("Rainbow","Nyankontɔn"));
        weatherArray.add(new Weather("I saw the rainbow","Me huu nyankontɔn no"));






        weatherArray.add(new Weather("Moon","Bosome"));*/




        WeatherAdapter weatherAdapter = new WeatherAdapter(this, weatherArray);
        weatherListView.setAdapter(weatherAdapter);

    }
}
