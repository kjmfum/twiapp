package com.learnakantwi.twiguides;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
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

public class TimeActivity extends AppCompatActivity {

    static ArrayList<Time> timeArrayList;
    ListView timeListView;
    TextView textView;
    TimeAdapter timeAdapter;

    StorageReference storageReference;
    MediaPlayer playFromDevice;
    MediaPlayer mp1;

    AdView mAdView;

    Toast toast;


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
            for (int j = 0; j < timeArrayList.size(); j++) {

                String bb = timeArrayList.get(j).getTwiTime();
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
            if (counter == timeArrayList.size()) {
                toast.setText("All downloaded");
                toast.show();

            } else {
                toast.setText("Downloading...");
                toast.show();

                for (int i = 0; i < timeArrayList.size(); i++) {
                    String b = timeArrayList.get(i).getTwiTime().toLowerCase();
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

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //MenuInflater menuInflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.main_menu, menu);

        final MenuItem item = menu.findItem(R.id.menusearch);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(TimeActivity.this, query, Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<Time> results = new ArrayList<>();
                for (Time x: timeArrayList ){

                    if(x.getEnglishTime().toLowerCase().contains(newText.toLowerCase()) || x.getTwiTime().toLowerCase().contains(newText.toLowerCase())){
                        results.add(x);
                    }

                    ((TimeAdapter)timeListView.getAdapter()).update(results);
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
            case R.id.downloadAudio:
                downloadClick();
                return true;
            case R.id.quiz1:
                goToQuizTime();
            default:
                return false;
        }
    }

    public void goToQuizTime() {
        Intent intent = new Intent(getApplicationContext(), QuizTime.class);
        startActivity(intent);
    }

    public void goToWeb() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.udemy.com/course/learn-akan-twi/?couponCode=FDISCOUNT1"));
        startActivity(intent);
    }

    public void goToMain(){
        Intent intent = new Intent(getApplicationContext(), Home.class);
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);

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

        timeListView = findViewById(R.id.timeListView);
        textView = findViewById(R.id.speakEnglishTime);

        storageReference = FirebaseStorage.getInstance().getReference();

       /* timeArrayList= new ArrayList<Time>();

        timeArrayList.add(new Time("Second","Anibu"));
        timeArrayList.add(new Time(" One second","Anibu baako"));
        timeArrayList.add(new Time(" Two seconds","Anibu mmienu"));


        timeArrayList.add(new Time("Minute (1)","Simma"));
        timeArrayList.add(new Time("Minute (2)","Miniti"));
        timeArrayList.add(new Time("One minute","Simma baako"));
        timeArrayList.add(new Time("Two minutes","Simma mmienu"));



        timeArrayList.add(new Time("Hour","Dɔnhwere"));
        timeArrayList.add(new Time("Hours","Nnɔnhwere"));
        timeArrayList.add(new Time("1 hour","Dɔnhwere baako"));
        timeArrayList.add(new Time("2 hours","Nnɔnhwere mmienu"));

        timeArrayList.add(new Time("Day","Da"));
        timeArrayList.add(new Time("Days","Nna"));
        timeArrayList.add(new Time("One day","Da koro"));
        timeArrayList.add(new Time("Two days","Nnanu"));
        timeArrayList.add(new Time("Three days","Nnansa"));
        timeArrayList.add(new Time("Four days","Nnanan"));
        timeArrayList.add(new Time("Five days","Nnanum"));
        timeArrayList.add(new Time("Six days","Nnansia"));
        timeArrayList.add(new Time("Seven days","Nnanson"));

        timeArrayList.add(new Time("First day","Da a edi kan"));
        timeArrayList.add(new Time("Second day","Da a ɛtɔ so mmienu"));
        timeArrayList.add(new Time("Third day","Da a ɛtɔ so mmiɛnsa"));

        timeArrayList.add(new Time("Week (1)","Nnawɔtwe"));
        timeArrayList.add(new Time("Week (2)","Dapɛn"));
        timeArrayList.add(new Time("Weeks (1)","Nnawɔtwe"));
        timeArrayList.add(new Time("Weeks (2)","Adapɛn"));
        timeArrayList.add(new Time("First week","Nnawɔtwe a edi kan"));
        timeArrayList.add(new Time("Second week","Nnawɔtwe a ɛtɔ so mmienu"));
        timeArrayList.add(new Time("Third week","Nnawɔtwe a ɛtɔ so mmiɛnsa"));
        timeArrayList.add(new Time("Fortnight","Nnawɔtwe mmienu"));
        timeArrayList.add(new Time("Next week","Nnawɔtwe a edi hɔ"));
        timeArrayList.add(new Time("Last week","Nnawɔtwe a etwaam"));
        timeArrayList.add(new Time("Last two weeks","Nnawɔtwe mmienu a etwaam"));
        timeArrayList.add(new Time("This week","Nnawɔtwe yi"));

        timeArrayList.add(new Time("Month (1)","Ɔsram"));
        timeArrayList.add(new Time("Month (2)","Bosome"));
        timeArrayList.add(new Time("Months","Abosome"));
        timeArrayList.add(new Time("This Month","Bosome yi"));
        timeArrayList.add(new Time("Last Month","Bosome a etwaam"));
        timeArrayList.add(new Time("First Month (2)","Bosome a edi kan"));
        timeArrayList.add(new Time("Second Month (2)","Bosome a ɛtɔ so mmienu"));

        timeArrayList.add(new Time("Year","Afe"));
        timeArrayList.add(new Time("Years","Mfe"));
        timeArrayList.add(new Time("This year","Afe yi"));
        timeArrayList.add(new Time("Last year","Afe a etwaam"));
        timeArrayList.add(new Time("A year by this time","Afe sesɛɛ"));
        timeArrayList.add(new Time("Next year (1)","Afedan"));
        timeArrayList.add(new Time("Next year (2)","Afe a yebesi mu"));


        timeArrayList.add(new Time("Today","Nnɛ"));
        timeArrayList.add(new Time("Yesterday","Ɛnnora"));
        timeArrayList.add(new Time("Tomorrow","Ɔkyena"));

        timeArrayList.add(new Time("When","Bere bɛn"));
        timeArrayList.add(new Time("What is the time?","Abɔ sɛn?"));
        timeArrayList.add(new Time("What time is it?","Abɔ sɛn?"));






        timeArrayList.add(new Time("Morning","Anɔpa"));
        timeArrayList.add(new Time("This morning","Anɔpa yi"));

        timeArrayList.add(new Time("Afternoon","Awia"));
        timeArrayList.add(new Time("This afternoon","Awia yi"));

        timeArrayList.add(new Time("Evening","Anwummere"));
        timeArrayList.add(new Time("This evening","Anwummere yi"));

        timeArrayList.add(new Time("Night","Anadwo"));
        timeArrayList.add(new Time("This night","Anadwo yi"));

        timeArrayList.add(new Time("Midnight","Dasuom"));

        timeArrayList.add(new Time("Dawn","Anɔpatutu"));


        timeArrayList.add(new Time("1 am","Anɔpa Dɔnkoro"));
        timeArrayList.add(new Time("1:01","Dɔnkoro apa ho simma baako"));
        timeArrayList.add(new Time("1:05","Dɔnkoro apa ho simma num"));
        timeArrayList.add(new Time("1:15","Dɔnkoro apa ho simma dunum"));



        timeArrayList.add(new Time("2 am","Anɔpa Nnɔnmmienu"));
        timeArrayList.add(new Time("2:30 (1)","Nnɔnmmienu ne fa"));
        timeArrayList.add(new Time("2:30 (2)","Nnɔnmmienu apa ho simma aduasa"));


        timeArrayList.add(new Time("3 am","Anɔpa Nnɔnmmiɛnsa "));

        timeArrayList.add(new Time("4 am","Anɔpa Nnɔnnan"));
        timeArrayList.add(new Time("5 am","Anɔpa Nnɔnnum"));
        timeArrayList.add(new Time("6 am","Anɔpa Nnɔnsia"));
        timeArrayList.add(new Time("7 am","Anɔpa Nnɔnson"));
        timeArrayList.add(new Time("8 am","Anɔpa Nnɔnwɔtwe"));
        timeArrayList.add(new Time("9 am","Anɔpa Nnɔnkron"));
        timeArrayList.add(new Time("10 am","Anɔpa Nnɔndu"));
        timeArrayList.add(new Time("11 am","Anɔpa Nnɔndubaako"));
        timeArrayList.add(new Time("12 am","Nnɔndummienu"));
        timeArrayList.add(new Time("Noon (1)","Nnɔndummienu"));
        timeArrayList.add(new Time("Noon (2)","Owigyinae"));
        timeArrayList.add(new Time("1 pm","Awia Dɔnkoro"));
        timeArrayList.add(new Time("2 pm","Awia Nnɔnmmienu"));
        timeArrayList.add(new Time("3 pm","Awia Nnɔnmmiɛnsa"));
        timeArrayList.add(new Time("4 pm","Anwummere Nnɔnnan"));
        timeArrayList.add(new Time("5 pm","Anwummere Nnɔnnum"));
        timeArrayList.add(new Time("6 pm","Anwummere Nnɔnsia"));
        timeArrayList.add(new Time("7 pm","Anwummere Nnɔnson"));
        timeArrayList.add(new Time("8 pm","Anadwo Nnɔnwɔtwe"));
        timeArrayList.add(new Time("9 pm","Anadwo Nnɔnkron"));
        timeArrayList.add(new Time("10 pm","Anadwo Nnɔndu"));
        timeArrayList.add(new Time("11 pm","Anadwo Nnɔndubaako"));
        timeArrayList.add(new Time("12 pm (1)","Anadwo dummienu"));
        timeArrayList.add(new Time("12 pm (2)","Dasuom"));*/


        timeAdapter = new TimeAdapter(this,timeArrayList);

        timeListView.setAdapter(timeAdapter);





    }
}
