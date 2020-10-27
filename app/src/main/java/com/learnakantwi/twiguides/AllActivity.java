package com.learnakantwi.twiguides;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

public class AllActivity extends AppCompatActivity {


    static ArrayList<All> allArrayList;
    ListView allListView;
    int showAdProbability;
    Random random;

    AdView mAdView;


    StorageReference storageReference;
    MediaPlayer playFromDevice;
    MediaPlayer mp1;
    String state;
    ArrayList<All> results;
    Context context;

    public InterstitialAd mInterstitialAd;

    int audioToDownload=0;
    int downloadAllClickCount=0;


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

    public void playFromFileOrDownload(final String filename, String type){

        File myFile = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/"+filename+ ".m4a");
        if (type.equals("verb")){
            myFile = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/VERBS/" + filename + ".m4a");
        }
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
                        /*toast.setText(appearText);
                        toast.show();*/
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {

            if (isNetworkAvailable()) {

                final StorageReference musicRef = storageReference.child("/AllTwi/" + filename + ".m4a");

                final StorageReference musicRef2 = storageReference.child("/Verbs/" + filename + ".m4a");

                if (type.equals("verb")) {
                    playFromFirebase(musicRef2);
                    musicRef2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();
                            downloadFile(getApplicationContext(), filename, ".m4a", url, type);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "No Internet", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    musicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();

                            playFromFirebase(musicRef);

                            downloadFile(getApplicationContext(), filename, ".m4a", url, type);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "No Internet", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            else {
                toast.setText("Please connect to Internet to download audio");
                toast.show();
            }


        }
    }

    public void downloadFile(final Context context, final String filename, final String fileExtension, final String url, String type) {

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
                    if(type.equals("verb")){
                        request.setDestinationInExternalFilesDir(getApplicationContext(),Environment.DIRECTORY_MUSIC + "/VERBS", filename + fileExtension);
                    }
                    else {
                        request.setDestinationInExternalFilesDir(getApplicationContext(), Environment.DIRECTORY_MUSIC, filename + fileExtension);
                    }
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

    public void downloadOnly(final String filename, String type){
        if (isNetworkAvailable()) {

            audioToDownload++;

            final StorageReference musicRef = storageReference.child("/AllTwi/" + filename + ".m4a");

            final StorageReference musicRef2 = storageReference.child("/Verbs/" + filename + ".m4a");

            if (type.equals("verb")) {
                musicRef2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String url = uri.toString();
                        downloadFile(getApplicationContext(), filename, ".m4a", url, type);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "No Internet", Toast.LENGTH_SHORT).show();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (audioToDownload == downloadAllClickCount){
                            downloadAllClickCount = 0;
                        }
                    }
                });
            } else {
                musicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String url = uri.toString();
                        downloadFile(getApplicationContext(), filename, ".m4a", url, type);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "No Internet", Toast.LENGTH_SHORT).show();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (audioToDownload == downloadAllClickCount){
                            downloadAllClickCount = 0;
                        }
                    }
                });
            }
        }
        else {
            toast.setText("Please connect to Internet to download audio");
            toast.show();
        }
    }

    public void downloadClick () {
        int counter = 0;

        downloadAllClickCount = allArrayList.size()- counter;

        if (isNetworkAvailable()) {
            for (int j = 0; j < allArrayList.size(); j++) {

                String bb = allArrayList.get(j).getTwiMain();
                String type1 = allArrayList.get(j).getEnglish1().toLowerCase();

                bb = PlayFromFirebase.viewTextConvert(bb);


                File myFiles = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/" + bb + ".m4a");

                if (type1.equals("verb")){
                    myFiles = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/VERBS/" + bb + ".m4a");
                }
                if (myFiles.exists()) {
                    counter++;
                    downloadAllClickCount = allArrayList.size()- counter;
                }
            }

            if (counter == allArrayList.size()) {
                toast.setText("All downloaded");
                toast.show();
                // Toast.makeText(this, "Download Complete", Toast.LENGTH_SHORT).show();

            } else {
                toast.setText("Downloading...");
                toast.show();

                // toast.setText(counter + " -- "+ allArrayList.size());
                // toast.show();
                audioToDownload = 0;
                for (int i = 0; i < allArrayList.size(); i++) {
                    String b = allArrayList.get(i).getTwiMain().toLowerCase();
                    String type2 = allArrayList.get(i).getEnglish1().toLowerCase();

                    b = PlayFromFirebase.viewTextConvert(b);

                    File myFile = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/" + b + ".m4a");
                    if (type2.equals("verb")){
                        myFile = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/VERBS/" + b + ".m4a");
                    }
                    if (!myFile.exists()) {
                        if (isNetworkAvailable()){
                            downloadOnly(b, type2);
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

    public boolean downloadState() {
        int testCounter = 0;
        if (isNetworkAvailable()) {
            for (int j = 0; j < allArrayList.size(); j++) {

                String bb = allArrayList.get(j).getTwiMain();
                String type = allArrayList.get(j).getEnglish1();

                bb = PlayFromFirebase.viewTextConvert(bb);

                File myFiles = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/" + bb + ".m4a");
                if (type.equals("verb")){
                    myFiles = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/VERBS/" + bb + ".m4a");
                }
                if (myFiles.exists()) {
                    testCounter++;
                }
            }
        }

        return testCounter != allArrayList.size();
    }

    public void downloadAll(){

        if (downloadAllClickCount==0) {
            if (downloadState()) {
                new AlertDialog.Builder(this)
                        .setIcon(R.drawable.learnakantwiimage)
                        .setTitle("Download Audio?")
                        .setMessage("Downloading Audio will download any new vocabulary audio which has been added and allow you to use the audio offline. This might take a few minutes and will use your data. Would you like to Download Audio to enhance your experience.")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // downloadAllClickCount++;
                                toast.setText("Downloading in background. Please ");
                                toast.show();
                                downloadClick();
                            }
                        }).setNegativeButton("No", null)
                        .show();
            } else {
                toast.setText("You have All Audio Downloaded. No New Audio");
                toast.show();
            }
        }
        else{
            //toast.setText("Please wait a moment \n" + downloadAllClickCount +"\n"+ audioToDownload);
            toast.setText("Downloading... \n Please wait a moment");
            toast.show();

        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //MenuInflater menuInflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.main_menu_all, menu);

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
                results = new ArrayList<>();
                for (All x: allArrayList ){

                    if(x.getEnglishmain().toLowerCase().contains(newText.toLowerCase()) || x.getTwiMain().toLowerCase().contains(newText.toLowerCase())
                            || x.getTwi1().toLowerCase().contains(newText.toLowerCase())
                            || x.getTwi2().toLowerCase().contains(newText.toLowerCase())
                            || x.getEnglish1().toLowerCase().contains(newText.toLowerCase())
                            || x.getEnglish2().toLowerCase().contains(newText.toLowerCase())

                    ){
                        results.add(x);
                    }

                    ((AllAdapter)allListView.getAdapter()).update(results);
                }


                allListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {


                        state = "hi";
                        String b = results.get(position).getTwiMain();
                        String a = results.get(position).getEnglishmain();
                        StringBuilder sb = new StringBuilder();



                        String type = results.get(position).getEnglish1().toLowerCase();
                        //sb = sb.append(a).append(" is:\n\t").append(b);

                        //Toast.makeText(context, sb, Toast.LENGTH_SHORT).show();

                        b = PlayFromFirebase.viewTextConvert(b);

                        playFromFileOrDownload(b, type);

                        return false;
                    }
                });



                allListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        state = "no";
                        String b = results.get(position).getTwiMain();
                        String a = results.get(position).getEnglishmain();

                        PlayFromFirebase playFromFirebase = new PlayFromFirebase();

                        StringBuilder sb = new StringBuilder();
                        sb = sb.append(a).append(" is:\n\t").append(b);

                        b = PlayFromFirebase.viewTextConvert(b);


                        Toast.makeText(context, sb, Toast.LENGTH_SHORT).show();

                        playFromFileOrDownload(b,a);
                    }
                });

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
                goToQuizAll();
                return  true;
            case R.id.downloadAllAudio:
               downloadAll();
              //  deleteAllDownload();
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



    public void goToMain(){
        Intent intent = new Intent(getApplicationContext(), HomeMainActivity.class);
        startActivity(intent);
    }

    public void goToQuizAll() {
        Intent intent = new Intent(getApplicationContext(), QuizAll.class);
        startActivity(intent);
    }

    public void deleteSection() {
        //  File myFileProverbs = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/PROVERBS/");
        File myFileProverbs = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/Temporary");
        File[] files2 = myFileProverbs.listFiles();

        Toast.makeText(this, "Yeah", Toast.LENGTH_SHORT).show();

        try {
            for (int i = 0; i < files2.length; i++) {

                File fileConversation = files2[i];
                fileConversation.delete();
            }
        }catch (Exception e){
            System.out.println("Errors "+ e);
        }


    }


    public void advert1() {



        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }

        //  Appodeal.cache(this, Appodeal.INTERSTITIAL);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all);

        context = this;

        isNetworkAvailable();

       // Appodeal.cache(this, Appodeal.INTERSTITIAL);

        random = new Random();
        showAdProbability = random.nextInt(10);

        toast = Toast.makeText(getApplicationContext(), "Tap to Listen" , Toast.LENGTH_LONG);
        toast.show();




       // test unit id: ca-app-pub-3940256099942544/6300978111

        mAdView = findViewById(R.id.adView);
        if (MainActivity.Subscribed!=1){

            MobileAds.initialize(this, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {
                }
            });

            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);

            mInterstitialAd = new InterstitialAd(this);
            mInterstitialAd.setAdUnitId(MainActivity.AdUnitInterstitial);
            mInterstitialAd.loadAd(new AdRequest.Builder().build());


        }

        //allArrayList = new ArrayList<>();
        allListView = findViewById(R.id.allListView);

        storageReference = FirebaseStorage.getInstance().getReference();

///////////////
        AllAdapter allAdapter= new AllAdapter(this, allArrayList);
        allListView.setAdapter(allAdapter);

       /* allListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {


                state = "hi";
                String b = allArrayList.get(position).getTwiMain();
                String a = allArrayList.get(position).getEnglishmain();



                PlayFromFirebase playFromFirebase = new PlayFromFirebase();




                b = PlayFromFirebase.viewTextConvert(b);

                playFromFileOrDownloadA(b, a);

                return false;
            }
        });
*/
allListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        state = "no";
        String type;

        String b = allArrayList.get(position).getTwiMain();
        String a = allArrayList.get(position).getEnglishmain();
        type = allArrayList.get(position).getEnglish1().toLowerCase();

        PlayFromFirebase playFromFirebase = new PlayFromFirebase();

        StringBuilder sb = new StringBuilder();
        sb = sb.append(a).append(" is:\n\t").append(b);

        b = PlayFromFirebase.viewTextConvert(b);


        Toast.makeText(context, sb, Toast.LENGTH_SHORT).show();

        try {
            playFromFileOrDownload(b, type);
        }
        catch (Exception E){
            Toast.makeText(context, "Too bad \n"+ E, Toast.LENGTH_SHORT).show();
        }

    }
});


    }

   /* public void stopPlay (){


        if (playFromDevice != null) {
            toast.cancel();
            playFromDevice.stop();
            playFromDevice.reset();
            playFromDevice.release();
        }

        if (mp1 != null) {
            mp1.stop();
            mp1.reset();
            mp1.release();
        }
    }

    @Override
    protected void onPause() {
        stopPlay();
        super.onPause();
    }
*/
   @Override
   protected void onDestroy() {
       if (showAdProbability<=6){
           advert1();
       }
       super.onDestroy();
   }


/*
    @Override
    protected void onUserLeaveHint() {
        stopPlay();
        super.onUserLeaveHint();
    }*/
}

