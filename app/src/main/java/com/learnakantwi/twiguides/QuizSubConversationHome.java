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
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
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
import java.util.Random;

import static android.Manifest.permission.INTERNET;
import static com.learnakantwi.twiguides.AllActivity.allArrayList;

public class QuizSubConversationHome extends AppCompatActivity {

    //ArrayList<HomeButton> homeButtonArrayList;
    ArrayList<String> subQuizHomeConversationArrayList;
    ListView homeListView;
    MediaPlayer mediaPlayer;

    public InterstitialAd mInterstitialAd;
    AdView mAdView;

    StorageReference storageReference;
    Toast toast;
    int audioToDownload;
    int downloadAllClickCount;

    String Ads = "";
    int Sub=0;
    int testShared;

    TextView tvSubscribeBanner;

   // SharedPreferences subscribed = getSharedPreferences("AdsDecision", MODE_PRIVATE);



    public void downloadCompleteToast(){
        toast.setText("Download Complete");
        toast.show();
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
    public void downloadOnly(final String filename){
        if (isNetworkAvailable()){

            final StorageReference musicRef = storageReference.child("/AllTwi/" + filename + ".m4a");
            musicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    String url = uri.toString();
                    downloadFile(getApplicationContext(), filename, ".m4a", url);
                    audioToDownload = audioToDownload-1;
                    if (audioToDownload==0){
                        downloadCompleteToast();
                        //toast.setText("Download Complete koraa");
                        //toast.show();
                        //Toast.makeText(context, "Download Complete", Toast.LENGTH_SHORT).show();
                        Log.i("Test1","Download complete");
                    }
                    else {
                       /* toast.setText("Downloading...");
                        toast.show();*/
                        Log.i("Test","Downloading");
                    }

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
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
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
                audioToDownload = allArrayList.size() - counter;
                //toast.setText(Integer.toString(audioToDownload));
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



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //MenuInflater menuInflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.quiz_home, menu);

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
                final ArrayList<String> results = new ArrayList<>();
                for (String x: subQuizHomeConversationArrayList ){

                    if(x.toLowerCase().contains(newText.toLowerCase())

                    ){
                        results.add(x);
                    }

                    ((ConversationMainAdapter)homeListView.getAdapter()).update(results);

                    homeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            String me1 = results.get(position);


                            switch (me1){
                                case "All Vocabulary":
                                    goToAllVocabularyTimedQuiz();
                                    return;
                                case "Numbers":
                                    goToNumberTimedQuiz();
                                    return;
                                case "HALL OF FAME":
                                    goToHallOfFameMain();
                                    return;
                                case "Children Animals":
                                    goToChildrenAnimals();
                                    return;
                                case "Animals":
                                    goToAnimals();
                                    return;
                                case "Download All Audio":
                                    downloadAll();
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

            case R.id.main:
                goToMain();
                return true;
            case R.id.searchAll:
                goToAll();
                return true;
            case R.id.videoCourse:
                goToWeb();
                return true;
            case R.id.rate:
                rateMe();
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

    public void goToMain(){
        if (Ads.equals("Ads")){
            Intent intent = new Intent(getApplicationContext(), HomeMainActivity.class);
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(getApplicationContext(), SubPHomeMainActivity.class);
            startActivity(intent);
        }

    }

    public void goToBusiness(){
        Intent intent = new Intent(getApplicationContext(), QuizBusiness.class);
        startActivity(intent);

    }

    public void goToWeb() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.udemy.com/course/learn-akan-twi/?referralCode=6D321CE6AEE1834CCB0F"));
        startActivity(intent);
    }

    public void goToAllVocabularyTimedQuiz(){
        Intent intent = new Intent(getApplicationContext(), QuizTimedAll.class);
        intent.putExtra("category","All Vocabulary");
        intent.putExtra("Ads", Ads);
        startActivity(intent);
    }

    public void goToHallOfFameMain(){
        Intent intent = new Intent(getApplicationContext(), QuizHighScoresHome.class);
        intent.putExtra("Ads", Ads);
       // intent.putExtra("from","no");
        startActivity(intent);
    }

    public void goToChildrenAnimals(){


       // intent.putExtra("category","Animals");

       // coming = intent.getStringExtra("coming");
        if (MainActivity.Subscribed !=1){
            Intent intent = new Intent(getApplicationContext(), PleaseSubscribePage.class);
           // intent.putExtra("coming","ads");
            startActivity(intent);
        }

        else {
           // Toast.makeText(this, "Two", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), QuizTimedChildrenAnimals.class);
        startActivity(intent);
            /*Intent intent = new Intent(getApplicationContext(), QuizTimedAll.class);
            intent.putExtra("category","Animals");
            //intent.putExtra("coming","soon");
            startActivity(intent);*/
        }

    }

    public void goToAnimals(){

        // coming = intent.getStringExtra("coming");
        if (Sub==0){
            Intent intent = new Intent(getApplicationContext(), QuizTimedAll.class);
            intent.putExtra("category","Animals");
            /*Intent intent = new Intent(getApplicationContext(), PleaseSubscribePage.class);
            intent.putExtra("coming","ads");*/
            startActivity(intent);
        }

        else {
            Intent intent = new Intent(getApplicationContext(), QuizTimedAll.class);
            intent.putExtra("category","Animals");
            //intent.putExtra("coming","soon");
            startActivity(intent);
        }

    }

    public void goToNumberTimedQuiz() {
        Intent intent = new Intent(getApplicationContext(), QuizTimedAll.class);
        intent.putExtra("category","Numbers");
        intent.putExtra("Ads", Ads);
        startActivity(intent);
    }



   /* public void goToPronouns() {
        Intent intent = new Intent(getApplicationContext(), PronounsActivity.class);
        startActivity(intent);
    }*/


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


    public void goToSubscriptionPage (View v){
        // Toast.makeText(this, String.valueOf(subscriptionState), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), InAppActivity.class);
        startActivity(intent);
    }

    public void goToPleaseSubPage(){
        Intent intent = new Intent(getApplicationContext(), PleaseSubscribePage.class);
        startActivity(intent);
    }

    public void goToCategory(String category){
        Intent intent;
        intent = new Intent(getApplicationContext(), QuizSubConversationIntroducing.class);
        intent.putExtra("arrayName",category);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizhome);


        tvSubscribeBanner = findViewById(R.id.tvSubscribe);

        if (MainActivity.Subscribed == 1){
            tvSubscribeBanner.setVisibility(View.GONE);
        }



        Intent intent = getIntent();
        Ads = intent.getStringExtra("Ads");

       // Toast.makeText(this, Ads, Toast.LENGTH_SHORT).show();


        storageReference = FirebaseStorage.getInstance().getReference();
        toast= Toast.makeText(this, "", Toast.LENGTH_SHORT);


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

        if (MainActivity.Subscribed != 1){

           /* random = new Random();
            showAdProbability = random.nextInt(10);*/

            mInterstitialAd = new InterstitialAd(this);
            mInterstitialAd.setAdUnitId(MainActivity.AdUnitInterstitial);
            mInterstitialAd.loadAd(new AdRequest.Builder().build());

            MobileAds.initialize(this, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {
                }
            });
            mAdView = findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        }
            subQuizHomeConversationArrayList =new ArrayList<>();
        subQuizHomeConversationArrayList.add("Introducing yourself");
        subQuizHomeConversationArrayList.add("Welcoming others");
        subQuizHomeConversationArrayList.add("On The Phone");
        subQuizHomeConversationArrayList.add("Apologies and Regret");
        subQuizHomeConversationArrayList.add("Asking and Giving Directions");
        subQuizHomeConversationArrayList.add("At the Hospital");

        homeListView = findViewById(R.id.homeListView);

        /*ArrayList<homeButtonArrayList = new ArrayList<>();
        homeListView = findViewById(R.id.homeListView);

        homeButtonArrayList.add(new HomeButton("HALL OF FAME", R.drawable.trophy));
        homeButtonArrayList.add(new HomeButton("All Vocabulary", R.drawable.vocabularyimage));
        homeButtonArrayList.add(new HomeButton("Numbers", R.drawable.numbers));
        homeButtonArrayList.add(new HomeButton("Animals", R.drawable.animalsimage));
        homeButtonArrayList.add(new HomeButton("Children Animals", R.drawable.childrenanimalsimage));*/



        ConversationMainAdapter homeAdapter = new ConversationMainAdapter(this, subQuizHomeConversationArrayList);
        homeListView.setAdapter(homeAdapter);




        homeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String me1 = subQuizHomeConversationArrayList.get(position);
                goToCategory(me1);


            }
        });
        //MobileAds.initialize(this, "ca-app-pub-6999427576830667~6251296006");

        SharedPreferences subscribed = getSharedPreferences("AdsDecision", MODE_PRIVATE);
        Sub = subscribed.getInt("Sub",0);
        //Toast.makeText(this, "Hello: "+ Sub, Toast.LENGTH_SHORT).show();



    }
}

