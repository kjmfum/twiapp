package com.learnakantwi.twiguides;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.DownloadManager;
import android.app.admin.FreezePeriod;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;
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
import java.security.acl.Group;
import java.util.ArrayList;
import java.util.Random;

import static androidx.annotation.InspectableProperty.ValueType.COLOR;
import static com.learnakantwi.twiguides.SubChildrenAnimals.childrenAnimalsArrayList;

import static com.learnakantwi.twiguides.FoodActivity.foodArrayList;

public class SubChildrenAnimalQuiz extends AppCompatActivity {

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
        int chosenSize=(foodArrayList.size()-1);
        int totalQuestions=20;
        int score;
        int counter;
        double scorePercent= ((score/totalQuestions)*100);

        StorageReference storageReference;
        MediaPlayer playFromDevice;
        MediaPlayer mp1;

        ImageView questionImage;
        TypedArray imgs;


       // Group group2;
        androidx.constraintlayout.widget.Group group2;

    Animation shake;


        Toast toast;
        boolean isRunning =false;

        //toast = Toast.makeText(getApplicationContext(), " " , Toast.LENGTH_SHORT);
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
        public void ExcellentSound() {
            File myFile = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/" + "excellentsound" + ".m4a");
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

    public void handleCorrectAnswer(String answer){

            String answer1 = answer;
            if (answer.equals("correct")){
                correctWrong.setText("CORRECT!!\n AYEKOO!!");
                correctWrong.setTextColor(Color.GREEN);
                correctWrong.setText("CORRECT!!\n AYEKOO!!");
                correctWrong.setTextColor(Color.GREEN);
                questionImage.setBackgroundColor(Color.GREEN);
                questionImage.startAnimation(shake);
                // tvTwi.setTextColor(Color.WHITE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        generateQuestion();
                        questionImage.setBackgroundColor(Color.WHITE);

                        //correctWrong.setText("");
                        correctWrong.setTextColor(Color.GRAY);
                    }
                },1300);
            }
            else{
                correctWrong.setText("WRONG!!\n DABI");
                correctWrong.setTextColor(Color.RED);
                questionImage.setBackgroundColor(Color.RED);
                // tvTwi.setTextColor(Color.WHITE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        questionImage.setBackgroundColor(Color.WHITE);
                        correctWrong.setText("");
                        correctWrong.setTextColor(Color.GRAY);
                    }
                },500);
            }



    }
        public void playFromFileOrDownload(final String filename, final String appearText) {

            if (appearText.equals(twi1)) {
                //toast.setText(appearText + " -" + " " + "CORRECT!!!!");
                //toast.show();
                score++;
                scoreText.setText("Score: "+ score);
            }

            if (counter == totalQuestions) {
                button5.setVisibility(View.VISIBLE);
                button5.setText(getString(R.string.playagain));

                double d1 = score;
                double d2 = totalQuestions;
                double scorePercent = ((d1 / d2) * 100);
                scorePercent = Math.round(scorePercent * 10.0) / 10.0;

                StringBuilder sb = new StringBuilder();
                sb.append("YOU HAD ").append(scorePercent).append("%");
                // questionText.setText("FINAL SCORE= " + String.valueOf(scorePercent)+"%");
                questionText.setText(sb);
                String finalGrade = "Play Again";

                if (scorePercent >= 90) {
                    finalGrade= getString(R.string.excellent);
                   // gradeText.setText(getString(R.string.excellent));
                    ExcellentSound();
                } else if (scorePercent > 40 && scorePercent < 90) {
                    finalGrade = getString(R.string.welldone);
                   // gradeText.setText(getString(R.string.welldone));
                } else if (scorePercent > 20 && scorePercent <= 40) {
                    finalGrade = getString(R.string.nicetry);
                   // gradeText.setText(getString(R.string.nicetry));
                } else if (scorePercent <= 20){
                    finalGrade = getString(R.string.fail);
                   // gradeText.setText(getString(R.string.fail));
                }
                else
                {
                    finalGrade = getString(R.string.keeptrying);
                    //gradeText.setText(getString(R.string.keeptrying));
                }


                button5.setText(finalGrade + "\n\n"+ getString(R.string.playagain));
            } else {

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
                                if (appearText.equals(twi1)) {
                                   // toast.setText(appearText + " -" + " " + "CORRECT!!!!");
                                    //toast.show();
                                    handleCorrectAnswer("correct");
                                } else {
                                    /*toast.setText(appearText);
                                    toast.show();*/
                                    handleCorrectAnswer("wrong");
                                }
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (isNetworkAvailable()) {
                    final StorageReference musicRef = storageReference.child("/AllTwi/" + filename + ".m4a");
                    musicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();
                            playFromFirebase(musicRef);
                            downloadFile(getApplicationContext(), filename, ".m4a", url);
                            if (appearText.equals(twi1)) {
                                toast.setText(appearText + " -" + " " + "CORRECT!!!!");
                                toast.show();
                                generateQuestion();
                            } else {
                                toast.setText(appearText);
                                toast.show();
                            }
                            //Toast.makeText(getApplicationContext(), appearText, Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if (appearText.equals(twi1)) {
                                toast.setText(appearText + " -" + " " + "CORRECT!!!!");
                                toast.show();

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        generateQuestion();
                                    }
                                },2000);


                            } else {
                                toast.setText(appearText);
                                toast.show();
                            }
                        }
                    });
                } else {

                    if (appearText.equals(twi1)) {
                        toast.setText(appearText + " -" + " " + "CORRECT!!!!");
                        toast.show();
                        generateQuestion();
                    } else {
                        toast.setText(appearText);
                        toast.show();
                    }

                }
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
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.udemy.com/course/learn-akan-twi/?referralCode=6D321CE6AEE1834CCB0F"));
            startActivity(intent);
        }

        public void goBack(){
            finish();
        }

        public void goToMain(){
            Intent intent = new Intent(getApplicationContext(), SubPHomeMainActivity.class);
            startActivity(intent);
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

            correctWrong.setText("Aboa bɛn ni?");

            imgs = getResources().obtainTypedArray(R.array.animalImages);
            //Random rand = new Random();
            randomChoiceQuestion = random.nextInt(imgs.length()-1);
          //  int randInt = rand.nextInt(imgs.length());
            int resID = imgs.getResourceId(randomChoiceQuestion,0);
            questionImage.setImageResource(resID);
            String one = imgs.getResources().getResourceName(resID);


           // english1 = foodArrayList.get(randomChoiceQuestion).getEnglishFood();
            english1= childrenAnimalsArrayList.get(randomChoiceQuestion).getEnglishAnimals();

            questionText.setText(english1);
            twi1 = childrenAnimalsArrayList.get(randomChoiceQuestion).getTwiAnimals();


            for (int i = 0; i < 4;i++ ){
                chosenSizeRand= random.nextInt(imgs.length()-1);
                //chosenSizeRand= random.nextInt(16);

                if (i== answerLocation){
                    answers.add(twi1);

              } /*else{
                    answers.add(childrenAnimalsArrayList.get(chosenSizeRand).getTwiAnimals());
                }*/
                else {
                    while (childrenAnimalsArrayList.get(chosenSizeRand).getTwiAnimals().equals(twi1) || childrenAnimalsArrayList.get(chosenSizeRand).getEnglishAnimals().contains("(")){
                        chosenSizeRand = random.nextInt(imgs.length()-1);}
                    answers.add(childrenAnimalsArrayList.get(chosenSizeRand).getTwiAnimals());
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

            scoreText.setText("Score: "+ score);
            counter++;
            String counterSet = counter +" / " + totalQuestions;
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

            playFromFileOrDownload(b,a);

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
        setContentView(R.layout.activity_sub_children_animal_quiz);

        shake = AnimationUtils.loadAnimation(this,R.anim.children_animation);
       /* group2 = findViewById(R.id.group2);

        group2.setVisibility(View.INVISIBLE);*/


            toast = Toast.makeText(getApplicationContext(), " " , Toast.LENGTH_SHORT);

            questionImage = findViewById(R.id.ivQuestionImage);



            questionImage.setImageResource(R.drawable.afofantx);



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


            questionImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    questionImage.startAnimation(shake);
                }
            });
            //resetQuiz();

            generateQuestion();
        }
    }
