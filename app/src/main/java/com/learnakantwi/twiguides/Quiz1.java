package com.learnakantwi.twiguides;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import static com.learnakantwi.twiguides.NumbersActivity.numbersArrayList;


public class Quiz1 extends AppCompatActivity {

    TextView correctAnswer;

    TextView correctWrong;
    TextView scoreText;
    TextView counterText;
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
    int totalQuestions=50;
    int score;
    int counter;


    /*public static void disableSound(Context context){
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, 0,0);
    }
*/
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



          /* //else if (i>0) {
                //for (int j = 0; j < 4; j++) {
                    //if (answers.get(j) != null) {

                       {
                        while (answers.get(i).equals(answers.get(j))) {
                            chosenSizeRand = random.nextInt(chosenSize);
                        }
                        answers.add("Me");
                    }
                }
            }*/

          /*
            else if (answers.get(1) == null) {
                answers.add(allArrayList.get(chosenSizeRand).getTwiMain());
            }

                else {
                if (answers.get(i).equals(answers.get(1))) {
                    while (answers.get(i).equals(answers.get(1))) {
                        answers.remove(i);
                        chosenSizeRand = random.nextInt(chosenSize);
                        answers.add(allArrayList.get(chosenSizeRand).getTwiMain());
                    }

                    answers.add(allArrayList.get(chosenSizeRand).getTwiMain());
                }
            }
*/


             /* for (int i = 0; i < 4;i++ ){
                chosenSizeRand= random.nextInt(chosenSize);


                //boolean compare = allArrayList.get(i).getTwiMain().equals(twi1);
                if (i== answerLocation){
                    answers.add(twi1);
                }
                else if (i>0){
                    for (int j=0;j<i;j++){
                        if (answers.get(i).equals(answers.get(j))){
                            chosenSizeRand= random.nextInt(chosenSize);
                        }
                        else {
                            answers.add(allArrayList.get(chosenSizeRand).getTwiMain());
                        }
            */ ///

           /* else if (answers.get(i) == null){
                answers.add(allArrayList.get(chosenSizeRand).getTwiMain());
            }
            else {
                if (answers.get(1).equals(answers.get(0))){
                    chosenSizeRand= random.nextInt(chosenSize);
                }

                if (answers.get(2).equals(answers.get(1) 0)){
                    chosenSizeRand= random.nextInt(chosenSize);
                }

                if (answers.get(3).equals(answers.get(2) 1 0)){
                    chosenSizeRand= random.nextInt(chosenSize);
                }

                if (answers.get(1).equals(answers.get(0))){
                    chosenSizeRand= random.nextInt(chosenSize);
                }
            }*/


           /*
            }*/

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

    }
    public void generateQuestion() {

        random = new Random();
        answers = new ArrayList<>();
        answerLocation = random.nextInt(4);

        randomChoiceQuestion = random.nextInt(allArrayList.size()-1);
        english1 = allArrayList.get(randomChoiceQuestion).getEnglishmain();

        questionText.setText(english1);

        twi1 = allArrayList.get(randomChoiceQuestion).getTwiMain();


        for (int i = 0; i < 4; i++) {
            chosenSizeRand = random.nextInt(chosenSize);

            if (i == answerLocation) {
                answers.add(twi1);
            } else {
                while (allArrayList.get(chosenSizeRand).getTwiMain().equals(twi1) || allArrayList.get(chosenSizeRand).getEnglishmain().contains("(")) {
                    chosenSizeRand = random.nextInt(chosenSize);
                }
                answers.add(numbersArrayList.get(chosenSizeRand).getNumberWord());

            }

        }
        button1.setText(answers.get(0));
        button2.setText(answers.get(1));
        button3.setText(answers.get(2));
        button4.setText(answers.get(3));


    }

    /*public void generateQuestion(){
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



          /* //else if (i>0) {
                //for (int j = 0; j < 4; j++) {
                    //if (answers.get(j) != null) {

                       {
                        while (answers.get(i).equals(answers.get(j))) {
                            chosenSizeRand = random.nextInt(chosenSize);
                        }
                        answers.add("Me");
                    }
                }
            }*/

          /*
            else if (answers.get(1) == null) {
                answers.add(allArrayList.get(chosenSizeRand).getTwiMain());
            }

                else {
                if (answers.get(i).equals(answers.get(1))) {
                    while (answers.get(i).equals(answers.get(1))) {
                        answers.remove(i);
                        chosenSizeRand = random.nextInt(chosenSize);
                        answers.add(allArrayList.get(chosenSizeRand).getTwiMain());
                    }

                    answers.add(allArrayList.get(chosenSizeRand).getTwiMain());
                }
            }
*/


             /* for (int i = 0; i < 4;i++ ){
                chosenSizeRand= random.nextInt(chosenSize);


                //boolean compare = allArrayList.get(i).getTwiMain().equals(twi1);
                if (i== answerLocation){
                    answers.add(twi1);
                }
                else if (i>0){
                    for (int j=0;j<i;j++){
                        if (answers.get(i).equals(answers.get(j))){
                            chosenSizeRand= random.nextInt(chosenSize);
                        }
                        else {
                            answers.add(allArrayList.get(chosenSizeRand).getTwiMain());
                        }
            */ ///

           /* else if (answers.get(i) == null){
                answers.add(allArrayList.get(chosenSizeRand).getTwiMain());
            }
            else {
                if (answers.get(1).equals(answers.get(0))){
                    chosenSizeRand= random.nextInt(chosenSize);
                }

                if (answers.get(2).equals(answers.get(1) 0)){
                    chosenSizeRand= random.nextInt(chosenSize);
                }

                if (answers.get(3).equals(answers.get(2) 1 0)){
                    chosenSizeRand= random.nextInt(chosenSize);
                }

                if (answers.get(1).equals(answers.get(0))){
                    chosenSizeRand= random.nextInt(chosenSize);
                }
            }*/


           /*
            }

        }

        button1.setText(answers.get(0));
        button2.setText(answers.get(1));
        button3.setText(answers.get(2));
        button4.setText(answers.get(3));



        Log.i("Got It", english1);



        Log.i("choose", animalsArrayList.get(randomChoiceQuestion).getEnglishAnimals());
        Log.i("choose1", String.valueOf(correctAnswerPosition));
    } */

    public void quizClickSound(View view){

        //int idview= view.getId();


        int idview= view.getId();

        Button blabla = (Button) view.findViewById(idview);
        String a = (String) blabla.getText();


        if (a.equals(twi1)) {
            Toast.makeText(this, a + " -" +" "+"CORRECT!!!!", Toast.LENGTH_SHORT).show();
            correctWrong.setText("CORRECT!!!!!");
            generateQuestion();
            score++;
        }
        else {
            //Toast.makeText(this, a, Toast.LENGTH_SHORT).show();
            correctWrong.setText("WRONG! TRY AGAIN");

        }
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

            correctWrong.setText("");
            button5.setVisibility(View.VISIBLE);
            button5.setText("PLAY AGAIN");

            double d1 = new Double(score);
            double d2 = new Double(totalQuestions);
            double scorePercent= ((d1/d2)*100);

            // questionText.setText("FINAL SCORE= " + String.valueOf(scorePercent)+"%");
            questionText.setText(Double.toString(scorePercent)+"%");


        }

    }

    public void hideStartButton (View v){
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
        counterText = findViewById(R.id.counterText);


        button5.setVisibility(View.INVISIBLE);
        resetQuiz();
        generateQuestion();


       // Random randomAnswerPostion =new Random();


        AdView mAdView;
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });


        mAdView = findViewById(R.id.adView1);

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("1E42299CB1A3F8218629BA7531041D73")  // An example device ID
                .build();

        mAdView.loadAd(adRequest);

        /*Random rand = new Random(10);
        int randAnimal = rand.nextInt();*/

        //String myAnimal = animalsArrayList.get(randAnimal).getEnglishAnimals() + " " + animalsArrayList.get(randAnimal).getTwiAnimals();
       Log.i("ItT1", homeButtonArrayList.get(3).getNameofActivity());
       Log.i("chooseAnimal", animalsArrayList.get(6).getEnglishAnimals() + " " + animalsArrayList.get(6).getTwiAnimals());
      // Log.i("ItT1", allArrayList.get(7).getEnglishmain() + " " + allArrayList.get(7).getTwiMain());
       // Log.i("choose1",myAnimal);

    }
}
