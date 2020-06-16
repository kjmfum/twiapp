package com.learnakantwi.twiguides;

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
import android.view.animation.Animation;
import android.widget.AdapterViewFlipper;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class ChildrenNumbersActivityRV extends AppCompatActivity implements ChildrenAdapterRV.onClickRecycle{



   public static  ArrayList<Children> lesson;
 /*   TextView tvTwi;
    TextView tvEnglish;*/
    public InterstitialAd mInterstitialAd;
RecyclerView recyclerView;
ChildrenAdapterRV myAdapter;
AdapterViewFlipper lessonAdapterView;
    Animation shake;
    Random random;
    int ranNumber;
   ArrayList<Children> recycleArrayList;
    MediaPlayer playFromDevice;
    MediaPlayer mp1;
    ArrayList<Animals> childrenImageAnimals;
    Toast toast;
    StorageReference storageReference;
    int Ads;
    AdView mAdView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //MenuInflater menuInflater = getMenuInflater();

      //  getMenuInflater().inflate(R.menu.main_menu, menu);
        getMenuInflater().inflate(R.menu.main_menu_shared, menu);

        final MenuItem item = menu.findItem(R.id.menusearch);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(getApplicationContext(), query, Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                myAdapter.getFilter().filter(newText);

             /*   ArrayList<Animals> results = new ArrayList<>();
                for (Animals x: childrenAnimalsArrayList ){

                    if(x.getEnglishAnimals().toLowerCase().contains(newText.toLowerCase()) || x.getTwiAnimals().toLowerCase().contains(newText.toLowerCase())){
                        results.add(x);
                    }

                   // ((ChildrenAnimalsAdapter)lvChildrenAnimals.getAdapter()).update(results);
                    RecyclerViewAdapter.update(results);
                }

*/
                return false;
            }
        });



        return super.onCreateOptionsMenu(menu);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()){

            case R.id.main:
                //Log.i("Menu Item Selected", "Alphabets");
                goToMain();
                return  true;
            case R.id.quiz1:
                //Log.i("Menu Item Selected", "Alphabets");
                // goToQuizAnimals();
                return  true;
            case R.id.videoCourse:
                //Log.i("Menu Item Selected", "Alphabets");
                goToWeb();
                return true;
            /*case R.id.downloadAudio:
               // downloadClick();
                return true;*/
            default:
                return false;
        }
    }

    public void goToWeb() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.udemy.com/course/learn-akan-twi/?referralCode=6D321CE6AEE1834CCB0F"));
        startActivity(intent);
    }


    public void goToMain(){
        if (Ads != 0){
            Intent intent = new Intent(getApplicationContext(), HomeMainActivity.class);
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(getApplicationContext(), SubPHomeMainActivity.class);
            startActivity(intent);
        }
    }




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
        } else {
            toast.setText("Please Connect to the Internet");
            toast.show();
        }

    }

    public void playFromFileOrDownload(final String filename) {

        //Toast.makeText(this, "I'm here o", Toast.LENGTH_SHORT).show();

        File myFile = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/CHILDREN/" + filename + ".m4a");
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
                       /* toast.setText(appearText);
                        toast.show();*/
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            if (isNetworkAvailable()) {
                final StorageReference musicRef = storageReference.child("/Children/" + filename + ".m4a");
                musicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String url = uri.toString();
                        playFromFirebase(musicRef);
                        downloadFile(getApplicationContext(), filename, ".m4a", url);
                        /*toast.setText(appearText);
                        toast.show();*/
                        //Toast.makeText(getApplicationContext(), appearText, Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "No Internet", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
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
                    request.setDestinationInExternalFilesDir(getApplicationContext(), Environment.DIRECTORY_MUSIC + "/CHILDREN", filename + fileExtension);
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

   /* MediaPlayer playFromDevice;
    MediaPlayer mp1;

    ArrayList<Animals> childrenImageAnimals;

    Toast toast;

    StorageReference storageReference;*/

    ///
 /*   toast= Toast.makeText(this, "", Toast.LENGTH_SHORT);
    storageReference = FirebaseStorage.getInstance().getReference();*/

    @Override
    public void onMyItemClick(int position, View view) {

        storageReference = FirebaseStorage.getInstance().getReference();

        String twiAnimals = recycleArrayList.get(position).getEnglishChildren();

        String b= PlayFromFirebase.viewTextConvert(twiAnimals);
        playFromFileOrDownload(b);

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                return false;
            }
        });
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

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_children_numbers_rv);

        random = new Random();

        ranNumber = random.nextInt(11);

        final SharedPreferences sharedPreferencesAds = this.getSharedPreferences("AdsDecision", MODE_PRIVATE);
        Ads = sharedPreferencesAds.getInt("Ads", 5);

        if (Ads != 0) {
            mInterstitialAd = new InterstitialAd(this);
            mInterstitialAd.setAdUnitId("ca-app-pub-7384642419407303/9880404420");
            mInterstitialAd.loadAd(new AdRequest.Builder().build());

           // Toast.makeText(this, "Hey", Toast.LENGTH_SHORT).show();
            MobileAds.initialize(this, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {
                }
            });
            mAdView = findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        }

        toast= Toast.makeText(this, "", Toast.LENGTH_SHORT);

        lesson = new ArrayList<>();

        lesson.add(new Children("1", "BAAKO"));
        lesson.add(new Children("2", "MMIENU"));
        lesson.add(new Children("3", "MMIƐNSA"));
        lesson.add(new Children("4", "ƐNAN"));
        lesson.add(new Children("5", "ENUM"));
        lesson.add(new Children("6", "NSIA"));
        lesson.add(new Children("7", "NSON"));
        lesson.add(new Children("8", "NWƆTWE"));
        lesson.add(new Children("9", "NKRON"));
        lesson.add(new Children("10", "DU"));
        lesson.add(new Children("11", "DUBAAKO"));
        lesson.add(new Children("12", "DU MMIENU"));
        lesson.add(new Children("13", "DU MMIƐNSA"));
        lesson.add(new Children("14", "DU NAN"));
        lesson.add(new Children("15", "DU NUM"));
        lesson.add(new Children("16", "DU NSIA"));
        lesson.add(new Children("17", "DU NSON"));
        lesson.add(new Children("18", "DU NWƆTWE"));
        lesson.add(new Children("19", "DU NKRON"));
        lesson.add(new Children("20", "ADUONU"));


        recycleArrayList = new ArrayList<>();
        recycleArrayList.addAll(lesson);

        recyclerView = findViewById(R.id.recyclerView);

        // myAdapter = new LessonsAdapter(this, recycleArrayList, this);

        myAdapter = new ChildrenAdapterRV(this,recycleArrayList,this);

        recyclerView.setAdapter(myAdapter);

          GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
          recyclerView.setLayoutManager(gridLayoutManager);

    }
    @Override
    protected void onDestroy() {

       if (ranNumber<11){
           if (Ads != 0){
              // Toast.makeText(this, "Display!!!", Toast.LENGTH_SHORT).show();
               advert1();
           }
         /*  else{
               Toast.makeText(this, "No Way!!!", Toast.LENGTH_SHORT).show();
           }*/
       }
        super.onDestroy();
    }
}

