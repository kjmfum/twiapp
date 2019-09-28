package com.learnakantwi.twiguides;

import android.content.Intent;
import android.media.MediaPlayer;
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

public class ColoursActivity extends AppCompatActivity {

    static ArrayList<Colours> coloursArrayList;
    ListView coloursListView;

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
                ArrayList<Colours> results = new ArrayList<>();
                for (Colours x: coloursArrayList ){

                    if(x.getEnglishColours().toLowerCase().contains(newText.toLowerCase()) || x.getTwiColours().toLowerCase().contains(newText.toLowerCase())){
                        results.add(x);
                    }

                    ((ColoursAdapter)coloursListView.getAdapter()).update(results);
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
                goToQuizColours();
                return  true;
            default:
                return false;
        }
    }


    public void goToMain(){
        Intent intent = new Intent(getApplicationContext(), Home.class);
        startActivity(intent);
    }

    public void goToQuizColours() {
        Intent intent = new Intent(getApplicationContext(), QuizColours.class);
        startActivity(intent);
    }
    public void goToWeb() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.udemy.com/course/learn-akan-twi/?couponCode=FDISCOUNT1"));
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
        setContentView(R.layout.activity_colours);


        coloursListView = findViewById(R.id.coloursListView);

        coloursArrayList = new ArrayList<>();

        coloursArrayList.add(new Colours("Colour (1)","Ahosu"));
        coloursArrayList.add(new Colours("Colour (2)","Kɔla"));
        coloursArrayList.add(new Colours("What colour is it?","Ɛyɛ kɔla bɛn?"));
        coloursArrayList.add(new Colours("Red","Kɔkɔɔ"));
        coloursArrayList.add(new Colours("Black","Tuntum"));
        coloursArrayList.add(new Colours("White (1)","Fitaa"));
        coloursArrayList.add(new Colours("White (2)","Fufuo"));
        coloursArrayList.add(new Colours("Green","Ahabammono"));
        coloursArrayList.add(new Colours("Blue","Bruu"));
        coloursArrayList.add(new Colours("Brown","Dodowee"));
        coloursArrayList.add(new Colours("Grey","Nsonso"));
        coloursArrayList.add(new Colours("Ash","Nsonso"));
        coloursArrayList.add(new Colours("Yellow","Akokɔsrade"));
        coloursArrayList.add(new Colours("Spotted","Nsisimu"));
        coloursArrayList.add(new Colours("Purple (1)","Beredum"));
        coloursArrayList.add(new Colours("Purple (2)","Afasebiri"));
        coloursArrayList.add(new Colours("Pink","Memen"));
        coloursArrayList.add(new Colours("Dark","Sum"));




        ColoursAdapter coloursAdapter = new ColoursAdapter(this,coloursArrayList);
        coloursListView.setAdapter(coloursAdapter);

    }
}
