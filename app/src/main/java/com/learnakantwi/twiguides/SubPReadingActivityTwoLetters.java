package com.learnakantwi.twiguides;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class SubPReadingActivityTwoLetters extends AppCompatActivity {

    ArrayList  <String> twoLettersArrayList = new ArrayList<>();
    String vowelLetter = "e";
    ListView lvTwoLetters;
    TextView tvHeader;
    String b;
    PlayFromFirebase playFromFirebase;
    StorageReference storageReference;

    MediaPlayer playFromDevice;
    MediaPlayer mp1;

    Toast toast;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //MenuInflater menuInflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.main_menu_simple, menu);

        final MenuItem item = menu.findItem(R.id.menusearch);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(SubPReadingActivityTwoLetters.this, query, Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                final ArrayList<String> results = new ArrayList<>();
                for (String x: twoLettersArrayList ){

                    if(x.toLowerCase().contains(newText.toLowerCase()) ){
                        results.add(x);
                    }

                    ((ReadingTwoLetterAdapter)lvTwoLetters.getAdapter()).update(results);
                }

                lvTwoLetters.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {

                        view.setBackgroundColor(Color.GREEN);


                        b =  results.get(position);


                        String c;
                        StringBuilder sb = new StringBuilder();

                        sb= sb.append(b.substring(0,1)).append("   ").append(b.charAt(1)).append("\n\t").append(b);
                        // c= b.substring(0,1) + "  "+ Character.toString(b.charAt(1)) +"   :"+ b.substring(0,2);

                        b= "read"+b.toLowerCase();
                        b=  PlayFromFirebase.viewTextConvert(b);

                        // Toast.makeText(ReadingActivityTwoLetters.this, sb, Toast.LENGTH_SHORT).show();

                        toast.setText(sb);
                        toast.show();

                        playFromFileOrDownload(b);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                view.setBackgroundColor(Color.WHITE);
                            }
                        }, 2000);


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
                goToMain();
                return  true;
            /*case R.id.quiz1:
                goToQuizFamily();
                return  true;
            case R.id.downloadAudio:
                downloadClick();
                return true;*/
            case R.id.videoCourse:
                //Log.i("Menu Item Selected", "Alphabets");
                goToWeb();
                return  true;
            default:
                return false;
        }
    }

    public void goToMain(){
        Intent intent = new Intent(getApplicationContext(), SubPHomeMainActivity.class);
        startActivity(intent);
    }

    public void goToWeb() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.udemy.com/course/learn-akan-twi/?referralCode=6D321CE6AEE1834CCB0F"));
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

        File myFile = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/" + filename + ".m4a");
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
                final StorageReference musicRef = storageReference.child("/AllTwi/" + filename + ".m4a");
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
                        //Toast.makeText(getApplicationContext(), "No Internet", Toast.LENGTH_SHORT).show();
                        toast.setText("No Internet");
                        toast.show();
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
                    request.setDestinationInExternalFilesDir(getApplicationContext(), Environment.DIRECTORY_MUSIC, filename + fileExtension);
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_preading_two_letters);


        toast = Toast.makeText(getApplicationContext(), "Tap to Listen" , Toast.LENGTH_LONG);

        playFromFirebase = new PlayFromFirebase();
        storageReference = FirebaseStorage.getInstance().getReference();

        playFromDevice = new MediaPlayer();
        mp1 = new MediaPlayer();


        Intent intent = getIntent();
        vowelLetter = intent.getStringExtra("vowel");
        //getIntent(vowel);

        tvHeader = findViewById(R.id.tvTwoLetterHeader);
        tvHeader.setText(vowelLetter);
        tvHeader.setTextColor(Color.BLUE);

        tvHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                v.setBackgroundColor(Color.YELLOW);
                tvHeader.setTextColor(Color.BLACK);


                b = tvHeader.getText().toString().toLowerCase();
                b=  PlayFromFirebase.viewTextConvert(b);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tvHeader.setTextColor(Color.BLUE);
                        v.setBackgroundColor(Color.WHITE);
                        //tvHeader.setRotation(270);
                    }
                },2000);

                playFromFileOrDownload(b);
            }
        });

        lvTwoLetters = findViewById(R.id.lvtwoLetters);



        twoLettersArrayList.add("B"+vowelLetter);
        twoLettersArrayList.add("D"+vowelLetter);
        twoLettersArrayList.add("F"+vowelLetter);
        twoLettersArrayList.add("G"+vowelLetter);
        twoLettersArrayList.add("H"+vowelLetter);
        twoLettersArrayList.add("K"+vowelLetter);
        twoLettersArrayList.add("L"+vowelLetter);
        twoLettersArrayList.add("M"+vowelLetter);
        twoLettersArrayList.add("N"+vowelLetter);
        twoLettersArrayList.add("P"+vowelLetter);
        twoLettersArrayList.add("R"+vowelLetter);
        twoLettersArrayList.add("S"+vowelLetter);
        twoLettersArrayList.add("T"+vowelLetter);
        twoLettersArrayList.add("W"+vowelLetter);
        twoLettersArrayList.add("Y"+vowelLetter);



        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,twoLetters);

        ReadingTwoLetterAdapter readingTwoLetterAdapter = new ReadingTwoLetterAdapter(this, twoLettersArrayList);
        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, ReadingTwoLetterAdapter);
        lvTwoLetters.setAdapter(readingTwoLetterAdapter);

        lvTwoLetters.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {

                view.setBackgroundColor(Color.GREEN);


                b =  twoLettersArrayList.get(position);


                String c;
                StringBuilder sb = new StringBuilder();

                sb= sb.append(b.substring(0,1)).append("   ").append(b.charAt(1)).append("\n\t").append(b);
                // c= b.substring(0,1) + "  "+ Character.toString(b.charAt(1)) +"   :"+ b.substring(0,2);

                b= "read"+b.toLowerCase();
                b=  PlayFromFirebase.viewTextConvert(b);

                // Toast.makeText(ReadingActivityTwoLetters.this, sb, Toast.LENGTH_SHORT).show();

                toast.setText(sb);
                toast.show();

                playFromFileOrDownload(b);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        view.setBackgroundColor(Color.WHITE);
                    }
                }, 3000);


            }
        });



    }

    @Override
    protected void onDestroy() {

        toast.setText("");
        toast.cancel();
        super.onDestroy();
    }
}
