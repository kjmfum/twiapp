package com.learnakantwi.twiguides;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import static com.learnakantwi.twiguides.AnimalsActivity.animalsArrayList;
import static com.learnakantwi.twiguides.SubChildrenAnimals.childrenAnimalsArrayList;

public class QuizTimedChildrenAnimals extends AppCompatActivity {

    ArrayList<Integer> answersList;
    ArrayList<String> answers;
    ArrayList<Integer> answersInt;

    TextView questionText;
    TextView gradeText;
    TextView tvCountdown;
    TextView correctWrong;
    TextView scoreText;


    Random random;

    int randomChoiceQuestion;
    int correctAnswerPosition;
    int answerLocation;
    int chosenSizeRand;
    int best5;
    int twi1Int;
    int score;
    int counter;
    int seconds;
    int totalQuestions = 30;
    int Sub;

    double scorePercent = ((score / totalQuestions) * 100);

    CountDownTimer countDownTimer;

    ImageView button1;
    ImageView button2;
    ImageView button3;
    ImageView button4;

    Button button5;
    Button btReset;
    Button btHallOfFame;

    String twi1;
    String english1;
    String Ads;

    MediaPlayer playFromDevice;


    public void generateQuestionAnimals(){

        random = new Random();
        answers = new ArrayList<>();
        answersInt = new ArrayList<>();
        answerLocation = random.nextInt(4);

        randomChoiceQuestion = random.nextInt(childrenAnimalsArrayList.size()-1);

        // randomChoiceQuestion = random.nextInt(checkArrayList.size()-1);

        ////////////
        if(answersList.size()< childrenAnimalsArrayList.size()-1) {
            while (answersList.contains(randomChoiceQuestion)) {
                // randomChoiceQuestion = random.nextInt(numbersArrayList.size()-1);
                randomChoiceQuestion = random.nextInt(childrenAnimalsArrayList.size() - 1);
                //Toast.makeText(this, Arrays.asList(answers).toString() + " Good", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            answersList.clear();
        }
        answersList.add(randomChoiceQuestion);

        ///////////////
        //Toast.makeText(this, "size: " + checkArrayList.size(), Toast.LENGTH_SHORT).show();

        //english1 = animalsArrayList.get(randomChoiceQuestion).getEnglishAnimals();
        english1 = childrenAnimalsArrayList.get(randomChoiceQuestion).getEnglishAnimals();

        //english1 = checkArrayList.get(randomChoiceQuestion).toString();

        //questionText.setText(english1);

       // twi1 = animalsArrayList.get(randomChoiceQuestion).getTwiAnimals();
        twi1Int = childrenAnimalsArrayList.get(randomChoiceQuestion).drawableID;
        twi1 = childrenAnimalsArrayList.get(randomChoiceQuestion).getTwiAnimals();

        questionText.setText(twi1);



        int chosenSize = (childrenAnimalsArrayList.size() - 1);
        for (int i = 0; i < 4;i++ ){
            chosenSizeRand= random.nextInt(chosenSize);

            if (i== answerLocation){
                answers.add(twi1);
                answersInt.add(twi1Int);
            }
            else {
                while (childrenAnimalsArrayList.get(chosenSizeRand).getTwiAnimals().equals(twi1) || animalsArrayList.get(chosenSizeRand).getEnglishAnimals().contains("(")){
                    chosenSizeRand = random.nextInt(chosenSize);}
                answers.add(childrenAnimalsArrayList.get(chosenSizeRand).getTwiAnimals());
                answersInt.add(childrenAnimalsArrayList.get(chosenSizeRand).drawableID);
               // answers.add(twi1);

            }

        }




        //childrenAnimals.put("wowa", image1);
        button1.setImageResource(answersInt.get(0));
        button2.setImageResource(answersInt.get(1));
        button3.setImageResource(answersInt.get(2));
        button4.setImageResource(answersInt.get(3));

        /*button1.setText(answers.get(0));
        button2.setText(answers.get(1));
        button3.setText(answers.get(2));
        button4.setText(answers.get(3));*/
    }

    public void quizClickSound(View view){


        String tag = view.getTag().toString();
        int positionIndex = Integer.parseInt(tag);

        correctWrong.setText(getString(R.string.correctanswers));
        scoreText.setText(String.valueOf(score));
        counter++;
        String counterSet = counter +" / " + totalQuestions;



        //ImageView blabla = view.findViewById(idview);

       // Toast.makeText(this, view.getTag().toString(), Toast.LENGTH_SHORT).show(); //blabla.getTag();
        String a = answers.get(positionIndex);



        String b = PlayFromFirebase.viewTextConvert(a);

       // Toast.makeText(this, b + " "+  a, Toast.LENGTH_SHORT).show();

        playFromFileOrDownload(b,a);

    }

    public void playFromFileOrDownload(final String filename, final String appearText) {

        if (appearText.equals(twi1)) {
            score++;
            scoreText.setText(String.valueOf(score));
            handleCorrectAnswer("correct");
        }

        else {
            handleCorrectAnswer("wrong");
        }
    }

    public void handleCorrectAnswer(String correct){

        if (seconds>0) {
            if (correct.equals("correct")) {
                ExcellentSound("correctsound");
                questionText.setTextColor(Color.GREEN);
                gradeText.setText("CORRECT");
                gradeText.setTextColor(Color.GREEN);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        generateQuestionAnimals();
                        //correctWrong.setText("");
                        questionText.setTextColor(Color.GRAY);
                        correctWrong.setTextColor(Color.GRAY);
                        gradeText.setTextColor(Color.GRAY);
                        gradeText.setText("");

                    }
                }, 300);
            } else {
                questionText.setTextColor(Color.RED);
                gradeText.setText("WRONG");
                gradeText.setTextColor(Color.RED);
                ExcellentSound("wrongsound");
                //Toast.makeText(QuizTimedAll.this, "WRONG", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        generateQuestionAnimals();
                        //correctWrong.setText("");
                        questionText.setTextColor(Color.GRAY);
                        correctWrong.setTextColor(Color.GRAY);
                        gradeText.setTextColor(Color.GRAY);
                        gradeText.setText("");
                    }
                }, 300);
            }
        }

    }

    public void ExcellentSound(String a){
        File myFile = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/" + a + ".m4a");
        if (myFile.exists()) {

            try {
                if (playFromDevice != null) {
                    playFromDevice.stop();
                    playFromDevice.reset();
                    playFromDevice.release();
                }
                playFromDevice = new MediaPlayer();

                playFromDevice.setDataSource(myFile.toString());
                playFromDevice.prepareAsync();
                playFromDevice.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.start();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void hideStartButton(View v) {
        button5.setVisibility(View.INVISIBLE);
        btHallOfFame.setVisibility(View.INVISIBLE);
        btReset.setVisibility(View.VISIBLE);
        scoreText.setText("");
        //questionText.setText("");
        // counterText.setText("");
        resetQuiz();
        generateQuestionAnimals();



        ///countdown Timer code
         countDownTimer = new CountDownTimer(1001*60*2,1000) {
       // countDownTimer = new CountDownTimer(1001 * 5, 1000) {
            @Override
            public void onTick(long l) {


                //tvCountdown.setText(5+ " : "+ 00);
                int minutes = ((int) l / 60000);
                //seconds = (int)(l- minutes*60000)/1000;
                seconds = (int) (l / 1000) - minutes * 60;
                String secondsString = String.valueOf(seconds);
                if (secondsString.length() > 1) {
                    tvCountdown.setText("0" + minutes + " : " + seconds);
                } else {
                    tvCountdown.setText("0" + minutes + " : " + "0" + seconds);
                }

                if (minutes == 0 && seconds <= 30) {
                    tvCountdown.setTextColor(Color.RED);
                }
                if (minutes == 0 && seconds == 1) {
                    tvCountdown.setText("00 : 00");
                }
            }

            @Override
            public void onFinish() {
                btReset.setVisibility(View.INVISIBLE);

                SharedPreferences preferencesChildren = getSharedPreferences("PREFSCHILDREN", MODE_PRIVATE);
                SharedPreferences.Editor editorChildren = preferencesChildren.edit();

                editorChildren.putInt("lastScore", score);
                editorChildren.apply();
                best5 = preferencesChildren.getInt("best5", 0);
                goToFinishPage();


            }
        };
        countDownTimer.start();
    }

    public void resetQuiz(){

        answersList.clear();
        tvCountdown.setTextColor(Color.BLACK);
        //countDownTimer.start();
        counter =0;
        score=0;
        correctWrong.setText("");
        scoreText.setText("");
//            counterText.setText("");
        gradeText.setText("");
        questionText.setText("");
    }


    public void goToFinishPage(){
        //Intent intent = new Intent(getApplicationContext(), QuizTimedAllFinishPage.class);
        //startActivity(intent);
        Intent intent = new Intent(getApplicationContext(), QuizTimedAllFinishPage.class);
        if (score > best5){
            StringBuilder sb = new StringBuilder();
            sb.append("YOU HAD ").append(score).append(" CORRECT");
            questionText.setText(sb);
            button5.setVisibility(View.VISIBLE);
            btHallOfFame.setVisibility(View.VISIBLE);
            button5.setText(getString(R.string.playagain));
            intent.putExtra("from","yes");
            intent.putExtra("category","Children Animals");
            intent.putExtra("Ads", Ads);
            startActivity(intent);
        }else{
            button5.setVisibility(View.VISIBLE);
            btHallOfFame.setVisibility(View.VISIBLE);
            button5.setText(getString(R.string.playagain));

            double d1 = score;
            double d2 = totalQuestions;
            double scorePercent = ((d1 / d2) * 100);
            scorePercent = Math.round(scorePercent * 10.0) / 10.0;

            StringBuilder sb = new StringBuilder();
            sb.append("YOU HAD ").append(score).append(" CORRECT");
            questionText.setText(sb);
            gradeText.setText("You Couldn't make the top 5");
        }


    }

    public void goToHallOfFame(){
        Intent intent = new Intent(getApplicationContext(), QuizTimedAllFinishPage.class);
        intent.putExtra("from","no");
        intent.putExtra("category","Children Animals");
        intent.putExtra("Ads",Ads);
        startActivity(intent);
    }

    public void resetButton(){

        button5.setVisibility(View.VISIBLE);
        btHallOfFame.setVisibility(View.VISIBLE);
        btReset.setVisibility(View.INVISIBLE);
        countDownTimer.cancel();
        tvCountdown.setText("02 : 00");

        resetQuiz();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_timed_children_animals);

        SharedPreferences subscribe = getSharedPreferences("AdsDecision",MODE_PRIVATE);
        Sub = subscribe.getInt("Sub",0);

        if (Sub==0){
            Ads="Ads";
        }
        else{
            Ads = "No";
        }

        answersList = new ArrayList<>();
        answersInt= new ArrayList<>();


        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);



        button5 = findViewById(R.id.playAgain);
        btHallOfFame = findViewById(R.id.btHallOfFame);
        btHallOfFame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToHallOfFame();
            }
        });

        questionText = findViewById(R.id.QuestionText);
        correctWrong = findViewById(R.id.CorrectWrongText);
        scoreText = findViewById(R.id.Score);
        // counterText=findViewById(R.id.counterText);
        gradeText = findViewById(R.id.grade);

        questionText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateQuestionAnimals();
            }
        });

        countDownTimer = new CountDownTimer(1000 * 60 * 1, 1000) {
            @Override
            public void onTick(long l) {
            }

            @Override
            public void onFinish() {

            }
        };

        btReset = findViewById(R.id.btReset);
        btReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetButton();
            }
        });

        tvCountdown = findViewById(R.id.tvCountDown);

        button5.setText("START QUIZ");
        button5.setVisibility(View.VISIBLE);
        btHallOfFame.setVisibility(View.VISIBLE);
        btReset.setVisibility(View.INVISIBLE);
        resetQuiz();
       // generateQuestionAnimals();
    }
}
