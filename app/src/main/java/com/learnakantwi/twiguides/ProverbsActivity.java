package com.learnakantwi.twiguides;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.AdapterViewFlipper;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

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

public class ProverbsActivity extends AppCompatActivity {

    static ArrayList<Proverbs> proverbsArrayList = new ArrayList<>();
    // AdView mAdView;
    AdView mAdView1;
    AdapterViewFlipper proverbsViewFlipper;
    StorageReference storageReference;
    MediaPlayer playFromDevice;
    MediaPlayer mp1;
    Toast toast;
    ArrayList<Proverbs> results = new ArrayList<>();
    ArrayList<Proverbs> results1 = new ArrayList<>();

    public InterstitialAd mInterstitialAd;
    Random random;

    int showAdProbability;


    public void advert() {

        final SharedPreferences sharedPreferences = this.getSharedPreferences("com.learnakantwi.twiguides", Context.MODE_PRIVATE);
        //  sharedPreferences.edit().putString("AdvertPreference", "No").apply();
        String advertPreference = sharedPreferences.getString("AdvertPreference", "No");

        assert advertPreference != null;
        if (!advertPreference.equals("Yes")) {
            new AlertDialog.Builder(this)
                    .setIcon(R.drawable.learnakantwiimage)
                    .setTitle("Please support us")
                    .setMessage("Would You Like To View An Advert To Support Us?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sharedPreferences.edit().putString("AdvertPreference", "Yes").apply();
                            if (mInterstitialAd.isLoaded()) {
                                mInterstitialAd.show();
                            } else {
                                Log.d("TAG", "The interstitial wasn't loaded yet.");
                            }
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    sharedPreferences.edit().putString("AdvertPreference", "No").apply();
                                }
                            }
                    )
                    .show();
        } else {
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();

            } else {
                Log.d("TAG", "The interstitial wasn't loaded yet.");

            }
        }

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-7384642419407303/9880404420");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }

    public void advert1() {

        /*final SharedPreferences sharedPreferences = this.getSharedPreferences("com.learnakantwi.twiguides", Context.MODE_PRIVATE);
        //  sharedPreferences.edit().putString("AdvertPreference", "No").apply();
        String advertPreference = sharedPreferences.getString("AdvertPreference", "No");

        assert advertPreference != null;
        if (advertPreference.equals("Yes")) {*/

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


    public void advertDialog() {

        final SharedPreferences sharedPreferences = this.getSharedPreferences("com.learnakantwi.twiguides", Context.MODE_PRIVATE);
        //  sharedPreferences.edit().putString("AdvertPreference", "No").apply();
        String advertPreference = sharedPreferences.getString("AdvertPreference", "No");

        assert advertPreference != null;
        if (!advertPreference.equals("Yes")) {
            new AlertDialog.Builder(this)
                    .setIcon(R.drawable.learnakantwiimage)
                    .setTitle("Please support us")
                    .setMessage("Would You Like To View An Advert Later To Support Us?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sharedPreferences.edit().putString("AdvertPreference", "Yes").apply();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    sharedPreferences.edit().putString("AdvertPreference", "No").apply();
                                }
                            }
                    )
                    .show();
        }

       // sharedPreferences.edit().putString("AdvertPreference", "No").apply();

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

    public void proverbClick(View view){



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


        if (b.contains(" ") ||b.contains("twi:") || b.contains("/") || b.contains(";")|| b.contains("'")|| b.contains(",") || b.contains("(") || b.contains(")") || b.contains("-") | b.contains("?")| b.contains("...")) {
            b = b.replace(" ", "");
            b = b.replace("/", "");
            b= b.replace(",","");
            b= b.replace("(","");
            b= b.replace(")","");
            b= b.replace("-","");
            b= b.replace("?","");
            b= b.replace("...","");
            b= b.replace("twi:","");
            b= b.replace("'","");
            b= b.replace(";","");
        }

       // toast.setText(b);
        //toast.show();

        playFromFileOrDownload(b, a);

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        //MenuInflater menuInflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.main_menu_proverbs1, menu);

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
                results1.add(new Proverbs("","",""));

                results = new ArrayList<>();
                for (Proverbs x: proverbsArrayList ){

                    if(x.getTwiProverb().toLowerCase().contains(newText.toLowerCase()) || x.getProverbLiteral().toLowerCase().contains(newText.toLowerCase())
                            || x.getProverbMeaning().toLowerCase().contains(newText.toLowerCase())

                    ){
                        results.add(x);
                    }
                    if (results.size()>0) {
                        ((ProverbsAdapter) proverbsViewFlipper.getAdapter()).update(results);
                    }
                    else{
                        ((ProverbsAdapter) proverbsViewFlipper.getAdapter()).update(results1);
                    }
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
                //Log.i("Menu Item Selected", "Alphabets");
                goToMain();
                return  true;
          case R.id.proverbsSlideShow:
                slideshow();
                return  true;
            case R.id.downloadAllAudio:
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

    public void goToWeb() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.udemy.com/course/learn-akan-twi/?referralCode=6D321CE6AEE1834CCB0F"));
        startActivity(intent);
    }

    public void downloadClick () {
        int counter = 0;

        /*toast.setText("Got here");
        toast.show();*/

        if (isNetworkAvailable()) {
            for (int j = 0; j < proverbsArrayList.size(); j++) {

                String bb = proverbsArrayList.get(j).getTwiProverb();
                bb= bb.toLowerCase();
                boolean dd = bb.contains("ɔ");
                boolean ee = bb.contains("ɛ");
                if (dd || ee) {
                    bb = bb.replace("ɔ", "x");
                    bb = bb.replace("ɛ", "q");
                }

                if (bb.contains(" ") ||bb.contains("twi:") || bb.contains("/") || bb.contains(";")|| bb.contains("'")|| bb.contains(",") || bb.contains("(") || bb.contains(")") || bb.contains("-") | bb.contains("?")| bb.contains("...")) {
                    bb = bb.replace(" ", "");
                    bb = bb.replace("/", "");
                    bb= bb.replace(",","");
                    bb= bb.replace("(","");
                    bb= bb.replace(")","");
                    bb= bb.replace("-","");
                    bb= bb.replace("?","");
                    bb= bb.replace("...","");
                    bb= bb.replace("twi:","");
                    bb= bb.replace("'","");
                    bb= bb.replace(";","");
                }
                File myFiles = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/PROVERBS/"+bb+ ".m4a");
                if (myFiles.exists()) {
                    counter++;
                }

            }
            if (counter == proverbsArrayList.size()) {
                toast.setText("All downloaded");
                toast.show();

            } else {
                toast.setText("Downloading...");
                toast.show();

                // toast.setText(counter + " -- "+ allArrayList.size());
                // toast.show();

                for (int i = 0; i < proverbsArrayList.size(); i++) {
                    String b = proverbsArrayList.get(i).getTwiProverb().toLowerCase();
                    boolean d = b.contains("ɔ");
                    boolean e = b.contains("ɛ");
                    if (d || e) {
                        b = b.replace("ɔ", "x");
                        b = b.replace("ɛ", "q");
                    }

                    if (b.contains(" ") ||b.contains("twi:") || b.contains("/") || b.contains(";")|| b.contains("'")|| b.contains(",") || b.contains("(") || b.contains(")") || b.contains("-") | b.contains("?")| b.contains("...")) {
                        b = b.replace(" ", "");
                        b = b.replace("/", "");
                        b= b.replace(",","");
                        b= b.replace("(","");
                        b= b.replace(")","");
                        b= b.replace("-","");
                        b= b.replace("?","");
                        b= b.replace("...","");
                        b= b.replace("twi:","");
                        b= b.replace("'","");
                        b= b.replace(";","");
                    }

                    File myFile = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/PROVERBS" + b + ".m4a");
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

    public void downloadOnly(final String filename){
        if (isNetworkAvailable()){
            final StorageReference musicRef = storageReference.child("/Proverbs/" + filename + ".m4a");
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
        checkFileinFolder();
    }
    public boolean checkFileinFolder() {

        int counter = 0;

        for (int j = 0; j < proverbsArrayList.size(); j++) {

            String bb = proverbsArrayList.get(j).getTwiProverb();
            bb = bb.toLowerCase();
            boolean dd = bb.contains("ɔ");
            boolean ee = bb.contains("ɛ");
            if (dd || ee) {
                bb = bb.replace("ɔ", "x");
                bb = bb.replace("ɛ", "q");
            }

            if (bb.contains(" ") || bb.contains("twi:") || bb.contains("/") || bb.contains(";") || bb.contains("'") || bb.contains(",") || bb.contains("(") || bb.contains(")") || bb.contains("-") | bb.contains("?") | bb.contains("...")) {
                bb = bb.replace(" ", "");
                bb = bb.replace("/", "");
                bb = bb.replace(",", "");
                bb = bb.replace("(", "");
                bb = bb.replace(")", "");
                bb = bb.replace("-", "");
                bb = bb.replace("?", "");
                bb = bb.replace("...", "");
                bb = bb.replace("twi:", "");
                bb = bb.replace("'", "");
                bb = bb.replace(";", "");
            }
            File myFiles = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/PROVERBS/"+bb+ ".m4a");
            if (myFiles.exists()) {
                counter++;
            }

        }
        if (counter == proverbsArrayList.size()) {
            toast.setText("Download Completed");
            toast.show();
            return true;
        }
        else {
            return false;
        }
    }



    public void goToMain(){
        Intent intent = new Intent(getApplicationContext(), HomeMainActivity.class);
        startActivity(intent);
    }


   public void next(View view){
       //proverbsViewFlipper.stopFlipping();
        proverbsViewFlipper.showNext();
       // int yes = proverbsViewFlipper.indexOfChild(proverbsViewFlipper.getCurrentView());
   }

    public void previous (View view){
        proverbsViewFlipper.showPrevious();
    }

    public void slideshow() {
        proverbsViewFlipper.showNext();
        proverbsViewFlipper.startFlipping();
        proverbsViewFlipper.setFlipInterval(5000);

        //proverbsViewFlipper.getChildCount();
    }

    public void slideshow(View view) {
        proverbsViewFlipper.showNext();
        proverbsViewFlipper.startFlipping();
        proverbsViewFlipper.setFlipInterval(5000);
        toast.setText("Slides change every 5 seconds");
        toast.show();

        //proverbsViewFlipper.getChildCount();
    }

    public void pauseSlideshow(View view) {
        if (proverbsViewFlipper.isFlipping()){
            proverbsViewFlipper.stopFlipping();
        }

        //proverbsViewFlipper.getChildCount();
    }


    public void deleteDuplicateProverbs() {
        File myFileProverbs = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/PROVERBS/");
        File[] files2 = myFileProverbs.listFiles();


        for (int i = 0; i < files2.length; i++) {

            File fileConversation = files2[i];
            if (fileConversation.getName().contains("-")) {
                fileConversation.delete();
            }
        }

    }


    /*public void deleteProverbsSection() {
      //  File myFileProverbs = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/PROVERBS/");
        File myFileProverbs = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/");
        File[] files2 = myFileProverbs.listFiles();


        for (int i = 0; i < files2.length; i++) {

            File fileConversation = files2[i];
                fileConversation.delete();
        }

    }*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proverbs);

        try {
            deleteDuplicateProverbs();
        }
        catch (NullPointerException e){
            System.out.println("Error Null");
        }

        random = new Random();
        showAdProbability = random.nextInt(10);

       // advertDialog();


        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-7384642419407303/9880404420");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());


        //ca-app-pub-7384642419407303/9880404420
        //ca-app-pub-3940256099942544/1033173712 test


        storageReference = FirebaseStorage.getInstance().getReference();
        toast = Toast.makeText(getApplicationContext(), " " , Toast.LENGTH_LONG);


        toast = Toast.makeText(getApplicationContext(), "Tap Twi to Listen", Toast.LENGTH_LONG);
        toast.show();

       // toast.setText(Integer.toString(showAdProbability));
        //toast.show();

      /*  MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
*/

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView1 = findViewById(R.id.adView1);
        AdRequest adRequest1 = new AdRequest.Builder().build();
        mAdView1.loadAd(adRequest1);


        proverbsViewFlipper = findViewById(R.id.proverbsAdapterViewFlipper);

        proverbsViewFlipper.setOnTouchListener(new OnSwipeTouchListener(ProverbsActivity.this) {
            public void onSwipeTop() {
            }
            public void onSwipeRight() {
               // Toast.makeText(ProverbsActivity.this, "right", Toast.LENGTH_SHORT).show();
                proverbsViewFlipper.showPrevious();
            }
            public void onSwipeLeft() {
                //Toast.makeText(ProverbsActivity.this, "left", Toast.LENGTH_SHORT).show();
                proverbsViewFlipper.showNext();
            }
            public void onSwipeBottom() {
            }

        });




        ProverbsAdapter proverbsAdapter = new ProverbsAdapter(this, proverbsArrayList);

        proverbsViewFlipper.setAdapter(proverbsAdapter);







    }

    @Override
    protected void onResume() {

        try {
            deleteDuplicateProverbs();
        }
        catch (NullPointerException e){
            System.out.println("Error Null");
        }

        super.onResume();
    }

   public void stopPlay (){
       toast.cancel();
       if (playFromDevice != null) {
           //toast.setText("Not Null");
           //toast.show();
           //playFromDevice.stop();
           //playFromDevice.reset();
           playFromDevice.release();
       }

       if (mp1 != null) {
          // mp1.stop();
           //mp1.reset();
           mp1.release();
       }
   }

    @Override
    protected void onDestroy() {

       // stopPlay();

        if (showAdProbability<=5){
       stopPlay();
            advert1();

        }
        super.onDestroy();
    }

    /*@Override
    protected void onUserLeaveHint() {

        if (showAdProbability>=7){
            stopPlay();
            advert1();
        }
        super.onUserLeaveHint();
    }*/
}


