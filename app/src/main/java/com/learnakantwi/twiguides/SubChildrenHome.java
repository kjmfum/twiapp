package com.learnakantwi.twiguides;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import static com.learnakantwi.twiguides.AlphabetsActivity.alphabetArray;
import static com.learnakantwi.twiguides.ChildrenHome.childrenArray;
import static com.learnakantwi.twiguides.SubChildrenAnimals.childrenAnimalsArrayList;

public class SubChildrenHome extends AppCompatActivity {

    static ArrayList<HomeButton> homeButtonArrayList;
    //static ArrayList<Children> childrenArray;
    public InterstitialAd mInterstitialAd;
    AdView mAdView;
    AdView mAdView1;
    ListView homeListView;
    MediaPlayer mediaPlayer;
    StorageReference storageReference;
    Toast toast;
    PlayFromFirebase playFromFirebaseChildren;

    public void downloadAllClick(){
        downloadClickAlphabet();
        downloadClick();
    }

    public void downloadFileAlphabet(final Context context, final String filename, final String fileExtension, final String url) {

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
                    request.setDestinationInExternalFilesDir(getApplicationContext(), Environment.DIRECTORY_MUSIC + "/CHILDREN", filename + fileExtension);
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

                    //Toast.makeText(getApplicationContext(), "Lost Internet Connection", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            Toast.makeText(this, "Please connect to Internet to download audio ", Toast.LENGTH_SHORT).show();
        }
    }




    public void downloadOnlyA (final String filename){
        if (isNetworkAvailable()){

            final StorageReference musicRef = storageReference.child("/AllTwi/" + filename + ".m4a");
            musicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    String url = uri.toString();
                    downloadFileAlphabet(getApplicationContext(), filename, ".m4a", url);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    // Toast.makeText(getApplicationContext(), "Lost Internet Connection", Toast.LENGTH_SHORT).show();
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

    public void goToMainMenu()
    {
        Intent intent = new Intent(getApplicationContext(), SubPHomeMainActivity.class);
        startActivity(intent);
    }


    public void downloadClick () {
        int counter = 0;

            for (int j = 0; j < childrenArray.size(); j++) {
                String bb;
                bb = childrenArray.get(j).getTwiChildren();
               // bb = playFromFirebaseChildren.viewTextConvert(bb);

                File myFiles = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/CHILDREN/" + bb + ".m4a");
                if (myFiles.exists()) {
                    counter++;
                }
            }
            if (counter == childrenArray.size()) {
               // Toast.makeText(this, "Numbers downloaded ", Toast.LENGTH_SHORT).show();
                Log.i("Check","Numbers Downloaded");

            } else {

                if (isNetworkAvailable()) {
                    toast.setText("Downloading...");
                    toast.show();
                }
                if (isNetworkAvailable()) {
                    String bb;
                    for (int i = 0; i < childrenArray.size(); i++) {

                        bb = childrenArray.get(i).getTwiChildren();
                        bb = PlayFromFirebase.viewTextConvert(bb);
                        File myFile = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/CHILDREN/" + bb + ".m4a");
                        if (!myFile.exists()) {
                            if (isNetworkAvailable()){
                                downloadOnly(bb);
                            }
                        }

                        // Toast.makeText(this, , Toast.LENGTH_SHORT).show();

                        else{
                            Toast.makeText(this, "Please Connect to the Inernet", Toast.LENGTH_SHORT).show(); //if (i + 1 == alphabetArray.size()) {
                            break;
                        }

                    }

                }
                else {
                    Toast.makeText(this, "Please connect to the Internet to download audio", Toast.LENGTH_SHORT).show();
                }
            }

    }

    public void downloadClickAlphabet () {
        int counter = 0;
        if (isNetworkAvailable()) {
            for (int j = 0; j < alphabetArray.size(); j++) {

                String bb = alphabetArray.get(j).getLower();
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
            if (counter == alphabetArray.size()) {
                //Toast.makeText(this, "Alphabets downloaded ", Toast.LENGTH_SHORT).show();
                Log.i("Check","Alphabet Downloaded");
            } else {

                Toast.makeText(this, "Downloading...", Toast.LENGTH_LONG).show();

                for (int i = 0; i < alphabetArray.size(); i++) {
                    String b = alphabetArray.get(i).getLower();
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
                    File myFile = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/CHILDREN" + b + ".m4a");
                    if (!myFile.exists()) {
                        if (isNetworkAvailable()){
                            downloadOnlyA(b);
                        }
                        else{
                            Toast.makeText(this, "Please Connect to the Internet", Toast.LENGTH_SHORT).show(); //if (i + 1 == alphabetArray.size()) {
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

    public void downloadClickAnimals () {
        int counter = 0;
        String bb;

        for (int j = 0; j < childrenAnimalsArrayList.size(); j++) {


            bb = childrenAnimalsArrayList.get(j).getTwiAnimals();
            bb = PlayFromFirebase.viewTextConvert(bb);

            File myFiles = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/" + bb + ".m4a");
            if (myFiles.exists()) {
                counter++;
            }

        }

        if (counter == childrenAnimalsArrayList.size()) {
            /*toast.setText("Animals Downloaded");
            toast.show();*/
            Log.i("Check","Animals Downloaded");

        } else {
            if (isNetworkAvailable()) {
                toast.setText("Downloading...");
                toast.show();
            }


            if (isNetworkAvailable()) {

                // toast.setText(counter + " -- "+ allArrayList.size());
                // toast.show();

                for (int i = 0; i < childrenAnimalsArrayList.size(); i++) {

                    bb = childrenAnimalsArrayList.get(i).getTwiAnimals();
                    bb = PlayFromFirebase.viewTextConvert(bb);

                    File myFile = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/" + bb + ".m4a");
                    if (!myFile.exists()) {
                        if (isNetworkAvailable()) {
                            downloadOnlyAnimals(bb);
                        } else {
                            toast.setText("Please connect to the Internet");
                            toast.show();
                            break;
                        }

                    }

                }
            } else {
                toast.setText("Please connect to the Internet to download audio");
                toast.show();
            }
        }
    }
    public void downloadOnlyAnimals(final String filename){
        if (isNetworkAvailable()){

            final StorageReference musicRef = storageReference.child("/AllTwi/" + filename + ".m4a");
            musicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    String url = uri.toString();
                    downloadFileAlphabet(getApplicationContext(), filename, ".m4a", url);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    //Toast.makeText(getApplicationContext(), "Lost Internet Connection", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            Toast.makeText(this, "Please connect to Internet to download audio ", Toast.LENGTH_SHORT).show();
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
                                case "Alphabets":
                                    goToAlphabets();
                                    return;
                                case "Animals":
                                    goToAnimals();
                                    return;
                                case "Body Parts":
                                    goToBodyparts();
                                    return;
                                case "Colours":
                                    goToColours();
                                    return;
                                case "Days of Week":
                                    goToDaysOfWk();
                                    return;
                                case "Expressions":
                                    goToCommonExpressionsa();
                                    return;
                                case "Family":
                                    goToFamily();
                                    return;
                                case "Food":
                                    goToFood();
                                    return;
                                case "Months":
                                    goToMonths();
                                    return;
                                case "Numbers":
                                    goToNumber();
                                    return;
                                case "Time":
                                    goToTime();
                                    return;
                                case "Weather":
                                    goToWeather();
                                    return;
                                case "Business":
                                    goToBusiness();
                                    return;
                                case "Search":
                                    goToAll();
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
                goToMainMenu();
                return true;
            case R.id.downloadAllAudio:
                downloadAllClick();
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


    public void goToBusiness(){
        Intent intent = new Intent(getApplicationContext(), SubChildrenAnimalQuiz.class);
        startActivity(intent);

    }

    public void goToWeb() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.udemy.com/course/learn-akan-twi/?referralCode=6D321CE6AEE1834CCB0F"));
        startActivity(intent);
    }

    public void goToAlphabets() {
        Intent intent = new Intent(getApplicationContext(), ChildrenAlphabet.class);
        startActivity(intent);
    }

    public void goToNumber() {
        //Intent intent = new Intent(getApplicationContext(), ChildrenNumberCount.class);
        Intent intent = new Intent(getApplicationContext(), ChildrenNumbersActivityRV.class);
        startActivity(intent);
    }

    public void goToDaysOfWk() {
        Intent intent = new Intent(getApplicationContext(), QuizDaysOfWeek.class);
        startActivity(intent);
    }

    public void goToTime() {
        Intent intent = new Intent(getApplicationContext(), QuizTime.class);
        startActivity(intent);
    }

    public void goToFamily() {
        Intent intent = new Intent(getApplicationContext(), QuizFamily.class);
        startActivity(intent);
    }

    public void goToWeather() {
        Intent intent = new Intent(getApplicationContext(), QuizWeather.class);
        startActivity(intent);
    }

    public void goToMonths() {
        Intent intent = new Intent(getApplicationContext(), QuizMonths.class);
        startActivity(intent);
    }

   /* public void goToPronouns() {
        Intent intent = new Intent(getApplicationContext(), PronounsActivity.class);
        startActivity(intent);
    }*/

    public void goToColours() {
        Intent intent = new Intent(getApplicationContext(), QuizColours.class);
        startActivity(intent);
    }

    public void goToAnimals() {
       // Intent intent = new Intent(getApplicationContext(), SubChildrenAnimals.class);
       // Intent intent = new Intent(getApplicationContext(), ReadingLessonTestRecycler.class);
        Intent intent = new Intent(getApplicationContext(), SubChildrenAnimalsRecyclerView.class);
        startActivity(intent);
    }

    public void goToBodyparts() {
        Intent intent = new Intent(getApplicationContext(), QuizBodyParts.class);
        startActivity(intent);
    }

    public void goToFood() {
        Intent intent = new Intent(getApplicationContext(), QuizFood.class);
        startActivity(intent);
    }

    public void goToCommonExpressionsa() {
        Intent intent = new Intent(getApplicationContext(), QuizCommonExpressionsa.class);
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_sub_home);

        ImageView topImage = findViewById(R.id.homeAdvertButton);
        topImage.setImageResource(R.drawable.childrenimage);

        playFromFirebaseChildren = new PlayFromFirebase();


        storageReference = FirebaseStorage.getInstance().getReference();
        toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);

        try {
            downloadClickAlphabet();
            downloadClick();
            downloadClickAnimals();
        }catch (Exception E){
            System.out.println("Errors: " + E);
        }
       /* try{
            downloadClickAnimals();
        }catch (Exception E){
            Toast.makeText(this, "Unable to Download Animals Audio", Toast.LENGTH_SHORT).show();
        }
*/
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

        homeButtonArrayList.add(new HomeButton("Alphabets", R.drawable.childrenalphabetimage));
        homeButtonArrayList.add(new HomeButton("Animals", R.drawable.childrenanimalsimage));
        homeButtonArrayList.add(new HomeButton("Animals Quiz", R.drawable.childrenanimalsimage));
        homeButtonArrayList.add(new HomeButton("Numbers", R.drawable.childrennumbersimage));


        HomeAdapter homeAdapter = new HomeAdapter(this, homeButtonArrayList);
        homeListView.setAdapter(homeAdapter);


        homeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String me1 = homeButtonArrayList.get(position).getNameofActivity();


                switch (me1){
                    case "Alphabets":
                        goToAlphabets();
                        return;
                    case "Animals":
                        goToAnimals();
                        return;
                    case "Numbers":
                        goToNumber();
                        return;
                    case "Time":
                        goToTime();
                        return;
                    case "Weather":
                        goToWeather();
                        return;
                    case "Animals Quiz":
                        goToBusiness();
                        return;
                    case "All":
                        goToAll();
                }
            }
        });





        //MobileAds.initialize(this, "ca-app-pub-6999427576830667~6251296006");


    }

}
