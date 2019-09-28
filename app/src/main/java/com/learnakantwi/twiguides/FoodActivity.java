package com.learnakantwi.twiguides;

import android.content.Intent;
import android.media.MediaPlayer;
//import android.support.v7.app.AppCompatActivity;
import android.net.Uri;
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

public class FoodActivity extends AppCompatActivity {

    ListView foodListView;
    static  ArrayList<Food> foodArrayList;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //MenuInflater menuInflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.main_menu, menu);

        final MenuItem item = menu.findItem(R.id.menusearch);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(FoodActivity.this, query, Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<Food> results = new ArrayList<>();
                for (Food x: foodArrayList ){

                    if(x.getEnglishFood().toLowerCase().contains(newText.toLowerCase()) || x.getTwiFood().toLowerCase().contains(newText.toLowerCase())){
                        results.add(x);
                    }

                    ((FoodAdapter)foodListView.getAdapter()).update(results);
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
                goToWeb();
            case R.id.quiz1:
                goToQuizFood();
            default:
                return false;
        }
    }

    public void goToWeb() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.udemy.com/course/learn-akan-twi/?couponCode=FDISCOUNT1"));
        startActivity(intent);
    }

    public void goToMain(){
        Intent intent = new Intent(getApplicationContext(), Home.class);
        startActivity(intent);
    }
    public void goToQuizFood() {
        Intent intent = new Intent(getApplicationContext(), QuizFood.class);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family);

        foodListView = findViewById(R.id.familyListView);

      /*  foodArrayList = new ArrayList<>();

        foodArrayList.add(new Food("Rice", "Ɛmo"));
        foodArrayList.add(new Food("Yam", "Bayerɛ"));
        foodArrayList.add(new Food("Plantain", "Bɔɔdeɛ"));
        foodArrayList.add(new Food("Cassava", "Bankye"));
        foodArrayList.add(new Food("Onion", "Gyeene"));
        foodArrayList.add(new Food("Salt", "Nkyene"));

        //fruits
        foodArrayList.add(new Food("Fruit", "Aduaba"));
        foodArrayList.add(new Food("Apple", "Aprɛ"));
        foodArrayList.add(new Food("Banana", "Kwadu"));
        foodArrayList.add(new Food("Orange (1)", "Ankaa"));
        foodArrayList.add(new Food("Orange (2)", "Akutu"));
        foodArrayList.add(new Food("Pawpaw", "Borɔferɛ"));
        foodArrayList.add(new Food("Coconut", "Kube"));
        foodArrayList.add(new Food("Pear", "Paya"));
        foodArrayList.add(new Food("Tigernut", "Atadwe"));
        foodArrayList.add(new Food("Pineapple", "Aborɔbɛ"));
        foodArrayList.add(new Food("Ginger", "Akekaduro"));
        foodArrayList.add(new Food("Sugarcane", "Ahwedeɛ"));
        foodArrayList.add(new Food("Corn", "Aburo"));
        foodArrayList.add(new Food("Maize", "Aburo"));
        foodArrayList.add(new Food("Groundnut", "Nkateɛ"));
        foodArrayList.add(new Food("Peanut", "Nkateɛ"));
        foodArrayList.add(new Food("Palm fruit", "Abɛ"));


        //Vegetables
        foodArrayList.add(new Food("Vegetable", "Atosodeɛ"));
        foodArrayList.add(new Food("Pepper", "Mako"));
        foodArrayList.add(new Food("Bean", "Adua"));
        foodArrayList.add(new Food("Okro", "Nkuruma"));
        foodArrayList.add(new Food("Garden eggs", "Nyaadewa"));
        foodArrayList.add(new Food("Tomato", "Ntoosi"));
        foodArrayList.add(new Food("Garlic", "Galik"));
        foodArrayList.add(new Food("Cucumber", "Ɛferɛ"));


        foodArrayList.add(new Food("Lobster", "Ɔbɔnkɔ"));
        foodArrayList.add(new Food("Cocoa", "Kokoo"));
        foodArrayList.add(new Food("Palm kernel", "Adwe"));
        foodArrayList.add(new Food("Palm kernel oil", "Adwe ngo"));
        foodArrayList.add(new Food("Vegetable oil", "Anwa"));
        foodArrayList.add(new Food("Snail", "Nwa"));
        foodArrayList.add(new Food("Groundnut soup", "Nkate nkwan"));
        foodArrayList.add(new Food("Palm nut soup", "Abɛ nkwan"));

        //Others
        foodArrayList.add(new Food("Dough", "Mmɔre"));
        foodArrayList.add(new Food("Kenkey", "Dɔkono"));
        foodArrayList.add(new Food("Flour", "Esiam"));
        foodArrayList.add(new Food("Wheat", "Ayuo"));
        foodArrayList.add(new Food("Soup", "Nkwan"));
        foodArrayList.add(new Food("Stew", "Abomu"));
        foodArrayList.add(new Food("Egg", "Kosua"));
        foodArrayList.add(new Food("Bread (1)", "Paanoo"));
        foodArrayList.add(new Food("Bread (2)", "Burodo"));
        foodArrayList.add(new Food("Oil", "Ngo"));
        foodArrayList.add(new Food("Fish (1)", "Apataa"));
        foodArrayList.add(new Food("Fish (2)", "Nsuomnam"));
        foodArrayList.add(new Food("Pork", "Prakonam"));
        foodArrayList.add(new Food("Meat", "Nam"));
        foodArrayList.add(new Food("Mutton", "Odwannam"));
        foodArrayList.add(new Food("Lamb", "Odwannam"));
        foodArrayList.add(new Food("Sugar", "Asikyire"));
        foodArrayList.add(new Food("Honey", "Ɛwoɔ"));
        foodArrayList.add(new Food("Water", "Nsuo"));
        foodArrayList.add(new Food("Food", "Aduane"));

        Collections.sort(this.foodArrayList);

        foodArrayList.add(new Food("I am hungry", "Ɛkɔm de me"));
        foodArrayList.add(new Food("Are you hungry?", "Ɛkɔm de wo anaa?"));
        foodArrayList.add(new Food("What will you eat?", "Dɛn na wobedi?"));
        foodArrayList.add(new Food("I will eat kenkey", "Medi dɔkono"));
*/



        FoodAdapter foodAdapter = new FoodAdapter(this, foodArrayList);
        foodListView.setAdapter(foodAdapter);

    }
}

