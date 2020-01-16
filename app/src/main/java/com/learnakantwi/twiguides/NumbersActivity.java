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
import android.view.MenuInflater;
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

public class NumbersActivity extends AppCompatActivity {

    static ArrayList<Numbers> numbersArrayList;
    ListView numbersListView;
    Numbers numbers;

    StorageReference storageReference;
    MediaPlayer playFromDevice;
    MediaPlayer mp1;

    AdView mAdView;


    Toast toast;
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }


    public void downloadOnly(final String filename){
        if (isNetworkAvailable()){

            final StorageReference musicRef = storageReference.child("/AllTwi/" + filename + ".m4a");
            musicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    String url = uri.toString();
                    downloadFile(getApplicationContext(), filename, ".m4a", url);
                    toast.setText("Downloaded");
                    toast.show();
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
            for (int j = 0; j < numbersArrayList.size(); j++) {

                String bb = numbersArrayList.get(j).getNumberWord();
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
            if (counter == numbersArrayList.size()) {
                toast.setText("All downloaded");
                toast.show();

            } else {
                toast.setText("Downloading...");
                toast.show();

                for (int i = 0; i < numbersArrayList.size(); i++) {
                    String b = numbersArrayList.get(i).getNumberWord().toLowerCase();
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
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        final MenuItem item = menu.findItem(R.id.menusearch);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(NumbersActivity.this, query, Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<Numbers> results = new ArrayList<>();

                for (Numbers x: numbersArrayList){


                    if(x.getNumberWord().toLowerCase().contains(newText.toLowerCase()) || x.getFigure().contains(newText)){
                        results.add(x);
                    }

                    ((NumbersAdapter)numbersListView.getAdapter()).update(results);
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
                goToQuizNumbers();
                return  true;
            case R.id.downloadAudio:
                downloadClick();
                return true;
            case R.id.videoCourse:
                goToWeb();
                return  true;
            default:
                return false;
        }
    }

    public void goToQuizNumbers() {
        Intent intent = new Intent(getApplicationContext(), QuizNumbers.class);
        startActivity(intent);
    }

    public void goToWeb() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.udemy.com/course/learn-akan-twi/?referralCode=6D321CE6AEE1834CCB0F"));
        startActivity(intent);
    }


    public void goToMain(){
        Intent intent = new Intent(getApplicationContext(), HomeMainActivity.class);
        startActivity(intent);
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


    public void log2(View view) {
        int idview = view.getId();

        TextView blabla = view.findViewById(idview);
        String a = (String) blabla.getText();

        toast.setText(a);
        toast.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numbers);

        isNetworkAvailable();
        toast = Toast.makeText(getApplicationContext(), " " , Toast.LENGTH_SHORT);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        numbersListView = findViewById(R.id.myList1);
        storageReference = FirebaseStorage.getInstance().getReference();

       /* numbersArrayList = new ArrayList<>();



        numbersArrayList.add(new Numbers("0", "Hwee/Ohunu"));
        numbersArrayList.add(new Numbers("1", "Baako"));
        numbersArrayList.add(new Numbers("2", "Mmienu"));
        numbersArrayList.add(new Numbers("3", "Mmiɛnsa"));
        numbersArrayList.add(new Numbers("4", "Ɛnan"));
        numbersArrayList.add(new Numbers("5", "Enum"));
        numbersArrayList.add(new Numbers("6", "Nsia"));
        numbersArrayList.add(new Numbers("7", "Nson"));
        numbersArrayList.add(new Numbers("8", "Nwɔtwe"));
        numbersArrayList.add(new Numbers("9", "Nkron"));
        numbersArrayList.add(new Numbers("10", "Du"));

        numbersArrayList.add(new Numbers("11", "Dubaako"));
        numbersArrayList.add(new Numbers("12", "Dummienu"));
        numbersArrayList.add(new Numbers("13", "Dummiɛnsa"));
        numbersArrayList.add(new Numbers("14", "Dunan"));
        numbersArrayList.add(new Numbers("15", "Dunum"));
        numbersArrayList.add(new Numbers("16", "Dunsia"));
        numbersArrayList.add(new Numbers("17", "Dunson"));
        numbersArrayList.add(new Numbers("18", "Dunwɔtwe"));
        numbersArrayList.add(new Numbers("19", "Dunkron"));
        numbersArrayList.add(new Numbers("20", "Aduonu"));

        numbersArrayList.add(new Numbers("21", "Aduonu baako"));
        numbersArrayList.add(new Numbers("22", "Aduonu mmienu"));
        numbersArrayList.add(new Numbers("23", "Aduonu mmiɛnsa"));
        numbersArrayList.add(new Numbers("24", "Aduonu nan"));
        numbersArrayList.add(new Numbers("25", "Aduonu num"));
        numbersArrayList.add(new Numbers("26", "Aduonu nsia"));
        numbersArrayList.add(new Numbers("27", "Aduonu nson"));
        numbersArrayList.add(new Numbers("28", "Aduonu nwɔtwe"));
        numbersArrayList.add(new Numbers("29", "Aduonu nkron"));
        numbersArrayList.add(new Numbers("30", "Aduasa"));


        numbersArrayList.add(new Numbers("31", "Aduasa baako"));
        numbersArrayList.add(new Numbers("32", "Aduasa mmienu"));
        numbersArrayList.add(new Numbers("33", "Aduasa mmiɛnsa"));
        numbersArrayList.add(new Numbers("34", "Aduasa nan"));
        numbersArrayList.add(new Numbers("35", "Aduasa num"));
        numbersArrayList.add(new Numbers("36", "Aduasa nsia"));
        numbersArrayList.add(new Numbers("37", "Aduasa nson"));
        numbersArrayList.add(new Numbers("38", "Aduasa nwɔtwe"));
        numbersArrayList.add(new Numbers("39", "Aduasa nkron"));
        numbersArrayList.add(new Numbers("40", "Aduanan"));

        numbersArrayList.add(new Numbers("41", "Aduanan baako"));
        numbersArrayList.add(new Numbers("42", "Aduanan mmienu"));
        numbersArrayList.add(new Numbers("43", "Aduanan mmiɛnsa"));
        numbersArrayList.add(new Numbers("44", "Aduanan nan"));
        numbersArrayList.add(new Numbers("45", "Aduanan num"));
        numbersArrayList.add(new Numbers("46", "Aduanan nsia"));
        numbersArrayList.add(new Numbers("47", "Aduanan nson"));
        numbersArrayList.add(new Numbers("48", "Aduanan nwɔtwe"));
        numbersArrayList.add(new Numbers("49", "Aduanan nkron"));
        numbersArrayList.add(new Numbers("50", "Aduonum"));

        numbersArrayList.add(new Numbers("51", "Aduonum baako"));
        numbersArrayList.add(new Numbers("52", "Aduonum mmienu"));
        numbersArrayList.add(new Numbers("53", "Aduonum mmiɛnsa"));
        numbersArrayList.add(new Numbers("54", "Aduonum nan"));
        numbersArrayList.add(new Numbers("55", "Aduonum num"));
        numbersArrayList.add(new Numbers("56", "Aduonum nsia"));
        numbersArrayList.add(new Numbers("57", "Aduonum nson"));
        numbersArrayList.add(new Numbers("58", "Aduonum nwɔtwe"));
        numbersArrayList.add(new Numbers("59", "Aduonum nkron"));
        numbersArrayList.add(new Numbers("60", "Aduosia"));

        numbersArrayList.add(new Numbers("61", "Aduosia baako"));
        numbersArrayList.add(new Numbers("62", "Aduosia mmienu"));
        numbersArrayList.add(new Numbers("63", "Aduosia mmiɛnsa"));
        numbersArrayList.add(new Numbers("64", "Aduosia nan"));
        numbersArrayList.add(new Numbers("65", "Aduosia num"));
        numbersArrayList.add(new Numbers("66", "Aduosia nsia"));
        numbersArrayList.add(new Numbers("67", "Aduosia nson"));
        numbersArrayList.add(new Numbers("68", "Aduosia nwɔtwe"));
        numbersArrayList.add(new Numbers("69", "Aduosia nkron"));
        numbersArrayList.add(new Numbers("70", "Aduoson"));

        numbersArrayList.add(new Numbers("71", "Aduoson baako"));
        numbersArrayList.add(new Numbers("72", "Aduoson mmienu"));
        numbersArrayList.add(new Numbers("73", "Aduoson mmiɛnsa"));
        numbersArrayList.add(new Numbers("74", "Aduoson nan"));
        numbersArrayList.add(new Numbers("75", "Aduoson num"));
        numbersArrayList.add(new Numbers("76", "Aduoson nsia"));
        numbersArrayList.add(new Numbers("77", "Aduoson nson"));
        numbersArrayList.add(new Numbers("78", "Aduoson nwɔtwe"));
        numbersArrayList.add(new Numbers("79", "Aduoson nkron"));
        numbersArrayList.add(new Numbers("80", "Aduowɔtwe"));

        numbersArrayList.add(new Numbers("81", "Aduowɔtwe baako"));
        numbersArrayList.add(new Numbers("82", "Aduowɔtwe mmienu"));
        numbersArrayList.add(new Numbers("83", "Aduowɔtwe mmiɛnsa"));
        numbersArrayList.add(new Numbers("84", "Aduowɔtwe nan"));
        numbersArrayList.add(new Numbers("85", "Aduowɔtwe num"));
        numbersArrayList.add(new Numbers("86", "Aduowɔtwe nsia"));
        numbersArrayList.add(new Numbers("87", "Aduowɔtwe nson"));
        numbersArrayList.add(new Numbers("88", "Aduowɔtwe nwɔtwe"));
        numbersArrayList.add(new Numbers("89", "Aduowɔtwe nkron"));
        numbersArrayList.add(new Numbers("90", "Aduokron"));

        numbersArrayList.add(new Numbers("91", "Aduokron baako"));
        numbersArrayList.add(new Numbers("92", "Aduokron mmienu"));
        numbersArrayList.add(new Numbers("93", "Aduokron mmiɛnsa"));
        numbersArrayList.add(new Numbers("94", "Aduokron nan"));
        numbersArrayList.add(new Numbers("95", "Aduokron num"));
        numbersArrayList.add(new Numbers("96", "Aduokron nsia"));
        numbersArrayList.add(new Numbers("97", "Aduokron nson"));
        numbersArrayList.add(new Numbers("98", "Aduokron nwɔtwe"));
        numbersArrayList.add(new Numbers("99", "Aduokron nkron"));
        numbersArrayList.add(new Numbers("100", "Ɔha"));

        numbersArrayList.add(new Numbers("101", "Ɔha ne baako"));
        numbersArrayList.add(new Numbers("102", "Ɔha ne mmienu"));
        numbersArrayList.add(new Numbers("103", "Ɔha ne mmiɛnsa"));
        numbersArrayList.add(new Numbers("104", "Ɔha ne nan"));
        numbersArrayList.add(new Numbers("105", "Ɔha ne num"));
        numbersArrayList.add(new Numbers("106", "Ɔha ne nsia"));
        numbersArrayList.add(new Numbers("107", "Ɔha ne nson"));
        numbersArrayList.add(new Numbers("108", "Ɔha ne nwɔtwe"));
        numbersArrayList.add(new Numbers("109", "Ɔha ne nkron"));
        numbersArrayList.add(new Numbers("110", "Ɔha ne du"));

        numbersArrayList.add(new Numbers("111", "Ɔha ne dubaako"));
        numbersArrayList.add(new Numbers("112", "Ɔha ne dummienu"));
        numbersArrayList.add(new Numbers("113", "Ɔha ne dummiɛnsa"));
        numbersArrayList.add(new Numbers("114", "Ɔha ne dunan"));
        numbersArrayList.add(new Numbers("115", "Ɔha ne dunum"));
        numbersArrayList.add(new Numbers("116", "Ɔha ne dunsia"));
        numbersArrayList.add(new Numbers("117", "Ɔha ne dunson"));
        numbersArrayList.add(new Numbers("118", "Ɔha ne dunwɔtwe"));
        numbersArrayList.add(new Numbers("119", "Ɔha ne dunkron"));
        numbersArrayList.add(new Numbers("120", "Ɔha aduonu"));


        numbersArrayList.add(new Numbers("121", "Ɔha aduonu baako"));
        numbersArrayList.add(new Numbers("122", "Ɔha aduonu mmienu"));
        numbersArrayList.add(new Numbers("123", "Ɔha aduonu mmiɛnsa"));
        numbersArrayList.add(new Numbers("124", "Ɔha aduonu nan"));
        numbersArrayList.add(new Numbers("125", "Ɔha aduonu num"));

        numbersArrayList.add(new Numbers("130", "Ɔha aduasa"));
        numbersArrayList.add(new Numbers("140", "Ɔha aduanan"));
        numbersArrayList.add(new Numbers("150", "Ɔha aduonum"));
        numbersArrayList.add(new Numbers("160", "Ɔha aduosia"));
        numbersArrayList.add(new Numbers("170", "Ɔha aduoson"));
        numbersArrayList.add(new Numbers("180", "Ɔha aduowɔtwe"));
        numbersArrayList.add(new Numbers("190", "Ɔha aduokron"));
        numbersArrayList.add(new Numbers("200", "Ahanu"));


        numbersArrayList.add(new Numbers("201", "Ahanu ne baako"));
        numbersArrayList.add(new Numbers("202", "Ahanu ne mmienu"));
        numbersArrayList.add(new Numbers("203", "Ahanu ne mmiɛnsa"));
        numbersArrayList.add(new Numbers("204", "Ahanu ne nan"));
        numbersArrayList.add(new Numbers("205", "Ahanu ne num"));
        numbersArrayList.add(new Numbers("206", "Ahanu ne nsia"));
        numbersArrayList.add(new Numbers("207", "Ahanu ne nson"));
        numbersArrayList.add(new Numbers("208", "Ahanu ne nwɔtwe"));
        numbersArrayList.add(new Numbers("209", "Ahanu ne nkron"));
        numbersArrayList.add(new Numbers("210", "Ahanu ne du"));
        numbersArrayList.add(new Numbers("211", "Ahanu ne dubaako"));

        numbersArrayList.add(new Numbers("220", "Ahanu aduonu"));
        numbersArrayList.add(new Numbers("221", "Ahanu aduonu baako"));
        numbersArrayList.add(new Numbers("230", "Ahanu aduasa"));
        numbersArrayList.add(new Numbers("240", "Ahanu aduanan"));
        numbersArrayList.add(new Numbers("250", "Ahanu aduonum"));

        numbersArrayList.add(new Numbers("300", "Ahasa"));
        numbersArrayList.add(new Numbers("400", "Ahanan"));
        numbersArrayList.add(new Numbers("500", "Ahanum"));
        numbersArrayList.add(new Numbers("600", "Ahansia"));
        numbersArrayList.add(new Numbers("700", "Ahanson"));
        numbersArrayList.add(new Numbers("800", "Ahanwɔtwe"));
        numbersArrayList.add(new Numbers("900", "Ahankron"));
        numbersArrayList.add(new Numbers("1000", "Apem"));


        numbersArrayList.add(new Numbers("1001", "Apem ne baako"));
        numbersArrayList.add(new Numbers("1002", "Apem ne mmienu"));
        numbersArrayList.add(new Numbers("1003", "Apem ne mmiɛnsa"));
        numbersArrayList.add(new Numbers("1004", "Apem ne nan"));
        numbersArrayList.add(new Numbers("1005", "Apem ne num"));

        numbersArrayList.add(new Numbers("1010", "Apem ne du"));
        numbersArrayList.add(new Numbers("1011", "Apem ne dubaako"));
        numbersArrayList.add(new Numbers("1012", "Apem ne dummienu"));
        numbersArrayList.add(new Numbers("1013", "Apem ne dummiɛnsa"));
        numbersArrayList.add(new Numbers("1014", "Apem ne dunan"));
        numbersArrayList.add(new Numbers("1015", "Apem ne dunum"));

        numbersArrayList.add(new Numbers("1020", "Apem aduonu"));
        numbersArrayList.add(new Numbers("1021", "Apem aduonu baako"));
        numbersArrayList.add(new Numbers("1022", "Apem aduonu mmienu"));
        numbersArrayList.add(new Numbers("1023", "Apem aduonu mmiɛnsa"));
        numbersArrayList.add(new Numbers("1024", "Apem aduonu nan"));
        numbersArrayList.add(new Numbers("1025", "Apem aduonu num"));

        numbersArrayList.add(new Numbers("1100", "Apem ɔha"));
        numbersArrayList.add(new Numbers("1101", "Apem ɔha ne baako"));
        numbersArrayList.add(new Numbers("1102", "Apem ɔha ne mmienu"));
        numbersArrayList.add(new Numbers("1103", "Apem ɔha ne mmiɛnsa"));
        numbersArrayList.add(new Numbers("1104", "Apem ɔha ne nan"));
        numbersArrayList.add(new Numbers("1105", "Apem ɔha ne num"));

        numbersArrayList.add(new Numbers("1200", "Apem ahanu"));
        numbersArrayList.add(new Numbers("1201", "Apem ahanu ne baako"));
        numbersArrayList.add(new Numbers("1202", "Apem ahanu ne mmienu"));
        numbersArrayList.add(new Numbers("1203", "Apem ahanu ne mmiɛnsa"));
        numbersArrayList.add(new Numbers("1204", "Apem ahanu ne nan"));
        numbersArrayList.add(new Numbers("1205", "Apem ahanu ne num"));

        numbersArrayList.add(new Numbers("1300", "Apem ahasa"));
        numbersArrayList.add(new Numbers("1400", "Apem ahanan"));
        numbersArrayList.add(new Numbers("1500", "Apem ahanum"));
        numbersArrayList.add(new Numbers("1600", "Apem ahansia"));
        numbersArrayList.add(new Numbers("1700", "Apem ahanson"));
        numbersArrayList.add(new Numbers("1800", "Apem ahanwɔtwe"));
        numbersArrayList.add(new Numbers("1900", "Apem ahankron"));
        numbersArrayList.add(new Numbers("2000", "Mpem mmienu"));

        numbersArrayList.add(new Numbers("2001", "Mpem mmienu ne baako"));
        numbersArrayList.add(new Numbers("2010", "Mpem mmienu ne du"));
        numbersArrayList.add(new Numbers("2100", "Mpem mmienu ne ɔha"));
        numbersArrayList.add(new Numbers("2101", "Mpem mmienu ɔha ne baako"));
        numbersArrayList.add(new Numbers("2201", "Mpem mmienu ahanu ne baako"));
        numbersArrayList.add(new Numbers("2301", "Mpem mmienu ahasa ne baako"));

        numbersArrayList.add(new Numbers("3000", "Mpem mmiɛnsa"));
        numbersArrayList.add(new Numbers("4000", "Mpem nan"));
        numbersArrayList.add(new Numbers("5000", "Mpem num"));
        numbersArrayList.add(new Numbers("6000", "Mpem nsia"));
        numbersArrayList.add(new Numbers("7000", "Mpem nson"));
        numbersArrayList.add(new Numbers("8000", "Mpem nwɔtwe"));
        numbersArrayList.add(new Numbers("9000", "Mpem nkron"));
        numbersArrayList.add(new Numbers("10000", "Mpem du"));

        numbersArrayList.add(new Numbers("10001", "Mpem du ne baako"));
        numbersArrayList.add(new Numbers("10100", "Mpem du ne ɔha"));
        numbersArrayList.add(new Numbers("10101", "Mpem du, ɔha ne baako"));
        numbersArrayList.add(new Numbers("10200", "Mpem du ne ahanu"));
        numbersArrayList.add(new Numbers("10201", "Mpem du, ahanu ne baako"));
        numbersArrayList.add(new Numbers("10300", "Mpem du ne ahasa"));

        numbersArrayList.add(new Numbers("11000", "Mpem dubaako"));
        numbersArrayList.add(new Numbers("11001", "Mpem dubaako ne baako"));
        numbersArrayList.add(new Numbers("11011", "Mpem dubaako ne dubaako"));
        numbersArrayList.add(new Numbers("11121", "Mpem dubaako, ɔha ne aduonu baako"));

        numbersArrayList.add(new Numbers("12000", "Mpem dummienu"));
        numbersArrayList.add(new Numbers("13000", "Mpem dummiɛnsa"));
        numbersArrayList.add(new Numbers("14000", "Mpem dunan"));
        numbersArrayList.add(new Numbers("15000", "Mpem dunum"));
        numbersArrayList.add(new Numbers("16000", "Mpem dunsia"));
        numbersArrayList.add(new Numbers("17000", "Mpem dunson"));
        numbersArrayList.add(new Numbers("18000", "Mpem dunwɔtwe"));
        numbersArrayList.add(new Numbers("19000", "Mpem dunkron"));
        numbersArrayList.add(new Numbers("20000", "Mpem aduonu"));

        numbersArrayList.add(new Numbers("30000", "Mpem aduasa"));
        numbersArrayList.add(new Numbers("40000", "Mpem aduanan"));
        numbersArrayList.add(new Numbers("50000", "Mpem aduonum"));
        numbersArrayList.add(new Numbers("60000", "Mpem aduosia"));
        numbersArrayList.add(new Numbers("70000", "Mpem aduoson"));
        numbersArrayList.add(new Numbers("80000", "Mpem aduowɔtwe"));
        numbersArrayList.add(new Numbers("90000", "Mpem aduokron"));
        numbersArrayList.add(new Numbers("100 000", "Mpem ɔha"));


        numbersArrayList.add(new Numbers("1,000,000", "Ɔpepem baako"));
        numbersArrayList.add(new Numbers("2 000 000", "Ɔpepem mmienu"));
        numbersArrayList.add(new Numbers("10 000 000", "Ɔpepem du"));

        numbersArrayList.add(new Numbers("100 000 000", "Ɔpepem ɔha"));

        numbersArrayList.add(new Numbers("1 000 000 000", "Ɔpepepem baako"));

*/
        NumbersAdapter numbersAdapter = new NumbersAdapter(this, numbersArrayList);
        numbersListView.setAdapter(numbersAdapter);

    }


}
