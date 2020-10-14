package com.learnakantwi.twiguides;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;

public class SubConversationLove extends AppCompatActivity {

       // static ArrayList<subConversation> conversationWelcomingOthersArrayList;
    static ArrayList<subConversation> conversationLove;
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
                    for (subConversation x: conversationLove ){

                        if(x.getEnglishConversation().toLowerCase().contains(newText.toLowerCase()) || x.getTwiConversation().toLowerCase().contains(newText.toLowerCase())
                            /*|| x.getTwi1().toLowerCase().contains(newText.toLowerCase())
                            || x.getTwi2().toLowerCase().contains(newText.toLowerCase())
                            || x.getEnglish1().toLowerCase().contains(newText.toLowerCase())
                            || x.getEnglish2().toLowerCase().contains(newText.toLowerCase())*/

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

            for (int j = 0; j < conversationLove.size(); j++) {


                bb = conversationLove.get(j).getTwiConversation();
                bb = PlayFromFirebase.viewTextConvert(bb);

                File myFiles = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/SUBCONVERSATION/" + bb + ".m4a");
                if (myFiles.exists()) {
                    counter++;
                }

            }

            if (counter == conversationLove.size()) {
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

                    for (int i = 0; i < conversationLove.size(); i++) {

                        bb = conversationLove.get(i).getTwiConversation();
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

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_sub_conversation_welcoming_others);


            storageReference = FirebaseStorage.getInstance().getReference();

            playFromFirebaseConversation = new PlayFromFirebase();
            toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);


            listView = findViewById(R.id.lvConversation);

            // ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,names);

            subConversationAdapter subConversationAdapter = new subConversationAdapter(this, conversationLove);

            listView.setAdapter(subConversationAdapter);


            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    //String Test = conversationDirections.get(position).getEnglishConversation();
                    // Toast.makeText(SubConversationIntroductionActivity.this, "Yes o "+ Test, Toast.LENGTH_SHORT).show();
                }
            });

            //listView.setAdapter(arrayAdapter);

        }
    }
