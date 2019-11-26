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

public class CommonExpressionsaActivity extends AppCompatActivity {


    static  ArrayList<CommonExpressionsA> commonExpressionsAArrayList;
    ListView commonExpressionaListView;
    AdView mAdView;

    StorageReference storageReference;
    MediaPlayer playFromDevice;
    MediaPlayer mp1;

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


    public void downloadClick () {
        int counter = 0;
        int counter1 =0;

        if (isNetworkAvailable()) {
            for (int j = 0; j < commonExpressionsAArrayList.size(); j++) {

                String bb = commonExpressionsAArrayList.get(j).getTwiCommonExpressionsA();
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
            if (counter == commonExpressionsAArrayList.size()) {
                toast.setText("All downloaded");
                toast.show();

            } else {
                toast.setText("Downloading");
                toast.show();

                for (int i = 0; i < commonExpressionsAArrayList.size(); i++) {
                    String b = commonExpressionsAArrayList.get(i).getTwiCommonExpressionsA().toLowerCase();
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
        File myFile = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/"+filename+ ".m4a");
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
                Toast.makeText(getApplicationContext(), query, Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<CommonExpressionsA> results = new ArrayList<>();
                for (CommonExpressionsA x: commonExpressionsAArrayList ){

                    if(x.getEnglishCommonExpressionsA().toLowerCase().contains(newText.toLowerCase()) || x.getTwiCommonExpressionsA().toLowerCase().contains(newText.toLowerCase())){
                        results.add(x);
                    }

                    ((CommonExpressionsAdapterA)commonExpressionaListView.getAdapter()).update(results);
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
            case R.id.videoCourse:
                goToWeb();
                return  true;
            case R.id.quiz1:
                goToQuizCommonExpressionsa();
                return true;
            case R.id.downloadAudio:
                downloadClick();
                return true;
            default:
                return false;
        }
    }


    public void goToMain(){
        Intent intent = new Intent(getApplicationContext(), HomeMainActivity.class);
        startActivity(intent);
    }

    public void goToQuizCommonExpressionsa() {
        Intent intent = new Intent(getApplicationContext(), QuizCommonExpressionsa.class);
        startActivity(intent);
    }
    public void goToWeb() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.udemy.com/course/learn-akan-twi/?couponCode=FDISCOUNT1"));
        startActivity(intent);
    }


    public void log2(View view) {
        int idview = view.getId();

        TextView blabla = view.findViewById(idview);
        String a = (String) blabla.getText();
        toast.setText(a);
        toast.show();
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


        if (b.contains(" ") || b.contains("/") || b.contains(",") || b.contains("(") || b.contains(")") || b.contains("-") || b.contains("?")|| b.contains("'")) {
            b = b.replace(" ", "");
            b = b.replace("/", "");
            b= b.replace(",","");
            b= b.replace("(","");
            b= b.replace(")","");
            b= b.replace("-","");
            b= b.replace("?","");
            b= b.replace("'","");
        }

        playFromFileOrDownload(b, a);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_expressionsa);

        isNetworkAvailable();

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        toast = Toast.makeText(getApplicationContext(), " " , Toast.LENGTH_SHORT);

        commonExpressionaListView = findViewById(R.id.commonExpressionsaListView);
        storageReference= FirebaseStorage.getInstance().getReference();

        commonExpressionsAArrayList = new ArrayList<>();

        commonExpressionsAArrayList.add(new CommonExpressionsA("Good morning (1)","Maakye"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("Good morning (2)","Mema wo akye"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("Good afternoon (1)","Maaha"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("Good afternoon (2)","Mema wo aha"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("Good evening (1)","Maadwo"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("Good evening (2)","Mema wo adwo"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("Good night","Da yie"));

        commonExpressionsAArrayList.add(new CommonExpressionsA("How are you?","Wo ho te sɛn?"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("How is your mother?","Wo maame ho te sɛn?"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("How is your wife?","Wo yere ho te sɛn?"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("I am fine","Me ho yɛ"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("How are they doing?","Wɔn ho te sɛn?"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("I am not feeling well (1)","Me ho mfa me"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("I am not feeling well (2)","Mente apɔ"));

        commonExpressionsAArrayList.add(new CommonExpressionsA("She is fine","Ne ho yɛ"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("He is fine","Ne ho yɛ"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("They are fine","Wɔn ho yɛ"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("They are all fine","Wɔn nyinaa ho yɛ"));


        commonExpressionsAArrayList.add(new CommonExpressionsA("I am happy to meet you","M'ani agye sɛ mahyia wo"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("Welcome","Akwaaba"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("I bid you welcome","Mema wo akwaaba"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("I am happy","M'ani agye"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("I am sad","Me werɛ ahow"));

        commonExpressionsAArrayList.add(new CommonExpressionsA("Stop crying","Gyae su"));


        commonExpressionsAArrayList.add(new CommonExpressionsA("Please (1)","Mepa wo kyɛw"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("Please (2)","Mesrɛ wo"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("Thank you (1)","Medaase"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("Thank you (2)","Me da wo ase"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("I miss you","Mafe wo"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("I will miss you","Mɛfe wo"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("We will miss you","Yɛbɛfe wo"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("We will miss you (Plural)","Yɛbɛfe mo"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("Love","Ɔdɔ"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("I love you","Me dɔ wo"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("We love you","Yɛdɔ mo"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("Do you love me?","Wo dɔ me?"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("Yes","Aane"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("No","Dabi"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("My name is Kwaku (1)","Yɛfrɛ me Kwaku"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("My name is Kwaku (2)","Me din de Kwaku"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("What is your name? (1)","Yɛfrɛ wo sɛn?"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("What is your name? (2)","Wo din de sɛn?"));

        commonExpressionsAArrayList.add(new CommonExpressionsA("How old are you?","Wadi mfe sɛn?"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("I am 30 years old","Madi mfe aduasa"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("Call me","Frɛ me"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("Do you speak English?","Wo ka borɔfo?"));

        commonExpressionsAArrayList.add(new CommonExpressionsA("I am sick","Me yare"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("I need money","Me hia sika"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("Do you have money","Wowɔ sika?"));

        commonExpressionsAArrayList.add(new CommonExpressionsA("Congratulations","Ayekoo"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("See you soon","Ɛrenkyɛ mehu wo"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("Give me a hug","Yɛ me atuu"));


        CommonExpressionsAdapterA commonExpressionsAdapterA = new CommonExpressionsAdapterA(this,commonExpressionsAArrayList);
        commonExpressionaListView.setAdapter(commonExpressionsAdapterA);

    }
}
