package com.learnakantwi.twiguides;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import java.util.ArrayList;
import java.util.Random;

import static com.learnakantwi.twiguides.AllActivity.allArrayList;
import static com.learnakantwi.twiguides.ProverbsActivity.proverbsArrayList;

public class RepeatingActivityProverbs extends AppCompatActivity {

    Random random;
    TextView textView;
    TextView textTwi;
    //Switch dailyTwiSwitch;
    //CheckBox dailyTwiSwitch;
    ArrayList<DaysOfWeek> answers;
    int randomChoiceQuestion;
    String english1;
    String twi1;
    StorageReference storageReference;
    MediaPlayer mp1;
    MediaPlayer playFromDevice;
    Toast toast;
    int testShared;


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //MenuInflater menuInflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.main_menu_wordoftheday, menu);
        return super.onCreateOptionsMenu(menu);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {

            case R.id.main:
                //Log.i("Menu Item Selected", "Alphabets");
                goToMain();
                return true;
            case R.id.quiz1:
                //Log.i("Menu Item Selected", "Alphabets");
                goToQuizAll();
                return true;
           /* case R.id.advert:
                advert();
                return true;*/
            case R.id.videoCourse:
                //Log.i("Menu Item Selected", "Alphabets");
                goToWeb();
                return true;
          /*  case R.id.dailyNotification:
                //Log.i("Menu Item Selected", "Alphabets");
                notificationPreference();
                return true;*/
            default:
                return false;
        }
    }

    public void goToMain(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    public void goToQuizAll() {
        Intent intent = new Intent(getApplicationContext(), QuizAll.class);
        startActivity(intent);
    }

    public void goToWeb() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.udemy_course)));
        startActivity(intent);
    }

    public void generateQuestion(){

        random = new Random();
        answers = new ArrayList<>();

        randomChoiceQuestion = random.nextInt(proverbsArrayList.size()-1);

       // english1 = allArrayList.get(randomChoiceQuestion).getEnglishmain();
        english1 =  proverbsArrayList.get(0).getProverbMeaning();


       // twi1 = allArrayList.get(randomChoiceQuestion).getTwiMain();
        twi1= proverbsArrayList.get(0).getTwiProverb();


        textView.setText(english1);
        textTwi.setText(twi1);

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

    public void playFromFileOrDownload(final String filename, final String appearText){
        File myFile = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/PROVERBS/"+filename+ ".m4a");
        if (myFile.exists()){

            try {
                if (playFromDevice != null){
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
                        toast.setText(appearText);
                        toast.show();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {

            if (isNetworkAvailable()){
                final StorageReference musicRef = storageReference.child("/Proverbs/" + filename + ".m4a");
                musicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String url = uri.toString();
                        playFromFirebase(musicRef);
                        downloadFile(getApplicationContext(), filename, ".m4a", url);
                        toast.setText(appearText);
                        toast.show();
                        //Toast.makeText(getApplicationContext(), appearText, Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "No Internet", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else {
                toast.setText("Please connect to Internet to download audio");
                toast.show();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repeating_proverbs);

        isNetworkAvailable();


        toast = Toast.makeText(getApplicationContext(), "Twi for the day. Tap to Listen", Toast.LENGTH_SHORT);
        toast.show();

        textView = findViewById(R.id.randomText);
        textTwi = findViewById(R.id.randomText1);
//        dailyTwiSwitch = findViewById(R.id.dailyTwiSwitch);


      /*  dailyTwiSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificationPreference();
            }
        });
*/


        storageReference = FirebaseStorage.getInstance().getReference();

        textTwi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int idview = view.getId();

                TextView blabla = view.findViewById(idview);
                String a = (String) blabla.getText();


               String b =  PlayFromFirebase.viewTextConvert(a);

                playFromFileOrDownload(b, a);
            }
        });



        generateQuestion();


        //ca-app-pub-7384642419407303/9880404420 correct
        //ca-app-pub-3940256099942544/1033173712 test

        findViewById(R.id.generate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMain();

                //notificationPreference();

       /* if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }*/
            }

           /* public void onClick(View v) {
                generateQuestion();
            }*/
        });

        SharedPreferences subscribe = getSharedPreferences("AdsDecision",MODE_PRIVATE);
        int sub =  subscribe.getInt("Sub",0);


        if (sub == 0) {
            Appodeal.show(this, Appodeal.BANNER_BOTTOM);
            Appodeal.show(this, Appodeal.BANNER_TOP);
        }
    }
}
