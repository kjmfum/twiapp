package com.learnakantwi.twiguides;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ReadingLesson2 extends AppCompatActivity implements LessonsAdapter.onClickRecycle{



   public static  ArrayList<Lessons> lesson2;
RecyclerView recyclerView;
LessonsAdapter myAdapter;
AdapterViewFlipper lessonAdapterView;
    Animation shake;
   ArrayList<Lessons> recycleArrayList;

    MediaPlayer playFromDevice;
    MediaPlayer mp1;

    ArrayList<Animals> childrenImageAnimals;

    Toast toast;

    StorageReference storageReference;


    TextView tvTwi;
    TextView tvEnglish;


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
        Intent intent = new Intent(getApplicationContext(), SubPHomeMainActivity.class);
        startActivity(intent);
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

        File myFile = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/READING/" + filename + ".m4a");
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
                final StorageReference musicRef = storageReference.child("/ReadingLessons/" + filename + ".m4a");
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
                    request.setDestinationInExternalFilesDir(getApplicationContext(), Environment.DIRECTORY_MUSIC+"/READING/", filename + fileExtension);
                    downloadManager.enqueue(request);
                }
            };
            Thread myThread = new Thread(runnable);
            myThread.start();
        } else {
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

        tvTwi = view.findViewById(R.id.textViewRecycle1);
        tvEnglish = view.findViewById(R.id.textViewRecycle2);


        tvTwi.setTextColor(Color.BLUE);
        toast.setText(tvTwi.getText().toString());
        toast.show();

        new Handler().postDelayed(() -> {
            tvTwi.setTextColor(Color.DKGRAY);
            //tvEnglish.setTextColor(Color.RED);

        },4000);



        storageReference = FirebaseStorage.getInstance().getReference();

        String twiAnimals = recycleArrayList.get(position).getTwi();

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_lesson1);

        toast= Toast.makeText(this, "", Toast.LENGTH_SHORT);

        lesson2 = new ArrayList<>();

        lesson2.add(new Lessons("Ataa baa ha", "Ataa came here"));
        lesson2.add(new Lessons("Ata pɛ abɛ", "Ata likes palm nut"));
        lesson2.add(new Lessons("kyɛw nyɛ na", "Hat is common"));
        lesson2.add(new Lessons("Pɛtea yɛ fɛ", "Ring is nice"));
        lesson2.add(new Lessons("Abɛ yɛ Ama dɛ", "Palm nut is sweet to Ama"));
        lesson2.add(new Lessons("Ataa hyɛ pɛtea", "Ataa wears ring"));
        lesson2.add(new Lessons("Ama hyɛ kyɛw", "Ama wears hat"));
        lesson2.add(new Lessons("Hyɛn abɛn no", "Blow the horn"));
        lesson2.add(new Lessons("Ataa hyɛn abɛn no", "Ataa came here"));
        lesson2.add(new Lessons("Ama sɛw kɛtɛ no", "Ama laid the mat"));
        lesson2.add(new Lessons("Adwoa hwɛɛ me", "Adwoa looked at me"));



        recycleArrayList = new ArrayList<>();
        recycleArrayList.addAll(lesson2);

        recyclerView = findViewById(R.id.recyclerView);

        // myAdapter = new LessonsAdapter(this, recycleArrayList, this);

        myAdapter = new LessonsAdapter(this,recycleArrayList,this);

        recyclerView.setAdapter(myAdapter);

        //  GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3, GridLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //  recyclerView.setLayoutManager(gridLayoutManager);

    }
}