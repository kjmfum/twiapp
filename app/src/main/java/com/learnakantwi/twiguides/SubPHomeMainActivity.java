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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import hotchemi.android.rate.AppRate;

import static android.Manifest.permission.INTERNET;
import static com.learnakantwi.twiguides.SubChildrenAnimals.childrenAnimalsArrayList;
import static com.learnakantwi.twiguides.SubConversationIntroductionActivity.conversationArrayList;

//import android.support.v7.app.AppCompatActivity;

public class SubPHomeMainActivity extends AppCompatActivity implements PurchasesUpdatedListener {
    //  app:adUnitId="ca-app-pub-6999427576830667~6251296006"ˆ

    static ArrayList<HomeMainButton> homeMainButtonArrayList;
    public InterstitialAd mInterstitialAd;
    BillingClient billingClient;
    String premiumUpgradePrice;
    Button buyButton;
    Toast toast;
    ListView homeListView;
    AdView mAdView;
    MediaPlayer mediaPlayer;
    SharedPreferences subscriptionStatePreference;
    boolean subscriptionState;

    TextView tvSignIn;
    FirebaseAuth mAuth;
    String currentUser;


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
                final ArrayList<HomeMainButton> results = new ArrayList<>();
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
        Intent intent = new Intent(getApplicationContext(), SubConversationIntroductionActivity.class);
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

    public void advert() {


        final SharedPreferences sharedPreferences = this.getSharedPreferences("com.learnakantwi.twiguides", Context.MODE_PRIVATE);
      //  sharedPreferences.edit().putString("AdvertPreference", "No").apply();
        String advertPreference = sharedPreferences.getString("AdvertPreference", "No");


        assert advertPreference != null;
        if (!advertPreference.equals("Yes")) {
            new AlertDialog.Builder(this)
                    .setIcon(R.drawable.learnakantwiimage)
                    .setTitle("We need your support")
                    .setMessage("Would You Like To View An Advert To Support Us?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sharedPreferences.edit().putString("AdvertPreference", "Yes").apply();
                            if (mInterstitialAd.isLoaded()) {
                                mInterstitialAd.show();
                            } else {
                                Log.d("TAG", "The interstitial wasn't loaded yet.");
                            }
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    sharedPreferences.edit().putString("AdvertPreference", "No").apply();
                                }
                            }
                    )
                    .show();
        } else {
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();

            } else {
                Log.d("TAG", "The interstitial wasn't loaded yet.");

            }
        }

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }

   /* public void checkPermission(String permission, int requestCode)
    {
        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(
                Home.this,
                permission)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat
                    .requestPermissions(
                            Home.this,
                            new String[] {permission },
                            100);
        }
        else {
            Toast
                    .makeText(Home.this,
                            "Permission already granted",
                            Toast.LENGTH_SHORT)
                    .show();
        }
    }*/
//In your case, you do not need the LinearLayout and ImageView at all. Just add android:drawableLeft="@drawable/up_count_big" to your TextView.

    public void buyMe() {
        setUpBillingClient();
    }

        /*BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                .setSkuDetails(skuDetails)
                .build();
        BillingResult responseCode = billingClient.launchBillingFlow(InAppActivity.this, flowParams);
       // int responseCode = billingClient.launchBillingFlow(flowParams);
    }*/

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
                                                        toast.setText(me1);
                                                        toast.show();

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

    public void deleteDuplicatelDownload(){

        try {
            deleteDuplicateVocabulary();
            deleteDuplicateConversation();
            deleteDuplicateProverbs();
        }
        catch (NullPointerException e){
            System.out.println("Error Null");
        }
        catch(Exception any){
            System.out.println("Strange Exception Caught");
        }



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



    public void SignIn(View view){
        Intent homeIntent = new Intent(getApplicationContext(), SignInActivity.class);
        startActivity(homeIntent);
    }

    public void SignIn(){
        Intent homeIntent = new Intent(getApplicationContext(), SignInActivity.class);
        startActivity(homeIntent);
    }

   /* public void Transition(View v){
        Intent homeIntent = new Intent(getApplicationContext(), HomeMainActivity.class);
        startActivity(homeIntent);
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subpactivity_home1);

        toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);

        toast.setText("Akwaaba");
        toast.show();

        mAuth = FirebaseAuth.getInstance();

        tvSignIn = findViewById(R.id.tvSignIn);
        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvSignIn.getText().toString().toLowerCase().contains("sign")){
                    SignIn();
                }

            }
        });



        deleteDuplicatelDownload();


        subscriptionStatePreference = this.getSharedPreferences("com.learnakantwi.twiguides", Context.MODE_PRIVATE);
        subscriptionState = subscriptionStatePreference.getBoolean("Paid",false);


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
        homeListView = findViewById(R.id.homeListView);


        homeMainButtonArrayList.add(new HomeMainButton("Manage Storage", R.drawable.ic_download_audio));
        homeMainButtonArrayList.add(new HomeMainButton("Conversation", R.drawable.conversationimage));
        homeMainButtonArrayList.add(new HomeMainButton("Vocabulary", R.drawable.vocabularyimage));
        homeMainButtonArrayList.add(new HomeMainButton("Quiz", R.drawable.quizimage));
        homeMainButtonArrayList.add(new HomeMainButton("Proverbs", R.drawable.proverbsimage));
        homeMainButtonArrayList.add(new HomeMainButton("Children", R.drawable.childrenimage));
        homeMainButtonArrayList.add(new HomeMainButton("Reading", R.drawable.readingimage));


        HomeMainAdapter homeMainAdapter = new HomeMainAdapter(this, homeMainButtonArrayList);
        homeListView.setAdapter(homeMainAdapter);




        homeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        });

        childrenAnimalsArrayList = new ArrayList<>();

        childrenAnimalsArrayList.add(new Animals("Bee", "Wowa"));
        childrenAnimalsArrayList.add(new Animals("Cat", "Ɔkra"));
        childrenAnimalsArrayList.add(new Animals("Cattle", "Nantwie"));
        childrenAnimalsArrayList.add(new Animals("Cockroach", "Tɛfrɛ"));
        childrenAnimalsArrayList.add(new Animals("Crab", "Kɔtɔ"));
        childrenAnimalsArrayList.add(new Animals("Crocodile", "Ɔdɛnkyɛm"));
        childrenAnimalsArrayList.add(new Animals("Dog", "Kraman"));
        childrenAnimalsArrayList.add(new Animals("Donkey", "Afurum"));
        childrenAnimalsArrayList.add(new Animals("Duck", "Dabodabo"));
        childrenAnimalsArrayList.add(new Animals("Elephant", "Ɔsono"));
        childrenAnimalsArrayList.add(new Animals("Fowl", "Akokɔ"));
        childrenAnimalsArrayList.add(new Animals("Goat", "Apɔnkye"));
        childrenAnimalsArrayList.add(new Animals("Horse", "Pɔnkɔ"));
        childrenAnimalsArrayList.add(new Animals("Pig", "Prako"));
        childrenAnimalsArrayList.add(new Animals("Lion", "Gyata"));
        childrenAnimalsArrayList.add(new Animals("Butterfly", "Afofantɔ"));


        //myself
        //conversationArrayList.add(new subConversation("Wo din de sɛn?", "What is your name?") );
        //conversationArrayList.add(new subConversation("My name is Michael Kwaku Asante and all is care about is that I don't ever want to see you in my life. Take note it is not because I hate you but I love you and want us to be friends forever but I just cant stand what you are doing to me right now", "My name is Michael Kwaku Asante and all is care about is that I don't ever want to see you in my life. Take note it is not because I hate you but I love you and want us to be friends forever but I just cant stand what you are doing to me right now.") );
        conversationArrayList.add(new subConversation("Me din de Michael Kwaku Asante", "My name is Michael Kwaku Asante") );
        conversationArrayList.add(new subConversation("Wubetumi afrɛ me Kwaku", "You can call me Kwaku") );
        conversationArrayList.add(new subConversation("Wo nso ɛ?", "What about you?") );
        conversationArrayList.add(new subConversation("Wo din de sɛn?", "What is your name?") );
        conversationArrayList.add(new subConversation("Yɛfrɛ wo sɛn?", "How are you called? (Lit.: How do they call you") );
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
        conversationArrayList.add(new subConversation("M'ani gye nnwom ho pa ara", "I like music very much") );
        conversationArrayList.add(new subConversation("M'ani gye akenkan ho", "I like reading") );
        conversationArrayList.add(new subConversation("W'ani gye akenkan ho?", "Do you like reading?") );
        conversationArrayList.add(new subConversation("M'ani nnye akenkan ho", "I do not like reading") );
        conversationArrayList.add(new subConversation("M'ani gye sini ho?", "I like movies") );
        conversationArrayList.add(new subConversation("M'ani gye sinihwɛ ho pa ara", "I like watching movies very much") );
        conversationArrayList.add(new subConversation("Mempɛ sinihwɛ", "I don't like watching movies") );
        conversationArrayList.add(new subConversation("Ade a mempɛ koraa ne ntɔkwa", "What I don't like at all is fighting") );





        conversationArrayList.add(new subConversation("Aduane a m\'ani gye ho pa ara ne fufuo ne nkate nkwan", "My favorite food is fufuo and groundnut soup") );
        conversationArrayList.add(new subConversation("Aduane a mepɛ pa ara ne banku", "The food I like most is banku") );
        conversationArrayList.add(new subConversation("Wopɛ fufuo?", "Do you like fufuo?") );
        conversationArrayList.add(new subConversation("Fufuo nyɛ me dɛ", "Fufu does not taste sweet to me") );
        conversationArrayList.add(new subConversation("Mempɛ fufuo", "I don't like fufuo") );
        conversationArrayList.add(new subConversation("Mepɛ fufuo", "I like fufuo") );
        conversationArrayList.add(new subConversation("Nkate nkwan yɛ dɛ", "Groundnut soup is sweet") );


        //family
        conversationArrayList.add(new subConversation("Wo aware anaa?", "Are you married?") );
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
        conversationArrayList.add(new subConversation("Medɔ me kunu", "I love my husband") );

    }

    @Override
    protected void onStart() {
        if (mAuth.getCurrentUser()!=null){
            currentUser = mAuth.getCurrentUser().getEmail();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Akwaaba: ").append(currentUser);
            tvSignIn.setText(stringBuilder);
            tvSignIn.setBackgroundColor(Color.WHITE);
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




}



