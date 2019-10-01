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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class DaysOfWeekActivity extends AppCompatActivity {
    ListView daysOfWeekListView;
    EditText daysOfWeekEditText;
    DaysOfWeekAdapter myAdapterDaysOfWk;
    static  ArrayList<DaysOfWeek> daysOfWeeksArray;

    StorageReference storageReference;
    MediaPlayer playFromDevice;
    MediaPlayer mp1;



    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
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
                        Toast.makeText(getApplicationContext(), appearText, Toast.LENGTH_SHORT).show();
                    }
                });
                //Toast.makeText(this, "From Device", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {

            if (isNetworkAvailable()){
                //Toast.makeText(this, "I'm available", Toast.LENGTH_SHORT).show();

                final StorageReference musicRef = storageReference.child("/AllTwi/" + filename + ".m4a");
                musicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String url = uri.toString();
                        playFromFirebase(musicRef);
                        // Toast.makeText(getApplicationContext(), "Got IT", Toast.LENGTH_SHORT).show();
                        downloadFile(getApplicationContext(), filename, ".m4a", url);
                        Toast.makeText(getApplicationContext(), appearText, Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "No Internet", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else {
                Toast.makeText(this, "Please connect to Internet to download audio", Toast.LENGTH_SHORT).show();
            }


        }
    }
    public void playFromFirebase(StorageReference musicRef) {

        if (Build.VERSION.SDK_INT > 22) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        try {
            final File localFile = File.createTempFile("aduonu", "m4a");

            if (localFile != null) {
                musicRef.getFile(localFile)
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                                if (mp1 != null){
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
                Toast.makeText(this, "File was not created", Toast.LENGTH_SHORT).show();
            }

        }catch(IOException ex){
            ex.printStackTrace();
        }

    }

    public void downloadFile(final Context context, final String filename, final String fileExtension, final String url) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse(url);
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setVisibleInDownloadsUi(false);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                //   request.setDestinationInExternalPublicDir(Environment.DIRECTORY_MUSIC+File.separator+"LearnTwi1", filename+fileExtension);
                request.setDestinationInExternalFilesDir(getApplicationContext(), Environment.DIRECTORY_MUSIC, filename + fileExtension);
                //request.setDestinationInExternalPublicDir(Environment.DIRECTORY_MUSIC+File.separator+"LearnTwi1", filename+fileExtension);
                downloadManager.enqueue(request);
            }
        };
        Thread myThread = new Thread(runnable);
        myThread.start();


    }


    public void log3(View view){
        int idview= view.getId();

        TextView blabla = (TextView) view.findViewById(idview);
        String a = (String) blabla.getText();

        String b = a.toLowerCase();

        boolean d = b.contains("ɔ");
        boolean e = b.contains("ɛ");

        if (d || e ){
            b= b.replace("ɔ","x");
            b= b.replace("ɛ","q");
        }

        playFromFileOrDownload(b, a);

       /* int resourceId = getResources().getIdentifier(b, "raw", "com.learnakantwi.twiguides");


        final MediaPlayer player = MediaPlayer.create(this, resourceId);
        player.start();

        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                player.release();
            }
        });
*/
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
                Toast.makeText(DaysOfWeekActivity.this, query, Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<DaysOfWeek> results = new ArrayList<>();
                for (DaysOfWeek x: daysOfWeeksArray ){

                    if(x.getNameEnglish().toLowerCase().contains(newText.toLowerCase()) || x.getNameTwi().toLowerCase().contains(newText.toLowerCase())){
                        results.add(x);
                    }

                    ((DaysOfWeekAdapter)daysOfWeekListView.getAdapter()).update(results);
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
           /* case R.id.settings:
                Log.i("Menu Item Selected", "Settings");
                playAll();
                return true;
            case R.id.alphabets:
                Log.i("Menu Item Selected", "Alphabets");
                return  true;*/
            case R.id.videoCourse:
                //Log.i("Menu Item Selected", "Alphabets");
                goToWeb();
                return  true;
            case R.id.quiz1:
                //Log.i("Menu Item Selected", "Alphabets");
                goToQuizDayOfWeek();
                return  true;
            case R.id.main:
                //Log.i("Menu Item Selected", "Alphabets");
                goToMain();
                return  true;

            default:
                return false;
        }
    }


    public void goToMain(){
        Intent intent = new Intent(getApplicationContext(), Home.class);
        startActivity(intent);
    }
    public void goToQuizDayOfWeek() {
        Intent intent = new Intent(getApplicationContext(), QuizDaysOfWeek.class);
        startActivity(intent);
    }
    public void goToWeb() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.udemy.com/course/learn-akan-twi/?couponCode=FDISCOUNT1"));
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_days_of_week);

        daysOfWeekListView = findViewById(R.id.lvdaysofweek);
        storageReference= FirebaseStorage.getInstance().getReference();



        /*daysOfWeeksArray = new ArrayList<>();

        daysOfWeeksArray.add(new DaysOfWeek("Monday", "Dwoada"));
        daysOfWeeksArray.add(new DaysOfWeek("Tuesday", "Benada"));
        daysOfWeeksArray.add(new DaysOfWeek("Wednesday", "Wukuada"));
        daysOfWeeksArray.add(new DaysOfWeek("Thursday", "Yawada"));
        daysOfWeeksArray.add(new DaysOfWeek("Friday", "Fiada"));
        daysOfWeeksArray.add(new DaysOfWeek("Saturday", "Memeneda"));
        daysOfWeeksArray.add(new DaysOfWeek("Sunday", "Kwasiada"));
        // daysOfWeeksArray.add(new DaysOfWeek("Holiday", "Afofida"));*/


        myAdapterDaysOfWk = new DaysOfWeekAdapter(this, daysOfWeeksArray);
        daysOfWeekListView.setAdapter(myAdapterDaysOfWk);
    }




}
