package com.learnakantwi.twiguides;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
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
import java.util.List;
import java.util.Random;

import static com.learnakantwi.twiguides.SubChildrenAnimals.childrenAnimalsArrayList;

public class ChildrenAnimalActivity extends AppCompatActivity {

    static ArrayList<Animals> childrenAnimalsArrayListFew;
    public InterstitialAd mInterstitialAd;
    public ImageView imageView;
    AdView mAdView1;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    StorageReference musicRef;
    Toast toast;
    String fileSavedName;
    String sb1;
    Random random;
    int showAdProbability;
    Bitmap myImage;
    Animation shake;
    MediaPlayer playFromDevice;
    MediaPlayer mp1;
    int textCol=6;
    List<String> childrenAnimals;
    ListView lvChildrenAnimals;
    PlayFromFirebase playFromFirebaseChildren;
    ChildrenAnimalsAdapter animalsAdapter;

    public void advert1() {

        /*final SharedPreferences sharedPreferences = this.getSharedPreferences("com.learnakantwi.twiguides", Context.MODE_PRIVATE);
        //  sharedPreferences.edit().putString("AdvertPreference", "No").apply();
        String advertPreference = sharedPreferences.getString("AdvertPreference", "No");

        assert advertPreference != null;
        if (advertPreference.equals("Yes")) {*/

        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-7384642419407303/9880404420");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        //ca-app-pub-7384642419407303/9880404420
        //ca-app-pub-3940256099942544/1033173712 test
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
                Toast.makeText(getApplicationContext(), query, Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<Animals> results = new ArrayList<>();
                for (Animals x: childrenAnimalsArrayListFew ){

                    if(x.getEnglishAnimals().toLowerCase().contains(newText.toLowerCase()) || x.getTwiAnimals().toLowerCase().contains(newText.toLowerCase())){
                        results.add(x);
                    }

                    ((ChildrenAnimalsAdapter)lvChildrenAnimals.getAdapter()).update(results);
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
                // goToQuizAnimals();
                return  true;
            case R.id.videoCourse:
                //Log.i("Menu Item Selected", "Alphabets");
                goToWeb();
                return true;
            /*case R.id.downloadAudio:
               // downloadClick();
                return true;*/
            default:
                return false;
        }
    }

    public void goToWeb() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.udemy.com/course/learn-akan-twi/?referralCode=6D321CE6AEE1834CCB0F"));
        startActivity(intent);
    }


    public void goToQuizAnimals() {
        Intent intent = new Intent(getApplicationContext(), QuizSubAnimals.class);
        startActivity(intent);
    }

    public void goToMain(){
        Intent intent = new Intent(getApplicationContext(), HomeMainActivity.class);
        startActivity(intent);
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
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
        } else {
            toast.setText("No Internet");
            toast.show();

        }

    }




    public void getImage(final String filename) {

        myImage = BitmapFactory.decodeFile("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/" + filename + ".png");
        imageView.setImageBitmap(myImage);

        int play1 = getResources().getIdentifier(filename, "drawable","com.learnakantwi.twiguides");
        // Toast.makeText(getApplicationContext(), filename + "= No Internet", Toast.LENGTH_SHORT).show();
        imageView.setImageResource(play1);


        /*if (myImage != null){
            imageView.setImageBitmap(myImage);
           // Toast.makeText(this, "I made it", Toast.LENGTH_SHORT).show();
        }
        else {

            musicRef = musicRef.child("/ImagesAnimals/" + filename + ".png");


            musicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    String url = uri.toString();

                    //playFromFirebase(musicRef);
                    downloadFileImage(getApplicationContext(), filename, ".png", url);
                    // toast.setText(appearText);
                    //toast.show();
                    //Toast.makeText(getApplicationContext(), appearText, Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    int play1 = getResources().getIdentifier(filename, "drawable","com.learnakantwi.twiguides");
                    Toast.makeText(getApplicationContext(), filename + "= No Internet", Toast.LENGTH_SHORT).show();
                    imageView.setImageResource(play1);
                    //getImage(filename);

                }
            });
        }*/
    }
    public void downloadFileImage(final Context context, final String filename, final String fileExtension, final String url) {

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
        } else {
            toast.setText("No Internet");
            toast.show();

        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getImage(filename);
            }
        }, 2500);

        /*myImage = BitmapFactory.decodeFile("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/" + filename + fileExtension);
        imageView.setImageBitmap(myImage);*/



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

        File myFile = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/" + filename + ".m4a");
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
                        Toast.makeText(getApplicationContext(), "No Internet", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                toast.setText("Please connect to Internet to download audio");
                toast.show();
            }



        }

        fileSavedName = filename;
    }


    public void log5(View view) {
        int idview = view.getId();



        TextView blabla = view.findViewById(idview);
        blabla.setTextColor(Color.GREEN);


       /* String a = (String) blabla.getText();


        PlayFromFirebase playFromFirebase = new PlayFromFirebase();
        String b = playFromFirebase.viewTextConvert(a);

        toast.setText(b + "image");
        toast.show();

        playFromFileOrDownload(b, a);
        getImage(b);
        */


    }

    public void downloadClickAnimals () {
        int counter = 0;
        String bb;

        for (int j = 0; j < childrenAnimalsArrayListFew.size(); j++) {


            bb = childrenAnimalsArrayListFew.get(j).getTwiAnimals();
            bb = playFromFirebaseChildren.viewTextConvert(bb);

            File myFiles = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/" + bb + ".m4a");
            if (myFiles.exists()) {
                counter++;
            }

        }

        if (counter == childrenAnimalsArrayListFew.size()) {
            toast.setText("Animals Downloaded");
            toast.show();
            Log.i("Check", "animals downloaded");

        } else {
            if (isNetworkAvailable()) {
                toast.setText("Downloading...");
                toast.show();
            }


            if (isNetworkAvailable()) {

                // toast.setText(counter + " -- "+ allArrayList.size());
                // toast.show();

                for (int i = 0; i < childrenAnimalsArrayListFew.size(); i++) {

                    bb = childrenAnimalsArrayListFew.get(i).getTwiAnimals();
                    bb = playFromFirebaseChildren.viewTextConvert(bb);

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_children_animal);

        playFromFirebaseChildren = new PlayFromFirebase();


        mInterstitialAd = new InterstitialAd(this);
       // mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.setAdUnitId("ca-app-pub-7384642419407303/9880404420");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());


        random = new Random();
        showAdProbability = random.nextInt(10);

        //ca-app-pub-7384642419407303/9880404420
        //ca-app-pub-3940256099942544/1033173712 test

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                Toast.makeText(ChildrenAnimalActivity.this, "I'm here o", Toast.LENGTH_SHORT).show();
            }
        });
        mAdView1 = findViewById(R.id.adView1);
        AdRequest adRequest1 = new AdRequest.Builder().build();
        mAdView1.loadAd(adRequest1);



        imageView = findViewById(R.id.ivAnimal);
        //shake = AnimationUtils.loadAnimation(SubChildrenAnimals.this,R.anim.children_animation);


        shake = AnimationUtils.loadAnimation(this,R.anim.children_animation);
        //shake = AnimationUtils.loadAnimation(ChildrenNumberCount.this, R.anim.children_animation);

        imageView.setImageResource(R.drawable.wowa);
        fileSavedName = "wowa";


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    imageView.startAnimation(shake);
                    playFromFileOrDownload(fileSavedName);
                    if (sb1.length()>2){
                        toast.setText(sb1);
                        toast.show();
                    }



                }catch (Exception e){
                    System.out.println("Errors: "+ e);
                }


            }

        });

        /*childrenAnimalsArrayListFew = new ArrayList<>();

        childrenAnimalsArrayListFew.add(new Animals("Bee", "Wowa"));
        childrenAnimalsArrayListFew.add(new Animals("Cat", "Ɔkra"));
        childrenAnimalsArrayListFew.add(new Animals("Cattle", "Nantwie"));
        childrenAnimalsArrayListFew.add(new Animals("Cockroach", "Tɛfrɛ"));
        childrenAnimalsArrayListFew.add(new Animals("Crab", "Kɔtɔ"));
        childrenAnimalsArrayListFew.add(new Animals("Crocodile", "Ɔdɛnkyɛm"));
        childrenAnimalsArrayListFew.add(new Animals("Dog", "Kraman"));
        childrenAnimalsArrayListFew.add(new Animals("Donkey", "Afurum"));
        childrenAnimalsArrayListFew.add(new Animals("Duck", "Dabodabo"));
        childrenAnimalsArrayListFew.add(new Animals("Elephant", "Ɔsono"));
        childrenAnimalsArrayListFew.add(new Animals("Fowl", "Akokɔ"));
        childrenAnimalsArrayListFew.add(new Animals("Goat", "Apɔnkye"));
        childrenAnimalsArrayListFew.add(new Animals("Horse", "Pɔnkɔ"));
        childrenAnimalsArrayListFew.add(new Animals("Pig", "Prako"));
        childrenAnimalsArrayListFew.add(new Animals("Lion", "Gyata"));
        childrenAnimalsArrayListFew.add(new Animals("Butterfly", "Afofantɔ"));*/


        lvChildrenAnimals = findViewById(R.id.lvChildrenAnimals);



        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, childrenAnimals);


        animalsAdapter = new ChildrenAnimalsAdapter(this, childrenAnimalsArrayListFew);

        lvChildrenAnimals.setAdapter(animalsAdapter);

        lvChildrenAnimals.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {

                imageView.startAnimation(shake);


                final TextView blabla = view.findViewById(R.id.speakTwiTime);
                final TextView blabla1 = view.findViewById(R.id.speakEnglishTime);

                blabla.setBackgroundColor(Color.GREEN);
                blabla1.setBackgroundColor(Color.GREEN);

                blabla.setTextColor(Color.BLACK);
                blabla1.setTextColor(Color.BLACK);


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        blabla.setBackgroundColor(Color.WHITE);
                        blabla1.setBackgroundColor(Color.WHITE);

                        blabla.setTextColor(Color.DKGRAY);
                        blabla1.setTextColor(Color.DKGRAY);

                    }
                }, 2500);



                String a = blabla.getText().toString();



                String me1 = blabla1.getText().toString();


                PlayFromFirebase playFromFirebase = new PlayFromFirebase();
                String b = playFromFirebase.viewTextConvert(a);

                getImage(b);


                StringBuilder sb = new StringBuilder();
                sb = sb.append(me1).append(" ").append("is: ").append(a);

                toast.setText(sb);
                toast.show();

                sb1 = sb.toString();

                playFromFileOrDownload(b);

            }
        });


        toast = Toast.makeText(getApplicationContext(), " ", Toast.LENGTH_SHORT);

        musicRef = FirebaseStorage.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

    }

    @Override
    protected void onDestroy() {

        // stopPlay();

        if (showAdProbability<=5){
            advert1();

        }
        super.onDestroy();
    }

 /*   @Override
    protected void onUserLeaveHint() {

        if (showAdProbability>=7){
            advert1();
        }
        super.onUserLeaveHint();
    }*/
}
