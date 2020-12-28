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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

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

public class SubConversationGratitude extends AppCompatActivity {

    // static ArrayList<subConversation> conversationWelcomingOthersArrayList;
    static ArrayList<subConversation> conversationGratitude;
    Handler handler1;
    Runnable ranable;
    int count= 0;
    long delayTime=3000;
    int showAdProbability;
    MediaPlayer playFromDevice;
    MediaPlayer mp1;
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
    TextView tvHeader;
    Button btStartSlideShow;
    TextView tvNumberWord;
    RecyclerView familyRecyclerView;
        ListView listView;
        ArrayList <String> names;

        StorageReference storageReference;

        String bb;

        PlayFromFirebase playFromFirebaseConversation;
        Toast toast;


        private boolean isNetworkAvailable() {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null;
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            //MenuInflater menuInflater = getMenuInflater();
            getMenuInflater().inflate(R.menu.main_menu_all, menu);

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
                    ArrayList<subConversation> results = new ArrayList<>();
                    for (subConversation x: conversationGratitude){

                        if(x.getEnglishConversation().toLowerCase().contains(newText.toLowerCase()) || x.getTwiConversation().toLowerCase().contains(newText.toLowerCase())
                        ){
                            results.add(x);
                        }

                        ((subConversationAdapter)listView.getAdapter()).update(results);
                    }


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
                    //Log.i("Menu Item Selected", "Alphabets");
                    goToMain();
                    return  true;
                case R.id.quiz1:
                    //Log.i("Menu Item Selected", "Alphabets");
                    goToQuizAll();
                    return  true;
                case R.id.downloadAllAudio:
                    downloadClick();
                    // deleteAllDownload();
                    return true;
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

        public void goToQuizAll() {
            Intent intent = new Intent(getApplicationContext(), QuizSubConversationIntroducing.class);
            startActivity(intent);
        }

        public void downloadClick () {
            int counter = 0;

            for (int j = 0; j < conversationGratitude.size(); j++) {


                bb = conversationGratitude.get(j).getTwiConversation();
                bb = PlayFromFirebase.viewTextConvert(bb);

                File myFiles = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/SUBCONVERSATION/" + bb + ".m4a");
                if (myFiles.exists()) {
                    counter++;
                }

            }

            if (counter == conversationGratitude.size()) {
                toast.setText("All downloaded");
                toast.show();

            } else {
                if (isNetworkAvailable()) {
                    toast.setText("Downloading...");
                    toast.show();
                }


                if (isNetworkAvailable()) {

                    // toast.setText(counter + " -- "+ allArrayList.size());
                    // toast.show();

                    for (int i = 0; i < conversationGratitude.size(); i++) {

                        bb = conversationGratitude.get(i).getTwiConversation();
                        bb = PlayFromFirebase.viewTextConvert(bb);

                        File myFile = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/SUBCONVERSATION/" + bb + ".m4a");
                        if (!myFile.exists()) {
                            if (isNetworkAvailable()) {
                                downloadOnly(bb);
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

        public void downloadOnly(final String filename){
            if (isNetworkAvailable()){
                final StorageReference musicRef = storageReference.child("/Conversations/" + filename + ".m4a");
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
                        request.setDestinationInExternalFilesDir(getApplicationContext(), Environment.DIRECTORY_MUSIC + "/SUBCONVERSATION" , filename + fileExtension);
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

    public void playFromFileOrDownload(final String filename){
        File myFile = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/SUBCONVERSATION/"+filename+ ".m4a");
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
                final StorageReference musicRef = storageReference.child("/Conversations/" + filename + ".m4a");
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

        String c = conversationGratitude.get(count).getTwiConversation();
        String a = conversationGratitude.get(count).getEnglishConversation();

        btSlideText.setText(c);
        tvNumberWord.setText(a);


        String b = PlayFromFirebase.viewTextConvert(c);

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
        if (count>= conversationGratitude.size()-1){
            count = conversationGratitude.size()-1;
            toast.setText("The End");
            toast.show();

        }
        String c = conversationGratitude.get(count).getTwiConversation();
        String a = conversationGratitude.get(count).getEnglishConversation();

        btSlideText.setText(c);
        tvNumberWord.setText(a);

        String b = PlayFromFirebase.viewTextConvert(c);

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
            setContentView(R.layout.activity_sub_conversation_welcoming_others);


            storageReference = FirebaseStorage.getInstance().getReference();

            playFromFirebaseConversation = new PlayFromFirebase();
            toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);

            {
                btSlideText = findViewById(R.id.btSlideText);
                tvStartSlideShow = findViewById(R.id.tvStartSlideshow);
                btStartSlideShow = findViewById(R.id.btStartSlideShow);
                familyRecyclerView = findViewById(R.id.familyRecyclerView);
                tvHeader = findViewById(R.id.tvHeader);

                tvNumberWord = findViewById(R.id.tvNumberWord);
                pauseButton = findViewById(R.id.pauseButton);
                playButton = findViewById(R.id.playButton);
                nextButton = findViewById(R.id.nextButton);
                previousButton = findViewById(R.id.previousButton);
                muteButton = findViewById(R.id.ivMuteButton);
                unmuteButton = findViewById(R.id.ivUnMuteButton);
                repeatButton = findViewById(R.id.repeatButton);
                repeatOne = findViewById(R.id.repeatOne);


                handler1 = new Handler();
                Handler handler2 = new Handler();

                ranable = new Runnable() {
                    @Override
                    public void run() {
                        // String a = recycleArrayList.get(count).getTwiProverb();
                        if (repeat1) {

                            String a = conversationGratitude.get(count).getTwiConversation();
                            String c = conversationGratitude.get(count).getEnglishConversation();


                            btSlideText.setText(a);
                            tvNumberWord.setText(c);

                            String b = PlayFromFirebase.viewTextConvert(a);

                            if (unMuted) {
                                playFromFileOrDownload(b);
                            }

                            if (b.length() > textLength) {
                                btSlideText.setTextSize(smallFont);

                                delayTime = longDelay;
                            } else {
                                delayTime = shortDelay;
                                btSlideText.setTextSize(largeFont);
                            }

                            handler1.postDelayed(ranable, delayTime);
                        } else {
                            if (count <= conversationGratitude.size() - 1) {
                                String a = conversationGratitude.get(count).getTwiConversation();
                                String c = conversationGratitude.get(count).getEnglishConversation();

                                btSlideText.setText(a);
                                tvNumberWord.setText(c);

                                String b = PlayFromFirebase.viewTextConvert(a);

                                if (unMuted) {
                                    playFromFileOrDownload(b);
                                }

                                if (b.length() > textLength) {
                                    btSlideText.setTextSize(smallFont);
                                    delayTime = longDelay;
                                } else {
                                    delayTime = shortDelay;
                                    btSlideText.setTextSize(largeFont);
                                }
                                //  Log.i("Mee1","Hi1 "+ count);
                                count++;


                                handler1.postDelayed(ranable, delayTime);
                            } else if (repeat) {
                                count = 0;
                                //repeat=false;

                                handler1.postDelayed(ranable, 1000);
                            } else {
                                listView.setVisibility(View.VISIBLE);
                                tvStartSlideShow.setVisibility(View.INVISIBLE);
                                btStartSlideShow.setVisibility(View.VISIBLE);
                                tvHeader.setVisibility(View.VISIBLE);

                                tvStartSlideShow.setText("End Slideshow");
                                tvNumberWord.setVisibility(View.INVISIBLE);
                                btSlideText.setVisibility(View.INVISIBLE);
                                playButton.setVisibility(View.INVISIBLE);
                                repeatButton.setVisibility(View.INVISIBLE);
                                repeatButton.setBackgroundColor(Color.WHITE);
                                repeatOne.setVisibility(View.INVISIBLE);
                                repeatOne.setBackgroundColor(Color.WHITE);
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

                muteButton.setVisibility(View.INVISIBLE);
                unmuteButton.setVisibility(View.INVISIBLE);

                playButton.setVisibility(View.INVISIBLE);
                tvStartSlideShow.setVisibility(View.INVISIBLE);
                familyRecyclerView.setVisibility(View.INVISIBLE);


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
                        if (count <= conversationGratitude.size()) {
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
                    public void onClick(View view) {

                        if (handler1 != null) {
                            handler1.removeCallbacks(ranable);
                        }
                        if (playFromDevice != null) {
                            playFromDevice.stop();
                        }


                        listView.setVisibility(View.VISIBLE);
                        tvStartSlideShow.setVisibility(View.INVISIBLE);
                        btStartSlideShow.setVisibility(View.VISIBLE);
                        tvHeader.setVisibility(View.VISIBLE);

                        tvStartSlideShow.setText("End Slideshow");
                        tvNumberWord.setVisibility(View.INVISIBLE);
                        btSlideText.setVisibility(View.INVISIBLE);
                        playButton.setVisibility(View.INVISIBLE);
                        repeatButton.setVisibility(View.INVISIBLE);
                        repeatButton.setBackgroundColor(Color.WHITE);
                        repeatOne.setVisibility(View.INVISIBLE);
                        repeatOne.setBackgroundColor(Color.WHITE);
                        pauseButton.setVisibility(View.INVISIBLE);
                        nextButton.setVisibility(View.INVISIBLE);
                        previousButton.setVisibility(View.INVISIBLE);

                        muteButton.setVisibility(View.INVISIBLE);
                        unmuteButton.setVisibility(View.INVISIBLE);
                        repeatButton.setVisibility(View.INVISIBLE);
                        repeatOne.setVisibility(View.INVISIBLE);
                    }
                });

                btStartSlideShow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listView.setVisibility(View.INVISIBLE);
                        btStartSlideShow.setVisibility(View.INVISIBLE);
                        tvStartSlideShow.setVisibility(View.VISIBLE);
                        tvStartSlideShow.setText("End Slideshow");
                        tvHeader.setVisibility(View.INVISIBLE);

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
                            Toast.makeText(SubConversationGratitude.this, "Sound Muted", Toast.LENGTH_SHORT).show();
                        }


                        slideshow();
                    }
                });


            }

            listView = findViewById(R.id.lvConversation);

            // ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,names);

            subConversationAdapter subConversationAdapter = new subConversationAdapter(this, conversationGratitude);

            listView.setAdapter(subConversationAdapter);


            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                }
            });

            //listView.setAdapter(arrayAdapter);

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
                //advert1();
                // advert1();
            }
        }
    }

}
