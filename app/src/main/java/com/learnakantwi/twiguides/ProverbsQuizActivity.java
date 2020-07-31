package com.learnakantwi.twiguides;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static com.learnakantwi.twiguides.ProverbsActivity.proverbsArrayList;
import static com.learnakantwi.twiguides.SubConversationIntroductionActivity.conversationArrayList;

public class ProverbsQuizActivity extends AppCompatActivity {

    static  ArrayList<Proverbs> proverbsQuizQuestionArray = new ArrayList<>();
    Random random;
    AdView mAdView;
    InterstitialAd mInterstitialAd;
    ArrayList<Integer> answers;
    Button btStartPlayAgain;
    Button btReset;
    TextView questionText;
    TextView possibleA1;
    TextView possibleA2;
    TextView possibleA3;
    TextView tvSubscribe;
    ImageView ivMute;
    ImageView ivUnMute;
    int answerLocation;
    Integer randomChoiceQuestion;
    int score;
    int wrong;
    int totalQuestions=20;
    int counter;
    int muteNumber=1;
    int finalScore;
    int freeze=0;
    int Sub;
    String english1;
    String twi1;
    String gradeText;
    StorageReference storageReference;
    MediaPlayer playFromDevice;
    MediaPlayer mp1;
    TextView correctWrong;
    TextView scoreText;
    TextView counterText;
    Toast toast;

    Handler handler1;
    Runnable ranable;

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
        if (MainActivity.Subscribed==1){
            Intent intent = new Intent(getApplicationContext(), SubPHomeMainActivity.class);
            startActivity(intent);


        }
        else{
            Intent intent = new Intent(getApplicationContext(), HomeMainActivity.class);
            startActivity(intent);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public void hideStartButton(View v){
        freeze=0;
        btStartPlayAgain.setVisibility(View.INVISIBLE);
        btReset.setVisibility(View.VISIBLE);
        scoreText.setText("");
        counterText.setText("");
        correctWrong.setText("");
        resetQuiz();
        generateQuestion();

    }

    public void resetQuiz(){
        counter =totalQuestions;
        score=0;
        wrong=0;
        correctWrong.setText("");
        scoreText.setText("");
       // counterText.setText("");
        counterText.setText("Remaining: "+ counter);
        questionText.setText("");
        answers.clear();
        //tvGradeText.setText("");
    }

    public void generateQuestion(){


        random = new Random();

        answerLocation = random.nextInt(3);

       randomChoiceQuestion = random.nextInt(proverbsQuizQuestionArray.size()-1);
       // answers.add(randomChoiceQuestion);


       // randomChoiceQuestion = proverbsQuizQuestionArray.size()-1; // particular number test

      /*  for (Integer x: answers){
            System.out.println("Hey: "+ x + answers.get() );
        }*/
        //if (answers.size()<proverbsQuizQuestionArray.size()-1){
            if (answers.size()<totalQuestions){
            while (answers.contains(randomChoiceQuestion)){
                randomChoiceQuestion = random.nextInt(proverbsQuizQuestionArray.size()-1);
                //Toast.makeText(this, Arrays.asList(answers).toString() + " Good", Toast.LENGTH_SHORT).show();
            }
               // System.out.println("Hey: "+ randomChoiceQuestion);
            answers.add(randomChoiceQuestion);
           // Toast.makeText(this, proverbsQuizQuestionArray.size() +": "+ answers.size(), Toast.LENGTH_SHORT).show();

            english1 = proverbsQuizQuestionArray.get(randomChoiceQuestion).getProverbQuiz();
            //english1 = conversationArrayList.get(0).getEnglishConversation();
            PlayFromFirebase playFromFirebase = new PlayFromFirebase();
            String c = PlayFromFirebase.viewTextConvert(english1);

            playFromFileOrDownload(c,english1);

            StringBuilder sb = new StringBuilder();
            sb = sb.append("\"").append(english1).append("\"");

            SpannableStringBuilder builder =new SpannableStringBuilder();
            SpannableString howWillYouSay = new SpannableString("Toa ɛbɛ yi so(Complete the proverb):\n");
            SpannableString inTwi = new SpannableString("\n In Twi");
            // howWillYouSay.setSpan(new ForegroundColorSpan(Color.GREEN), 0,howWillYouSay.length(),0);
            howWillYouSay.setSpan(new StyleSpan(Typeface.ITALIC), 0,howWillYouSay.length(),0);
            inTwi.setSpan(new StyleSpan(Typeface.NORMAL), 0, inTwi.length(),0);

            SpannableString questionText1 = new SpannableString(sb);
            questionText1.setSpan(new StyleSpan(Typeface.BOLD),0, questionText1.length(),0);
            builder.append(howWillYouSay).append(questionText1).append(inTwi);


            questionText.setText(builder, TextView.BufferType.SPANNABLE);


            questionText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playFromFileOrDownload(c,english1);
                }
            });

            twi1 = proverbsQuizQuestionArray.get(randomChoiceQuestion).getTwiProverb();
            //twi1 = conversationArrayList.get(0).getTwiConversation();


            int randomiseAnswer = random.nextInt(5);
            switch (randomiseAnswer) {
                case 1:
                    possibleA1.setText(proverbsQuizQuestionArray.get(randomChoiceQuestion).getpQuizA());
                    possibleA2.setText(proverbsQuizQuestionArray.get(randomChoiceQuestion).getpQuizB());
                    possibleA3.setText(proverbsQuizQuestionArray.get(randomChoiceQuestion).getpQuizC());
                    return;
                case 2:
                    possibleA1.setText(proverbsQuizQuestionArray.get(randomChoiceQuestion).getpQuizA());
                    possibleA2.setText(proverbsQuizQuestionArray.get(randomChoiceQuestion).getpQuizC());
                    possibleA3.setText(proverbsQuizQuestionArray.get(randomChoiceQuestion).getpQuizB());
                    return;
                case 3:
                    possibleA1.setText(proverbsQuizQuestionArray.get(randomChoiceQuestion).getpQuizB());
                    possibleA2.setText(proverbsQuizQuestionArray.get(randomChoiceQuestion).getpQuizA());
                    possibleA3.setText(proverbsQuizQuestionArray.get(randomChoiceQuestion).getpQuizC());
                    return;
                case 4:
                    possibleA1.setText(proverbsQuizQuestionArray.get(randomChoiceQuestion).getpQuizB());
                    possibleA2.setText(proverbsQuizQuestionArray.get(randomChoiceQuestion).getpQuizC());
                    possibleA3.setText(proverbsQuizQuestionArray.get(randomChoiceQuestion).getpQuizA());
                    return;
                case 0:
                    possibleA1.setText(proverbsQuizQuestionArray.get(randomChoiceQuestion).getpQuizC());
                    possibleA2.setText(proverbsQuizQuestionArray.get(randomChoiceQuestion).getpQuizA());
                    possibleA3.setText(proverbsQuizQuestionArray.get(randomChoiceQuestion).getpQuizB());
                    return;
                default:
                    throw new IllegalStateException("Unexpected value: " + randomiseAnswer);
            }

        }
        else{
            timeup();
                //btStartPlayAgain.setVisibility(View.VISIBLE);
           // Toast.makeText(this, "The End "+ randomChoiceQuestion+ "\n "+ answers.size(), Toast.LENGTH_SHORT).show();
        }


       /* for (int i=0; i<answers.size(); i++){
            System.out.println("Hey: "+ answers.get(i));
        }*/

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

    public void timeup(){
        btStartPlayAgain.setVisibility(View.VISIBLE);
        btStartPlayAgain.setText(getString(R.string.playagain));
        counterText.setText("");
       // finalScore = score;

       // Toast.makeText(this, "Hey: "+ score +"\n"+totalQuestions, Toast.LENGTH_SHORT).show();
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

    public void playFromFileOrDownload(final String filename, final String appearText){

       // Toast.makeText(this, counter+":\n"+ proverbsQuizQuestionArray.size(), Toast.LENGTH_SHORT).show();
        if(freeze==1) {
        }
        else {
            File myFile = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/PROVERBS/"+filename+ ".m4a");
            if (myFile.exists() && muteNumber ==1) {
                //if (myFile.exists()) {

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
            } else if (isNetworkAvailable() && muteNumber ==1) {
                final StorageReference musicRef = storageReference.child("/Proverbs/" + filename + ".m4a");
                musicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String url = uri.toString();
                        playFromFirebase(musicRef);
                        downloadFile(getApplicationContext(), filename, ".m4a", url);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        toast.setText("No Internet");
                        toast.show();
                    }
                });

            }
            else if (!isNetworkAvailable()){
                if (!(muteNumber==1)){
                    toast.setText("Sound Muted");
                    toast.show();
                }
                else {
                    toast.setText("No Internet");
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
                    request.setDestinationInExternalFilesDir(getApplicationContext(), Environment.DIRECTORY_MUSIC + "/PROVERBS" , filename + fileExtension);
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

       // Toast.makeText(this, "Hi:\n"+ proverbsQuizQuestionArray.size(), Toast.LENGTH_SHORT).show();
        int idview= view.getId();

        TextView blabla = view.findViewById(idview);
        String a = (String) blabla.getText();
        String c= proverbsQuizQuestionArray.get(randomChoiceQuestion).getTwiProverb();

        counter--;

        counterText.setText("Remaining: "+ counter);

        if (a.equals(proverbsQuizQuestionArray.get(randomChoiceQuestion).getpQuizA())){
           // Toast.makeText(this, proverbsQuizQuestionArray.get(randomChoiceQuestion).getTwiProverb() , Toast.LENGTH_SHORT).show();

            toast.setText(proverbsQuizQuestionArray.get(randomChoiceQuestion).getTwiProverb());
            toast.show();

            PlayFromFirebase playFromFirebase = new PlayFromFirebase();
            String b = PlayFromFirebase.viewTextConvert(c);
            playFromFileOrDownload(b,c);

            score++;
            scoreText.setText("Correct:" + score);
            scoreText.setBackgroundColor(Color.GREEN);
            scoreText.setTextColor(Color.WHITE);

            if(muteNumber==1){
               /* new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!(freeze==1)){
                            generateQuestion();
                            scoreText.setBackgroundColor(Color.WHITE );
                            scoreText.setTextColor(Color.GRAY);
                        }

                    }
                },6000);*/
               handler1.postDelayed(ranable,6000);
            }
            else{
                handler1.postDelayed(ranable,700);
            }


        }
        else{
            wrong++;

            correctWrong.setText("Wrong:" + wrong);
            correctWrong.setBackgroundColor(Color.RED);
            correctWrong.setTextColor(Color.WHITE);
            toast.setText("Dabi (Wrong)");
            toast.show();
          /*  new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!(freeze==1)){
                        generateQuestion();
                        if(muteNumber==1){
                            ExcellentSound("wrongsound");
                        }
                        correctWrong.setBackgroundColor(Color.WHITE);
                        correctWrong.setTextColor(Color.GRAY);
                    }
                }
            }, 700);*/
          handler1.postDelayed(ranable,700);
        }

       // correctWrong.setText(getString(R.string.correctanswers));
        String counterSet = counter +" / " + totalQuestions;

       // generateQuestion();
       // playFromFileOrDownload(b,a);

    }

    public void resetButton(){

        freeze=1;
        btReset.setVisibility(View.INVISIBLE);
        btStartPlayAgain.setVisibility(View.VISIBLE);
        btStartPlayAgain.setText(getString(R.string.playagain));
        questionText.setText("Are you Ready? \n Toa Ɛbɛ Yi So");
        counter =0;
        score=0;
        wrong=0;
        correctWrong.setText("");
        scoreText.setText("");
        counterText.setText("");
        scoreText.setBackgroundColor(Color.WHITE );
        scoreText.setTextColor(Color.GRAY);

        correctWrong.setBackgroundColor(Color.WHITE);
        correctWrong.setTextColor(Color.GRAY);

        answers.clear();
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

    public void goToSubscriptionPage (View v){
        // Toast.makeText(this, String.valueOf(subscriptionState), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), InAppActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proverbs_quiz);

        tvSubscribe = findViewById(R.id.tvSubscribe);
        tvSubscribe.setVisibility(View.INVISIBLE);

        handler1 = new Handler();
        ranable = new Runnable() {
            @Override
            public void run() {
                if (!(freeze==1)){
                    generateQuestion();
                    scoreText.setBackgroundColor(Color.WHITE );
                    scoreText.setTextColor(Color.GRAY);
                }
            }
        };





        SharedPreferences subscribe = getSharedPreferences("AdsDecision",MODE_PRIVATE);
        Sub = subscribe.getInt("Sub",0);

        if (MainActivity.Subscribed != 1){
            mInterstitialAd = new InterstitialAd(this);
            mInterstitialAd.setAdUnitId(getString(R.string.AdUnitIDInterstitial));
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
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
        }

        if (Sub==0){
            tvSubscribe.setVisibility(View.VISIBLE);
            totalQuestions=20;
        }
        else{
            totalQuestions=45;
        }

        answers = new ArrayList<>();

        storageReference = FirebaseStorage.getInstance().getReference();

        toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);

        btStartPlayAgain = findViewById(R.id.btPlayStartAgain);
        questionText = findViewById(R.id.tvQuizQuestion);
        possibleA1 = findViewById(R.id.tvPossibleA1);
        possibleA2 = findViewById(R.id.tvPossibleA2);
        possibleA3 = findViewById(R.id.tvPossibleA3);


        correctWrong = findViewById(R.id.tvCorrectWrongText);
        scoreText = findViewById(R.id.tvScore);
        counterText=findViewById(R.id.tvCounterText);
        ivMute = findViewById(R.id.tvgrade);

        ivUnMute = findViewById(R.id.ivUnMute);
        ivUnMute.setVisibility(View.INVISIBLE);

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

        counterText.setText("");


        btReset = findViewById(R.id.btReset);
        btReset.setVisibility(View.INVISIBLE);
        btReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetButton();
            }
        });

        proverbsQuizQuestionArray.add(new Proverbs("Woforo dua pa a", "ɛnna yepia wo","wo ho tɔ wo","wo nya aduane","Woforo dua pa a ɛnna yepia woɔ","If you climb a good tree, you get a push","If you start something good you will attract people to help"));
        proverbsQuizQuestionArray.add(new Proverbs("Wusie nni ahoɔden wɔ","mframa kurom","nsuo anim","soro","Wusie nni ahoɔden wɔ mframa kurom","The smoke has no power in the city of the wind","The stranger has no power in a strange land"));
        proverbsQuizQuestionArray.add(new Proverbs("Nokware di","etuo","nkonim","nokware","Nokware di etuo", "The truth gets a gun","Speaking the truth boldly can at times end your life as if you called for to be shot by a gun"));
        proverbsQuizQuestionArray.add(new Proverbs("Sɛnea sekan te no","saa ara na ne bɔha nso te","saa ara na etuo te","saa ara na apasoɔ te","Sɛnea sekan te no saa ara na ne bɔha nso te","The shape of the sword is the same as its scabbard","Your behaviour tells where you come from"));
        proverbsQuizQuestionArray.add(new Proverbs("Aboa aserewa hwɛ ne kɛse ho na","wanwene ne buo","wadidi","wafa nnamfo","Aboa aserewa hwɛ ne kɛse ho na wanwene ne buo", "The silverbird weaves its nest according to its size", "Do what you can do and avoid trying to do things beyond your ability in order to please others"));
        proverbsQuizQuestionArray.add(new Proverbs("Abofra bɔ nnwa","na ɔmmɔ akyekyedeɛ","na ɔmmɔ game","na ɔmmɔ toa","Abofra bɔ nnwa na ɔmmɔ akyekyedeɛ","A child breaks the shell of a crab and not the tortoise", "A child must know his limit. Some things are for adults"));
        proverbsQuizQuestionArray.add(new Proverbs("Aboa bi bɛka wo a","na ofiri wo ntoma mu","na ne ho yɛ den","suro no","Aboa bi bɛka wo a, na ofiri wo ntoma mu","If an animal will bite you, it is the one hidden in your clothes", "It is people close to you that will hurt you"));
        proverbsQuizQuestionArray.add(new Proverbs("Ɔba nyansafo, yebu no","bɛ, yɛnnka no asɛm","onipa kɛse","sɛ ɔhene","Ɔba nyansafo, yebu no bɛ, yɛnnka no asɛm","We speak in proverbs to a wise son, we do not use direct statements","Just a proverb can be used to instruct a wise person. No need to explain things in detail to a wise person"));
        proverbsQuizQuestionArray.add(new Proverbs("Ti koro nkɔ","agyina","nnyaw nyansa","ɔhaw mu","Ti koro nkɔ agyina","One head does not consult","It is better to consult with others before making a decision"));
        proverbsQuizQuestionArray.add(new Proverbs("Anomaa antu a","obua da","ogyina hɔ","ɛkɔm de no","Anomaa antu a, obua da","If a bird does not fly, it starves to death","If you don't take action and work you will not gain anything and will die as a result. Laziness produces nothing"));
        proverbsQuizQuestionArray.add(new Proverbs("Obi nnim oberempɔn","ahyɛase","din ankasa","awieɛ","Obi nnim oberempɔn ahyɛase","No one knows the beginning of a great man","You cannot always tell how great a man will be by his current state. A poor person can become a great man in the future"));
        proverbsQuizQuestionArray.add(new Proverbs("Agya bi wu a","agya bi te ase","yɛyɛ ayie ma no","ne mma su","Agya bi wu a, agya bi te ase","If a father dies, another father lives","If a parent dies, you can find someone who will look after you like your parent"));
        proverbsQuizQuestionArray.add(new Proverbs("Animguase mfata","Akanni ba","obiara","me dɔfo","Animguase mfata Akanni ba","To be disgraced is not deserving of an Akan", "If you have respect for yourself then it is better to die than to be disgraced"));
        proverbsQuizQuestionArray.add(new Proverbs("Agorɔ bɛsɔ a, efiri","anɔpa","ase ntɛm","nhyehyɛe pa","Agorɔ bɛsɔ a, efiri anɔpa","You can tell from the morning if the play will be nice","You can tell from the beginning of a venture how successfull it will be in the future"));
        proverbsQuizQuestionArray.add(new Proverbs("Kwaterekwa se ɔbɛma wo ntoma a","tie ne din","hwɛ wo ho yie","ɔredaadaa wo","Kwaterekwa se ɔbɛma wo ntoma a, tie ne din","If a naked person says that he will give you a cloth, listen to his name","Be careful when someone who is himself in need of something promises to give you that thing. Dont trust all promises. Take into consideratoin the calibre of a person who promises you something"));
        proverbsQuizQuestionArray.add(new Proverbs("Yɛsoma onyansafo","ɛnyɛ anamɔntenten","ɛnyɛ kwasea","ɛnyɛ obiara kɛkɛ","Yɛsoma onyansafo, ɛnyɛ anamɔntenten","It is the wise person that we send on errand but not a person with long steps","Speed should not override efficiency. Getting things done properly is better than getting it done fast but not properly."));
        proverbsQuizQuestionArray.add(new Proverbs("Aboa a onni dua no","Nyame na ɔpra ne ho","ne ho yɛ fɛ","ne ho tɔ no","Aboa a onni dua no, Nyame na ɔpra ne ho","An animal without a tail is cleaned by God","God provides for those who have no means of catering for themselves"));
        proverbsQuizQuestionArray.add(new Proverbs("Borɔferɛ a ɛyɛ dɛ","na abaa da ase","na yedi","na yɛde ma adɔfo","Borɔferɛ a ɛyɛ dɛ na abaa da ase","It is the pawpaw tree which has tasty fruits that you will find a stick beneath it","If you see people flocking to a particular venture it is because it is profitable"));
        proverbsQuizQuestionArray.add(new Proverbs("Prayɛ, sɛ wuyi baako a na ebu","woka bom a emmu","nanso yɛde pra","wuyi mmienu a emmu","Prayɛ, sɛ wuyi baako a na ebu, woka bom a emmu","If you remove one broomstick it will break but if you put all the broomsticks together it will not break","We are stronger when we are united. It is harder to defeat a united people than a single person"));
        proverbsQuizQuestionArray.add(new Proverbs("Nsateaa nyinaa","nnyɛ pɛ","ho hia","yɛ fɛ","Nsateaa nyinaa nnyɛ pɛ","All fingers are not the same","We all have different abilities"));
        proverbsQuizQuestionArray.add(new Proverbs("Obi nnim a","obi kyerɛ","osua","ɔbɔ ka","Obi nnim a, obi kyerɛ","If one does not know, another teaches","You should allow others to teach you things you don't know. You should listen to advice"));
        proverbsQuizQuestionArray.add(new Proverbs("Abofra hunu ne nsa hohoro a","ɔne mpanyimfoɔ didi","ɔnyare","obiara pɛ n'asɛm","Abofra hunu ne nsa hohoro a ɔne mpanyimfoɔ didi","If a child learns how to wash his hands, he will eat with adults","If a person learns and applies the customs and traditions, people in higher positions will work with him. "));
        proverbsQuizQuestionArray.add(new Proverbs("Yɛwo wo to esie so a","wo nnkyɛ tenten yɛ","mmoa we wo","wo ho tɔ wo","Yɛwo wo to esie so a, wo nnkyɛ tenten yɛ","If you are born onto an anthill, you become tall quickly","If you have a good foundation in life, it is easy for you to succeed in life. If your family is rich, you are able to make money early in life"));
        proverbsQuizQuestionArray.add(new Proverbs("Ayɔnko gorɔ nti","na ɔkɔtɔ annya ti","yetumi nya nnamfo pii","yɛn ani tumi gye","Ayɔnko gorɔ nti na ɔkɔtɔ annya ti","It is because of mingling with friends that the crab has no head","Too many friends can make you lose a lot life. Choose friends wisely"));
        proverbsQuizQuestionArray.add(new Proverbs("Wo nsa akyi bɛyɛ wo dɛ a ɛnte sɛ","wo nsa yam","ɛwoɔ","fufuo ne abɛ nkwan","Wo nsa akyi bɛyɛ wo dɛ a ɛnte sɛ wo nsa yam","The back of your hand can be sweet but it will not be as sweet as your palm or inner surface of your hand","The original is always better than imitation"));


        if (Sub==1){
            proverbsQuizQuestionArray.add(new Proverbs("Obi fom kum a","yɛn mfom ndwa","ɛnyɛ koraa","wasɛe ade","Obi fom kum a, yɛn mfom ndwa","If one kills by mistake, we do not cut up by mistake","We do not deliberately repay someone with a bad deed for their unintentional mistake "));
            proverbsQuizQuestionArray.add(new Proverbs("Wuhu sɛ wo yɔnko abɔdwesɛ rehye a","na wasa nsuo asi wo de ho","na wahwɛ yiye","dum no ma no","Wuhu sɛ wo yɔnko abɔdwesɛ rehye a na wasa nsuo asi wo de ho","If you see that the beard of a friend is on fire, fetch water and put by your beard","If something bad happens to a neighbour, plan on how you will deal it with if it happens to you next"));
            proverbsQuizQuestionArray.add(new Proverbs("Dua a enya wo a ɛbɛwɔ w'ani no","yetu ase; yɛnsensene ano","nhwɛ koraa","bu to fam","Dua a enya wo a ɛbɛwɔ w'ani no, yetu ase; yɛnsensene ano","A tree which is likely to pierce your eye must be uprooted, not merely pruned","If a problem has the potential to harm you, we do not solve it partially but rather we solve it completely. Eliminate completely anything that can harm you"));
            proverbsQuizQuestionArray.add(new Proverbs("Nyansapɔ wɔsane no","badwemma","nkakrankakra","ntɛmntɛm","Nyansapɔ wɔsane no badwemma","A discerning man loosens a hard tight know","It takes a wise person to solve hard riddles. One who solves a complex problem is wise"));
            proverbsQuizQuestionArray.add(new Proverbs("Ahunu bi pɛn nti na aserewa regyegye ne ba agorɔ a","na wayi n'ani ato nkyɛn","ɔhwɛ no yiye","ogyina nkyɛn","Ahunu bi pɛn nti na aserewa regyegye ne ba agorɔ a na wayi n'ani ato nkyɛn","When playing with its child, the silverbird looks away because of what it has seen before","The behaviour of some people are as a result of the bad experiences they have encountered in their lives"));
            proverbsQuizQuestionArray.add(new Proverbs("Abaa a yɛde bɔ Takyi no","yɛde bɛbɔ Baa","yɛde bɛbɔ obiara bi","yɛde bɔ adwoa","Abaa a yɛde bɔ Takyi no yɛde bɛbɔ Baa","The cane which is used on Takyi will also be used on Baa","The punishment given to someone for a crime will be the same punishment for another who commits the same crime. What is done to someone will be the same that will happen to you if you act like him"));
            proverbsQuizQuestionArray.add(new Proverbs("Abɛ bi rebewu a","na ɛsɔ","yɛnnom","ɛso nni mfaso","Abɛ bi rebewu a na ɛsɔ","When some palm trees are about to die, its wine tastes good","Some people get to their best when they are about to retire or in their old age. The aged are the most experienced"));
            proverbsQuizQuestionArray.add(new Proverbs("Nea ɔwɔ aka no pɛn no suro","sonsono","ɔwɔ","ne ho","Nea ɔwɔ aka no pɛn no suro sonsono","He who has been bitten by a snake before, fears the worm", "A bad experience with a particular venture makes one afraid of anything resembling it"));
            proverbsQuizQuestionArray.add(new Proverbs("Efie biara","Mensah wɔ mu","Nnipa wom","Ɔhaw wom","Efie biara Mensah wɔ mu","Every household has a third born","In every community there will be people whose opinion are totally different which might result in problems"));
            proverbsQuizQuestionArray.add(new Proverbs("Abofra a ɔmma ne maame nna no","bentoa mpa ne to da","ɔno nso brɛ","osu saa","Abofra a ɔmma ne maame nna no, bentoa mpa ne to da","The enema will not depart from the buttocks of a child who doesn't let his mother sleep", "If you make trouble for your leaders who cater for you, you will also not have peace"));
            proverbsQuizQuestionArray.add(new Proverbs("Hu m'ani so ma me nti na","atwe mmienu nam","nnipa mmienu nam","nnamfo ho hia","Hu m'ani so ma me nti na atwe mmienu nam","Deers walk in pairs so that one can blow the eye of the other if needed","It is good to have a partner so that he will support you when you are in need"));
            proverbsQuizQuestionArray.add(new Proverbs("Obi nnom aduro mma","ɔyarefo","ne maame","ne ho nkyere no","Obi nnom aduro mma ɔyarefo","One does not take medicine for a sick person", "Don't expect someone to handle your responsibilities for you. There are some responsibilities you cannot do for another person"));
            proverbsQuizQuestionArray.add(new Proverbs("Akokɔbere nim adekyee nanso","otie firi akokɔ nini ano","ɔyɛ dinn","onnim adesae","Akokɔbere nim adekyee nanso otie firi akokɔ nini ano","The hen knows of the new day but it listens to the announcement from the cock","Even though you might be knowledgeable in something it is always good to listen to the elderly and wait for direction"));
            proverbsQuizQuestionArray.add(new Proverbs("Ano da hɔ kwa a","ɛkeka nsɛm","ɛso nni mfaso","ɛsɛ sɛ woyɛ dinn","Ano da hɔ kwa a, ɛkeka nsɛm","A mouth which is idle will say many things","Those who have nothing to do become gossipers or commit crimes"));
            proverbsQuizQuestionArray.add(new Proverbs("Ɔsansa fa adeɛ a","ɔde kyerɛ amansan","ɔde ma ne maame","ɔde ma ne ba","Ɔsansa fa adeɛ a ɔde kyerɛ amansan","When the hawk picks up something it shows it to the universe","An honest person will not hide his works"));
            proverbsQuizQuestionArray.add(new Proverbs("Ɛtoa na ɛpɛ na","ahoma da ne kɔn mu","wɔde nsa gu mu","ɛda fam","Ɛtoa na ɛpɛ na ahoma da ne kɔn mu","The bottle likes it that is why there is a rope around its neck","It is your fault if you allow your enemies to trap you"));
            proverbsQuizQuestionArray.add(new Proverbs("Dadeɛ bi twa","dadeɛ bi","dua bi","sikakɔkɔɔ","Dadeɛ bi twa dadeɛ bi","An iron can cut another iron or can sharpen another iron","There is someone or something stronger than you. Even if you are strong it doesn't mean that you are unbeatable"));
            proverbsQuizQuestionArray.add(new Proverbs("Obi atifi nso yɛ obi","anaafoɔ","atifi","atɔe","Obi atifi nso yɛ obi anaafoɔ","Your north is someone's south","What you see as new maybe old to someone. "));
            proverbsQuizQuestionArray.add(new Proverbs("Abosomakoterɛ se ntɛm yɛ","brɛbrɛ nso yɛ","sen brɛbrɛ","akwadworɔ","Abosomakoterɛ se ntɛm yɛ, brɛbrɛ nso yɛ","The chameleon claims that to be fast is good and to be slow is also good","If you do anything in good faith it is good"));
            proverbsQuizQuestionArray.add(new Proverbs("Wo sum borɔdeɛ a","sum kwadu","wunya aduane","wunya sika","Wo sum borɔdeɛ a, sum kwadu","You should take good care of the banana just as you take good care of the plantain","Give the same attention to those you see as good and those you see as bad because you don't know when they will be useful to you"));
            proverbsQuizQuestionArray.add(new Proverbs("Nea ɔte fam no nsuro","ahweaseɛ","gyata","obiara","Nea ɔte fam no nsuro ahweaseɛ","The one who is sitting down does not fear falling","If you have done all that is required of you, you don't fear what will come. If you are at your lowest point, you do not fear humiliation"));
            proverbsQuizQuestionArray.add(new Proverbs("Abɔdwesɛ bɛtoo","ani ntɔn nhwi","ti nhwi","nipadua","Abɔdwesɛ bɛtoo ani ntɔn nhwi","The eyebrow was there before the beard came","No matter how high your current position is, you have to give respect to the elderly"));
            proverbsQuizQuestionArray.add(new Proverbs("Koterɛ nnkɔdi mako mma","mfifire nkɔfiri apɔnkyerɛni","ne ho nkyere no","obi nhu","Koterɛ nnkɔdi mako mma mfifire nkɔfiri apɔnkyerɛni", "If the lizard consumes pepper, it's not the frog that sweats","The one who commits a crime must bear his own punishment"));
            proverbsQuizQuestionArray.add(new Proverbs("Obi ntɔn n'akokɔ","bedeɛ kwa","mma ne tamfo","a ne bo yɛ den","Obi ntɔn n'akokɔ bedeɛ kwa","No one sells his laying hen for nothing","There is no action without cause. If there is nothing wrong, no one will sell his valuable property"));
            proverbsQuizQuestionArray.add(new Proverbs("Abaa nna hɔ mma","kraman nnka nipa","ɔkorɔmfo ndwane","nnipa nsu","Abaa nna hɔ mma kraman nnka nipa","The stick should not lie idle while a dog bites a human","Use your resources for your good. Don't keep money and end up dying from a minor ailment"));


            //Not recorded
           // proverbsQuizQuestionArray.add(new Proverbs("Abaa nna hɔ mma","kraman nnka nipa","ɔkorɔmfo ndwane","nnipa nsu","","",""));

            proverbsQuizQuestionArray.add(new Proverbs("Eti wɔ hɔ a","yɛnhyɛ kotodwe kyɛ","fa dwene","wuhu nyansa","Eti wɔ hɔ a yɛnhyɛ kotodwe kyɛ","If the head is availble we do not put a hat on the knee","Give honour to who is due. Give privileges to those who can handle it better"));
            proverbsQuizQuestionArray.add(new Proverbs("Ɔhɔhoɔ behu mpoatwa a"," na kuroman ni na akyerɛ no","na ne suban nyɛ","na wasua no yiye","Ɔhɔhoɔ behu mpoatwa a, na kuroman ni na akyerɛ no","If a foreigner knows how to insult, it is a citizen who has taught him","New comers learn bad traits from ones they came to meet."));
            proverbsQuizQuestionArray.add(new Proverbs("Ɛnyɛ ɔdehye biara na","ɔfata kyinneɛ ase","ahenni no","na ne ho yɛ fɛ","Ɛnyɛ ɔdehye biara na ɔfata kyinneɛ ase","It is not every royal who deserves an umbrella over him","Having the birthright or legal right to receiving honor does not automatically mean you will be praised or pampered especially if your behaviour is not fitting"));
            proverbsQuizQuestionArray.add(new Proverbs("Nkurow dɔɔso a","yɛntena faako nnye animguase","nnipa dɔɔso","ɔkannifo brɛ","Nkurow dɔɔso a yɛntena faako nnye animguase","If there are many cities, we do not stay in one and be shamed","If you fail in something you should try other avenues. Move from where you have been disgraced to where you will not be shamed or disgraced"));
            proverbsQuizQuestionArray.add(new Proverbs("Wo se akyi nnyɛ wo dɛ a","ɛhɔ a na wotafere","twitwi hɔ yie","ɛmfa obiara ho","Wo se akyi nnyɛ wo dɛ a, ɛhɔ a na wotafere","If the back of your tooth is not sweet to you, that is the exact place you lick","We tend to dwell on tends that hurt us or things we don't like. We spend more time to fix things we don't like"));
            proverbsQuizQuestionArray.add(new Proverbs("Dua koro ntumi","nyɛ kwae","nku nnipa","mma aduane","Dua koro ntumi nyɛ kwae","One tree cannot be a forest","One person cannot do it all. We all need help"));
            proverbsQuizQuestionArray.add(new Proverbs("Sotorɔ a ɛbɛn wo no","yegye no ntɛm","ɛyɛ ya paa","dwane fi ho","Sotorɔ a ɛbɛn wo no, yegye no ntɛm","A slap which is close to you is to be received quickly","If something bad will inevitably happen to you, it is better to face it quickly than to try to delay it."));
            proverbsQuizQuestionArray.add(new Proverbs("Baabiara nni hɔ a wusie befi a","egya nni hɔ","ɛhɔ adwo","ɛhɔ nhye","Baabiara nni hɔ a wusie befi a egya nni hɔ","There is no where that you will find smoke without fire","There is always a root cause to a problem"));
            proverbsQuizQuestionArray.add(new Proverbs("Ntɛtea betwa nsuo a","na efi dua","gye sɛ ɔnante ntɛm","ɔkasa","Ntɛtea betwa nsuo a, na efi dua","If the ant will cross a river, then it's because of a tree or wood","Someone is only able to overcome an unsurmountable challenge only with help from someone or something"));
            proverbsQuizQuestionArray.add(new Proverbs("Sɛ yɛhwɛ nea etuo ayɛ a","nka yenni ano nam da","ɛnyɛ koraa","anka yɛnkɔ ko bio","Sɛ yɛhwɛ nea etuo ayɛ a, anka yenni ano nam da","If we consider what guns have caused then we would never eat an animal that has been brought down by a gun","If you take into consideration all the bad that a person has committed you will not take any good from him so at times we have to forget the evil others have done"));
            proverbsQuizQuestionArray.add(new Proverbs("Sɛ tipae ba kurom a","kɔtɔ nka ho","obiara werɛ how","sika ho kyere no","Sɛ tipae ba kurom a, kɔtɔ nka ho","If there is headahe in town, the crab is not part","If there is a case to be investigated, it is only suspects who should be interrogated"));
            proverbsQuizQuestionArray.add(new Proverbs("Sɛ wokɔto aboa kraman wɔ dua so a","na ɛnyɛ ɔno ara na ɔforoe na mmom ɔdesani na apagyaw no asi so","boa no ma onsi fam","ɛyɛ asumansɛm","Sɛ wokɔto aboa kraman wɔ dua so a, na ɛnyɛ ɔno ara na ɔforoe na mmom ɔdesani na apagyaw no asi so","If you see a dog on top of a tree, it did not climb the tree by itself but rather a human put it there","If someone is able to do something he does not have access to, then someone who has access to it is involved."));
            proverbsQuizQuestionArray.add(new Proverbs("Sɛ nsuo fa wo a","wunnyae nsuo nom","ɛsɛ sɛ woyɛ ntɛm pue fi mu","daa wode ka asɛm","Sɛ nsuo fa wo a, wunnyae nsuo nom","If you get drowned in water, you do not stop drinking water","A bad experience with something(or someone) does not mean you should not use it again if there are benefits"));
            proverbsQuizQuestionArray.add(new Proverbs("Nea ade yɛ ne dea no","ɔde benkum na ɛgye","ɔde ma ne maame","ɔde sie","Nea ade yɛ ne dea no ɔde benkum na ɛgye","He who owns something collects it with his left hand","The one owns something does not need to thank you if you return it. He has the right to get it from you anyway he wishes"));
            proverbsQuizQuestionArray.add(new Proverbs("Nea odi akyiri sua nea odi kan","nanteɛ","kasa","ho nsɛm","Nea odi akyiri sua nea odi kan nanteɛ","He one behind learns the walking style of the one in front","The young learns their traits and habits from the old who trained them"));
            proverbsQuizQuestionArray.add(new Proverbs("Baabi a yedidi no","yɛnsɛe hɔ","yɛfrɛ hɔ adidibea","ɛhɔ na obiara pɛ","Baabi a yedidi no yɛnsɛe hɔ","We do not spoil where we eat","Make sure that you are always at peace with the people who provide your needs for you"));
            //  proverbsArrayList.add(new Proverbs("Baabi a nkosua gu no yɛnto boɔ nkɔ hɔ","We do not throw stones to where there are eggs",""));
            proverbsQuizQuestionArray.add(new Proverbs("Aboa apɔnkyerɛni na ɛkaa sɛ wobaa me fie yɛkotow hɔ","wo nso rebisa me akonwa","ɛnti gyina hɔ","","Aboa apɔnkyerɛni na ɛkaa sɛ wobaa me fie yɛkotow hɔ, wo nso rebisa me akonwa","The frog says that when you came to my house you saw us squatting and you are asking me for a chair","Do not from others things which are obvious that the don't have. A good guest does not ask more than the host himself uses"));
            proverbsQuizQuestionArray.add(new Proverbs("Etua wo yɔnko a","etua dua mu","ɛnyɛ ya","bɔ mpae ma no","Etua wo yɔnko a etua dua mu","If it is pierced into your friend then it is pierced into a tree","You do not feel the pain if you are not the one suffering"));
            //proverbsArrayList.add(new Proverbs("Yɛma wo ɔhene na wɔanni a, ahenkwaa bɔ wo","",""));
            proverbsQuizQuestionArray.add(new Proverbs("Sɛ ehia wo na ehia wo hia mu boafo a","ɛnna na awie wo hia","ɛbɛyɛ daakye","su frɛ Onyankopɔn","Sɛ ehia wo na ehia wo hia mu boafo a, ɛnna na awie wo hia","It is when you are poor and the one who bails you is also poor then you have ended up poor","If you are poor and you have someone to help you, then there is hope"));
            proverbsQuizQuestionArray.add(new Proverbs("Ɛkɔm de wo a ɛnkyerɛ sɛ","fa wo nsa mmienu nyinaa didi","worebewu","didi ntɛm","Ɛkɔm de wo a ɛnkyerɛ sɛ fa wo nsa mmienu nyinaa didi","If you are hungry it doesn't mean you should eat with your two hands","Even if you need something so much you should still follow the right procedure to get it"));
            proverbsQuizQuestionArray.add(new Proverbs("Mogya mu yɛ du sen","nsuo","nsa","nkwan","Mogya mu yɛ du sen nsuo","Blood is thicker than water","Your blood relatives are more dear to you than your friends"));
            proverbsQuizQuestionArray.add(new Proverbs("Wuhu nea ɔbɛsɛe wo maame ayie a","na wode ayie no ahyɛ ne nsa","frɛ polisifo","mma no mma ayie no bi","Wuhu nea ɔbɛsɛe wo maame ayie a, na wode ayie no ahyɛ ne nsa","If you notice someone who will make trouble at your mum's funeral, let that person handle the funeral arrangements","If you suspect someone of sabotaging or stealing something, it is best to make that person responsible for that thing"));
            proverbsQuizQuestionArray.add(new Proverbs("Baabi a ɔsono bɛfa biara","yɛ kwan","mmoa di n'akyi","obi mfa hɔ","Baabi a ɔsono bɛfa biara yɛ kwan","Where ever the elephant passes is a way","A powerful person can make a way where there seems to be no way"));
            proverbsQuizQuestionArray.add(new Proverbs("Dua koro yi ara","sɛ woforo si a wasi na wote hwe nso a wasi","wutumi de yɛ aduro","etumi ma onwunu","Dua koro yi ara, sɛ woforo si a wasi na wote hwe nso a wasi","This same tree, if you climb up and climb down, you are down and similarly if you fall down from the tree you are also down","The end of a matter is what matters most"));



        }



        //generateQuestion();
    }

    @Override
    protected void onDestroy() {

        Log.i("Hi1","Came");
        if (handler1 !=null){
            Log.i("Hi1","Came1");
            handler1.removeCallbacks(ranable);
        }
        if (playFromDevice!=null){
            playFromDevice.stop();
        }

        if (Sub==0){
            advert1();
        }

        proverbsQuizQuestionArray.clear();
        super.onDestroy();
    }
}
