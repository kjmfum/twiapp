package com.learnakantwi.twiguides;

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
import android.widget.AdapterViewFlipper;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.Collections;
import java.util.Random;

import static com.learnakantwi.twiguides.ProverbsActivity.proverbsArrayList;

public class SubPProverbsActivity extends AppCompatActivity implements RVProverbsAdapter.onClickRecycle {

   // AdView mAdView;
    AdView mAdView1;
    AdapterViewFlipper proverbsViewFlipper;
    StorageReference storageReference;
    MediaPlayer playFromDevice;
    MediaPlayer mp1;
    Toast toast;
    ArrayList<Proverbs> results = new ArrayList<>();
    ArrayList<Proverbs> results1 = new ArrayList<>();

    ProverbsAdapter proverbsAdapter;

    public InterstitialAd mInterstitialAd;
    ImageButton playButton;
    ImageButton pauseButton;
    ImageButton muteButton;
    ImageButton unmuteButton;
    Boolean unMuted = true;
    Random random;
    int showAdProbability;

    int countProverb=0;
    int count= 0;

    long delaytime= 6000;

    boolean slideshowBool = false;

    ArrayList<Proverbs> recycleArrayList;

    Handler handler1;

    Runnable ranable;


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
        }

        else {
            toast.setText("Please Connect to the Internet");
            toast.show();
        }

    }

    public void playFromFileOrDownload(final String filename, final String appearText){
        File myFile = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/PROVERBS/"+filename+ ".m4a");
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
                final StorageReference musicRef = storageReference.child("/Proverbs/" + filename + ".m4a");
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
                    request.setDestinationInExternalFilesDir(getApplicationContext(), Environment.DIRECTORY_MUSIC + "/PROVERBS" , filename + fileExtension);
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

    public void proverbClick(View view){

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


        if (b.contains(" ") ||b.contains("twi:") || b.contains("/") || b.contains(";")|| b.contains("'")|| b.contains(",") || b.contains("(") || b.contains(")") || b.contains("-") | b.contains("?")| b.contains("...")) {
            b = b.replace(" ", "");
            b = b.replace("/", "");
            b= b.replace(",","");
            b= b.replace("(","");
            b= b.replace(")","");
            b= b.replace("-","");
            b= b.replace("?","");
            b= b.replace("...","");
            b= b.replace("twi:","");
            b= b.replace("'","");
            b= b.replace(";","");
        }

       // toast.setText(b);
        //toast.show();
        if (unMuted){
            playFromFileOrDownload(b, a);
        }
        else{
            toast.setText("Sound Muted");
            toast.show();
        }


    }


    public boolean onCreateOptionsMenu(Menu menu) {
        //MenuInflater menuInflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.main_menu_proverbs1, menu);

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
                results1.add(new Proverbs("","",""));

                results = new ArrayList<>();
                for (Proverbs x: proverbsArrayList ){

                    if(x.getTwiProverb().toLowerCase().contains(newText.toLowerCase()) || x.getProverbLiteral().toLowerCase().contains(newText.toLowerCase())
                            || x.getProverbMeaning().toLowerCase().contains(newText.toLowerCase())

                    ){
                        results.add(x);
                    }
                    if (results.size()>0) {
                        ((ProverbsAdapter) proverbsViewFlipper.getAdapter()).update(results);
                    }
                    else{
                        ((ProverbsAdapter) proverbsViewFlipper.getAdapter()).update(results1);
                    }
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
          case R.id.proverbsSlideShow:
                slideshow();
                return  true;
            case R.id.downloadAllAudio:
                downloadClick();
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

    public void downloadClick () {
        int counter = 0;

        toast.setText("Got here");
        toast.show();

        if (isNetworkAvailable()) {
            for (int j = 0; j < proverbsArrayList.size(); j++) {

                String bb = proverbsArrayList.get(j).getTwiProverb();
                bb= bb.toLowerCase();
                boolean dd = bb.contains("ɔ");
                boolean ee = bb.contains("ɛ");
                if (dd || ee) {
                    bb = bb.replace("ɔ", "x");
                    bb = bb.replace("ɛ", "q");
                }

                if (bb.contains(" ") ||bb.contains("twi:") || bb.contains("/") || bb.contains(";")|| bb.contains("'")|| bb.contains(",") || bb.contains("(") || bb.contains(")") || bb.contains("-") | bb.contains("?")| bb.contains("...")) {
                    bb = bb.replace(" ", "");
                    bb = bb.replace("/", "");
                    bb= bb.replace(",","");
                    bb= bb.replace("(","");
                    bb= bb.replace(")","");
                    bb= bb.replace("-","");
                    bb= bb.replace("?","");
                    bb= bb.replace("...","");
                    bb= bb.replace("twi:","");
                    bb= bb.replace("'","");
                    bb= bb.replace(";","");
                }
                File myFiles = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/PROVERBS/"+bb+ ".m4a");
                if (myFiles.exists()) {
                    counter++;
                }

            }
            if (counter == proverbsArrayList.size()) {
                toast.setText("All downloaded");
                toast.show();

            } else {
                toast.setText("Downloading...");
                toast.show();

                // toast.setText(counter + " -- "+ allArrayList.size());
                // toast.show();

                for (int i = 0; i < proverbsArrayList.size(); i++) {
                    String b = proverbsArrayList.get(i).getTwiProverb().toLowerCase();
                    boolean d = b.contains("ɔ");
                    boolean e = b.contains("ɛ");
                    if (d || e) {
                        b = b.replace("ɔ", "x");
                        b = b.replace("ɛ", "q");
                    }

                    if (b.contains(" ") ||b.contains("twi:") || b.contains("/") || b.contains(";")|| b.contains("'")|| b.contains(",") || b.contains("(") || b.contains(")") || b.contains("-") | b.contains("?")| b.contains("...")) {
                        b = b.replace(" ", "");
                        b = b.replace("/", "");
                        b= b.replace(",","");
                        b= b.replace("(","");
                        b= b.replace(")","");
                        b= b.replace("-","");
                        b= b.replace("?","");
                        b= b.replace("...","");
                        b= b.replace("twi:","");
                        b= b.replace("'","");
                        b= b.replace(";","");
                    }

                    File myFile = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/PROVERBS" + b + ".m4a");
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

    public void downloadOnly(final String filename){
        if (isNetworkAvailable()){
            final StorageReference musicRef = storageReference.child("/Proverbs/" + filename + ".m4a");
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
        checkFileinFolder();
    }
    public boolean checkFileinFolder() {

        int counter = 0;

        for (int j = 0; j < proverbsArrayList.size(); j++) {

            String bb = proverbsArrayList.get(j).getTwiProverb();
            bb = bb.toLowerCase();
            boolean dd = bb.contains("ɔ");
            boolean ee = bb.contains("ɛ");
            if (dd || ee) {
                bb = bb.replace("ɔ", "x");
                bb = bb.replace("ɛ", "q");
            }

            if (bb.contains(" ") || bb.contains("twi:") || bb.contains("/") || bb.contains(";") || bb.contains("'") || bb.contains(",") || bb.contains("(") || bb.contains(")") || bb.contains("-") | bb.contains("?") | bb.contains("...")) {
                bb = bb.replace(" ", "");
                bb = bb.replace("/", "");
                bb = bb.replace(",", "");
                bb = bb.replace("(", "");
                bb = bb.replace(")", "");
                bb = bb.replace("-", "");
                bb = bb.replace("?", "");
                bb = bb.replace("...", "");
                bb = bb.replace("twi:", "");
                bb = bb.replace("'", "");
                bb = bb.replace(";", "");
            }
            File myFiles = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/PROVERBS/"+bb+ ".m4a");
            if (myFiles.exists()) {
                counter++;
            }

        }
        if (counter == proverbsArrayList.size()) {
            toast.setText("Download Completed");
            toast.show();
            return true;
        }
        else {
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


   public void next(View view){
       pauseSlideshow();
       //proverbsViewFlipper.stopFlipping();
        proverbsViewFlipper.showNext();
       int position = proverbsViewFlipper.getDisplayedChild();

        Log.i("Mee1","Hi "+ proverbsArrayList.get(position).getTwiProverb());

        String a = proverbsArrayList.get(position).getTwiProverb();
       String b= PlayFromFirebase.viewTextConvert(a);

       if (unMuted){
           playFromFileOrDownload(b, a);
       }


   }

    public void next(){

        //proverbsViewFlipper.stopFlipping();
        proverbsViewFlipper.showNext();
        int position = proverbsViewFlipper.getDisplayedChild();
       // count = position+1;
        count = position;
        Log.i("Mee1","Hi "+ proverbsArrayList.get(position).getTwiProverb());

       /* String b = proverbsArrayList.get(position).getTwiProverb();
        b= PlayFromFirebase.viewTextConvert(b);*/
    }

    public void previous (View view){
        pauseSlideshow();
        proverbsViewFlipper.showPrevious();

        int position = proverbsViewFlipper.getDisplayedChild();
        String a = proverbsArrayList.get(position).getTwiProverb();
        String b= PlayFromFirebase.viewTextConvert(a);

        if (unMuted){
            playFromFileOrDownload(b, a);
        }

    }

    public void slideshow() {

        playButton.setVisibility(View.INVISIBLE);
        pauseButton.setVisibility(View.VISIBLE);
        toast.setText("Proverbs change after 6 seconds");
        toast.show();

        int position = proverbsViewFlipper.getDisplayedChild();
        slideshowBool = true;

        if (slideshowBool){
            count = position;
            handler1.postDelayed(ranable, 2);
        }

    }

    public void slideshow(View view) {
       // proverbsViewFlipper.showNext();
        //proverbsViewFlipper.startFlipping();

        //proverbsViewFlipper.setFlipInterval(6000);


        toast.setText("Proverbs change after 6 seconds");
        toast.show();

        slideshowBool = true;

        int position = proverbsViewFlipper.getDisplayedChild();


        if (slideshowBool){
            count = position;
            handler1.postDelayed(ranable, 2);
        }
       /* else{
            playFromFileOrDownload(b, a);
        }*/


       // onMyItemClick(position, view);
        //proverbsViewFlipper.getChildCount();
    }

    public void pauseSlideshow(View view) {



        pauseButton.setVisibility(View.INVISIBLE);
        pauseButton.setVisibility(View.INVISIBLE);
        playButton.setVisibility(View.VISIBLE);
        if (proverbsViewFlipper.isFlipping()){
            proverbsViewFlipper.stopFlipping();
        }
        toast.setText("Paused");
        toast.show();

        slideshowBool = false;

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
        if (proverbsViewFlipper.isFlipping()){
            proverbsViewFlipper.stopFlipping();
        }

        slideshowBool = false;

        if (handler1 !=null){
            handler1.removeCallbacks(ranable);
        }
        if (playFromDevice!=null){
            playFromDevice.stop();
        }



        //proverbsViewFlipper.getChildCount();
    }

        /*public void addProverbs(){

            if(MainActivity.Lifetime ==1 || MainActivity.Subscribed == 1){
                proverbsArrayList.add(new Proverbs("Baabi a ɔsono bɛfa biara yɛ kwan","Where ever the elephant passes is a way","A powerful person can make a way where there seems to be no way"));
                proverbsArrayList.add(new Proverbs("Ɛba a, ɛka oni", "If it comes it affects mother", "If trouble comes it affects your closest relatives"));
                //  proverbsArrayList.add(new Proverbs("Ɛreba a, mɛbɔ ho ban, ne ɛtoto a, mɛsane, wɔn mu hena na wowɔ n'afa? Mewɔ ɛreba a, mɛbɔ ano ban afa.","hello","YEs"));
                proverbsArrayList.add(new Proverbs("Yɛbaa mmebu a, anka yɛso nkuma", "If we had come to fell proverbs then we would be carrying axes", "We did not come to play. We mean business"));
                proverbsArrayList.add(new Proverbs("Bra bɛhwɛ bi nkyerɛ sɛ behunu sen me","Come and see some does not mean that come and see more than me","If you are offered something, you shouldn't take more than your fair share or more than the one who offered it to you"));
                proverbsArrayList.add(new Proverbs("Bra bɛhwɛ ne deɛ wahunu","Come and see is what you have seen","You can only talk about what you have seen or experienced"));
                proverbsArrayList.add(new Proverbs("Aba a ɛtɔ nyinaa na efifi a, anka obi rennya dua ase kwan","If all seeds that fall grow, then there will be no way to pass under trees", "Not everyone or every endeavour will succeed. That will allow room for others to succeed"));
                //proverbsArrayList.add(new Proverbs("Wo ba koro wu a, na wo kosua korɔ abɔ","a","b"));
                proverbsArrayList.add(new Proverbs("Ɔba nsɛ oni a ɔsɛ ɔse","If a baby or child does not resemble its mother then it resembles its father","Traits that people exhibit are picked up from those closest to you especially your relatives"));
                proverbsArrayList.add(new Proverbs("Ɔba nsu a, yɛma no nom","If a baby does not cry we let it drink","You don't have to wait for someone to cry or toil for something that you know he needs before giving it"));
                proverbsArrayList.add(new Proverbs("Ɔbaakofoɔ nkyere bɔdamfo","One person does not arrest a mad person","We have to work together"));
                // proverbsArrayList.add(new Proverbs("","",""));

                proverbsArrayList.add(new Proverbs("Ɔbaa na ɔwo ɔbarima","It is a woman who gives birth to a man","A seemingly weak person can help a strong person to succeed"));
                proverbsArrayList.add(new Proverbs("Ɔbaa ne ne kunu asɛm, obi nnim mu","No one knows the issues between a woman and her husband","Intimacy is required to know underlying issues. Marriage issues are best known to the couples only. Don't interfere in marital affairs of others"));
                proverbsArrayList.add(new Proverbs("Ɔbaakofoɔ di awu a, ɔsoa ne funu","If one person kills, he carries the corpse","If you plot and commit a sin alone, you will suffer the consequence alone"));
                proverbsArrayList.add(new Proverbs("Mmaapɛ mu wɔ ade a, anka ɔpapo da apakan mu","If there was something to be gained from being a womanizer the he-goat would ride in a palanquin","Look at womanizers and learn that their lifestyle do not make them chiefs. You will not gain profit by being a womanizer"));
                proverbsArrayList.add(new Proverbs("Ɔbaatan na onim nea ne ba bedi","A mother knows what her child will eat","Someone who cares for your physical needs even before you ask can be considered your mother"));
                proverbsArrayList.add(new Proverbs("Abaa a yɛde bɔ efie aboa no, yɛmfa mmɔ wuram aboa","The stick that is used to hit the domestic animal is not the same stick that is used to hit a wild animal","Friends should be treated differently from strangers (even if they all deserve to be punished)"));
                proverbsArrayList.add(new Proverbs("Ɔbaa kɔ adware na ɔmma ntɛm a, na ɔresiesie ne ho","If a woman goes to the bathroom and she delays, she is tidying herself up","If someone or something is delaying maybe there is a good reason. Be patient when expecting good things. There is an underlying reason for each cause"));
                proverbsArrayList.add(new Proverbs("Baabi dehyeɛ kɔsom a, wɔfrɛ no afenaa","When a royal goes to another place to serve others, she is called a slave","You can be regarded with great respect within your community or family but viewed as of little value by those of another community"));
                proverbsArrayList.add(new Proverbs("Baabiara nni hɔ a wotena we atadwe a, ɛnyɛ dɛ","There is no place that if you sit and chew tiger nuts, the tiger nuts will become bitter","A good thing is good, no matter where it is located"));
                proverbsArrayList.add(new Proverbs("Sɛ ababaa wu na yɛde aberewa di n'adeɛ a, na ayie no na yɛatu ahyɛ da","If a young lady dies and we make an old lady inherit her property then it is the funeral that we are postponing to another day","If you replace a good thing with a bad thing then you will have to replace it again very soon"));
                proverbsArrayList.add(new Proverbs("Ɔbaa na onim ne kunu yam kɔm","A woman knows how hungry her husband is","Those closest to you are those who know what you are going through"));
                proverbsArrayList.add(new Proverbs("Sɛ bafan nnim hwee mpo a, onim nsam bɔ","If a cripple knows nothing at all, he can clap his hands","Even if you are disabled or disadvantaged in a way, there is still something you can do"));
                proverbsArrayList.add(new Proverbs("Ɔba kwasea rehonhono a, ɔse ɔreyɛ kɛse","If a stupid child (person) is developing a swollen body he says he is getting fat","A stupid person cannot see the truth in the reality of events"));
                // proverbsArrayList.add(new Proverbs("Ɔbaa ","",""));

                proverbsArrayList.add(new Proverbs("Ɔbaako yɛ ya","One is painful","To be a alone hurts"));
                proverbsArrayList.add(new Proverbs("Ɔbaakofo di ɛwoɔ a enyane ne yam","If one person eats honey alone it awakens his stomach","A greedy person will suffer. If one enjoys good things alone he will suffer"));
                proverbsArrayList.add(new Proverbs("Ɔbaakofo nkyekyere kurow","One person does not build a city","You cannot achieve great things by working alone"));
                proverbsArrayList.add(new Proverbs("Ɔbaakofo na ɔto tuo na ɛdɔm guo","One person shoots a gun and an army falls","One person's decision determines the outcome.(Usually said when one makes a wise suggestion"));
                proverbsArrayList.add(new Proverbs("Ɔbaako nnante anadwo","One person does not walk alone at night","In blurry situations seek the company of others. Be careful and take care"));
                proverbsArrayList.add(new Proverbs("Ɔbaakofo nsa nso nyame ani kata","One person's hand cannot cover god's eyes","You need help to be able to do something that seems impossible"));
                proverbsArrayList.add(new Proverbs("Ɔbaako tirim nni adwen","There are no thoughts in the head of a single person","It is difficult to come up with wise thought if you don't consult others"));
                proverbsArrayList.add(new Proverbs("Ɔdaadaafo na ɔsɛe adamfo","A deceitful person spoils a friend","It is the dishonesty of people that corrupts a friend"));
                proverbsArrayList.add(new Proverbs("Dadeɛ, yɛse no boɔ so, na yɛnse no nku so","Iron, we sharpen it on a stone but not on cream","Use the right tools for the job at hand"));
                proverbsArrayList.add(new Proverbs("W'adaka si aburokyire mpo a, nea ɛwɔ mu nyinaa wo nim","If your box is overseas, you still know it's contents","You know what is yours and its details no matter where it may be"));
                proverbsArrayList.add(new Proverbs("Adamfoɔ, adamfoɔ ɛne me nnɛ","Friends, friends and this is me today","Being over friendly has made me lose a lot"));
                proverbsArrayList.add(new Proverbs("Dammirifua firi tete, ɛmfiri nnɛ","Condolences are from ancient times, they ar not from today","The causes of bad events are from the remote past not from present situations"));
                proverbsArrayList.add(new Proverbs("Ɛdan wɔ aso","Houses have ears","Others can hear you even when you think you are alone. Be careful of what you say if you don't want it to spread"));
                proverbsArrayList.add(new Proverbs("Wodan wo na ka a, wokɔto w'agya deɛ","If you request your mother to pay you what she owes you, you go and meet that of your father","The boss pays the debt in the long run"));
                proverbsArrayList.add(new Proverbs("Wodan kusie amoakua a, ɔrennane da","If you change a rat into a squirrel it won't change","It is impossible to change the real nature of a person"));

            }

            Toast.makeText(this, "After add: "+ proverbsArrayList.size(), Toast.LENGTH_SHORT).show();
        }*/

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_proverbs_one);
        // setContentView(R.layout.activity_proverbs);

        if (MainActivity.Subscribed != 1){

            random = new Random();
            showAdProbability = random.nextInt(10);

            mInterstitialAd = new InterstitialAd(this);
            mInterstitialAd.setAdUnitId("ca-app-pub-7384642419407303/9880404420");
            mInterstitialAd.loadAd(new AdRequest.Builder().build());

            //Toast.makeText(this, "Show Advert: " +  proverbsArrayList.size(), Toast.LENGTH_SHORT).show();
            MobileAds.initialize(this, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {
                }
            });
            mAdView1 = findViewById(R.id.adView1);
            AdRequest adRequest1 = new AdRequest.Builder().build();
            mAdView1.loadAd(adRequest1);
        }
        else{
           // Toast.makeText(this, "No Advert: " +  proverbsArrayList.size(), Toast.LENGTH_SHORT).show();
            //addProverbs();
        }

        playButton = findViewById(R.id.playButton);
        playButton.setVisibility(View.VISIBLE);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideshow();
            }
        });

        pauseButton = findViewById(R.id.pauseButton);
        pauseButton.setVisibility(View.INVISIBLE);

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseSlideshow(v);
            }
        });

        muteButton = findViewById(R.id.ivMuteButton);
        unmuteButton = findViewById(R.id.ivUnMuteButton);
        muteButton.setVisibility(View.VISIBLE);
        unmuteButton.setVisibility(View.INVISIBLE);


        unmuteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unMuted = true;
                Toast.makeText(SubPProverbsActivity.this, "Sound Unmuted", Toast.LENGTH_SHORT).show();
                muteButton.setVisibility(View.VISIBLE);
                unmuteButton.setVisibility(View.INVISIBLE);
            }
        });

        muteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                unMuted = false;
                Toast.makeText(SubPProverbsActivity.this, "Sound Muted", Toast.LENGTH_SHORT).show();
                unmuteButton.setVisibility(View.VISIBLE);
                muteButton.setVisibility(View.INVISIBLE);
            }
        });


        /*proverbsArrayList.add(new Proverbs("Ɛba a, ɛka oni", "If it comes it affects mother", "If trouble comes it affects your closest relatives"));
      //  proverbsArrayList.add(new Proverbs("Ɛreba a, mɛbɔ ho ban, ne ɛtoto a, mɛsane, wɔn mu hena na wowɔ n'afa? Mewɔ ɛreba a, mɛbɔ ano ban afa.","hello","YEs"));
        proverbsArrayList.add(new Proverbs("Yɛbaa mmebu a, anka yɛso nkuma", "If we had come to fell proverbs then we would be carrying axes", "We did not come to play. We mean business"));
        proverbsArrayList.add(new Proverbs("Bra bɛhwɛ bi nkyerɛ sɛ behunu sen me","Come and see some does not mean that come and see more than me","If you are offered something, you shouldn't take more than your fair share or more than the one who offered it to you"));
        proverbsArrayList.add(new Proverbs("Bra bɛhwɛ ne deɛ wahunu","Come and see is what you have seen","You can only talk about what you have seen or experienced"));
        proverbsArrayList.add(new Proverbs("Aba a ɛtɔ nyinaa na efifi a, anka obi rennya dua ase kwan","If all seeds that fall grow, then there will be no way to pass under trees", "Not everyone or every endeavour will succeed. That will allow room for others to succeed"));
        //proverbsArrayList.add(new Proverbs("Wo ba koro wu a, na wo kosua korɔ abɔ","a","b"));
        proverbsArrayList.add(new Proverbs("Ɔba nsɛ oni a ɔsɛ ɔse","If a baby or child does not resemble its mother then it resembles its father","Traits that people exhibit are picked up from those closest to you especially your relatives"));
        proverbsArrayList.add(new Proverbs("Ɔba nsu a, yɛma no nom","If a baby does not cry we let it drink","You don't have to wait for someone to cry or toil for something that you know he needs before giving it"));
        proverbsArrayList.add(new Proverbs("Ɔbaakofoɔ nkyere bɔdamfo","One person does not arrest a mad person","We have to work together"));
        // proverbsArrayList.add(new Proverbs("","",""));

        proverbsArrayList.add(new Proverbs("Ɔbaa na ɔwo ɔbarima","It is a woman who gives birth to a man","A seemingly weak person can help a strong person to succeed"));
        proverbsArrayList.add(new Proverbs("Ɔbaa ne ne kunu asɛm, obi nnim mu","No one knows the issues between a woman and her husband","Intimacy is required to know underlying issues. Marriage issues are best known to the couples only. Don't interfere in marital affairs of others"));
        proverbsArrayList.add(new Proverbs("Ɔbaakofoɔ di awu a, ɔsoa ne funu","If one person kills, he carries the corpse","If you plot and commit a sin alone, you will suffer the consequence alone"));
        proverbsArrayList.add(new Proverbs("Mmaapɛ mu wɔ ade a, anka ɔpapo da apakan mu","If there was something to be gained from being a womanizer the he-goat would ride in a palanquin","Look at womanizers and learn that their lifestyle do not make them chiefs. You will not gain profit by being a womanizer"));
        proverbsArrayList.add(new Proverbs("Ɔbaatan na onim nea ne ba bedi","A mother knows what her child will eat","Someone who cares for your physical needs even before you ask can be considered your mother"));
        proverbsArrayList.add(new Proverbs("Abaa a yɛde bɔ efie aboa no, yɛmfa mmɔ wuram aboa","The stick that is used to hit the domestic animal is not the same stick that is used to hit a wild animal","Friends should be treated differently from strangers (even if they all deserve to be punished)"));
        proverbsArrayList.add(new Proverbs("Ɔbaa kɔ adware na ɔmma ntɛm a, na ɔresiesie ne ho","If a woman goes to the bathroom and she delays, she is tidying herself up","If someone or something is delaying maybe there is a good reason. Be patient when expecting good things. There is an underlying reason for each cause"));
        proverbsArrayList.add(new Proverbs("Baabi dehyeɛ kɔsom a, wɔfrɛ no afenaa","When a royal goes to another place to serve others, she is called a slave","You can be regarded with great respect within your community or family but viewed as of little value by those of another community"));
        proverbsArrayList.add(new Proverbs("Baabiara nni hɔ a wotena we atadwe a, ɛnyɛ dɛ","There is no place that if you sit and chew tiger nuts, the tiger nuts will become bitter","A good thing is good, no matter where it is located"));
        proverbsArrayList.add(new Proverbs("Sɛ ababaa wu na yɛde aberewa di n'adeɛ a, na ayie no na yɛatu ahyɛ da","If a young lady dies and we make an old lady inherit her property then it is the funeral that we are postponing to another day","If you replace a good thing with a bad thing then you will have to replace it again very soon"));
        proverbsArrayList.add(new Proverbs("Ɔbaa na onim ne kunu yam kɔm","A woman knows how hungry her husband is","Those closest to you are those who know what you are going through"));
        proverbsArrayList.add(new Proverbs("Sɛ bafan nnim hwee mpo a, onim nsam bɔ","If a cripple knows nothing at all, he can clap his hands","Even if you are disabled or disadvantaged in a way, there is still something you can do"));
        proverbsArrayList.add(new Proverbs("Ɔba kwasea rehonhono a, ɔse ɔreyɛ kɛse","If a stupid child (person) is developing a swollen body he says he is getting fat","A stupid person cannot see the truth in the reality of events"));
       // proverbsArrayList.add(new Proverbs("Ɔbaa ","",""));

        proverbsArrayList.add(new Proverbs("Ɔbaako yɛ ya","One is painful","To be a alone hurts"));
        proverbsArrayList.add(new Proverbs("Ɔbaakofo di ɛwoɔ a enyane ne yam","If one person eats honey alone it awakens his stomach","A greedy person will suffer. If one enjoys good things alone he will suffer"));
        proverbsArrayList.add(new Proverbs("Ɔbaakofo nkyekyere kurow","One person does not build a city","You cannot achieve great things by working alone"));
        proverbsArrayList.add(new Proverbs("Ɔbaakofo na ɔto tuo na ɛdɔm guo","One person shoots a gun and an army falls","One person's decision determines the outcome.(Usually said when one makes a wise suggestion"));
        proverbsArrayList.add(new Proverbs("Ɔbaako nnante anadwo","One person does not walk alone at night","In blurry situations seek the company of others. Be careful and take care"));
        proverbsArrayList.add(new Proverbs("Ɔbaakofo nsa nso nyame ani kata","One person's hand cannot cover god's eyes","You need help to be able to do something that seems impossible"));
        proverbsArrayList.add(new Proverbs("Ɔbaako tirim nni adwen","There are no thoughts in the head of a single person","It is difficult to come up with wise thought if you don't consult others"));
        proverbsArrayList.add(new Proverbs("Ɔdaadaafo na ɔsɛe adamfo","A deceitful person spoils a friend","It is the dishonesty of people that corrupts a friend"));
        proverbsArrayList.add(new Proverbs("Dadeɛ, yɛse no boɔ so, na yɛnse no nku so","Iron, we sharpen it on a stone but not on cream","Use the right tools for the job at hand"));
        proverbsArrayList.add(new Proverbs("W'adaka si aburokyire mpo a, nea ɛwɔ mu nyinaa wo nim","If your box is overseas, you still know it's contents","You know what is yours and its details no matter where it may be"));
        proverbsArrayList.add(new Proverbs("Adamfoɔ, adamfoɔ ɛne me nnɛ","Friends, friends and this is me today","Being over friendly has made me lose a lot"));
        proverbsArrayList.add(new Proverbs("Dammirifua firi tete, ɛmfiri nnɛ","Condolences are from ancient times, they ar not from today","The causes of bad events are from the remote past not from present situations"));
        proverbsArrayList.add(new Proverbs("Ɛdan wɔ aso","Houses have ears","Others can hear you even when you think you are alone. Be careful of what you say if you don't want it to spread"));
        proverbsArrayList.add(new Proverbs("Wodan wo na ka a, wokɔto w'agya deɛ","If you request your mother to pay you what she owes you, you go and meet that of your father","The boss pays the debt in the long run"));
        proverbsArrayList.add(new Proverbs("Wodan kusie amoakua a, ɔrennane da","If you change a rat into a squirrel it won't change","It is impossible to change the real nature of a person"));
*/


        handler1 = new Handler();
        Handler handler2 = new Handler();


        storageReference = FirebaseStorage.getInstance().getReference();
       // toast = Toast.makeText(getApplicationContext(), "Subscribed" , Toast.LENGTH_SHORT);

        toast = Toast.makeText(getApplicationContext(), "Tap Twi to Listen", Toast.LENGTH_SHORT);
        toast.show();


        proverbsViewFlipper = findViewById(R.id.proverbsAdapterViewFlipper);

        count= proverbsViewFlipper.getDisplayedChild();

        ranable = new Runnable() {
            @Override
            public void run() {
                    next();
                   // String a = recycleArrayList.get(count).getTwiProverb();
                String a = proverbsArrayList.get(count).getTwiProverb();
                    String b = PlayFromFirebase.viewTextConvert(a);
                    if (unMuted){
                        playFromFileOrDownload(b, a);
                    }
                    handler1.postDelayed(ranable, 6500);
            }
        };


        proverbsViewFlipper.setOnTouchListener(new OnSwipeTouchListener(SubPProverbsActivity.this) {
            public void onSwipeTop() {
            }
            public void onSwipeRight() {
               // Toast.makeText(ProverbsActivity.this, "right", Toast.LENGTH_SHORT).show();
                proverbsViewFlipper.showPrevious();
            }
            public void onSwipeLeft() {
                //Toast.makeText(ProverbsActivity.this, "left", Toast.LENGTH_SHORT).show();
                proverbsViewFlipper.showNext();
            }
            public void onSwipeBottom() {
            }

        });


        Collections.shuffle(proverbsArrayList);
       // proverbsAdapter = new ProverbsAdapter(this, recycleArrayList);
        proverbsAdapter = new ProverbsAdapter(this, proverbsArrayList);


        proverbsViewFlipper.setAdapter(proverbsAdapter);


        PlayFromFirebase convertText = new PlayFromFirebase();

        //String b = PlayFromFirebase.viewTextConvert(proverbsAdapter.playSound(0));
       // Toast.makeText(this, "Hi "+ b, Toast.LENGTH_SHORT).show();

       // playFromFileOrDownload(b,"Tap Twi To Listen");
    }

    @Override
    public void onMyItemClick(int position, View view) {

        count = position;

        String b = recycleArrayList.get(position).getTwiProverb();

        b = PlayFromFirebase.viewTextConvert(b);

        String a = recycleArrayList.get(position).getTwiProverb()+" is: "+ recycleArrayList.get(position).getProverbMeaning();

        ///
        Log.i("Hello", "Count: "+count +" position: "+position);

        if(count != -1 ){
            handler1.removeCallbacks(ranable);
            if (playFromDevice!=null){
                playFromDevice.stop();
            }

        }

//        handler1 = new Handler();
        count =position;

        if (slideshowBool){
            handler1.postDelayed(ranable, 2);
        }
        else{
            if (unMuted){
                playFromFileOrDownload(b, a);
            }
        }

        ////

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

