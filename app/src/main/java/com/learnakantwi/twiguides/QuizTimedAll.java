package com.learnakantwi.twiguides;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.appodeal.ads.Appodeal;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Random;

import static com.learnakantwi.twiguides.AllActivity.allArrayList;
import static com.learnakantwi.twiguides.NumbersActivity.numbersArrayList;
import static com.learnakantwi.twiguides.AnimalsActivity.animalsArrayList;

public class QuizTimedAll extends AppCompatActivity {


    //ArrayList<Integer> checkArrayList;
    ArrayList<Integer> answersList;
    TextView correctAnswer;
        TextView correctWrong;
        TextView scoreText;
        TextView counterText;
        TextView gradeText;
    TextView tvCountdown;
    TextView questionText;
    Intent intent;
        Random random;
        int randomChoiceQuestion;
        int correctAnswerPosition;
        int answerLocation;
        int chosenSizeRand;
        int best5;
    int Sub;
        String english1;
        String twi1;
        String Ads;


        Button button1;
        Button button2;
        Button button3;
        Button button4;
        Button button5;
        Button btReset;
        Button btHallOfFame;

        ArrayList<String> answers;

        int chosenSize = (allArrayList.size() - 1);
        int totalQuestions = 50;
        int score;
        int counter;
        int seconds;

        double scorePercent = ((score / totalQuestions) * 100);

        StorageReference storageReference;
        MediaPlayer playFromDevice;
        MediaPlayer mp1;
        MediaPlayer playCorrectSound;
        MediaPlayer playWrongSound;

        Toast toast;


        ImageView ivMute;
        ImageView ivUnMute;

        String category;

    CountDownTimer countDownTimer;

        boolean isRunning =false;
        public Runnable runnable = new Runnable() {

            @Override
            public void run() {
                try {
                    URL url = new URL("http://www.google.com");
                    URLConnection connection = url.openConnection();
                    connection.connect();
                    isRunning = true;
                    System.out.println("Internet is now connected");
                } catch (MalformedURLException e) {
                    isRunning =false;
                } catch (IOException e) {
                    isRunning=false;
                }
            }
        };

        private boolean isNetworkAvailable() {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null;
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


        public void playFromFirebase(StorageReference musicRef) {

            if (Build.VERSION.SDK_INT > 22) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }

            if (isNetworkAvailable()) {

                try {
                    final File localFile = File.createTempFile("aduonu", "m4a");

                    if (localFile != null) {
                        musicRef.getFile(localFile)
                                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                                        if (mp1 != null) {
                                            mp1.stop();
                                            mp1.reset();
                                            mp1.release();
                                        }
                                        mp1 = new MediaPlayer();
                                        try {
                                            mp1.setDataSource(getApplicationContext(), Uri.fromFile(localFile));
                                            mp1.prepareAsync();
                                            mp1.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                                @Override
                                                public void onPrepared(MediaPlayer mp) {
                                                    mp.start();
                                                }
                                            });
                                        } catch (IOException ex) {
                                            ex.printStackTrace();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle failed download
                                // ...
                            }
                        });
                    } else {
                        toast.setText("Unable to download now. Please try later");
                        toast.show();
                    }

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            else {
                toast.setText("Please Connect to the Internet");
                toast.show();
            }

        }

        public void handleCorrectAnswer(String correct){
            if (seconds>0){
                if (correct.equals("correct")){
                    ExcellentSound("correctsound");
                    questionText.setTextColor(Color.GREEN);
                    gradeText.setText("CORRECT");
                    gradeText.setTextColor(Color.GREEN);
                    //Toast.makeText(QuizTimedAll.this, , Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            generateQuestionCategory(category);
                            //correctWrong.setText("");
                            questionText.setTextColor(Color.GRAY);
                            correctWrong.setTextColor(Color.GRAY);
                            gradeText.setTextColor(Color.GRAY);
                            gradeText.setText("");

                        }
                    },300);
                }
                else{
                    questionText.setTextColor(Color.RED);
                    gradeText.setText("WRONG");
                    gradeText.setTextColor(Color.RED);
                    ExcellentSound("wrongsound");
                    //playWrongSound = MediaPlayer.create(getApplicationContext(), R.raw)
                /*playWrongSound = MediaPlayer.create(getApplicationContext(),R.raw.wrongsound);
                playWrongSound.start();*/
                    //Toast.makeText(QuizTimedAll.this, "WRONG", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            generateQuestionCategory(category);
                            //correctWrong.setText("");
                            questionText.setTextColor(Color.GRAY);
                            correctWrong.setTextColor(Color.GRAY);
                            gradeText.setTextColor(Color.GRAY);
                            gradeText.setText("");
                        }
                    },300);
                }
            }


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


        public void downloadFile(final Context context, final String filename, final String fileExtension, final String url) {

            if (Build.VERSION.SDK_INT > 22) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }

            if (isNetworkAvailable()) {
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                        Uri uri = Uri.parse(url);
                        DownloadManager.Request request = new DownloadManager.Request(uri);
                        request.setVisibleInDownloadsUi(false);
                        request.setDestinationInExternalFilesDir(getApplicationContext(), Environment.DIRECTORY_MUSIC, filename + fileExtension);
                        downloadManager.enqueue(request);
                    }
                };
                Thread myThread = new Thread(runnable);
                myThread.start();
            }
            else
            {
                toast.setText("No Internet");
                toast.show();

            }
        }


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
                case R.id.quiz11:
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
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.udemy.com/course/learn-akan-twi/?referralCode=6D321CE6AEE1834CCB0F"));
            startActivity(intent);
        }

        public void goBack(){
            finish();
        }

        public void goToMain(){
                if (Ads.equals("Ads")){
                    Intent intent = new Intent(getApplicationContext(), HomeMainActivity.class);
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(getApplicationContext(), SubPHomeMainActivity.class);
                    startActivity(intent);
                }
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
            intent.putExtra("category",category);
            intent.putExtra("Ads",Ads);
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

    public void resetButton(){

        button5.setVisibility(View.VISIBLE);
        btHallOfFame.setVisibility(View.VISIBLE);
        btReset.setVisibility(View.INVISIBLE);
        countDownTimer.cancel();
        tvCountdown.setText("02 : 00");

        resetQuiz();
    }

    public void generateQuestionCategory(String category){
        switch (category){
            case "All Vocabulary":
                generateQuestionVocabulary();
                return;
            case "Numbers" :
                generateQuestionNumbers();
                return;
            case "Animals":
                generateQuestionAnimals();
                //goToFinishPage();
                return;
            default:
                best5 = 0;
        }
    }

        public void generateQuestionVocabulary(){

            random = new Random();
            answers = new ArrayList<>();
            answerLocation = random.nextInt(4);

            randomChoiceQuestion = random.nextInt(allArrayList.size()-1);

            if(answersList.size()< allArrayList.size()-1) {
                while (answersList.contains(randomChoiceQuestion)) {
                    // randomChoiceQuestion = random.nextInt(numbersArrayList.size()-1);
                    randomChoiceQuestion = random.nextInt(allArrayList.size() - 1);
                    //Toast.makeText(this, Arrays.asList(answers).toString() + " Good", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                answersList.clear();
            }
            answersList.add(randomChoiceQuestion);


            english1 = allArrayList.get(randomChoiceQuestion).getEnglishmain();

            questionText.setText(english1);

            twi1 = allArrayList.get(randomChoiceQuestion).getTwiMain();



            for (int i = 0; i < 4;i++ ){
                chosenSizeRand= random.nextInt(chosenSize);

                if (i== answerLocation){
                    answers.add(twi1);
                }
                else {
                    while (allArrayList.get(chosenSizeRand).getTwiMain().equals(twi1) || allArrayList.get(chosenSizeRand).getEnglishmain().contains("(")){
                        chosenSizeRand = random.nextInt(chosenSize);}
                    answers.add(allArrayList.get(chosenSizeRand).getTwiMain());

                }

            }


                button1.setText(answers.get(0));
                button2.setText(answers.get(1));
                button3.setText(answers.get(2));
                button4.setText(answers.get(3));
        }
    public void generateQuestionNumbers(){

        random = new Random();
        answers = new ArrayList<>();
        answerLocation = random.nextInt(4);

        randomChoiceQuestion = random.nextInt(numbersArrayList.size()-1);

       // randomChoiceQuestion = random.nextInt(checkArrayList.size()-1);

        if(answersList.size()< numbersArrayList.size()-1) {
            while (answersList.contains(randomChoiceQuestion)) {
                // randomChoiceQuestion = random.nextInt(numbersArrayList.size()-1);
                randomChoiceQuestion = random.nextInt(numbersArrayList.size() - 1);
                //Toast.makeText(this, Arrays.asList(answers).toString() + " Good", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            answersList.clear();
        }
        answersList.add(randomChoiceQuestion);
        //Toast.makeText(this, "size: " + checkArrayList.size(), Toast.LENGTH_SHORT).show();

        english1 = numbersArrayList.get(randomChoiceQuestion).getFigure();

        //english1 = checkArrayList.get(randomChoiceQuestion).toString();

        questionText.setText(english1);

        twi1 = numbersArrayList.get(randomChoiceQuestion).getNumberWord();



        int chosenSize = (numbersArrayList.size() - 1);
        for (int i = 0; i < 4;i++ ){
            chosenSizeRand= random.nextInt(chosenSize);

            if (i== answerLocation){
                answers.add(twi1);
            }
            else {
                while (numbersArrayList.get(chosenSizeRand).getNumberWord().equals(twi1) || numbersArrayList.get(chosenSizeRand).getFigure().contains("(")){
                    chosenSizeRand = random.nextInt(chosenSize);}
                answers.add(numbersArrayList.get(chosenSizeRand).getNumberWord());

            }

        }

        button1.setText(answers.get(0));
        button2.setText(answers.get(1));
        button3.setText(answers.get(2));
        button4.setText(answers.get(3));
    }

    public void generateQuestionAnimals(){

        random = new Random();
        answers = new ArrayList<>();
        answerLocation = random.nextInt(4);

        randomChoiceQuestion = random.nextInt(animalsArrayList.size()-1);

        // randomChoiceQuestion = random.nextInt(checkArrayList.size()-1);

        if(answersList.size()< animalsArrayList.size()-1) {
            while (answersList.contains(randomChoiceQuestion)) {
                // randomChoiceQuestion = random.nextInt(numbersArrayList.size()-1);
                randomChoiceQuestion = random.nextInt(animalsArrayList.size() - 1);
                //Toast.makeText(this, Arrays.asList(answers).toString() + " Good", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            answersList.clear();
        }
        answersList.add(randomChoiceQuestion);
        //Toast.makeText(this, "size: " + checkArrayList.size(), Toast.LENGTH_SHORT).show();

        english1 = animalsArrayList.get(randomChoiceQuestion).getEnglishAnimals();

        //english1 = checkArrayList.get(randomChoiceQuestion).toString();

        questionText.setText(english1);

        twi1 = animalsArrayList.get(randomChoiceQuestion).getTwiAnimals();



        int chosenSize = (animalsArrayList.size() - 1);
        for (int i = 0; i < 4;i++ ){
            chosenSizeRand= random.nextInt(chosenSize);

            if (i== answerLocation){
                answers.add(twi1);
            }
            else {
                while (animalsArrayList.get(chosenSizeRand).getTwiAnimals().equals(twi1) || animalsArrayList.get(chosenSizeRand).getEnglishAnimals().contains("(")) {
                    chosenSizeRand = random.nextInt(chosenSize);}

                answers.add(animalsArrayList.get(chosenSizeRand).getTwiAnimals());

            }

        }

        button1.setText(answers.get(0));
        button2.setText(answers.get(1));
        button3.setText(answers.get(2));
        button4.setText(answers.get(3));
    }

        public void quizClickSound(View view){


            int idview= view.getId();

            Button blabla = view.findViewById(idview);
            String a = (String) blabla.getText();


            correctWrong.setText(getString(R.string.correctanswers));
            scoreText.setText(String.valueOf(score));
            counter++;
            String counterSet = counter +" / " + totalQuestions;
           // counterText.setText(counterSet );



            String b = a.toLowerCase();

            boolean d = b.contains("ɔ");
            boolean e = b.contains("ɛ");

            if (d || e ){
                b= b.replace("ɔ","x");
                b= b.replace("ɛ","q");
            }


            if (b.contains(" ") || b.contains("/") || b.contains(",") || b.contains("(") || b.contains(")") || b.contains("-") | b.contains("?")| b.contains("...")) {
                b = b.replace(" ", "");
                b = b.replace("/", "");
                b= b.replace(",","");
                b= b.replace("(","");
                b= b.replace(")","");
                b= b.replace("-","");
                b= b.replace("?","");
                b= b.replace("...","");
            }

            playFromFileOrDownload(b,a);

        }

        public void resetShared(){

            SharedPreferences preferences = getSharedPreferences("PREFS", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();

            ////clear

            int best1=0;
            int best2=0;
            int best3=0;
            int best4=0;
            int best5=0;

            SharedPreferences withNames = getSharedPreferences("PREFS", 0);
            SharedPreferences.Editor editors = withNames.edit();

            editor.putInt("best1",best1);
            editor.putInt("best2",best2);
            editor.putInt("best3",best3);
            editor.putInt("best4",best4);
            editor.putInt("best5",best5);
            editor.apply();
            editors.putString("Best1", "A1: "+ best1);
            editors.putString("Best2", "A2: "+ best2);
            editors.putString("Best3", "A3: "+ best3);
            editors.putString("Best4", "A4: "+ best4);
            editors.putString("Best5", "A5: "+ best5);
            editors.apply();

            ///clear end
        }

        public void hideStartButton(View v){
            button5.setVisibility(View.INVISIBLE);
            btHallOfFame.setVisibility(View.INVISIBLE);
            btReset.setVisibility(View.VISIBLE);
            scoreText.setText("");
            //questionText.setText("");
           // counterText.setText("");
            resetQuiz();
            generateQuestionCategory(category);


            ///countdown Timer code
            countDownTimer = new CountDownTimer(1001*60*2,1000) {
           //     countDownTimer = new CountDownTimer(1001*5 ,1000) {
                @Override
                public void onTick(long l) {


                    //tvCountdown.setText(5+ " : "+ 00);
                    int minutes = ((int)l/60000);
                    //seconds = (int)(l- minutes*60000)/1000;
                    seconds = (int)(l/1000) - minutes*60;
                    String secondsString = String.valueOf(seconds);
                    if (secondsString.length()>1){
                        tvCountdown.setText("0"+minutes+ " : "+ seconds);
                    }
                    else {
                        tvCountdown.setText("0"+minutes+ " : "+"0"+ seconds);
                    }

                    if (minutes==0 && seconds<=30){
                        tvCountdown.setTextColor(Color.RED);
                    }
                    if (minutes==0 && seconds==1){
                        tvCountdown.setText("00 : 00");
                    }
                }

                @Override
            public void onFinish() {
                    btReset.setVisibility(View.INVISIBLE);
                SharedPreferences preferences = getSharedPreferences("PREFSVOCAB", MODE_PRIVATE);
                SharedPreferences preferencesNumbers = getSharedPreferences("PREFSNUMBERS", MODE_PRIVATE);
                SharedPreferences preferencesChildren = getSharedPreferences("PREFSCHILDREN", MODE_PRIVATE);
                    SharedPreferences preferencesAnimals = getSharedPreferences("PREFSANIMALS", MODE_PRIVATE);
                SharedPreferences preferences4 = getSharedPreferences("PREFS4", MODE_PRIVATE);
                SharedPreferences preferences5 = getSharedPreferences("PREFS5", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                SharedPreferences.Editor editorNumber = preferencesNumbers.edit();
                SharedPreferences.Editor editorChildren = preferencesChildren.edit();
                SharedPreferences.Editor editorAnimals = preferencesAnimals.edit();


                switch (category){
                    case "All Vocabulary":
                        editor.putInt("lastScore", score);
                        editor.putInt("attempts", counter);
                        editor.apply();
                        best5 = preferences.getInt("best5",0);
                        goToFinishPage();
                        return;
                    case "Numbers" :
                        editorNumber.putInt("lastScore", score);
                        editorNumber.apply();
                        best5 = preferencesNumbers.getInt("best5",0);
                        goToFinishPage();
                        return;
                    case "Children":
                        editorChildren.putInt("lastScore", score);
                        editorChildren.apply();
                        best5 = preferencesChildren.getInt("best5",0);
                        goToFinishPage();
                        return;
                    case "Animals":
                        editorAnimals.putInt("lastScore", score);
                        editorAnimals.apply();
                        best5 = preferencesAnimals.getInt("best5",0);
                        goToFinishPage();
                        return;
                    default:
                        best5 = 0;
                }
            }
        };
        countDownTimer.start();

        }

        public void goToHallOfFame(){
            Intent intent = new Intent(getApplicationContext(), QuizTimedAllFinishPage.class);
            intent.putExtra("from","no");
            intent.putExtra("category",category);
            intent.putExtra("Ads",Ads);
            startActivity(intent);
        }

    public void advert1() {

        if (Appodeal.isLoaded(Appodeal.INTERSTITIAL)) {
            Appodeal.show(this, Appodeal.INTERSTITIAL);
        }

        //  Appodeal.cache(this, Appodeal.INTERSTITIAL);
    }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_quiz_timed_all);

            answersList = new ArrayList<>();
            /*checkArrayList = new ArrayList<>();


            checkArrayList.add(1);
            checkArrayList.add(2);
            checkArrayList.add(3);
            checkArrayList.add(4);
            checkArrayList.add(5);
            checkArrayList.add(6);
            checkArrayList.add(7);
            checkArrayList.add(8);
            checkArrayList.add(9);
            checkArrayList.add(10);*/

            SharedPreferences subscribe = getSharedPreferences("AdsDecision",MODE_PRIVATE);
            Sub = subscribe.getInt("Sub",0);

            intent = getIntent();
            category = intent.getStringExtra("category");// All Vocabulary
            Ads = intent.getStringExtra("Ads");

            //resetShared();


            //if (!Ads.equals(null) && Ads.equals("Ads")){
                if (Sub==0){
                    Ads="Ads";
               // Toast.makeText(this, "Displayed!!", Toast.LENGTH_SHORT).show();
                    Appodeal.cache(this, Appodeal.INTERSTITIAL);
                //ca-app-pub-7384642419407303/9880404420
                //ca-app-pub-3940256099942544/1033173712 test

                    Appodeal.show(this, Appodeal.BANNER_BOTTOM);

              /*  MobileAds.initialize(this, new OnInitializationCompleteListener() {
                    @Override
                    public void onInitializationComplete(InitializationStatus initializationStatus) {
                    }
                });
                mAdView = findViewById(R.id.adView);
                AdRequest adRequest = new AdRequest.Builder().build();
                mAdView.loadAd(adRequest);


                MobileAds.initialize(this, new OnInitializationCompleteListener() {
                    @Override
                    public void onInitializationComplete(InitializationStatus initializationStatus) {
                    }
                });*/
            }else {
                    Ads ="No Ads";
                }



            btHallOfFame = findViewById(R.id.btHallOfFame);
            btHallOfFame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToHallOfFame();
                }
            });

            btReset = findViewById(R.id.btReset);
            btReset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetButton();
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

            tvCountdown = findViewById(R.id.tvCountDown);

            toast= Toast.makeText(getApplicationContext()," ",Toast.LENGTH_SHORT);


            storageReference = FirebaseStorage.getInstance().getReference();
            seconds=2;

            button1 = findViewById(R.id.button1);
            button2 = findViewById(R.id.button2);
            button3 = findViewById(R.id.button3);
            button4 = findViewById(R.id.button4);
            button5 = findViewById(R.id.playAgain);
            questionText = findViewById(R.id.QuestionText);
            correctWrong = findViewById(R.id.CorrectWrongText);
            scoreText = findViewById(R.id.Score);
           // counterText=findViewById(R.id.counterText);
            gradeText = findViewById(R.id.grade);


            button5.setText("START QUIZ");
            button5.setVisibility(View.VISIBLE);
            btHallOfFame.setVisibility(View.VISIBLE);
            btReset.setVisibility(View.INVISIBLE);
            resetQuiz();
           // generateQuestion();
        }



    @Override
    protected void onDestroy() {
        countDownTimer.cancel();

        if (score <= best5 && Sub==0){
           //Toast.makeText(this, "Display!!!", Toast.LENGTH_SHORT).show();
            advert1();
        }

        super.onDestroy();
    }

    @Override
    protected void onResume() {
            resetQuiz();
        super.onResume();
    }

    @Override
    protected void onStop() {
        resetQuiz();
        super.onStop();
    }
}

