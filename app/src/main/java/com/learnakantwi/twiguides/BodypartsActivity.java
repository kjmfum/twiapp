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

public class BodypartsActivity extends AppCompatActivity {

    public static ArrayList <Bodyparts> bodypartsArrayList;
    ListView bodypartsListView;


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
                ArrayList<Bodyparts> results = new ArrayList<>();
                for (Bodyparts x: bodypartsArrayList ){

                    if(x.getEnglishBodyparts().toLowerCase().contains(newText.toLowerCase()) || x.getTwiBodyparts().toLowerCase().contains(newText.toLowerCase())){
                        results.add(x);
                    }

                    ((BodypartsAdapter)bodypartsListView.getAdapter()).update(results);
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
            case R.id.quiz1:
                //Log.i("Menu Item Selected", "Alphabets");
                goToQuizBodyParts();
                return  true;
            case R.id.videoCourse:
                //Log.i("Menu Item Selected", "Alphabets");
                goToWeb();
                return  true;
            default:
                return false;
        }
    }


    public void goToMain(){
        Intent intent = new Intent(getApplicationContext(), Home.class);
        startActivity(intent);
    }

    public void goToQuizBodyParts(){
        Intent intent = new Intent(getApplicationContext(), QuizBodyParts.class);
        startActivity(intent);
    }

    public void goToWeb() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.udemy.com/course/learn-akan-twi/?couponCode=FDISCOUNT1"));
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bodyparts);


        bodypartsListView = findViewById(R.id.bodypartsListView);

        /*bodypartsArrayList = new ArrayList<>();

        bodypartsArrayList.add(new Bodyparts("Hand","Nsa"));
        bodypartsArrayList.add(new Bodyparts("Finger","Nsateaa"));

        bodypartsArrayList.add(new Bodyparts("Leg","Nan"));
        bodypartsArrayList.add(new Bodyparts("Nose","Hwene"));
        bodypartsArrayList.add(new Bodyparts("Head","Eti"));
        bodypartsArrayList.add(new Bodyparts("Mouth","Ano"));
        bodypartsArrayList.add(new Bodyparts("Gum","Ɛse akyi nam"));

        bodypartsArrayList.add(new Bodyparts("Cheek","Afono"));
        bodypartsArrayList.add(new Bodyparts("Teeth","Ɛse"));
        bodypartsArrayList.add(new Bodyparts("Tongue","Tɛkrɛma"));
        bodypartsArrayList.add(new Bodyparts("Eyebrow (1)","Ani akyi nhwi"));
        bodypartsArrayList.add(new Bodyparts("Eyebrow (2)","Ani ntɔn nhwi"));
        bodypartsArrayList.add(new Bodyparts("Eyelashes (1)","Anisoatɛtɛ"));
        bodypartsArrayList.add(new Bodyparts("Eyelashes (2)","Ani ntɔn"));

        bodypartsArrayList.add(new Bodyparts("Hair","Nhwi"));
        bodypartsArrayList.add(new Bodyparts("Forehead","Moma"));
        bodypartsArrayList.add(new Bodyparts("Eyeball","Ani kosua"));
        bodypartsArrayList.add(new Bodyparts("Chin","Abɔdwe"));
        bodypartsArrayList.add(new Bodyparts("Beard","Abɔdwesɛ nhwi"));
        bodypartsArrayList.add(new Bodyparts("Moustache (1)","Ano ho nhwi"));
        bodypartsArrayList.add(new Bodyparts("Moustache (1)","Mfemfem"));

        bodypartsArrayList.add(new Bodyparts("Human","Nipa"));
        bodypartsArrayList.add(new Bodyparts("Body","Nipadua"));
        bodypartsArrayList.add(new Bodyparts("Neck","Kɔn"));
        bodypartsArrayList.add(new Bodyparts("Chest (1)","Koko"));
        bodypartsArrayList.add(new Bodyparts("Chest (2)","Bo"));

        bodypartsArrayList.add(new Bodyparts("Navel","Afunuma"));
        bodypartsArrayList.add(new Bodyparts("Stomach (1)","Yafunu"));
        bodypartsArrayList.add(new Bodyparts("Stomach (2)","Yam"));

        bodypartsArrayList.add(new Bodyparts("Ribs (1)","Mparow"));
        bodypartsArrayList.add(new Bodyparts("Ribs (2)","Mfe mpade"));
        bodypartsArrayList.add(new Bodyparts("Shoulder","Abati"));
        bodypartsArrayList.add(new Bodyparts("Palm","Nsayam"));
        bodypartsArrayList.add(new Bodyparts("Knee","Kotodwe"));
        bodypartsArrayList.add(new Bodyparts("Intestine","Nsono"));
        bodypartsArrayList.add(new Bodyparts("Lung","Ahrawa"));

        bodypartsArrayList.add(new Bodyparts("Armpit","Mmɔtoam"));
        bodypartsArrayList.add(new Bodyparts("Bone","Dompe"));
        bodypartsArrayList.add(new Bodyparts("Breast","Nufuo"));
        bodypartsArrayList.add(new Bodyparts("Heart","Koma"));

        bodypartsArrayList.add(new Bodyparts("Brain (1)","Adwene"));
        bodypartsArrayList.add(new Bodyparts("Brain (2)","Amemene"));
        bodypartsArrayList.add(new Bodyparts("Fingernail","Mmɔwerɛ"));
        bodypartsArrayList.add(new Bodyparts("Thumb","Kokuromoti"));
        bodypartsArrayList.add(new Bodyparts("Arm","Abasa"));
        bodypartsArrayList.add(new Bodyparts("Elbow","Abatwɛ"));
        bodypartsArrayList.add(new Bodyparts("Vein","Ntini"));

        bodypartsArrayList.add(new Bodyparts("Buttock","Ɛto"));
        bodypartsArrayList.add(new Bodyparts("Bladder","Dwonsɔtwaa"));
        bodypartsArrayList.add(new Bodyparts("Waist","Sisi"));
        bodypartsArrayList.add(new Bodyparts("Womb (1)","Awode"));
        bodypartsArrayList.add(new Bodyparts("Womb (2)","Awotwaa"));

        bodypartsArrayList.add(new Bodyparts("Toe","Nansoaa"));
        bodypartsArrayList.add(new Bodyparts("Wrist","Abakɔn"));
        bodypartsArrayList.add(new Bodyparts("Heel","Nantin"));
        bodypartsArrayList.add(new Bodyparts("Throat (1)","Mene"));
        bodypartsArrayList.add(new Bodyparts("Throat (2)","Menemu"));
        bodypartsArrayList.add(new Bodyparts("Thigh","Srɛ"));
        bodypartsArrayList.add(new Bodyparts("Blood","Mogya"));
        bodypartsArrayList.add(new Bodyparts("Calf","Nantu"));
        bodypartsArrayList.add(new Bodyparts("Lips","Anofafa"));

        bodypartsArrayList.add(new Bodyparts("Skull","Tikwankora"));
        bodypartsArrayList.add(new Bodyparts("Skin","Honam"));

        bodypartsArrayList.add(new Bodyparts("Liver","Berɛboɔ"));
        bodypartsArrayList.add(new Bodyparts("Occiput","Atikɔ"));
        bodypartsArrayList.add(new Bodyparts("Hip (1)","Dwonku"));
        bodypartsArrayList.add(new Bodyparts("Hip (2)","Asen"));




        //Collections.sort(this.bodypartsArrayList);
        */

        BodypartsAdapter bodypartsAdapter = new BodypartsAdapter(this,bodypartsArrayList);
        bodypartsListView.setAdapter(bodypartsAdapter);

    }
}
