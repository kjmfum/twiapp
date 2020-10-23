package com.learnakantwi.twiguides;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
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
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
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

import static com.learnakantwi.twiguides.SubConversationApologiesAndResponses.conversationsApologiesArrayList;
import static com.learnakantwi.twiguides.SubConversationDirections.conversationDirections;
import static com.learnakantwi.twiguides.SubConversationHospital.conversationHospital;
import static com.learnakantwi.twiguides.SubConversationIntroductionActivity.conversationArrayList;
import static com.learnakantwi.twiguides.SubConversationPhone.conversationPhone;
import static com.learnakantwi.twiguides.SubConversationLove.conversationLove;
import static com.learnakantwi.twiguides.SubConversationWelcomingOthers.conversationWelcomingOthersArrayList;

public class QuizSubConversationIntroducing extends AppCompatActivity {

    Random random;
    ArrayList<String> answers;
    ArrayList<subConversation> arrayList;
    ArrayList<Integer> answersList;
    ArrayList<Integer> possibleAnswersList;

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

    int chosenSize=10;
    int chosenSizeRand;
    int muteNumber;

    boolean canPlay = true;

    StorageReference storageReference;
    MediaPlayer playFromDevice;
    MediaPlayer mp1;

    Button btStartPlayAgain;

    ImageView ivMute;
    ImageView ivUnMute;


    Toast toast;

    PlayFromFirebase convertAndPlay;



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
        generateQuestion(arrayList);
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
        generateQuestion(arrayList);

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
        //canPlay = false;
        counter++;

        String counterSet = counter +" / " + totalQuestions;
        counterText.setText(counterSet );


        if(appearText.equals(twi1)){


            if (muteNumber == 0){
                delaytime = 1000;

            }else {
                delaytime = 5000;
            }

            questionText.setBackgroundColor(Color.GREEN);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    questionText.setBackgroundColor(Color.WHITE);
                    canPlay = true;

                }
//4700
            },delaytime);

        }
        else{
            delaytime = 600;

            ColorStateList colorStateList = questionText.getTextColors();
            questionText.setTextColor(Color.WHITE);
            questionText.setBackgroundColor(Color.RED);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    questionText.setBackgroundColor(Color.WHITE);
                    questionText.setTextColor(colorStateList);
                    canPlay = true;
                }
                //600
            },delaytime);
        }

        if (counter<=totalQuestions && appearText.equals(twi1)) {
            toast.setText(appearText + " -" + " " + "CORRECT!!!!");
            toast.show();
            score++;
            scoreText.setText(String.valueOf(score));
        }

        if (counter == totalQuestions) {

           /* toast.setText(appearText + " -" + " " + "CORRECT!!!!");
            toast.show();
            score++;
            scoreText.setText(String.valueOf(score));*/

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
              /*      playFromDevice.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            toast.setText("Completed Playing: " + "CORRECT!!!!");
                            toast.show();
                        }
                    });*/
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
                            generateQuestion(arrayList);
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
                    if (muteNumber == 0){
                        delaytime = 1000;
                    }
                    handler1.postDelayed(ranable,delaytime);
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

//        Toast.makeText(this, "Yes o", Toast.LENGTH_SHORT).show();


        correctWrong.setText(getString(R.string.correctanswers));



        // scoreText.setText(String.valueOf(score));

        String b = PlayFromFirebase.viewTextConvert(a);


        if (canPlay){
            canPlay = false;
          /*  if(!canPlay){

                counter++;
            }*/
            playFromFileOrDownload(b,a);

        }


    }



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

    public void generateQuestion(ArrayList<subConversation> arrayList){


        random = new Random();
        answers = new ArrayList<>();
        answerLocation = random.nextInt(3);

        randomChoiceQuestion = random.nextInt(arrayList.size()-1);

        if(answersList.size()< arrayList.size()-1) {
            while (answersList.contains(randomChoiceQuestion)) {
                // randomChoiceQuestion = random.nextInt(numbersArrayList.size()-1);
                randomChoiceQuestion = random.nextInt(arrayList.size() - 1);
                //Toast.makeText(this, Arrays.asList(answers).toString() + " Good", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            answersList.clear();
        }
        answersList.add(randomChoiceQuestion);


        english1 = arrayList.get(randomChoiceQuestion).getEnglishConversation();
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



        twi1 = arrayList.get(randomChoiceQuestion).getTwiConversation();
        //twi1 = conversationArrayList.get(0).getTwiConversation();


        for (int i = 0; i < 3;i++ ){

            chosenSizeRand= random.nextInt(chosenSize);


            if (i== answerLocation){
                answers.add(twi1);
               // possibleAnswersList.add(chosenSizeRand);
            }
            else {
                while (arrayList.get(chosenSizeRand).getTwiConversation().equals(twi1) || arrayList.get(chosenSizeRand).getEnglishConversation().contains("(")){
                        chosenSizeRand = random.nextInt(chosenSize);
                    }

                answers.add(arrayList.get(chosenSizeRand).getTwiConversation());
                //possibleAnswersList.add(chosenSizeRand);
            }
            //Log.i("List", possibleAnswersList.toString());
            //Toast.makeText(this, possibleAnswersList.toString(), Toast.LENGTH_SHORT).show();
        }

       // possibleAnswersList.clear();


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


        convertAndPlay = new PlayFromFirebase();

        muteNumber=1;
        answersList = new ArrayList<>();
        possibleAnswersList = new ArrayList<>();

        Intent intent = getIntent();
        String category = intent.getStringExtra("arrayName");
        switch (category){
            case "Welcoming others":
                chosenSize = conversationWelcomingOthersArrayList.size()-1;
                arrayList = conversationWelcomingOthersArrayList;
                break;
            case "On The Phone":
                chosenSize = conversationPhone.size()-1;
                arrayList = conversationPhone;
                break;
            case "Apologies and Regret":
                chosenSize = conversationsApologiesArrayList.size()-1;
                arrayList = conversationsApologiesArrayList;
                break;
            case "Asking and Giving Directions":
                chosenSize = conversationDirections.size()-1;
                arrayList = conversationDirections;
                break;
            case "At the Hospital":
                chosenSize = conversationHospital.size()-1;
                arrayList = conversationHospital;
                break;
            case "Love and Relationship":
                chosenSize = conversationLove.size()-1;
                arrayList = conversationLove;
                break;
            default: chosenSize = conversationArrayList.size()-1;
                arrayList = conversationArrayList;
        }

        handler1 = new Handler();

        ranable = new Runnable() {
            @Override
            public void run() {
                //Toast.makeText(QuizSubConversationIntroducing.this, "Me", Toast.LENGTH_SHORT).show();
                generateQuestion(arrayList);
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
