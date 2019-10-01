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

public class PronounsActivity extends AppCompatActivity {

    ListView pronounsListView;
    static ArrayList<Pronouns> pronounsArrayList;
    String b;

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



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //MenuInflater menuInflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.main_alphabet, menu);

        final MenuItem item = menu.findItem(R.id.menusearch);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(PronounsActivity.this, query, Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<Pronouns> results = new ArrayList<>();
                for (Pronouns x: pronounsArrayList ){

                    if(x.getEnglishPronoun().toLowerCase().contains(newText.toLowerCase()) || x.getTwiPronoun().toLowerCase().contains(newText.toLowerCase())|| x.getSingPlural().toLowerCase().contains(newText.toLowerCase())|| x.getSubObject().toLowerCase().contains(newText.toLowerCase())){
                        results.add(x);
                    }

                    ((PronounsAdapter)pronounsListView.getAdapter()).update(results);
                }


                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }


    public void log2(View view) {
        int idview = view.getId();

        TextView blabla = (TextView) view.findViewById(idview);
        String a = (String) blabla.getText();

        Toast.makeText(this, a, Toast.LENGTH_SHORT).show();
    }

    public void pronounClick(View view){

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

        playFromFileOrDownload(b,a);

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

    public void goToWeb() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.udemy.com/course/learn-akan-twi/?couponCode=FDISCOUNT1"));
        startActivity(intent);
    }


    public void getOther(){
        System.out.println(b);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pronouns);

        pronounsListView = findViewById(R.id.pronounsListView);
        storageReference = FirebaseStorage.getInstance().getReference();
       /* pronounsArrayList = new ArrayList<>();

        //1st Person subject

        pronounsArrayList.add(new Pronouns("I","Me","1st Person Singular","Subject"));
        pronounsArrayList.add(new Pronouns("I am a boy","Me yɛ ɔbarima","",""));
        pronounsArrayList.add(new Pronouns("I am a child","Me yɛ abofra","",""));

        pronounsArrayList.add(new Pronouns("We","Yɛ(n)","1st Person Plural","Subject"));
        pronounsArrayList.add(new Pronouns("We are strong","Yɛn ho yɛ den","",""));
        pronounsArrayList.add(new Pronouns("We will go there","Yɛbɛkɔ hɔ","",""));

        //1st Person object

        pronounsArrayList.add(new Pronouns("Me","Me","1st Person Singular","Object"));
        pronounsArrayList.add(new Pronouns("Give it to me","Fa ma me","",""));
        pronounsArrayList.add(new Pronouns("You told me","Wo ka kyerɛɛ me","",""));


        pronounsArrayList.add(new Pronouns("Us","Yɛn","1st Person Plural","Object"));
        pronounsArrayList.add(new Pronouns("You told us","Woka kyerɛɛ yɛn","",""));
        pronounsArrayList.add(new Pronouns("They invited us","Wɔtoo nsa frɛɛ yɛn","",""));


        //2nd Person subject
        pronounsArrayList.add(new Pronouns("You","Wo","2nd Person Singular","Subject"));
        pronounsArrayList.add(new Pronouns("You are a boy","Woyɛ ɔbarima","",""));
        pronounsArrayList.add(new Pronouns("You are beautiful","Wo ho yɛ fɛ","",""));

        pronounsArrayList.add(new Pronouns("You","Mo","2nd Person Plural","Subject"));
        pronounsArrayList.add(new Pronouns("You are many","Mo dɔɔso","",""));
        pronounsArrayList.add(new Pronouns("You are farmers","Mo yɛ akuafo","",""));


        //2nd Person object

        pronounsArrayList.add(new Pronouns("You","Wo","2nd Person Singular","Object"));
        pronounsArrayList.add(new Pronouns("I gave you money","Me de sika maa wo","",""));
        pronounsArrayList.add(new Pronouns("She told you","Ɔka kyerɛɛ wo","",""));

        pronounsArrayList.add(new Pronouns("You","Mo","2nd Person Plural","Object"));
        pronounsArrayList.add(new Pronouns("I saw all of you","Me huu mo nyinaa","",""));

        //3rd Person subject

        pronounsArrayList.add(new Pronouns("He","Ɔ-(no)","3rd Person Singular","Subject"));
        pronounsArrayList.add(new Pronouns("He gave it to you","Ɔde maa wo","",""));
        pronounsArrayList.add(new Pronouns("She","Ɔ-(no)","3rd Person Singular","Subject"));
        pronounsArrayList.add(new Pronouns("She told you","Ɔka kyerɛɛ wo","",""));
        pronounsArrayList.add(new Pronouns("It","Ɛ-(no)","3rd Person Singular","Subject"));
        pronounsArrayList.add(new Pronouns("It is nice","Ɛyɛ fɛ","",""));

        pronounsArrayList.add(new Pronouns("They","Wɔ(n)","3rd Person Plural","Subject"));
        pronounsArrayList.add(new Pronouns("They gave it to you","Wɔde maa wo","",""));
        pronounsArrayList.add(new Pronouns("They are strong","Wɔn ho yɛ den","",""));


        //3rd Person object

        pronounsArrayList.add(new Pronouns("Him","Ɔ-(no)","3rd Person Singular","Object"));
        pronounsArrayList.add(new Pronouns("I gave it to him","Me de maa no","",""));
        pronounsArrayList.add(new Pronouns("It is him","Ɛyɛ ɔno","",""));
        pronounsArrayList.add(new Pronouns("Her","Ɔ-(no)","3rd Person Singular","Object"));
        pronounsArrayList.add(new Pronouns("I gave it to her","Me de maa no","",""));
        pronounsArrayList.add(new Pronouns("It is her","Ɛyɛ ɔno","",""));
        pronounsArrayList.add(new Pronouns("It","Ɛ-(no)","3rd Person Singular","Object"));
        pronounsArrayList.add(new Pronouns("We killed it","Yekum no","",""));


        pronounsArrayList.add(new Pronouns("Them","Wɔn","3rd Person Plural","Object"));
        pronounsArrayList.add(new Pronouns("Give it to them","Fa ma wɔn","",""));
        pronounsArrayList.add(new Pronouns("Help them","Boa Wɔn","",""));


        //Possessive pronouns

        pronounsArrayList.add(new Pronouns("Mine","Me dea","1st Person Singular","Possessive"));
        pronounsArrayList.add(new Pronouns("Ours","Yɛn dea","1st Person Plural","Possessive"));

        pronounsArrayList.add(new Pronouns("Yours","Wo dea","2nd Person Singular","Possessive"));
        pronounsArrayList.add(new Pronouns("Yours","Mo dea","2nd Person Plural","Possessive"));


        pronounsArrayList.add(new Pronouns("His","Ne dea","3rd Person Singular","Possessive"));
        pronounsArrayList.add(new Pronouns("Hers","Ne dea","3rd Person Singular","Possessive"));
        pronounsArrayList.add(new Pronouns("Theirs","Wɔn dea","3rd Person Plural","Possessive"));

*/

        b= pronounsArrayList.get(0).toString();

        PronounsAdapter pronounsAdapter = new PronounsAdapter(this, pronounsArrayList);
        pronounsListView.setAdapter(pronounsAdapter);

    }
}
