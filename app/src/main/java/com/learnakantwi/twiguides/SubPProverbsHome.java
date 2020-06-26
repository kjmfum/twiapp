package com.learnakantwi.twiguides;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;

import static android.Manifest.permission.INTERNET;
import static com.learnakantwi.twiguides.ProverbsActivity.proverbsArrayList;

//import android.support.v7.app.AppCompatActivity;

public class SubPProverbsHome extends AppCompatActivity {
    //  app:adUnitId="ca-app-pub-6999427576830667~6251296006"

    static ArrayList<HomeButton> homeButtonArrayList;
    public InterstitialAd mInterstitialAd;
    ListView homeListView;
    AdView mAdView;
    AdView mAdView1;
    MediaPlayer mediaPlayer;
    StorageReference storageReference;

    Toast toast;


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public void downloadOnly(final String filename){
        if (isNetworkAvailable()){
            final StorageReference musicRef = storageReference.child("/Proverbs/" + filename + ".m4a");
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
        checkFileinFolder();
    }

    /*public boolean hasInternetAccess() {

        Thread myThread = new Thread(runnable);
        myThread.start();
        return isRunning;
    }*/

    public boolean checkFileinFolder() {

        int counter = 0;

        for (int j = 0; j < proverbsArrayList.size(); j++) {

            String bb = proverbsArrayList.get(j).getTwiProverb();
            bb = bb.toLowerCase();
            boolean dd = bb.contains("ɔ");
            boolean ee = bb.contains("ɛ");
            if (dd || ee) {
                bb = bb.replace("ɔ", "x");
                bb = bb.replace("ɛ", "q");
            }

            if (bb.contains(" ") || bb.contains("twi:") || bb.contains("/") || bb.contains(";") || bb.contains("'") || bb.contains(",") || bb.contains("(") || bb.contains(")") || bb.contains("-") | bb.contains("?") | bb.contains("...")) {
                bb = bb.replace(" ", "");
                bb = bb.replace("/", "");
                bb = bb.replace(",", "");
                bb = bb.replace("(", "");
                bb = bb.replace(")", "");
                bb = bb.replace("-", "");
                bb = bb.replace("?", "");
                bb = bb.replace("...", "");
                bb = bb.replace("twi:", "");
                bb = bb.replace("'", "");
                bb = bb.replace(";", "");
            }
            File myFiles = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/PROVERBS/"+bb+ ".m4a");
            if (myFiles.exists()) {
                counter++;
            }

        }
        if (counter == proverbsArrayList.size()) {
            toast.setText("Download Completed");
            toast.show();
            return true;
        }
        else {
            return false;
        }
    }

    public void downloadClick () {
        int counter = 0;

        /*toast.setText("Got here");
        toast.show();
*/
        if (isNetworkAvailable()) {
            for (int j = 0; j < proverbsArrayList.size(); j++) {

                String bb = proverbsArrayList.get(j).getTwiProverb();
                bb= bb.toLowerCase();
                boolean dd = bb.contains("ɔ");
                boolean ee = bb.contains("ɛ");
                if (dd || ee) {
                    bb = bb.replace("ɔ", "x");
                    bb = bb.replace("ɛ", "q");
                }

                if (bb.contains(" ") ||bb.contains("twi:") || bb.contains("/") || bb.contains(";")|| bb.contains("'")|| bb.contains(",") || bb.contains("(") || bb.contains(")") || bb.contains("-") | bb.contains("?")| bb.contains("...")) {
                    bb = bb.replace(" ", "");
                    bb = bb.replace("/", "");
                    bb= bb.replace(",","");
                    bb= bb.replace("(","");
                    bb= bb.replace(")","");
                    bb= bb.replace("-","");
                    bb= bb.replace("?","");
                    bb= bb.replace("...","");
                    bb= bb.replace("twi:","");
                    bb= bb.replace("'","");
                    bb= bb.replace(";","");
                }
                File myFiles = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/PROVERBS/"+bb+ ".m4a");
                if (myFiles.exists()) {
                    counter++;
                }

            }
            if (counter == proverbsArrayList.size()) {
                toast.setText("All downloaded");
                toast.show();

            } else {
                toast.setText("Downloading...");
                toast.show();

                // toast.setText(counter + " -- "+ allArrayList.size());
                // toast.show();

                for (int i = 0; i < proverbsArrayList.size(); i++) {
                    String b = proverbsArrayList.get(i).getTwiProverb().toLowerCase();
                    boolean d = b.contains("ɔ");
                    boolean e = b.contains("ɛ");
                    if (d || e) {
                        b = b.replace("ɔ", "x");
                        b = b.replace("ɛ", "q");
                    }

                    if (b.contains(" ") ||b.contains("twi:") || b.contains("/") || b.contains(";")|| b.contains("'")|| b.contains(",") || b.contains("(") || b.contains(")") || b.contains("-") | b.contains("?")| b.contains("...")) {
                        b = b.replace(" ", "");
                        b = b.replace("/", "");
                        b= b.replace(",","");
                        b= b.replace("(","");
                        b= b.replace(")","");
                        b= b.replace("-","");
                        b= b.replace("?","");
                        b= b.replace("...","");
                        b= b.replace("twi:","");
                        b= b.replace("'","");
                        b= b.replace(";","");
                    }

                    File myFile = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/PROVERBS" + b + ".m4a");
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
                    request.setDestinationInExternalFilesDir(getApplicationContext(), Environment.DIRECTORY_MUSIC + "/PROVERBS" , filename + fileExtension);
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
        getMenuInflater().inflate(R.menu.main_menu_proverbs, menu);


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
                final ArrayList<HomeButton> results = new ArrayList<>();
                for (HomeButton x: homeButtonArrayList ){

                    if(x.getNameofActivity().toLowerCase().contains(newText.toLowerCase())

                    ){
                        results.add(x);
                    }

                    ((HomeAdapter)homeListView.getAdapter()).update(results);

                    homeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            String me1 = results.get(position).getNameofActivity();


                            switch (me1){
                                case "Proverbs and Their Meaning":
                                    goToProverbs();
                                    return;
                                case "Download Proverb Audio":
                                    downloadClick();
                                    return;
                                case "Proverbs Quiz":
                                    goToProverbsQuiz();
                                    return;
                            }
                        }
                    });
                }


                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
           /*case R.id.settings:
                Log.i("Menu Item Selected", "Settings");
                playAll();
                return true;
            case R.id.alphabets:
                Log.i("Menu Item Selected", "Alphabets");
                return  true;*/

            case R.id.downloadAllAudio:
                downloadClick();
                return true;
            case R.id.searchAll:
                goToAll();
                return true;
            case R.id.videoCourse:
                goToWeb();
                return true;
            case R.id.main:
                goToMainMenu();
                return true;
            case R.id.share:
                shareApp();
                return true;
            case R.id.dailyTwiAlert:
                tunOnDailyTwi();
                return true;
            default:
                return false;
        }
    }

    public void shareApp(){
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        //sharingIntent.setAction("http://play.google.com/store/apps/details?id=" + getPackageName());
        sharingIntent.setType("text/plain");
        String shareBody = "http://play.google.com/store/apps/details?id=" + getPackageName();
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Please install this Nice Android Twi App");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    public void rateMe() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + getPackageName())));
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
        }
    }


    public void goToQuizAll() {
        Intent intent = new Intent(getApplicationContext(), QuizAll.class);
        startActivity(intent);
    }

    public void goToProverbsQuiz() {
        Intent intent = new Intent(getApplicationContext(), ProverbsQuizActivity.class);
        startActivity(intent);
    }


        public void goToMainMenu()
        {
            Intent intent = new Intent(getApplicationContext(), SubPHomeMainActivity.class);
            startActivity(intent);
        }
    public void goToProverbs() {
        Intent intent = new Intent(getApplicationContext(), SubPProverbsActivity.class);
        startActivity(intent);
    }

    public void goToWeb() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.udemy.com/course/learn-akan-twi/?couponCode=FDISCOUNT1"));
        startActivity(intent);
    }

    public void goToAlphabets() {
        Intent intent = new Intent(getApplicationContext(), QuizAlphabet.class);
        startActivity(intent);
    }

    public void goToAll() {
        Intent intent = new Intent(getApplicationContext(), QuizAll.class);
        startActivity(intent);
    }


    public void tunOnDailyTwi() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.learnakantwi.twiguides", Context.MODE_PRIVATE);
        String dailyTwiPreference = sharedPreferences.getString("DailyTwiPreference", "Yes");
        sharedPreferences.edit().putString("DailyTwiPreference", "Yes").apply();
        Toast.makeText(this, "Daily Twi Alerts Turned On", Toast.LENGTH_SHORT).show();
    }

    public void advert() {


        final SharedPreferences sharedPreferences = this.getSharedPreferences("com.learnakantwi.twiguides", Context.MODE_PRIVATE);
        //  sharedPreferences.edit().putString("AdvertPreference", "No").apply();
        String advertPreference = sharedPreferences.getString("AdvertPreference", "No");


        assert advertPreference != null;
        if (!advertPreference.equals("Yes")) {
            new AlertDialog.Builder(this)
                    .setIcon(R.drawable.learnakantwiimage)
                    .setTitle("We need your support")
                    .setMessage("Would You Like To View An Advert To Support Us?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sharedPreferences.edit().putString("AdvertPreference", "Yes").apply();
                            if (mInterstitialAd.isLoaded()) {
                                mInterstitialAd.show();
                            } else {
                                Log.d("TAG", "The interstitial wasn't loaded yet.");
                            }
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    sharedPreferences.edit().putString("AdvertPreference", "No").apply();
                                }
                            }
                    )
                    .show();
        } else {
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();

            } else {
                Log.d("TAG", "The interstitial wasn't loaded yet.");

            }
        }

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }

//In your case, you do not need the LinearLayout and ImageView at all. Just add android:drawableLeft="@drawable/up_count_big" to your TextView.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizhome_sub);

        ImageView topImage = findViewById(R.id.homeAdvertButton);
        topImage.setImageResource(R.drawable.proverbsimage);

        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);


        storageReference = FirebaseStorage.getInstance().getReference();

        // Function to check and request permission
        // checkPermission(INTERNET, 100);

        if (Build.VERSION.SDK_INT > 22) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    INTERNET,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.SET_ALARM
            }, 1);

            if (Build.VERSION.SDK_INT > 27) {
                requestPermissions(new String[]{Manifest.permission.FOREGROUND_SERVICE
                }, 1);
            }
        }


        homeButtonArrayList = new ArrayList<>();
        homeListView = findViewById(R.id.homeListView);

        homeButtonArrayList.add(new HomeButton("Proverbs and Their Meaning", R.drawable.proverbsimage));
        homeButtonArrayList.add(new HomeButton("Proverbs Quiz", R.drawable.quizimage));
        homeButtonArrayList.add(new HomeButton("Download Proverb Audio", R.drawable.ic_download_audio));

        HomeAdapter homeAdapter = new HomeAdapter(this, homeButtonArrayList);
        homeListView.setAdapter(homeAdapter);




        homeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String me1 = homeButtonArrayList.get(position).getNameofActivity();


                switch (me1){
                    case "Proverbs and Their Meaning":
                        goToProverbs();
                        return;
                    case "Download Proverb Audio":
                        downloadClick();
                        return;
                    case "Proverbs Quiz":
                        goToProverbsQuiz();
                        return;
                }
            }
        });





        //MobileAds.initialize(this, "ca-app-pub-6999427576830667~6251296006");


    }
}



