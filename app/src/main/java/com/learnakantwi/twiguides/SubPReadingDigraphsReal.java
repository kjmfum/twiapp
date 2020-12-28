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
import android.widget.AdapterView;
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

public class SubPReadingDigraphsReal extends AppCompatActivity {

        ArrayList<String> digraphArrayList = new ArrayList<>();
        String vowelLetter = "e";
        ListView lvDigraph;
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
                    //Toast.makeText(com.learnakantwi.twiguides.SubPReadingActivityTwoLetters.this, query, Toast.LENGTH_SHORT).show();
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    final ArrayList<String> results = new ArrayList<>();
                    for (String x : digraphArrayList) {

                        if (x.toLowerCase().contains(newText.toLowerCase())) {
                            results.add(x);
                        }

                        //((ReadingTwoLetterAdapter)digraphArrayList.getAdapter()).update(results);
                    }

                    lvDigraph.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {

                            view.setBackgroundColor(Color.GREEN);


                            b = results.get(position);


                            String c;
                            StringBuilder sb = new StringBuilder();

                            sb = sb.append(b.substring(0, 1)).append("   ").append(b.charAt(1)).append("\n\t").append(b);
                            // c= b.substring(0,1) + "  "+ Character.toString(b.charAt(1)) +"   :"+ b.substring(0,2);

                            b = "read" + b.toLowerCase();
                            b = PlayFromFirebase.viewTextConvert(b);

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

            switch (item.getItemId()) {
                case R.id.main:
                    goToMain();
                    return true;
            /*case R.id.quiz1:
                goToQuizFamily();
                return  true;
            case R.id.downloadAudio:
                downloadClick();
                return true;*/
                case R.id.videoCourse:
                    //Log.i("Menu Item Selected", "Alphabets");
                    goToWeb();
                    return true;
                default:
                    return false;
            }
        }

        public void goToMain() {
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


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_sub_pdigraphs);

            toast = Toast.makeText(getApplicationContext(), "Tap to Listen", Toast.LENGTH_LONG);

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
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            tvHeader.setTextColor(Color.BLUE);
                            v.setBackgroundColor(Color.WHITE);
                            //tvHeader.setRotation(270);
                        }
                    }, 3000);



                    b = tvHeader.getText().toString().toLowerCase();
                    if (b.contains("as")){
                        b = "read" + PlayFromFirebase.viewTextConvert(b);
                        playFromFileOrDownload(b);
                    }


                }
            });

            lvDigraph = findViewById(R.id.lvDigraph);
/*
            digraphArrayList.add(vowelLetter + "ama");
            digraphArrayList.add(vowelLetter + "kofi");*/

            switch (vowelLetter) {
                case ("Gy"):
                    tvHeader.setText("Gy as in James");
                    digraphArrayList.add(vowelLetter + "a");
                    digraphArrayList.add(vowelLetter + "e");
                    digraphArrayList.add(vowelLetter + "i");
                    break;
                case ("Ny"):
                    tvHeader.setText("Ny as in canyon");
                    digraphArrayList.add(vowelLetter + "a");
                    digraphArrayList.add(vowelLetter + "e");
                    digraphArrayList.add(vowelLetter + "ɛ");
                    digraphArrayList.add(vowelLetter + "i");
                    break;
                case ("Ky"):
                    tvHeader.setText("Ky as in child");
                    digraphArrayList.add(vowelLetter + "a");
                    digraphArrayList.add(vowelLetter + "e");
                    digraphArrayList.add(vowelLetter + "ɛ");
                    digraphArrayList.add(vowelLetter + "i");
                    break;
                case ("Kw"):
                    tvHeader.setText("Kw as in quick");
                    digraphArrayList.add(vowelLetter + "a");
                    digraphArrayList.add(vowelLetter + "e");
                    digraphArrayList.add(vowelLetter + "ɛ");
                    digraphArrayList.add(vowelLetter + "i");
                    break;
                case ("Hy"):
                    tvHeader.setText("Hy as in shirts");
                    digraphArrayList.add(vowelLetter + "a");
                    digraphArrayList.add(vowelLetter + "e");
                    digraphArrayList.add(vowelLetter + "ɛ");
                    digraphArrayList.add(vowelLetter + "i");
                    break;
                case ("Dw"):
                    digraphArrayList.add(vowelLetter + "a");
                    digraphArrayList.add(vowelLetter + "e");
                    digraphArrayList.add(vowelLetter + "ɛ");
                    digraphArrayList.add(vowelLetter + "i");
                    digraphArrayList.add(vowelLetter + "o");
                    digraphArrayList.add(vowelLetter + "ɔ");
                    digraphArrayList.add(vowelLetter + "u");
                    break;
                case ("Tw"):
                    digraphArrayList.add(vowelLetter + "a");
                    digraphArrayList.add(vowelLetter + "e");
                    digraphArrayList.add(vowelLetter + "ɛn");
                    digraphArrayList.add(vowelLetter + "i");
                    break;
                case ("Hw"):
                    digraphArrayList.add(vowelLetter + "e");
                    digraphArrayList.add(vowelLetter + "ɛ");
                    digraphArrayList.add(vowelLetter + "i");
                    break;
                default:
                    digraphArrayList.add(vowelLetter);
                    digraphArrayList.add(vowelLetter);
                    return;
            }

            //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,twoLetters);

            ReadingTwoLetterAdapter readingTwoLetterAdapter = new ReadingTwoLetterAdapter(this, digraphArrayList);
            //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, ReadingTwoLetterAdapter);
            lvDigraph.setAdapter(readingTwoLetterAdapter);

            lvDigraph.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {

                    view.setBackgroundColor(Color.GREEN);


                    b = digraphArrayList.get(position);

                    toast.setText(b);
                    toast.show();
                    String c;

                    /*StringBuilder sb = new StringBuilder();

                    sb = sb.append(b.substring(0, 1)).append("   ").append(Character.toString(b.charAt(1))).append("\n\t").append(b);*/
                    // c= b.substring(0,1) + "  "+ Character.toString(b.charAt(1)) +"   :"+ b.substring(0,2);

                    b = "read" + b.toLowerCase();
                    b = PlayFromFirebase.viewTextConvert(b);

                    // Toast.makeText(ReadingActivityTwoLetters.this, sb, Toast.LENGTH_SHORT).show();



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


