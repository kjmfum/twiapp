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
import java.util.ArrayList;
import java.util.Collections;

public class BodypartsActivity extends AppCompatActivity {

    public static ArrayList <Bodyparts> bodypartsArrayList;
    ListView bodypartsListView;

    StorageReference storageReference;
    MediaPlayer playFromDevice;
    MediaPlayer mp1;



    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
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

            if (isNetworkAvailable()){
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

    public void downloadFile(final Context context, final String filename, final String fileExtension, final String url) {

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




    public void log2(View view) {
        int idview = view.getId();

        TextView blabla = (TextView) view.findViewById(idview);
        String a = (String) blabla.getText();

        Toast.makeText(this, a, Toast.LENGTH_SHORT).show();
    }

    public void timeClick(View view){

        int idview= view.getId();

        TextView blabla = (TextView) view.findViewById(idview);
        String a = (String) blabla.getText();

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

       /* int resourceId = getResources().getIdentifier(b, "raw", "com.learnakantwi.twiguides");


        final MediaPlayer player = MediaPlayer.create(this, resourceId);
        player.start();

        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                player.release();
            }
        });*/

       playFromFileOrDownload(b, a);

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
                ArrayList<Bodyparts> results = new ArrayList<>();
                for (Bodyparts x: bodypartsArrayList ){

                    if(x.getEnglishBodyparts().toLowerCase().contains(newText.toLowerCase()) || x.getTwiBodyparts().toLowerCase().contains(newText.toLowerCase())){
                        results.add(x);
                    }

                    ((BodypartsAdapter)bodypartsListView.getAdapter()).update(results);
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
           /* case R.id.settings:
                Log.i("Menu Item Selected", "Settings");
                playAll();
                return true;
            case R.id.alphabets:
                Log.i("Menu Item Selected", "Alphabets");
                return  true;*/

            case R.id.main:
                //Log.i("Menu Item Selected", "Alphabets");
                goToMain();
                return  true;
            case R.id.quiz1:
                //Log.i("Menu Item Selected", "Alphabets");
                goToQuizBodyParts();
                return  true;
            case R.id.videoCourse:
                //Log.i("Menu Item Selected", "Alphabets");
                goToWeb();
                return  true;
            default:
                return false;
        }
    }


    public void goToMain(){
        Intent intent = new Intent(getApplicationContext(), Home.class);
        startActivity(intent);
    }

    public void goToQuizBodyParts(){
        Intent intent = new Intent(getApplicationContext(), QuizBodyParts.class);
        startActivity(intent);
    }

    public void goToWeb() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.udemy.com/course/learn-akan-twi/?couponCode=FDISCOUNT1"));
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bodyparts);


        bodypartsListView = findViewById(R.id.bodypartsListView);
        storageReference = FirebaseStorage.getInstance().getReference();

        /*bodypartsArrayList = new ArrayList<>();

        bodypartsArrayList.add(new Bodyparts("Hand","Nsa"));
        bodypartsArrayList.add(new Bodyparts("Finger","Nsateaa"));

        bodypartsArrayList.add(new Bodyparts("Leg","Nan"));
        bodypartsArrayList.add(new Bodyparts("Nose","Hwene"));
        bodypartsArrayList.add(new Bodyparts("Head","Eti"));
        bodypartsArrayList.add(new Bodyparts("Mouth","Ano"));
        bodypartsArrayList.add(new Bodyparts("Gum","Ɛse akyi nam"));

        bodypartsArrayList.add(new Bodyparts("Cheek","Afono"));
        bodypartsArrayList.add(new Bodyparts("Teeth","Ɛse"));
        bodypartsArrayList.add(new Bodyparts("Tongue","Tɛkrɛma"));
        bodypartsArrayList.add(new Bodyparts("Eyebrow (1)","Ani akyi nhwi"));
        bodypartsArrayList.add(new Bodyparts("Eyebrow (2)","Ani ntɔn nhwi"));
        bodypartsArrayList.add(new Bodyparts("Eyelashes (1)","Anisoatɛtɛ"));
        bodypartsArrayList.add(new Bodyparts("Eyelashes (2)","Ani ntɔn"));

        bodypartsArrayList.add(new Bodyparts("Hair","Nhwi"));
        bodypartsArrayList.add(new Bodyparts("Forehead","Moma"));
        bodypartsArrayList.add(new Bodyparts("Eyeball","Ani kosua"));
        bodypartsArrayList.add(new Bodyparts("Chin","Abɔdwe"));
        bodypartsArrayList.add(new Bodyparts("Beard","Abɔdwesɛ nhwi"));
        bodypartsArrayList.add(new Bodyparts("Moustache (1)","Ano ho nhwi"));
        bodypartsArrayList.add(new Bodyparts("Moustache (1)","Mfemfem"));

        bodypartsArrayList.add(new Bodyparts("Human","Nipa"));
        bodypartsArrayList.add(new Bodyparts("Body","Nipadua"));
        bodypartsArrayList.add(new Bodyparts("Neck","Kɔn"));
        bodypartsArrayList.add(new Bodyparts("Chest (1)","Koko"));
        bodypartsArrayList.add(new Bodyparts("Chest (2)","Bo"));

        bodypartsArrayList.add(new Bodyparts("Navel","Afunuma"));
        bodypartsArrayList.add(new Bodyparts("Stomach (1)","Yafunu"));
        bodypartsArrayList.add(new Bodyparts("Stomach (2)","Yam"));

        bodypartsArrayList.add(new Bodyparts("Ribs (1)","Mparow"));
        bodypartsArrayList.add(new Bodyparts("Ribs (2)","Mfe mpade"));
        bodypartsArrayList.add(new Bodyparts("Shoulder","Abati"));
        bodypartsArrayList.add(new Bodyparts("Palm","Nsayam"));
        bodypartsArrayList.add(new Bodyparts("Knee","Kotodwe"));
        bodypartsArrayList.add(new Bodyparts("Intestine","Nsono"));
        bodypartsArrayList.add(new Bodyparts("Lung","Ahrawa"));

        bodypartsArrayList.add(new Bodyparts("Armpit","Mmɔtoam"));
        bodypartsArrayList.add(new Bodyparts("Bone","Dompe"));
        bodypartsArrayList.add(new Bodyparts("Breast","Nufuo"));
        bodypartsArrayList.add(new Bodyparts("Heart","Koma"));

        bodypartsArrayList.add(new Bodyparts("Brain (1)","Adwene"));
        bodypartsArrayList.add(new Bodyparts("Brain (2)","Amemene"));
        bodypartsArrayList.add(new Bodyparts("Fingernail","Mmɔwerɛ"));
        bodypartsArrayList.add(new Bodyparts("Thumb","Kokuromoti"));
        bodypartsArrayList.add(new Bodyparts("Arm","Abasa"));
        bodypartsArrayList.add(new Bodyparts("Elbow","Abatwɛ"));
        bodypartsArrayList.add(new Bodyparts("Vein","Ntini"));

        bodypartsArrayList.add(new Bodyparts("Buttock","Ɛto"));
        bodypartsArrayList.add(new Bodyparts("Bladder","Dwonsɔtwaa"));
        bodypartsArrayList.add(new Bodyparts("Waist","Sisi"));
        bodypartsArrayList.add(new Bodyparts("Womb (1)","Awode"));
        bodypartsArrayList.add(new Bodyparts("Womb (2)","Awotwaa"));

        bodypartsArrayList.add(new Bodyparts("Toe","Nansoaa"));
        bodypartsArrayList.add(new Bodyparts("Wrist","Abakɔn"));
        bodypartsArrayList.add(new Bodyparts("Heel","Nantin"));
        bodypartsArrayList.add(new Bodyparts("Throat (1)","Mene"));
        bodypartsArrayList.add(new Bodyparts("Throat (2)","Menemu"));
        bodypartsArrayList.add(new Bodyparts("Thigh","Srɛ"));
        bodypartsArrayList.add(new Bodyparts("Blood","Mogya"));
        bodypartsArrayList.add(new Bodyparts("Calf","Nantu"));
        bodypartsArrayList.add(new Bodyparts("Lips","Anofafa"));

        bodypartsArrayList.add(new Bodyparts("Skull","Tikwankora"));
        bodypartsArrayList.add(new Bodyparts("Skin","Honam"));

        bodypartsArrayList.add(new Bodyparts("Liver","Berɛboɔ"));
        bodypartsArrayList.add(new Bodyparts("Occiput","Atikɔ"));
        bodypartsArrayList.add(new Bodyparts("Hip (1)","Dwonku"));
        bodypartsArrayList.add(new Bodyparts("Hip (2)","Asen"));




        //Collections.sort(this.bodypartsArrayList);
        */

        BodypartsAdapter bodypartsAdapter = new BodypartsAdapter(this,bodypartsArrayList);
        bodypartsListView.setAdapter(bodypartsAdapter);

    }
}
