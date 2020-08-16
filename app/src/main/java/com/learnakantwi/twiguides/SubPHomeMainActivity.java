package com.learnakantwi.twiguides;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.rpc.context.AttributeContext;

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
import static com.learnakantwi.twiguides.SubConversationWelcomingOthers.conversationWelcomingOthersArrayList;
import static com.learnakantwi.twiguides.SubConversationPhone.conversationPhone;

//import android.support.v7.app.AppCompatActivity;

public class SubPHomeMainActivity extends AppCompatActivity implements PurchasesUpdatedListener, RVHomeMainAdapter.onClickRecycle {
    //  app:adUnitId="ca-app-pub-6999427576830667~6251296006"ˆ


   static ArrayList<HomeMainButton> homeMainButtonArrayList;
    BillingClient billingClient;
    String premiumUpgradePrice;
    Button buyButton;
    Toast toast;
    ListView homeListView;
    MediaPlayer mediaPlayer;
    SharedPreferences subscriptionStatePreference;
    boolean subscriptionState;

    TextView tvSignIn;
    FirebaseAuth mAuth;
    FirebaseUser User;

    String currentUser;

    RecyclerView recyclerView;
    RVHomeMainAdapter rvHomeMainAdapter;


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

                /*final ArrayList<HomeMainButton> results = new ArrayList<>();
                for (HomeMainButton x: homeMainButtonArrayList ){

                    if(x.getNameofActivity().toLowerCase().contains(newText.toLowerCase())

                    ){
                        results.add(x);
                    }

                    ((HomeMainAdapter)homeListView.getAdapter()).update(results);

                   homeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            String me1 = results.get(position).getNameofActivity();


                            switch (me1){
                                case "Reading":
                                    goToReading();
                                    return;
                                case "Manage Storage":
                                    goToSettings();
                                    return;
                                case "Conversation":
                                    goToConversation();
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
                            }
                        }
                    });
                }

*/
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
           /*case R.id.settings:
                Log.i("Menu Item Selected", "Settings");
                playAll();
                return true;
            case R.id.alphabets:
                Log.i("Menu Item Selected", "Alphabets");
                return  true;*/

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
        Intent intent = new Intent(getApplicationContext(), SubPHomeVocabulary.class);
        startActivity(intent);
    }

    public void goToSettings(){
        Intent intent = new Intent(getApplicationContext(), SettingsAndTips.class);
        startActivity(intent);
    }

    public void goToConversation(){
       // Intent intent = new Intent(getApplicationContext(), SubConversationIntroductionActivity.class);
       // Intent intent = new Intent(getApplicationContext(), SubConversationMain.class);
        Intent intent = new Intent(getApplicationContext(), SubConversationHomeActivity.class);
        startActivity(intent);
    }

    public void goToReading(){
        Intent intent = new Intent(getApplicationContext(), SubPReadingActivityMain.class);
        startActivity(intent);
    }



    public void goToQuizAll() {
        Intent intent = new Intent(getApplicationContext(), QuizSubHome.class);
        startActivity(intent);
    }

    public void goToQuizHome() {
        Intent intent = new Intent(getApplicationContext(), QuizSubHome.class);
        startActivity(intent);
    }

    public void goToProverbs() {
        Intent intent = new Intent(getApplicationContext(), SubPProverbsHome.class);
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
       // Intent intent = new Intent(getApplicationContext(), ChildrenNumberCount.class);
        Intent intent = new Intent(getApplicationContext(), SubChildrenHome.class);
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

    public void buyMe() {
        setUpBillingClient();
    }

    public void setUpBillingClient() {
        billingClient = BillingClient.newBuilder(this)
                .setListener(this)
                .enablePendingPurchases()
                .build();
        setUpBilling();
    }

    public void setUpBilling(){


        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                        //


                    List<String> skuList = new ArrayList<>();
                    skuList.add("reading_club");
                    // skuList.add("gas");
                    SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
                    params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS);



                    billingClient.querySkuDetailsAsync(params.build(),
                            new SkuDetailsResponseListener() {
                                @Override
                                public void onSkuDetailsResponse(BillingResult billingResult, List<SkuDetails> skuDetailsList) {
                                    // Process the result.

                                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
                                        toast.setText("Already Purchased 1");
                                        toast.show();

                                    } else{
                                        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && skuDetailsList != null) {
                                            for (SkuDetails skuDetails : skuDetailsList) {
                                                String sku = skuDetails.getSku();
                                                String price = skuDetails.getPrice();
                                                if ("reading_club".equals(sku)) {
                                                    premiumUpgradePrice = price;


                                                    Purchase.PurchasesResult purchasesResult = billingClient.queryPurchases(BillingClient.SkuType.SUBS);
                                                    List<Purchase> purchasesList = purchasesResult.getPurchasesList();
                                                    //


                                                    if (purchasesList !=null && !purchasesList.isEmpty()){
                                                        String me1 = purchasesList.get(0).getOrderId();
                                                        int me2 = purchasesList.get(0).getPurchaseState();
                                                        int me3=  purchasesList.size();

                                                        //toast.setText(Integer.toString(me3));
                                                        /*toast.setText(me1);
                                                        toast.show();*/

                                                    }
                                                    else{
                                                        toast.setText("nothing subscribed");
                                                        toast.show();
                                                        goToAll();
                                                    }


                                                    //purchase.getOrderId();

//                                                    billingClient.queryPurchases(sku);
//                                                    Purchase.PurchasesResult purchasesResult = billingClient.queryPurchases(BillingClient.SkuType.SUBS);
//                                                    purchasesResult.getPurchasesList();

                                                } /*else if ("gas".equals(sku)) {
                                                gasPrice = price;
                                            }*/
                                            }
                                        }
                                    }



                                }
                            });
                }
            }
            @Override
            public void onBillingServiceDisconnected() {
                Toast.makeText(SubPHomeMainActivity.this, "I got disconnected", Toast.LENGTH_SHORT).show();
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                setUpBillingClient();
            }


        });
        //billingClient.endConnection();
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
                            Toast.makeText(SubPHomeMainActivity.this, "Success", Toast.LENGTH_SHORT).show();
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


        FirebaseFirestore db = FirebaseFirestore.getInstance();
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

    public void SignIn(){
        Intent homeIntent = new Intent(getApplicationContext(), SignInActivity.class);
        startActivity(homeIntent);
      /*  FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.i("getInstanceId failed", task.getException().toString());
                            Toast.makeText(SubPHomeMainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        Log.i("getInstanceId Good", token);
                        Toast.makeText(SubPHomeMainActivity.this, token, Toast.LENGTH_SHORT).show();

                        sendRegistrationToServer(token);
                    }
                });*/



    }

   /* public void Transition(View v){
        Intent homeIntent = new Intent(getApplicationContext(), HomeMainActivity.class);
        startActivity(homeIntent);
    }*/

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
            case "Conversation":
                goToConversation();
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
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subpactivity_home1);


        recyclerView = findViewById(R.id.recyclerView);

        toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);

        toast.setText("Akwaaba");
        toast.show();

        mAuth = FirebaseAuth.getInstance();

        User = mAuth.getCurrentUser();

        tvSignIn = findViewById(R.id.tvSignIn);
        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   if (tvSignIn.getText().toString().toLowerCase().contains("sign")) {
                if (mAuth.getCurrentUser() == null) {
                   // tvSignIn.setText("SIGN IN");
                    SignIn();
                }
                else{
                  //  if (User != null){
                        FirebaseAuth.getInstance().signOut();
                        tvSignIn.setBackgroundColor(getResources().getColor(R.color.colorGreen));
                       Toast.makeText(SubPHomeMainActivity.this, "You have been signed out : \n"+ User.getEmail(), Toast.LENGTH_SHORT).show();
                        tvSignIn.setText("SIGN IN");
                  //  }


                }

            }
        });


        deleteDuplicatelDownload();


        subscriptionStatePreference = this.getSharedPreferences("com.learnakantwi.twiguides", Context.MODE_PRIVATE);
        subscriptionState = subscriptionStatePreference.getBoolean("Paid", false);


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


        AppRate.with(this)
                .setInstallDays(0)
                .setLaunchTimes(3)
                .setRemindInterval(2)
                .monitor();

        AppRate.showRateDialogIfMeetsConditions(this);

        homeMainButtonArrayList = new ArrayList<>();
//  correct      homeListView = findViewById(R.id.homeListView);


        homeMainButtonArrayList.add(new HomeMainButton("Conversation", R.drawable.conversationimage));
        homeMainButtonArrayList.add(new HomeMainButton("Vocabulary", R.drawable.vocabularyimage));
        homeMainButtonArrayList.add(new HomeMainButton("Quiz", R.drawable.quizimage));
        homeMainButtonArrayList.add(new HomeMainButton("Proverbs", R.drawable.proverbsimage));
        homeMainButtonArrayList.add(new HomeMainButton("Children", R.drawable.childrenimage));
        homeMainButtonArrayList.add(new HomeMainButton("Reading", R.drawable.readingimage));
        homeMainButtonArrayList.add(new HomeMainButton("Manage Storage", R.drawable.ic_download_audio));


        HomeMainAdapter homeMainAdapter = new HomeMainAdapter(this, homeMainButtonArrayList);
        //  correct homeListView.setAdapter(homeMainAdapter);


        rvHomeMainAdapter = new RVHomeMainAdapter(this, homeMainButtonArrayList, this);
        // recyclerView.setLayoutManager(new LinearLayoutManager(this));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(rvHomeMainAdapter);




       /* homeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String me1 = homeMainButtonArrayList.get(position).getNameofActivity();


                switch (me1){
                    case "Reading":
                        goToReading();
                        return;
                    case "Manage Storage":
                        goToSettings();
                        return;
                    case "Conversation":
                        goToConversation();
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
                }
            }
        });*/

        childrenAnimalsArrayList = new ArrayList<>();
        if(5>2) {
        childrenAnimalsArrayList.add(new Animals("Bee", "Wowa", 1, R.drawable.wowa));
        childrenAnimalsArrayList.add(new Animals("Cat", "Ɔkra", 2, R.drawable.xkra));
        childrenAnimalsArrayList.add(new Animals("Cattle", "Nantwie", 3, R.drawable.nantwie));
        childrenAnimalsArrayList.add(new Animals("Cockroach", "Tɛfrɛ", 4, R.drawable.tqfrq));
        childrenAnimalsArrayList.add(new Animals("Crab", "Kɔtɔ", 5, R.drawable.kxtx));
        childrenAnimalsArrayList.add(new Animals("Crocodile", "Ɔdɛnkyɛm", 6, R.drawable.xdqnkyqm));
        childrenAnimalsArrayList.add(new Animals("Dog", "Kraman", 7, R.drawable.kraman));
        childrenAnimalsArrayList.add(new Animals("Donkey", "Afurum", 8, R.drawable.afurum));
        childrenAnimalsArrayList.add(new Animals("Duck", "Dabodabo", 9, R.drawable.dabodabo));
        childrenAnimalsArrayList.add(new Animals("Elephant", "Ɔsono", 10, R.drawable.xsono));
        childrenAnimalsArrayList.add(new Animals("Fowl", "Akokɔ", 11, R.drawable.akokx));
        childrenAnimalsArrayList.add(new Animals("Goat", "Apɔnkye", 12, R.drawable.apxnkye));
        childrenAnimalsArrayList.add(new Animals("Horse", "Pɔnkɔ", 13, R.drawable.pxnkx));
        childrenAnimalsArrayList.add(new Animals("Pig", "Prako", 14, R.drawable.prako));
        childrenAnimalsArrayList.add(new Animals("Lion", "Gyata", 15, R.drawable.gyata));
        childrenAnimalsArrayList.add(new Animals("Butterfly", "Afofantɔ", 16, R.drawable.afofantx));


        childrenAnimalsArrayList.add(new Animals("Chameleon", "Abosomakoterɛ", 17, R.drawable.abosomakoterq));
        childrenAnimalsArrayList.add(new Animals("Mouse", "Akura", 18, R.drawable.akura));
        childrenAnimalsArrayList.add(new Animals("Tortoise", "Akyekyedeɛ", 19, R.drawable.akyekyedeq));
        childrenAnimalsArrayList.add(new Animals("Bird", "Anomaa", 20, R.drawable.anomaa));
        childrenAnimalsArrayList.add(new Animals("Fish", "Apataa", 21, R.drawable.apataa));
        childrenAnimalsArrayList.add(new Animals("Spider", "Ananse", 22, R.drawable.ananse));
        childrenAnimalsArrayList.add(new Animals("Porcupine", "Kɔtɔkɔ", 23, R.drawable.kxtxkx));
        childrenAnimalsArrayList.add(new Animals("Bear", "Sisire", 24, R.drawable.sisire));
        childrenAnimalsArrayList.add(new Animals("Snake", "Ɔwɔ", 25, R.drawable.xwx));
        childrenAnimalsArrayList.add(new Animals("Camel", "Yoma", 26, R.drawable.yoma));
    }

        //myself
        //conversationArrayList.add(new subConversation("Wo din de sɛn?", "What is your name?") );
        //conversationArrayList.add(new subConversation("My name is Michael Kwaku Asante and all is care about is that I don't ever want to see you in my life. Take note it is not because I hate you but I love you and want us to be friends forever but I just cant stand what you are doing to me right now", "My name is Michael Kwaku Asante and all is care about is that I don't ever want to see you in my life. Take note it is not because I hate you but I love you and want us to be friends forever but I just cant stand what you are doing to me right now.") );
        if(5>2) {
        conversationArrayList.add(new subConversation("Me din de Michael Kwaku Asante", "My name is Michael Kwaku Asante"));
        conversationArrayList.add(new subConversation("Wubetumi afrɛ me Kwaku", "You can call me Kwaku"));
        conversationArrayList.add(new subConversation("Wo nso ɛ?", "What about you?"));
        conversationArrayList.add(new subConversation("Wo din de sɛn?", "What is your name?"));
        conversationArrayList.add(new subConversation("Yɛfrɛ wo sɛn?", "How are you called? (Lit.: How do they call you)"));
        conversationArrayList.add(new subConversation("Me fi Abuakwa", "I come from Abuakwa. (\"Come from\" as used here means my hometown)"));
        conversationArrayList.add(new subConversation("Wufi kurow bɛn so?", "What is your hometown? (Lit.: Which town do you come from)"));
        conversationArrayList.add(new subConversation("Me te Achimota", "I live at Achimota"));
        conversationArrayList.add(new subConversation("Wote he?", "Where do you live?"));
        conversationArrayList.add(new subConversation("Me kɔɔ ntoaso sukuu wɔ Achimota", "I attended Secondary School at Achimota"));
        conversationArrayList.add(new subConversation("Wokɔɔ ntoaso sukuu wɔ he?", "Where did you attend Secondary School?"));

        conversationArrayList.add(new subConversation("Me kɔ sukuu wɔ Legon Sukuupɔn mu", "I school at Legon University"));
        conversationArrayList.add(new subConversation("Wo kɔ sukuu wɔ he?", "Where do you attend school?"));
        conversationArrayList.add(new subConversation("Megyina gyinapɛn num", "I am in class five"));

        conversationArrayList.add(new subConversation("Madi mfe aduonu", "I am twenty years old"));

        conversationArrayList.add(new subConversation("Meyɛ tikyani", "I am a teacher"));
        conversationArrayList.add(new subConversation("Woyɛ adwuma bɛn?", "What's your occupation (Lit.: What work do you do?)"));
        conversationArrayList.add(new subConversation("Meyɛ okuani", "I am a farmer"));
        conversationArrayList.add(new subConversation("Meyɛ adwuma wɔ Ghana Education Service", "I work at Ghana Education Service"));


        conversationArrayList.add(new subConversation("Agorɔ a mepɛ paa ne \"basketball\" ", "My favorite game is basketball"));
        conversationArrayList.add(new subConversation("Agorɔ bɛn na w'ani gye ho pa ara?", "What game do you like most?"));
        conversationArrayList.add(new subConversation("M'ani gye nnwom ho pa ara", "I like music very much"));
        conversationArrayList.add(new subConversation("M'ani gye akenkan ho", "I like reading"));
        conversationArrayList.add(new subConversation("W'ani gye akenkan ho?", "Do you like reading?"));
        conversationArrayList.add(new subConversation("M'ani nnye akenkan ho", "I do not like reading"));
        conversationArrayList.add(new subConversation("M'ani gye sini ho?", "I like movies"));
        conversationArrayList.add(new subConversation("M'ani gye sinihwɛ ho pa ara", "I like watching movies very much"));
        conversationArrayList.add(new subConversation("Mempɛ sinihwɛ", "I don't like watching movies"));
        conversationArrayList.add(new subConversation("Ade a mempɛ koraa ne ntɔkwa", "What I don't like at all is fighting"));


        conversationArrayList.add(new subConversation("Aduane a m'ani gye ho pa ara ne fufuo ne nkate nkwan", "My favorite food is fufuo and groundnut soup"));
        conversationArrayList.add(new subConversation("Aduane a mepɛ pa ara ne banku", "The food I like most is banku"));
        conversationArrayList.add(new subConversation("Wopɛ fufuo?", "Do you like fufuo?"));
        conversationArrayList.add(new subConversation("Fufuo nyɛ me dɛ", "Fufu does not taste sweet to me"));
        conversationArrayList.add(new subConversation("Mempɛ fufuo", "I don't like fufuo"));
        conversationArrayList.add(new subConversation("Mepɛ fufuo", "I like fufuo"));
        conversationArrayList.add(new subConversation("Nkate nkwan yɛ dɛ", "Groundnut soup is sweet"));


        //family
        conversationArrayList.add(new subConversation("Wo aware anaa?", "Are you married?"));
        conversationArrayList.add(new subConversation("Menwaree", "I am not married"));
        conversationArrayList.add(new subConversation("Maware", "I am married"));
        conversationArrayList.add(new subConversation("Me yɛ sigyani", "I am single"));
        conversationArrayList.add(new subConversation("Meyɛ ɔbaa warefo", "I am a married woman"));
        conversationArrayList.add(new subConversation("Me yɛ ɔbarima warefo", "I am a married man"));
        conversationArrayList.add(new subConversation("Me kunu din de Kwame", "My husband's name is Kwame"));
        conversationArrayList.add(new subConversation("Yɛfrɛ me kunu Kwame", "My husband is called Kwame"));
        conversationArrayList.add(new subConversation("Me yere din de Abena", "My wife's name is Abena"));
        conversationArrayList.add(new subConversation("Yɛfrɛ me yere Abena", "My wife is called Abena"));
        conversationArrayList.add(new subConversation("Medɔ me yere ", "I love my wife"));
        conversationArrayList.add(new subConversation("Me yere ho yɛ fɛ", "My wife is beautiful"));
        conversationArrayList.add(new subConversation("Medɔ me kunu", "I love my husband"));
    }

        //Welcoming Others
        conversationWelcomingOthersArrayList = new ArrayList<>();
        if(5>2) {
        conversationWelcomingOthersArrayList.add(new subConversation("Agoo", "Knock Knock"));
        conversationWelcomingOthersArrayList.add(new subConversation("Amee", "Response to Knock Knock"));
        conversationWelcomingOthersArrayList.add(new subConversation("Bra mu", "Come in"));
        conversationWelcomingOthersArrayList.add(new subConversation("Akwaaba", "Welcome"));
        conversationWelcomingOthersArrayList.add(new subConversation("Yɛma wo akwaaba", "We welcome you"));
        conversationWelcomingOthersArrayList.add(new subConversation("Me pa wo kyɛw, akonnwa wɔ hɔ", "Please, there is a chair (1)"));
        conversationWelcomingOthersArrayList.add(new subConversation("Me pa wo kyɛw, adwa wɔ hɔ", "Please, there is a chair (2)"));
        conversationWelcomingOthersArrayList.add(new subConversation("Memma wo nsuo?", "Should I give you water?"));
        conversationWelcomingOthersArrayList.add(new subConversation("Me nya nsuo a anka m'ani begye paa", "I would be very grateful if I get water"));
        conversationWelcomingOthersArrayList.add(new subConversation("Me nom bi nkyɛe medaase", "I drank some not long ago, thank you"));
        conversationWelcomingOthersArrayList.add(new subConversation("Ɛyɛ medaase", "It's alright, thank you"));
        conversationWelcomingOthersArrayList.add(new subConversation("Mesrɛ wo, dɛn na wobɛnom?", "Please what will you drink?"));
        conversationWelcomingOthersArrayList.add(new subConversation("Dɛn na wobɛnom?", "What will you drink?"));
        conversationWelcomingOthersArrayList.add(new subConversation("Ɛkwan so ɛ?", "How was your journey?"));
        conversationWelcomingOthersArrayList.add(new subConversation("Yoo, medaase menkura no bɔne", "Okay, thank you. I come in peace"));
        conversationWelcomingOthersArrayList.add(new subConversation("Akonnwa wɔ hɔ", "There is a chair"));
        conversationWelcomingOthersArrayList.add(new subConversation("Ɛha bɔkɔɔ", "There is peace here"));
        conversationWelcomingOthersArrayList.add(new subConversation("Ɛkwan so bɔkɔɔ", "My journey was smooth"));
        conversationWelcomingOthersArrayList.add(new subConversation("Ɛha bɔkɔɔ, wo na wonam", "There is peace here, you are the traveller"));

        //conversationWelcomingOthersArrayList.add(new subConversation("Me", "There is a chair") );
    }
        ///Apologies
        conversationsApologiesArrayList = new ArrayList<>();
        if(5>2) {
            conversationsApologiesArrayList.add(new subConversation("Mepa wo kyɛw", "Please"));
            conversationsApologiesArrayList.add(new subConversation("Mesrɛ wo", "I beg you"));
            conversationsApologiesArrayList.add(new subConversation("Manu me ho", "I have regretted"));
            conversationsApologiesArrayList.add(new subConversation("Fa kyɛ me", "Forgive me"));
            conversationsApologiesArrayList.add(new subConversation("Kosɛ", "Sorry"));
            conversationsApologiesArrayList.add(new subConversation("Kafra", "Sorry"));
            conversationsApologiesArrayList.add(new subConversation("Na ɛsɛ sɛ mefrɛ wo", "I should have called you"));
            conversationsApologiesArrayList.add(new subConversation("Mekoto srɛ wo", "I kneel before you in apology"));
            conversationsApologiesArrayList.add(new subConversation("Ɛyɛ me ara me mfomsoɔ", "It is solely my fault"));
            conversationsApologiesArrayList.add(new subConversation("Me mfomsoɔ", "My mistake"));
            conversationsApologiesArrayList.add(new subConversation("Mafom wo", "I have wronged you"));
            conversationsApologiesArrayList.add(new subConversation("Mayɛ wo bɔne", "I have sinned against you"));
            conversationsApologiesArrayList.add(new subConversation("Wodi bem", "You are right"));
            conversationsApologiesArrayList.add(new subConversation("Medi fɔ", "I am wrong"));
            conversationsApologiesArrayList.add(new subConversation("Manhyɛ da", "It wasn't deliberate"));
            conversationsApologiesArrayList.add(new subConversation("M'ani awu", "I'm ashamed"));
            conversationsApologiesArrayList.add(new subConversation("Ɛnsi bio", "It won't happen again"));
            conversationsApologiesArrayList.add(new subConversation("Menyɛ saa bio", "I won't do that again"));
            conversationsApologiesArrayList.add(new subConversation("Ɛyɛ", "It's okay"));
            conversationsApologiesArrayList.add(new subConversation("Ɛnyɛ hwee", "It is nothing. It's alright"));
            // conversationsApologiesArrayList.add(new subConversation("Ɛsi","It happens"));
            conversationsApologiesArrayList.add(new subConversation("Esi daa", "It happens all the time"));
            conversationsApologiesArrayList.add(new subConversation("Ɛtaa si", "It always happens"));
            conversationsApologiesArrayList.add(new subConversation("Ɛnha wo ho", "Don't worry yourself"));
            conversationsApologiesArrayList.add(new subConversation("Ɛnnwene ho", "Don't think about it. Don't worry"));
            conversationsApologiesArrayList.add(new subConversation("Ɛnnwene ho koraa", "Don't worry about it at all"));
            conversationsApologiesArrayList.add(new subConversation("Mede akyɛ wo", "I have forgiven you. I forgive you"));
            conversationsApologiesArrayList.add(new subConversation("Me mfa nkyɛ wo", "I won't forgive you"));
            conversationsApologiesArrayList.add(new subConversation("Ɛnyɛ saa bio", "Don't do that again"));
            conversationsApologiesArrayList.add(new subConversation("Magyae ama aka", "I've left it to rest. I won't pursue any further"));
            conversationsApologiesArrayList.add(new subConversation("Ɛho nhia", "It's not important"));
            conversationsApologiesArrayList.add(new subConversation("Magye wo kyɛwpa no atom", "I have accepted your apology"));
            conversationsApologiesArrayList.add(new subConversation("Mennim nea ɛbaa me so", "I don't know what came over me"));
            conversationsApologiesArrayList.add(new subConversation("Menhu nea menka mpo", "I don't even know what to say"));
            conversationsApologiesArrayList.add(new subConversation("Masua me nyansa", "I have learnt my lesson. I have learnt wisdom"));
            conversationsApologiesArrayList.add(new subConversation("Ɛnyɛ saa na mete o", "That's not how I am o"));
            conversationsApologiesArrayList.add(new subConversation("Manka no yie, fa kyɛ me", "I didn't say it well. Forgive me"));
            // conversationsApologiesArrayList.add(new subConversation("",""));
        }

        /// Asking and Giving Directions
        conversationDirections = new ArrayList<>();
        if(5>2) {
            conversationDirections.add(new subConversation("Meyɛ ɔhɔhoɔ", "I'm a stranger (1)"));
            conversationDirections.add(new subConversation("Meyɛ ɔhɔhoɔ", "I'm a guest (2)"));
            conversationDirections.add(new subConversation("Ɛhe na bɔs \"stop\" no wɔ?", "Where is the bus stop"));
            conversationDirections.add(new subConversation("Ɛhe na menya kar akɔ Kumasi", "Where can I board a car to Kumasi"));
            conversationDirections.add(new subConversation("Ɛhe na ayaresabea no wɔ?", "Where is the hospital located?"));
            conversationDirections.add(new subConversation("Merehwehwɛ ayaresabea", "I'm looking for a hospital"));
            conversationDirections.add(new subConversation("Merehwehwɛ...", "I'm looking for..."));
            conversationDirections.add(new subConversation("Merekɔ mpoano", "I'm going to the beach"));
            conversationDirections.add(new subConversation("Ɛkwan bɛn na ɛkɔ mpoano hɔ?", "Which road(way) leads to the beach?"));
            conversationDirections.add(new subConversation("Ɛhe na keteke gyinabea wɔ?", "Where is the train station?"));
            conversationDirections.add(new subConversation("Kyerɛ me kwan no", "Show me the way"));
            conversationDirections.add(new subConversation("Me mfa he?", "Where should I pass?"));
            conversationDirections.add(new subConversation("Fa Benkum", "Take left"));
            conversationDirections.add(new subConversation("Fa nifa", "Take Right"));
            conversationDirections.add(new subConversation("Dan wo ho na fa nifa", "Turn and take Right"));
            conversationDirections.add(new subConversation("Kɔ w'anim", "Go straight"));
            conversationDirections.add(new subConversation("Kɔ w'akyi", "Go back"));
            conversationDirections.add(new subConversation("Fa dua no nkyɛn", "Pass by the tree"));
            conversationDirections.add(new subConversation("Wadu Accra", "You have arrived at Accra"));
            conversationDirections.add(new subConversation("Aka kakra na wadu hɔ", "You will get there in a short period of time"));
            conversationDirections.add(new subConversation("Hwɛ w'akyi", "Look back"));
            conversationDirections.add(new subConversation("Ɛha yɛ he?", "Which place is this?"));
            conversationDirections.add(new subConversation("Kyerɛ me kwan kɔ Achimota", "Show me the way(give me directions) to Achimota"));
            conversationDirections.add(new subConversation("Ɛkwan no wa", "It(the road) is far from here"));
            conversationDirections.add(new subConversation("Ɛkwan no wa anaa?", "Is it far from here?"));
            conversationDirections.add(new subConversation("Wo ntumi nnante nkɔ", "You cannot walk to the place"));
            conversationDirections.add(new subConversation("Gye sɛ wofa kar", "Unless you use a car"));
            conversationDirections.add(new subConversation("Di m'akyi", "Follow me"));
            conversationDirections.add(new subConversation("Di ahyɛnsode no akyi", "Follow the signs"));
            conversationDirections.add(new subConversation("Bisa", "Ask"));
            conversationDirections.add(new subConversation("Wo duru hɔ a, bisa obiara", "When you reach there ask anyone"));
            conversationDirections.add(new subConversation("Me nnim hɔ", "I don't know the place"));
            conversationDirections.add(new subConversation("Me nim hɔ", "I know the place"));
            conversationDirections.add(new subConversation("Ɛhe na wopɛ sɛ wokɔ?", "Where do you want to go?"));
            conversationDirections.add(new subConversation("Fa me kɔ wimhyɛn gyinabea", "Take me to the airport"));
            conversationDirections.add(new subConversation("Mepɛ sɛ mekɔ fie", "I want to go home"));
            conversationDirections.add(new subConversation("Ɛhe na wote?", "Where do you stay?"));
            conversationDirections.add(new subConversation("Mete Dansoman", "I stay at Dansoman"));
        }

        conversationHospital = new ArrayList<>();
        if (5>2){
           // conversationHospital.add(new subConversation("", ""));
            conversationHospital.add(new subConversation("Me pɛ sɛ mehu dɔkota", "I want to see the doctor"));
            conversationHospital.add(new subConversation("Me ho ayɛ hye", "My temperature is high"));
            conversationHospital.add(new subConversation("Me yam yɛ me ya", "My stomach pains me"));
            conversationHospital.add(new subConversation("Awɔw de me", "I am feeling cold"));
            conversationHospital.add(new subConversation("Wo he na ɛyɛ wo ya?", "Where do you feel pains?"));
            conversationHospital.add(new subConversation("Meyare", "I am sick"));
            conversationHospital.add(new subConversation("Wo nipadua he na ɛyɛ wo ya", "Which part of your body is paining you"));
            conversationHospital.add(new subConversation("Me nan yɛ me ya", "My leg pains me"));
            conversationHospital.add(new subConversation("Me ti yɛ me ya", "My head pains me"));
            conversationHospital.add(new subConversation("Me nsa yɛ me ya", "My hand pains me"));
            conversationHospital.add(new subConversation("Pɔmpɔ abɔ me mmɔtoam", "I have lumps in my armpit"));
            conversationHospital.add(new subConversation("Me yam atim", "I am constipated"));
            conversationHospital.add(new subConversation("Me nsa ahono", "My hand is swollen"));
            conversationHospital.add(new subConversation("Me nan ahono", "My leg is swollen"));
            conversationHospital.add(new subConversation("Me ho keka me", "My body itches"));
            conversationHospital.add(new subConversation("M'afe saa", "I've been vomiting many times"));
            conversationHospital.add(new subConversation("Me yam rehwie", "I have diarrhea"));
            conversationHospital.add(new subConversation("Mogya retu me", "I'm bleeding"));
            conversationHospital.add(new subConversation("M'anom ato", "I have lost appetite"));
            conversationHospital.add(new subConversation("Me ntumi nnidi", "I'm unable to eat"));
            conversationHospital.add(new subConversation("Me ntumi nna", "I am unable to sleep"));
            conversationHospital.add(new subConversation("Kɔ wɔ paneɛ", "Go for an injection"));
            conversationHospital.add(new subConversation("Kɔ gye aduro", "Go for medicine"));
            conversationHospital.add(new subConversation("Me bɔ wa", "I cough"));
            conversationHospital.add(new subConversation("Me bɔ wa saa", "I cough repeatedly"));
            conversationHospital.add(new subConversation("Mentumi nhome yiye", "I can't breath well"));
            conversationHospital.add(new subConversation("Mehome a ɛnsi so", "I have difficulty breathing"));
            conversationHospital.add(new subConversation("Mepɛ sɛ moyɛ me lab test", "I want a lab test"));
            conversationHospital.add(new subConversation("Yɛbɛtwe wo mogya kakra", "We will take your blood sample"));
            conversationHospital.add(new subConversation("Yɛbɛyɛ wo mogya mu nhwehwɛmu", "We will test your blood"));
            conversationHospital.add(new subConversation("Yɛbɛyɛ wo tiafi mu nhwehwɛmu", "We will examine your stool (toilet)"));
            conversationHospital.add(new subConversation("Yɛbɛyɛ wo dwonsɔ mu nhwehwɛmu", "We will examine your urine"));
            conversationHospital.add(new subConversation("Ɛyɛ atiridii", "It is malaria"));
            conversationHospital.add(new subConversation("Wo nyem", "You are pregnant"));
            conversationHospital.add(new subConversation("Yɛbɛgye wo ato ha kakra", "You will be admitted here for a while"));
            conversationHospital.add(new subConversation("Yɛbɛyɛ wo oprehyɛn", "We will perform surgery"));
            conversationHospital.add(new subConversation("Nom aduro no ansa na wada", "Take the medicine before you sleep"));
            conversationHospital.add(new subConversation("Didi ansa na w'anom aduro no", "Eat before you take the medicine"));
            conversationHospital.add(new subConversation("Fa no mprɛnsa da biara", "Take it three times each day"));
            conversationHospital.add(new subConversation("Nɛɛse no bɛhwɛ wo", "The nurse will take care of you"));
            //conversationHospital.add(new subConversation("Mentumi nhome yiye", "I can't breath well"));
            conversationHospital.add(new subConversation("Wo ho bɛtɔ wo", "You will get well"));

        }

        conversationPhone = new ArrayList<>();
        if (5>2){
            // conversationPhone.add(new subConversation("", ""));

            conversationPhone.add(new subConversation("Hɛloo", "Hello"));
            conversationPhone.add(new subConversation("Wote me nka?", "Can you hear me"));
            conversationPhone.add(new subConversation("Kwame nie", "This is Kwame"));
            conversationPhone.add(new subConversation("Aane, Kwame nie", "Yes, this is Kwame"));
            conversationPhone.add(new subConversation("Dabi, Kwadwo nie", "No, this is Kwadwo"));
            conversationPhone.add(new subConversation("Kwame ba a, ka kyerɛ no sɛ mefrɛe", "If Kwame comes tell him that I called"));
            conversationPhone.add(new subConversation("Kwame ba a, ma no mfrɛ me", "If Kwame comes let him call me"));
            conversationPhone.add(new subConversation("Ka kyerɛ no sɛ mefrɛe", "Tell him/her that I called"));




            conversationPhone.add(new subConversation("Mɛtumi ne Kwabena akasa?", "Can I speak with Kwabena?"));
            conversationPhone.add(new subConversation("Merehwehwɛ Abena", "I'm looking for Abena"));
            conversationPhone.add(new subConversation("Abena na ɛrekasa yi", "This is Abena speaking"));

            conversationPhone.add(new subConversation("Abena wɔ hɔ?", "Is Abena there?"));
            conversationPhone.add(new subConversation("Mesrɛ wo, wobetumi afrɛ Akua ama me?", "Please, can you call Akua for me?"));
            conversationPhone.add(new subConversation("Mesrɛ wo, wofrɛɛ me?", "Please, did you call me?"));
            conversationPhone.add(new subConversation("Wofrɛe", "You called"));
            conversationPhone.add(new subConversation("Wofrɛe anaa?", "Did you call?"));
            conversationPhone.add(new subConversation("Wofrɛɛ me ɛnnora anaa?", "Did you call me yesterday?"));
            conversationPhone.add(new subConversation("Gyina so", "Hold on"));
            conversationPhone.add(new subConversation("Gyina so kakra", "Hold on for a short while"));
            conversationPhone.add(new subConversation("Gyina so simma", "Hang on a minute"));
            conversationPhone.add(new subConversation("Magyina so akyɛ dodo", "I've held on for too long"));
            conversationPhone.add(new subConversation("Frɛ me anwummere", "Call me in the evening"));
            conversationPhone.add(new subConversation("Frɛ me anɔpa", "Call me in the morning"));
            conversationPhone.add(new subConversation("Ɛmfrɛ me", "Don't call me"));
            conversationPhone.add(new subConversation("Ɛmfrɛ me bio", "Don't call me again"));
            conversationPhone.add(new subConversation("Ɛmfrɛ me anwummere bio", "Don't call me in the evening again"));
            conversationPhone.add(new subConversation("Adeɛ asa", "It's late"));
            conversationPhone.add(new subConversation("Mefrɛ wo a wo mfa", "You don't answer my calls"));
            conversationPhone.add(new subConversation("Adɛn nti na mefrɛe woamfa?", "Why didn't you pick up when I called?"));
            conversationPhone.add(new subConversation("Wofrɛe anaa?", "Did you call?"));

            conversationPhone.add(new subConversation("Ahoma no retwita", "The line is breaking"));
            conversationPhone.add(new subConversation("Gyina yie", "Position your self well"));
            conversationPhone.add(new subConversation("Medaase sɛ wofrɛe", "Thanks for calling"));

            conversationPhone.add(new subConversation("Yɛbɛkasa akyiri", "We will talk later"));

            conversationPhone.add(new subConversation("Mɛfrɛ wo ɔkyena", "I will call you tomorrow"));


            conversationPhone.add(new subConversation("M'ani agye sɛ wo afrɛ me", "I am happy that you have called"));
            conversationPhone.add(new subConversation("Bye byee", "Bye bye"));
           /* conversationPhone.add(new subConversation("", ""));
            conversationPhone.add(new subConversation("", ""));
            conversationPhone.add(new subConversation("", ""));
            conversationPhone.add(new subConversation("", ""));*/




        }
        //conversationPhone = new ArrayList<>();


    }

    @Override
    protected void onStart() {
        if (mAuth.getCurrentUser()!=null){
            currentUser = mAuth.getCurrentUser().getEmail();
            //currentUser =  User.getUid();
            //String token = mAuth.getAccessToken();
            User = mAuth.getCurrentUser();
            SpannableStringBuilder ssb = new SpannableStringBuilder("Akwaaba");
            ForegroundColorSpan fcsGreen = new ForegroundColorSpan(Color.GREEN);
            ssb.setSpan(fcsGreen, 0,ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssb.append(": ").append(currentUser);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(ssb).append(": ").append(currentUser);
           // tvSignIn.setText(stringBuilder);
            tvSignIn.setText(ssb);
            tvSignIn.setBackgroundColor(Color.WHITE);

         /*   User.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SubPHomeMainActivity.this, "Email sent.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });*/


            //  mAuth.
        }
        super.onStart();
    }

    @Override
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
    }

   /* TextView textView = findViewById(R.id.text_view);
    String text = "I want THIS and THIS to be colored";
    SpannableString ss = new SpannableString(text);
    SpannableStringBuilder ssb = new SpannableStringBuilder(text);
    ForegroundColorSpan fcsRed = new ForegroundColorSpan(Color.RED);
    ForegroundColorSpan fcsGreen = new ForegroundColorSpan(Color.GREEN);
    BackgroundColorSpan bcsYellow = new BackgroundColorSpan(Color.YELLOW);
        ssb.setSpan(fcsRed, 7, 11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(fcsGreen, 16, 20, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(bcsYellow, 27, 34, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.append(" and this to be appended");
        textView.setText(ssb);
*/
}



