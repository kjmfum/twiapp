package com.learnakantwi.twiguides;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.util.Size;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import static com.learnakantwi.twiguides.NumbersActivity.numbersArrayList;
import static com.learnakantwi.twiguides.SubConversationIntroductionActivity.conversationArrayList;

public class QuizSubConversation extends AppCompatActivity {

    Random random;
    ArrayList<String> answers;
    int answerLocation;
    int randomChoiceQuestion;
    TextView questionText;
    TextView possibleA1;
    TextView possibleA2;
    TextView possibleA3;
    String english1;
    String twi1;
    String gradeText;
    int finalScore;

    Handler handler1;
    Runnable ranable;


    TextView correctAnswer;
    TextView correctWrong;
    TextView scoreText;
    TextView counterText;
   // TextView tvGradeText;

    int totalQuestions=50;
    int score;
    int counter;

    long delaytime = 5000;
    double scorePercent= ((score/totalQuestions)*100);

    int chosenSize=(conversationArrayList.size()-1);
    int chosenSizeRand;
    int muteNumber;

    StorageReference storageReference;
    MediaPlayer playFromDevice;
    MediaPlayer mp1;

    Button btStartPlayAgain;

    ImageView ivMute;
    ImageView ivUnMute;


    Toast toast;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
                resetQuiz1();
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

    public void resetQuiz1(){
        counter =0;
        score=0;
        correctWrong.setText("");
        scoreText.setText("");
        counterText.setText("");
        generateQuestion();
        //tvGradeText.setText("");
    }



    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public void resetQuiz(){
        counter =0;
        score=0;
        correctWrong.setText("");
        scoreText.setText("");
        counterText.setText("");
        //tvGradeText.setText("");
    }

    public void hideStartButton(View v){
        btStartPlayAgain.setVisibility(View.INVISIBLE);
        scoreText.setText("");
        counterText.setText("");
        resetQuiz();
        generateQuestion();

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
        if (myFile.exists() && muteNumber ==1) {

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

    public void playFromFileOrDownload(final String filename, final String appearText) {

        if (counter<totalQuestions && appearText.equals(twi1)) {
            toast.setText(appearText + " -" + " " + "CORRECT!!!!");
            toast.show();
            score++;
            scoreText.setText(String.valueOf(score));
        }

        if (counter == totalQuestions) {

            toast.setText(appearText + " -" + " " + "CORRECT!!!!");
            toast.show();
            score++;
            scoreText.setText(String.valueOf(score));

            btStartPlayAgain.setVisibility(View.VISIBLE);
            btStartPlayAgain.setText(getString(R.string.playagain));
            finalScore = score;

            double d1 = score;
            double d2 = totalQuestions;
            double scorePercent = ((d1 / d2) * 100);
            scorePercent = Math.round(scorePercent * 10.0) / 10.0;


            if (scorePercent >= 90) {
                //tvGradeText.setText(getString(R.string.excellent));
                ExcellentSound();
                gradeText = getString(R.string.excellent);
            } else if (scorePercent > 40 && scorePercent < 90) {
                //tvGradeText.setText(getString(R.string.welldone));
                gradeText = getString(R.string.welldone);
            } else if (scorePercent > 20 && scorePercent <= 40) {
                //tvGradeText.setText(getString(R.string.nicetry));
                gradeText = getString(R.string.nicetry);
            } else if (scorePercent <= 20){
                //tvGradeText.setText(getString(R.string.fail));
                gradeText = getString(R.string.fail);
            }
            else
            {
                //tvGradeText.setText(getString(R.string.keeptrying));
                gradeText = getString(R.string.keeptrying);

            }


            StringBuilder sb = new StringBuilder();
            sb.append("YOU HAD ").append(scorePercent).append("%").append("\n").append(gradeText);
            // questionText.setText("FINAL SCORE= " + String.valueOf(scorePercent)+"%");
            questionText.setText(sb);

        }
        else if(counter > totalQuestions) {
            double d1 = finalScore;
            double d2 = totalQuestions;
            double scorePercent = ((d1 / d2) * 100);
            scorePercent = Math.round(scorePercent * 10.0) / 10.0;

            StringBuilder sb = new StringBuilder();
            sb.append("YOU HAD ").append(scorePercent).append("%");
            // questionText.setText("FINAL SCORE= " + String.valueOf(scorePercent)+"%");
            questionText.setText(sb);
            scoreText.setText(String.valueOf(finalScore));
            counterText.setText("");

        }
            else {

            File myFile = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/SUBCONVERSATION/" + filename + ".m4a");
            if (myFile.exists() && muteNumber ==1) {

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
                                toast.setText(appearText + " -" + " " + "CORRECT!!!!");
                                toast.show();
                                handler1.postDelayed(ranable,delaytime);
                                //generateQuestion();
                            } else {
                                toast.setText(appearText + " -" + " " + "WRONG \n TRY AGAIN");
                                toast.show();
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (isNetworkAvailable() && muteNumber ==1) {
                final StorageReference musicRef = storageReference.child("/Conversations/" + filename + ".m4a");
                musicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String url = uri.toString();
                        playFromFirebase(musicRef);
                        downloadFile(getApplicationContext(), filename, ".m4a", url);
                        if (appearText.equals(twi1)) {
                            toast.setText(appearText + " -" + " " + "CORRECT!!!!");
                            toast.show();

                            handler1.postDelayed(ranable,delaytime);
                           // generateQuestion();
                        } else {
                                toast.setText(appearText + " -" + " " + "WRONG \n   TRY AGAIN");
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
                            generateQuestion();
                        } else {
                           // toast.setText(appearText);
                            toast.setText(appearText + " -" + " " + "WRONG \n   \t\t\tTRY AGAIN");
                            toast.show();
                        }
                    }
                });
            } else {

                if (appearText.equals(twi1)) {
                    toast.setText(appearText + " -" + " " + "CORRECT!!!!");
                    toast.show();
                   // generateQuestion();
                    handler1.postDelayed(ranable,3000);
                } else {
                    toast.setText(appearText + " -" + " " + "WRONG \n   \t\t\tTRY AGAIN");
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
                    request.setDestinationInExternalFilesDir(getApplicationContext(), Environment.DIRECTORY_MUSIC+"/SUBCONVERSATION", filename + fileExtension);
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

    public void quizClickSound(View view){


        int idview= view.getId();

        TextView blabla = view.findViewById(idview);
        String a = (String) blabla.getText();


        correctWrong.setText(getString(R.string.correctanswers));
        counter++;
        String counterSet = counter +" / " + totalQuestions;
        counterText.setText(counterSet );

        // scoreText.setText(String.valueOf(score));


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

   /* public void hideStartButton(View v){
        button5.setVisibility(View.INVISIBLE);
        scoreText.setText("");
        counterText.setText("");
        resetQuiz();
        generateQuestion();

    }*/


    public void generateQuestion(){


        random = new Random();
        answers = new ArrayList<>();
        answerLocation = random.nextInt(3);

        randomChoiceQuestion = random.nextInt(conversationArrayList.size()-1);
       english1 = conversationArrayList.get(randomChoiceQuestion).getEnglishConversation();
       //english1 = conversationArrayList.get(0).getEnglishConversation();

        StringBuilder sb = new StringBuilder();
        sb = sb.append("\"").append(english1).append("\"");

        SpannableStringBuilder builder =new SpannableStringBuilder();
        SpannableString howWillYouSay = new SpannableString("How will you say:\n");
        SpannableString inTwi = new SpannableString("\n In Twi?");
       // howWillYouSay.setSpan(new ForegroundColorSpan(Color.GREEN), 0,howWillYouSay.length(),0);
        howWillYouSay.setSpan(new StyleSpan(Typeface.ITALIC), 0,howWillYouSay.length(),0);
        inTwi.setSpan(new StyleSpan(Typeface.NORMAL), 0, inTwi.length(),0);

        SpannableString questionText1 = new SpannableString(sb);
        questionText1.setSpan(new StyleSpan(Typeface.BOLD),0,questionText1.length(),0);
        builder.append(howWillYouSay).append(questionText1).append(inTwi);


        questionText.setText(builder, TextView.BufferType.SPANNABLE);



        twi1 = conversationArrayList.get(randomChoiceQuestion).getTwiConversation();
       //twi1 = conversationArrayList.get(0).getTwiConversation();


        for (int i = 0; i < 3;i++ ){
            chosenSizeRand= random.nextInt(chosenSize);

            if (i== answerLocation){
                answers.add(twi1);
            }
            else {
                while (conversationArrayList.get(chosenSizeRand).getTwiConversation().equals(twi1) || conversationArrayList.get(chosenSizeRand).getEnglishConversation().contains("(")){
                    chosenSizeRand = random.nextInt(chosenSize);}
                answers.add(conversationArrayList.get(chosenSizeRand).getTwiConversation());

            }
        }


        possibleA1.setText(answers.get(0));
        possibleA2.setText(answers.get(1));
        possibleA3.setText(answers.get(2));

        /*possibleA1.setText(String.format("A.   %s", answers.get(0)));
        possibleA2.setText(String.format("B.   %s", answers.get(1)));
        possibleA3.setText(String.format("C.   %s", answers.get(2)));*/
        //button4.setText(answers.get(3));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_sub_conversation);

        muteNumber=1;

        handler1 = new Handler();

        ranable = new Runnable() {
            @Override
            public void run() {
                //Toast.makeText(QuizSubConversation.this, "Me", Toast.LENGTH_SHORT).show();
                generateQuestion();
            }
        };

        toast = Toast.makeText(getApplicationContext(), " " , Toast.LENGTH_SHORT);
        btStartPlayAgain = findViewById(R.id.btPlayStartAgain);

        questionText = findViewById(R.id.tvQuizQuestion);
        possibleA1 = findViewById(R.id.tvPossibleA1);
        possibleA2 = findViewById(R.id.tvPossibleA2);
        possibleA3 = findViewById(R.id.tvPossibleA3);




        storageReference = FirebaseStorage.getInstance().getReference();


       // button5 = findViewById(R.id.playAgain);
        correctWrong = findViewById(R.id.tvCorrectWrongText);
        scoreText = findViewById(R.id.tvScore);
        counterText=findViewById(R.id.tvCounterText);
        ivMute = findViewById(R.id.tvgrade);

        ivUnMute = findViewById(R.id.ivUnMute);
        ivUnMute.setVisibility(View.INVISIBLE);

        ivMute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               ivMute.setVisibility(View.INVISIBLE);
                ivUnMute.setVisibility(View.VISIBLE);
                muteNumber=0;
                toast.setText("Sound Muted");
                toast.show();

            }
        });

        ivUnMute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivMute.setVisibility(View.VISIBLE);
                ivUnMute.setVisibility(View.INVISIBLE);
                toast.setText("Sound Unmuted");
                muteNumber=1;
            }
        });


        //button5.setVisibility(View.INVISIBLE);


        resetQuiz();
        //generateQuestion();
        //questionText.set

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (handler1 !=null){
            handler1.removeCallbacks(ranable);
        }
        if (playFromDevice!=null){
            playFromDevice.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (handler1 !=null){
            handler1.removeCallbacks(ranable);
        }
        if (playFromDevice!=null){
            playFromDevice.stop();
        }

        Random random = new Random();
        int prob = random.nextInt(10);
    }

}
