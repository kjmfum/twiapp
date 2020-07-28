package com.learnakantwi.twiguides;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class SubConversationHomeActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    String currentUser;
    TextView tvAkwaaba;
    ListView lvsubconversation;
   // ArrayList <HomeButton> subConversationHomeArrayList;

    ArrayList <String> subConversationHomeArrayList;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //MenuInflater menuInflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.main_menu_simple, menu);

        final MenuItem item = menu.findItem(R.id.menusearch);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(SubConversationHomeActivity.this, query, Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                final ArrayList<String> results = new ArrayList<>();
                for (String x: subConversationHomeArrayList ){

                    if(x.toLowerCase().contains(newText.toLowerCase()) ){
                        results.add(x);
                    }

                    ((ReadingMainAdapter)lvsubconversation.getAdapter()).update(results);
                }

                lvsubconversation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // vowel = readingAlphabetArray.get(position);

                       String  me1 = results.get(position);

                        switch (me1){
                            case "Introducing yourself":
                                goToConversationIntroduction();
                                return;
                            case "Welcoming others":
                                goToQuiz();
                        }
                        //goToTwoLetters();

                    }
                });

                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()){
            case R.id.main:
                goToMain();
                return  true;
            /*case R.id.quiz1:
                goToQuizFamily();
                return  true;
            case R.id.downloadAudio:
                downloadClick();
                return true;*/
            case R.id.videoCourse:
                //Log.i("Menu Item Selected", "Alphabets");
                goToWeb();
                return  true;
            default:
                return false;
        }
    }

    public void tunOnDailyTwi() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.learnakantwi.twiguides", Context.MODE_PRIVATE);
        String dailyTwiPreference = sharedPreferences.getString("DailyTwiPreference", "Yes");
        sharedPreferences.edit().putString("DailyTwiPreference", "Yes").apply();
        Toast.makeText(this, "Daily Twi Alerts Turned On", Toast.LENGTH_SHORT).show();
    }

    public void goToWeb() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.udemy.com/course/learn-akan-twi/?referralCode=6D321CE6AEE1834CCB0F"));
        startActivity(intent);
    }

    public void shareApp(){
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        //sharingIntent.setAction("http://play.google.com/store/apps/details?id=" + getPackageName());
        sharingIntent.setType("text/plain");
        String shareBody = "http://play.google.com/store/apps/details?id=" + getPackageName();
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Please install this Nice Android Twi App");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    public void rateMe() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + getPackageName())));
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
        }
    }


    public void goToMain(){
        Intent intent = new Intent(getApplicationContext(), SubPHomeMainActivity.class);
        startActivity(intent);
    }

    public void goToQuiz(){
        Intent intent = new Intent(getApplicationContext(), QuizSubConversation.class);
        startActivity(intent);
    }


    public void goToCategory(String category){
        Intent intent;
        switch (category){
            case "directions":
                intent = new Intent(getApplicationContext(), SubConversationDirections.class);
                startActivity(intent);
                return;
            case "welcome":
                intent = new Intent(getApplicationContext(), SubConversationWelcomingOthers.class);
                startActivity(intent);
        }
    }
    public void goToConversationIntroduction(){
        Intent intent = new Intent(getApplicationContext(), SubConversationIntroductionActivity.class);
        startActivity(intent);
    }

    public void goToConversationWelcome(){
        Intent intent = new Intent(getApplicationContext(), SubConversationWelcomingOthers.class);
        startActivity(intent);
    }

    public void goToConversationApologies(){
        Intent intent = new Intent(getApplicationContext(), SubConversationApologiesAndResponses.class);
        startActivity(intent);
    }

    public void goToConversationHospital(){
        Intent intent = new Intent(getApplicationContext(), SubConversationHospital.class);
        startActivity(intent);
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_conversation_home);

        Toast.makeText(this, "AKWAABA", Toast.LENGTH_SHORT).show();

        tvAkwaaba = findViewById(R.id.tvAkwaaba);


        mAuth = FirebaseAuth.getInstance();

        subConversationHomeArrayList = new ArrayList<>();
        lvsubconversation = findViewById(R.id.lvsubconversation);

        subConversationHomeArrayList.add("Introducing yourself");
        subConversationHomeArrayList.add("Welcoming others");
        subConversationHomeArrayList.add("Apologies and Regret");
        subConversationHomeArrayList.add("Asking and Giving Directions");

        subConversationHomeArrayList.add("At the Hospital");
       // homeButtonArrayList.add(new HomeButton("Business", R.drawable.businessimage));

        //HomeAdapter homeAdapter = new HomeAdapter(this,subConversationHomeArrayList);

        ConversationMainAdapter homeAdapter = new ConversationMainAdapter(this,subConversationHomeArrayList);

        lvsubconversation.setAdapter(homeAdapter);

        lvsubconversation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String me1 = subConversationHomeArrayList.get(position);


                switch (me1){
                    case "Introducing yourself":
                        goToConversationIntroduction();
                        return;
                    case "Apologies and Regret":
                        goToConversationApologies();
                        return;
                    case "Welcoming others":
                        goToConversationWelcome();
                        return;
                    case "At the Hospital":
                        goToConversationHospital();
                        return;
                    case "Asking and Giving Directions":
                        goToCategory("directions");
                }
            }
        });





    }

    @Override
    protected void onStart() {
        if (mAuth.getCurrentUser()!=null){
            currentUser = mAuth.getCurrentUser().getEmail();
            tvAkwaaba.setText(currentUser);
        }
        super.onStart();
    }
}


