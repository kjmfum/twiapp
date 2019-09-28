package com.learnakantwi.twiguides;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static com.learnakantwi.twiguides.DaysOfWeekActivity.daysOfWeeksArray;
import static com.learnakantwi.twiguides.Home.homeButtonArrayList;

public class AllActivity extends AppCompatActivity {


    static ArrayList<All> allArrayList;
    ListView allListView;
    AdView mAdView;

    public void generate() {



        String english1;
        english1 = allArrayList.get(1).toString();
        Log.i("Got It", english1);
    }

    public void log2(View view) {

generate();

        int idview = view.getId();

        TextView blabla = (TextView) view.findViewById(idview);
        String a = (String) blabla.getText();

        Toast.makeText(this, a, Toast.LENGTH_SHORT).show();
    }

    public void allClick(View view){

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
                ArrayList<All> results = new ArrayList<>();
                for (All x: allArrayList ){

                    if(x.getEnglishmain().toLowerCase().contains(newText.toLowerCase()) || x.getTwiMain().toLowerCase().contains(newText.toLowerCase())
                            || x.getTwi1().toLowerCase().contains(newText.toLowerCase())
                            || x.getTwi2().toLowerCase().contains(newText.toLowerCase())
                            || x.getEnglish1().toLowerCase().contains(newText.toLowerCase())
                            || x.getEnglish2().toLowerCase().contains(newText.toLowerCase())

                    ){
                        results.add(x);
                    }

                    ((AllAdapter)allListView.getAdapter()).update(results);
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
                goToQuizAll();
                return  true;
            default:
                return false;
        }
    }


    public void goToMain(){
        Intent intent = new Intent(getApplicationContext(), Home.class);
        startActivity(intent);
    }

    public void goToQuizAll() {
        Intent intent = new Intent(getApplicationContext(), QuizAll.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all);

    /* MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView1);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);*/

        //allArrayList = new ArrayList<>();
        allListView = findViewById(R.id.allListView);

        /*
        //Animals

        allArrayList.add(new All("Bull","Nantwinini", "", "","",""));
        allArrayList.add(new All("Animal","Aboa", "", "","",""));
        allArrayList.add(new All("Animals","Mmoa" , "", "","",""));
        allArrayList.add(new All("Animals","Mmoa", "", "","",""));
        allArrayList.add(new All("Cow","Nantwibere","","", "",""));
        allArrayList.add(new All("Dog","Kraman","", "", "",""));
        allArrayList.add(new All("Cat (1)","Ɔkra","", "", "",""));
        allArrayList.add(new All("Cat (2)","Agyinamoa","", "", "",""));
        allArrayList.add(new All("Donkey","Afurum","", "", "",""));
        allArrayList.add(new All("Horse","Pɔnkɔ","", "", "",""));
        allArrayList.add(new All("Lamb","Oguammaa","", "", "",""));
        allArrayList.add(new All("Pig","Prako","", "", "",""));
        allArrayList.add(new All("Rabbit","Adanko","", "", "",""));
        allArrayList.add(new All("Sheep","Odwan","", "", "",""));
        allArrayList.add(new All("Bat","Ampan","", "", "",""));
        allArrayList.add(new All("Crocodile","Ɔdɛnkyɛm","", "", "",""));
        allArrayList.add(new All("Deer","Ɔforote","", "", "",""));
        allArrayList.add(new All("Elephant","Ɔsono","", "", "",""));
        allArrayList.add(new All("Hippopotamus","Susono","", "", "",""));
        allArrayList.add(new All("Hyena","Pataku","", "", "",""));
        allArrayList.add(new All("Wolf (1)","Pataku","", "", "",""));
        allArrayList.add(new All("Wolf (2)","Sakraman","", "", "",""));
        allArrayList.add(new All("Leopard","Ɔsebɔ","", "", "",""));
        allArrayList.add(new All("Lion","Gyata","", "", "",""));
        allArrayList.add(new All("Rat","Kusie","", "", "",""));
        allArrayList.add(new All("Snake","Ɔwɔ","", "", "",""));
        allArrayList.add(new All("Duck","Dabodabo","", "", "",""));


        //,"", "", "","")

        allArrayList.add(new All("Bear","Sisire","", "", "",""));
        allArrayList.add(new All("Chameleon","Abosomakoterɛ","", "", "",""));
        allArrayList.add(new All("Lizard","Koterɛ","", "", "",""));
        allArrayList.add(new All("Mouse","Akura","", "", "",""));
        allArrayList.add(new All("Tortoise","Akyekyedeɛ","", "", "",""));
        allArrayList.add(new All("Centipede","Sakasaka","", "", "",""));
        allArrayList.add(new All("Millipede","Kankabi","", "", "",""));
        allArrayList.add(new All("Crab","Kɔtɔ","", "", "",""));
        allArrayList.add(new All("Camel","Yoma","", "", "",""));
        allArrayList.add(new All("Fowl","Akokɔ","", "", "",""));
        allArrayList.add(new All("Bird","Anomaa","", "", "",""));
        allArrayList.add(new All("Scorpion","Akekantwɛre","", "", "",""));
        allArrayList.add(new All("Cockroach","Tɛfrɛ","", "", "",""));
        allArrayList.add(new All("Ants","Tɛtea","", "", "",""));
        allArrayList.add(new All("Locust (1)","Ntutummɛ","", "", "",""));
        allArrayList.add(new All("Locust (2)","Mmoadabi","", "", "",""));
        allArrayList.add(new All("Goat (1)","Apɔnkye","", "", "",""));
        allArrayList.add(new All("Goat (2)","Abirekyie","", "", "",""));
        allArrayList.add(new All("Tiger","Ɔsebɔ","", "", "",""));
        allArrayList.add(new All("Butterfly","Afofantɔ","", "", "",""));
        allArrayList.add(new All("Grasscutter","Akranteɛ","", "", "",""));
        allArrayList.add(new All("Lice","Edwie","", "", "",""));
        allArrayList.add(new All("Porcupine","Kɔtɔkɔ","", "", "",""));
        allArrayList.add(new All("Hedgehog (1)","Apɛsɛ","", "", "",""));
        allArrayList.add(new All("Hedgehog (2)","Apɛsɛe","", "", "",""));
        allArrayList.add(new All("Whale","Bonsu","", "", "",""));
        allArrayList.add(new All("Shark","Oboodede","", "", "",""));
        allArrayList.add(new All("Mosquito","Ntontom","", "", "",""));
        allArrayList.add(new All("Grasshopper","Abɛbɛ","", "", "",""));
        allArrayList.add(new All("Bedbug","Nsonkuronsuo","", "", "",""));
        allArrayList.add(new All("Squirrel","Opuro","", "", "",""));
        allArrayList.add(new All("Alligator","Ɔmampam","", "", "",""));
        allArrayList.add(new All("Buffalo","Ɛkoɔ","", "", "",""));
        allArrayList.add(new All("Worm","Sonsono","", "", "",""));
        allArrayList.add(new All("Cattle","Nantwie","", "", "",""));
        allArrayList.add(new All("Fish","Apataa","", "", "",""));

        allArrayList.add(new All("Tsetsefly","Ohurii","", "", "",""));
        allArrayList.add(new All("Red Tree Ant","Nhohoa","", "", "",""));
        allArrayList.add(new All("Driver Ants","Nkrane","", "", "",""));
        allArrayList.add(new All("Praying Mantis","Akokromfi","", "", "",""));
        allArrayList.add(new All("House fly","Nwansena","", "", "",""));
        allArrayList.add(new All("Beetle","Ɔbankuo","", "", "",""));



        allArrayList.add(new All("Vulture (1)","Pɛtɛ","", "", "",""));
        allArrayList.add(new All("Vulture (2)","Kɔkɔsakyi","", "", "",""));
        allArrayList.add(new All("Hawk","Akorɔma","", "", "",""));
        allArrayList.add(new All("Guinea Fowl","Akɔmfɛm","", "", "",""));
        allArrayList.add(new All("Monkey","Adoe","", "", "",""));
        allArrayList.add(new All("Parrot","Akoo","", "", "",""));
        allArrayList.add(new All("Crow","Kwaakwaadabi","", "", "",""));
        allArrayList.add(new All("Owl","Patuo","", "", "",""));
        allArrayList.add(new All("Eagle","Ɔkɔre","", "", "",""));
        allArrayList.add(new All("Sparrow","Akasanoma","", "", "",""));
        allArrayList.add(new All("Swallow","Asomfena","", "", "",""));
        allArrayList.add(new All("Dove","Aborɔnoma","", "", "",""));


        allArrayList.add(new All("Bee","Wowa","", "", "",""));
        allArrayList.add(new All("Herring","Ɛmane","", "", "",""));
        allArrayList.add(new All("Lobster","Ɔbɔnkɔ","", "", "",""));
        allArrayList.add(new All("Lobsters","Mmɔnkɔ","", "", "",""));

        Collections.sort(allArrayList);

        allArrayList.add(new All("Which animal?","Aboa bɛn?","", "", "","")) ;
        allArrayList.add(new All("Which animal is this?","Aboa bɛn ni?","", "", "","")) ;
        allArrayList.add(new All("It is a lion","Ɛyɛ gyata","", "", "","")) ;


        //BodyParts

        allArrayList.add(new All("Hand","Nsa","", "", "",""));
        allArrayList.add(new All("Finger","Nsateaa","", "", "",""));

        allArrayList.add(new All("Leg","Nan","", "", "",""));
        allArrayList.add(new All("Nose","Hwene","", "", "",""));
        allArrayList.add(new All("Head","Eti","", "", "",""));
        allArrayList.add(new All("Mouth","Ano","", "", "",""));
        allArrayList.add(new All("Gum","Ɛse akyi nam","", "", "",""));

        allArrayList.add(new All("Cheek","Afono","", "", "",""));
        allArrayList.add(new All("Teeth","Ɛse","", "", "",""));
        allArrayList.add(new All("Tongue","Tɛkrɛma","", "", "",""));
        allArrayList.add(new All("Eyebrow (1)","Ani akyi nhwi","", "", "",""));
        allArrayList.add(new All("Eyebrow (2)","Ani ntɔn nhwi","", "", "",""));
        allArrayList.add(new All("Eyelashes (1)","Anisoatɛtɛ","", "", "",""));
        allArrayList.add(new All("Eyelashes (2)","Ani ntɔn","", "", "",""));

        allArrayList.add(new All("Hair","Nhwi","", "", "",""));
        allArrayList.add(new All("Forehead","Moma","", "", "",""));
        allArrayList.add(new All("Eyeball","Ani kosua","", "", "",""));
        allArrayList.add(new All("Chin","Abɔdwe","", "", "",""));
        allArrayList.add(new All("Beard","Abɔdwesɛ nhwi","", "", "",""));
        allArrayList.add(new All("Moustache (1)","Ano ho nhwi","", "", "",""));
        allArrayList.add(new All("Moustache (1)","Mfemfem","", "", "",""));

        allArrayList.add(new All("Human","Nipa","", "", "",""));
        allArrayList.add(new All("Body","Nipadua","", "", "",""));
        allArrayList.add(new All("Neck","Kɔn","", "", "",""));
        allArrayList.add(new All("Chest (1)","Koko","", "", "",""));
        allArrayList.add(new All("Chest (2)","Bo","", "", "",""));

        allArrayList.add(new All("Navel","Afunuma","", "", "",""));
        allArrayList.add(new All("Stomach (1)","Yafunu","", "", "",""));
        allArrayList.add(new All("Stomach (2)","Yam","", "", "",""));

        allArrayList.add(new All("Ribs (1)","Mparow","", "", "",""));
        allArrayList.add(new All("Ribs (2)","Mfe mpade","", "", "",""));
        allArrayList.add(new All("Shoulder","Abati","", "", "",""));
        allArrayList.add(new All("Palm","Nsayam","", "", "",""));
        allArrayList.add(new All("Knee","Kotodwe","", "", "",""));
        allArrayList.add(new All("Intestine","Nsono","", "", "",""));
        allArrayList.add(new All("Lung","Ahrawa","", "", "",""));

        allArrayList.add(new All("Armpit","Mmɔtoam","", "", "",""));
        allArrayList.add(new All("Bone","Dompe","", "", "",""));
        allArrayList.add(new All("Breast","Nufuo","", "", "",""));
        allArrayList.add(new All("Heart","Koma","", "", "",""));

        allArrayList.add(new All("Brain (1)","Adwene","", "", "",""));
        allArrayList.add(new All("Brain (2)","Amemene","", "", "",""));
        allArrayList.add(new All("Fingernail","Mmɔwerɛ","", "", "",""));
        allArrayList.add(new All("Thumb","Kokuromoti","", "", "",""));
        allArrayList.add(new All("Arm","Abasa","", "", "",""));
        allArrayList.add(new All("Elbow","Abatwɛ","", "", "",""));
        allArrayList.add(new All("Vein","Ntini","", "", "",""));

        allArrayList.add(new All("Buttock","Ɛto","", "", "",""));
        allArrayList.add(new All("Bladder","Dwonsɔtwaa","", "", "",""));
        allArrayList.add(new All("Waist","Sisi","", "", "",""));
        allArrayList.add(new All("Womb (1)","Awode","", "", "",""));
        allArrayList.add(new All("Womb (2)","Awotwaa","", "", "",""));

        allArrayList.add(new All("Toe","Nansoaa","", "", "",""));
        allArrayList.add(new All("Wrist","Abakɔn","", "", "",""));
        allArrayList.add(new All("Heel","Nantin","", "", "",""));
        allArrayList.add(new All("Throat (1)","Mene","", "", "",""));
        allArrayList.add(new All("Throat (2)","Menemu","", "", "",""));
        allArrayList.add(new All("Thigh","Srɛ","", "", "",""));
        allArrayList.add(new All("Blood","Mogya","", "", "",""));
        allArrayList.add(new All("Calf","Nantu","", "", "",""));
        allArrayList.add(new All("Lips","Anofafa","", "", "",""));

        allArrayList.add(new All("Skull","Tikwankora","", "", "",""));
        allArrayList.add(new All("Skin","Honam","", "", "",""));

        allArrayList.add(new All("Liver","Berɛboɔ","", "", "",""));
        allArrayList.add(new All("Occiput","Atikɔ","", "", "",""));
        allArrayList.add(new All("Hip (1)","Dwonku","", "", "",""));
        allArrayList.add(new All("Hip (2)","Asen","", "", "",""));

//Pronouns



//1st Person subject

        allArrayList.add(new All("I","Me","1st Person Singular","Subject","", ""));
        allArrayList.add(new All("I am a boy","Me yɛ ɔbarima","","", "", ""));
        allArrayList.add(new All("I am a child","Me yɛ abofra","","", "", ""));

        allArrayList.add(new All("We","Yɛ(n)","1st Person Plural","Subject","", ""));
        allArrayList.add(new All("We are strong","Yɛn ho yɛ den","","", "", ""));
        allArrayList.add(new All("We will go there","Yɛbɛkɔ hɔ","","", "", ""));

//1st Person object

        allArrayList.add(new All("Me","Me","1st Person Singular","Object","", ""));
        allArrayList.add(new All("Give it to me","Fa ma me","","", "", ""));
        allArrayList.add(new All("You told me","Wo ka kyerɛɛ me","","", "", ""));


        allArrayList.add(new All("Us","Yɛn","1st Person Plural","Object","", ""));
        allArrayList.add(new All("You told us","Woka kyerɛɛ yɛn","","", "", ""));
        allArrayList.add(new All("They invited us","Wɔtoo nsa frɛɛ yɛn","","", "", ""));


//2nd Person subject
        allArrayList.add(new All("You","Wo","2nd Person Singular","Subject","", ""));
        allArrayList.add(new All("You are a boy","Woyɛ ɔbarima","","", "", ""));
        allArrayList.add(new All("You are beautiful","Wo ho yɛ fɛ","","", "", ""));

        allArrayList.add(new All("You","Mo","2nd Person Plural","Subject","", ""));
        allArrayList.add(new All("You are many","Mo dɔɔso","","", "", ""));
        allArrayList.add(new All("You are farmers","Mo yɛ akuafo","","", "", ""));


//2nd Person object

        allArrayList.add(new All("You","Wo","2nd Person Singular","Object","", ""));
        allArrayList.add(new All("I gave you money","Me de sika maa wo","","", "", ""));
        allArrayList.add(new All("She told you","Ɔka kyerɛɛ wo","","", "", ""));

        allArrayList.add(new All("You","Mo","2nd Person Plural","Object","", ""));
        allArrayList.add(new All("I saw all of you","Me huu mo nyinaa","","", "", ""));

//3rd Person subject

        allArrayList.add(new All("He","Ɔ-(no)","3rd Person Singular","Subject","", ""));
        allArrayList.add(new All("He gave it to you","Ɔde maa wo","","", "", ""));
        allArrayList.add(new All("She","Ɔ-(no)","3rd Person Singular","Subject","", ""));
        allArrayList.add(new All("She told you","Ɔka kyerɛɛ wo","","", "", ""));
        allArrayList.add(new All("It","Ɛ-(no)","3rd Person Singular","Subject","", ""));
        allArrayList.add(new All("It is nice","Ɛyɛ fɛ","","", "", ""));

        allArrayList.add(new All("They","Wɔ(n)","3rd Person Plural","Subject","", ""));
        allArrayList.add(new All("They gave it to you","Wɔde maa wo","","", "", ""));
        allArrayList.add(new All("They are strong","Wɔn ho yɛ den","","", "", ""));


//3rd Person object

        allArrayList.add(new All("Him","Ɔ-(no)","3rd Person Singular","Object","", ""));
        allArrayList.add(new All("I gave it to him","Me de maa no","","", "", ""));
        allArrayList.add(new All("It is him","Ɛyɛ ɔno","","", "", ""));
        allArrayList.add(new All("Her","Ɔ-(no)","3rd Person Singular","Object","", ""));
        allArrayList.add(new All("I gave it to her","Me de maa no","","", "", ""));
        allArrayList.add(new All("It is her","Ɛyɛ ɔno","","", "", ""));
        allArrayList.add(new All("It","Ɛ-(no)","3rd Person Singular","Object","", ""));
        allArrayList.add(new All("We killed it","Yekum no","","", "", ""));


        allArrayList.add(new All("Them","Wɔn","3rd Person Plural","Object","", ""));
        allArrayList.add(new All("Give it to them","Fa ma wɔn","","", "", ""));
        allArrayList.add(new All("Help them","Boa Wɔn","","", "", ""));


//Possessive All

        allArrayList.add(new All("Mine","Me dea","1st Person Singular","Possessive","",""));
        allArrayList.add(new All("Ours","Yɛn dea","1st Person Plural","Possessive","",""));

        allArrayList.add(new All("Yours","Wo dea","2nd Person Singular","Possessive","",""));
        allArrayList.add(new All("Yours","Mo dea","2nd Person Plural","Possessive","",""));


        allArrayList.add(new All("His","Ne dea","3rd Person Singular","Possessive","",""));
        allArrayList.add(new All("Hers","Ne dea","3rd Person Singular","Possessive","",""));
        allArrayList.add(new All("Theirs","Wɔn dea","3rd Person Plural","Possessive","",""));

        //



        //Months
        allArrayList.add(new All("January","Ɔpɛpɔn","","", "", ""));
        allArrayList.add(new All("We are in the month of January","Yɛwɔ Ɔpɛpɔn bosome mu","","", "", ""));
        allArrayList.add(new All("February","Ɔgyefoɔ","","", "", ""));
        allArrayList.add(new All("We will go to Ghana in February","Yɛbɛkɔ Ghana Ɔgyefoɔ bosome no mu","","", "", ""));
        allArrayList.add(new All("March","Ɔbɛnem","","", "", ""));
        allArrayList.add(new All("I will see you in March","Mehu wo wɔ Ɔbɛnem bosome no mu","","", "", ""));
        allArrayList.add(new All("April","Oforisuo","","", "", ""));
        allArrayList.add(new All("It often rains in April","Osu taa tɔ wɔ Oforisuo bosome no mu","","", "", ""));
        allArrayList.add(new All("May","Kotonimaa","","", "", ""));
        allArrayList.add(new All("June","Ayɛwohomumɔ","","", "", ""));
        allArrayList.add(new All("July","Kitawonsa","","", "", ""));
        allArrayList.add(new All("August","Ɔsanaa","","", "", ""));
        allArrayList.add(new All("I was born in the month of August","Wɔwoo me Ɔsanaa bosome no mu","","", "", ""));
        allArrayList.add(new All("September","Ɛbɔ","","", "", ""));
        allArrayList.add(new All("October","Ahinime","","", "", ""));
        allArrayList.add(new All("November","Obubuo","","", "", ""));
        allArrayList.add(new All("December","Ɔpɛnimma","","", "", ""));
        allArrayList.add(new All("It is often cold in December","Awɔw taa ba wɔ Ɔpɛnimma bosome no mu","","", "", ""));

        allArrayList.add(new All("Which month?","Bosome bɛn?","","", "", ""));


        //Food

        allArrayList.add(new All("Rice", "Ɛmo","","", "", ""));
        allArrayList.add(new All("Yam", "Bayerɛ","","", "", ""));
        allArrayList.add(new All("Plantain", "Bɔɔdeɛ","","", "", ""));
        allArrayList.add(new All("Cassava", "Bankye","","", "", ""));
        allArrayList.add(new All("Onion", "Gyeene","","", "", ""));
        allArrayList.add(new All("Salt", "Nkyene","","", "", ""));

//fruits
        allArrayList.add(new All("Fruit", "Aduaba","","", "", ""));
        allArrayList.add(new All("Apple", "Aprɛ","","", "", ""));
        allArrayList.add(new All("Banana", "Kwadu","","", "", ""));
        allArrayList.add(new All("Orange (1)", "Ankaa","","", "", ""));
        allArrayList.add(new All("Orange (2)", "Akutu","","", "", ""));
        allArrayList.add(new All("Pawpaw", "Borɔferɛ","","", "", ""));
        allArrayList.add(new All("Coconut", "Kube","","", "", ""));
        allArrayList.add(new All("Pear", "Paya","","", "", ""));
        allArrayList.add(new All("Tigernut", "Atadwe","","", "", ""));
        allArrayList.add(new All("Pineapple", "Aborɔbɛ","","", "", ""));
        allArrayList.add(new All("Ginger", "Akekaduro","","", "", ""));
        allArrayList.add(new All("Sugarcane", "Ahwedeɛ","","", "", ""));
        allArrayList.add(new All("Corn", "Aburo","","", "", ""));
        allArrayList.add(new All("Maize", "Aburo","","", "", ""));
        allArrayList.add(new All("Groundnut", "Nkateɛ","","", "", ""));
        allArrayList.add(new All("Peanut", "Nkateɛ","","", "", ""));
        allArrayList.add(new All("Palm fruit", "Abɛ","","", "", ""));


//Vegetables
        allArrayList.add(new All("Vegetable", "Atosodeɛ","","", "", ""));
        allArrayList.add(new All("Pepper", "Mako","","", "", ""));
        allArrayList.add(new All("Bean", "Adua","","", "", ""));
        allArrayList.add(new All("Okro", "Nkuruma","","", "", ""));
        allArrayList.add(new All("Garden eggs", "Nyaadewa","","", "", ""));
        allArrayList.add(new All("Tomato", "Ntoosi","","", "", ""));
        allArrayList.add(new All("Garlic", "Galik","","", "", ""));
        allArrayList.add(new All("Cucumber", "Ɛferɛ","","", "", ""));


        allArrayList.add(new All("Lobster", "Ɔbɔnkɔ","","", "", ""));
        allArrayList.add(new All("Cocoa", "Kokoo","","", "", ""));
        allArrayList.add(new All("Palm kernel", "Adwe","","", "", ""));
        allArrayList.add(new All("Palm kernel oil", "Adwe ngo","","", "", ""));
        allArrayList.add(new All("Vegetable oil", "Anwa","","", "", ""));
        allArrayList.add(new All("Snail", "Nwa","","", "", ""));
        allArrayList.add(new All("Groundnut soup", "Nkate nkwan","","", "", ""));
        allArrayList.add(new All("Palm nut soup", "Abɛ nkwan","","", "", ""));

//Others
        allArrayList.add(new All("Dough", "Mmɔre","","", "", ""));
        allArrayList.add(new All("Kenkey", "Dɔkono","","", "", ""));
        allArrayList.add(new All("Flour", "Esiam","","", "", ""));
        allArrayList.add(new All("Wheat", "Ayuo","","", "", ""));
        allArrayList.add(new All("Soup", "Nkwan","","", "", ""));
        allArrayList.add(new All("Stew", "Abomu","","", "", ""));
        allArrayList.add(new All("Egg", "Kosua","","", "", ""));
        allArrayList.add(new All("Bread (1)", "Paanoo","","", "", ""));
        allArrayList.add(new All("Bread (2)", "Burodo","","", "", ""));
        allArrayList.add(new All("Oil", "Ngo","","", "", ""));
        allArrayList.add(new All("Fish (1)", "Apataa","","", "", ""));
        allArrayList.add(new All("Fish (2)", "Nsuomnam","","", "", ""));
        allArrayList.add(new All("Pork", "Prakonam","","", "", ""));
        allArrayList.add(new All("Meat", "Nam","","", "", ""));
        allArrayList.add(new All("Mutton", "Odwannam","","", "", ""));
        allArrayList.add(new All("Lamb", "Odwannam","","", "", ""));
        allArrayList.add(new All("Sugar", "Asikyire","","", "", ""));
        allArrayList.add(new All("Honey", "Ɛwoɔ","","", "", ""));
        allArrayList.add(new All("Water", "Nsuo","","", "", ""));
        allArrayList.add(new All("Food", "Aduane","","", "", ""));

        Collections.sort(allArrayList);

        allArrayList.add(new All("I am hungry", "Ɛkɔm de me","","", "", ""));
        allArrayList.add(new All("Are you hungry?", "Ɛkɔm de wo anaa?","","", "", ""));
        allArrayList.add(new All("What will you eat?", "Dɛn na wobedi?","","", "", ""));
        allArrayList.add(new All("I will eat kenkey", "Medi dɔkono","","", "", ""));


        //FAMILY

        allArrayList.add(new All("Abusua", "Abusua","","", "", ""));
        allArrayList.add(new All("Families", "Mmusua","","", "", ""));

        allArrayList.add(new All("Father (1)", "Papa","","", "", ""));
        allArrayList.add(new All("Father (2)", "Agya","","", "", ""));
        allArrayList.add(new All("Father (3)", "Ɔse","","", "", ""));
        allArrayList.add(new All("My father (1)", "Me papa","","", "", ""));
        allArrayList.add(new All("My father (2)", "M'agya","","", "", ""));
        allArrayList.add(new All("My father (3)", "Me se","","", "", ""));
        allArrayList.add(new All("Daddy", "Dada","","", "", ""));

        allArrayList.add(new All("Mother (1)", "Maame","","", "", ""));
        allArrayList.add(new All("Mother (2)", "Ɛna","","", "", ""));
        allArrayList.add(new All("Mother (3)", "Oni","","", "", ""));
        allArrayList.add(new All("My Mother (1)", "Me maame","","", "", ""));
        allArrayList.add(new All("My Mother (2)", "Me na","","", "", ""));
        allArrayList.add(new All("My Mother (3)", "Me ni","","", "", ""));
        allArrayList.add(new All("Mummy", "Mama","","", "", ""));


        allArrayList.add(new All("Parent", "Ɔwofo","","", "", ""));
        allArrayList.add(new All("Parents", "Awofo","","", "", ""));
        allArrayList.add(new All("Child (1)", "Abofra","","", "", ""));
        allArrayList.add(new All("Child (2)", "Akwadaa","","", "", ""));
        allArrayList.add(new All("Children (1)", "mma","","", "", ""));
        allArrayList.add(new All("Children (2)", "mmofra","","", "", ""));
        allArrayList.add(new All("Baby", "Abofra","","", "", ""));

        allArrayList.add(new All("Firstborn (1)", "Abakan","","", "", ""));
        allArrayList.add(new All("Firstborn (2)", "Piesie","","", "", ""));
        allArrayList.add(new All("Lastborn", "Kaakyire","","", "", ""));

        allArrayList.add(new All("Husband", "Kunu","","", "", ""));
        allArrayList.add(new All("Husbands", "Kununom","","", "", ""));

        allArrayList.add(new All("Wife", "Yere","","", "", ""));
        allArrayList.add(new All("Wives", "Yerenom","","", "", ""));

        allArrayList.add(new All("Brother", "Nuabarima","","", "", ""));
        allArrayList.add(new All("Brothers", "Nua mmarima","","", "", ""));
        allArrayList.add(new All("Sister", "Nuabaa","","", "", ""));
        allArrayList.add(new All("Sisters", "Nua mmaa","","", "", ""));

        allArrayList.add(new All("Sibling", "Nua","","", "", ""));
        allArrayList.add(new All("Siblings", "Nuanom","","", "", ""));

        allArrayList.add(new All("Son", "Babarima","","", "", ""));
        allArrayList.add(new All("Sons", "mma mmarima","","", "", ""));
        allArrayList.add(new All("Daughter", "Babaa","","", "", ""));
        allArrayList.add(new All("Daughters", "Mma mmaa","","", "", ""));

        allArrayList.add(new All("Cousin", "Nua","","", "", ""));
        allArrayList.add(new All("Grandchild", "Banana","","", "", ""));
        allArrayList.add(new All("Great Grandchild", "Nanankanso","","", "", ""));
        allArrayList.add(new All("Grandfather", "Nanabarima","","", "", ""));
        allArrayList.add(new All("Great grandfather", "Nanabarima prenu","","", "", ""));
        allArrayList.add(new All("Grandmother", "Nanabaa","","", "", ""));
        allArrayList.add(new All("Great grandmother", "Nanabaa prenu","","", "", ""));

        allArrayList.add(new All("In-law", "Asew","","", "", ""));
        allArrayList.add(new All("Father-in-law", "Asebarima","","", "", ""));
        allArrayList.add(new All("Mother-in-law", "Asebaa","","", "", ""));
        allArrayList.add(new All("Brother-in-law", "Akonta","","", "", ""));
        allArrayList.add(new All("Sister-in-law", "Akumaa","","", "", ""));
        allArrayList.add(new All("Son-in-law (1)", "Asew","","", "", ""));
        allArrayList.add(new All("Son-in-law (2)", "Babaa kunu","","", "", ""));
        allArrayList.add(new All("Daughter-in-law", "Asew","","", "", ""));
        allArrayList.add(new All("Daughter-in-law", "Babarima yere","","", "", ""));


        allArrayList.add(new All("Maternal Uncle", "Wɔfa","","", "", ""));
        allArrayList.add(new All("Paternal Uncle (1)", "Papa","","", "", ""));
        allArrayList.add(new All("Paternal Uncle (2)", "Agya","","", "", ""));
        allArrayList.add(new All("Paternal Uncle (3)", "Papa nuabarima","","", "", ""));

        allArrayList.add(new All("Paternal Aunt", "Sewaa","","", "", ""));
        allArrayList.add(new All("My Paternal Aunt", "Me Sewaa","","", "", ""));

        allArrayList.add(new All("Maternal Aunt (1)", "Maame nuabaa","","", "", ""));
        allArrayList.add(new All("Maternal Aunt (2)", "Maame","","", "", ""));
        allArrayList.add(new All("My maternal Aunt", "Me maame nuabaa","","", "", ""));

        allArrayList.add(new All("Niece", "Wɔfaase","","", "", ""));
        allArrayList.add(new All("Nephew", "Wɔfaase","","", "", ""));

        allArrayList.add(new All("Cousin (1)", "Nua","","", "", ""));
        allArrayList.add(new All("Cousin (2)", "Wɔfa ba","","", "", ""));
        allArrayList.add(new All("Cousin (3)", "Sewaa ba","","", "", ""));
        allArrayList.add(new All("My Cousin", "Me nua","","", "", ""));


        allArrayList.add(new All("Adopted child", "Abanoma","","", "", ""));
        allArrayList.add(new All("Orphan", "Agyanka","","", "", ""));
        allArrayList.add(new All("Widow", "Okunafo","","", "", ""));
        allArrayList.add(new All("Widower", "Barima kunafo","","", "", ""));

        allArrayList.add(new All("Marriage", "Awareɛ","","", "", ""));

        allArrayList.add(new All("Twins", "Ntafoɔ","","", "", ""));
        allArrayList.add(new All("Triplets", "Ahenasa","","", "", ""));
        allArrayList.add(new All("Quadruplets", "Ahenanan","","", "", ""));

        //COMMONEXPRESSIONSA

        allArrayList.add(new All("Good morning (1)","Maakye","","", "", ""));
        allArrayList.add(new All("Good morning (2)","Mema wo akye","","", "", ""));
        allArrayList.add(new All("Good afternoon (1)","Maaha","","", "", ""));
        allArrayList.add(new All("Good afternoon (2)","Mema wo aha","","", "", ""));
        allArrayList.add(new All("Good evening (1)","Maadwo","","", "", ""));
        allArrayList.add(new All("Good evening (2)","Mema wo adwo","","", "", ""));
        allArrayList.add(new All("Good night","Da yie","","", "", ""));

        allArrayList.add(new All("How are you?","Wo ho te sɛn?","","", "", ""));
        allArrayList.add(new All("How is your mother?","Wo maame ho te sɛn?","","", "", ""));
        allArrayList.add(new All("How is your wife?","Wo yere ho te sɛn?","","", "", ""));
        allArrayList.add(new All("I am fine","Me ho yɛ","","", "", ""));
        allArrayList.add(new All("How are they doing?","Wɔn ho te sɛn?","","", "", ""));
        allArrayList.add(new All("I am not feeling well (1)","Me ho mfa me","","", "", ""));
        allArrayList.add(new All("I am not feeling well (2)","Mente apɔ","","", "", ""));

        allArrayList.add(new All("She is fine","Ne ho yɛ","","", "", ""));
        allArrayList.add(new All("He is fine","Ne ho yɛ","","", "", ""));
        allArrayList.add(new All("They are fine","Wɔn ho yɛ","","", "", ""));
        allArrayList.add(new All("They are all fine","Wɔn nyinaa ho yɛ","","", "", ""));


        allArrayList.add(new All("I am happy to meet you","M'ani agye sɛ mahyia wo","","", "", ""));
        allArrayList.add(new All("Welcome","Akwaaba","","", "", ""));
        allArrayList.add(new All("I bid you welcome","Mema wo akwaaba","","", "", ""));
        allArrayList.add(new All("I am happy","M'ani agye","","", "", ""));
        allArrayList.add(new All("I am sad","Me werɛ ahow","","", "", ""));

        allArrayList.add(new All("Stop crying","Gyae su","","", "", ""));


        allArrayList.add(new All("Please (1)","Mepa wo kyɛw","","", "", ""));
        allArrayList.add(new All("Please (2)","Mesrɛ wo","","", "", ""));
        allArrayList.add(new All("Thank you (1)","Medaase","","", "", ""));
        allArrayList.add(new All("Thank you (2)","Me da wo ase","","", "", ""));
        allArrayList.add(new All("I miss you","Mafe wo","","", "", ""));
        allArrayList.add(new All("I will miss you","Mɛfe wo","","", "", ""));
        allArrayList.add(new All("We will miss you","Yɛbɛfe wo","","", "", ""));
        allArrayList.add(new All("We will miss you (Plural)","Yɛbɛfe mo","","", "", ""));
        allArrayList.add(new All("Love","Ɔdɔ","","", "", ""));
        allArrayList.add(new All("I love you","Me dɔ wo","","", "", ""));
        allArrayList.add(new All("We love you","Yɛdɔ mo","","", "", ""));
        allArrayList.add(new All("Do you love me?","Wo dɔ me?","","", "", ""));
        allArrayList.add(new All("Yes","Aane","","", "", ""));
        allArrayList.add(new All("No","Dabi","","", "", ""));
        allArrayList.add(new All("My name is Kwaku (1)","Yɛfrɛ me Kwaku","","", "", ""));
        allArrayList.add(new All("My name is Kwaku (2)","Me din de Kwaku","","", "", ""));
        allArrayList.add(new All("What is your name? (1)","Yɛfrɛ wo sɛn?","","", "", ""));
        allArrayList.add(new All("What is your name? (2)","Wo din de sɛn?","","", "", ""));

        allArrayList.add(new All("How old are you?","Wadi mfe sɛn?","","", "", ""));
        allArrayList.add(new All("I am 30 years old","Madi mfe aduasa","","", "", ""));
        allArrayList.add(new All("Call me","Frɛ me","","", "", ""));
        allArrayList.add(new All("Do you speak English?","Wo ka borɔfo?","","", "", ""));

        allArrayList.add(new All("I am sick","Me yare","","", "", ""));
        allArrayList.add(new All("I need money","Me hia sika","","", "", ""));
        allArrayList.add(new All("Do you have money","Wowɔ sika?","","", "", ""));

        allArrayList.add(new All("Congratulations","Ayekoo","","", "", ""));
        allArrayList.add(new All("See you soon","Ɛrenkyɛ mehu wo","","", "", ""));
        allArrayList.add(new All("Give me a hug","Yɛ me atuu","","", "", ""));


        //COLOURS

        allArrayList.add(new All("Colour (1)","Ahosu","","", "", ""));
        allArrayList.add(new All("Colour (2)","Kɔla","","", "", ""));
        allArrayList.add(new All("What colour is it?","Ɛyɛ kɔla bɛn?","","", "", ""));
        allArrayList.add(new All("Red","Kɔkɔɔ","","", "", ""));
        allArrayList.add(new All("Black","Tuntum","","", "", ""));
        allArrayList.add(new All("White (1)","Fitaa","","", "", ""));
        allArrayList.add(new All("White (2)","Fufuo","","", "", ""));
        allArrayList.add(new All("Green","Ahabammono","","", "", ""));
        allArrayList.add(new All("Blue","Bruu","","", "", ""));
        allArrayList.add(new All("Brown","Dodowee","","", "", ""));
        allArrayList.add(new All("Grey","Nsonso","","", "", ""));
        allArrayList.add(new All("Ash","Nsonso","","", "", ""));
        allArrayList.add(new All("Yellow","Akokɔsrade","","", "", ""));
        allArrayList.add(new All("Spotted","Nsisimu","","", "", ""));
        allArrayList.add(new All("Purple (1)","Beredum","","", "", ""));
        allArrayList.add(new All("Purple (2)","Afasebiri","","", "", ""));
        allArrayList.add(new All("Pink","Memen","","", "", ""));
        allArrayList.add(new All("Dark","Sum","","", "", ""));

        //DAYS OF WEEK

        allArrayList.add(new All("Monday", "Dwoada","","", "", ""));
        allArrayList.add(new All("Tuesday", "Benada","","", "", ""));
        allArrayList.add(new All("Wednesday", "Wukuada","","", "", ""));
        allArrayList.add(new All("Thursday", "Yawada","","", "", ""));
        allArrayList.add(new All("Friday", "Fiada","","", "", ""));
        allArrayList.add(new All("Saturday", "Memeneda","","", "", ""));
        allArrayList.add(new All("Sunday", "Kwasiada","","", "", ""));


        //TIME

        allArrayList.add(new All("Second","Anibu","","", "", ""));
        allArrayList.add(new All(" One second","Anibu baako","","", "", ""));
        allArrayList.add(new All(" Two seconds","Anibu mmienu","","", "", ""));


        allArrayList.add(new All("Minute (1)","Simma","","", "", ""));
        allArrayList.add(new All("Minute (2)","Miniti","","", "", ""));
        allArrayList.add(new All("One minute","Simma baako","","", "", ""));
        allArrayList.add(new All("Two minutes","Simma mmienu","","", "", ""));


        allArrayList.add(new All("Hour","Dɔnhwere","","", "", ""));
        allArrayList.add(new All("Hours","Nnɔnhwere","","", "", ""));
        allArrayList.add(new All("1 hour","Dɔnhwere baako","","", "", ""));
        allArrayList.add(new All("2 hours","Nnɔnhwere mmienu","","", "", ""));

        allArrayList.add(new All("Day","Da","","", "", ""));
        allArrayList.add(new All("Days","Nna","","", "", ""));
        allArrayList.add(new All("One day","Da koro","","", "", ""));
        allArrayList.add(new All("Two days","Nnanu","","", "", ""));
        allArrayList.add(new All("Three days","Nnansa","","", "", ""));
        allArrayList.add(new All("Four days","Nnanan","","", "", ""));
        allArrayList.add(new All("Five days","Nnanum","","", "", ""));
        allArrayList.add(new All("Six days","Nnansia","","", "", ""));
        allArrayList.add(new All("Seven days","Nnanson","","", "", ""));

        allArrayList.add(new All("First day","Da a edi kan","","", "", ""));
        allArrayList.add(new All("Second day","Da a ɛtɔ so mmienu","","", "", ""));
        allArrayList.add(new All("Third day","Da a ɛtɔ so mmiɛnsa","","", "", ""));

        allArrayList.add(new All("Week (1)","Nnawɔtwe","","", "", ""));
        allArrayList.add(new All("Week (2)","Dapɛn","","", "", ""));
        allArrayList.add(new All("Weeks (1)","Nnawɔtwe","","", "", ""));
        allArrayList.add(new All("Weeks (2)","Adapɛn","","", "", ""));
        allArrayList.add(new All("First week","Nnawɔtwe a edi kan","","", "", ""));
        allArrayList.add(new All("Second week","Nnawɔtwe a ɛtɔ so mmienu","","", "", ""));
        allArrayList.add(new All("Third week","Nnawɔtwe a ɛtɔ so mmiɛnsa","","", "", ""));
        allArrayList.add(new All("Fortnight","Nnawɔtwe mmienu","","", "", ""));
        allArrayList.add(new All("Next week","Nnawɔtwe a edi hɔ","","", "", ""));
        allArrayList.add(new All("Last week","Nnawɔtwe a etwaam","","", "", ""));
        allArrayList.add(new All("Last two weeks","Nnawɔtwe mmienu a etwaam","","", "", ""));
        allArrayList.add(new All("This week","Nnawɔtwe yi","","", "", ""));

        allArrayList.add(new All("Month (1)","Ɔsram","","", "", ""));
        allArrayList.add(new All("Month (2)","Bosome","","", "", ""));
        allArrayList.add(new All("Months","Abosome","","", "", ""));
        allArrayList.add(new All("This Month","Bosome yi","","", "", ""));
        allArrayList.add(new All("Last Month","Bosome a etwaam","","", "", ""));
        allArrayList.add(new All("First Month (2)","Bosome a edi kan","","", "", ""));
        allArrayList.add(new All("Second Month (2)","Bosome a ɛtɔ so mmienu","","", "", ""));

        allArrayList.add(new All("Year","Afe","","", "", ""));
        allArrayList.add(new All("Years","Mfe","","", "", ""));
        allArrayList.add(new All("This year","Afe yi","","", "", ""));
        allArrayList.add(new All("Last year","Afe a etwaam","","", "", ""));
        allArrayList.add(new All("A year by this All","Afe sesɛɛ","","", "", ""));
        allArrayList.add(new All("Next year (1)","Afedan","","", "", ""));
        allArrayList.add(new All("Next year (2)","Afe a yebesi mu","","", "", ""));


        allArrayList.add(new All("Today","Nnɛ","","", "", ""));
        allArrayList.add(new All("Yesterday","Ɛnnora","","", "", ""));
        allArrayList.add(new All("Tomorrow","Ɔkyena","","", "", ""));

        allArrayList.add(new All("When","Bere bɛn","","", "", ""));
        allArrayList.add(new All("What is the All?","Abɔ sɛn?","","", "", ""));
        allArrayList.add(new All("What All is it?","Abɔ sɛn?","","", "", ""));



        allArrayList.add(new All("Morning","Anɔpa","","", "", ""));
        allArrayList.add(new All("This morning","Anɔpa yi","","", "", ""));

        allArrayList.add(new All("Afternoon","Awia","","", "", ""));
        allArrayList.add(new All("This afternoon","Awia yi","","", "", ""));

        allArrayList.add(new All("Evening","Anwummere","","", "", ""));
        allArrayList.add(new All("This evening","Anwummere yi","","", "", ""));

        allArrayList.add(new All("Night","Anadwo","","", "", ""));
        allArrayList.add(new All("This night","Anadwo yi","","", "", ""));

        allArrayList.add(new All("Midnight","Dasuom","","", "", ""));

        allArrayList.add(new All("Dawn","Anɔpatutu","","", "", ""));


        allArrayList.add(new All("1 am","Anɔpa Dɔnkoro","","", "", ""));
        allArrayList.add(new All("1:01","Dɔnkoro apa ho simma baako","","", "", ""));
        allArrayList.add(new All("1:05","Dɔnkoro apa ho simma num","","", "", ""));
        allArrayList.add(new All("1:15","Dɔnkoro apa ho simma dunum","","", "", ""));



        allArrayList.add(new All("2 am","Anɔpa Nnɔnmmienu","","", "", ""));
        allArrayList.add(new All("2:30 (1)","Nnɔnmmienu ne fa","","", "", ""));
        allArrayList.add(new All("2:30 (2)","Nnɔnmmienu apa ho simma aduasa","","", "", ""));


        allArrayList.add(new All("3 am","Anɔpa Nnɔnmmiɛnsa ","","", "", ""));

        allArrayList.add(new All("4 am","Anɔpa Nnɔnnan","","", "", ""));
        allArrayList.add(new All("5 am","Anɔpa Nnɔnnum","","", "", ""));
        allArrayList.add(new All("6 am","Anɔpa Nnɔnsia","","", "", ""));
        allArrayList.add(new All("7 am","Anɔpa Nnɔnson","","", "", ""));
        allArrayList.add(new All("8 am","Anɔpa Nnɔnwɔtwe","","", "", ""));
        allArrayList.add(new All("9 am","Anɔpa Nnɔnkron","","", "", ""));
        allArrayList.add(new All("10 am","Anɔpa Nnɔndu","","", "", ""));
        allArrayList.add(new All("11 am","Anɔpa Nnɔndubaako","","", "", ""));
        allArrayList.add(new All("12 am","Nnɔndummienu","","", "", ""));
        allArrayList.add(new All("Noon (1)","Nnɔndummienu","","", "", ""));
        allArrayList.add(new All("Noon (2)","Owigyinae","","", "", ""));
        allArrayList.add(new All("1 pm","Awia Dɔnkoro","","", "", ""));
        allArrayList.add(new All("2 pm","Awia Nnɔnmmienu","","", "", ""));
        allArrayList.add(new All("3 pm","Awia Nnɔnmmiɛnsa","","", "", ""));
        allArrayList.add(new All("4 pm","Anwummere Nnɔnnan","","", "", ""));
        allArrayList.add(new All("5 pm","Anwummere Nnɔnnum","","", "", ""));
        allArrayList.add(new All("6 pm","Anwummere Nnɔnsia","","", "", ""));
        allArrayList.add(new All("7 pm","Anwummere Nnɔnson","","", "", ""));
        allArrayList.add(new All("8 pm","Anadwo Nnɔnwɔtwe","","", "", ""));
        allArrayList.add(new All("9 pm","Anadwo Nnɔnkron","","", "", ""));
        allArrayList.add(new All("10 pm","Anadwo Nnɔndu","","", "", ""));
        allArrayList.add(new All("11 pm","Anadwo Nnɔndubaako","","", "", ""));
        allArrayList.add(new All("12 pm (1)","Anadwo dummienu","","", "", ""));
        allArrayList.add(new All("12 pm (2)","Dasuom","","", "", ""));


        //WEATHER ARRAY

        allArrayList.add(new All("Rain","Osu","","", "", ""));
        allArrayList.add(new All("It is raining","Osu retɔ","","", "", ""));
        allArrayList.add(new All("Will it rain today?","Osu bɛtɔ nnɛ anaa?","","", "", ""));
        allArrayList.add(new All("Did it rain yesterday?","Osu tɔɔ ɛnnora anaa?","","", "", ""));
        allArrayList.add(new All("It will rain today","Osu bɛtɔ nnɛ","","", "", ""));
        allArrayList.add(new All("Umbrella","Kyinneɛ","","", "", ""));
        allArrayList.add(new All("Take the umbrella","Fa kyinneɛ no","","", "", ""));
        allArrayList.add(new All("It is drizzling","Osu repetepete","","", "", ""));

        allArrayList.add(new All("Snow","Sukyerɛmma","","", "", ""));


        allArrayList.add(new All("Sun","Awia","","", "", ""));
        allArrayList.add(new All("It is sunny","Awia abɔ","","", "", ""));
        allArrayList.add(new All("It is very sunny","Awia abɔ paa","","", "", ""));
        allArrayList.add(new All("It is hot today","Ewiem ayɛ hye nnɛ","","", "", ""));
        allArrayList.add(new All("Sunset","Owitɔe","","", "", ""));
        allArrayList.add(new All("The sun has set","Owia no akɔtɔ","","", "", ""));
        allArrayList.add(new All("Sunrise","Owipue","","", "", ""));
        allArrayList.add(new All("The sun is rising","Owia no repue","","", "", ""));
        allArrayList.add(new All("The sun has risen","Owia no apue","","", "", ""));

        allArrayList.add(new All("It is warm","Ahohuru wom","","", "", ""));
        allArrayList.add(new All("I am feeling hot","Ɔhyew de me","","", "", ""));
        allArrayList.add(new All("I am feeling warm","Ahohuru de me","","", "", ""));
        allArrayList.add(new All("It is cold","Awɔw wom","","", "", ""));
        allArrayList.add(new All("It is very cold","Awɔw wom paa","","", "", ""));
        allArrayList.add(new All("I am feeling cold","Awɔw de me","","", "", ""));
        allArrayList.add(new All("It is very chilly","Awɔw wom paa","","", "", ""));

        allArrayList.add(new All("Lightning","Anyinam","","", "", ""));
        allArrayList.add(new All("Thunder","Apranaa","","", "", ""));
        allArrayList.add(new All("Cloud","Mununkum","","", "", ""));
        allArrayList.add(new All("Rain clouds have formed","Osu amuna","","", "", ""));
        allArrayList.add(new All("It is cloudy","Ewiem ayɛ kusuu","","", "", ""));
        allArrayList.add(new All("Wind","Mframa","","", "", ""));
        allArrayList.add(new All("It is windy","Mframa rebɔ","","", "", ""));
        allArrayList.add(new All("It is very windy","Mframa rebɔ paa","","", "", ""));

        allArrayList.add(new All("Sky (1)","Soro","","", "", ""));
        allArrayList.add(new All("Sky (2)","Ewiem","","", "", ""));
        allArrayList.add(new All("Weather","Ewiem tebea","","", "", ""));
        allArrayList.add(new All("How is the All like?","Ewiem tebea te sɛn?","","", "", ""));
        allArrayList.add(new All("What will the All be like today?","Ɛnnɛ ewiem tebea bɛyɛ sɛn?","","", "", ""));
        allArrayList.add(new All("Weather changes","Ewiem nsakrae","","", "", ""));
        allArrayList.add(new All("Stars","Nsoromma","","", "", ""));
        allArrayList.add(new All("The stars are glittering","Nsoromma no rehyerɛn","","", "", ""));

        allArrayList.add(new All("Misty","Ɛbɔ","","", "", ""));
        allArrayList.add(new All("It is misty","Ɛbɔ asi","","", "", ""));

        allArrayList.add(new All("Harmattan","Ɔpɛ","","", "", ""));
        allArrayList.add(new All("Harmattan is here","Ɔpɛ asi","","", "", ""));

        allArrayList.add(new All("Shade","Onwunu","","", "", ""));
        allArrayList.add(new All("Storm","Ahum","","", "", ""));
        allArrayList.add(new All("Rainbow","Nyankontɔn","","", "", ""));
        allArrayList.add(new All("I saw the rainbow","Me huu nyankontɔn no","","", "", ""));


        allArrayList.add(new All("Moon","Bosome","","", "", ""));


        //NUMBERS

        allArrayList.add(new All("0", "Hwee/Ohunu","","", "", ""));
        allArrayList.add(new All("1", "Baako","","", "", ""));
        allArrayList.add(new All("2", "Mmienu","","", "", ""));
        allArrayList.add(new All("3", "Mmiɛnsa","","", "", ""));
        allArrayList.add(new All("4", "Ɛnan","","", "", ""));
        allArrayList.add(new All("5", "Enum","","", "", ""));
        allArrayList.add(new All("6", "Nsia","","", "", ""));
        allArrayList.add(new All("7", "Nson","","", "", ""));
        allArrayList.add(new All("8", "Nwɔtwe","","", "", ""));
        allArrayList.add(new All("9", "Nkron","","", "", ""));
        allArrayList.add(new All("10", "Du","","", "", ""));

        allArrayList.add(new All("11", "Dubaako","","", "", ""));
        allArrayList.add(new All("12", "Dummienu","","", "", ""));
        allArrayList.add(new All("13", "Dummiɛnsa","","", "", ""));
        allArrayList.add(new All("14", "Dunan","","", "", ""));
        allArrayList.add(new All("15", "Dunum","","", "", ""));
        allArrayList.add(new All("16", "Dunsia","","", "", ""));
        allArrayList.add(new All("17", "Dunson","","", "", ""));
        allArrayList.add(new All("18", "Dunwɔtwe","","", "", ""));
        allArrayList.add(new All("19", "Dunkron","","", "", ""));
        allArrayList.add(new All("20", "Aduonu","","", "", ""));

        allArrayList.add(new All("21", "Aduonu baako","","", "", ""));
        allArrayList.add(new All("22", "Aduonu mmienu","","", "", ""));
        allArrayList.add(new All("23", "Aduonu mmiɛnsa","","", "", ""));
        allArrayList.add(new All("24", "Aduonu nan","","", "", ""));
        allArrayList.add(new All("25", "Aduonu num","","", "", ""));
        allArrayList.add(new All("26", "Aduonu nsia","","", "", ""));
        allArrayList.add(new All("27", "Aduonu nson","","", "", ""));
        allArrayList.add(new All("28", "Aduonu nwɔtwe","","", "", ""));
        allArrayList.add(new All("29", "Aduonu nkron","","", "", ""));
        allArrayList.add(new All("30", "Aduasa","","", "", ""));


        allArrayList.add(new All("31", "Aduasa baako","","", "", ""));
        allArrayList.add(new All("32", "Aduasa mmienu","","", "", ""));
        allArrayList.add(new All("33", "Aduasa mmiɛnsa","","", "", ""));
        allArrayList.add(new All("34", "Aduasa nan","","", "", ""));
        allArrayList.add(new All("35", "Aduasa num","","", "", ""));
        allArrayList.add(new All("36", "Aduasa nsia","","", "", ""));
        allArrayList.add(new All("37", "Aduasa nson","","", "", ""));
        allArrayList.add(new All("38", "Aduasa nwɔtwe","","", "", ""));
        allArrayList.add(new All("39", "Aduasa nkron","","", "", ""));
        allArrayList.add(new All("40", "Aduanan","","", "", ""));

        allArrayList.add(new All("41", "Aduanan baako","","", "", ""));
        allArrayList.add(new All("42", "Aduanan mmienu","","", "", ""));
        allArrayList.add(new All("43", "Aduanan mmiɛnsa","","", "", ""));
        allArrayList.add(new All("44", "Aduanan nan","","", "", ""));
        allArrayList.add(new All("45", "Aduanan num","","", "", ""));
        allArrayList.add(new All("46", "Aduanan nsia","","", "", ""));
        allArrayList.add(new All("47", "Aduanan nson","","", "", ""));
        allArrayList.add(new All("48", "Aduanan nwɔtwe","","", "", ""));
        allArrayList.add(new All("49", "Aduanan nkron","","", "", ""));
        allArrayList.add(new All("50", "Aduonum","","", "", ""));

        allArrayList.add(new All("51", "Aduonum baako","","", "", ""));
        allArrayList.add(new All("52", "Aduonum mmienu","","", "", ""));
        allArrayList.add(new All("53", "Aduonum mmiɛnsa","","", "", ""));
        allArrayList.add(new All("54", "Aduonum nan","","", "", ""));
        allArrayList.add(new All("55", "Aduonum num","","", "", ""));
        allArrayList.add(new All("56", "Aduonum nsia","","", "", ""));
        allArrayList.add(new All("57", "Aduonum nson","","", "", ""));
        allArrayList.add(new All("58", "Aduonum nwɔtwe","","", "", ""));
        allArrayList.add(new All("59", "Aduonum nkron","","", "", ""));
        allArrayList.add(new All("60", "Aduosia","","", "", ""));

        allArrayList.add(new All("61", "Aduosia baako","","", "", ""));
        allArrayList.add(new All("62", "Aduosia mmienu","","", "", ""));
        allArrayList.add(new All("63", "Aduosia mmiɛnsa","","", "", ""));
        allArrayList.add(new All("64", "Aduosia nan","","", "", ""));
        allArrayList.add(new All("65", "Aduosia num","","", "", ""));
        allArrayList.add(new All("66", "Aduosia nsia","","", "", ""));
        allArrayList.add(new All("67", "Aduosia nson","","", "", ""));
        allArrayList.add(new All("68", "Aduosia nwɔtwe","","", "", ""));
        allArrayList.add(new All("69", "Aduosia nkron","","", "", ""));
        allArrayList.add(new All("70", "Aduoson","","", "", ""));

        allArrayList.add(new All("71", "Aduoson baako","","", "", ""));
        allArrayList.add(new All("72", "Aduoson mmienu","","", "", ""));
        allArrayList.add(new All("73", "Aduoson mmiɛnsa","","", "", ""));
        allArrayList.add(new All("74", "Aduoson nan","","", "", ""));
        allArrayList.add(new All("75", "Aduoson num","","", "", ""));
        allArrayList.add(new All("76", "Aduoson nsia","","", "", ""));
        allArrayList.add(new All("77", "Aduoson nson","","", "", ""));
        allArrayList.add(new All("78", "Aduoson nwɔtwe","","", "", ""));
        allArrayList.add(new All("79", "Aduoson nkron","","", "", ""));
        allArrayList.add(new All("80", "Aduowɔtwe","","", "", ""));

        allArrayList.add(new All("81", "Aduowɔtwe baako","","", "", ""));
        allArrayList.add(new All("82", "Aduowɔtwe mmienu","","", "", ""));
        allArrayList.add(new All("83", "Aduowɔtwe mmiɛnsa","","", "", ""));
        allArrayList.add(new All("84", "Aduowɔtwe nan","","", "", ""));
        allArrayList.add(new All("85", "Aduowɔtwe num","","", "", ""));
        allArrayList.add(new All("86", "Aduowɔtwe nsia","","", "", ""));
        allArrayList.add(new All("87", "Aduowɔtwe nson","","", "", ""));
        allArrayList.add(new All("88", "Aduowɔtwe nwɔtwe","","", "", ""));
        allArrayList.add(new All("89", "Aduowɔtwe nkron","","", "", ""));
        allArrayList.add(new All("90", "Aduokron","","", "", ""));

        allArrayList.add(new All("91", "Aduokron baako","","", "", ""));
        allArrayList.add(new All("92", "Aduokron mmienu","","", "", ""));
        allArrayList.add(new All("93", "Aduokron mmiɛnsa","","", "", ""));
        allArrayList.add(new All("94", "Aduokron nan","","", "", ""));
        allArrayList.add(new All("95", "Aduokron num","","", "", ""));
        allArrayList.add(new All("96", "Aduokron nsia","","", "", ""));
        allArrayList.add(new All("97", "Aduokron nson","","", "", ""));
        allArrayList.add(new All("98", "Aduokron nwɔtwe","","", "", ""));
        allArrayList.add(new All("99", "Aduokron nkron","","", "", ""));
        allArrayList.add(new All("100", "Ɔha","","", "", ""));

        allArrayList.add(new All("101", "Ɔha ne baako","","", "", ""));
        allArrayList.add(new All("102", "Ɔha ne mmienu","","", "", ""));
        allArrayList.add(new All("103", "Ɔha ne mmiɛnsa","","", "", ""));
        allArrayList.add(new All("104", "Ɔha ne nan","","", "", ""));
        allArrayList.add(new All("105", "Ɔha ne num","","", "", ""));
        allArrayList.add(new All("106", "Ɔha ne nsia","","", "", ""));
        allArrayList.add(new All("107", "Ɔha ne nson","","", "", ""));
        allArrayList.add(new All("108", "Ɔha ne nwɔtwe","","", "", ""));
        allArrayList.add(new All("109", "Ɔha ne nkron","","", "", ""));
        allArrayList.add(new All("110", "Ɔha ne du","","", "", ""));

        allArrayList.add(new All("111", "Ɔha ne dubaako","","", "", ""));
        allArrayList.add(new All("112", "Ɔha ne dummienu","","", "", ""));
        allArrayList.add(new All("113", "Ɔha ne dummiɛnsa","","", "", ""));
        allArrayList.add(new All("114", "Ɔha ne dunan","","", "", ""));
        allArrayList.add(new All("115", "Ɔha ne dunum","","", "", ""));
        allArrayList.add(new All("116", "Ɔha ne dunsia","","", "", ""));
        allArrayList.add(new All("117", "Ɔha ne dunson","","", "", ""));
        allArrayList.add(new All("118", "Ɔha ne dunwɔtwe","","", "", ""));
        allArrayList.add(new All("119", "Ɔha ne dunkron","","", "", ""));
        allArrayList.add(new All("120", "Ɔha aduonu","","", "", ""));


        allArrayList.add(new All("121", "Ɔha aduonu baako","","", "", ""));
        allArrayList.add(new All("122", "Ɔha aduonu mmienu","","", "", ""));
        allArrayList.add(new All("123", "Ɔha aduonu mmiɛnsa","","", "", ""));
        allArrayList.add(new All("124", "Ɔha aduonu nan","","", "", ""));
        allArrayList.add(new All("125", "Ɔha aduonu num","","", "", ""));

        allArrayList.add(new All("130", "Ɔha aduasa","","", "", ""));
        allArrayList.add(new All("140", "Ɔha aduanan","","", "", ""));
        allArrayList.add(new All("150", "Ɔha aduonum","","", "", ""));
        allArrayList.add(new All("160", "Ɔha aduosia","","", "", ""));
        allArrayList.add(new All("170", "Ɔha aduoson","","", "", ""));
        allArrayList.add(new All("180", "Ɔha aduowɔtwe","","", "", ""));
        allArrayList.add(new All("190", "Ɔha aduokron","","", "", ""));
        allArrayList.add(new All("200", "Ahanu","","", "", ""));


        allArrayList.add(new All("201", "Ahanu ne baako","","", "", ""));
        allArrayList.add(new All("202", "Ahanu ne mmienu","","", "", ""));
        allArrayList.add(new All("203", "Ahanu ne mmiɛnsa","","", "", ""));
        allArrayList.add(new All("204", "Ahanu ne nan","","", "", ""));
        allArrayList.add(new All("205", "Ahanu ne num","","", "", ""));
        allArrayList.add(new All("206", "Ahanu ne nsia","","", "", ""));
        allArrayList.add(new All("207", "Ahanu ne nson","","", "", ""));
        allArrayList.add(new All("208", "Ahanu ne nwɔtwe","","", "", ""));
        allArrayList.add(new All("209", "Ahanu ne nkron","","", "", ""));
        allArrayList.add(new All("210", "Ahanu ne du","","", "", ""));
        allArrayList.add(new All("211", "Ahanu ne dubaako","","", "", ""));

        allArrayList.add(new All("220", "Ahanu aduonu","","", "", ""));
        allArrayList.add(new All("221", "Ahanu aduonu baako","","", "", ""));
        allArrayList.add(new All("230", "Ahanu aduasa","","", "", ""));
        allArrayList.add(new All("240", "Ahanu aduanan","","", "", ""));
        allArrayList.add(new All("250", "Ahanu aduonum","","", "", ""));

        allArrayList.add(new All("300", "Ahasa","","", "", ""));
        allArrayList.add(new All("400", "Ahanan","","", "", ""));
        allArrayList.add(new All("500", "Ahanum","","", "", ""));
        allArrayList.add(new All("600", "Ahansia","","", "", ""));
        allArrayList.add(new All("700", "Ahanson","","", "", ""));
        allArrayList.add(new All("800", "Ahanwɔtwe","","", "", ""));
        allArrayList.add(new All("900", "Ahankron","","", "", ""));
        allArrayList.add(new All("1000", "Apem","","", "", ""));


        allArrayList.add(new All("1001", "Apem ne baako","","", "", ""));
        allArrayList.add(new All("1002", "Apem ne mmienu","","", "", ""));
        allArrayList.add(new All("1003", "Apem ne mmiɛnsa","","", "", ""));
        allArrayList.add(new All("1004", "Apem ne nan","","", "", ""));
        allArrayList.add(new All("1005", "Apem ne num","","", "", ""));

        allArrayList.add(new All("1010", "Apem ne du","","", "", ""));
        allArrayList.add(new All("1011", "Apem ne dubaako","","", "", ""));
        allArrayList.add(new All("1012", "Apem ne dummienu","","", "", ""));
        allArrayList.add(new All("1013", "Apem ne dummiɛnsa","","", "", ""));
        allArrayList.add(new All("1014", "Apem ne dunan","","", "", ""));
        allArrayList.add(new All("1015", "Apem ne dunum","","", "", ""));

        allArrayList.add(new All("1020", "Apem aduonu","","", "", ""));
        allArrayList.add(new All("1021", "Apem aduonu baako","","", "", ""));
        allArrayList.add(new All("1022", "Apem aduonu mmienu","","", "", ""));
        allArrayList.add(new All("1023", "Apem aduonu mmiɛnsa","","", "", ""));
        allArrayList.add(new All("1024", "Apem aduonu nan","","", "", ""));
        allArrayList.add(new All("1025", "Apem aduonu num","","", "", ""));

        allArrayList.add(new All("1100", "Apem ɔha","","", "", ""));
        allArrayList.add(new All("1101", "Apem ɔha ne baako","","", "", ""));
        allArrayList.add(new All("1102", "Apem ɔha ne mmienu","","", "", ""));
        allArrayList.add(new All("1103", "Apem ɔha ne mmiɛnsa","","", "", ""));
        allArrayList.add(new All("1104", "Apem ɔha ne nan","","", "", ""));
        allArrayList.add(new All("1105", "Apem ɔha ne num","","", "", ""));

        allArrayList.add(new All("1200", "Apem ahanu","","", "", ""));
        allArrayList.add(new All("1201", "Apem ahanu ne baako","","", "", ""));
        allArrayList.add(new All("1202", "Apem ahanu ne mmienu","","", "", ""));
        allArrayList.add(new All("1203", "Apem ahanu ne mmiɛnsa","","", "", ""));
        allArrayList.add(new All("1204", "Apem ahanu ne nan","","", "", ""));
        allArrayList.add(new All("1205", "Apem ahanu ne num","","", "", ""));

        allArrayList.add(new All("1300", "Apem ahasa","","", "", ""));
        allArrayList.add(new All("1400", "Apem ahanan","","", "", ""));
        allArrayList.add(new All("1500", "Apem ahanum","","", "", ""));
        allArrayList.add(new All("1600", "Apem ahansia","","", "", ""));
        allArrayList.add(new All("1700", "Apem ahanson","","", "", ""));
        allArrayList.add(new All("1800", "Apem ahanwɔtwe","","", "", ""));
        allArrayList.add(new All("1900", "Apem ahankron","","", "", ""));
        allArrayList.add(new All("2000", "Mpem mmienu","","", "", ""));

        allArrayList.add(new All("2001", "Mpem mmienu ne baako","","", "", ""));
        allArrayList.add(new All("2010", "Mpem mmienu ne du","","", "", ""));
        allArrayList.add(new All("2100", "Mpem mmienu ne ɔha","","", "", ""));
        allArrayList.add(new All("2101", "Mpem mmienu ɔha ne baako","","", "", ""));
        allArrayList.add(new All("2201", "Mpem mmienu ahanu ne baako","","", "", ""));
        allArrayList.add(new All("2301", "Mpem mmienu ahasa ne baako","","", "", ""));

        allArrayList.add(new All("3000", "Mpem mmiɛnsa","","", "", ""));
        allArrayList.add(new All("4000", "Mpem nan","","", "", ""));
        allArrayList.add(new All("5000", "Mpem num","","", "", ""));
        allArrayList.add(new All("6000", "Mpem nsia","","", "", ""));
        allArrayList.add(new All("7000", "Mpem nson","","", "", ""));
        allArrayList.add(new All("8000", "Mpem nwɔtwe","","", "", ""));
        allArrayList.add(new All("9000", "Mpem nkron","","", "", ""));
        allArrayList.add(new All("10000", "Mpem du","","", "", ""));

        allArrayList.add(new All("10001", "Mpem du ne baako","","", "", ""));
        allArrayList.add(new All("10100", "Mpem du ne ɔha","","", "", ""));
        allArrayList.add(new All("10101", "Mpem du, ɔha ne baako","","", "", ""));
        allArrayList.add(new All("10200", "Mpem du ne ahanu","","", "", ""));
        allArrayList.add(new All("10201", "Mpem du, ahanu ne baako","","", "", ""));
        allArrayList.add(new All("10300", "Mpem du ne ahasa","","", "", ""));

        allArrayList.add(new All("11000", "Mpem dubaako","","", "", ""));
        allArrayList.add(new All("11001", "Mpem dubaako ne baako","","", "", ""));
        allArrayList.add(new All("11011", "Mpem dubaako ne dubaako","","", "", ""));
        allArrayList.add(new All("11121", "Mpem dubaako, ɔha ne aduonu baako","","", "", ""));

        allArrayList.add(new All("12000", "Mpem dummienu","","", "", ""));
        allArrayList.add(new All("13000", "Mpem dummiɛnsa","","", "", ""));
        allArrayList.add(new All("14000", "Mpem dunan","","", "", ""));
        allArrayList.add(new All("15000", "Mpem dunum","","", "", ""));
        allArrayList.add(new All("16000", "Mpem dunsia","","", "", ""));
        allArrayList.add(new All("17000", "Mpem dunson","","", "", ""));
        allArrayList.add(new All("18000", "Mpem dunwɔtwe","","", "", ""));
        allArrayList.add(new All("19000", "Mpem dunkron","","", "", ""));
        allArrayList.add(new All("20000", "Mpem aduonu","","", "", ""));

        allArrayList.add(new All("30000", "Mpem aduasa","","", "", ""));
        allArrayList.add(new All("40000", "Mpem aduanan","","", "", ""));
        allArrayList.add(new All("50000", "Mpem aduonum","","", "", ""));
        allArrayList.add(new All("60000", "Mpem aduosia","","", "", ""));
        allArrayList.add(new All("70000", "Mpem aduoson","","", "", ""));
        allArrayList.add(new All("80000", "Mpem aduowɔtwe","","", "", ""));
        allArrayList.add(new All("90000", "Mpem aduokron","","", "", ""));
        allArrayList.add(new All("100 000", "Mpem ɔha","","", "", ""));


        allArrayList.add(new All("1,000,000", "Ɔpepem baako","","", "", ""));
        allArrayList.add(new All("2 000 000", "Ɔpepem mmienu","","", "", ""));
        allArrayList.add(new All("10 000 000", "Ɔpepem du","","", "", ""));

        allArrayList.add(new All("100 000 000", "Ɔpepem ɔha","","", "", ""));

        allArrayList.add(new All("1 000 000 000", "Ɔpepepem baako","","", "", ""));
*/

///////////////
        AllAdapter allAdapter= new AllAdapter(this, allArrayList);
        allListView.setAdapter(allAdapter);

    }
}

