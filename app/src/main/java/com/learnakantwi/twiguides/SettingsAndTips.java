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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

import static com.learnakantwi.twiguides.AllActivity.allArrayList;

public class SettingsAndTips extends AppCompatActivity {

    Button btDeleteAll;
    Button btDownloadAll;
    Toast toast;
    StorageReference storageReference;
    String sharedDownloadingOrNot;
    int downloadAllClickCount;
    int audioToDownload;

    public void deleteAllDownload(){

        File myFiles = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/");

        try {
        File [] files1 = myFiles.listFiles();

        if (files1.length>0) {
            for (File file : files1) {

                //for (int j = 0; j < files1.length; j++)
                // toast.setText(String.valueOf(files1.length));
                //toast.show();

                    boolean wasDeleted = file.delete();
                    if (!wasDeleted) {
                        System.out.println("Was not deleted");
                    }

                }

            }
        }catch (Exception E){
            System.out.println("Errors "+ E);
        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public void downloadClick () {
        int counter = 0;

        if (isNetworkAvailable()) {
            for (int j = 0; j < allArrayList.size(); j++) {

                String bb = allArrayList.get(j).getTwiMain();
                bb= bb.toLowerCase();
                boolean dd = bb.contains("ɔ");
                boolean ee = bb.contains("ɛ");
                if (dd || ee) {
                    bb = bb.replace("ɔ", "x");
                    bb = bb.replace("ɛ", "q");
                }

                if (bb.contains(" ") || bb.contains("/") || bb.contains(",") || bb.contains("(") || bb.contains(")") || bb.contains("-") || bb.contains("?") || bb.contains("'") | bb.contains("...")) {
                    bb = bb.replace(" ", "");
                    bb = bb.replace("/", "");
                    bb = bb.replace(",", "");
                    bb = bb.replace("(", "");
                    bb = bb.replace(")", "");
                    bb = bb.replace("-", "");
                    bb = bb.replace("?", "");
                    bb = bb.replace("'", "");
                    bb= bb.replace("...","");
                }
                File myFiles = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/" + bb + ".m4a");
                if (myFiles.exists()) {
                    counter++;
                }

            }
            if (counter == allArrayList.size()) {

                toast.setText("All downloaded");
                toast.show();

            } else {
                toast.setText("Downloading...");
                toast.show();

                // toast.setText(counter + " -- "+ allArrayList.size());
                // toast.show();

                for (int i = 0; i < allArrayList.size(); i++) {
                    toast.setText("Downloading...");
                    toast.show();

                    String b = allArrayList.get(i).getTwiMain().toLowerCase();
                    boolean d = b.contains("ɔ");
                    boolean e = b.contains("ɛ");
                    if (d || e) {
                        b = b.replace("ɔ", "x");
                        b = b.replace("ɛ", "q");
                    }

                    if (b.contains(" ") || b.contains("/") || b.contains(",") || b.contains("(") || b.contains(")") || b.contains("-") || b.contains("?")|| b.contains("...")|| b.contains("'")) {
                        b = b.replace(" ", "");
                        b = b.replace("/", "");
                        b= b.replace(",","");
                        b= b.replace("(","");
                        b= b.replace(")","");
                        b= b.replace("-","");
                        b= b.replace("?","");
                        b= b.replace("...","");
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
                    audioToDownload = audioToDownload-1;
                    if (audioToDownload==0){
                        sharedDownloadingOrNot="Yes";
                    }

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

    public boolean downloadState() {
        int testCounter = 0;
        if (isNetworkAvailable()) {
            for (int j = 0; j < allArrayList.size(); j++) {

                String bb = allArrayList.get(j).getTwiMain();
                bb = bb.toLowerCase();
                boolean dd = bb.contains("ɔ");
                boolean ee = bb.contains("ɛ");
                if (dd || ee) {
                    bb = bb.replace("ɔ", "x");
                    bb = bb.replace("ɛ", "q");
                }

                if (bb.contains(" ") || bb.contains("/") || bb.contains(",") || bb.contains("(") || bb.contains(")") || bb.contains("-") || bb.contains("?") || bb.contains("'") | bb.contains("...")) {
                    bb = bb.replace(" ", "");
                    bb = bb.replace("/", "");
                    bb = bb.replace(",", "");
                    bb = bb.replace("(", "");
                    bb = bb.replace(")", "");
                    bb = bb.replace("-", "");
                    bb = bb.replace("?", "");
                    bb = bb.replace("'", "");
                    bb = bb.replace("...", "");
                }
                File myFiles = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/" + bb + ".m4a");
                if (myFiles.exists()) {
                    testCounter++;
                }
            }
        }
        return testCounter != allArrayList.size();
    }
    public void downloadAll(){

        // Toast.makeText(this, sharedDownloadingOrNot, Toast.LENGTH_SHORT).show();

        if (downloadAllClickCount==0) {
            if (downloadState()) {
                new AlertDialog.Builder(this)
                        .setIcon(R.drawable.learnakantwiimage)
                        .setTitle("Download Audio?")
                        .setMessage("Downloading Audio will download any new vocabulary audio which has been added and allow you to use the audio offline. This might take a few minutes and will use your data. Would you like to Download Audio to enhance your experience.")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                downloadAllClickCount++;
                                toast.setText("Downloading in background. Please ");
                                toast.show();
                                // sharedDownloadingOrNot = "Yes";
                                downloadClick();
                                //Toast.makeText(Home.this, "Hi"+" "+ sharedDownloadingOrNot, Toast.LENGTH_LONG).show();
                            }
                        }).setNegativeButton("No", null)
                        .show();
            } else {
                toast.setText("You have All Audio Downloaded. No new audio yet");
                toast.show();
            }
        }
        else{
            toast.setText("Please wait a moment");
            toast.show();
            downloadAllClickCount=0;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_and_tips);

        toast= Toast.makeText(this, "", Toast.LENGTH_SHORT);
        btDeleteAll = findViewById(R.id.btDeleteAll);
        btDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), DeleteDownloadsActivity.class);
                startActivity(intent);

                //deleteAllDownload();
            }
        });





        btDownloadAll = findViewById(R.id.btDownloadAllAudio);
        btDownloadAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DownloadsActivity.class);
                startActivity(intent);
            }
        });

        final SharedPreferences sharedPreferences1 = this.getSharedPreferences("com.learnakantwi.twiguides", Context.MODE_PRIVATE);
        sharedDownloadingOrNot = sharedPreferences1.getString("Downloading", "");




        downloadAllClickCount =0;

        sharedPreferences1.edit().putString("Downloading", "Yes").apply();

        storageReference = FirebaseStorage.getInstance().getReference();
        toast= Toast.makeText(this, "", Toast.LENGTH_SHORT);
    }
}
