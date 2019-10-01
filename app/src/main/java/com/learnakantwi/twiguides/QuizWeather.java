package com.learnakantwi.twiguides;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
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
import java.util.ArrayList;
import java.util.Random;

import static com.learnakantwi.twiguides.WeatherActivity.weatherArray;


public class QuizWeather extends AppCompatActivity {

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
    int chosenSize=(weatherArray.size()-1);
    int totalQuestions=30;
    int score;
    int counter;
    double scorePercent= ((score/totalQuestions)*100);
    AdView mAdView;

    StorageReference storageReference;
    MediaPlayer playFromDevice;
    MediaPlayer mp1;


    public void playFromFileOrDownload(final String filename) {
        File myFile = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/" + filename + ".m4a");
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
                //generateQuestion();
                // Toast.makeText(this, "From Device", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {

            final StorageReference musicRef = storageReference.child("/AllTwi/" + filename + ".m4a");
            musicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    String url = uri.toString();
                    playFromFirebase(musicRef);
                    // Toast.makeText(getApplicationContext(), "Got IT", Toast.LENGTH_SHORT).show();
                    downloadFile(getApplicationContext(), filename, ".m4a", url);
                    //generateQuestion();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });

        }
    }

    public void playFromFirebase(StorageReference musicRef) {

        if (Build.VERSION.SDK_INT > 22) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

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
                                    // Toast.makeText(getApplicationContext(), "Yes Yes", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(this, "File was not created", Toast.LENGTH_SHORT).show();
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void downloadFile(final Context context, final String filename, final String fileExtension, final String url) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse(url);
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setVisibleInDownloadsUi(false);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                //   request.setDestinationInExternalPublicDir(Environment.DIRECTORY_MUSIC+File.separator+"LearnTwi1", filename+fileExtension);
                request.setDestinationInExternalFilesDir(getApplicationContext(), Environment.DIRECTORY_MUSIC, filename + fileExtension);
                //request.setDestinationInExternalPublicDir(Environment.DIRECTORY_MUSIC+File.separator+"LearnTwi1", filename+fileExtension);
                downloadManager.enqueue(request);
            }
        };
        Thread myThread = new Thread(runnable);
        myThread.start();


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

        randomChoiceQuestion = random.nextInt(weatherArray.size()-1);
        english1 = weatherArray.get(randomChoiceQuestion).getWeatherEnglish();

        questionText.setText(english1);

        twi1 = weatherArray.get(randomChoiceQuestion).getWeatherTwi();



        for (int i = 0; i < 4;i++ ){
            chosenSizeRand= random.nextInt(chosenSize);

            if (i== answerLocation){
                answers.add(twi1);
            }
            else {
                while (weatherArray.get(chosenSizeRand).getWeatherTwi().equals(twi1) || weatherArray.get(chosenSizeRand).getWeatherEnglish().contains("(")){
                    chosenSizeRand = random.nextInt(chosenSize);}
                answers.add(weatherArray.get(chosenSizeRand).getWeatherTwi());

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
    }

    public void quizClickSound(View view){

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

        playFromFileOrDownload(b);

        if (counter == totalQuestions){

            button5.setVisibility(View.VISIBLE);
            button5.setText("PLAY AGAIN");

            double d1 = new Double(score);
            double d2 = new Double(totalQuestions);
            double scorePercent= ((d1/d2)*100);
            scorePercent= Math.round(scorePercent*10.0)/10.0;

           // questionText.setText("FINAL SCORE= " + String.valueOf(scorePercent)+"%");
            questionText.setText(getString(R.string.resulttext) + Double.toString(scorePercent)+"%");

            if (scorePercent> 90)
                gradeText.setText(getString(R.string.excellent));
            else if (scorePercent>40 && scorePercent <90)
                gradeText.setText(getString(R.string.welldone));
            else if (scorePercent>20 && scorePercent <40) {
                gradeText.setText(getString(R.string.nicetry));
            } else gradeText.setText(getString(R.string.fail));



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

        storageReference = FirebaseStorage.getInstance().getReference();


           resetQuiz();
           generateQuestion();


       /* MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });


        mAdView = findViewById(R.id.adView1);

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("1E42299CB1A3F8218629BA7531041D73")  // An example device ID
                .build();

        mAdView.loadAd(adRequest); */

    }

}
