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

import com.learnakantwi.twiguides.AlphabetAdapter;
import com.learnakantwi.twiguides.Alphabets;

import java.util.ArrayList;

//import android.support.v7.app.AppCompatActivity;

public class AlphabetsActivity extends AppCompatActivity {

    AlphabetAdapter twiAlphapetAdapter;

    MediaPlayer mediaPlayer;

    ArrayList<Alphabets> alphabetArray;

    ListView myListView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        final MenuItem item = menu.findItem(R.id.menusearch);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(AlphabetsActivity.this, query, Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<Alphabets> results = new ArrayList<>();
                for (Alphabets x: alphabetArray){

                    if(x.getBoth().contains(newText)){
                        results.add(x);
                    }

                    ((AlphabetAdapter)myListView.getAdapter()).update(results);
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

    public void playAll(){

        String all = "abdeqfghiklmnoxprstuwy";
        for (int i=0; i <= all.length() ; i++) {
            char c = all.charAt(i);
            String b = String.valueOf(c);

            int resourceId = getResources().getIdentifier(b, "raw", "com.example.customlist2");
            mediaPlayer = MediaPlayer.create(this, resourceId);
            mediaPlayer.start();


            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mediaPlayer.release();
                }
            });
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    MediaPlayer player = null;

    public void log1(View view){


        int idview= view.getId();

        TextView blabla = (TextView) view.findViewById(idview);
        String a = (String) blabla.getText();
        String b = a.toLowerCase();
        int index = b.indexOf("ɔ");
        int index1 = b.indexOf("ɛ");

        boolean d = b.contains("ɔ");
        boolean e = b.contains("ɛ");

        if (d || e ){
            b= b.replace("ɔ","x");
            b= b.replace("ɛ","q");
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

        Toast.makeText(this, a, Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alphabets);


        myListView = findViewById(R.id.myList);

        alphabetArray = new ArrayList<>();

        alphabetArray.add(new Alphabets("Aa" ,"A","a"));
        alphabetArray.add(new Alphabets("Bb","B","b"));
        alphabetArray.add(new Alphabets("Dd","D","d"));
        alphabetArray.add(new Alphabets("Ee","E","e"));
        alphabetArray.add(new Alphabets("Ɛɛ","Ɛ","ɛ"));
        alphabetArray.add(new Alphabets("Ff","F","f"));
        alphabetArray.add(new Alphabets("Gg","G","g"));
        alphabetArray.add(new Alphabets("Hh","H","h"));
        alphabetArray.add(new Alphabets("Ii","I","i"));
        alphabetArray.add(new Alphabets("Kk","K","k"));
        alphabetArray.add(new Alphabets("Ll","L","l"));
        alphabetArray.add(new Alphabets("Mm","M","m"));
        alphabetArray.add(new Alphabets("Nn","N","n"));
        alphabetArray.add(new Alphabets("Oo","O","o"));
        alphabetArray.add(new Alphabets("Ɔɔ","Ɔ","ɔ"));
        alphabetArray.add(new Alphabets("Pp","P","p"));
        alphabetArray.add(new Alphabets("Rr","R","r"));
        alphabetArray.add(new Alphabets("Ss","S","s"));
        alphabetArray.add(new Alphabets("Tt","T","t"));
        alphabetArray.add(new Alphabets("Uu","U","u"));
        alphabetArray.add(new Alphabets("Ww","W","w"));
        alphabetArray.add(new Alphabets("Yy","Y","y"));


        twiAlphapetAdapter = new AlphabetAdapter(this, alphabetArray);
        myListView.setAdapter(twiAlphapetAdapter);
    }

}

