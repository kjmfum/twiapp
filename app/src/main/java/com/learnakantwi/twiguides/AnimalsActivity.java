package com.learnakantwi.twiguides;

import android.content.Intent;
import android.media.MediaPlayer;
//import android.support.v7.app.AppCompatActivity;
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
import java.util.Random;

public class AnimalsActivity extends AppCompatActivity {

    static ArrayList<Animals> animalsArrayList;
    ListView animalsListView;
    AnimalsAdapter animalsAdapter;

    /*    ListView daysOfWeekListView;
    EditText daysOfWeekEditText;
    DaysOfWeekAdapter myAdapterDaysOfWk;
    static  ArrayList<DaysOfWeek> daysOfWeeksArray;
    */

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
                ArrayList<Animals> results = new ArrayList<>();
                for (Animals x: animalsArrayList ){

                    if(x.getEnglishAnimals().toLowerCase().contains(newText.toLowerCase()) || x.getTwiAnimals().toLowerCase().contains(newText.toLowerCase())){
                        results.add(x);
                    }

                    ((AnimalsAdapter)animalsListView.getAdapter()).update(results);
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
                goToQuizAnimals();
                return  true;
            default:
                return false;
        }
    }

    public void goToQuizAnimals() {
        Intent intent = new Intent(getApplicationContext(), QuizAnimals.class);
        startActivity(intent);
    }

    public void goToMain(){
        Intent intent = new Intent(getApplicationContext(), Home.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animals);

        animalsListView = findViewById(R.id.animalsListView);

        /*animalsArrayList.add(new Animals("Bull","Nantwinini"));
        animalsArrayList.add(new Animals("Animal","Aboa"));
        animalsArrayList.add(new Animals("Animals","Mmoa"));
        animalsArrayList.add(new Animals("Cow","Nantwibere"));
        animalsArrayList.add(new Animals("Dog","Kraman"));
        animalsArrayList.add(new Animals("Cat (1)","Ɔkra"));
        animalsArrayList.add(new Animals("Cat (2)","Agyinamoa"));
        animalsArrayList.add(new Animals("Donkey","Afurum"));
        animalsArrayList.add(new Animals("Horse","Pɔnkɔ"));
        animalsArrayList.add(new Animals("Lamb","Oguammaa"));
        animalsArrayList.add(new Animals("Pig","Prako"));
        animalsArrayList.add(new Animals("Rabbit","Adanko"));
        animalsArrayList.add(new Animals("Sheep","Odwan"));
        animalsArrayList.add(new Animals("Bat","Ampan"));
        animalsArrayList.add(new Animals("Crocodile","Ɔdɛnkyɛm"));
        animalsArrayList.add(new Animals("Deer","Ɔforote"));
        animalsArrayList.add(new Animals("Elephant","Ɔsono"));
        animalsArrayList.add(new Animals("Hippopotamus","Susono"));
        animalsArrayList.add(new Animals("Hyena","Pataku"));
        animalsArrayList.add(new Animals("Wolf (1)","Pataku"));
        animalsArrayList.add(new Animals("Wolf (2)","Sakraman"));
        animalsArrayList.add(new Animals("Leopard","Ɔsebɔ"));
        animalsArrayList.add(new Animals("Lion","Gyata"));
        animalsArrayList.add(new Animals("Rat","Kusie"));
        animalsArrayList.add(new Animals("Spider","Ananse"));
        animalsArrayList.add(new Animals("Snake","Ɔwɔ"));
        animalsArrayList.add(new Animals("Duck","Dabodabo"));
        animalsArrayList.add(new Animals("Bear","Sisire"));
        animalsArrayList.add(new Animals("Chameleon","Abosomakoterɛ"));
        animalsArrayList.add(new Animals("Lizard","Koterɛ"));
        animalsArrayList.add(new Animals("Mouse","Akura"));
        animalsArrayList.add(new Animals("Tortoise","Akyekyedeɛ"));
        animalsArrayList.add(new Animals("Centipede","Sakasaka"));
        animalsArrayList.add(new Animals("Millipede","Kankabi"));
        animalsArrayList.add(new Animals("Crab","Kɔtɔ"));
        animalsArrayList.add(new Animals("Camel","Yoma"));
        animalsArrayList.add(new Animals("Fowl","Akokɔ"));
        animalsArrayList.add(new Animals("Bird","Anomaa"));
        animalsArrayList.add(new Animals("Scorpion","Akekantwɛre"));
        animalsArrayList.add(new Animals("Cockroach","Tɛfrɛ"));
        animalsArrayList.add(new Animals("Ants","Tɛtea"));
        animalsArrayList.add(new Animals("Locust (1)","Ntutummɛ"));
        animalsArrayList.add(new Animals("Locust (2)","Mmoadabi"));
        animalsArrayList.add(new Animals("Goat (1)","Apɔnkye"));
        animalsArrayList.add(new Animals("Goat (2)","Abirekyie"));
        animalsArrayList.add(new Animals("Tiger","Ɔsebɔ"));
        animalsArrayList.add(new Animals("Butterfly","Afofantɔ"));
        animalsArrayList.add(new Animals("Grasscutter","Akranteɛ"));
        animalsArrayList.add(new Animals("Lice","Edwie"));
        animalsArrayList.add(new Animals("Porcupine","Kɔtɔkɔ"));
        animalsArrayList.add(new Animals("Hedgehog (1)","Apɛsɛ"));
        animalsArrayList.add(new Animals("Hedgehog (2)","Apɛsɛe"));
        animalsArrayList.add(new Animals("Whale","Bonsu"));
        animalsArrayList.add(new Animals("Shark","Oboodede"));
        animalsArrayList.add(new Animals("Mosquito","Ntontom"));
        animalsArrayList.add(new Animals("Grasshopper","Abɛbɛ"));
        animalsArrayList.add(new Animals("Bedbug","Nsonkuronsuo"));
        animalsArrayList.add(new Animals("Squirrel","Opuro"));
        animalsArrayList.add(new Animals("Alligator","Ɔmampam"));
        animalsArrayList.add(new Animals("Buffalo","Ɛkoɔ"));
        animalsArrayList.add(new Animals("Worm","Sonsono"));
        animalsArrayList.add(new Animals("Cattle","Nantwie"));
        animalsArrayList.add(new Animals("Fish","Apataa"));

        animalsArrayList.add(new Animals("Tsetsefly","Ohurii"));
        animalsArrayList.add(new Animals("Red Tree Ant","Nhohoa"));
        animalsArrayList.add(new Animals("Driver Ants","Nkrane"));
        animalsArrayList.add(new Animals("Praying Mantis","Akokromfi"));
        animalsArrayList.add(new Animals("House fly","Nwansena"));
        animalsArrayList.add(new Animals("Beetle","Ɔbankuo"));



        animalsArrayList.add(new Animals("Vulture (1)","Pɛtɛ"));
        animalsArrayList.add(new Animals("Vulture (2)","Kɔkɔsakyi"));
        animalsArrayList.add(new Animals("Hawk","Akorɔma"));
        animalsArrayList.add(new Animals("Guinea Fowl","Akɔmfɛm"));
        animalsArrayList.add(new Animals("Monkey","Adoe"));
        animalsArrayList.add(new Animals("Parrot","Akoo"));
        animalsArrayList.add(new Animals("Crow","Kwaakwaadabi"));
        animalsArrayList.add(new Animals("Owl","Patuo"));
        animalsArrayList.add(new Animals("Eagle","Ɔkɔre"));
        animalsArrayList.add(new Animals("Sparrow","Akasanoma"));
        animalsArrayList.add(new Animals("Swallow","Asomfena"));
        animalsArrayList.add(new Animals("Dove","Aborɔnoma"));


        animalsArrayList.add(new Animals("Bee","Wowa"));
        animalsArrayList.add(new Animals("Herring","Ɛmane"));
        animalsArrayList.add(new Animals("Lobster","Ɔbɔnkɔ"));
        animalsArrayList.add(new Animals("Lobsters","Mmɔnkɔ"));




        Collections.sort(this.animalsArrayList);

        animalsArrayList.add(new Animals("Which animal?","Aboa bɛn?"));
        animalsArrayList.add(new Animals("Which animal is this?","Aboa bɛn ni?"));
        animalsArrayList.add(new Animals("It is a lion","Ɛyɛ gyata"));

*/
        animalsAdapter= new AnimalsAdapter(this, animalsArrayList);
        animalsListView.setAdapter(animalsAdapter);



    }
}
