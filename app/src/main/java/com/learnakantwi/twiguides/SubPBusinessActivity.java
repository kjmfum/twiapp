package com.learnakantwi.twiguides;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.appodeal.ads.Appodeal;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Random;

import static com.learnakantwi.twiguides.BusinessActivity.businessArrayList;
import static com.learnakantwi.twiguides.FoodActivity.foodArrayList;
import static com.learnakantwi.twiguides.MainActivity.largeFont;
import static com.learnakantwi.twiguides.MainActivity.longDelay;
import static com.learnakantwi.twiguides.MainActivity.shortDelay;
import static com.learnakantwi.twiguides.MainActivity.smallFont;
import static com.learnakantwi.twiguides.MainActivity.textLength;

public class SubPBusinessActivity extends AppCompatActivity  implements  RVBusinessAdapter.onClickRecycle{

    RecyclerView foodListView;
    PlayFromFirebase convertAndPlay;
    RVBusinessAdapter businessAdapter;
    ArrayList<Business> recycleArrayList;


    StorageReference storageReference;
    MediaPlayer playFromDevice;
    MediaPlayer mp1;

    int count= 0;
    String textSlideshow = "Start Business Slideshow";
    int ArraySize = businessArrayList.size();
    ImageButton playButton;
    ImageButton pauseButton;
    ImageButton nextButton;
    ImageButton previousButton;
    ImageButton muteButton;
    ImageButton unmuteButton;
    ImageButton repeatButton;
    ImageButton repeatOne;
    Boolean slideshowBool = false;
    Boolean unMuted = true;
    Boolean repeat = false;
    Boolean repeat1 = false;
    Button btSlideText;
    TextView tvStartSlideShow;
    TextView tvNumberWord;
    Handler handler1;
    Runnable ranable;
    Random random;


    long delayTime=3000;
    int showAdProbability;


    Toast toast;
    boolean isRunning =false;
    public Runnable runnable = new Runnable() {

        @Override
        public void run() {
            try {
                URL url = new URL("http://www.google.com");
                URLConnection connection = url.openConnection();
                connection.connect();
                isRunning = true;
                System.out.println("Internet is now connected");
            } catch (MalformedURLException e) {
                isRunning =false;
                //System.out.println("Internet is now not connected 1");
            } catch (IOException e) {
                isRunning=false;
                //System.out.println("Internet is now not connected 2");
            }
        }
    };

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
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


    public void downloadClick () {
        int counter = 0;
        int counter1 =0;

        if (isNetworkAvailable()) {
            for (int j = 0; j < businessArrayList.size(); j++) {

                String bb = businessArrayList.get(j).getTwiBusiness();
                bb= bb.toLowerCase();
                boolean dd = bb.contains("ɔ");
                boolean ee = bb.contains("ɛ");
                if (dd || ee) {
                    bb = bb.replace("ɔ", "x");
                    bb = bb.replace("ɛ", "q");
                }

                if (bb.contains(" ") || bb.contains("/") || bb.contains(",") || bb.contains("(") || bb.contains(")") || bb.contains("-") || bb.contains("?") || bb.contains("'")| bb.contains("...")) {
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
            if (counter == businessArrayList.size()) {
                toast.setText("All downloaded");
                toast.show();

            } else {
                toast.setText("Downloading...");
                toast.show();

                for (int i = 0; i < businessArrayList.size(); i++) {
                    String b = businessArrayList.get(i).getTwiBusiness().toLowerCase();
                    boolean d = b.contains("ɔ");
                    boolean e = b.contains("ɛ");
                    if (d || e) {
                        b = b.replace("ɔ", "x");
                        b = b.replace("ɛ", "q");
                    }

                    if (b.contains(" ") || b.contains("/") || b.contains(",") || b.contains("(") || b.contains(")") || b.contains("-") | b.contains("?")| b.contains("...")) {
                        b = b.replace(" ", "");
                        b = b.replace("/", "");
                        b= b.replace(",","");
                        b= b.replace("(","");
                        b= b.replace(")","");
                        b= b.replace("-","");
                        b= b.replace("?","");
                        b= b.replace("...","");
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
        }

        else {
            toast.setText("Please Connect to the Internet");
            toast.show();
        }

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
                        toast.setText(appearText);
                        toast.show();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {

            if (isNetworkAvailable()){
                final StorageReference musicRef = storageReference.child("/AllTwi/" + filename + ".m4a");
                musicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String url = uri.toString();
                        playFromFirebase(musicRef);
                        downloadFile(getApplicationContext(), filename, ".m4a", url);
                        toast.setText(appearText);
                        toast.show();
                        //Toast.makeText(getApplicationContext(), appearText, Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "No Internet", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else {
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
        getMenuInflater().inflate(R.menu.main_menu, menu);

        final MenuItem item = menu.findItem(R.id.menusearch);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Toast.makeText(FoodActivity.this, query, Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                businessAdapter.getFilter().filter(newText);

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
            case R.id.videoCourse:
                goToWeb();
            case R.id.quiz1:
                goToQuizBusiness();
                return true;
            case R.id.downloadAudio:
                downloadClick();
                return true;
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
    public void goToQuizBusiness() {
        Intent intent = new Intent(getApplicationContext(), QuizSubBusiness.class);
        startActivity(intent);
    }

    public void log2(View view) {
        int idview = view.getId();

        TextView blabla = view.findViewById(idview);
        String a = (String) blabla.getText();

        Toast.makeText(this, a, Toast.LENGTH_SHORT).show();
    }

    public void timeClick(View view){

        int idview= view.getId();

        TextView blabla = view.findViewById(idview);
        String a = (String) blabla.getText();

        String b = a.toLowerCase();

        boolean d = b.contains("ɔ");
        boolean e = b.contains("ɛ");

        if (d || e ){
            b= b.replace("ɔ","x");
            b= b.replace("ɛ","q");
        }


        if (b.contains(" ") || b.contains("/") || b.contains(",") || b.contains("(") || b.contains(")") || b.contains("-") | b.contains("?")| b.contains("...")) {
            b = b.replace(" ", "");
            b = b.replace("/", "");
            b= b.replace(",","");
            b= b.replace("(","");
            b= b.replace(")","");
            b= b.replace("-","");
            b= b.replace("?","");
            b= b.replace("...","");
        }

        playFromFileOrDownload(b,a);
    }

    public void playFromFileOrDownload(final String filename){
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
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {

            if (isNetworkAvailable()){
                final StorageReference musicRef = storageReference.child("/AllTwi/" + filename + ".m4a");
                musicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String url = uri.toString();
                        playFromFirebase(musicRef);
                        downloadFile(getApplicationContext(), filename, ".m4a", url);
                        //Toast.makeText(getApplicationContext(), appearText, Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "No Internet", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else {
                toast.setText("Please connect to Internet to download audio");
                toast.show();
            }


        }
    }

    public void slideshow() {

        /*toast.setText("Proverbs change after 6 seconds");
        toast.show();*/
        slideshowBool = true;

        count = 0;
        handler1.postDelayed(ranable, 2);

        /*slideshowBool = true;

        if (slideshowBool){
            count = position;
            handler1.postDelayed(ranable, 2);
        }*/

    }

    public void slideshow(View v) {

        playButton.setVisibility(View.INVISIBLE);
        pauseButton.setVisibility(View.VISIBLE);

        handler1.postDelayed(ranable, 2);

        slideshowBool = true;
            /*
        if (slideshowBool){
            count = position;
            handler1.postDelayed(ranable, 2);
        }*/

    }


    public void pauseSlideshow(View view) {

        //Log.i("Mee1","Hi b4pause"+ count);
        count--;
        // Log.i("Mee1","Hi pause"+ count);

        pauseButton.setVisibility(View.INVISIBLE);
        pauseButton.setVisibility(View.INVISIBLE);
        playButton.setVisibility(View.VISIBLE);

        toast.setText("Paused");
        toast.show();

        //slideshowBool = false;

        if (handler1 !=null){
            handler1.removeCallbacks(ranable);
        }
        if (playFromDevice!=null){
            playFromDevice.stop();
        }



        //proverbsViewFlipper.getChildCount();
    }
    public void pauseSlideshow() {

        pauseButton.setVisibility(View.INVISIBLE);
        pauseButton.setVisibility(View.INVISIBLE);
        playButton.setVisibility(View.VISIBLE);


        //slideshowBool = false;

        if (handler1 !=null){
            handler1.removeCallbacks(ranable);
        }
        if (playFromDevice!=null){
            playFromDevice.stop();
        }



        //proverbsViewFlipper.getChildCount();
    }

    public void previous (View view){
        pauseSlideshow();
        //proverbsViewFlipper.showPrevious();

        Log.i("Mee1","Hi b4"+ count);
        // count = count-1;
        if (!slideshowBool){
            if (count!=0){
                count--;
            }
        }
        else{
            if (count>=2){
                count= count-2;
            }
            slideshowBool = false;
        }

        Log.i("Mee1","Hi after "+ count);
        // int position = proverbsViewFlipper.getDisplayedChild();

        String a = businessArrayList.get(count).getTwiBusiness();
        String c = businessArrayList.get(count).getEnglishBusiness();

        btSlideText.setText(a);
        tvNumberWord.setText(c);


        String b = PlayFromFirebase.viewTextConvert(a);

        if(b.length()>textLength){
            btSlideText.setTextSize(smallFont);

        }else{
            btSlideText.setTextSize(largeFont);
        }

        if (unMuted){
            playFromFileOrDownload(b);
        }
        else{
            toast.setText("Sound Muted");
            toast.show();
        }

       /* if (unMuted){
            playFromFileOrDownload(b, a);
        }*/

    }

    public void next(View view){
        pauseSlideshow();
        //proverbsViewFlipper.stopFlipping();
        Log.i("Mee1","Hi next b4 "+ count);
        if (!slideshowBool){
            count++;
        }
        else{
            slideshowBool = false;
        }



        if (count>=ArraySize-1){
            count = ArraySize-1;
            toast.setText("The End");
            toast.show();

        }


        String a = businessArrayList.get(count).getTwiBusiness();
        String c = businessArrayList.get(count).getEnglishBusiness();


        btSlideText.setText(a);
        tvNumberWord.setText(c);

        String b = PlayFromFirebase.viewTextConvert(a);

        if(b.length()>textLength){
            btSlideText.setTextSize(smallFont);

        }else{
            btSlideText.setTextSize(largeFont);
        }
        if (unMuted){
            playFromFileOrDownload(b);
        }
        else{
            toast.setText("Sound Muted");
            toast.show();
        }


    }

    public void advert1() {

        if (Appodeal.isLoaded(Appodeal.INTERSTITIAL)) {
            Appodeal.show(this, Appodeal.INTERSTITIAL);
        }

        //  Appodeal.cache(this, Appodeal.INTERSTITIAL);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_pfamily_two);


        isNetworkAvailable();

        toast = Toast.makeText(getApplicationContext(), "Tap to Listen" , Toast.LENGTH_LONG);
        toast.show();


        if (MainActivity.Subscribed != 1){

            random = new Random();
            showAdProbability = random.nextInt(10);

            Appodeal.cache(this, Appodeal.INTERSTITIAL);

            Appodeal.show(this, Appodeal.BANNER_BOTTOM);
        }

        btSlideText = findViewById(R.id.btSlideText);
        tvStartSlideShow = findViewById(R.id.tvStartSlideshow);
        tvNumberWord = findViewById(R.id.tvNumberWord);
        pauseButton = findViewById(R.id.pauseButton);
        playButton = findViewById(R.id.playButton);
        nextButton = findViewById(R.id.nextButton);
        previousButton = findViewById(R.id.previousButton);
        muteButton = findViewById(R.id.ivMuteButton);
        unmuteButton = findViewById(R.id.ivUnMuteButton);
        repeatButton = findViewById(R.id.repeatButton);
        repeatOne = findViewById(R.id.repeatOne);

        muteButton.setVisibility(View.INVISIBLE);
        unmuteButton.setVisibility(View.INVISIBLE);

        playButton.setVisibility(View.INVISIBLE);
        repeatButton.setVisibility(View.INVISIBLE);
        repeatOne.setVisibility(View.INVISIBLE);
        pauseButton.setVisibility(View.INVISIBLE);
        nextButton.setVisibility(View.INVISIBLE);
        previousButton.setVisibility(View.INVISIBLE);

        tvNumberWord.setVisibility(View.INVISIBLE);
        btSlideText.setVisibility(View.INVISIBLE);



        repeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.Subscribed != 1){
                    //Toast.makeText(SubPFamilyActivity.this, "Repeat All Feature \n Only For Premium Users", Toast.LENGTH_SHORT).show();
                    toast.setText("Repeat All Feature \n Only For Premium Users");
                    toast.show();
                }
                else{
                    repeat = !repeat;
                    if (repeat){
                        repeatButton.setBackgroundColor(Color.GREEN);
                        repeatOne.setBackgroundColor(Color.WHITE);
                        repeat1=false;
                        toast.setText("REPEAT ALL\n ACTIVATED");
                        toast.show();
                    }
                    else {
                        repeatButton.setBackgroundColor(Color.WHITE);
                        toast.setText("REPEAT ALL\n DEACTIVATED");
                        toast.show();
                    }
                }
            }
        });

        repeatOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.Subscribed != 1){
                    toast.setText("Repeat All Feature \n Only For Premium Users");
                    toast.show();
                    //Toast.makeText(SubPFamilyActivity.this, "Repeat All Feature \n Only For Premium Users", Toast.LENGTH_SHORT).show();
                }
                else{
                    repeat1 = !repeat1;
                    if (repeat1){
                        repeat=false;
                        repeatOne.setBackgroundColor(Color.GREEN);
                        repeatButton.setBackgroundColor(Color.WHITE);
                        toast.setText("REPEAT SELECTED\n ACTIVATED");
                        toast.show();
                    }
                    else {
                        repeatOne.setBackgroundColor(Color.WHITE);
                        toast.setText("REPEAT SELECTED\n DEACTIVATED");
                        toast.show();
                    }
                }
            }
        });


        handler1 = new Handler();
        Handler handler2 = new Handler();

        ranable = new Runnable() {
            @Override
            public void run() {
                // String a = recycleArrayList.get(count).getTwiProverb();
                if (repeat1){
                    String a = businessArrayList.get(count-1).getTwiBusiness();
                    String c = businessArrayList.get(count-1).getEnglishBusiness();
                    btSlideText.setText(a);
                    tvNumberWord.setText(c);

                    String b = PlayFromFirebase.viewTextConvert(a);

                    if (unMuted){
                        playFromFileOrDownload(b);
                    }

                    if(b.length()>textLength){
                        btSlideText.setTextSize(smallFont);

                        delayTime= longDelay;
                    }else{
                        delayTime=shortDelay;
                        btSlideText.setTextSize(largeFont);
                    }

                    handler1.postDelayed(ranable, delayTime);
                }
                else{
                    if (count<= ArraySize-1){
                        String a = businessArrayList.get(count).getTwiBusiness();
                        String c = businessArrayList.get(count).getEnglishBusiness();

                        btSlideText.setText(a);
                        tvNumberWord.setText(c);

                        String b = PlayFromFirebase.viewTextConvert(a);

                        if (unMuted){
                            playFromFileOrDownload(b);
                        }

                        if(b.length()> textLength){
                            btSlideText.setTextSize(smallFont);
                            delayTime= longDelay;
                        }else{
                            delayTime=shortDelay;
                            btSlideText.setTextSize(largeFont);
                        }
                        //  Log.i("Mee1","Hi1 "+ count);
                        count++;


                        handler1.postDelayed(ranable, delayTime);
                    }
                    else if(repeat){
                        count =0;
                        //repeat=false;

                        handler1.postDelayed(ranable, 1000);
                    }
                    else{
                        tvStartSlideShow.setText(textSlideshow);
                        foodListView.setVisibility(View.VISIBLE);
                        tvStartSlideShow.setVisibility(View.VISIBLE);
                        btSlideText.setVisibility(View.INVISIBLE);
                        tvNumberWord.setVisibility(View.INVISIBLE);
                        playButton.setVisibility(View.INVISIBLE);
                        pauseButton.setVisibility(View.INVISIBLE);
                        nextButton.setVisibility(View.INVISIBLE);
                        previousButton.setVisibility(View.INVISIBLE);
                        muteButton.setVisibility(View.INVISIBLE);
                        unmuteButton.setVisibility(View.INVISIBLE);
                        repeatButton.setVisibility(View.INVISIBLE);
                        repeatOne.setVisibility(View.INVISIBLE);

                    }
                }


            }
        };

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideshow(v);
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseSlideshow(v);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count<= ArraySize){
                    next(v);
                }

            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previous(v);
            }
        });

        unmuteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unMuted = true;
                toast.setText("Sound Unmuted");
                toast.show();
                muteButton.setVisibility(View.VISIBLE);
                unmuteButton.setVisibility(View.INVISIBLE);
            }
        });

        muteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                unMuted = false;
                toast.setText("Sound Muted");
                toast.show();
                unmuteButton.setVisibility(View.VISIBLE);
                muteButton.setVisibility(View.INVISIBLE);
            }
        });

        btSlideText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // foodListView.setVisibility(View.INVISIBLE);
                //slideshow();
                String c = btSlideText.getText().toString();
                toast.setText(c);
                toast.show();

            }
        });

        tvStartSlideShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tvStartSlideShow.getText().toString().toLowerCase().contains("end")){

                    if (handler1 !=null){
                        handler1.removeCallbacks(ranable);
                    }
                    if (playFromDevice!=null){
                        playFromDevice.stop();
                    }

                    repeat1=false;
                    tvStartSlideShow.setText(textSlideshow);
                    foodListView.setVisibility(View.VISIBLE);
                    tvStartSlideShow.setVisibility(View.VISIBLE);
                    btSlideText.setVisibility(View.INVISIBLE);
                    tvNumberWord.setVisibility(View.INVISIBLE);
                    playButton.setVisibility(View.INVISIBLE);
                    pauseButton.setVisibility(View.INVISIBLE);
                    nextButton.setVisibility(View.INVISIBLE);
                    previousButton.setVisibility(View.INVISIBLE);
                    muteButton.setVisibility(View.INVISIBLE);
                    unmuteButton.setVisibility(View.INVISIBLE);
                    repeatButton.setVisibility(View.INVISIBLE);
                    repeatOne.setVisibility(View.INVISIBLE);


                }
                else{
                    foodListView.setVisibility(View.INVISIBLE);
                    //tvStartSlideShow.setVisibility(View.INVISIBLE);
                    tvStartSlideShow.setText("End Slideshow");
                    tvNumberWord.setVisibility(View.VISIBLE);
                    btSlideText.setVisibility(View.VISIBLE);
                    playButton.setVisibility(View.VISIBLE);
                    repeatButton.setVisibility(View.VISIBLE);
                    repeatButton.setBackgroundColor(Color.WHITE);
                    repeatOne.setVisibility(View.VISIBLE);
                    repeatOne.setBackgroundColor(Color.WHITE);
                    pauseButton.setVisibility(View.VISIBLE);
                    nextButton.setVisibility(View.VISIBLE);
                    previousButton.setVisibility(View.VISIBLE);
                    if (unMuted){
                        muteButton.setVisibility(View.VISIBLE);
                        unmuteButton.setVisibility(View.INVISIBLE);
                    }else{
                        muteButton.setVisibility(View.INVISIBLE);
                        unmuteButton.setVisibility(View.VISIBLE);
                        toast.setText("Sound Muted");
                        toast.show();
                    }

                    slideshow();
                }

            }
        });

        convertAndPlay = new PlayFromFirebase();



        foodListView = findViewById(R.id.familyRecyclerView);
        storageReference = FirebaseStorage.getInstance().getReference();

        recycleArrayList = new ArrayList<>();
        recycleArrayList.addAll(businessArrayList);

        businessAdapter = new RVBusinessAdapter(this, recycleArrayList, this);
        foodListView.setAdapter(businessAdapter);

        foodListView.setLayoutManager(new LinearLayoutManager(this));

    }


    @Override
    public void onMyItemClick(int position, View view) {
        String b = recycleArrayList.get(position).getTwiBusiness();

        TextView tvEnglish = view.findViewById(R.id.textViewEnglish);
        TextView tvTwi = view.findViewById(R.id.textViewTwi);

        ColorStateList oldColor = tvEnglish.getTextColors();
        // tvTwi.getTextColors();

        tvEnglish.setTextColor(Color.BLACK);
        tvTwi.setTextColor(Color.BLACK);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i("Here1","I'm here");
                tvEnglish.setTextColor(oldColor);
                tvTwi.setTextColor(oldColor);
            }
        },1500);


        b = PlayFromFirebase.viewTextConvert(b);

        String a = recycleArrayList.get(position).getEnglishBusiness()+" is: "+ recycleArrayList.get(position).getTwiBusiness();

        //Toast.makeText(this,recycleArrayList.get(position).englishFood+" is: "+ recycleArrayList.get(position).twiFood, Toast.LENGTH_SHORT).show();

        playFromFileOrDownload(b, a);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (handler1 !=null){
            handler1.removeCallbacks(ranable);
        }
        if (playFromDevice!=null){
            playFromDevice.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (handler1 !=null){
            handler1.removeCallbacks(ranable);
        }
        if (playFromDevice!=null){
            playFromDevice.stop();
        }

        Random random = new Random();
        int prob = random.nextInt(10);

        if (MainActivity.Subscribed != 1) {

            if (prob < 7) {
                Log.i("advert", "came");
                advert1();
                // advert1();
            }
        }
    }
}
