package com.learnakantwi.twiguides;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hotchemi.android.rate.AppRate;

import static android.Manifest.permission.INTERNET;
import static com.learnakantwi.twiguides.SubChildrenAnimals.childrenAnimalsArrayList;
import static com.learnakantwi.twiguides.SubConversationApologiesAndResponses.conversationsApologiesArrayList;
import static com.learnakantwi.twiguides.SubConversationDirections.conversationDirections;
import static com.learnakantwi.twiguides.SubConversationHospital.conversationHospital;
import static com.learnakantwi.twiguides.SubConversationIntroductionActivity.conversationArrayList;
import static com.learnakantwi.twiguides.SubConversationLove.conversationLove;
import static com.learnakantwi.twiguides.SubConversationPhone.conversationPhone;
import static com.learnakantwi.twiguides.SubConversationWelcomingOthers.conversationWelcomingOthersArrayList;

//import android.support.v7.app.AppCompatActivity;

public class HomeMainActivity extends AppCompatActivity implements RVHomeMainAdapter.onClickRecycle {
    //  app:adUnitId="ca-app-pub-6999427576830667~6251296006"ˆ


    static ArrayList<HomeMainButton> homeMainButtonArrayList;
    BillingClient billingClient;
    String premiumUpgradePrice;
    String userEmail;

    Button buyButton;
    Toast toast;
    ListView homeListView;
    MediaPlayer mediaPlayer;
    SharedPreferences subscriptionStatePreference;
    boolean subscriptionState;

    TextView tvSignIn;
    FirebaseAuth mAuth;
    FirebaseUser User;
    String displayName;

    String currentUserEmail;

    RecyclerView recyclerView;
    RVHomeMainAdapter rvHomeMainAdapter;
    AdView mAdView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference usersReference = db.collection("users");
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //MenuInflater menuInflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.vocabulary_menu, menu);

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
                rvHomeMainAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {

            case R.id.quiz1:
                goToQuizHome();
                return true;
            case R.id.searchAll:
                goToAll();
                return true;
            case R.id.videoCourse:
                goToWeb();
                return true;
            case R.id.rate:
                rateMe();
                return true;
            case R.id.share:
                shareApp();
                return true;
            case R.id.dailyTwiAlert:
                tunOnDailyTwi();
                return true;
            default:
                return false;
        }
    }

    public void shareApp(){
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        //sharingIntent.setAction("http://play.google.com/store/apps/details?id=" + getPackageName());
        sharingIntent.setType("text/plain");
        String shareBody = "http://play.google.com/store/apps/details?id=" + getPackageName();
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Please install this Nice Android Twi App");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    public void rateMe() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + getPackageName())));
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
        }
    }

    public void goToMain(){
        Intent intent = new Intent(getApplicationContext(), VocabularyMain.class);
        startActivity(intent);
    }

    public void goToSettings(){
        Intent intent = new Intent(getApplicationContext(), SettingsAndTips.class);
        startActivity(intent);
    }

    public void goToPleaseSubPage(){
        Intent intent = new Intent(getApplicationContext(), PleaseSubscribePage.class);
        startActivity(intent);
    }

    public void goToReading(){
        Intent intent = new Intent(getApplicationContext(), ReadingActivityMain.class);
        startActivity(intent);
    }



    public void goToQuizAll() {
        Intent intent = new Intent(getApplicationContext(), QuizSubHome.class);
        startActivity(intent);
    }

    public void goToQuizHome() {
        Intent intent = new Intent(getApplicationContext(), QuizHome.class);
        startActivity(intent);
    }

    public void goToProverbs() {
        Intent intent = new Intent(getApplicationContext(), ProverbsHome.class);
        startActivity(intent);
    }
    public void goToNoticeBoard(){
        Intent intent = new Intent(getApplicationContext(), NoticeBoardActivity.class);
        startActivity(intent);
    }

    public void goToWeb() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.udemy.com/course/learn-akan-twi/?referralCode=6D321CE6AEE1834CCB0F"));
        startActivity(intent);
    }


    public void goToAll() {
        Intent intent = new Intent(getApplicationContext(), SubPAllActivity.class);
        startActivity(intent);
    }

    public void goToChildren() {
        Intent intent = new Intent(getApplicationContext(), ChildrenHome.class);
        startActivity(intent);
    }

    public void goToGames() {
        Intent intent = new Intent(getApplicationContext(), Games.class);
        startActivity(intent);
    }

    public void tunOnDailyTwi() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.learnakantwi.twiguides", Context.MODE_PRIVATE);
        String dailyTwiPreference = sharedPreferences.getString("DailyTwiPreference", "Yes");
        sharedPreferences.edit().putString("DailyTwiPreference", "Yes").apply();
        Toast.makeText(this, "Daily Twi Alerts Turned On", Toast.LENGTH_SHORT).show();
    }

    public void goToSubscriptionPage (View v){
        Toast.makeText(this, String.valueOf(subscriptionState), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), InAppActivity.class);
        startActivity(intent);
    }

    public void deleteDuplicateConversation() {
        File myFileConversation = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/SUBCONVERSATION/");
        File[] files2 = myFileConversation.listFiles();

        for (int i = 0; i < files2.length; i++) {

            File fileConversation = files2[i];
            if (fileConversation.getName().contains("-")) {
                fileConversation.delete();
            }
        }
    }

    public void deleteDuplicateProverbs() {
        File myFileProverbs = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/PROVERBS/");
        File[] files2 = myFileProverbs.listFiles();


        for (int i = 0; i < files2.length; i++) {

            File fileConversation = files2[i];
            if (fileConversation.getName().contains("-")) {
                fileConversation.delete();
            }
        }

    }

    public void deleteDuplicateVocabulary() {
        File myFiles = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/");

        File[] files1 = myFiles.listFiles();


        for (int j = 0; j < files1.length; j++) {
            File file = files1[j];
            if (file.getName().contains("-")) {
                file.delete();
                //toast.setText("Deleted");
                //toast.show();

            }
        }
    }

    public void deleteDuplicatelDownload() {

        try {
            deleteDuplicateVocabulary();
            deleteDuplicateConversation();
            deleteDuplicateProverbs();
        } catch (NullPointerException e) {
            System.out.println("Error Null");
        } catch (Exception any) {
            System.out.println("Strange Exception Caught");
        }

    }

    public void SignIn(View view){
        //Intent homeIntent = new Intent(getApplicationContext(), SignInActivity.class);
        //startActivity(homeIntent);

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.i("getInstanceId failed", task.getException().toString());
                            // Toast.makeText(SubPHomeMainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        // String msg = getString(R.string.msg_token_fmt, token);
                        Log.i("getInstanceId Good", token);
                        // Toast.makeText(SubPHomeMainActivity.this, token, Toast.LENGTH_SHORT).show();

                        sendRegistrationToServer(token);
                    }
                });
    }

    public void sendRegistrationToServer(String token){

        Toast.makeText(this, "SentToken", Toast.LENGTH_SHORT).show();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference reference = database.getReference("message");

        // reference.setValue("Hello, World!");
        reference.push().setValue(token);



        Map<String, String> user = new HashMap<>();

        user.put("Justice", token);

        // Create a new user with a first and last name
      /*  Map<String, Object> user = new HashMap<>();
        user.put("first", "Ada");
        user.put("last", "Lovelace");
        user.put("born", 1815);*/



// Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("Debugged", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Debugged", "Error adding document", e);
                    }
                });

    }


    @Override
    public void onMyItemClick(int position, View view) {
        String me1 = homeMainButtonArrayList.get(position).getNameofActivity();


        switch (me1){
            case "Reading":
                goToReading();
                return;
            case "Manage Storage":
                goToSettings();
                return;
            case "Conversations":
            case "Premium Users":
                goToPleaseSubPage();
                return;
            case "Vocabulary":
                goToMain();
                return;
            case "Children":
                goToChildren();
                return;
            case "Proverbs":
                goToProverbs();
                return;
            case "Quiz":
                goToQuizHome();
                return;
            case "Non Twi Games":
                goToGames();
        }
    }

    public void goToSubscriptionPage (){
        Intent intent = new Intent(getApplicationContext(), InAppActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subpactivity_home1);


        if (Build.VERSION.SDK_INT > 22) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    INTERNET,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.SET_ALARM
            }, 1);

            if (Build.VERSION.SDK_INT > 27) {
                requestPermissions(new String[]{Manifest.permission.FOREGROUND_SERVICE
                }, 1);
            }
        }
        recyclerView = findViewById(R.id.recyclerView);

        toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);

        toast.setText("Akwaaba");
        toast.show();

        mAuth = FirebaseAuth.getInstance();

        User = mAuth.getCurrentUser();

        tvSignIn = findViewById(R.id.tvSignIn);
        tvSignIn.setText("PURCHASE OR SUBSCRIBE FOR MORE CONTENT");
        tvSignIn.setTypeface(tvSignIn.getTypeface(), Typeface.BOLD_ITALIC);
        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSubscriptionPage();
            }
        });


        deleteDuplicatelDownload();

        mAdView = findViewById(R.id.adView);
        if (MainActivity.Subscribed!=1){

            MobileAds.initialize(this, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {
                }
            });
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        }else{
            mAdView.setVisibility(View.GONE);
        }


        AppRate.with(this)
                .setInstallDays(0)
                .setLaunchTimes(5)
                .setRemindInterval(2)
                .monitor();

        AppRate.showRateDialogIfMeetsConditions(this);

        homeMainButtonArrayList = new ArrayList<>();

        homeMainButtonArrayList.add(new HomeMainButton("Proverbs", R.drawable.proverbsimage));
        homeMainButtonArrayList.add(new HomeMainButton("Quiz", R.drawable.quizimage));
        homeMainButtonArrayList.add(new HomeMainButton("Vocabulary", R.drawable.vocabularyimage));
        homeMainButtonArrayList.add(new HomeMainButton("Children", R.drawable.childrenimage));
        homeMainButtonArrayList.add(new HomeMainButton("Conversations", R.drawable.conversationimage));
        homeMainButtonArrayList.add(new HomeMainButton("Reading", R.drawable.readingimage));
        homeMainButtonArrayList.add(new HomeMainButton("Manage Storage", R.drawable.ic_download_audio));
        homeMainButtonArrayList.add(new HomeMainButton("Non Twi Games", R.drawable.gamesimage));

       // homeMainButtonArrayList.add(new HomeMainButton("Premium Users", R.drawable.noticeimage));


        rvHomeMainAdapter = new RVHomeMainAdapter(this, homeMainButtonArrayList, this);
        // recyclerView.setLayoutManager(new LinearLayoutManager(this));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(rvHomeMainAdapter);
    }


   /* @Override
    public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> purchases) {
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK
                && purchases != null) {
            toast.setText("Okay1");
            toast.show();
        } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
            // Handle an error caused by a user cancelling the purchase flow.
            Toast.makeText(this, "You cancelled the Purchase", Toast.LENGTH_SHORT).show();
            billingClient.endConnection();}
        else if(billingResult.getResponseCode()== BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED){
            Toast.makeText(this, "Already Purchased", Toast.LENGTH_SHORT).show();
        } else {
            // Handle any other error codes.
            Toast.makeText(this,"Could not complete purchase", Toast.LENGTH_LONG).show();
        }
    }*/

}



