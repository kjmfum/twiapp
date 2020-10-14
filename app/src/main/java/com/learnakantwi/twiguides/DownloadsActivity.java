package com.learnakantwi.twiguides;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
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
import java.util.ArrayList;

import static com.learnakantwi.twiguides.FoodActivity.foodArrayList;
import static com.learnakantwi.twiguides.ProverbsActivity.proverbsArrayList;

public class DownloadsActivity extends AppCompatActivity {

    Toast toast;
    String bb;
    String b;
    StorageReference storageReference;
    int downloadAllClickCount;

    Button btdownloadProverbs;
    Button btdownloadChildren;
    Button btdownloadConversation;
    Button btdownloadFood;



    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    /*public void downloadFile(final Context context, final String filename, final String fileExtension, final String url, final String c) {

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
                    switch (c){
                        case "Proverbs":
                            request.setDestinationInExternalFilesDir(getApplicationContext(), Environment.DIRECTORY_MUSIC + "/PROVERBS/" , filename + fileExtension);
                            break;
                        default:
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
    }*/

       public void downloadFile(final Context context, final String filename, final String url, final String c) {

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
                    switch (c){
                        case "Proverbs":
                            request.setDestinationInExternalFilesDir(getApplicationContext(), Environment.DIRECTORY_MUSIC + "/PROVERBS/" , filename + ".m4a");
                            break;
                        default:
                            request.setDestinationInExternalFilesDir(getApplicationContext(), Environment.DIRECTORY_MUSIC, filename);
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

    public void downloadOnly(final String filename, final String category){
        if (isNetworkAvailable()) {
            final StorageReference musicRef;
            switch (category) {
                case "Proverbs":
                    musicRef = storageReference.child("/Proverbs/" + b + ".m4a");
                    break;
                /*case "Food":
                    //musicRef = storageReference.child("/AllTwi/" + b + ".m4a");
                   // downloadOnly(b, c);*/
                default:
                    musicRef = storageReference.child("/AllTwi/" + b + ".m4a");


                    //final StorageReference musicRef = storageReference.child("/AllTwi/" + filename + ".m4a");
                    musicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();
                            downloadFile(getApplicationContext(), filename + ".m4a", url, category);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            toast.setText("Lost Internet Connection");
                            toast.show();
                        }
                    });
            }
        }
        else {
            toast.setText("Please connect to Internet to download audio");
            toast.show();
        }
    }

    public void Dialog(String category){
        if (downloadAllClickCount==0) {
           // if (downloadState()) {
                if (1==1) {
                new AlertDialog.Builder(this)
                        .setIcon(R.drawable.learnakantwiimage)
                        .setTitle("Download Audio?")
                        .setMessage("This will download all "+ category +" audio. This might take a few minutes and will use your data. Would you like to proceed?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                downloadAllClickCount++;
                                toast.setText("Downloading in background. Please ");
                                toast.show();
                                // sharedDownloadingOrNot = "Yes";
                                //downloadClick();
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
            toast.setText("Please wait a moment for other downloads to complete");
            toast.show();
            if (downloadAllClickCount >=5)
            downloadAllClickCount=0;
        }
    }
        public void DownloadProverbs(ArrayList hi, final String c) {
            b=c;

            int counter = 0;
            PlayFromFirebase playFromFirebase = new PlayFromFirebase();
            //File myFiles = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/PROVERBS/"+bb+ ".m4a");
            File myFiles = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/"+ ".m4a");
            if (isNetworkAvailable()) {
                for (int j = 0; j < hi.size(); j++) {
                    String b;
                    switch (c) {
                        case "Proverbs":
                            // String bb = proverbsArrayList.get(j).getTwiProverb();
                            b = proverbsArrayList.get(j).getTwiProverb();
                            b = PlayFromFirebase.viewTextConvert(b);
                            myFiles = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/PROVERBS/"+b+ ".m4a");
                            if (myFiles.exists()) {
                                counter++;
                            }
                            break;
                        case "Food":
                            // String bb = proverbsArrayList.get(j).getTwiProverb();
                            b = foodArrayList.get(j).getTwiFood();
                            b = PlayFromFirebase.viewTextConvert(b);
                            myFiles = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/"+b+ ".m4a");
                            if (myFiles.exists()) {
                                counter++;
                            }
                            break;
                        case "Conversation":
                            bb = SubConversationIntroductionActivity.conversationArrayList.get(j).getTwiConversation();
                            if (myFiles.exists()) {
                                counter++;
                            }
                            break;
                    }

                   // myFiles = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/PROVERBS/"+bb+ ".m4a");
                }
                if (counter == hi.size()) {
                    toast.setText("All downloaded: "+ counter +" And: "+hi.size());
                    toast.show();
                } else {
                   toast.setText("Downloading...");
                    toast.show();

                    for (int i = 0; i < hi.size(); i++) {
                        String  b= " " ;
                        /*b = proverbsArrayList.get(i).getTwiProverb();
                        b = playFromFirebase.viewTextConvert(b);
                        toast.setText(b+ " "+ "I'm here "+ hi.size());
                        toast.show();
*/
                        switch (c) {
                            case "Proverbs":
                                // String bb = proverbsArrayList.get(j).getTwiProverb();
                                b = proverbsArrayList.get(i).getTwiProverb();
                                b = PlayFromFirebase.viewTextConvert(b);
                                myFiles = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/PROVERBS/"+b+ ".m4a");
                                break;
                            case "Food":
                                // String bb = proverbsArrayList.get(j).getTwiProverb();
                                b = foodArrayList.get(i).getTwiFood();
                                b = PlayFromFirebase.viewTextConvert(b);
                                myFiles = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/"+b+ ".m4a");
                                break;
                            case "Conversation":
                                b = SubConversationIntroductionActivity.conversationArrayList.get(i).getTwiConversation();
                                break;
                        }

                        if (!myFiles.exists()) {
                            StorageReference musicRef= storageReference.child("/Proverbs/" + b + ".m4a");
                            if (isNetworkAvailable()){
                               // Toast.makeText(this, bb, Toast.LENGTH_SHORT).show();
                                switch (c){
                                    case "Proverbs":
                                         //musicRef = storageReference.child("/Proverbs/" + b + ".m4a");
                                         downloadOnly(b, c);
                                         break;
                                    case "Food":
                                        //musicRef = storageReference.child("/AllTwi/" + b + ".m4a");
                                        downloadOnly(b, c);
                                    default:
                                        musicRef = storageReference.child("/AllTwi/" + b + ".m4a");

                                }

                                //final String finalB = b;
                                /*final String finalB = b;
                                musicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String url = uri.toString();
                                        downloadOnly(finalB +".m4a",url, c);
                                        //downloadFile(getApplicationContext(), b +".m4a" ,  url, c);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        toast.setText("Lost Internet Connection");
                                        toast.show();
                                    }
                                });*/
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

    public void DownloadProverbs() {
        int counter = 0;

        if (isNetworkAvailable()) {
            for (int j = 0; j < proverbsArrayList.size(); j++) {

                String bb = proverbsArrayList.get(j).getTwiProverb();
                PlayFromFirebase playFromFirebase = new PlayFromFirebase();
                bb=  PlayFromFirebase.viewTextConvert(bb);

                File myFiles = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/PROVERBS/"+bb+ ".m4a");
                if (myFiles.exists()) {
                    counter++;
                }

            }
            if (counter == proverbsArrayList.size()) {
                toast.setText("All downloaded ");
                toast.show();
            } else {
                toast.setText("Downloading...");
                toast.show();

                for (int i = 0; i < proverbsArrayList.size(); i++) {
                    b = proverbsArrayList.get(i).getTwiProverb();
                    PlayFromFirebase playFromFirebase = new PlayFromFirebase();
                    b=  PlayFromFirebase.viewTextConvert(b);


                    File myFile = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/PROVERBS/" + b + ".m4a");
                    if (!myFile.exists()) {
                        if (isNetworkAvailable()){
                            Toast.makeText(this, b, Toast.LENGTH_SHORT).show();
                            final StorageReference musicRef = storageReference.child("/Proverbs/" + b + ".m4a");
                            musicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String url = uri.toString();
                                   // downloadFile(getApplicationContext(), b, ".m4a", url,"Proverbs");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    toast.setText("Lost Internet Connection");
                                    toast.show();
                                }
                            });
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

        public void CheckDownloadComplete(ArrayList hi, String bb) {

            //Toast.makeText(this, "About to download "+ bb, Toast.LENGTH_SHORT).show();

        int counter = 0;
            PlayFromFirebase playFromFirebase = new PlayFromFirebase();
            File myFiles = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/PROVERBS/"+bb+ ".m4a");
            //if (isNetworkAvailable()) {
                for (int j = 0; j < hi.size(); j++) {

                    switch (bb) {
                        case "Proverbs":
                            // String bb = proverbsArrayList.get(j).getTwiProverb();
                            bb = ProverbsActivity.proverbsArrayList.get(j).getTwiProverb();
                            bb = PlayFromFirebase.viewTextConvert(bb);
                            myFiles = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/PROVERBS/"+bb+ ".m4a");
                            break;
                        case "Food":
                            // String bb = proverbsArrayList.get(j).getTwiProverb();
                            bb = foodArrayList.get(j).getTwiFood();
                            bb = PlayFromFirebase.viewTextConvert(bb);
                            myFiles = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/"+bb+ ".m4a");
                            break;
                        case "Conversation":
                            bb = SubConversationIntroductionActivity.conversationArrayList.get(j).getTwiConversation();
                            break;
                    }
                            /*Toast.makeText(this, "Hi " + j, Toast.LENGTH_SHORT).show();
                            System.out.println("Hi " + j);*/
                   // }

                    if (myFiles.exists()) {
                        counter++;
                    }

                }
                if (counter == hi.size()) {
                    toast.setText("All downloaded");
                    toast.show();
                }
                else{
                    Toast.makeText(this, "About to download "+ bb, Toast.LENGTH_SHORT).show();
                    //int left = hi.size()-counter;
                }
            }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloads);
        toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);

       // CheckDownloadComplete(foodArrayList, "Food");

        storageReference = FirebaseStorage.getInstance().getReference();



        btdownloadProverbs = findViewById(R.id.btDownloadProverbs);
        btdownloadFood=findViewById(R.id.btDownloadFood);


        btdownloadProverbs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadProverbs(proverbsArrayList,"Proverbs");
            }
        });

        btdownloadFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadProverbs(foodArrayList,"Food");
            }
        });


    }

    @Override
    protected void onResume() {
        //CheckDownloadComplete(foodArrayList, "Food");
        super.onResume();
    }
}
