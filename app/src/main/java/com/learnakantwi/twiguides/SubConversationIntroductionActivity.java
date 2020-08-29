package com.learnakantwi.twiguides;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;

public class SubConversationIntroductionActivity extends AppCompatActivity {

   static ArrayList <subConversation> conversationArrayList;
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
                for (subConversation x: conversationArrayList ){

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

        for (int j = 0; j < conversationArrayList.size(); j++) {


            bb = conversationArrayList.get(j).getTwiConversation();
            bb = PlayFromFirebase.viewTextConvert(bb);

            File myFiles = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/SUBCONVERSATION/" + bb + ".m4a");
            if (myFiles.exists()) {
                counter++;
        }

        }

        if (counter == conversationArrayList.size()) {
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

                for (int i = 0; i < conversationArrayList.size(); i++) {

                    bb = conversationArrayList.get(i).getTwiConversation();
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
        setContentView(R.layout.activity_sub_conversation_introduction);


        storageReference = FirebaseStorage.getInstance().getReference();

        playFromFirebaseConversation = new PlayFromFirebase();
        toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);

       // deleteDuplicatelDownload();

       // conversationArrayList = new ArrayList<>();

        //myself
       /* conversationArrayList.add(new subConversation("Wo din de sɛn?", "What is your name?") );
        conversationArrayList.add(new subConversation("My name is Michael Kwaku Asante and all is care about is that I don't ever want to see you in my life. Take note it is not because I hate you but I love you and want us to be friends forever but I just cant stand what you are doing to me right now", "My name is Michael Kwaku Asante and all is care about is that I don't ever want to see you in my life. Take note it is not because I hate you but I love you and want us to be friends forever but I just cant stand what you are doing to me right now.") );
        conversationArrayList.add(new subConversation("Me din de Michael Kwaku Asante", "My name is Michael Kwaku Asante") );
        conversationArrayList.add(new subConversation("Wubetumi afrɛ me Kwaku", "You can call me Kwaku") );
        conversationArrayList.add(new subConversation("Wo nso ɛ?", "What about you?") );
        conversationArrayList.add(new subConversation("Wo din de sɛn?", "What is your name?") );
        conversationArrayList.add(new subConversation("Yɛfrɛ wo sɛn", "How are you called? (Lit.: How do they call you") );
        conversationArrayList.add(new subConversation("Me fi Abuakwa", "I come from Abuakwa. (\"Come from\" as used here means my hometown)") );
        conversationArrayList.add(new subConversation("Wufi kurow bɛn so?", "What is your hometown? (Lit.: Which town do you come from)") );
        conversationArrayList.add(new subConversation("Me te Achimota", "I live at Achimota") );
        conversationArrayList.add(new subConversation("Wote he?", "Where do you live?") );
        conversationArrayList.add(new subConversation("Me kɔɔ ntoaso sukuu wɔ Achimota", "I attended Secondary School at Achimota") );
        conversationArrayList.add(new subConversation("Wokɔɔ ntoaso sukuu wɔ he?", "Where did you attend Secondary School?") );

        conversationArrayList.add(new subConversation("Me kɔ sukuu wɔ Legon Sukuupɔn mu", "I school at Legon University") );
        conversationArrayList.add(new subConversation("Wo kɔ sukuu wɔ he?", "Where do you attend school?") );
        conversationArrayList.add(new subConversation("Megyina gyinapɛn num", "I am in class five") );

        conversationArrayList.add(new subConversation("Madi mfe aduonu", "I am twenty years old") );

        conversationArrayList.add(new subConversation("Meyɛ tikyani", "I am a teacher") );
        conversationArrayList.add(new subConversation("Woyɛ adwuma bɛn?", "What's your occupation (Lit.: What work do you do?)") );
        conversationArrayList.add(new subConversation("Meyɛ okuani", "I am a farmer") );
        conversationArrayList.add(new subConversation("Meyɛ adwuma wɔ Ghana Education Service", "I work at Ghana Education Service") );


        conversationArrayList.add(new subConversation("Agorɔ a mepɛ paa ne \"basketball\" ", "My favorite game is basketball") );
        conversationArrayList.add(new subConversation("Agorɔ bɛn na w'ani gye ho pa ara?", "What game do you like most?") );
        conversationArrayList.add(new subConversation("M'ani gye nnwom ho pa are", "I like music very much") );
        conversationArrayList.add(new subConversation("M'ani gye akenkan ho", "I like reading") );
        conversationArrayList.add(new subConversation("W'ani gye akenkan ho?", "Do you like reading?") );
        conversationArrayList.add(new subConversation("M'ani nnye akenkan ho", "I do not like reading") );
        conversationArrayList.add(new subConversation("M'ani gye sini ho?", "I like movies") );
        conversationArrayList.add(new subConversation("M'ani gye sinihwɛ ho pa ara", "I like watching movies very much") );
        conversationArrayList.add(new subConversation("Mempɛ sinihwɛ", "I don't like watching movies") );
        conversationArrayList.add(new subConversation("Ade a mempɛ koraa ne ntɔkwa", "What I don't like at all is fighting") );





        conversationArrayList.add(new subConversation("Aduane a m'ani gye ho pa ara ne fufuo ne nkate nkwan", "My favorite food is fufuo and groundnut soup") );
        conversationArrayList.add(new subConversation("Aduane a mepɛ pa ara ne banku", "The food I like most is banku") );
        conversationArrayList.add(new subConversation("Wopɛ fufuo?", "Do you like fufuo?") );
        conversationArrayList.add(new subConversation("Fufuo nyɛ me dɛ", "Fufu does't taste sweet to me") );
        conversationArrayList.add(new subConversation("Mempɛ fufuo", "I don't like fufuo") );
        conversationArrayList.add(new subConversation("Mepɛ fufuo", "I like fufuo") );
        conversationArrayList.add(new subConversation("Nkate nkwan yɛ dɛ", "Groundnut soup is sweet") );
*/

        //family
       /* conversationArrayList.add(new subConversation("Wo aware anaa?", "Are you married?") );
        conversationArrayList.add(new subConversation("Menwaree", "I am not married") );
        conversationArrayList.add(new subConversation("Maware", "I am married") );
        conversationArrayList.add(new subConversation("Me yɛ sigyani", "I am single") );
        conversationArrayList.add(new subConversation("Meyɛ ɔbaa warefo", "I am a married woman") );
        conversationArrayList.add(new subConversation("Me yɛ ɔbarima warefo", "I am a married man") );
        conversationArrayList.add(new subConversation("Me kunu din de Kwame", "My husband's name is Kwame") );
        conversationArrayList.add(new subConversation("Yɛfrɛ me kunu Kwame", "My husband is called Kwame") );
        conversationArrayList.add(new subConversation("Me yere din de Abena", "My wife's name is Abena") );
        conversationArrayList.add(new subConversation("Yɛfrɛ me yere Abena", "My wife is called Abena") );
        conversationArrayList.add(new subConversation("Medɔ me yere ", "I love my wife") );
        conversationArrayList.add(new subConversation("Me yere ho yɛ fɛ", "My wife is beautiful") );
        conversationArrayList.add(new subConversation("Medɔ me kunu", "I love my husband") );*/


      /*  names = new ArrayList<>();
        names.add("Justice");
        names.add("Abena");*/

        listView = findViewById(R.id.lvConversation);

       // ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,names);

        subConversationAdapter subConversationAdapter = new subConversationAdapter(this, conversationArrayList);

        listView.setAdapter(subConversationAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String Test = conversationArrayList.get(position).getEnglishConversation();
               // Toast.makeText(SubConversationIntroductionActivity.this, "Yes o "+ Test, Toast.LENGTH_SHORT).show();
            }
        });

        //listView.setAdapter(arrayAdapter);

    }
}
