package com.learnakantwi.twiguides;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;
import java.util.Random;

import static com.learnakantwi.twiguides.AllActivity.allArrayList;
import static com.learnakantwi.twiguides.AnimalsActivity.animalsArrayList;
import static com.learnakantwi.twiguides.Home.homeButtonArrayList;


public class QuizAnimals extends AppCompatActivity {

    TextView correctAnswer;
    TextView correctWrong;
    TextView scoreText;
    TextView counterText;
    TextView gradeText;
    Random random;
    int randomChoiceQuestion;
    int correctAnswerPosition;
    int answerLocation;
    int chosenSizeRand;
    String english1;
    String twi1;
    Button button1;
    Button button2;
    Button button3;
    Button button4;
    Button button5;
    ArrayList<String> answers;
    TextView questionText;
    int chosenSize=(animalsArrayList.size()-1);
    int totalQuestions=30;
    int score;
    int counter;
    double scorePercent= ((score/totalQuestions)*100);
    AdView mAdView;



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //MenuInflater menuInflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.quiz_menu, menu);

        return super.onCreateOptionsMenu(menu);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
           /*case R.id.settings:
                Log.i("Menu Item Selected", "Settings");
                playAll();
                return true;
            case R.id.alphabets:
                Log.i("Menu Item Selected", "Alphabets");
                return  true;*/

            case R.id.quiz11:
                //Log.i("Menu Item Selected", "Alphabets");
                // goToMain();
                //disableSound(this);
                goBack();
                return true;
            case R.id.main:
                goToMain();
                return true;
            case R.id.resetquiz:
                resetQuiz();
                return true;
            case R.id.videoCourse:
                goToWeb();
                return true;
            default:
                return false;
        }
    }

    public void goToWeb() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.udemy.com/course/learn-akan-twi/?couponCode=FDISCOUNT1"));
        startActivity(intent);
    }

    public void goBack(){
        finish();
    }

    public void goToMain(){
        Intent intent = new Intent(getApplicationContext(), Home.class);
        startActivity(intent);
    }

    /*public static void disableSound(Context context){
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0,0);
    }*/






    public void generateQuestion(View v){
        random = new Random();
        answers = new ArrayList<>();
        answerLocation = random.nextInt(4);

        randomChoiceQuestion = random.nextInt(9);
        english1 = allArrayList.get(randomChoiceQuestion).getEnglishmain();

        questionText.setText(english1);

        twi1 = allArrayList.get(randomChoiceQuestion).getTwiMain();



        for (int i = 0; i < 4;i++ ){
            chosenSizeRand= random.nextInt(chosenSize);

            //boolean compare = allArrayList.get(i).getTwiMain().equals(twi1);
            if (i== answerLocation){
                answers.add(twi1);
            }
            else {

                //answers.add(allArrayList.get(chosenSizeRand).getTwiMain());
                while (allArrayList.get(chosenSizeRand).getTwiMain().equals(twi1)
                ) {
                    chosenSizeRand = random.nextInt(chosenSize);
                    // answers.add(allArrayList.get(chosenSize).getTwiMain());
                }

                answers.add(allArrayList.get(chosenSizeRand).getTwiMain());
            }



        }

        button1.setText(answers.get(0));
        button2.setText(answers.get(1));
        button3.setText(answers.get(2));
        button4.setText(answers.get(3));



        Log.i("Got It", english1);



        Log.i("choose", animalsArrayList.get(randomChoiceQuestion).getEnglishAnimals());
        Log.i("choose1", String.valueOf(correctAnswerPosition));
    }

    public void resetQuiz(){
        counter =0;
        score=0;
        correctWrong.setText("");
        scoreText.setText("");
        counterText.setText("");
        gradeText.setText("");

    }

    public void generateQuestion(){


        random = new Random();
        answers = new ArrayList<>();
        answerLocation = random.nextInt(4);

        randomChoiceQuestion = random.nextInt(animalsArrayList.size()-1);
        english1 = animalsArrayList.get(randomChoiceQuestion).getEnglishAnimals();

        questionText.setText(english1);

        twi1 = animalsArrayList.get(randomChoiceQuestion).getTwiAnimals();



        for (int i = 0; i < 4;i++ ){
            chosenSizeRand= random.nextInt(chosenSize);

            if (i== answerLocation){
                answers.add(twi1);
            }
            else {
                while (animalsArrayList.get(chosenSizeRand).getTwiAnimals().equals(twi1) || animalsArrayList.get(chosenSizeRand).getEnglishAnimals().contains("(")){
                    chosenSizeRand = random.nextInt(chosenSize);}
                answers.add(animalsArrayList.get(chosenSizeRand).getTwiAnimals());

           }
            /*else {
              // compare = (animalsArrayList.get(chosenSizeRand).getTwiAnimals().equals(twi1) || animalsArrayList.get(chosenSize).getEnglishAnimals().contains("("));

                //answers.add(allArrayList.get(chosenSizeRand).getTwiMain());
                while (animalsArrayList.get(chosenSizeRand).getTwiAnimals().equals(twi1) ) {
                    chosenSizeRand = random.nextInt(chosenSize);
                    while (animalsArrayList.get(chosenSize).getEnglishAnimals().contains("(")){
                       chosenSizeRand = random.nextInt(chosenSize);
                   }// answers.add(allArrayList.get(chosenSize).getTwiMain());
                }

               // answers.add(animalsArrayList.get(chosenSizeRand).getTwiAnimals());
            }*/

            //answers.add(animalsArrayList.get(chosenSizeRand).getTwiAnimals());
        }

        button1.setText(answers.get(0));
        button2.setText(answers.get(1));
        button3.setText(answers.get(2));
        button4.setText(answers.get(3));

        Log.i("Size", String.valueOf(animalsArrayList.size()));
       // Log.i("Size", String.valueOf(animalsArrayList.m);

    }

    public void quizClickSound(View view){

        //int idview= view.getId();


        int idview= view.getId();

        Button blabla = (Button) view.findViewById(idview);
        String a = (String) blabla.getText();


        if (a.equals(twi1)) {
            Toast.makeText(this, a + " -" +" "+"CORRECT!!!!", Toast.LENGTH_SHORT).show();
            generateQuestion();
            score++;
        }

        correctWrong.setText("CORRECT ANSWERS");
        scoreText.setText(String.valueOf(score));
        counter++;
        String counterSet = String.valueOf(counter) +" / " + String.valueOf(totalQuestions);
        counterText.setText(counterSet );



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

        if (counter == totalQuestions){

            button5.setVisibility(View.VISIBLE);
            button5.setText("PLAY AGAIN");

            double d1 = new Double(score);
            double d2 = new Double(totalQuestions);
            double scorePercent= ((d1/d2)*100);
            scorePercent= Math.round(scorePercent*10.0)/10.0;

           // questionText.setText("FINAL SCORE= " + String.valueOf(scorePercent)+"%");
            questionText.setText("YOU HAD " + Double.toString(scorePercent)+"%");

            if (scorePercent> 90){
                gradeText.setText("Excellent!!!!!");
            }
            else if (scorePercent>40 && scorePercent <90){
                gradeText.setText("Well Done!!");
            }
            else if (scorePercent>20 && scorePercent <40){
                gradeText.setText("Nice Try");
            }
            else {
                gradeText.setText("Fail. You can do better");
            }



        }

    }

    public void hideStartButton(View v){
        button5.setVisibility(View.INVISIBLE);
        scoreText.setText("");
        counterText.setText("");
        resetQuiz();
        generateQuestion();

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_animals);



        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        button5 = findViewById(R.id.playAgain);
        questionText = findViewById(R.id.QuestionText);
        correctWrong = findViewById(R.id.CorrectWrongText);
        scoreText = findViewById(R.id.Score);
        counterText=findViewById(R.id.counterText);
        gradeText = findViewById(R.id.grade);


        button5.setVisibility(View.INVISIBLE);


           resetQuiz();
           generateQuestion();




/*
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });


        mAdView = findViewById(R.id.adView1);

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("1E42299CB1A3F8218629BA7531041D73")  // An example device ID
                .build();

        mAdView.loadAd(adRequest);*/

    }

    /*@Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        setContentView(R.layout.activity_quiz_animals);
    }*/


}
