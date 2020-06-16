package com.learnakantwi.twiguides;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
//import android.support.v7.app.AppCompatActivity;
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
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class FamilyActivity extends AppCompatActivity {

    ListView listView;
    static ArrayList<Family> familyArrayList;

    StorageReference storageReference;
    MediaPlayer playFromDevice;
    MediaPlayer mp1;
    AdView mAdView;
    boolean playsoon = true;

    public InterstitialAd mInterstitialAd;
    int showAdProbability;
    Random random;


    Toast toast;

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
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
                //System.out.println("Internet is now not connected 1");
            } catch (IOException e) {
                isRunning=false;
                //System.out.println("Internet is now not connected 2");
            }
        }
    };
    public void downloadOnly(final String filename){
        if (isNetworkAvailable()){

            final StorageReference musicRef = storageReference.child("/AllTwi/" + filename + ".m4a");
            musicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    String url = uri.toString();
                    downloadFile(getApplicationContext(), filename, ".m4a", url);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    toast.setText("Lost Internet Connection");
                    toast.show();
                }
            });
        }
        else {
            toast.setText("Please connect to Internet to download audio");
            toast.show();
        }
    }

   /* public boolean hasInternetAccess() {

        Thread myThread = new Thread(runnable);
        myThread.start();
        return isRunning;
    }*/

    public void downloadClick () {
        int counter = 0;
        int counter1 =0;

        if (isNetworkAvailable()) {
            for (int j = 0; j < familyArrayList.size(); j++) {

                String bb = familyArrayList.get(j).getTwiFamily();
                bb= bb.toLowerCase();
                boolean dd = bb.contains("ɔ");
                boolean ee = bb.contains("ɛ");
                if (dd || ee) {
                    bb = bb.replace("ɔ", "x");
                    bb = bb.replace("ɛ", "q");
                }

                if (bb.contains(" ") || bb.contains("/") || bb.contains(",") || bb.contains("(") || bb.contains(")") || bb.contains("-") || bb.contains("?") || bb.contains("'")) {
                    bb = bb.replace(" ", "");
                    bb = bb.replace("/", "");
                    bb = bb.replace(",", "");
                    bb = bb.replace("(", "");
                    bb = bb.replace(")", "");
                    bb = bb.replace("-", "");
                    bb = bb.replace("?", "");
                    bb = bb.replace("'", "");
                }
                File myFiles = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/" + bb + ".m4a");
                if (myFiles.exists()) {
                    counter++;
                }
            }
            if (counter == familyArrayList.size()) {
                toast.setText("All downloaded");
                toast.show();

            } else {
                toast.setText("Downloading...");
                toast.show();

                for (int i = 0; i < familyArrayList.size(); i++) {
                    String b = familyArrayList.get(i).getTwiFamily().toLowerCase();
                    boolean d = b.contains("ɔ");
                    boolean e = b.contains("ɛ");
                    if (d || e) {
                        b = b.replace("ɔ", "x");
                        b = b.replace("ɛ", "q");
                    }

                    if (b.contains(" ") || b.contains("/") || b.contains(",") || b.contains("(") || b.contains(")") || b.contains("-") || b.contains("?") || b.contains("'")) {
                        b = b.replace(" ", "");
                        b = b.replace("/", "");
                        b = b.replace(",", "");
                        b = b.replace("(", "");
                        b = b.replace(")", "");
                        b = b.replace("-", "");
                        b = b.replace("?", "");
                        b = b.replace("'", "");
                    }

                    File myFile = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/" + b + ".m4a");
                    if (!myFile.exists()) {
                        if (isNetworkAvailable()){
                            downloadOnly(b);
                        }
                        else{
                            toast.setText("Please connect to the Internet");
                            toast.show();
                            break;
                        }

                    }

                }

            }
        }
        else{
            toast.setText("Please connect to the Internet to download audio");
            toast.show();
        }
    }

    public void playFromFirebase(StorageReference musicRef) {
        playsoon =true;

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
                                                if (!mp1.isPlaying() && !playFromDevice.isPlaying()) {
                                                    mp.start();
                                                }
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
                            toast.setText("Unable to download now. Please try later");
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
        File myFile = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/"+filename+ ".m4a");
        if (myFile.exists()){



            try {
                if (playFromDevice != null){
                    playFromDevice.pause();
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
                final StorageReference musicRef = storageReference.child("/AllTwi/" + filename + ".m4a");
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
        getMenuInflater().inflate(R.menu.main_menu, menu);

        final MenuItem item = menu.findItem(R.id.menusearch);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(FamilyActivity.this, query, Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<Family> results = new ArrayList<>();
                for (Family x: familyArrayList ){

                    if(x.getEnglishFamily().toLowerCase().contains(newText.toLowerCase()) || x.getTwiFamily().toLowerCase().contains(newText.toLowerCase())){
                        results.add(x);
                    }

                    ((FamilyAdapter)listView.getAdapter()).update(results);
                }


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
                goToMain();
                return  true;
            case R.id.quiz1:
                goToQuizFamily();
                return  true;
            case R.id.downloadAudio:
                downloadClick();
                return true;
            case R.id.videoCourse:
                //Log.i("Menu Item Selected", "Alphabets");
                goToWeb();
                return  true;
            default:
                return false;
        }
    }


    public void goToMain(){
        Intent intent = new Intent(getApplicationContext(), HomeMainActivity.class);
        startActivity(intent);
    }
    public void goToWeb() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.udemy.com/course/learn-akan-twi/?referralCode=6D321CE6AEE1834CCB0F"));
        startActivity(intent);
    }


    public void goToQuizFamily() {
        Intent intent = new Intent(getApplicationContext(), QuizFamily.class);
        startActivity(intent);
    }

    public void log2(View view) {
        int idview = view.getId();

        TextView blabla = view.findViewById(idview);
        String a = (String) blabla.getText();

        toast.setText(a);
        toast.show();

       /* if (isNetworkAvailable()){
            Toast.makeText(this, "There is Internet 1", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "No Internet 1", Toast.LENGTH_SHORT).show();
        }

        if (isNetworkAvailable()){
            toast.setText("There is Internet 2");
            toast.show();
        }
        else{
            toast.setText("No Internet 2");
            toast.show();

        }*/
    }

    public void timeClick(View view){


        int idview= view.getId();

        TextView blabla = view.findViewById(idview);
        String a = (String) blabla.getText();

        String b = a.toLowerCase();

        boolean d = b.contains("ɔ");
        boolean e = b.contains("ɛ");

        if (d || e ){
            b= b.replace("ɔ","x");
            b= b.replace("ɛ","q");
        }

        if (b.contains(" ") || b.contains("/") || b.contains(",") || b.contains("'")) {
            b = b.replace(" ", "");
            b = b.replace("/", "");
            b= b.replace(",","");
            b= b.replace("'","");
        }
        playFromFileOrDownload(b,a);

    }

    /*public void advert1() {

        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-7384642419407303/9880404420");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        int showAdProbability;
        InterstitialAd mInterstitialAd;
        Random random;

        mInterstitialAd = new InterstitialAd(this);
        // mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.setAdUnitId("ca-app-pub-7384642419407303/9880404420");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        random = new Random();
        showAdProbability = random.nextInt(10);

        @Override
        protected void onDestroy() {
            if (showAdProbability<=2){
                advert1();
            }
            super.onDestroy();
        }
    }*/

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
        setContentView(R.layout.activity_family);

        mInterstitialAd = new InterstitialAd(this);
        // mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.setAdUnitId("ca-app-pub-7384642419407303/9880404420");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        random = new Random();
        showAdProbability = random.nextInt(10);

        playFromDevice = new MediaPlayer();
        mp1 = new MediaPlayer();


        toast = Toast.makeText(getApplicationContext(), "Tap to Listen" , Toast.LENGTH_LONG);
        toast.show();

        isNetworkAvailable();


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);



        listView = findViewById(R.id.familyListView);
        storageReference = FirebaseStorage.getInstance().getReference();


        FamilyAdapter familyAdapter = new FamilyAdapter(this, familyArrayList);
        listView.setAdapter(familyAdapter);

    }


    public void stopPlay () {

        toast.cancel();

        if (!playsoon){

        }

            if (playFromDevice.isPlaying()) {

                playFromDevice.pause();
                playFromDevice.reset();
                playFromDevice.release();
            }


        if (mp1.isPlaying()) {
            mp1.pause();
            mp1.reset();
            mp1.release();
        }
    }
      /*  catch (NullPointerException e){
            System.out.println("Null Pointer 1");
        }*/


    @Override
    protected void onDestroy() {
        stopPlay();
        if (showAdProbability<=5){
            advert1();
        }
        super.onDestroy();
    }


   /* public void stopPlay (){

        toast.cancel();
        if (playFromDevice != null) {
            playFromDevice.release();
        }

        if (mp1 != null) {
            mp1.release();
        }
    }

    @Override
    protected void onDestroy() {
        stopPlay();
        super.onDestroy();
    }

    @Override
    protected void onUserLeaveHint() {
        stopPlay();
        super.onUserLeaveHint();
    }*/
}
