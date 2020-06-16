package com.learnakantwi.twiguides;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchaseHistoryRecord;
import com.android.billingclient.api.PurchaseHistoryResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;

import java.util.ArrayList;
import java.util.List;

import static com.learnakantwi.twiguides.ProverbsActivity.proverbsArrayList;


public class MainActivity extends AppCompatActivity implements PurchasesUpdatedListener, PurchaseHistoryResponseListener {

    private NotificationManagerCompat notificationManager;
    private EditText editTextTitle;
    private EditText editTextMessage;
    BillingClient billingClient;
    Toast toast;
    String premiumUpgradePrice;
    SharedPreferences sharedPreferencesAds;

    //String AdUnitInterstitialID = getString(R.string.AdUnitIDInterstitial);



    int SPLASH_TIME_OUT = 3000;
    int times =0;
    static int Lifetime;
    static  int Subscribed;

    public void goToAll() {
        Intent intent = new Intent(getApplicationContext(), AllActivity.class);
        startActivity(intent);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    @Override
    public void onPurchaseHistoryResponse(BillingResult billingResult, List<PurchaseHistoryRecord> list) {

    }
    void handlePurchase(Purchase purchase) {
        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged()) {
                AcknowledgePurchaseParams acknowledgePurchaseParams =
                        AcknowledgePurchaseParams.newBuilder()
                                .setPurchaseToken(purchase.getPurchaseToken())
                                .build();
                AcknowledgePurchaseResponseListener acknowledgePurchaseResponseListener = new AcknowledgePurchaseResponseListener() {
                    @Override
                    public void onAcknowledgePurchaseResponse(BillingResult billingResult) {

                        toast.setText("Acknowleged");
                        toast.show();

                        /*Intent intent = new Intent(getApplicationContext(), SubPHomeMainActivity.class);
                        startActivity(intent);*/
                    }

                };


                billingClient.acknowledgePurchase(acknowledgePurchaseParams, acknowledgePurchaseResponseListener);
                Intent intent = new Intent(getApplicationContext(), SubPHomeMainActivity.class);
                startActivity(intent);

            }
        }
    }

    @Override
    public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> purchases) {

        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK
                && purchases != null) {
            for (Purchase purchase : purchases) {
                handlePurchase(purchase);
            }
        } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
            // Handle an error caused by a user cancelling the purchase flow.
            Toast.makeText(this, "You cancelled the Purchase", Toast.LENGTH_SHORT).show();
            billingClient.endConnection();}
        else if(billingResult.getResponseCode()== BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED){
            Intent homeIntent = new Intent(getApplicationContext(), SubPHomeMainActivity.class);
            startActivity(homeIntent);
            finish();
            Toast.makeText(this, "Already Purchased", Toast.LENGTH_SHORT).show();
        } else {
            // Handle any other error codes.
            Toast.makeText(this,"Could not complete purchase", Toast.LENGTH_LONG).show();
        }


    }

    public void setUpBillingClient() {

        billingClient = BillingClient.newBuilder(this)
                .setListener(this)
                .enablePendingPurchases()
                .build();
        //setUpBillingInApp();
        setUpBilling();
    }

    public void setUpBilling(){

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {

        List<String> skuList1 = new ArrayList<>();

        //skuList1.add("likoio");
        //skuList1.add("lifetime_full_access");
        skuList1.add("lifetime_full_access1");
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList1).setType(BillingClient.SkuType.INAPP);



////////OneTimeProducts
            billingClient.querySkuDetailsAsync(params.build(),
                    new SkuDetailsResponseListener() {
                        @Override
                        public void onSkuDetailsResponse(BillingResult billingResult, List<SkuDetails> skuDetailsList) {
                            // Process the result.
                            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
                                toast.setText("Already Purchased");
                                toast.show();

                            } else{
                                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && skuDetailsList != null) {
                                    for (SkuDetails skuDetails : skuDetailsList) {
                                        String sku = skuDetails.getSku();
                                        String price = skuDetails.getPrice();
                                        //
                                        if ("lifetime_full_access1".equals(sku)) {
                                       //     if ("likoio".equals(sku)) {
                                            premiumUpgradePrice = price;
                                            Purchase.PurchasesResult purchasesResult = billingClient.queryPurchases(BillingClient.SkuType.INAPP);
                                            List<Purchase> purchasesList = purchasesResult.getPurchasesList();
                                            //
                                           // Toast.makeText(MainActivity.this, "Hi2 "+ purchasesList.size(), Toast.LENGTH_SHORT).show();

                                          if (purchasesList !=null && !purchasesList.isEmpty()){
                                          // if (purchasesList !=null && purchasesList.size()>1){
                                               //Toast.makeText(MainActivity.this, "Hi "+ sku +" "+ purchasesList.size(), Toast.LENGTH_SHORT).show();
                                                for (Purchase purchase : purchasesList) {
                                                    String skuName = purchase.getSku();
                                                   // if (Lifetime == 6) {

                                                       if (skuName.equals("lifetime_full_access1")) {
                                                       //     if (skuName.equals("likoio")) {

                                                            sharedPreferencesAds = getSharedPreferences("AdsDecision", MODE_PRIVATE);
                                                            SharedPreferences.Editor editor = sharedPreferencesAds.edit();
                                                            editor.putInt("Lifetime", 1);
                                                            editor.putInt("Sub", 1);
                                                            editor.apply();
                                                            Lifetime = sharedPreferencesAds.getInt("Lifetime", 5);
                                                           Subscribed = sharedPreferencesAds.getInt("Sub", 5);
                                                            Intent homeIntent = new Intent(getApplicationContext(), SubPHomeMainActivity.class);
                                                           //Intent homeIntent = new Intent(getApplicationContext(), HomeMainActivity.class);
                                                            //Intent homeIntent = new Intent(getApplicationContext(), InAppActivity.class);
                                                            startActivity(homeIntent);
                                                        } else {
                                                           Lifetime=4;
                                                           //Toast.makeText(MainActivity.this, "I don't have access5: "+skuName, Toast.LENGTH_SHORT).show();
                                                        }
                                                   // }
                                                }
                                                //toast.setText(Integer.toString(me3));
                                            }/*else {
                                                Toast.makeText(MainActivity.this, "Hi "+Lifetime, Toast.LENGTH_SHORT).show();
                                            }*/

                                        }
                                    }
                                }

                            }

                        }
                    });



        ///////


   /*     billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {*/
                    // The BillingClient is ready. You can query purchases here.
                    //

////////Subscriptions
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(!(Lifetime==1)) {

                               // Toast.makeText(MainActivity.this, "Inside Here 3: "+ Lifetime , Toast.LENGTH_SHORT).show();
                                List<String> skuList = new ArrayList<>();
                                skuList.add("reading_club");
                                skuList.add("monthly_subscription");
                                skuList.add("6months_subscription");
                                skuList.add("year_subscription");
                                skuList.add("premium_annually");
                                skuList.add("premium_6months");
                                // SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
                                params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS);


                                billingClient.querySkuDetailsAsync(params.build(),
                                        new SkuDetailsResponseListener() {
                                            @Override
                                            public void onSkuDetailsResponse(BillingResult billingResult, List<SkuDetails> skuDetailsList) {
                                                // Process the result.

                                                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
                                                    toast.setText("Already Purchased");
                                                    toast.show();

                                                } else {
                                                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && skuDetailsList != null) {

                                            /*toast.setText(Integer.toString(times));
                                            toast.show();*/

                                                        for (SkuDetails skuDetails : skuDetailsList) {
                                                            String sku = skuDetails.getSku();
                                                            String price = skuDetails.getPrice();
                                                            if ("year_subscription".equals(sku) ||"6months_subscription".equals(sku) ||"monthly_subscription".equals(sku) || "reading_club".equals(sku) || "premium_annually".equals(sku) || "premium_6months".equals(sku)) {
                                                                premiumUpgradePrice = price;
                                                                Purchase.PurchasesResult purchasesResult = billingClient.queryPurchases(BillingClient.SkuType.SUBS);
                                                                List<Purchase> purchasesList = purchasesResult.getPurchasesList();
                                                                //


                                                                if (purchasesList != null && !purchasesList.isEmpty()) {
                                                                    for (Purchase purchase : purchasesList) {
                                                                        String skuName = purchase.getSku();
                                                            /*String ptoken = purchase.getPurchaseToken();
                                                             me1 = purchasesList.get(0).getOrderId();
                                                            int me2 = purchasesList.get(0).getPurchaseState();
                                                            int me3 = purchasesList.size();*/
                                                            /*toast.setText(skuName);
                                                            toast.show();*/
                                                                        if ("year_subscription".equals(sku) ||"6months_subscription".equals(sku) ||"monthly_subscription".equals(sku) || skuName.equals("reading_club") || skuName.equals("premium_6months") || skuName.equals("premium_annually")) {
                                                                            // Intent homeIntent = new Intent(getApplicationContext(), SubPHomeMainActivity.class);
                                                                            //Intent homeIntent = new Intent(getApplicationContext(), HomeMainActivity.class);

                                                                            sharedPreferencesAds = getSharedPreferences("AdsDecision", MODE_PRIVATE);
                                                                            SharedPreferences.Editor editor = sharedPreferencesAds.edit();
                                                                            editor.putInt("Ads", 0);
                                                                            editor.putInt("Sub", 1);
                                                                            editor.apply();
                                                                            Subscribed = sharedPreferencesAds.getInt("Sub", 5);

                                                                            Intent homeIntent = new Intent(getApplicationContext(), SubPHomeMainActivity.class);
                                                                            startActivity(homeIntent);

                                                                        }
                                                       /*     if (skuName.equals("premium_annually" )){
                                                                Intent homeIntent = new Intent(getApplicationContext(), SubPHomeMainActivity.class);
                                                                startActivity(homeIntent);
                                                            }*/
                                                                    }
                                                                    //toast.setText(Integer.toString(me3));
                                                                } else {
                                                                    // SharedPreferences.Editor editor = sharedPreferencesAds.edit();
                                                                    // editor.putString("Ads","Yes");
                                                                    //editor.putInt("Sub",0);
                                                                    //editor.apply();


                                                                    //toast.setText("nothing subscribed");
                                                                    //toast.show();
                                                                    // Intent homeIntent = new Intent(getApplicationContext(), SignInActivity.class);
                                                                    //Intent homeIntent = new Intent(getApplicationContext(), SignUpActivity.class);
                                                                    Intent homeIntent = new Intent(getApplicationContext(), HomeMainActivity.class);
                                                                    // Intent homeIntent = new Intent(getApplicationContext(), SubPHomeMainActivity.class);
                                                                    // Intent homeIntent = new Intent(getApplicationContext(), RealTimeDatabase.class);
                                                                    startActivity(homeIntent);
                                                                    finish();
                                                                }

                                                            } /*else if ("gas".equals(sku))
                                                gasPrice = price;
                                            }*/
                                                        }
                                                    } else {
                                                        times++;
                                                        if (times < 5) {
                                                            toast.setText("No Internet Connection");
                                                            toast.show();
                                                            if (!isNetworkAvailable()) {
                                                                Intent homeIntent = new Intent(getApplicationContext(), HomeMainActivity.class);
                                                                startActivity(homeIntent);
                                                                finish();
                                                            } else {
                                                                setUpBillingClient();
                                                            }

                                                        } else {
                                                            Intent homeIntent = new Intent(getApplicationContext(), HomeMainActivity.class);
                                                            startActivity(homeIntent);
                                                            finish();
                                                        }

                                                    }

                                                }


                                            }


                                            // }
                                        });
                            }
                        }
                    },1500);
                    ///In App Purchase
                }
            }
            @Override
            public void onBillingServiceDisconnected() {

                times++;
                times++;
                if (times < 2){
                    toast.setText("Internet Disconnected");
                    toast.show();

                    setUpBillingClient();
                }
                else {
                    Intent homeIntent = new Intent(getApplicationContext(), HomeMainActivity.class);
                    //Toast.makeText(MainActivity.this, "Disconnected O", Toast.LENGTH_SHORT).show();
                    startActivity(homeIntent);
                    finish();
                }

            }
        });
        //billingClient.endConnection();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferencesAds = getSharedPreferences("AdsDecision",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferencesAds.edit();
        editor.putInt("Ads",1);
        editor.putInt("Sub",5);  // not subscribed
       // editor.putInt("Sub",1);  //subscribed
        editor.putInt("Lifetime",5); //not subscribed
       // editor.putInt("Lifetime",1); //subscribed
        editor.apply();

        Lifetime = sharedPreferencesAds.getInt("Lifetime",5); //runtime
        Subscribed = sharedPreferencesAds.getInt("Sub", 5);
       // Lifetime = 1;  //Subscribed
        //Lifetime = 5;

        //Toast.makeText(this, "My: "+ Lifetime, Toast.LENGTH_SHORT).show();

        toast = Toast.makeText(this, "", Toast.LENGTH_LONG);

       // Intent homeIntent = new Intent(getApplicationContext(), SubPHomeMainActivity.class);
        //Intent homeIntent = new Intent(getApplicationContext(), HomeMainActivity.class);
        //Intent homeIntent = new Intent(getApplicationContext(), QuizTimedAll.class);
        //Intent homeIntent = new Intent(getApplicationContext(), InAppActivity.class);
        //startActivity(homeIntent);


    new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Lifetime==1){

                    sharedPreferencesAds = getSharedPreferences("AdsDecision",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferencesAds.edit();
                    editor.putInt("Sub",1);
                    editor.putInt("Ads",0);
                    editor.apply();
                    Subscribed = sharedPreferencesAds.getInt("Sub", 5);

                    //Intent homeIntent = new Intent(getApplicationContext(), InAppActivity.class);
                   Intent homeIntent = new Intent(getApplicationContext(), SubPHomeMainActivity.class);
                    startActivity(homeIntent);
                   // Toast.makeText(MainActivity.this, "You ", Toast.LENGTH_SHORT).show();
                }else{
                   // Toast.makeText(MainActivity.this, "Hi "+Lifetime, Toast.LENGTH_SHORT).show();
                    setUpBillingClient();
                }
            }
        }, SPLASH_TIME_OUT);

       ///////////////////

        //int sub = sharedPreferencesAds.getInt("Sub",10);
/*if(sub!=0){
    proverbsArrayList.add( 0, new Proverbs( "Seious o", "If it comes it affects mother", "If trouble comes it affects your closest relatives"));
}*/



    }

   @Override
    protected void onResume() {

    //Intent homeIntent = new Intent(getApplicationContext(), SubPHomeMainActivity.class);
   // Intent homeIntent = new Intent(getApplicationContext(), QuizTimedAll.class);
      // Intent homeIntent = new Intent(getApplicationContext(), HomeMainActivity.class);
       // Intent homeIntent = new Intent(getApplicationContext(), InAppActivity.class);
        //startActivity(homeIntent);
        super.onResume();

        /////////////

   new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (Lifetime==1){
                    sharedPreferencesAds = getSharedPreferences("AdsDecision",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferencesAds.edit();
                    editor.putInt("Sub",1);
                    editor.putInt("Ads",0);
                    editor.apply();
                    Subscribed = sharedPreferencesAds.getInt("Sub", 5);
                    //Toast.makeText(MainActivity.this, "Me1 Only ", Toast.LENGTH_SHORT).show();
                    Intent homeIntent = new Intent(getApplicationContext(), SubPHomeMainActivity.class);
                   // Intent homeIntent = new Intent(getApplicationContext(), InAppActivity.class);
                    startActivity(homeIntent);
                }else{
                  //  Toast.makeText(MainActivity.this, "Me1 "+Lifetime, Toast.LENGTH_SHORT).show();
                    setUpBillingClient();
                }
            }
        }, SPLASH_TIME_OUT);

       if(MainActivity.Lifetime ==1 || MainActivity.Subscribed == 1){
           proverbsArrayList.add(new Proverbs("Subscribed Baabi a ɔsono bɛfa biara yɛ kwan","Where ever the elephant passes is a way","A powerful person can make a way where there seems to be no way"));


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

        ///////////////
    }

    public void sendOnChannel1(View v) {
        String title = editTextTitle.getText().toString();
        String message = editTextMessage.getText().toString();

       /* Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_one)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        notificationManager.notify(1, notification);
    }

    public void sendOnChannel2(View v) {
        String title = editTextTitle.getText().toString();
        String message = editTextMessage.getText().toString();

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_2_ID)
                .setSmallIcon(R.drawable.ic_two)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();

        notificationManager.notify(2, notification); */


        /*Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, 10);
        //calendar.set(Calendar.HOUR_OF_DAY, 9);
        //calendar.set(Calendar.MINUTE, 45);

        Intent intent = new Intent(getApplicationContext(), Notification_receiver.class);
        //Intent intent = new Intent(getApplicationContext(), Home.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),100,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        //PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),100,intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        //alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);*/

    }

}
