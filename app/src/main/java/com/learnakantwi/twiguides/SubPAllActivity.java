package com.learnakantwi.twiguides;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import static com.learnakantwi.twiguides.AllActivity.allArrayList;

public class SubPAllActivity extends AppCompatActivity {

    ListView allListView;

    StorageReference storageReference;
    MediaPlayer playFromDevice;
    MediaPlayer mp1;

    Context context;

    int audioToDownload=0;
    int downloadAllClickCount=0;


    Toast toast;

    String b;
    PlayFromFirebase playFromFirebase;


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




    public void deleteAllDownload(){

        File myFiles = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/");

        String[] files = myFiles.list();
        String name1= myFiles.getName();

        File [] files1 = myFiles.listFiles();

        //System.out.println(Arrays.toString(files));
        System.out.println("Hey "+ name1);

        ArrayList<String[]> filesArray = new ArrayList<>();
        filesArray.add(files);


        for (int j = 0; j < files1.length; j++) {

            toast.setText(String.valueOf(files1.length));
            toast.show();

            File file = files1[j];
            if (file.getName().contains("-")){
                file.delete();
                //toast.setText("Deleted");
                //toast.show();

            /*String bb = allArrayList.get(j).getTwiMain();
            bb= bb.toLowerCase();
            boolean dd = bb.contains("ɔ");
            boolean ee = bb.contains("ɛ");
            if (dd || ee) {
                bb = bb.replace("ɔ", "x");
                bb = bb.replace("ɛ", "q");
            }

            if (bb.contains(" ") || bb.contains("/") || bb.contains(",") || bb.contains("(") || bb.contains(")") || bb.contains("-") || bb.contains("?") || bb.contains("'") | bb.contains("...")) {
                bb = bb.replace(" ", "");
                bb = bb.replace("/", "");
                bb = bb.replace(",", "");
                bb = bb.replace("(", "");
                bb = bb.replace(")", "");
                bb = bb.replace("-", "");
                bb = bb.replace("?", "");
                bb = bb.replace("'", "");
                bb= bb.replace("...","");*/
            }
           /* File myFiles = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/" + bb + ".m4a");
           * if (myFiles.exists()) {
                myFiles.delete();
            }*/



            /*for (File f: myFiles.listFiles()){
                long space= f.getTotalSpace();

                //f.delete();
            }
*/
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

    public void playFromFileOrDownload(final String filename, String type){

        File myFile = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/"+filename+ ".m4a");
        if (type.equals("verb")){
            myFile = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/VERBS/" + filename + ".m4a");
        }
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
                        /*toast.setText(appearText);
                        toast.show();*/
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {

            if (isNetworkAvailable()) {

                final StorageReference musicRef = storageReference.child("/AllTwi/" + filename + ".m4a");

                final StorageReference musicRef2 = storageReference.child("/Verbs/" + filename + ".m4a");

                if (type.equals("verb")) {
                    playFromFirebase(musicRef2);
                    musicRef2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();
                          downloadFile(getApplicationContext(), filename, ".m4a", url, type);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "No Internet", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    musicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();

                            playFromFirebase(musicRef);

                            downloadFile(getApplicationContext(), filename, ".m4a", url, type);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "No Internet", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            else {
                toast.setText("Please connect to Internet to download audio");
                toast.show();
            }


        }
    }

    public void downloadFile(final Context context, final String filename, final String fileExtension, final String url, String type) {

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
                    if(type.equals("verb")){
                        request.setDestinationInExternalFilesDir(getApplicationContext(),Environment.DIRECTORY_MUSIC + "/VERBS", filename + fileExtension);
                    }
                    else {
                        request.setDestinationInExternalFilesDir(getApplicationContext(), Environment.DIRECTORY_MUSIC, filename + fileExtension);
                    }
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

    public void downloadOnly(final String filename, String type){
        if (isNetworkAvailable()) {

            audioToDownload++;

            final StorageReference musicRef = storageReference.child("/AllTwi/" + filename + ".m4a");

            final StorageReference musicRef2 = storageReference.child("/Verbs/" + filename + ".m4a");

            if (type.equals("verb")) {
                musicRef2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String url = uri.toString();
                        downloadFile(getApplicationContext(), filename, ".m4a", url, type);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "No Internet", Toast.LENGTH_SHORT).show();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (audioToDownload == downloadAllClickCount){
                            downloadAllClickCount = 0;
                        }
                    }
                });
            } else {
                musicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String url = uri.toString();
                        downloadFile(getApplicationContext(), filename, ".m4a", url, type);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "No Internet", Toast.LENGTH_SHORT).show();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (audioToDownload == downloadAllClickCount){
                            downloadAllClickCount = 0;
                        }
                    }
                });
            }
        }
        else {
            toast.setText("Please connect to Internet to download audio");
            toast.show();
        }
    }

    public void downloadClick () {
        int counter = 0;

        downloadAllClickCount = allArrayList.size()- counter;

        if (isNetworkAvailable()) {
            for (int j = 0; j < allArrayList.size(); j++) {

                String bb = allArrayList.get(j).getTwiMain();
                String type1 = allArrayList.get(j).getEnglish1().toLowerCase();

                bb = PlayFromFirebase.viewTextConvert(bb);


                File myFiles = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/" + bb + ".m4a");

                if (type1.equals("verb")){
                    myFiles = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/VERBS/" + bb + ".m4a");
                }
                if (myFiles.exists()) {
                    counter++;
                    downloadAllClickCount = allArrayList.size()- counter;
                }
            }

            if (counter == allArrayList.size()) {
                toast.setText("All downloaded");
                toast.show();
                // Toast.makeText(this, "Download Complete", Toast.LENGTH_SHORT).show();

            } else {
                toast.setText("Downloading...");
                toast.show();

                // toast.setText(counter + " -- "+ allArrayList.size());
                // toast.show();
                audioToDownload = 0;
                for (int i = 0; i < allArrayList.size(); i++) {
                    String b = allArrayList.get(i).getTwiMain().toLowerCase();
                    String type2 = allArrayList.get(i).getEnglish1().toLowerCase();

                    b = PlayFromFirebase.viewTextConvert(b);

                    File myFile = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/" + b + ".m4a");
                    if (type2.equals("verb")){
                        myFile = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/VERBS/" + b + ".m4a");
                    }
                    if (!myFile.exists()) {
                        if (isNetworkAvailable()){
                            downloadOnly(b, type2);
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

    public boolean downloadState() {
        int testCounter = 0;
        if (isNetworkAvailable()) {
            for (int j = 0; j < allArrayList.size(); j++) {

                String bb = allArrayList.get(j).getTwiMain();
                String type = allArrayList.get(j).getEnglish1();

                bb = PlayFromFirebase.viewTextConvert(bb);

                File myFiles = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/" + bb + ".m4a");
                if (type.equals("verb")){
                    myFiles = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/VERBS/" + bb + ".m4a");
                }
                if (myFiles.exists()) {
                    testCounter++;
                }
            }
        }

        return testCounter != allArrayList.size();
    }

    public void downloadAll(){

        if (downloadAllClickCount==0) {
            if (downloadState()) {
                new AlertDialog.Builder(this)
                        .setIcon(R.drawable.learnakantwiimage)
                        .setTitle("Download Audio?")
                        .setMessage("Downloading Audio will download any new vocabulary audio which has been added and allow you to use the audio offline. This might take a few minutes and will use your data. Would you like to Download Audio to enhance your experience.")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // downloadAllClickCount++;
                                toast.setText("Downloading in background. Please ");
                                toast.show();
                                downloadClick();
                            }
                        }).setNegativeButton("No", null)
                        .show();
            } else {
                toast.setText("You have All Audio Downloaded. No New Audio");
                toast.show();
            }
        }
        else{
            //toast.setText("Please wait a moment \n" + downloadAllClickCount +"\n"+ audioToDownload);
           toast.setText("Downloading... \n Please wait a moment");
            toast.show();

        }

    }



    public void log2(View view) {

        int idview = view.getId();

        TextView blabla = view.findViewById(idview);
        String a = (String) blabla.getText();

        toast.setText(a);
        toast.show();
    }

    public void allClick(View view){

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


        playFromFileOrDownload(b, "all");

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
                final ArrayList<All> results = new ArrayList<>();
                for (All x: allArrayList ){

                    if(x.getEnglishmain().toLowerCase().contains(newText.toLowerCase()) || x.getTwiMain().toLowerCase().contains(newText.toLowerCase())
                            || x.getTwi1().toLowerCase().contains(newText.toLowerCase())
                            || x.getTwi2().toLowerCase().contains(newText.toLowerCase())
                            || x.getEnglish1().toLowerCase().contains(newText.toLowerCase())
                            || x.getEnglish2().toLowerCase().contains(newText.toLowerCase())

                    ){
                        results.add(x);
                    }

                    ((AllAdapter)allListView.getAdapter()).update(results);
                }

                allListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {

                        view.setBackgroundColor(Color.GREEN);
                        String a;

                        a = results.get(position).getTwiMain();

                        String type = results.get(position).getEnglish1().toLowerCase();

               /* if (!type.equals("verb")){
                    type = "all";
                }*/


                        String c;
                        c= results.get(position).getEnglishmain();
                        StringBuilder sb = new StringBuilder();

                       // sb = sb.append(c).append(" is:\n").append(a);
                        sb = sb.append("English: ").append(c).append("\n").append("Twi: ").append(a);
                        // sb= sb.append(b.substring(0,1)).append("   ").append(b.charAt(1)).append("\n\t").append(b);
                        // c= b.substring(0,1) + "  "+ Character.toString(b.charAt(1)) +"   :"+ b.substring(0,2);

                        b=  PlayFromFirebase.viewTextConvert(a);

                        // Toast.makeText(ReadingActivityTwoLetters.this, sb, Toast.LENGTH_SHORT).show();

                        toast.setText(sb);
                        toast.show();
                        playFromFileOrDownload(b, type);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                view.setBackgroundColor(Color.WHITE);
                            }
                        }, 3000);


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
                //Log.i("Menu Item Selected", "Alphabets");
                goToMain();
                return  true;
            case R.id.quiz1:
                //Log.i("Menu Item Selected", "Alphabets");
                goToQuizAll();
                return  true;
            case R.id.downloadAllAudio:
                 downloadAll();
                //deleteAllDownload();
                return true;
            case R.id.videoCourse:
                //Log.i("Menu Item Selected", "Alphabets");
                goToWeb();
                return  true;
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

    public void goToQuizAll() {
        Intent intent = new Intent(getApplicationContext(), QuizSubAll.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_pall);

        downloadAllClickCount =0;

        context = this;
        playFromFirebase = new PlayFromFirebase();

        isNetworkAvailable();

        toast = Toast.makeText(getApplicationContext(), "Tap to Listen" , Toast.LENGTH_LONG);
        toast.show();



        storageReference = FirebaseStorage.getInstance().getReference();
        allListView = findViewById(R.id.allListView);
///////////////
        AllAdapter allAdapter= new AllAdapter(this, allArrayList);
        allListView.setAdapter(allAdapter);

        allListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {

                view.setBackgroundColor(Color.GREEN);
                String a;
                String type;

                a = allArrayList.get(position).getTwiMain();
                type = allArrayList.get(position).getEnglish1().toLowerCase();

               /* if (!type.equals("verb")){
                    type = "all";
                }*/


                String c;
                c= allArrayList.get(position).getEnglishmain();
                StringBuilder sb = new StringBuilder();

                sb = sb.append("English: ").append(c).append("\n").append("Twi: ").append(a);
               // sb= sb.append(b.substring(0,1)).append("   ").append(b.charAt(1)).append("\n\t").append(b);
                // c= b.substring(0,1) + "  "+ Character.toString(b.charAt(1)) +"   :"+ b.substring(0,2);

                b=  PlayFromFirebase.viewTextConvert(a);

                // Toast.makeText(ReadingActivityTwoLetters.this, sb, Toast.LENGTH_SHORT).show();

                toast.setText(sb);
                toast.show();
                playFromFileOrDownload(b, type);

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
    protected void onDestroy() {
        toast.cancel();
        super.onDestroy();
    }
}
