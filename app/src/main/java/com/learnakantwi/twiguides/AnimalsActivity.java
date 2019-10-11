package com.learnakantwi.twiguides;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
//import android.support.v7.app.AppCompatActivity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import java.util.Collections;
import java.util.Random;

public class AnimalsActivity extends AppCompatActivity {

    static ArrayList<Animals> animalsArrayList;
    ListView animalsListView;
    AnimalsAdapter animalsAdapter;

    StorageReference storageReference;
    MediaPlayer playFromDevice;
    MediaPlayer mp1;

    int counter2 = 1;
    boolean isRunning =false;



   /* private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }*/
    public Runnable runnable = new Runnable() {

        @Override
        public void run() {
            try {
                URL url = new URL("http://www.google.com");
                URLConnection connection = url.openConnection();
                connection.connect();
                isRunning = true;
                System.out.println("Internet is now connected");
                // Toast.makeText(context, "Connected", Toast.LENGTH_SHORT).show();
            } catch (MalformedURLException e) {
                isRunning =false;
                //System.out.println("Internet is now not connected 1");
            } catch (IOException e) {
                isRunning=false;
                //System.out.println("Internet is now not connected 2");
            }
        }
    };
    public void playFromFirebase(StorageReference musicRef) {

        if (Build.VERSION.SDK_INT > 22) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        try {
            final File localFile = File.createTempFile("aduonu", "m4a");

            if (localFile != null) {
                musicRef.getFile(localFile)
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                                if (mp1 != null){
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
                Toast.makeText(this, "File was not created", Toast.LENGTH_SHORT).show();
            }

        }catch(IOException ex){
            ex.printStackTrace();
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
                        Toast.makeText(getApplicationContext(), appearText, Toast.LENGTH_SHORT).show();
                    }
                });
                //Toast.makeText(this, "From Device", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {

            if (hasInternetAccess()){
                //Toast.makeText(this, "I'm available", Toast.LENGTH_SHORT).show();

                final StorageReference musicRef = storageReference.child("/AllTwi/" + filename + ".m4a");
                musicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String url = uri.toString();
                        playFromFirebase(musicRef);
                        // Toast.makeText(getApplicationContext(), "Got IT", Toast.LENGTH_SHORT).show();
                        downloadFile(getApplicationContext(), filename, ".m4a", url);
                        Toast.makeText(getApplicationContext(), appearText, Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "No Internet", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else {
                Toast.makeText(this, "Please connect to Internet to download audio", Toast.LENGTH_SHORT).show();
            }


        }
    }

    public void downloadFile(final Context context, final String filename, final String fileExtension, final String url) {

        if (Build.VERSION.SDK_INT > 22) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        if (hasInternetAccess()) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                    Uri uri = Uri.parse(url);
                    DownloadManager.Request request = new DownloadManager.Request(uri);
                    request.setVisibleInDownloadsUi(false);
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    //   request.setDestinationInExternalPublicDir(Environment.DIRECTORY_MUSIC+File.separator+"LearnTwi1", filename+fileExtension);
                    request.setDestinationInExternalFilesDir(getApplicationContext(), Environment.DIRECTORY_MUSIC, filename + fileExtension);
                    //request.setDestinationInExternalPublicDir(Environment.DIRECTORY_MUSIC+File.separator+"LearnTwi1", filename+fileExtension);
                    downloadManager.enqueue(request);
                }
            };
            Thread myThread = new Thread(runnable);
            myThread.start();
        }
        else
        {
            Toast.makeText(context, "I'm here", Toast.LENGTH_SHORT).show();
        }
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
        //Toast.makeText(this, a, Toast.LENGTH_SHORT).show();

        String b = a.toLowerCase();

        boolean d = b.contains("ɔ");
        boolean e = b.contains("ɛ");

        if (d || e ){
            b= b.replace("ɔ","x");
            b= b.replace("ɛ","q");
        }


        if (b.contains(" ") || b.contains("/") || b.contains(",") || b.contains("(") || b.contains(")") || b.contains("-") | b.contains("?")) {
            b = b.replace(" ", "");
            b = b.replace("/", "");
            b= b.replace(",","");
            b= b.replace("(","");
            b= b.replace(")","");
            b= b.replace("-","");
            b= b.replace("?","");
        }

        playFromFileOrDownload(b, a);
        /*
        int resourceId = getResources().getIdentifier(b, "raw", "com.learnakantwi.twiguides");


        final MediaPlayer player = MediaPlayer.create(this, resourceId);
        player.start();

        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                player.release();
            }
        });*/

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
                for (Animals x: animalsArrayList ){

                    if(x.getEnglishAnimals().toLowerCase().contains(newText.toLowerCase()) || x.getTwiAnimals().toLowerCase().contains(newText.toLowerCase())){
                        results.add(x);
                    }

                    ((AnimalsAdapter)animalsListView.getAdapter()).update(results);
                }


                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);


    }

    public void goToWeb() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.udemy.com/course/learn-akan-twi/?couponCode=FDISCOUNT1"));
        startActivity(intent);
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
                goToQuizAnimals();
                return  true;
            case R.id.videoCourse:
                //Log.i("Menu Item Selected", "Alphabets");
                goToWeb();
            case R.id.downloadAudio:
                downloadClick();
                return true;
            default:
                return false;
        }
    }

    public void downloadOnly(final String filename){
        if (isRunning){
            //Toast.makeText(this, "I'm available", Toast.LENGTH_SHORT).show();

            final StorageReference musicRef = storageReference.child("/AllTwi/" + filename + ".m4a");
            musicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    String url = uri.toString();
                    downloadFile(getApplicationContext(), filename, ".m4a", url);
                   // counter2 = 1;
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                   //counter2 = 2;
                   Toast.makeText(getApplicationContext(), "Lost Internet Connection", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            Toast.makeText(this, "Please connect to Internet to download audio ", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean hasInternetAccess() {

        Thread myThread = new Thread(runnable);
        myThread.start();
        //Toast.makeText(this, "Got here", Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, "CONNECTED", Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, "NO INTERNET", Toast.LENGTH_SHORT).show();
        return isRunning;
    }

    public void downloadClick () {
        int counter = 0;
        int counter1 =0;
        counter2 =1;

        if (hasInternetAccess()) {
            for (int j = 0; j < animalsArrayList.size(); j++) {

                String bb = animalsArrayList.get(j).getTwiAnimals();
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
            if (counter == animalsArrayList.size()) {
                Toast.makeText(this, "All downloaded ", Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(this, "Downloading", Toast.LENGTH_SHORT).show();

               for (int i = 0; i < animalsArrayList.size(); i++) {
                    String b = animalsArrayList.get(i).getTwiAnimals().toLowerCase();
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
                        if (isRunning){
                                downloadOnly(b);
                           // Toast.makeText(this, counter2 + " New audio file(s)." + " Downloaded", Toast.LENGTH_SHORT).show();
                                //counter1++;
                            }
                       else{
                            Toast.makeText(this, "Please Connect to the Inernet", Toast.LENGTH_SHORT).show(); //if (i + 1 == alphabetArray.size()) {
                            break;
                        }

                    }

                }

               // Toast.makeText(this, counter2 + " New audio file(s)." + " Download Complete", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this, "Please connect to the Internet to download audio", Toast.LENGTH_SHORT).show();
        }
    }

    public void goToQuizAnimals() {
        Intent intent = new Intent(getApplicationContext(), QuizAnimals.class);
        startActivity(intent);
    }

    public void goToMain(){
        Intent intent = new Intent(getApplicationContext(), Home.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animals);

        animalsListView = findViewById(R.id.animalsListView);
        storageReference = FirebaseStorage.getInstance().getReference();

        /*animalsArrayList.add(new Animals("Bull","Nantwinini"));
        animalsArrayList.add(new Animals("Animal","Aboa"));
        animalsArrayList.add(new Animals("Animals","Mmoa"));
        animalsArrayList.add(new Animals("Cow","Nantwibere"));
        animalsArrayList.add(new Animals("Dog","Kraman"));
        animalsArrayList.add(new Animals("Cat (1)","Ɔkra"));
        animalsArrayList.add(new Animals("Cat (2)","Agyinamoa"));
        animalsArrayList.add(new Animals("Donkey","Afurum"));
        animalsArrayList.add(new Animals("Horse","Pɔnkɔ"));
        animalsArrayList.add(new Animals("Lamb","Oguammaa"));
        animalsArrayList.add(new Animals("Pig","Prako"));
        animalsArrayList.add(new Animals("Rabbit","Adanko"));
        animalsArrayList.add(new Animals("Sheep","Odwan"));
        animalsArrayList.add(new Animals("Bat","Ampan"));
        animalsArrayList.add(new Animals("Crocodile","Ɔdɛnkyɛm"));
        animalsArrayList.add(new Animals("Deer","Ɔforote"));
        animalsArrayList.add(new Animals("Elephant","Ɔsono"));
        animalsArrayList.add(new Animals("Hippopotamus","Susono"));
        animalsArrayList.add(new Animals("Hyena","Pataku"));
        animalsArrayList.add(new Animals("Wolf (1)","Pataku"));
        animalsArrayList.add(new Animals("Wolf (2)","Sakraman"));
        animalsArrayList.add(new Animals("Leopard","Ɔsebɔ"));
        animalsArrayList.add(new Animals("Lion","Gyata"));
        animalsArrayList.add(new Animals("Rat","Kusie"));
        animalsArrayList.add(new Animals("Spider","Ananse"));
        animalsArrayList.add(new Animals("Snake","Ɔwɔ"));
        animalsArrayList.add(new Animals("Duck","Dabodabo"));
        animalsArrayList.add(new Animals("Bear","Sisire"));
        animalsArrayList.add(new Animals("Chameleon","Abosomakoterɛ"));
        animalsArrayList.add(new Animals("Lizard","Koterɛ"));
        animalsArrayList.add(new Animals("Mouse","Akura"));
        animalsArrayList.add(new Animals("Tortoise","Akyekyedeɛ"));
        animalsArrayList.add(new Animals("Centipede","Sakasaka"));
        animalsArrayList.add(new Animals("Millipede","Kankabi"));
        animalsArrayList.add(new Animals("Crab","Kɔtɔ"));
        animalsArrayList.add(new Animals("Camel","Yoma"));
        animalsArrayList.add(new Animals("Fowl","Akokɔ"));
        animalsArrayList.add(new Animals("Bird","Anomaa"));
        animalsArrayList.add(new Animals("Scorpion","Akekantwɛre"));
        animalsArrayList.add(new Animals("Cockroach","Tɛfrɛ"));
        animalsArrayList.add(new Animals("Ants","Tɛtea"));
        animalsArrayList.add(new Animals("Locust (1)","Ntutummɛ"));
        animalsArrayList.add(new Animals("Locust (2)","Mmoadabi"));
        animalsArrayList.add(new Animals("Goat (1)","Apɔnkye"));
        animalsArrayList.add(new Animals("Goat (2)","Abirekyie"));
        animalsArrayList.add(new Animals("Tiger","Ɔsebɔ"));
        animalsArrayList.add(new Animals("Butterfly","Afofantɔ"));
        animalsArrayList.add(new Animals("Grasscutter","Akranteɛ"));
        animalsArrayList.add(new Animals("Lice","Edwie"));
        animalsArrayList.add(new Animals("Porcupine","Kɔtɔkɔ"));
        animalsArrayList.add(new Animals("Hedgehog (1)","Apɛsɛ"));
        animalsArrayList.add(new Animals("Hedgehog (2)","Apɛsɛe"));
        animalsArrayList.add(new Animals("Whale","Bonsu"));
        animalsArrayList.add(new Animals("Shark","Oboodede"));
        animalsArrayList.add(new Animals("Mosquito","Ntontom"));
        animalsArrayList.add(new Animals("Grasshopper","Abɛbɛ"));
        animalsArrayList.add(new Animals("Bedbug","Nsonkuronsuo"));
        animalsArrayList.add(new Animals("Squirrel","Opuro"));
        animalsArrayList.add(new Animals("Alligator","Ɔmampam"));
        animalsArrayList.add(new Animals("Buffalo","Ɛkoɔ"));
        animalsArrayList.add(new Animals("Worm","Sonsono"));
        animalsArrayList.add(new Animals("Cattle","Nantwie"));
        animalsArrayList.add(new Animals("Fish","Apataa"));

        animalsArrayList.add(new Animals("Tsetsefly","Ohurii"));
        animalsArrayList.add(new Animals("Red Tree Ant","Nhohoa"));
        animalsArrayList.add(new Animals("Driver Ants","Nkrane"));
        animalsArrayList.add(new Animals("Praying Mantis","Akokromfi"));
        animalsArrayList.add(new Animals("House fly","Nwansena"));
        animalsArrayList.add(new Animals("Beetle","Ɔbankuo"));



        animalsArrayList.add(new Animals("Vulture (1)","Pɛtɛ"));
        animalsArrayList.add(new Animals("Vulture (2)","Kɔkɔsakyi"));
        animalsArrayList.add(new Animals("Hawk","Akorɔma"));
        animalsArrayList.add(new Animals("Guinea Fowl","Akɔmfɛm"));
        animalsArrayList.add(new Animals("Monkey","Adoe"));
        animalsArrayList.add(new Animals("Parrot","Akoo"));
        animalsArrayList.add(new Animals("Crow","Kwaakwaadabi"));
        animalsArrayList.add(new Animals("Owl","Patuo"));
        animalsArrayList.add(new Animals("Eagle","Ɔkɔre"));
        animalsArrayList.add(new Animals("Sparrow","Akasanoma"));
        animalsArrayList.add(new Animals("Swallow","Asomfena"));
        animalsArrayList.add(new Animals("Dove","Aborɔnoma"));


        animalsArrayList.add(new Animals("Bee","Wowa"));
        animalsArrayList.add(new Animals("Herring","Ɛmane"));
        animalsArrayList.add(new Animals("Lobster","Ɔbɔnkɔ"));
        animalsArrayList.add(new Animals("Lobsters","Mmɔnkɔ"));




        Collections.sort(this.animalsArrayList);

        animalsArrayList.add(new Animals("Which animal?","Aboa bɛn?"));
        animalsArrayList.add(new Animals("Which animal is this?","Aboa bɛn ni?"));
        animalsArrayList.add(new Animals("It is a lion","Ɛyɛ gyata"));

*/
        animalsAdapter= new AnimalsAdapter(this, animalsArrayList);
        animalsListView.setAdapter(animalsAdapter);



    }
}
