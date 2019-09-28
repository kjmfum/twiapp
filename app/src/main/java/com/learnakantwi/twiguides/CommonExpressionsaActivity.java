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

public class CommonExpressionsaActivity extends AppCompatActivity {


    static  ArrayList<CommonExpressionsA> commonExpressionsAArrayList;
    ListView commonExpressionaListView;

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
                ArrayList<CommonExpressionsA> results = new ArrayList<>();
                for (CommonExpressionsA x: commonExpressionsAArrayList ){

                    if(x.getEnglishCommonExpressionsA().toLowerCase().contains(newText.toLowerCase()) || x.getTwiCommonExpressionsA().toLowerCase().contains(newText.toLowerCase())){
                        results.add(x);
                    }

                    ((CommonExpressionsAdapterA)commonExpressionaListView.getAdapter()).update(results);
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
            case R.id.quiz1:
                //Log.i("Menu Item Selected", "Alphabets");
                goToQuizCommonExpressionsa();
            default:
                return false;
        }
    }


    public void goToMain(){
        Intent intent = new Intent(getApplicationContext(), Home.class);
        startActivity(intent);
    }

    public void goToQuizCommonExpressionsa() {
        Intent intent = new Intent(getApplicationContext(), QuizCommonExpressionsa.class);
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


        if (b.contains(" ") || b.contains("/") || b.contains(",") || b.contains("(") || b.contains(")") || b.contains("-") || b.contains("?")|| b.contains("'")) {
            b = b.replace(" ", "");
            b = b.replace("/", "");
            b= b.replace(",","");
            b= b.replace("(","");
            b= b.replace(")","");
            b= b.replace("-","");
            b= b.replace("?","");
            b= b.replace("'","");
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
        setContentView(R.layout.activity_common_expressionsa);


        commonExpressionaListView = findViewById(R.id.commonExpressionsaListView);

        commonExpressionsAArrayList = new ArrayList<>();

        commonExpressionsAArrayList.add(new CommonExpressionsA("Good morning (1)","Maakye"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("Good morning (2)","Mema wo akye"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("Good afternoon (1)","Maaha"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("Good afternoon (2)","Mema wo aha"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("Good evening (1)","Maadwo"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("Good evening (2)","Mema wo adwo"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("Good night","Da yie"));

        commonExpressionsAArrayList.add(new CommonExpressionsA("How are you?","Wo ho te sɛn?"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("How is your mother?","Wo maame ho te sɛn?"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("How is your wife?","Wo yere ho te sɛn?"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("I am fine","Me ho yɛ"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("How are they doing?","Wɔn ho te sɛn?"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("I am not feeling well (1)","Me ho mfa me"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("I am not feeling well (2)","Mente apɔ"));

        commonExpressionsAArrayList.add(new CommonExpressionsA("She is fine","Ne ho yɛ"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("He is fine","Ne ho yɛ"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("They are fine","Wɔn ho yɛ"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("They are all fine","Wɔn nyinaa ho yɛ"));


        commonExpressionsAArrayList.add(new CommonExpressionsA("I am happy to meet you","M'ani agye sɛ mahyia wo"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("Welcome","Akwaaba"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("I bid you welcome","Mema wo akwaaba"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("I am happy","M'ani agye"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("I am sad","Me werɛ ahow"));

        commonExpressionsAArrayList.add(new CommonExpressionsA("Stop crying","Gyae su"));


        commonExpressionsAArrayList.add(new CommonExpressionsA("Please (1)","Mepa wo kyɛw"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("Please (2)","Mesrɛ wo"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("Thank you (1)","Medaase"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("Thank you (2)","Me da wo ase"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("I miss you","Mafe wo"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("I will miss you","Mɛfe wo"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("We will miss you","Yɛbɛfe wo"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("We will miss you (Plural)","Yɛbɛfe mo"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("Love","Ɔdɔ"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("I love you","Me dɔ wo"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("We love you","Yɛdɔ mo"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("Do you love me?","Wo dɔ me?"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("Yes","Aane"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("No","Dabi"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("My name is Kwaku (1)","Yɛfrɛ me Kwaku"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("My name is Kwaku (2)","Me din de Kwaku"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("What is your name? (1)","Yɛfrɛ wo sɛn?"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("What is your name? (2)","Wo din de sɛn?"));

        commonExpressionsAArrayList.add(new CommonExpressionsA("How old are you?","Wadi mfe sɛn?"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("I am 30 years old","Madi mfe aduasa"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("Call me","Frɛ me"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("Do you speak English?","Wo ka borɔfo?"));

        commonExpressionsAArrayList.add(new CommonExpressionsA("I am sick","Me yare"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("I need money","Me hia sika"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("Do you have money","Wowɔ sika?"));

        commonExpressionsAArrayList.add(new CommonExpressionsA("Congratulations","Ayekoo"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("See you soon","Ɛrenkyɛ mehu wo"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("Give me a hug","Yɛ me atuu"));


        CommonExpressionsAdapterA commonExpressionsAdapterA = new CommonExpressionsAdapterA(this,commonExpressionsAArrayList);
        commonExpressionaListView.setAdapter(commonExpressionsAdapterA);

    }
}
