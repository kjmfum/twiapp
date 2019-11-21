package com.learnakantwi.twiguides;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.common.util.Strings;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;

public class ChildrenHome extends AppCompatActivity {



    static ArrayList<Children> childrenArray;
    AdView mAdView;
    AdView mAdView1;
    StorageReference storageReference;
    Toast toast;

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
            toast.setText("Please Connect to the Internet to Download Audio");
            toast.show();

        }
    }

    public void downloadOnly(final String filename){
        if (isNetworkAvailable()){

            final StorageReference musicRef = storageReference.child("/Children/" + filename + ".m4a");
            musicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    String url = uri.toString();
                    downloadFile(getApplicationContext(), filename, ".m4a", url);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(getApplicationContext(), "Lost Internet Connection", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            Toast.makeText(this, "Please connect to Internet to download audio ", Toast.LENGTH_SHORT).show();
        }
    }
    //LinearLayout childrenHome;
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public void downloadClick () {
        int counter = 0;
        if (isNetworkAvailable()) {
            for (int j = 0; j < childrenArray.size(); j++) {

                String bb = childrenArray.get(j).getTwiChildren();
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
            if (counter == childrenArray.size()) {
                //Toast.makeText(this, "All downloaded ", Toast.LENGTH_SHORT).show();
                Log.i("Great","All downloaded");
            } else {

                Toast.makeText(this, "Downloading...", Toast.LENGTH_SHORT).show();

                for (int i = 0; i < childrenArray.size(); i++) {
                    String b = childrenArray.get(i).getTwiChildren();
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

                    // Toast.makeText(this, , Toast.LENGTH_SHORT).show();
                    File myFile = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/" + b + ".m4a");
                    if (!myFile.exists()) {
                        if (isNetworkAvailable()){
                            downloadOnly(b);
                        }
                        else{
                            Toast.makeText(this, "Please Connect to the Inernet", Toast.LENGTH_SHORT).show(); //if (i + 1 == alphabetArray.size()) {
                            break;
                        }

                    }

                }

            }
        }
        else{
            Toast.makeText(this, "Please connect to the Internet to download audio", Toast.LENGTH_SHORT).show();
        }
    }



    public void goToWeb() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.udemy.com/course/learn-akan-twi/?couponCode=FDISCOUNT1"));
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //MenuInflater menuInflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.main_menu_children, menu);
        return super.onCreateOptionsMenu(menu);


    }

    public void goToMain(){
        Intent intent = new Intent(getApplicationContext(), Home.class);
        startActivity(intent);
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
                return true;
            case R.id.goback:
                finish();
            default:
                return false;
        }
    }


    public void goToChildren() {
        Intent intent = new Intent(getApplicationContext(), ChildrenNumberCount.class);
        startActivity(intent);
    }

    public void goToChildrenAplphabet(){
        Intent intent = new Intent(getApplicationContext(), ChildrenAlphabet.class);
        startActivity(intent);

    }

    public void imageClick(View v){
        //Toast.makeText(ChildrenHome.this, "YEs", Toast.LENGTH_SHORT).show();
        //int me1 = v.getId();

        int idview= v.getId();

        ImageView blabla = v.findViewById(idview);
        String a = (String) blabla.getTag();

        switch (a){
            case "numbers":
                goToChildren();
                return;
            case "alphabets":
                goToChildrenAplphabet();
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_children_home);


        toast = Toast.makeText(getApplicationContext(), "" , Toast.LENGTH_SHORT);

        storageReference = FirebaseStorage.getInstance().getReference();


        childrenArray = new ArrayList<>();

        childrenArray.add(new Children("1"));
        childrenArray.add(new Children("0"));
        childrenArray.add(new Children("2"));
        childrenArray.add(new Children("3"));
        childrenArray.add(new Children("4"));
        childrenArray.add(new Children("5"));
        childrenArray.add(new Children("6"));
        childrenArray.add(new Children("7"));
        childrenArray.add(new Children("8"));
        childrenArray.add(new Children("9"));
        childrenArray.add(new Children("10"));
        childrenArray.add(new Children("11"));
        childrenArray.add(new Children("12"));
        childrenArray.add(new Children("13"));
        childrenArray.add(new Children("14"));
        childrenArray.add(new Children("15"));
        childrenArray.add(new Children("16"));
        childrenArray.add(new Children("17"));
        childrenArray.add(new Children("18"));
        childrenArray.add(new Children("19"));
        childrenArray.add(new Children("20"));



        downloadClick();


       /* numbers = findViewById(R.id.childrenNumbers);
        findViewById(R.id.childrenHomeHorizontalScroll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChildrenHome.this, "YEs", Toast.LENGTH_SHORT).show();
                int me1 = v.getId();

                int idview= v.getId();

                ImageView blabla = v.findViewById(idview);
                String a = (String) blabla.getTag();

                switch (a){
                    case "numbers":
                        goToChildren();
                        return;
                }


            }
        });
*/

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView1 = findViewById(R.id.adView1);
        AdRequest adRequest1 = new AdRequest.Builder().build();
        mAdView1.loadAd(adRequest1);
    }


}
