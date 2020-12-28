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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


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
import java.util.ArrayList;
import java.util.Random;

import static com.learnakantwi.twiguides.MainActivity.largeFont;
import static com.learnakantwi.twiguides.MainActivity.longDelay;
import static com.learnakantwi.twiguides.MainActivity.shortDelay;
import static com.learnakantwi.twiguides.MainActivity.smallFont;
import static com.learnakantwi.twiguides.MainActivity.textLength;

public class SubPReadingActivityTwoLetters extends AppCompatActivity {

    ArrayList  <String> twoLettersArrayList = new ArrayList<>();
    String vowelLetter = "e";
    public InterstitialAd mInterstitialAd;
    ListView lvTwoLetters;
    TextView tvHeader;
    String b;
    PlayFromFirebase playFromFirebase;
    StorageReference storageReference;

    MediaPlayer playFromDevice;
    MediaPlayer mp1;

    Toast toast;
    String type = "vowel";
    AdView mAdView;
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


    long delayTime=5000;
    int showAdProbability;
    int count= 0;



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
        if (MainActivity.Subscribed !=1){
            Intent intent = new Intent(getApplicationContext(), HomeMainActivity.class);
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(getApplicationContext(), SubPHomeMainActivity.class);
            startActivity(intent);
        }

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
                    request.setDestinationInExternalFilesDir(getApplicationContext(), Environment.DIRECTORY_MUSIC, "/READING/"+filename + fileExtension);
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


    public void advert1() {

        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    public void slideshow() {

        slideshowBool = true;

        count = 0;
        handler1.postDelayed(ranable, 2);
    }

    public void slideshow(View v) {

        playButton.setVisibility(View.INVISIBLE);
        pauseButton.setVisibility(View.VISIBLE);

        handler1.postDelayed(ranable, 2);

        slideshowBool = true;
    }

    public void pauseSlideshow(View view) {

        if (!repeat1){
            count--;
        }

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

        String a = twoLettersArrayList.get(count);
        String c =  twoLettersArrayList.get(count);

        StringBuilder sb = new StringBuilder();
        sb= sb.append(a.substring(0,1)).append("   ").append(a.charAt(1));

        btSlideText.setText(sb);
        tvNumberWord.setText(c);


        String b = PlayFromFirebase.viewTextConvert("read"+a);

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
        if (!slideshowBool){
            count++;
        }
        else{
            slideshowBool = false;
        }
        if (count>=twoLettersArrayList.size()-1){
            count = twoLettersArrayList.size()-1;
            toast.setText("The End");
            toast.show();

        }
        String a = twoLettersArrayList.get(count);
        String c =  twoLettersArrayList.get(count);

        StringBuilder sb = new StringBuilder();
        sb= sb.append(a.substring(0,1)).append("   ").append(a.charAt(1));

        btSlideText.setText(sb);
        tvNumberWord.setText(c);

        String b = PlayFromFirebase.viewTextConvert("read"+a);

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_preading_two_letters);



        Intent intent = getIntent();
        Intent typeIntent = getIntent();
        vowelLetter = intent.getStringExtra("vowel");
        type = typeIntent.getStringExtra("type");

        tvHeader = findViewById(R.id.tvTwoLetterHeader);
        tvHeader.setText(vowelLetter);
        tvHeader.setTextColor(Color.BLUE);

        if (type.contains("vowel")){
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
        }
        else{
            twoLettersArrayList.add(vowelLetter+"A");
            twoLettersArrayList.add(vowelLetter+"E");
            twoLettersArrayList.add(vowelLetter+"I");
            twoLettersArrayList.add(vowelLetter+"O");
            twoLettersArrayList.add(vowelLetter+"U");
            twoLettersArrayList.add(vowelLetter+"Ɔ");
            twoLettersArrayList.add(vowelLetter+"Ɛ");
        }


        if (MainActivity.Subscribed != 1){

            random = new Random();
            showAdProbability = random.nextInt(10);

            mInterstitialAd = new InterstitialAd(this);
            mInterstitialAd.setAdUnitId(MainActivity.AdUnitInterstitial);
            mInterstitialAd.loadAd(new AdRequest.Builder().build());

            MobileAds.initialize(this, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {
                }
            });
            mAdView = findViewById(R.id.adView);
            mAdView.setVisibility(View.VISIBLE);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        }

//////SlideshowStuff

        {
            isNetworkAvailable();

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
                    if (MainActivity.Subscribed != 1) {
                        //Toast.makeText(SubPFamilyActivity.this, "Repeat All Feature \n Only For Premium Users", Toast.LENGTH_SHORT).show();
                        toast.setText("Repeat All Feature \n Only For Premium Users");
                        toast.show();
                    } else {
                        repeat = !repeat;
                        if (repeat) {
                            repeatButton.setBackgroundColor(Color.GREEN);
                            repeatOne.setBackgroundColor(Color.WHITE);
                            repeat1 = false;
                            toast.setText("REPEAT ALL\n ACTIVATED");
                            toast.show();
                        } else {
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
                    if (MainActivity.Subscribed != 1) {
                        toast.setText("Repeat One Feature \n Only For Premium Users");
                        toast.show();
                        //Toast.makeText(SubPFamilyActivity.this, "Repeat All Feature \n Only For Premium Users", Toast.LENGTH_SHORT).show();
                    } else {
                        repeat1 = !repeat1;
                        if (repeat1) {
                            repeat = false;
                            repeatOne.setBackgroundColor(Color.GREEN);
                            repeatButton.setBackgroundColor(Color.WHITE);
                            toast.setText("REPEAT SELECTED\n ACTIVATED");
                            toast.show();
                        } else {
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
                    if (repeat1) {

                        String a = twoLettersArrayList.get(count);
                        String c = twoLettersArrayList.get(count);

                        StringBuilder sb = new StringBuilder();
                        sb= sb.append(a.substring(0,1)).append("   ").append(a.charAt(1));

                        btSlideText.setText(sb);
                        tvNumberWord.setText(c);

                        String b = PlayFromFirebase.viewTextConvert("read" + a);

                        if (unMuted) {
                            playFromFileOrDownload(b);
                            delayTime = longDelay;
                        } else {
                            delayTime = shortDelay;
                        }


                    /*if(b.length()>textLength){
                        btSlideText.setTextSize(smallFont);

                        delayTime= longDelay;
                    }else{
                        delayTime=shortDelay;
                        btSlideText.setTextSize(largeFont);
                    }*/
                        handler1.postDelayed(ranable, delayTime);
                    } else {
                        if (count <= twoLettersArrayList.size() - 1) {
                            String a = twoLettersArrayList.get(count);
                            String c = twoLettersArrayList.get(count);

                            StringBuilder sb = new StringBuilder();
                            sb= sb.append(a.substring(0,1)).append("   ").append(a.charAt(1));

                            btSlideText.setText(sb);
                            tvNumberWord.setText(c);

                            String b = PlayFromFirebase.viewTextConvert("read" + a);

                            if (unMuted) {
                                playFromFileOrDownload(b);
                                delayTime = longDelay;
                            } else {
                                delayTime = shortDelay;
                            }
                            //  Log.i("Mee1","Hi1 "+ count);
                            count++;


                            handler1.postDelayed(ranable, delayTime);
                        } else if (repeat) {
                            count = 0;
                            //repeat=false;

                            handler1.postDelayed(ranable, 1000);
                        } else {
                            tvStartSlideShow.setText("Start "+ "\""+ vowelLetter+ "\""+ " Slideshow");
                            lvTwoLetters.setVisibility(View.VISIBLE);
                            tvStartSlideShow.setVisibility(View.VISIBLE);
                            btSlideText.setVisibility(View.INVISIBLE);
                            tvNumberWord.setVisibility(View.INVISIBLE);
                            playButton.setVisibility(View.INVISIBLE);
                            pauseButton.setVisibility(View.INVISIBLE);
                            nextButton.setVisibility(View.INVISIBLE);
                            previousButton.setVisibility(View.INVISIBLE);
                            ////

                            muteButton.setVisibility(View.INVISIBLE);
                            unmuteButton.setVisibility(View.INVISIBLE);
                            repeatButton.setVisibility(View.INVISIBLE);
                            repeatOne.setVisibility(View.INVISIBLE);

                            tvHeader.setVisibility(View.VISIBLE);

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
                    if (count <= twoLettersArrayList.size()) {
                        System.out.println("Mee " + twoLettersArrayList.size() + ": " + count);
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
                    //Toast.makeText(SubPFamilyActivity.this, "Sound Muted", Toast.LENGTH_SHORT).show();
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

                    if (tvStartSlideShow.getText().toString().toLowerCase().contains("end")) {

                        if (handler1 != null) {
                            handler1.removeCallbacks(ranable);
                        }
                        if (playFromDevice != null) {
                            playFromDevice.stop();
                        }

                        tvStartSlideShow.setText("Start " + "\"" + vowelLetter + "\"" + " Slideshow");
                        lvTwoLetters.setVisibility(View.VISIBLE);
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

                        ///
                        tvHeader.setVisibility(View.VISIBLE);


                    } else {
                        tvHeader.setVisibility(View.INVISIBLE);
                        lvTwoLetters.setVisibility(View.INVISIBLE);
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
                        if (unMuted) {
                            muteButton.setVisibility(View.VISIBLE);
                            unmuteButton.setVisibility(View.INVISIBLE);
                        } else {
                            muteButton.setVisibility(View.INVISIBLE);
                            unmuteButton.setVisibility(View.VISIBLE);
                            Toast.makeText(SubPReadingActivityTwoLetters.this, "Sound Muted", Toast.LENGTH_SHORT).show();
                        }

                        slideshow();
                    }

                }
            });

            //convertAndPlay = new PlayFromFirebase();


        }
        ////////

        toast = Toast.makeText(getApplicationContext(), "Tap to Listen" , Toast.LENGTH_LONG);

        playFromFirebase = new PlayFromFirebase();
        storageReference = FirebaseStorage.getInstance().getReference();

        playFromDevice = new MediaPlayer();
        mp1 = new MediaPlayer();



        //getIntent(vowel);



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
