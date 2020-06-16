package com.learnakantwi.twiguides;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.Random;

public class QuizTimedAllFinishPage extends AppCompatActivity {


    public InterstitialAd mInterstitialAd;
        TextView tvLastScore;
        String playerName;
        String coming;
        Intent intent;
        Group group2;
        EditText etPlayerName;
        Button btGetName;
        int level;
        String category;
        String sharedPref;
        String Ads;
    AdView mAdView;
    Random random;

    int showAdProbability;



    public void AllVocabularyHighScores(String sharedPref){
            SharedPreferences preferences = getSharedPreferences(sharedPref, 0);
            SharedPreferences.Editor editor = preferences.edit();

            SharedPreferences withNames = getSharedPreferences(sharedPref, 0);
            SharedPreferences.Editor editors = withNames.edit();

            int lastScore =  preferences.getInt("lastScore", 0);

            int best1 = preferences.getInt("best1",0);
            int best2 = preferences.getInt("best2",0);
            int best3 = preferences.getInt("best3",0);
            int best4 = preferences.getInt("best4",0);
            int best5 = preferences.getInt("best5",0);

            String Best1 = withNames.getString("Best1",Integer.toString(best1));
            String Best2 = withNames.getString("Best2",Integer.toString(best2));
            String Best3 = withNames.getString("Best3",Integer.toString(best3));
            String Best4 = withNames.getString("Best4",Integer.toString(best4));
            String Best5 = withNames.getString("Best5",Integer.toString(best5));


            if (lastScore>best1){
                int temp = best1;
                int temp3 = best2;
                int temp4=best3;
                int temp5=best4;
                String tempo = Best1;
                String tempo3 = Best2;
                String tempo4 = Best3;
                String tempo5 = Best4;

                //best1= lastScore;
                Best1 = playerName + ": "+ lastScore;
                best2= temp;


                editor.putInt("best2",temp);
                editor.putInt("best1",lastScore);
                editor.putInt("best3",temp3);
                editor.putInt("best4",temp4);
                editor.putInt("best5",temp5);
                editor.apply();
                editors.putString("Best1", Best1);
                editors.putString("Best2", tempo);
                editors.putString("Best3", tempo3);
                editors.putString("Best4", tempo4);
                editors.putString("Best5", tempo5);
                editors.apply();

                Best2 = tempo;
                Best3=tempo3;
                Best4 = tempo4;
                Best5=tempo5;
            }
            else if(lastScore==best1 && lastScore>best2 ){
                int temp3 = best2;
                int temp4=best3;
                int temp5=best4;
                String tempo3 = Best2;
                String tempo4 = Best3;
                String tempo5 = Best4;


                Best2 = playerName + ": "+ lastScore;


               // editor.putInt("best1",best1);
                editor.putInt("best5",temp5);
                editor.putInt("best4",temp4);
                editor.putInt("best3", temp3);
                editor.putInt("best2",lastScore);
                editor.apply();

                editors.putString("Best5", tempo5);
                editors.putString("Best4", tempo4);
                editors.putString("Best3", tempo3);
                editors.putString("Best2", Best2);
                editors.apply();


                Best3 = tempo3;
                Best4 = tempo4;
                Best5 = tempo5;

            }
            else if(lastScore>best2){

                int temp3 = best2;
                int temp4=best3;
                int temp5=best4;

                String tempo3 = Best2;
                String tempo4 = Best3;
                String tempo5 = Best4;

                Best2 = playerName + ": "+ lastScore;

                editor.putInt("best5",temp5);
                editor.putInt("best4",temp4);
                editor.putInt("best3",temp3);
                editor.putInt("best2",lastScore);
                editor.apply();
                editors.putString("Best5", tempo5);
                editors.putString("Best4", tempo4);
                editors.putString("Best3", tempo3);
                editors.putString("Best2", Best2);
                editors.apply();

                Best3=tempo3;
                Best4 = tempo4;
                Best5=tempo5;


            }
            else if(lastScore==best2 && lastScore>best3){
               /* int temp = best1;
                int temp3 = best2;*/
                int temp4=best3;
                int temp5=best4;


                String tempo4 = Best3;
                String tempo5 =Best4;
                // editor.putInt("best1",best1);
                Best3 = playerName + ": "+ lastScore;

                editor.putInt("best5",temp5);
                editor.putInt("best4",temp4);
                editor.putInt("best3",lastScore);
                editor.apply();
               // editor.putInt("best2",best1);

                editors.putString("Best5", tempo5);
                editors.putString("Best4", tempo4);
                editors.putString("Best3", Best3);
                editors.apply();
               // editors.putString("Best2", Best1);

                Best4 = tempo4;
                Best5 = tempo5;


            }
            else if(lastScore>best3){

                int temp4 = best3;
                int temp5 =best4;


                String tempo4 = Best3;
                String tempo5 = Best4;

                Best3= playerName + ": "+ lastScore;

                editor.putInt("best4",temp4);
                editor.putInt("best5",temp5);
                editor.putInt("best3",lastScore);
                editor.apply();
                editors.putString("Best5", tempo5);
                editors.putString("Best4", tempo4);
                editors.putString("Best3", Best3);
                editors.apply();

                Best4 = tempo4;
                Best5= tempo5;



            }
            else if(lastScore==best3 && lastScore>best4){
                int temp5 = best4;
                String tempo5 = Best4;

                Best4 = playerName + ": "+ lastScore;

                // editor.putInt("best1",best1);
                editor.putInt("best5",temp5);
                editor.putInt("best4",lastScore);
                editor.apply();

                editors.putString("Best5", tempo5);
                editors.putString("Best4", Best4);
                editors.apply();

                Best5 = tempo5;


            }
            else if(lastScore>best4){


                int temp5=best4;

                String tempo5 = Best4;

                Best4= playerName + ": "+ lastScore;

                editor.putInt("best5",temp5);
                editor.putInt("best4",lastScore);
                editor.apply();

                editors.putString("Best5", tempo5);
                editors.putString("Best4", Best4);
                editors.apply();

                Best5=tempo5;
            }
            else if(lastScore==best4 && lastScore>best5){

               // String tempo5 = Best4;

                // editor.putInt("best1",best1);
                Best5 = playerName + ": "+ lastScore;

                editor.putInt("best5",lastScore);
                editor.apply();
                // editor.putInt("best2",best1);

                editors.putString("Best5", Best5);
                editors.apply();

               // Best5 = tempo5;

            }
            else if(lastScore>best5){
                best5= lastScore;
                Best5 = playerName + ": "+ best5;
                editor.putInt("best5", best5);
                editor.apply();

                editors.putString("Best5", Best5);
                editors.apply();
            }

            tvLastScore.setText("Category: "+category+"\n\n"+"High Scores \n\n\t" +Best1 + "\n\t"+Best2+  "\n\t"+Best3+ "\n\t"+Best4+ "\n\t" +Best5+"\n\t");
           // tvLastScore.setText("Level: "+level+"\n\n"+"Your Score:" +lastScore+"\n\n\t"+ "High Scores \n\t" +Best1 + "\n\t"+Best2+  "\n\t"+Best3+ "\n\t"+Best4+ "\n\t" +Best5+"\n\t");



           // tvLastScore.setText("TIMED QUIZ "+"\n"+"Your Score:" +lastScore+"\n\n\t"+ "High Scores \n\t" +Best1 + "\n\t"+Best2+  "\n\t"+Best3+ "\n\t"+Best4+ "\n\t" +Best5+"\n\t");
            // tvLastScore.setText(String.valueOf(lastScore));
            //tvLastScore.setText("Your Score:" +lastScore+"\n\t"+ "High Scores \n\t" +best1 + "\n\t"+best2+  "\n\t"+best3+ "\n\t"+best4+ "\n\t" +best5+"\n\t");
        }

        public void newHighScoreList(String category) {

            switch (category){
                case "All Vocabulary":
                    AllVocabularyHighScores("PREFSVOCAB");
                    return;
                case "Children Animals":
                    AllVocabularyHighScores("PREFSCHILDREN");
                    return;
                case "Animals":
                    AllVocabularyHighScores("PREFSANIMALS");
                    return;
                case "Numbers":
                    AllVocabularyHighScores("PREFSNUMBERS");
                    //level3HighScores();
                    return;
                default:
                    AllVocabularyHighScores("PREFSNUMBERS");
                /*case 4:
                    level4HighScores();
                    return;
                case 5:
                    level5HighScores();
                    return;*/
            }


        }


        public void AllVocabularyHighScoresList(String sharedPref){
            SharedPreferences preferences = getSharedPreferences(sharedPref, 0);
            //SharedPreferences.Editor editor = preferences.edit();
            //int lastScore =  preferences.getInt("lastScore", 5);
            int best1 = preferences.getInt("best1",0);
            int best2 = preferences.getInt("best2",0);
            int best3 = preferences.getInt("best3",0);
            int best4 = preferences.getInt("best4",0);
            int best5 = preferences.getInt("best5",0);


            SharedPreferences withNames = getSharedPreferences(sharedPref, 0);

            String Best1 = withNames.getString("Best1",Integer.toString(best1));
            String Best2 = withNames.getString("Best2",Integer.toString(best2));
            String Best3 = withNames.getString("Best3",Integer.toString(best3));
            String Best4 = withNames.getString("Best4",Integer.toString(best4));
            String Best5 = withNames.getString("Best5",Integer.toString(best5));

            tvLastScore.setText("Category: "+ category +"\n\n"+"High Scores \n\n\t" +Best1 + "\n\t"+Best2+  "\n\t"+Best3+ "\n\t"+Best4+ "\n\t" +Best5+"\n\t");
            //tvLastScore.setText("Level: "+level+"\n\n"+"High Scores \n\n\t" +Best1 + "\n\t"+Best2+  "\n\t"+Best3+ "\n\t"+Best4+ "\n\t" +Best5+"\n\t");
        }


        public void HighScoreList(String category){

            switch (category){
                case "All Vocabulary":
                    AllVocabularyHighScoresList("PREFSVOCAB");
                    return;
                case "Children Animals":
                    AllVocabularyHighScoresList("PREFSCHILDREN");
                    return;
                case "Animals":
                    AllVocabularyHighScoresList("PREFSANIMALS");
                    return;
                case "Numbers":
                    AllVocabularyHighScoresList("PREFSNUMBERS");
                    //level3HighScores();
                    return;
                default:  AllVocabularyHighScoresList("PREFSVOCAB");
                /*case 4:
                    level4HighScores();
                    return;
                case 5:
                    level5HighScores();
                    return;*/
            }



            // tvLastScore.setText(String.valueOf(lastScore));
            // tvLastScore.setText("High Scores \n\t" +stringBest1 + "\n\t"+best2+  "\n\t"+best3+ "\n\t"+best4+ "\n\t" +best5+"\n\t");
        }

    public void advert1() {


        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-7384642419407303/9880404420");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        //ca-app-pub-7384642419407303/9880404420
        //ca-app-pub-3940256099942544/1033173712 test
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_timed_all_finish_page);


        Intent intent = getIntent();
        Ads = intent.getStringExtra("Ads");

        if (!Ads.equals(null) && Ads.equals("Ads")){
         //   Toast.makeText(this, "Displayed!!!", Toast.LENGTH_SHORT).show();
            mInterstitialAd = new InterstitialAd(this);
            mInterstitialAd.setAdUnitId("ca-app-pub-7384642419407303/9880404420");
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
            //ca-app-pub-7384642419407303/9880404420
            //ca-app-pub-3940256099942544/1033173712 test
        }

            tvLastScore = findViewById(R.id.tvHighScores);
            tvLastScore.setVisibility(View.INVISIBLE);
            etPlayerName = findViewById(R.id.editText);
            group2 = findViewById(R.id.group2);
            btGetName = findViewById(R.id.btGetName);

            intent = getIntent();
            coming = intent.getStringExtra("from");
            category = intent.getStringExtra("category");
               // intent.putExtra("category","All Vocabulary");



            //SharedPreferences preferences = getSharedPreferences("PREFS", 0);
           // int lastScore =  preferences.getInt("lastScore", 5);

            if (coming!=null && coming.equals("yes")){
                group2.setVisibility(View.VISIBLE);
                level = intent.getIntExtra("level",1);
                // Toast.makeText(this, "Hi"+ level, Toast.LENGTH_SHORT).show();
            }
            else{
                group2.setVisibility(View.GONE);
                //level=1;
                //level = intent.getIntExtra("level",1);
                tvLastScore.setVisibility(View.VISIBLE);
                HighScoreList(category);
            }

            btGetName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    playerName = etPlayerName.getText().toString();
                    // Toast.makeText(AdditionFinishPage.this, playerName, Toast.LENGTH_SHORT).show();
                    group2.setVisibility(View.GONE);
                    tvLastScore.setVisibility(View.VISIBLE);

                    if (coming!=null && coming.equals("yes")){
                        newHighScoreList(category);
                    }
                    else{
                        HighScoreList(category);
                    }
                }
            });

        }

    @Override
    protected void onDestroy() {

        if (!Ads.equals(null) && Ads.equals("Ads")){
            advert1();
        }
        super.onDestroy();
    }

        /*    @Override
        public void onBackPressed() {

            if (coming!=null && coming.equals("yes")){
                Intent intent = new Intent(getApplicationContext(), AdditionCountdownMain.class);
                startActivity(intent);
            }

            super.onBackPressed();
        }*/
    }

