package com.learnakantwi.twiguides;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Application;
import android.app.DownloadManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.learnakantwi.twiguides.AllActivity.allArrayList;
import static com.learnakantwi.twiguides.AlphabetsActivity.alphabetArray;
import static com.learnakantwi.twiguides.AnimalsActivity.animalsArrayList;
import static com.learnakantwi.twiguides.BodypartsActivity.bodypartsArrayList;
import static com.learnakantwi.twiguides.BusinessActivity.businessArrayList;
import static com.learnakantwi.twiguides.ColoursActivity.coloursArrayList;
import static com.learnakantwi.twiguides.CommonExpressionsaActivity.commonExpressionsAArrayList;
import static com.learnakantwi.twiguides.DaysOfWeekActivity.daysOfWeeksArray;
import static com.learnakantwi.twiguides.FamilyActivity.familyArrayList;
import static com.learnakantwi.twiguides.FoodActivity.foodArrayList;
import static com.learnakantwi.twiguides.MonthsActivity.monthsArrayList;
import static com.learnakantwi.twiguides.NumbersActivity.numbersArrayList;
import static com.learnakantwi.twiguides.PronounsActivity.pronounsArrayList;
import static com.learnakantwi.twiguides.ProverbsActivity.proverbsArrayList;
import static com.learnakantwi.twiguides.TimeActivity.timeArrayList;
import static com.learnakantwi.twiguides.WeatherActivity.weatherArray;

public class AppStartClass extends Application {

    StorageReference storageReference;

    public static final String CHANNEL_1_ID = "channel1";
    public static final String CHANNEL_2_ID = "channel2";

    //  sharedPreferences.edit().putString("AdvertPreference", "No").apply();


    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    "Channel 1",
                    NotificationManager.IMPORTANCE_HIGH

            );
            channel1.setDescription("This is Channel 1");

            NotificationChannel channel2 = new NotificationChannel(
                    CHANNEL_2_ID,
                    "Channel 2",
                    NotificationManager.IMPORTANCE_LOW
            );
            channel2.setDescription("This is Channel 2");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
            manager.createNotificationChannel(channel2);
        }
    }


        public void showDaily() {
            Calendar calendar = Calendar.getInstance();
            //calendar.add(Calendar.SECOND, 5);
           calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 30);
            //calendar.set(Calendar.SECOND,1);

            Intent intent = new Intent(getApplicationContext(), Notification_receiver.class);
            //Intent intent = new Intent(getApplicationContext(), Home.class);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),100,intent,PendingIntent.FLAG_UPDATE_CURRENT);

            //PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),100,intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            //alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);

            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);

        }

        public void downloadEssential(final String a){

            File myFiles = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/" + a + ".m4a");
            if (!myFiles.exists()) {
                final StorageReference musicRef = storageReference.child("/raw/" +a + ".m4a");
                musicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        final String url = uri.toString();
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                DownloadManager downloadManager = (DownloadManager) getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
                                Uri uri = Uri.parse(url);
                                DownloadManager.Request request = new DownloadManager.Request(uri);
                                request.setVisibleInDownloadsUi(false);
                                request.setDestinationInExternalFilesDir(getApplicationContext(), Environment.DIRECTORY_MUSIC, a + ".m4a");
                                downloadManager.enqueue(request);
                            }
                        };
                        Thread myThread = new Thread(runnable);
                        myThread.start();
                        Log.e(TAG, "onSuccess: Downloadede",null );
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: No",null );
                    }
                });
            }
            else {
                Log.e(TAG, "onAlreadyThere: Yes",null );
            }

        }



    @Override
    public void onCreate() {
        super.onCreate();

        storageReference = FirebaseStorage.getInstance().getReference();
        downloadEssential("excellentsound");


        createNotificationChannels();

        SharedPreferences sharedPreferences = this.getSharedPreferences("com.learnakantwi.twiguides", Context.MODE_PRIVATE);
        String dailyTwiPreference = sharedPreferences.getString("DailyTwiPreference", "Yes");

        assert dailyTwiPreference != null;
        if (dailyTwiPreference.equals("Yes")) {
            showDaily();
        }


//BUSINESS ARRAY

        businessArrayList = new ArrayList<>();
        businessArrayList.add(new Business("Money","Sika"));
        businessArrayList.add(new Business("I don't have money","Menni sika"));
        businessArrayList.add(new Business("I have money","Mewɔ sika"));
        businessArrayList.add(new Business("Bank","Sikakorabea"));
        businessArrayList.add(new Business("Buy","Tɔ"));
        businessArrayList.add(new Business("What are you buying?","Woretɔ dɛn?"));
        businessArrayList.add(new Business("What should I buy?","Dɛn na mentɔ?"));
        businessArrayList.add(new Business("What would you like to buy?","Dɛn na wopɛ sɛ wotɔ?"));
        businessArrayList.add(new Business("Buy for me","Tɔ ma me"));
        businessArrayList.add(new Business("I am buying...","Meretɔ..."));
        businessArrayList.add(new Business("I am buying food","Meretɔ aduane"));
        businessArrayList.add(new Business("I will buy","Mɛtɔ"));
        businessArrayList.add(new Business("I will buy food","Mɛtɔ aduane"));
        businessArrayList.add(new Business("I will not buy","Me ntɔ"));
        businessArrayList.add(new Business("I will not buy food","Me ntɔ aduane"));
        businessArrayList.add(new Business("How much will you buy?","Wobɛtɔ sɛn?"));
        businessArrayList.add(new Business("Where can I buy...?","Ɛhe na menya...atɔ?"));
        businessArrayList.add(new Business("Where can I buy water?","Ɛhe na menya nsuo atɔ?"));
        businessArrayList.add(new Business("I want to buy..","Mepɛ sɛ metɔ..."));
        businessArrayList.add(new Business("How many do you want to buy?","Ahe na wopɛ sɛ wotɔ?"));
        businessArrayList.add(new Business("I will buy two?","Mɛtɔ mmienu"));


        businessArrayList.add(new Business("It is expensive","Ne bo yɛ den"));
        businessArrayList.add(new Business("It is too expensive","Ne bo yɛ den dodo"));
        businessArrayList.add(new Business("I want discount","Mepɛ ntesoɔ"));
        businessArrayList.add(new Business("Give me discount","Te me so"));

        businessArrayList.add(new Business("Please reduce the price a little","Mesrɛ wo te so kakra"));
        businessArrayList.add(new Business("I want you to give to me on credit","Mepɛ sɛ wode firi me"));


        businessArrayList.add(new Business("It is cheap","Ɛyɛ fo"));
        businessArrayList.add(new Business("It is too cheap","Ɛyɛ fo dodo"));
        businessArrayList.add(new Business("My change","Me nsesa"));


        businessArrayList.add(new Business("Sell","Tɔn"));
        businessArrayList.add(new Business("I want to sell..","Mepɛ sɛ metɔn..."));
        businessArrayList.add(new Business("I want to sell clothes","Mepɛ sɛ metɔn ntade"));
        businessArrayList.add(new Business("How much is it?","Ɛyɛ sɛn"));
        businessArrayList.add(new Business("It is fifty cedis?","Ɛyɛ cedis aduonum"));
        businessArrayList.add(new Business("How much is this?","Wei yɛ sɛn?"));
        businessArrayList.add(new Business("Do you sell...here?","Wotɔn...wɔ ha?"));
        businessArrayList.add(new Business("Do you sell food here?","Wotɔn aduane wɔ ha?"));

        businessArrayList.add(new Business("How much do you sell it?","Wotɔn no sɛn?"));
        businessArrayList.add(new Business("Market (1)","Gua"));
        businessArrayList.add(new Business("Market (2)","Dwaso"));
        businessArrayList.add(new Business("I am going to the market","Merekɔ dwaso"));


        businessArrayList.add(new Business("Give me money","Ma me sika"));
        businessArrayList.add(new Business("Pay me","Tua me"));
        businessArrayList.add(new Business("Pay","Tua"));
        businessArrayList.add(new Business("Pay for me","Tua ma me"));
        businessArrayList.add(new Business("Pay for it","Tua ka"));
        businessArrayList.add(new Business("Can I pay tomorrow?","Metumi atua no ɔkyena?"));
        businessArrayList.add(new Business("I can't pay","Mentumi ntua"));
        businessArrayList.add(new Business("Who is selling?","Hena na ɔretɔn?"));

        businessArrayList.add(new Business("Work","Adwuma"));
        businessArrayList.add(new Business("Shop","Sotɔɔ"));
        businessArrayList.add(new Business("Profit","Mfasoɔ"));
        businessArrayList.add(new Business("I have made profit","Manya mfasoɔ"));
        businessArrayList.add(new Business("I will make profit","Mɛnya mfasoɔ"));
        businessArrayList.add(new Business("Loss","Ɛka"));
        businessArrayList.add(new Business("I have made a loss","Mabɔ ka"));
        businessArrayList.add(new Business("I will make a loss","Mɛbɔ ka"));








        // DAYS OF WEEK ARRAY


        daysOfWeeksArray = new ArrayList<>();

        daysOfWeeksArray.add(new DaysOfWeek("Monday", "Dwoada"));
        daysOfWeeksArray.add(new DaysOfWeek("Tuesday", "Benada"));
        daysOfWeeksArray.add(new DaysOfWeek("Wednesday", "Wukuada"));
        daysOfWeeksArray.add(new DaysOfWeek("Thursday", "Yawada"));
        daysOfWeeksArray.add(new DaysOfWeek("Friday", "Fiada"));
        daysOfWeeksArray.add(new DaysOfWeek("Saturday", "Memeneda"));
        daysOfWeeksArray.add(new DaysOfWeek("Sunday", "Kwasiada"));


        // ANIMALS ARRAY

        animalsArrayList = new ArrayList<>();

        animalsArrayList.add(new Animals("Bull", "Nantwinini"));
        animalsArrayList.add(new Animals("Animal", "Aboa"));
        animalsArrayList.add(new Animals("Animals", "Mmoa"));
        animalsArrayList.add(new Animals("Cow", "Nantwibere"));
        animalsArrayList.add(new Animals("Dog", "Kraman"));
        animalsArrayList.add(new Animals("Cat (1)", "Ɔkra"));
        animalsArrayList.add(new Animals("Cat (2)", "Agyinamoa"));
        animalsArrayList.add(new Animals("Donkey", "Afurum"));
        animalsArrayList.add(new Animals("Horse", "Pɔnkɔ"));
        animalsArrayList.add(new Animals("Lamb", "Oguammaa"));
        animalsArrayList.add(new Animals("Pig", "Prako"));
        animalsArrayList.add(new Animals("Rabbit", "Adanko"));
        animalsArrayList.add(new Animals("Sheep", "Odwan"));
        animalsArrayList.add(new Animals("Bat", "Ampan"));
        animalsArrayList.add(new Animals("Crocodile", "Ɔdɛnkyɛm"));
        animalsArrayList.add(new Animals("Deer", "Ɔforote"));
        animalsArrayList.add(new Animals("Elephant", "Ɔsono"));
        animalsArrayList.add(new Animals("Hippopotamus", "Susono"));
        animalsArrayList.add(new Animals("Hyena", "Pataku"));
        animalsArrayList.add(new Animals("Wolf (1)", "Pataku"));
        animalsArrayList.add(new Animals("Wolf (2)", "Sakraman"));
        animalsArrayList.add(new Animals("Leopard", "Ɔsebɔ"));
        animalsArrayList.add(new Animals("Lion", "Gyata"));
        animalsArrayList.add(new Animals("Rat", "Kusie"));
        animalsArrayList.add(new Animals("Spider", "Ananse"));
        animalsArrayList.add(new Animals("Snake", "Ɔwɔ"));
        animalsArrayList.add(new Animals("Duck", "Dabodabo"));
        animalsArrayList.add(new Animals("Bear", "Sisire"));
        animalsArrayList.add(new Animals("Chameleon", "Abosomakoterɛ"));
        animalsArrayList.add(new Animals("Lizard", "Koterɛ"));
        animalsArrayList.add(new Animals("Mouse", "Akura"));
        animalsArrayList.add(new Animals("Tortoise", "Akyekyedeɛ"));
        animalsArrayList.add(new Animals("Centipede", "Sakasaka"));
        animalsArrayList.add(new Animals("Millipede", "Kankabi"));
        animalsArrayList.add(new Animals("Crab", "Kɔtɔ"));
        animalsArrayList.add(new Animals("Camel", "Yoma"));
        animalsArrayList.add(new Animals("Fowl", "Akokɔ"));
        animalsArrayList.add(new Animals("Bird", "Anomaa"));
        animalsArrayList.add(new Animals("Scorpion", "Akekantwɛre"));
        animalsArrayList.add(new Animals("Cockroach", "Tɛfrɛ"));
        animalsArrayList.add(new Animals("Ants", "Tɛtea"));
        animalsArrayList.add(new Animals("Locust (1)", "Ntutummɛ"));
        animalsArrayList.add(new Animals("Locust (2)", "Mmoadabi"));
        animalsArrayList.add(new Animals("Goat (1)", "Apɔnkye"));
        animalsArrayList.add(new Animals("Goat (2)", "Abirekyie"));
        animalsArrayList.add(new Animals("Tiger", "Ɔsebɔ"));
        animalsArrayList.add(new Animals("Butterfly", "Afofantɔ"));
        animalsArrayList.add(new Animals("Grasscutter", "Akranteɛ"));
        animalsArrayList.add(new Animals("Lice", "Edwie"));
        animalsArrayList.add(new Animals("Porcupine", "Kɔtɔkɔ"));
        animalsArrayList.add(new Animals("Hedgehog (1)", "Apɛsɛ"));
        animalsArrayList.add(new Animals("Hedgehog (2)", "Apɛsɛe"));
        animalsArrayList.add(new Animals("Whale", "Bonsu"));
        animalsArrayList.add(new Animals("Shark", "Oboodede"));
        animalsArrayList.add(new Animals("Mosquito", "Ntontom"));
        animalsArrayList.add(new Animals("Grasshopper", "Abɛbɛ"));
        animalsArrayList.add(new Animals("Bedbug", "Nsonkuronsuo"));
        animalsArrayList.add(new Animals("Squirrel", "Opuro"));
        animalsArrayList.add(new Animals("Alligator", "Ɔmampam"));
        animalsArrayList.add(new Animals("Buffalo", "Ɛkoɔ"));
        animalsArrayList.add(new Animals("Worm", "Sonsono"));
        animalsArrayList.add(new Animals("Cattle", "Nantwie"));
        animalsArrayList.add(new Animals("Fish (1)", "Apataa"));
        animalsArrayList.add(new Animals("Fish (2)", "Nsuomnam"));

        animalsArrayList.add(new Animals("Tsetsefly", "Ohurii"));
        animalsArrayList.add(new Animals("Red Tree Ant", "Nhohoa"));
        animalsArrayList.add(new Animals("Driver Ants", "Nkrane"));
        animalsArrayList.add(new Animals("Praying Mantis", "Akokromfi"));
        animalsArrayList.add(new Animals("House fly", "Nwansena"));
        animalsArrayList.add(new Animals("Beetle", "Ɔbankuo"));


        animalsArrayList.add(new Animals("Vulture (1)", "Pɛtɛ"));
        animalsArrayList.add(new Animals("Vulture (2)", "Kɔkɔsakyi"));
        animalsArrayList.add(new Animals("Hawk", "Akorɔma"));
        animalsArrayList.add(new Animals("Guinea Fowl", "Akɔmfɛm"));
        animalsArrayList.add(new Animals("Monkey", "Adoe"));
        animalsArrayList.add(new Animals("Parrot", "Akoo"));
        animalsArrayList.add(new Animals("Crow", "Kwaakwaadabi"));
        animalsArrayList.add(new Animals("Owl", "Patuo"));
        animalsArrayList.add(new Animals("Eagle", "Ɔkɔre"));
        animalsArrayList.add(new Animals("Sparrow", "Akasanoma"));
        animalsArrayList.add(new Animals("Swallow", "Asomfena"));
        animalsArrayList.add(new Animals("Dove", "Aborɔnoma"));


        animalsArrayList.add(new Animals("Bee", "Wowa"));
        animalsArrayList.add(new Animals("Herring", "Ɛmane"));
        animalsArrayList.add(new Animals("Lobster", "Ɔbɔnkɔ"));
        animalsArrayList.add(new Animals("Lobsters", "Mmɔnkɔ"));


        Collections.sort(animalsArrayList);

        animalsArrayList.add(new Animals("Which animal?", "Aboa bɛn?"));
        animalsArrayList.add(new Animals("Which animal is this?", "Aboa bɛn ni?"));
        animalsArrayList.add(new Animals("It is a lion", "Ɛyɛ gyata"));


// Alphabet Array
        alphabetArray = new ArrayList<>();

        alphabetArray.add(new Alphabets("Aa" ,"A","a"));
        alphabetArray.add(new Alphabets("Bb","B","b"));
        alphabetArray.add(new Alphabets("Dd","D","d"));
        alphabetArray.add(new Alphabets("Ee","E","e"));
        alphabetArray.add(new Alphabets("Ɛɛ","Ɛ","ɛ"));
        alphabetArray.add(new Alphabets("Ff","F","f"));
        alphabetArray.add(new Alphabets("Gg","G","g"));
        alphabetArray.add(new Alphabets("Hh","H","h"));
        alphabetArray.add(new Alphabets("Ii","I","i"));
        alphabetArray.add(new Alphabets("Kk","K","k"));
        alphabetArray.add(new Alphabets("Ll","L","l"));
        alphabetArray.add(new Alphabets("Mm","M","m"));
        alphabetArray.add(new Alphabets("Nn","N","n"));
        alphabetArray.add(new Alphabets("Oo","O","o"));
        alphabetArray.add(new Alphabets("Ɔɔ","Ɔ","ɔ"));
        alphabetArray.add(new Alphabets("Pp","P","p"));
        alphabetArray.add(new Alphabets("Rr","R","r"));
        alphabetArray.add(new Alphabets("Ss","S","s"));
        alphabetArray.add(new Alphabets("Tt","T","t"));
        alphabetArray.add(new Alphabets("Uu","U","u"));
        alphabetArray.add(new Alphabets("Ww","W","w"));
        alphabetArray.add(new Alphabets("Yy","Y","y"));


// Body Parts Array

        bodypartsArrayList = new ArrayList<>();

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

        bodypartsArrayList.add(new Bodyparts("Spine","Akyi berɛmo"));


        Collections.sort(bodypartsArrayList);


        //ColoursArray

        coloursArrayList = new ArrayList<>();

        coloursArrayList.add(new Colours("Colour (1)","Ahosu"));
        coloursArrayList.add(new Colours("Colour (2)","Kɔla"));
        coloursArrayList.add(new Colours("What colour is it?","Ɛyɛ kɔla bɛn?"));
        coloursArrayList.add(new Colours("Red","Kɔkɔɔ"));
        coloursArrayList.add(new Colours("Black","Tuntum"));
        coloursArrayList.add(new Colours("White (1)","Fitaa"));
        coloursArrayList.add(new Colours("White (2)","Fufuo"));
        coloursArrayList.add(new Colours("Green","Ahabammono"));
        coloursArrayList.add(new Colours("Blue","Bruu"));
        coloursArrayList.add(new Colours("Brown","Dodowee"));
        coloursArrayList.add(new Colours("Grey","Nsonso"));
        coloursArrayList.add(new Colours("Ash","Nsonso"));
        coloursArrayList.add(new Colours("Yellow","Akokɔsrade"));
        coloursArrayList.add(new Colours("Spotted","Nsisimu"));
        coloursArrayList.add(new Colours("Purple (1)","Beredum"));
        coloursArrayList.add(new Colours("Purple (2)","Afasebiri"));
        coloursArrayList.add(new Colours("Pink","Memen"));
        coloursArrayList.add(new Colours("Dark","Sum"));


        //CommonExpressionsAArrayList

        commonExpressionsAArrayList = new ArrayList<>();

        commonExpressionsAArrayList.add(new CommonExpressionsA("Good morning (1)","Maakye"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("Good morning (2)","Mema wo akye"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("Good afternoon (1)","Maaha"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("Good afternoon (2)","Mema wo aha"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("Good evening (1)","Maadwo"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("Good evening (2)","Mema wo adwo"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("Good night","Da yie"));

        commonExpressionsAArrayList.add(new CommonExpressionsA("How are you?","Wo ho te sɛn?"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("How is your mother?","Wo maame ho te sɛn?"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("How is your wife?","Wo yere ho te sɛn?"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("I am fine","Me ho yɛ"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("How are they doing?","Wɔn ho te sɛn?"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("I am not feeling well (1)","Me ho mfa me"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("I am not feeling well (2)","Mente apɔ"));

        commonExpressionsAArrayList.add(new CommonExpressionsA("She is fine","Ne ho yɛ"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("He is fine","Ne ho yɛ"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("They are fine","Wɔn ho yɛ"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("They are all fine","Wɔn nyinaa ho yɛ"));


        commonExpressionsAArrayList.add(new CommonExpressionsA("I am happy to meet you","M'ani agye sɛ mahyia wo"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("Welcome","Akwaaba"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("I bid you welcome","Mema wo akwaaba"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("I am happy","M'ani agye"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("I am sad","Me werɛ ahow"));

        commonExpressionsAArrayList.add(new CommonExpressionsA("Stop crying","Gyae su"));


        commonExpressionsAArrayList.add(new CommonExpressionsA("Please (1)","Mepa wo kyɛw"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("Please (2)","Mesrɛ wo"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("Thank you (1)","Medaase"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("Thank you (2)","Me da wo ase"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("I miss you","Mafe wo"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("I will miss you","Mɛfe wo"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("We will miss you","Yɛbɛfe wo"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("We will miss you (Plural)","Yɛbɛfe mo"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("Love","Ɔdɔ"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("I love you","Me dɔ wo"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("We love you","Yɛdɔ mo"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("Do you love me?","Wo dɔ me?"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("Yes","Aane"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("No","Dabi"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("My name is Kwaku (1)","Yɛfrɛ me Kwaku"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("My name is Kwaku (2)","Me din de Kwaku"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("What is your name? (1)","Yɛfrɛ wo sɛn?"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("What is your name? (2)","Wo din de sɛn?"));

        commonExpressionsAArrayList.add(new CommonExpressionsA("How old are you?","Wadi mfe sɛn?"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("I am 30 years old","Madi mfe aduasa"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("Call me","Frɛ me"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("Do you speak English?","Wo ka borɔfo?"));

        commonExpressionsAArrayList.add(new CommonExpressionsA("I am sick","Me yare"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("I need money","Me hia sika"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("Do you have money","Wowɔ sika?"));

        commonExpressionsAArrayList.add(new CommonExpressionsA("Congratulations","Ayekoo"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("See you soon","Ɛrenkyɛ mehu wo"));
        commonExpressionsAArrayList.add(new CommonExpressionsA("Give me a hug","Yɛ me atuu"));

        //FAMILY ARRAY LIST

        familyArrayList = new ArrayList<>();

        familyArrayList.add(new Family("Family", "Abusua"));
        familyArrayList.add(new Family("Families", "Mmusua"));

        familyArrayList.add(new Family("Father (1)", "Papa"));
        familyArrayList.add(new Family("Father (2)", "Agya"));
        familyArrayList.add(new Family("Father (3)", "Ɔse"));
        familyArrayList.add(new Family("My father (1)", "Me papa"));
        familyArrayList.add(new Family("My father (2)", "M'agya"));
        familyArrayList.add(new Family("My father (3)", "Me se"));
        familyArrayList.add(new Family("Daddy", "Dada"));

        familyArrayList.add(new Family("Mother (1)", "Maame"));
        familyArrayList.add(new Family("Mother (2)", "Ɛna"));
        familyArrayList.add(new Family("Mother (3)", "Oni"));
        familyArrayList.add(new Family("My Mother (1)", "Me maame"));
        familyArrayList.add(new Family("My Mother (2)", "Me na"));
        familyArrayList.add(new Family("My Mother (3)", "Me ni"));
        familyArrayList.add(new Family("Mummy", "Mama"));


        familyArrayList.add(new Family("Parent", "Ɔwofo"));
        familyArrayList.add(new Family("Parents", "Awofo"));
        familyArrayList.add(new Family("Child (1)", "Abofra"));
        familyArrayList.add(new Family("Child (2)", "Akwadaa"));
        familyArrayList.add(new Family("Children (1)", "Mma"));
        familyArrayList.add(new Family("Children (2)", "Mmofra"));
        familyArrayList.add(new Family("Baby", "Abofra"));

        familyArrayList.add(new Family("Firstborn (1)", "Abakan"));
        familyArrayList.add(new Family("Firstborn (2)", "Piesie"));
        familyArrayList.add(new Family("Lastborn", "Kaakyire"));

        familyArrayList.add(new Family("Husband", "Kunu"));
        familyArrayList.add(new Family("Husbands", "Kununom"));

        familyArrayList.add(new Family("Wife", "Yere"));
        familyArrayList.add(new Family("Wives", "Yerenom"));

        familyArrayList.add(new Family("Brother", "Nuabarima"));
        familyArrayList.add(new Family("Brothers", "Nua mmarima"));
        familyArrayList.add(new Family("Sister", "Nuabaa"));
        familyArrayList.add(new Family("Sisters", "Nua mmaa"));

        familyArrayList.add(new Family("Sibling", "Nua"));
        familyArrayList.add(new Family("Siblings", "Nuanom"));

        familyArrayList.add(new Family("Son", "Babarima"));
        familyArrayList.add(new Family("Sons", "mma mmarima"));
        familyArrayList.add(new Family("Daughter", "Babaa"));
        familyArrayList.add(new Family("Daughters", "Mma mmaa"));

        familyArrayList.add(new Family("Cousin", "Nua"));
        familyArrayList.add(new Family("Grandchild", "Banana"));
        familyArrayList.add(new Family("Great Grandchild", "Nanankanso"));
        familyArrayList.add(new Family("Grandfather", "Nanabarima"));
        familyArrayList.add(new Family("Great grandfather", "Nanabarima prenu"));
        familyArrayList.add(new Family("Grandmother", "Nanabaa"));
        familyArrayList.add(new Family("Great grandmother", "Nanabaa prenu"));

        familyArrayList.add(new Family("In-law", "Asew"));
        familyArrayList.add(new Family("Father-in-law", "Asebarima"));
        familyArrayList.add(new Family("Mother-in-law", "Asebaa"));
        familyArrayList.add(new Family("Brother-in-law", "Akonta"));
        familyArrayList.add(new Family("Sister-in-law", "Akumaa"));
        familyArrayList.add(new Family("Son-in-law (1)", "Asew"));
        familyArrayList.add(new Family("Son-in-law (2)", "Babaa kunu"));
        familyArrayList.add(new Family("Daughter-in-law", "Asew"));
        familyArrayList.add(new Family("Daughter-in-law", "Babarima yere"));


        familyArrayList.add(new Family("Maternal Uncle", "Wɔfa"));
        familyArrayList.add(new Family("Paternal Uncle (1)", "Papa"));
        familyArrayList.add(new Family("Paternal Uncle (2)", "Agya"));
        familyArrayList.add(new Family("Paternal Uncle (3)", "Papa nuabarima"));

        familyArrayList.add(new Family("Paternal Aunt", "Sewaa"));
        familyArrayList.add(new Family("My Paternal Aunt", "Me Sewaa"));

        familyArrayList.add(new Family("Maternal Aunt (1)", "Maame nuabaa"));
        familyArrayList.add(new Family("Maternal Aunt (2)", "Maame"));
        familyArrayList.add(new Family("My maternal Aunt", "Me maame nuabaa"));


        familyArrayList.add(new Family("Niece", "Wɔfaase baa"));
        familyArrayList.add(new Family("Nephew", "Wɔfaase barima"));
        familyArrayList.add(new Family("Niece/Nephew", "Wɔfaase"));

        familyArrayList.add(new Family("Cousin (1)", "Nua"));
        familyArrayList.add(new Family("Cousin (2)", "Wɔfa ba"));
        familyArrayList.add(new Family("Cousin (3)", "Sewaa ba"));
        familyArrayList.add(new Family("My Cousin", "Me nua"));


        familyArrayList.add(new Family("Adopted child", "Abanoma"));
        familyArrayList.add(new Family("Orphan", "Agyanka"));
        familyArrayList.add(new Family("Widow", "Okunafo"));
        familyArrayList.add(new Family("Widower", "Barima kunafo"));

        familyArrayList.add(new Family("Marriage", "Awareɛ"));

        familyArrayList.add(new Family("Twins", "Ntafoɔ"));
        familyArrayList.add(new Family("Triplets", "Ahenasa"));
        familyArrayList.add(new Family("Quadruplets", "Ahenanan"));

//FOOD ARRAY LIST

        foodArrayList = new ArrayList<>();

        foodArrayList.add(new Food("Rice", "Ɛmo"));
        foodArrayList.add(new Food("Yam", "Bayerɛ"));
        foodArrayList.add(new Food("Plantain", "Bɔɔdeɛ"));
        foodArrayList.add(new Food("Cassava", "Bankye"));
        foodArrayList.add(new Food("Onion", "Gyeene"));
        foodArrayList.add(new Food("Salt", "Nkyene"));

        //fruits
        foodArrayList.add(new Food("Fruit", "Aduaba"));
        foodArrayList.add(new Food("Apple", "Aprɛ"));
        foodArrayList.add(new Food("Banana", "Kwadu"));
        foodArrayList.add(new Food("Orange (1)", "Ankaa"));
        foodArrayList.add(new Food("Orange (2)", "Akutu"));
        foodArrayList.add(new Food("Pawpaw", "Borɔferɛ"));
        foodArrayList.add(new Food("Coconut", "Kube"));
        foodArrayList.add(new Food("Pear", "Paya"));
        foodArrayList.add(new Food("Tigernut", "Atadwe"));
        foodArrayList.add(new Food("Pineapple", "Aborɔbɛ"));
        foodArrayList.add(new Food("Ginger", "Akekaduro"));
        foodArrayList.add(new Food("Sugarcane", "Ahwedeɛ"));
        foodArrayList.add(new Food("Corn", "Aburo"));
        foodArrayList.add(new Food("Maize", "Aburo"));
        foodArrayList.add(new Food("Groundnut", "Nkateɛ"));
        foodArrayList.add(new Food("Peanut", "Nkateɛ"));
        foodArrayList.add(new Food("Palm fruit", "Abɛ"));


        //Vegetables
        foodArrayList.add(new Food("Vegetable", "Atosodeɛ"));
        foodArrayList.add(new Food("Pepper", "Mako"));
        foodArrayList.add(new Food("Bean", "Adua"));
        foodArrayList.add(new Food("Okro", "Nkuruma"));
        foodArrayList.add(new Food("Garden eggs", "Nyaadewa"));
        foodArrayList.add(new Food("Tomato", "Ntoosi"));
        foodArrayList.add(new Food("Garlic", "Galik"));
        foodArrayList.add(new Food("Cucumber", "Ɛferɛ"));


        foodArrayList.add(new Food("Lobster", "Ɔbɔnkɔ"));
        foodArrayList.add(new Food("Cocoa", "Kokoo"));
        foodArrayList.add(new Food("Palm kernel", "Adwe"));
        foodArrayList.add(new Food("Palm kernel oil", "Adwe ngo"));
        foodArrayList.add(new Food("Vegetable oil", "Anwa"));
        foodArrayList.add(new Food("Snail", "Nwa"));
        foodArrayList.add(new Food("Groundnut soup", "Nkate nkwan"));
        foodArrayList.add(new Food("Palm nut soup", "Abɛ nkwan"));

        //Others
        foodArrayList.add(new Food("Dough", "Mmɔre"));
        foodArrayList.add(new Food("Kenkey", "Dɔkono"));
        foodArrayList.add(new Food("Flour", "Esiam"));
        foodArrayList.add(new Food("Wheat", "Ayuo"));
        foodArrayList.add(new Food("Soup", "Nkwan"));
        foodArrayList.add(new Food("Stew", "Abomu"));
        foodArrayList.add(new Food("Egg", "Kosua"));
        foodArrayList.add(new Food("Bread (1)", "Paanoo"));
        foodArrayList.add(new Food("Bread (2)", "Burodo"));
        foodArrayList.add(new Food("Oil", "Ngo"));
        foodArrayList.add(new Food("Fish (1)", "Apataa"));
        foodArrayList.add(new Food("Fish (2)", "Nsuomnam"));
        foodArrayList.add(new Food("Pork", "Prakonam"));
        foodArrayList.add(new Food("Meat", "Nam"));
        foodArrayList.add(new Food("Mutton", "Odwannam"));
        foodArrayList.add(new Food("Lamb", "Odwannam"));
        foodArrayList.add(new Food("Sugar", "Asikyire"));
        foodArrayList.add(new Food("Honey", "Ɛwoɔ"));
        foodArrayList.add(new Food("Water", "Nsuo"));
        foodArrayList.add(new Food("Food", "Aduane"));

        Collections.sort(foodArrayList);

        foodArrayList.add(new Food("I am hungry", "Ɛkɔm de me"));
        foodArrayList.add(new Food("Are you hungry?", "Ɛkɔm de wo anaa?"));
        foodArrayList.add(new Food("What will you eat?", "Dɛn na wobedi?"));
        foodArrayList.add(new Food("I will eat kenkey", "Medi dɔkono"));


        //MONTHS ARRAY LIST

        monthsArrayList = new ArrayList<>();

        monthsArrayList.add(new Months("January","Ɔpɛpɔn"));
        monthsArrayList.add(new Months("We are in the month of January","Yɛwɔ Ɔpɛpɔn bosome mu"));
        monthsArrayList.add(new Months("February","Ɔgyefoɔ"));
        monthsArrayList.add(new Months("We will go to Ghana in February","Yɛbɛkɔ Ghana Ɔgyefoɔ bosome no mu"));
        monthsArrayList.add(new Months("March","Ɔbɛnem"));
        monthsArrayList.add(new Months("I will see you in March","Mehu wo wɔ Ɔbɛnem bosome no mu"));
        monthsArrayList.add(new Months("April","Oforisuo"));
        monthsArrayList.add(new Months("It often rains in April","Osu taa tɔ wɔ Oforisuo bosome no mu"));
        monthsArrayList.add(new Months("May","Kotonimaa"));
        monthsArrayList.add(new Months("June","Ayɛwohomumɔ"));
        monthsArrayList.add(new Months("July","Kitawonsa"));
        monthsArrayList.add(new Months("August","Ɔsanaa"));
        monthsArrayList.add(new Months("I was born in the month of August","Wɔwoo me Ɔsanaa bosome no mu"));
        monthsArrayList.add(new Months("September","Ɛbɔ"));
        monthsArrayList.add(new Months("October","Ahinime"));
        monthsArrayList.add(new Months("November","Obubuo"));
        monthsArrayList.add(new Months("December","Ɔpɛnimma"));
        monthsArrayList.add(new Months("It is often cold in December","Awɔw taa ba wɔ Ɔpɛnimma bosome no mu"));

        monthsArrayList.add(new Months("Which month?","Bosome bɛn?"));


        //NUMBERS ARRAY LIST

        numbersArrayList = new ArrayList<>();

        numbersArrayList.add(new Numbers("0", "Hwee/Ohunu"));
        numbersArrayList.add(new Numbers("1", "Baako"));
        numbersArrayList.add(new Numbers("2", "Mmienu"));
        numbersArrayList.add(new Numbers("3", "Mmiɛnsa"));
        numbersArrayList.add(new Numbers("4", "Ɛnan"));
        numbersArrayList.add(new Numbers("5", "Enum"));
        numbersArrayList.add(new Numbers("6", "Nsia"));
        numbersArrayList.add(new Numbers("7", "Nson"));
        numbersArrayList.add(new Numbers("8", "Nwɔtwe"));
        numbersArrayList.add(new Numbers("9", "Nkron"));
        numbersArrayList.add(new Numbers("10", "Du"));

        numbersArrayList.add(new Numbers("11", "Dubaako"));
        numbersArrayList.add(new Numbers("12", "Dummienu"));
        numbersArrayList.add(new Numbers("13", "Dummiɛnsa"));
        numbersArrayList.add(new Numbers("14", "Dunan"));
        numbersArrayList.add(new Numbers("15", "Dunum"));
        numbersArrayList.add(new Numbers("16", "Dunsia"));
        numbersArrayList.add(new Numbers("17", "Dunson"));
        numbersArrayList.add(new Numbers("18", "Dunwɔtwe"));
        numbersArrayList.add(new Numbers("19", "Dunkron"));
        numbersArrayList.add(new Numbers("20", "Aduonu"));

        numbersArrayList.add(new Numbers("21", "Aduonu baako"));
        numbersArrayList.add(new Numbers("22", "Aduonu mmienu"));
        numbersArrayList.add(new Numbers("23", "Aduonu mmiɛnsa"));
        numbersArrayList.add(new Numbers("24", "Aduonu nan"));
        numbersArrayList.add(new Numbers("25", "Aduonu num"));
        numbersArrayList.add(new Numbers("26", "Aduonu nsia"));
        numbersArrayList.add(new Numbers("27", "Aduonu nson"));
        numbersArrayList.add(new Numbers("28", "Aduonu nwɔtwe"));
        numbersArrayList.add(new Numbers("29", "Aduonu nkron"));
        numbersArrayList.add(new Numbers("30", "Aduasa"));


        numbersArrayList.add(new Numbers("31", "Aduasa baako"));
        numbersArrayList.add(new Numbers("32", "Aduasa mmienu"));
        numbersArrayList.add(new Numbers("33", "Aduasa mmiɛnsa"));
        numbersArrayList.add(new Numbers("34", "Aduasa nan"));
        numbersArrayList.add(new Numbers("35", "Aduasa num"));
        numbersArrayList.add(new Numbers("36", "Aduasa nsia"));
        numbersArrayList.add(new Numbers("37", "Aduasa nson"));
        numbersArrayList.add(new Numbers("38", "Aduasa nwɔtwe"));
        numbersArrayList.add(new Numbers("39", "Aduasa nkron"));
        numbersArrayList.add(new Numbers("40", "Aduanan"));

        numbersArrayList.add(new Numbers("41", "Aduanan baako"));
        numbersArrayList.add(new Numbers("42", "Aduanan mmienu"));
        numbersArrayList.add(new Numbers("43", "Aduanan mmiɛnsa"));
        numbersArrayList.add(new Numbers("44", "Aduanan nan"));
        numbersArrayList.add(new Numbers("45", "Aduanan num"));
        numbersArrayList.add(new Numbers("46", "Aduanan nsia"));
        numbersArrayList.add(new Numbers("47", "Aduanan nson"));
        numbersArrayList.add(new Numbers("48", "Aduanan nwɔtwe"));
        numbersArrayList.add(new Numbers("49", "Aduanan nkron"));
        numbersArrayList.add(new Numbers("50", "Aduonum"));

        numbersArrayList.add(new Numbers("51", "Aduonum baako"));
        numbersArrayList.add(new Numbers("52", "Aduonum mmienu"));
        numbersArrayList.add(new Numbers("53", "Aduonum mmiɛnsa"));
        numbersArrayList.add(new Numbers("54", "Aduonum nan"));
        numbersArrayList.add(new Numbers("55", "Aduonum num"));
        numbersArrayList.add(new Numbers("56", "Aduonum nsia"));
        numbersArrayList.add(new Numbers("57", "Aduonum nson"));
        numbersArrayList.add(new Numbers("58", "Aduonum nwɔtwe"));
        numbersArrayList.add(new Numbers("59", "Aduonum nkron"));
        numbersArrayList.add(new Numbers("60", "Aduosia"));

        numbersArrayList.add(new Numbers("61", "Aduosia baako"));
        numbersArrayList.add(new Numbers("62", "Aduosia mmienu"));
        numbersArrayList.add(new Numbers("63", "Aduosia mmiɛnsa"));
        numbersArrayList.add(new Numbers("64", "Aduosia nan"));
        numbersArrayList.add(new Numbers("65", "Aduosia num"));
        numbersArrayList.add(new Numbers("66", "Aduosia nsia"));
        numbersArrayList.add(new Numbers("67", "Aduosia nson"));
        numbersArrayList.add(new Numbers("68", "Aduosia nwɔtwe"));
        numbersArrayList.add(new Numbers("69", "Aduosia nkron"));
        numbersArrayList.add(new Numbers("70", "Aduoson"));

        numbersArrayList.add(new Numbers("71", "Aduoson baako"));
        numbersArrayList.add(new Numbers("72", "Aduoson mmienu"));
        numbersArrayList.add(new Numbers("73", "Aduoson mmiɛnsa"));
        numbersArrayList.add(new Numbers("74", "Aduoson nan"));
        numbersArrayList.add(new Numbers("75", "Aduoson num"));
        numbersArrayList.add(new Numbers("76", "Aduoson nsia"));
        numbersArrayList.add(new Numbers("77", "Aduoson nson"));
        numbersArrayList.add(new Numbers("78", "Aduoson nwɔtwe"));
        numbersArrayList.add(new Numbers("79", "Aduoson nkron"));
        numbersArrayList.add(new Numbers("80", "Aduowɔtwe"));

        numbersArrayList.add(new Numbers("81", "Aduowɔtwe baako"));
        numbersArrayList.add(new Numbers("82", "Aduowɔtwe mmienu"));
        numbersArrayList.add(new Numbers("83", "Aduowɔtwe mmiɛnsa"));
        numbersArrayList.add(new Numbers("84", "Aduowɔtwe nan"));
        numbersArrayList.add(new Numbers("85", "Aduowɔtwe num"));
        numbersArrayList.add(new Numbers("86", "Aduowɔtwe nsia"));
        numbersArrayList.add(new Numbers("87", "Aduowɔtwe nson"));
        numbersArrayList.add(new Numbers("88", "Aduowɔtwe nwɔtwe"));
        numbersArrayList.add(new Numbers("89", "Aduowɔtwe nkron"));
        numbersArrayList.add(new Numbers("90", "Aduokron"));

        numbersArrayList.add(new Numbers("91", "Aduokron baako"));
        numbersArrayList.add(new Numbers("92", "Aduokron mmienu"));
        numbersArrayList.add(new Numbers("93", "Aduokron mmiɛnsa"));
        numbersArrayList.add(new Numbers("94", "Aduokron nan"));
        numbersArrayList.add(new Numbers("95", "Aduokron num"));
        numbersArrayList.add(new Numbers("96", "Aduokron nsia"));
        numbersArrayList.add(new Numbers("97", "Aduokron nson"));
        numbersArrayList.add(new Numbers("98", "Aduokron nwɔtwe"));
        numbersArrayList.add(new Numbers("99", "Aduokron nkron"));
        numbersArrayList.add(new Numbers("100", "Ɔha"));

        numbersArrayList.add(new Numbers("101", "Ɔha ne baako"));
        numbersArrayList.add(new Numbers("102", "Ɔha ne mmienu"));
        numbersArrayList.add(new Numbers("103", "Ɔha ne mmiɛnsa"));
        numbersArrayList.add(new Numbers("104", "Ɔha ne nan"));
        numbersArrayList.add(new Numbers("105", "Ɔha ne num"));
        numbersArrayList.add(new Numbers("106", "Ɔha ne nsia"));
        numbersArrayList.add(new Numbers("107", "Ɔha ne nson"));
        numbersArrayList.add(new Numbers("108", "Ɔha ne nwɔtwe"));
        numbersArrayList.add(new Numbers("109", "Ɔha ne nkron"));
        numbersArrayList.add(new Numbers("110", "Ɔha ne du"));

        numbersArrayList.add(new Numbers("111", "Ɔha ne dubaako"));
        numbersArrayList.add(new Numbers("112", "Ɔha ne dummienu"));
        numbersArrayList.add(new Numbers("113", "Ɔha ne dummiɛnsa"));
        numbersArrayList.add(new Numbers("114", "Ɔha ne dunan"));
        numbersArrayList.add(new Numbers("115", "Ɔha ne dunum"));
        numbersArrayList.add(new Numbers("116", "Ɔha ne dunsia"));
        numbersArrayList.add(new Numbers("117", "Ɔha ne dunson"));
        numbersArrayList.add(new Numbers("118", "Ɔha ne dunwɔtwe"));
        numbersArrayList.add(new Numbers("119", "Ɔha ne dunkron"));
        numbersArrayList.add(new Numbers("120", "Ɔha aduonu"));


        numbersArrayList.add(new Numbers("121", "Ɔha aduonu baako"));
        numbersArrayList.add(new Numbers("122", "Ɔha aduonu mmienu"));
        numbersArrayList.add(new Numbers("123", "Ɔha aduonu mmiɛnsa"));
        numbersArrayList.add(new Numbers("124", "Ɔha aduonu nan"));
        numbersArrayList.add(new Numbers("125", "Ɔha aduonu num"));

        numbersArrayList.add(new Numbers("130", "Ɔha aduasa"));
        numbersArrayList.add(new Numbers("140", "Ɔha aduanan"));
        numbersArrayList.add(new Numbers("150", "Ɔha aduonum"));
        numbersArrayList.add(new Numbers("160", "Ɔha aduosia"));
        numbersArrayList.add(new Numbers("170", "Ɔha aduoson"));
        numbersArrayList.add(new Numbers("180", "Ɔha aduowɔtwe"));
        numbersArrayList.add(new Numbers("190", "Ɔha aduokron"));
        numbersArrayList.add(new Numbers("200", "Ahanu"));


        numbersArrayList.add(new Numbers("201", "Ahanu ne baako"));
        numbersArrayList.add(new Numbers("202", "Ahanu ne mmienu"));
        numbersArrayList.add(new Numbers("203", "Ahanu ne mmiɛnsa"));
        numbersArrayList.add(new Numbers("204", "Ahanu ne nan"));
        numbersArrayList.add(new Numbers("205", "Ahanu ne num"));
        numbersArrayList.add(new Numbers("206", "Ahanu ne nsia"));
        numbersArrayList.add(new Numbers("207", "Ahanu ne nson"));
        numbersArrayList.add(new Numbers("208", "Ahanu ne nwɔtwe"));
        numbersArrayList.add(new Numbers("209", "Ahanu ne nkron"));
        numbersArrayList.add(new Numbers("210", "Ahanu ne du"));
        numbersArrayList.add(new Numbers("211", "Ahanu ne dubaako"));

        numbersArrayList.add(new Numbers("220", "Ahanu aduonu"));
        numbersArrayList.add(new Numbers("221", "Ahanu aduonu baako"));
        numbersArrayList.add(new Numbers("230", "Ahanu aduasa"));
        numbersArrayList.add(new Numbers("240", "Ahanu aduanan"));
        numbersArrayList.add(new Numbers("250", "Ahanu aduonum"));

        numbersArrayList.add(new Numbers("300", "Ahasa"));
        numbersArrayList.add(new Numbers("400", "Ahanan"));
        numbersArrayList.add(new Numbers("500", "Ahanum"));
        numbersArrayList.add(new Numbers("600", "Ahansia"));
        numbersArrayList.add(new Numbers("700", "Ahanson"));
        numbersArrayList.add(new Numbers("800", "Ahanwɔtwe"));
        numbersArrayList.add(new Numbers("900", "Ahankron"));
        numbersArrayList.add(new Numbers("1000", "Apem"));


        numbersArrayList.add(new Numbers("1001", "Apem ne baako"));
        numbersArrayList.add(new Numbers("1002", "Apem ne mmienu"));
        numbersArrayList.add(new Numbers("1003", "Apem ne mmiɛnsa"));
        numbersArrayList.add(new Numbers("1004", "Apem ne nan"));
        numbersArrayList.add(new Numbers("1005", "Apem ne num"));

        numbersArrayList.add(new Numbers("1010", "Apem ne du"));
        numbersArrayList.add(new Numbers("1011", "Apem ne dubaako"));
        numbersArrayList.add(new Numbers("1012", "Apem ne dummienu"));
        numbersArrayList.add(new Numbers("1013", "Apem ne dummiɛnsa"));
        numbersArrayList.add(new Numbers("1014", "Apem ne dunan"));
        numbersArrayList.add(new Numbers("1015", "Apem ne dunum"));

        numbersArrayList.add(new Numbers("1020", "Apem aduonu"));
        numbersArrayList.add(new Numbers("1021", "Apem aduonu baako"));
        numbersArrayList.add(new Numbers("1022", "Apem aduonu mmienu"));
        numbersArrayList.add(new Numbers("1023", "Apem aduonu mmiɛnsa"));
        numbersArrayList.add(new Numbers("1024", "Apem aduonu nan"));
        numbersArrayList.add(new Numbers("1025", "Apem aduonu num"));

        numbersArrayList.add(new Numbers("1100", "Apem ɔha"));
        numbersArrayList.add(new Numbers("1101", "Apem ɔha ne baako"));
        numbersArrayList.add(new Numbers("1102", "Apem ɔha ne mmienu"));
        numbersArrayList.add(new Numbers("1103", "Apem ɔha ne mmiɛnsa"));
        numbersArrayList.add(new Numbers("1104", "Apem ɔha ne nan"));
        numbersArrayList.add(new Numbers("1105", "Apem ɔha ne num"));

        numbersArrayList.add(new Numbers("1200", "Apem ahanu"));
        numbersArrayList.add(new Numbers("1201", "Apem ahanu ne baako"));
        numbersArrayList.add(new Numbers("1202", "Apem ahanu ne mmienu"));
        numbersArrayList.add(new Numbers("1203", "Apem ahanu ne mmiɛnsa"));
        numbersArrayList.add(new Numbers("1204", "Apem ahanu ne nan"));
        numbersArrayList.add(new Numbers("1205", "Apem ahanu ne num"));

        numbersArrayList.add(new Numbers("1300", "Apem ahasa"));
        numbersArrayList.add(new Numbers("1400", "Apem ahanan"));
        numbersArrayList.add(new Numbers("1500", "Apem ahanum"));
        numbersArrayList.add(new Numbers("1600", "Apem ahansia"));
        numbersArrayList.add(new Numbers("1700", "Apem ahanson"));
        numbersArrayList.add(new Numbers("1800", "Apem ahanwɔtwe"));
        numbersArrayList.add(new Numbers("1900", "Apem ahankron"));
        numbersArrayList.add(new Numbers("2000", "Mpem mmienu"));

        numbersArrayList.add(new Numbers("2001", "Mpem mmienu ne baako"));
        numbersArrayList.add(new Numbers("2002", "Mpem mmienu ne mmienu"));
        numbersArrayList.add(new Numbers("2010", "Mpem mmienu ne du"));
        numbersArrayList.add(new Numbers("2100", "Mpem mmienu ne ɔha"));
        numbersArrayList.add(new Numbers("2101", "Mpem mmienu ɔha ne baako"));
        numbersArrayList.add(new Numbers("2201", "Mpem mmienu ahanu ne baako"));
        numbersArrayList.add(new Numbers("2301", "Mpem mmienu ahasa ne baako"));

        numbersArrayList.add(new Numbers("3000", "Mpem mmiɛnsa"));
        numbersArrayList.add(new Numbers("4000", "Mpem nan"));
        numbersArrayList.add(new Numbers("5000", "Mpem num"));
        numbersArrayList.add(new Numbers("6000", "Mpem nsia"));
        numbersArrayList.add(new Numbers("7000", "Mpem nson"));
        numbersArrayList.add(new Numbers("8000", "Mpem nwɔtwe"));
        numbersArrayList.add(new Numbers("9000", "Mpem nkron"));
        numbersArrayList.add(new Numbers("10000", "Mpem du"));

        numbersArrayList.add(new Numbers("10001", "Mpem du ne baako"));
        numbersArrayList.add(new Numbers("10100", "Mpem du ne ɔha"));
        numbersArrayList.add(new Numbers("10101", "Mpem du, ɔha ne baako"));
        numbersArrayList.add(new Numbers("10200", "Mpem du ne ahanu"));
        numbersArrayList.add(new Numbers("10201", "Mpem du, ahanu ne baako"));
        numbersArrayList.add(new Numbers("10300", "Mpem du ne ahasa"));

        numbersArrayList.add(new Numbers("11000", "Mpem dubaako"));
        numbersArrayList.add(new Numbers("11001", "Mpem dubaako ne baako"));
        numbersArrayList.add(new Numbers("11011", "Mpem dubaako ne dubaako"));
        numbersArrayList.add(new Numbers("11121", "Mpem dubaako, ɔha ne aduonu baako"));

        numbersArrayList.add(new Numbers("12000", "Mpem dummienu"));
        numbersArrayList.add(new Numbers("13000", "Mpem dummiɛnsa"));
        numbersArrayList.add(new Numbers("14000", "Mpem dunan"));
        numbersArrayList.add(new Numbers("15000", "Mpem dunum"));
        numbersArrayList.add(new Numbers("16000", "Mpem dunsia"));
        numbersArrayList.add(new Numbers("17000", "Mpem dunson"));
        numbersArrayList.add(new Numbers("18000", "Mpem dunwɔtwe"));
        numbersArrayList.add(new Numbers("19000", "Mpem dunkron"));
        numbersArrayList.add(new Numbers("20000", "Mpem aduonu"));

        numbersArrayList.add(new Numbers("30000", "Mpem aduasa"));
        numbersArrayList.add(new Numbers("40000", "Mpem aduanan"));
        numbersArrayList.add(new Numbers("50000", "Mpem aduonum"));
        numbersArrayList.add(new Numbers("60000", "Mpem aduosia"));
        numbersArrayList.add(new Numbers("70000", "Mpem aduoson"));
        numbersArrayList.add(new Numbers("80000", "Mpem aduowɔtwe"));
        numbersArrayList.add(new Numbers("90000", "Mpem aduokron"));
        numbersArrayList.add(new Numbers("100 000", "Mpem ɔha"));


        numbersArrayList.add(new Numbers("1,000,000", "Ɔpepem baako"));
        numbersArrayList.add(new Numbers("2 000 000", "Ɔpepem mmienu"));
        numbersArrayList.add(new Numbers("10 000 000", "Ɔpepem du"));

        numbersArrayList.add(new Numbers("100 000 000", "Ɔpepem ɔha"));

        numbersArrayList.add(new Numbers("1 000 000 000", "Ɔpepepem baako"));

//PRONOUNS ARRAY LIST

        pronounsArrayList = new ArrayList<>();

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


        //TIME ARRAY LIST

        timeArrayList= new ArrayList<Time>();

        timeArrayList.add(new Time("Second","Anibu"));
        timeArrayList.add(new Time(" One second","Anibu baako"));
        timeArrayList.add(new Time(" Two seconds","Anibu mmienu"));


        timeArrayList.add(new Time("Minute (1)","Simma"));
        timeArrayList.add(new Time("Minute (2)","Miniti"));
        timeArrayList.add(new Time("One minute","Simma baako"));
        timeArrayList.add(new Time("Two minutes","Simma mmienu"));



        timeArrayList.add(new Time("Hour","Dɔnhwere"));
        timeArrayList.add(new Time("Hours","Nnɔnhwere"));
        timeArrayList.add(new Time("1 hour","Dɔnhwere baako"));
        timeArrayList.add(new Time("2 hours","Nnɔnhwere mmienu"));

        timeArrayList.add(new Time("Day","Da"));
        timeArrayList.add(new Time("Days","Nna"));
        timeArrayList.add(new Time("One day","Da koro"));
        timeArrayList.add(new Time("Two days","Nnanu"));
        timeArrayList.add(new Time("Three days","Nnansa"));
        timeArrayList.add(new Time("Four days","Nnanan"));
        timeArrayList.add(new Time("Five days","Nnanum"));
        timeArrayList.add(new Time("Six days","Nnansia"));
        timeArrayList.add(new Time("Seven days","Nnanson"));

        timeArrayList.add(new Time("First day","Da a edi kan"));
        timeArrayList.add(new Time("Second day","Da a ɛtɔ so mmienu"));
        timeArrayList.add(new Time("Third day","Da a ɛtɔ so mmiɛnsa"));

        timeArrayList.add(new Time("Week (1)","Nnawɔtwe"));
        timeArrayList.add(new Time("Week (2)","Dapɛn"));
        timeArrayList.add(new Time("Weeks (1)","Nnawɔtwe"));
        timeArrayList.add(new Time("Weeks (2)","Adapɛn"));
        timeArrayList.add(new Time("First week","Nnawɔtwe a edi kan"));
        timeArrayList.add(new Time("Second week","Nnawɔtwe a ɛtɔ so mmienu"));
        timeArrayList.add(new Time("Third week","Nnawɔtwe a ɛtɔ so mmiɛnsa"));
        timeArrayList.add(new Time("Fortnight","Nnawɔtwe mmienu"));
        timeArrayList.add(new Time("Next week","Nnawɔtwe a edi hɔ"));
        timeArrayList.add(new Time("Last week","Nnawɔtwe a etwaam"));
        timeArrayList.add(new Time("Last two weeks","Nnawɔtwe mmienu a etwaam"));
        timeArrayList.add(new Time("This week","Nnawɔtwe yi"));

        timeArrayList.add(new Time("Month (1)","Ɔsram"));
        timeArrayList.add(new Time("Month (2)","Bosome"));
        timeArrayList.add(new Time("Months","Abosome"));
        timeArrayList.add(new Time("This Month","Bosome yi"));
        timeArrayList.add(new Time("Last Month","Bosome a etwaam"));
        timeArrayList.add(new Time("First Month (2)","Bosome a edi kan"));
        timeArrayList.add(new Time("Second Month (2)","Bosome a ɛtɔ so mmienu"));

        timeArrayList.add(new Time("Year","Afe"));
        timeArrayList.add(new Time("Years","Mfe"));
        timeArrayList.add(new Time("This year","Afe yi"));
        timeArrayList.add(new Time("Last year","Afe a etwaam"));
        timeArrayList.add(new Time("A year by this time","Afe sesɛɛ"));
        timeArrayList.add(new Time("Next year (1)","Afedan"));
        timeArrayList.add(new Time("Next year (2)","Afe a yebesi mu"));


        timeArrayList.add(new Time("Today","Nnɛ"));
        timeArrayList.add(new Time("Yesterday","Ɛnnora"));
        timeArrayList.add(new Time("Tomorrow","Ɔkyena"));

        timeArrayList.add(new Time("When","Bere bɛn"));
        timeArrayList.add(new Time("What is the time?","Abɔ sɛn?"));
        timeArrayList.add(new Time("What time is it?","Abɔ sɛn?"));



        timeArrayList.add(new Time("Morning","Anɔpa"));
        timeArrayList.add(new Time("This morning","Anɔpa yi"));

        timeArrayList.add(new Time("Afternoon","Awia"));
        timeArrayList.add(new Time("This afternoon","Awia yi"));

        timeArrayList.add(new Time("Evening","Anwummere"));
        timeArrayList.add(new Time("This evening","Anwummere yi"));

        timeArrayList.add(new Time("Night","Anadwo"));
        timeArrayList.add(new Time("This night","Anadwo yi"));

        timeArrayList.add(new Time("Midnight","Dasuom"));

        timeArrayList.add(new Time("Dawn","Anɔpatutu"));


        timeArrayList.add(new Time("1 am","Anɔpa Dɔnkoro"));
        timeArrayList.add(new Time("1:01","Dɔnkoro apa ho simma baako"));
        timeArrayList.add(new Time("1:05","Dɔnkoro apa ho simma num"));
        timeArrayList.add(new Time("1:15","Dɔnkoro apa ho simma dunum"));



        timeArrayList.add(new Time("2 am","Anɔpa Nnɔnmmienu"));
        timeArrayList.add(new Time("2:30 (1)","Nnɔnmmienu ne fa"));
        timeArrayList.add(new Time("2:30 (2)","Nnɔnmmienu apa ho simma aduasa"));


        timeArrayList.add(new Time("3 am","Anɔpa Nnɔnmmiɛnsa "));

        timeArrayList.add(new Time("4 am","Anɔpa Nnɔnnan"));
        timeArrayList.add(new Time("5 am","Anɔpa Nnɔnnum"));
        timeArrayList.add(new Time("6 am","Anɔpa Nnɔnsia"));
        timeArrayList.add(new Time("7 am","Anɔpa Nnɔnson"));
        timeArrayList.add(new Time("8 am","Anɔpa Nnɔnwɔtwe"));
        timeArrayList.add(new Time("9 am","Anɔpa Nnɔnkron"));
        timeArrayList.add(new Time("10 am","Anɔpa Nnɔndu"));
        timeArrayList.add(new Time("11 am","Anɔpa Nnɔndubaako"));
        timeArrayList.add(new Time("12 am","Nnɔndummienu"));
        timeArrayList.add(new Time("Noon (1)","Nnɔndummienu"));
        timeArrayList.add(new Time("Noon (2)","Owigyinae"));
        timeArrayList.add(new Time("1 pm","Awia Dɔnkoro"));
        timeArrayList.add(new Time("2 pm","Awia Nnɔnmmienu"));
        timeArrayList.add(new Time("3 pm","Awia Nnɔnmmiɛnsa"));
        timeArrayList.add(new Time("4 pm","Anwummere Nnɔnnan"));
        timeArrayList.add(new Time("5 pm","Anwummere Nnɔnnum"));
        timeArrayList.add(new Time("6 pm","Anwummere Nnɔnsia"));
        timeArrayList.add(new Time("7 pm","Anwummere Nnɔnson"));
        timeArrayList.add(new Time("8 pm","Anadwo Nnɔnwɔtwe"));
        timeArrayList.add(new Time("9 pm","Anadwo Nnɔnkron"));
        timeArrayList.add(new Time("10 pm","Anadwo Nnɔndu"));
        timeArrayList.add(new Time("11 pm","Anadwo Nnɔndubaako"));
        timeArrayList.add(new Time("12 pm (1)","Anadwo dummienu"));
        timeArrayList.add(new Time("12 pm (2)","Dasuom"));


        //WEATHER ARRAY LIST

        weatherArray= new ArrayList<>();

        weatherArray.add(new Weather("Rain","Osu"));
        weatherArray.add(new Weather("It is raining","Osu retɔ"));
        weatherArray.add(new Weather("Will it rain today?","Osu bɛtɔ nnɛ anaa?"));
        weatherArray.add(new Weather("Did it rain yesterday?","Osu tɔɔ ɛnnora anaa?"));
        weatherArray.add(new Weather("It will rain today","Osu bɛtɔ nnɛ"));
        weatherArray.add(new Weather("Umbrella","Kyinneɛ"));
        weatherArray.add(new Weather("Take the umbrella","Fa kyinneɛ no"));
        weatherArray.add(new Weather("It is drizzling","Osu repetepete"));

        weatherArray.add(new Weather("Snow","Sukyerɛmma"));

        weatherArray.add(new Weather("Sun","Awia"));
        weatherArray.add(new Weather("It is sunny","Awia abɔ"));
        weatherArray.add(new Weather("It is very sunny","Awia abɔ paa"));
        weatherArray.add(new Weather("It is hot today","Ewiem ayɛ hye nnɛ"));
        weatherArray.add(new Weather("Sunset","Owitɔe"));
        weatherArray.add(new Weather("The sun has set","Owia no akɔtɔ"));
        weatherArray.add(new Weather("Sunrise","Owipue"));
        weatherArray.add(new Weather("The sun is rising","Owia no repue"));
        weatherArray.add(new Weather("The sun has risen","Owia no apue"));

        weatherArray.add(new Weather("It is warm","Ahohuru wom"));
        weatherArray.add(new Weather("I am feeling hot","Ɔhyew de me"));
        weatherArray.add(new Weather("I am feeling warm","Ahohuru de me"));
        weatherArray.add(new Weather("It is cold","Awɔw wom"));
        weatherArray.add(new Weather("It is very cold","Awɔw wom paa"));
        weatherArray.add(new Weather("I am feeling cold","Awɔw de me"));
        weatherArray.add(new Weather("It is very chilly","Awɔw wom paa"));

        weatherArray.add(new Weather("Lightning","Anyinam"));
        weatherArray.add(new Weather("Thunder","Apranaa"));
        weatherArray.add(new Weather("Cloud","Mununkum"));
        weatherArray.add(new Weather("Rain clouds have formed","Osu amuna"));
        weatherArray.add(new Weather("It is cloudy","Ewiem ayɛ kusuu"));
        weatherArray.add(new Weather("Wind","Mframa"));
        weatherArray.add(new Weather("It is windy","Mframa rebɔ"));
        weatherArray.add(new Weather("It is very windy","Mframa rebɔ paa"));

        weatherArray.add(new Weather("Sky (1)","Soro"));
        weatherArray.add(new Weather("Sky (2)","Ewiem"));
        weatherArray.add(new Weather("Weather","Ewiem tebea"));
        weatherArray.add(new Weather("How is the weather like?","Ewiem tebea te sɛn?"));
        weatherArray.add(new Weather("What will the weather be like today?","Ɛnnɛ ewiem tebea bɛyɛ sɛn?"));
        weatherArray.add(new Weather("Weather changes","Ewiem nsakrae"));
        weatherArray.add(new Weather("Stars","Nsoromma"));
        weatherArray.add(new Weather("The stars are glittering","Nsoromma no rehyerɛn"));

        weatherArray.add(new Weather("Misty","Ɛbɔ"));
        weatherArray.add(new Weather("It is misty","Ɛbɔ asi"));

        weatherArray.add(new Weather("Harmattan","Ɔpɛ"));
        weatherArray.add(new Weather("Harmattan is here","Ɔpɛ asi"));

        weatherArray.add(new Weather("Shade","Onwunu"));
        weatherArray.add(new Weather("Storm","Ahum"));
        weatherArray.add(new Weather("Rainbow","Nyankontɔn"));
        weatherArray.add(new Weather("I saw the rainbow","Me huu nyankontɔn no"));


        weatherArray.add(new Weather("Moon","Bosome"));





        /////////////////////////////////ALL ARRAY LIST BEGIN///////////////////////////

        allArrayList = new ArrayList<>();

        //AllBusiness

        allArrayList.add(new All("Money","Sika","", "", "",""));
        allArrayList.add(new All("I don't have money","Menni sika","", "", "",""));
        allArrayList.add(new All("I have money","Mewɔ sika","", "", "",""));
        allArrayList.add(new All("Bank","Sikakorabea","", "", "",""));
        allArrayList.add(new All("Buy","Tɔ","", "", "",""));
        allArrayList.add(new All("What are you buying?","Woretɔ dɛn?","", "", "",""));
        allArrayList.add(new All("What should I buy?","Dɛn na mentɔ?","", "", "",""));
        allArrayList.add(new All("What would you like to buy?","Dɛn na wopɛ sɛ wotɔ?","", "", "",""));
        allArrayList.add(new All("Buy for me","Tɔ ma me","", "", "",""));
        allArrayList.add(new All("I am buying...","Meretɔ...","", "", "",""));
        allArrayList.add(new All("I am buying food","Meretɔ aduane","", "", "",""));
        allArrayList.add(new All("I will buy","Mɛtɔ","", "", "",""));
        allArrayList.add(new All("I will buy food","Mɛtɔ aduane","", "", "",""));
        allArrayList.add(new All("I will not buy","Me ntɔ","", "", "",""));
        allArrayList.add(new All("I will not buy food","Me ntɔ aduane","", "", "",""));
        allArrayList.add(new All("How much will you buy?","Wobɛtɔ sɛn?","", "", "",""));
        allArrayList.add(new All("Where can I buy...?","Ɛhe na menya...atɔ?","", "", "",""));
        allArrayList.add(new All("Where can I buy water?","Ɛhe na menya nsuo atɔ?","", "", "",""));
        allArrayList.add(new All("I want to buy..","Mepɛ sɛ metɔ...","", "", "",""));
        allArrayList.add(new All("How many do you want to buy?","Ahe na wopɛ sɛ wotɔ?","", "", "",""));
        allArrayList.add(new All("I will buy two?","Mɛtɔ mmienu","", "", "",""));


        allArrayList.add(new All("It is expensive","Ne bo yɛ den","", "", "",""));
        allArrayList.add(new All("It is too expensive","Ne bo yɛ den dodo","", "", "",""));
        allArrayList.add(new All("I want discount","Mepɛ ntesoɔ","", "", "",""));
        allArrayList.add(new All("Give me discount","Te me so","", "", "",""));

        allArrayList.add(new All("Please reduce the price a little","Mesrɛ wo te so kakra","", "", "",""));
        allArrayList.add(new All("I want you to give to me on credit","Mepɛ sɛ wode firi me","", "", "",""));


        allArrayList.add(new All("It is cheap","Ɛyɛ fo","", "", "",""));
        allArrayList.add(new All("It is too cheap","Ɛyɛ fo dodo","", "", "",""));
        allArrayList.add(new All("My change","Me nsesa","", "", "",""));


        allArrayList.add(new All("Sell","Tɔn","", "", "",""));
        allArrayList.add(new All("I want to sell..","Mepɛ sɛ metɔn...","", "", "",""));
        allArrayList.add(new All("I want to sell clothes","Mepɛ sɛ metɔn ntade","", "", "",""));
        allArrayList.add(new All("How much is it?","Ɛyɛ sɛn","", "", "",""));
        allArrayList.add(new All("It is fifty cedis?","Ɛyɛ cedis aduonum","", "", "",""));
        allArrayList.add(new All("How much is this?","Wei yɛ sɛn?","", "", "",""));
        allArrayList.add(new All("Do you sell...here?","Wotɔn...wɔ ha?","", "", "",""));
        allArrayList.add(new All("Do you sell food here?","Wotɔn aduane wɔ ha?","", "", "",""));

        allArrayList.add(new All("How much do you sell it?","Wotɔn no sɛn?","", "", "",""));
        allArrayList.add(new All("Market (1)","Gua","", "", "",""));
        allArrayList.add(new All("Market (2)","Dwaso","", "", "",""));
        allArrayList.add(new All("I am going to the market","Merekɔ dwaso","", "", "",""));


        allArrayList.add(new All("Give me money","Ma me sika","", "", "",""));
        allArrayList.add(new All("Pay me","Tua me","", "", "",""));
        allArrayList.add(new All("Pay","Tua","", "", "",""));
        allArrayList.add(new All("Pay for me","Tua ma me","", "", "",""));
        allArrayList.add(new All("Pay for it","Tua ka","", "", "",""));
        allArrayList.add(new All("Can I pay tomorrow?","Metumi atua no ɔkyena?","", "", "",""));
        allArrayList.add(new All("I can't pay","Mentumi ntua","", "", "",""));
        allArrayList.add(new All("Who is selling?","Hena na ɔretɔn?","", "", "",""));

        allArrayList.add(new All("Work","Adwuma","", "", "",""));
        allArrayList.add(new All("Shop","Sotɔɔ","", "", "",""));
        allArrayList.add(new All("Profit","Mfasoɔ","", "", "",""));
        allArrayList.add(new All("I have made profit","Manya mfasoɔ","", "", "",""));
        allArrayList.add(new All("I will make profit","Mɛnya mfasoɔ","", "", "",""));
        allArrayList.add(new All("Loss","Ɛka","", "", "",""));
        allArrayList.add(new All("I have made a loss","Mabɔ ka","", "", "",""));
        allArrayList.add(new All("I will make a loss","Mɛbɔ ka","", "", "",""));












        //AllAnimals

        allArrayList.add(new All("Bull","Nantwinini", "", "","",""));
        allArrayList.add(new All("Animal","Aboa", "", "","",""));
        allArrayList.add(new All("Animals","Mmoa", "", "","",""));
        allArrayList.add(new All("Cow","Nantwibere","","", "",""));
        allArrayList.add(new All("Dog","Kraman","", "", "",""));
        allArrayList.add(new All("Cat (1)","Ɔkra","", "", "",""));
        allArrayList.add(new All("Cat (2)","Agyinamoa","", "", "",""));
        allArrayList.add(new All("Donkey","Afurum","", "", "",""));
        allArrayList.add(new All("Horse","Pɔnkɔ","", "", "",""));
        allArrayList.add(new All("Lamb","Oguammaa","", "", "",""));
        allArrayList.add(new All("Pig","Prako","", "", "",""));
        allArrayList.add(new All("Rabbit","Adanko","", "", "",""));
        allArrayList.add(new All("Sheep","Odwan","", "", "",""));
        allArrayList.add(new All("Bat","Ampan","", "", "",""));
        allArrayList.add(new All("Crocodile","Ɔdɛnkyɛm","", "", "",""));
        allArrayList.add(new All("Deer","Ɔforote","", "", "",""));
        allArrayList.add(new All("Elephant","Ɔsono","", "", "",""));
        allArrayList.add(new All("Hippopotamus","Susono","", "", "",""));
        allArrayList.add(new All("Hyena","Pataku","", "", "",""));
        allArrayList.add(new All("Wolf (1)","Pataku","", "", "",""));
        allArrayList.add(new All("Wolf (2)","Sakraman","", "", "",""));
        allArrayList.add(new All("Leopard","Ɔsebɔ","", "", "",""));
        allArrayList.add(new All("Lion","Gyata","", "", "",""));
        allArrayList.add(new All("Rat","Kusie","", "", "",""));
        allArrayList.add(new All("Snake","Ɔwɔ","", "", "",""));
        allArrayList.add(new All("Duck","Dabodabo","", "", "",""));


        //,"", "", "","")

        allArrayList.add(new All("Bear","Sisire","", "", "",""));
        allArrayList.add(new All("Chameleon","Abosomakoterɛ","", "", "",""));
        allArrayList.add(new All("Lizard","Koterɛ","", "", "",""));
        allArrayList.add(new All("Mouse","Akura","", "", "",""));
        allArrayList.add(new All("Tortoise","Akyekyedeɛ","", "", "",""));
        allArrayList.add(new All("Centipede","Sakasaka","", "", "",""));
        allArrayList.add(new All("Millipede","Kankabi","", "", "",""));
        allArrayList.add(new All("Crab","Kɔtɔ","", "", "",""));
        allArrayList.add(new All("Camel","Yoma","", "", "",""));
        allArrayList.add(new All("Fowl","Akokɔ","", "", "",""));
        allArrayList.add(new All("Bird","Anomaa","", "", "",""));
        allArrayList.add(new All("Scorpion","Akekantwɛre","", "", "",""));
        allArrayList.add(new All("Cockroach","Tɛfrɛ","", "", "",""));
        allArrayList.add(new All("Ants","Tɛtea","", "", "",""));
        allArrayList.add(new All("Locust (1)","Ntutummɛ","", "", "",""));
        allArrayList.add(new All("Locust (2)","Mmoadabi","", "", "",""));
        allArrayList.add(new All("Goat (1)","Apɔnkye","", "", "",""));
        allArrayList.add(new All("Goat (2)","Abirekyie","", "", "",""));
        allArrayList.add(new All("Tiger","Ɔsebɔ","", "", "",""));
        allArrayList.add(new All("Butterfly","Afofantɔ","", "", "",""));
        allArrayList.add(new All("Grasscutter","Akranteɛ","", "", "",""));
        allArrayList.add(new All("Lice","Edwie","", "", "",""));
        allArrayList.add(new All("Porcupine","Kɔtɔkɔ","", "", "",""));
        allArrayList.add(new All("Hedgehog (1)","Apɛsɛ","", "", "",""));
        allArrayList.add(new All("Hedgehog (2)","Apɛsɛe","", "", "",""));
        allArrayList.add(new All("Whale","Bonsu","", "", "",""));
        allArrayList.add(new All("Shark","Oboodede","", "", "",""));
        allArrayList.add(new All("Mosquito","Ntontom","", "", "",""));
        allArrayList.add(new All("Grasshopper","Abɛbɛ","", "", "",""));
        allArrayList.add(new All("Bedbug","Nsonkuronsuo","", "", "",""));
        allArrayList.add(new All("Squirrel","Opuro","", "", "",""));
        allArrayList.add(new All("Alligator","Ɔmampam","", "", "",""));
        allArrayList.add(new All("Buffalo","Ɛkoɔ","", "", "",""));
        allArrayList.add(new All("Worm","Sonsono","", "", "",""));
        allArrayList.add(new All("Cattle","Nantwie","", "", "",""));

        allArrayList.add(new All("Tsetsefly","Ohurii","", "", "",""));
        allArrayList.add(new All("Red Tree Ant","Nhohoa","", "", "",""));
        allArrayList.add(new All("Driver Ants","Nkrane","", "", "",""));
        allArrayList.add(new All("Praying Mantis","Akokromfi","", "", "",""));
        allArrayList.add(new All("House fly","Nwansena","", "", "",""));
        allArrayList.add(new All("Beetle","Ɔbankuo","", "", "",""));



        allArrayList.add(new All("Vulture (1)","Pɛtɛ","", "", "",""));
        allArrayList.add(new All("Vulture (2)","Kɔkɔsakyi","", "", "",""));
        allArrayList.add(new All("Hawk","Akorɔma","", "", "",""));
        allArrayList.add(new All("Guinea Fowl","Akɔmfɛm","", "", "",""));
        allArrayList.add(new All("Monkey","Adoe","", "", "",""));
        allArrayList.add(new All("Parrot","Akoo","", "", "",""));
        allArrayList.add(new All("Crow","Kwaakwaadabi","", "", "",""));
        allArrayList.add(new All("Owl","Patuo","", "", "",""));
        allArrayList.add(new All("Eagle","Ɔkɔre","", "", "",""));
        allArrayList.add(new All("Sparrow","Akasanoma","", "", "",""));
        allArrayList.add(new All("Swallow","Asomfena","", "", "",""));
        allArrayList.add(new All("Dove","Aborɔnoma","", "", "",""));


        allArrayList.add(new All("Bee","Wowa","", "", "",""));
        allArrayList.add(new All("Herring","Ɛmane","", "", "",""));
        allArrayList.add(new All("Lobster","Ɔbɔnkɔ","", "", "",""));
        allArrayList.add(new All("Lobsters","Mmɔnkɔ","", "", "",""));

        Collections.sort(allArrayList);

        allArrayList.add(new All("Which animal?","Aboa bɛn?","", "", "","")) ;
        allArrayList.add(new All("Which animal is this?","Aboa bɛn ni?","", "", "","")) ;
        allArrayList.add(new All("It is a lion","Ɛyɛ gyata","", "", "","")) ;


        //BodyParts

        allArrayList.add(new All("Hand","Nsa","", "", "",""));
        allArrayList.add(new All("Finger","Nsateaa","", "", "",""));

        allArrayList.add(new All("Leg","Nan","", "", "",""));
        allArrayList.add(new All("Nose","Hwene","", "", "",""));
        allArrayList.add(new All("Head","Eti","", "", "",""));
        allArrayList.add(new All("Mouth","Ano","", "", "",""));
        allArrayList.add(new All("Gum","Ɛse akyi nam","", "", "",""));

        allArrayList.add(new All("Cheek","Afono","", "", "",""));
        allArrayList.add(new All("Teeth","Ɛse","", "", "",""));
        allArrayList.add(new All("Tongue","Tɛkrɛma","", "", "",""));
        allArrayList.add(new All("Eyebrow (1)","Ani akyi nhwi","", "", "",""));
        allArrayList.add(new All("Eyebrow (2)","Ani ntɔn nhwi","", "", "",""));
        allArrayList.add(new All("Eyelashes (1)","Anisoatɛtɛ","", "", "",""));
        allArrayList.add(new All("Eyelashes (2)","Ani ntɔn","", "", "",""));

        allArrayList.add(new All("Hair","Nhwi","", "", "",""));
        allArrayList.add(new All("Forehead","Moma","", "", "",""));
        allArrayList.add(new All("Eyeball","Ani kosua","", "", "",""));
        allArrayList.add(new All("Chin","Abɔdwe","", "", "",""));
        allArrayList.add(new All("Beard","Abɔdwesɛ nhwi","", "", "",""));
        allArrayList.add(new All("Moustache (1)","Ano ho nhwi","", "", "",""));
        allArrayList.add(new All("Moustache (1)","Mfemfem","", "", "",""));

        allArrayList.add(new All("Human","Nipa","", "", "",""));
        allArrayList.add(new All("Body","Nipadua","", "", "",""));
        allArrayList.add(new All("Neck","Kɔn","", "", "",""));
        allArrayList.add(new All("Chest (1)","Koko","", "", "",""));
        allArrayList.add(new All("Chest (2)","Bo","", "", "",""));

        allArrayList.add(new All("Navel","Afunuma","", "", "",""));
        allArrayList.add(new All("Stomach (1)","Yafunu","", "", "",""));
        allArrayList.add(new All("Stomach (2)","Yam","", "", "",""));

        allArrayList.add(new All("Ribs (1)","Mparow","", "", "",""));
        allArrayList.add(new All("Ribs (2)","Mfe mpade","", "", "",""));
        allArrayList.add(new All("Shoulder","Abati","", "", "",""));
        allArrayList.add(new All("Palm","Nsayam","", "", "",""));
        allArrayList.add(new All("Knee","Kotodwe","", "", "",""));
        allArrayList.add(new All("Intestine","Nsono","", "", "",""));
        allArrayList.add(new All("Lung","Ahrawa","", "", "",""));

        allArrayList.add(new All("Armpit","Mmɔtoam","", "", "",""));
        allArrayList.add(new All("Bone","Dompe","", "", "",""));
        allArrayList.add(new All("Breast","Nufuo","", "", "",""));
        allArrayList.add(new All("Heart","Koma","", "", "",""));

        allArrayList.add(new All("Brain (1)","Adwene","", "", "",""));
        allArrayList.add(new All("Brain (2)","Amemene","", "", "",""));
        allArrayList.add(new All("Fingernail","Mmɔwerɛ","", "", "",""));
        allArrayList.add(new All("Thumb","Kokuromoti","", "", "",""));
        allArrayList.add(new All("Arm","Abasa","", "", "",""));
        allArrayList.add(new All("Elbow","Abatwɛ","", "", "",""));
        allArrayList.add(new All("Vein","Ntini","", "", "",""));

        allArrayList.add(new All("Buttock","Ɛto","", "", "",""));
        allArrayList.add(new All("Bladder","Dwonsɔtwaa","", "", "",""));
        allArrayList.add(new All("Waist","Sisi","", "", "",""));
        allArrayList.add(new All("Womb (1)","Awode","", "", "",""));
        allArrayList.add(new All("Womb (2)","Awotwaa","", "", "",""));

        allArrayList.add(new All("Toe","Nansoaa","", "", "",""));
        allArrayList.add(new All("Wrist","Abakɔn","", "", "",""));
        allArrayList.add(new All("Heel","Nantin","", "", "",""));
        allArrayList.add(new All("Throat (1)","Mene","", "", "",""));
        allArrayList.add(new All("Throat (2)","Menemu","", "", "",""));
        allArrayList.add(new All("Thigh","Srɛ","", "", "",""));
        allArrayList.add(new All("Blood","Mogya","", "", "",""));
        allArrayList.add(new All("Calf","Nantu","", "", "",""));
        allArrayList.add(new All("Lips","Anofafa","", "", "",""));

        allArrayList.add(new All("Skull","Tikwankora","", "", "",""));
        allArrayList.add(new All("Skin","Honam","", "", "",""));

        allArrayList.add(new All("Liver","Berɛboɔ","", "", "",""));
        allArrayList.add(new All("Occiput","Atikɔ","", "", "",""));
        allArrayList.add(new All("Hip (1)","Dwonku","", "", "",""));
        allArrayList.add(new All("Hip (2)","Asen","", "", "",""));
        allArrayList.add(new All("Spine","Akyi berɛmo","", "", "",""));


//Pronouns



//1st Person subject

        allArrayList.add(new All("I","Me","1st Person Singular","Subject","", ""));
        allArrayList.add(new All("I am a boy","Me yɛ ɔbarima","","", "", ""));
        allArrayList.add(new All("I am a child","Me yɛ abofra","","", "", ""));

        allArrayList.add(new All("We","Yɛ(n)","1st Person Plural","Subject","", ""));
        allArrayList.add(new All("We are strong","Yɛn ho yɛ den","","", "", ""));
        allArrayList.add(new All("We will go there","Yɛbɛkɔ hɔ","","", "", ""));

//1st Person object

        allArrayList.add(new All("Me","Me","1st Person Singular","Object","", ""));
        allArrayList.add(new All("Give it to me","Fa ma me","","", "", ""));
        allArrayList.add(new All("You told me","Wo ka kyerɛɛ me","","", "", ""));


        allArrayList.add(new All("Us","Yɛn","1st Person Plural","Object","", ""));
        allArrayList.add(new All("You told us","Woka kyerɛɛ yɛn","","", "", ""));
        allArrayList.add(new All("They invited us","Wɔtoo nsa frɛɛ yɛn","","", "", ""));


//2nd Person subject
        allArrayList.add(new All("You","Wo","2nd Person Singular","Subject","", ""));
        allArrayList.add(new All("You are a boy","Woyɛ ɔbarima","","", "", ""));
        allArrayList.add(new All("You are beautiful","Wo ho yɛ fɛ","","", "", ""));

        allArrayList.add(new All("You","Mo","2nd Person Plural","Subject","", ""));
        allArrayList.add(new All("You are many","Mo dɔɔso","","", "", ""));
        allArrayList.add(new All("You are farmers","Mo yɛ akuafo","","", "", ""));


//2nd Person object

        allArrayList.add(new All("You","Wo","2nd Person Singular","Object","", ""));
        allArrayList.add(new All("I gave you money","Me de sika maa wo","","", "", ""));
        allArrayList.add(new All("She told you","Ɔka kyerɛɛ wo","","", "", ""));

        allArrayList.add(new All("You","Mo","2nd Person Plural","Object","", ""));
        allArrayList.add(new All("I saw all of you","Me huu mo nyinaa","","", "", ""));

//3rd Person subject

        allArrayList.add(new All("He","Ɔ-(no)","3rd Person Singular","Subject","", ""));
        allArrayList.add(new All("He gave it to you","Ɔde maa wo","","", "", ""));
        allArrayList.add(new All("She","Ɔ-(no)","3rd Person Singular","Subject","", ""));
        allArrayList.add(new All("She told you","Ɔka kyerɛɛ wo","","", "", ""));
        allArrayList.add(new All("It","Ɛ-(no)","3rd Person Singular","Subject","", ""));
        allArrayList.add(new All("It is nice","Ɛyɛ fɛ","","", "", ""));

        allArrayList.add(new All("They","Wɔ(n)","3rd Person Plural","Subject","", ""));
        allArrayList.add(new All("They gave it to you","Wɔde maa wo","","", "", ""));
        allArrayList.add(new All("They are strong","Wɔn ho yɛ den","","", "", ""));


//3rd Person object

        allArrayList.add(new All("Him","Ɔ-(no)","3rd Person Singular","Object","", ""));
        allArrayList.add(new All("I gave it to him","Me de maa no","","", "", ""));
        allArrayList.add(new All("It is him","Ɛyɛ ɔno","","", "", ""));
        allArrayList.add(new All("Her","Ɔ-(no)","3rd Person Singular","Object","", ""));
        allArrayList.add(new All("I gave it to her","Me de maa no","","", "", ""));
        allArrayList.add(new All("It is her","Ɛyɛ ɔno","","", "", ""));
        allArrayList.add(new All("It","Ɛ-(no)","3rd Person Singular","Object","", ""));
        allArrayList.add(new All("We killed it","Yekum no","","", "", ""));


        allArrayList.add(new All("Them","Wɔn","3rd Person Plural","Object","", ""));
        allArrayList.add(new All("Give it to them","Fa ma wɔn","","", "", ""));
        allArrayList.add(new All("Help them","Boa Wɔn","","", "", ""));


//Possessive All

        allArrayList.add(new All("Mine","Me dea","1st Person Singular","Possessive","",""));
        allArrayList.add(new All("Ours","Yɛn dea","1st Person Plural","Possessive","",""));

        allArrayList.add(new All("Yours","Wo dea","2nd Person Singular","Possessive","",""));
        allArrayList.add(new All("Yours","Mo dea","2nd Person Plural","Possessive","",""));


        allArrayList.add(new All("His","Ne dea","3rd Person Singular","Possessive","",""));
        allArrayList.add(new All("Hers","Ne dea","3rd Person Singular","Possessive","",""));
        allArrayList.add(new All("Theirs","Wɔn dea","3rd Person Plural","Possessive","",""));

        //



        //Months
        allArrayList.add(new All("January","Ɔpɛpɔn","","", "", ""));
        allArrayList.add(new All("We are in the month of January","Yɛwɔ Ɔpɛpɔn bosome mu","","", "", ""));
        allArrayList.add(new All("February","Ɔgyefoɔ","","", "", ""));
        allArrayList.add(new All("We will go to Ghana in February","Yɛbɛkɔ Ghana Ɔgyefoɔ bosome no mu","","", "", ""));
        allArrayList.add(new All("March","Ɔbɛnem","","", "", ""));
        allArrayList.add(new All("I will see you in March","Mehu wo wɔ Ɔbɛnem bosome no mu","","", "", ""));
        allArrayList.add(new All("April","Oforisuo","","", "", ""));
        allArrayList.add(new All("It often rains in April","Osu taa tɔ wɔ Oforisuo bosome no mu","","", "", ""));
        allArrayList.add(new All("May","Kotonimaa","","", "", ""));
        allArrayList.add(new All("June","Ayɛwohomumɔ","","", "", ""));
        allArrayList.add(new All("July","Kitawonsa","","", "", ""));
        allArrayList.add(new All("August","Ɔsanaa","","", "", ""));
        allArrayList.add(new All("I was born in the month of August","Wɔwoo me Ɔsanaa bosome no mu","","", "", ""));
        allArrayList.add(new All("September","Ɛbɔ","","", "", ""));
        allArrayList.add(new All("October","Ahinime","","", "", ""));
        allArrayList.add(new All("November","Obubuo","","", "", ""));
        allArrayList.add(new All("December","Ɔpɛnimma","","", "", ""));
        allArrayList.add(new All("It is often cold in December","Awɔw taa ba wɔ Ɔpɛnimma bosome no mu","","", "", ""));

        allArrayList.add(new All("Which month?","Bosome bɛn?","","", "", ""));


        //Food

        allArrayList.add(new All("Rice", "Ɛmo","","", "", ""));
        allArrayList.add(new All("Yam", "Bayerɛ","","", "", ""));
        allArrayList.add(new All("Plantain", "Bɔɔdeɛ","","", "", ""));
        allArrayList.add(new All("Cassava", "Bankye","","", "", ""));
        allArrayList.add(new All("Onion", "Gyeene","","", "", ""));
        allArrayList.add(new All("Salt", "Nkyene","","", "", ""));

//fruits
        allArrayList.add(new All("Fruit", "Aduaba","","", "", ""));
        allArrayList.add(new All("Apple", "Aprɛ","","", "", ""));
        allArrayList.add(new All("Banana", "Kwadu","","", "", ""));
        allArrayList.add(new All("Orange (1)", "Ankaa","","", "", ""));
        allArrayList.add(new All("Orange (2)", "Akutu","","", "", ""));
        allArrayList.add(new All("Pawpaw", "Borɔferɛ","","", "", ""));
        allArrayList.add(new All("Coconut", "Kube","","", "", ""));
        allArrayList.add(new All("Pear", "Paya","","", "", ""));
        allArrayList.add(new All("Tigernut", "Atadwe","","", "", ""));
        allArrayList.add(new All("Pineapple", "Aborɔbɛ","","", "", ""));
        allArrayList.add(new All("Ginger", "Akekaduro","","", "", ""));
        allArrayList.add(new All("Sugarcane", "Ahwedeɛ","","", "", ""));
        allArrayList.add(new All("Corn", "Aburo","","", "", ""));
        allArrayList.add(new All("Maize", "Aburo","","", "", ""));
        allArrayList.add(new All("Groundnut", "Nkateɛ","","", "", ""));
        allArrayList.add(new All("Peanut", "Nkateɛ","","", "", ""));
        allArrayList.add(new All("Palm fruit", "Abɛ","","", "", ""));


//Vegetables
        allArrayList.add(new All("Vegetable", "Atosodeɛ","","", "", ""));
        allArrayList.add(new All("Pepper", "Mako","","", "", ""));
        allArrayList.add(new All("Bean", "Adua","","", "", ""));
        allArrayList.add(new All("Okro", "Nkuruma","","", "", ""));
        allArrayList.add(new All("Garden eggs", "Nyaadewa","","", "", ""));
        allArrayList.add(new All("Tomato", "Ntoosi","","", "", ""));
        allArrayList.add(new All("Garlic", "Galik","","", "", ""));
        allArrayList.add(new All("Cucumber", "Ɛferɛ","","", "", ""));


        allArrayList.add(new All("Lobster", "Ɔbɔnkɔ","","", "", ""));
        allArrayList.add(new All("Cocoa", "Kokoo","","", "", ""));
        allArrayList.add(new All("Palm kernel", "Adwe","","", "", ""));
        allArrayList.add(new All("Palm kernel oil", "Adwe ngo","","", "", ""));
        allArrayList.add(new All("Vegetable oil", "Anwa","","", "", ""));
        allArrayList.add(new All("Snail", "Nwa","","", "", ""));
        allArrayList.add(new All("Groundnut soup", "Nkate nkwan","","", "", ""));
        allArrayList.add(new All("Palm nut soup", "Abɛ nkwan","","", "", ""));

//Others
        allArrayList.add(new All("Dough", "Mmɔre","","", "", ""));
        allArrayList.add(new All("Kenkey", "Dɔkono","","", "", ""));
        allArrayList.add(new All("Flour", "Esiam","","", "", ""));
        allArrayList.add(new All("Wheat", "Ayuo","","", "", ""));
        allArrayList.add(new All("Soup", "Nkwan","","", "", ""));
        allArrayList.add(new All("Stew", "Abomu","","", "", ""));
        allArrayList.add(new All("Egg", "Kosua","","", "", ""));
        allArrayList.add(new All("Bread (1)", "Paanoo","","", "", ""));
        allArrayList.add(new All("Bread (2)", "Burodo","","", "", ""));
        allArrayList.add(new All("Oil", "Ngo","","", "", ""));
        allArrayList.add(new All("Fish (1)", "Apataa","","", "", ""));
        allArrayList.add(new All("Fish (2)", "Nsuomnam","","", "", ""));
        allArrayList.add(new All("Pork", "Prakonam","","", "", ""));
        allArrayList.add(new All("Meat", "Nam","","", "", ""));
        allArrayList.add(new All("Mutton", "Odwannam","","", "", ""));
        allArrayList.add(new All("Lamb", "Odwannam","","", "", ""));
        allArrayList.add(new All("Sugar", "Asikyire","","", "", ""));
        allArrayList.add(new All("Honey", "Ɛwoɔ","","", "", ""));
        allArrayList.add(new All("Water", "Nsuo","","", "", ""));
        allArrayList.add(new All("Food", "Aduane","","", "", ""));

        Collections.sort(allArrayList);

        allArrayList.add(new All("I am hungry", "Ɛkɔm de me","","", "", ""));
        allArrayList.add(new All("Are you hungry?", "Ɛkɔm de wo anaa?","","", "", ""));
        allArrayList.add(new All("What will you eat?", "Dɛn na wobedi?","","", "", ""));
        allArrayList.add(new All("I will eat kenkey", "Medi dɔkono","","", "", ""));


        //FAMILY

        allArrayList.add(new All("Family", "Abusua","","", "", ""));
        allArrayList.add(new All("Families", "Mmusua","","", "", ""));

        allArrayList.add(new All("Father (1)", "Papa","","", "", ""));
        allArrayList.add(new All("Father (2)", "Agya","","", "", ""));
        allArrayList.add(new All("Father (3)", "Ɔse","","", "", ""));
        allArrayList.add(new All("My father (1)", "Me papa","","", "", ""));
        allArrayList.add(new All("My father (2)", "M'agya","","", "", ""));
        allArrayList.add(new All("My father (3)", "Me se","","", "", ""));
        allArrayList.add(new All("Daddy", "Dada","","", "", ""));

        allArrayList.add(new All("Mother (1)", "Maame","","", "", ""));
        allArrayList.add(new All("Mother (2)", "Ɛna","","", "", ""));
        allArrayList.add(new All("Mother (3)", "Oni","","", "", ""));
        allArrayList.add(new All("My Mother (1)", "Me maame","","", "", ""));
        allArrayList.add(new All("My Mother (2)", "Me na","","", "", ""));
        allArrayList.add(new All("My Mother (3)", "Me ni","","", "", ""));
        allArrayList.add(new All("Mummy", "Mama","","", "", ""));


        allArrayList.add(new All("Parent", "Ɔwofo","","", "", ""));
        allArrayList.add(new All("Parents", "Awofo","","", "", ""));
        allArrayList.add(new All("Child (1)", "Abofra","","", "", ""));
        allArrayList.add(new All("Child (2)", "Akwadaa","","", "", ""));
        allArrayList.add(new All("Children (1)", "Mma","","", "", ""));
        allArrayList.add(new All("Children (2)", "Mmofra","","", "", ""));
        allArrayList.add(new All("Baby", "Abofra","","", "", ""));

        allArrayList.add(new All("Firstborn (1)", "Abakan","","", "", ""));
        allArrayList.add(new All("Firstborn (2)", "Piesie","","", "", ""));
        allArrayList.add(new All("Lastborn", "Kaakyire","","", "", ""));

        allArrayList.add(new All("Husband", "Kunu","","", "", ""));
        allArrayList.add(new All("Husbands", "Kununom","","", "", ""));

        allArrayList.add(new All("Wife", "Yere","","", "", ""));
        allArrayList.add(new All("Wives", "Yerenom","","", "", ""));

        allArrayList.add(new All("Brother", "Nuabarima","","", "", ""));
        allArrayList.add(new All("Brothers", "Nua mmarima","","", "", ""));
        allArrayList.add(new All("Sister", "Nuabaa","","", "", ""));
        allArrayList.add(new All("Sisters", "Nua mmaa","","", "", ""));

        allArrayList.add(new All("Sibling", "Nua","","", "", ""));
        allArrayList.add(new All("Siblings", "Nuanom","","", "", ""));

        allArrayList.add(new All("Son", "Babarima","","", "", ""));
        allArrayList.add(new All("Sons", "mma mmarima","","", "", ""));
        allArrayList.add(new All("Daughter", "Babaa","","", "", ""));
        allArrayList.add(new All("Daughters", "Mma mmaa","","", "", ""));

        allArrayList.add(new All("Cousin", "Nua","","", "", ""));
        allArrayList.add(new All("Grandchild", "Banana","","", "", ""));
        allArrayList.add(new All("Great Grandchild", "Nanankanso","","", "", ""));
        allArrayList.add(new All("Grandfather", "Nanabarima","","", "", ""));
        allArrayList.add(new All("Great grandfather", "Nanabarima prenu","","", "", ""));
        allArrayList.add(new All("Grandmother", "Nanabaa","","", "", ""));
        allArrayList.add(new All("Great grandmother", "Nanabaa prenu","","", "", ""));

        allArrayList.add(new All("In-law", "Asew","","", "", ""));
        allArrayList.add(new All("Father-in-law", "Asebarima","","", "", ""));
        allArrayList.add(new All("Mother-in-law", "Asebaa","","", "", ""));
        allArrayList.add(new All("Brother-in-law", "Akonta","","", "", ""));
        allArrayList.add(new All("Sister-in-law", "Akumaa","","", "", ""));
        allArrayList.add(new All("Son-in-law (1)", "Asew","","", "", ""));
        allArrayList.add(new All("Son-in-law (2)", "Babaa kunu","","", "", ""));
        allArrayList.add(new All("Daughter-in-law", "Asew","","", "", ""));
        allArrayList.add(new All("Daughter-in-law", "Babarima yere","","", "", ""));


        allArrayList.add(new All("Maternal Uncle", "Wɔfa","","", "", ""));
        allArrayList.add(new All("Paternal Uncle (1)", "Papa","","", "", ""));
        allArrayList.add(new All("Paternal Uncle (2)", "Agya","","", "", ""));
        allArrayList.add(new All("Paternal Uncle (3)", "Papa nuabarima","","", "", ""));

        allArrayList.add(new All("Paternal Aunt", "Sewaa","","", "", ""));
        allArrayList.add(new All("My Paternal Aunt", "Me Sewaa","","", "", ""));

        allArrayList.add(new All("Maternal Aunt (1)", "Maame nuabaa","","", "", ""));
        allArrayList.add(new All("Maternal Aunt (2)", "Maame","","", "", ""));
        allArrayList.add(new All("My maternal Aunt", "Me maame nuabaa","","", "", ""));

        allArrayList.add(new All("Niece", "Wɔfaase baa","","", "", ""));
        allArrayList.add(new All("Nephew", "Wɔfaase barima","","", "", ""));
        allArrayList.add(new All("Niece/Nephew", "Wɔfaase","","", "", ""));

        allArrayList.add(new All("Cousin (1)", "Nua","","", "", ""));
        allArrayList.add(new All("Cousin (2)", "Wɔfa ba","","", "", ""));
        allArrayList.add(new All("Cousin (3)", "Sewaa ba","","", "", ""));
        allArrayList.add(new All("My Cousin", "Me nua","","", "", ""));


        allArrayList.add(new All("Adopted child", "Abanoma","","", "", ""));
        allArrayList.add(new All("Orphan", "Agyanka","","", "", ""));
        allArrayList.add(new All("Widow", "Okunafo","","", "", ""));
        allArrayList.add(new All("Widower", "Barima kunafo","","", "", ""));

        allArrayList.add(new All("Marriage", "Awareɛ","","", "", ""));

        allArrayList.add(new All("Twins", "Ntafoɔ","","", "", ""));
        allArrayList.add(new All("Triplets", "Ahenasa","","", "", ""));
        allArrayList.add(new All("Quadruplets", "Ahenanan","","", "", ""));

        //COMMONEXPRESSIONSA

        allArrayList.add(new All("Good morning (1)","Maakye","","", "", ""));
        allArrayList.add(new All("Good morning (2)","Mema wo akye","","", "", ""));
        allArrayList.add(new All("Good afternoon (1)","Maaha","","", "", ""));
        allArrayList.add(new All("Good afternoon (2)","Mema wo aha","","", "", ""));
        allArrayList.add(new All("Good evening (1)","Maadwo","","", "", ""));
        allArrayList.add(new All("Good evening (2)","Mema wo adwo","","", "", ""));
        allArrayList.add(new All("Good night","Da yie","","", "", ""));

        allArrayList.add(new All("How are you?","Wo ho te sɛn?","","", "", ""));
        allArrayList.add(new All("How is your mother?","Wo maame ho te sɛn?","","", "", ""));
        allArrayList.add(new All("How is your wife?","Wo yere ho te sɛn?","","", "", ""));
        allArrayList.add(new All("I am fine","Me ho yɛ","","", "", ""));
        allArrayList.add(new All("How are they doing?","Wɔn ho te sɛn?","","", "", ""));
        allArrayList.add(new All("I am not feeling well (1)","Me ho mfa me","","", "", ""));
        allArrayList.add(new All("I am not feeling well (2)","Mente apɔ","","", "", ""));

        allArrayList.add(new All("She is fine","Ne ho yɛ","","", "", ""));
        allArrayList.add(new All("He is fine","Ne ho yɛ","","", "", ""));
        allArrayList.add(new All("They are fine","Wɔn ho yɛ","","", "", ""));
        allArrayList.add(new All("They are all fine","Wɔn nyinaa ho yɛ","","", "", ""));


        allArrayList.add(new All("I am happy to meet you","M'ani agye sɛ mahyia wo","","", "", ""));
        allArrayList.add(new All("Welcome","Akwaaba","","", "", ""));
        allArrayList.add(new All("I bid you welcome","Mema wo akwaaba","","", "", ""));
        allArrayList.add(new All("I am happy","M'ani agye","","", "", ""));
        allArrayList.add(new All("I am sad","Me werɛ ahow","","", "", ""));

        allArrayList.add(new All("Stop crying","Gyae su","","", "", ""));


        allArrayList.add(new All("Please (1)","Mepa wo kyɛw","","", "", ""));
        allArrayList.add(new All("Please (2)","Mesrɛ wo","","", "", ""));
        allArrayList.add(new All("Thank you (1)","Medaase","","", "", ""));
        allArrayList.add(new All("Thank you (2)","Me da wo ase","","", "", ""));
        allArrayList.add(new All("I miss you","Mafe wo","","", "", ""));
        allArrayList.add(new All("I will miss you","Mɛfe wo","","", "", ""));
        allArrayList.add(new All("We will miss you","Yɛbɛfe wo","","", "", ""));
        allArrayList.add(new All("We will miss you (Plural)","Yɛbɛfe mo","","", "", ""));
        allArrayList.add(new All("Love","Ɔdɔ","","", "", ""));
        allArrayList.add(new All("I love you","Me dɔ wo","","", "", ""));
        allArrayList.add(new All("We love you","Yɛdɔ mo","","", "", ""));
        allArrayList.add(new All("Do you love me?","Wo dɔ me?","","", "", ""));
        allArrayList.add(new All("Yes","Aane","","", "", ""));
        allArrayList.add(new All("No","Dabi","","", "", ""));
        allArrayList.add(new All("My name is Kwaku (1)","Yɛfrɛ me Kwaku","","", "", ""));
        allArrayList.add(new All("My name is Kwaku (2)","Me din de Kwaku","","", "", ""));
        allArrayList.add(new All("What is your name? (1)","Yɛfrɛ wo sɛn?","","", "", ""));
        allArrayList.add(new All("What is your name? (2)","Wo din de sɛn?","","", "", ""));

        allArrayList.add(new All("How old are you?","Wadi mfe sɛn?","","", "", ""));
        allArrayList.add(new All("I am 30 years old","Madi mfe aduasa","","", "", ""));
        allArrayList.add(new All("Call me","Frɛ me","","", "", ""));
        allArrayList.add(new All("Do you speak English?","Wo ka borɔfo?","","", "", ""));

        allArrayList.add(new All("I am sick","Me yare","","", "", ""));
        allArrayList.add(new All("I need money","Me hia sika","","", "", ""));
        allArrayList.add(new All("Do you have money","Wowɔ sika?","","", "", ""));

        allArrayList.add(new All("Congratulations","Ayekoo","","", "", ""));
        allArrayList.add(new All("See you soon","Ɛrenkyɛ mehu wo","","", "", ""));
        allArrayList.add(new All("Give me a hug","Yɛ me atuu","","", "", ""));


        //COLOURS

        allArrayList.add(new All("Colour (1)","Ahosu","","", "", ""));
        allArrayList.add(new All("Colour (2)","Kɔla","","", "", ""));
        allArrayList.add(new All("What colour is it?","Ɛyɛ kɔla bɛn?","","", "", ""));
        allArrayList.add(new All("Red","Kɔkɔɔ","","", "", ""));
        allArrayList.add(new All("Black","Tuntum","","", "", ""));
        allArrayList.add(new All("White (1)","Fitaa","","", "", ""));
        allArrayList.add(new All("White (2)","Fufuo","","", "", ""));
        allArrayList.add(new All("Green","Ahabammono","","", "", ""));
        allArrayList.add(new All("Blue","Bruu","","", "", ""));
        allArrayList.add(new All("Brown","Dodowee","","", "", ""));
        allArrayList.add(new All("Grey","Nsonso","","", "", ""));
        allArrayList.add(new All("Ash","Nsonso","","", "", ""));
        allArrayList.add(new All("Yellow","Akokɔsrade","","", "", ""));
        allArrayList.add(new All("Spotted","Nsisimu","","", "", ""));
        allArrayList.add(new All("Purple (1)","Beredum","","", "", ""));
        allArrayList.add(new All("Purple (2)","Afasebiri","","", "", ""));
        allArrayList.add(new All("Pink","Memen","","", "", ""));
        allArrayList.add(new All("Dark","Sum","","", "", ""));

        //DAYS OF WEEK

        allArrayList.add(new All("Monday", "Dwoada","","", "", ""));
        allArrayList.add(new All("Tuesday", "Benada","","", "", ""));
        allArrayList.add(new All("Wednesday", "Wukuada","","", "", ""));
        allArrayList.add(new All("Thursday", "Yawada","","", "", ""));
        allArrayList.add(new All("Friday", "Fiada","","", "", ""));
        allArrayList.add(new All("Saturday", "Memeneda","","", "", ""));
        allArrayList.add(new All("Sunday", "Kwasiada","","", "", ""));


        //TIME

        allArrayList.add(new All("Second","Anibu","","", "", ""));
        allArrayList.add(new All(" One second","Anibu baako","","", "", ""));
        allArrayList.add(new All(" Two seconds","Anibu mmienu","","", "", ""));


        allArrayList.add(new All("Minute (1)","Simma","","", "", ""));
        allArrayList.add(new All("Minute (2)","Miniti","","", "", ""));
        allArrayList.add(new All("One minute","Simma baako","","", "", ""));
        allArrayList.add(new All("Two minutes","Simma mmienu","","", "", ""));


        allArrayList.add(new All("Hour","Dɔnhwere","","", "", ""));
        allArrayList.add(new All("Hours","Nnɔnhwere","","", "", ""));
        allArrayList.add(new All("1 hour","Dɔnhwere baako","","", "", ""));
        allArrayList.add(new All("2 hours","Nnɔnhwere mmienu","","", "", ""));

        allArrayList.add(new All("Day","Da","","", "", ""));
        allArrayList.add(new All("Days","Nna","","", "", ""));
        allArrayList.add(new All("One day","Da koro","","", "", ""));
        allArrayList.add(new All("Two days","Nnanu","","", "", ""));
        allArrayList.add(new All("Three days","Nnansa","","", "", ""));
        allArrayList.add(new All("Four days","Nnanan","","", "", ""));
        allArrayList.add(new All("Five days","Nnanum","","", "", ""));
        allArrayList.add(new All("Six days","Nnansia","","", "", ""));
        allArrayList.add(new All("Seven days","Nnanson","","", "", ""));

        allArrayList.add(new All("First day","Da a edi kan","","", "", ""));
        allArrayList.add(new All("Second day","Da a ɛtɔ so mmienu","","", "", ""));
        allArrayList.add(new All("Third day","Da a ɛtɔ so mmiɛnsa","","", "", ""));

        allArrayList.add(new All("Week (1)","Nnawɔtwe","","", "", ""));
        allArrayList.add(new All("Week (2)","Dapɛn","","", "", ""));
        allArrayList.add(new All("Weeks (1)","Nnawɔtwe","","", "", ""));
        allArrayList.add(new All("Weeks (2)","Adapɛn","","", "", ""));
        allArrayList.add(new All("First week","Nnawɔtwe a edi kan","","", "", ""));
        allArrayList.add(new All("Second week","Nnawɔtwe a ɛtɔ so mmienu","","", "", ""));
        allArrayList.add(new All("Third week","Nnawɔtwe a ɛtɔ so mmiɛnsa","","", "", ""));
        allArrayList.add(new All("Fortnight","Nnawɔtwe mmienu","","", "", ""));
        allArrayList.add(new All("Next week","Nnawɔtwe a edi hɔ","","", "", ""));
        allArrayList.add(new All("Last week","Nnawɔtwe a etwaam","","", "", ""));
        allArrayList.add(new All("Last two weeks","Nnawɔtwe mmienu a etwaam","","", "", ""));
        allArrayList.add(new All("This week","Nnawɔtwe yi","","", "", ""));

        allArrayList.add(new All("Month (1)","Ɔsram","","", "", ""));
        allArrayList.add(new All("Month (2)","Bosome","","", "", ""));
        allArrayList.add(new All("Months","Abosome","","", "", ""));
        allArrayList.add(new All("This Month","Bosome yi","","", "", ""));
        allArrayList.add(new All("Last Month","Bosome a etwaam","","", "", ""));
        allArrayList.add(new All("First Month (2)","Bosome a edi kan","","", "", ""));
        allArrayList.add(new All("Second Month (2)","Bosome a ɛtɔ so mmienu","","", "", ""));

        allArrayList.add(new All("Year","Afe","","", "", ""));
        allArrayList.add(new All("Years","Mfe","","", "", ""));
        allArrayList.add(new All("This year","Afe yi","","", "", ""));
        allArrayList.add(new All("Last year","Afe a etwaam","","", "", ""));
        allArrayList.add(new All("A year by this time","Afe sesɛɛ","","", "", ""));
        allArrayList.add(new All("Next year (1)","Afedan","","", "", ""));
        allArrayList.add(new All("Next year (2)","Afe a yebesi mu","","", "", ""));


        allArrayList.add(new All("Today","Nnɛ","","", "", ""));
        allArrayList.add(new All("Yesterday","Ɛnnora","","", "", ""));
        allArrayList.add(new All("Tomorrow","Ɔkyena","","", "", ""));

        allArrayList.add(new All("When","Bere bɛn","","", "", ""));
        allArrayList.add(new All("What is the weather?","Abɔ sɛn?","","", "", ""));
        allArrayList.add(new All("What time is it?","Abɔ sɛn?","","", "", ""));



        allArrayList.add(new All("Morning","Anɔpa","","", "", ""));
        allArrayList.add(new All("This morning","Anɔpa yi","","", "", ""));

        allArrayList.add(new All("Afternoon","Awia","","", "", ""));
        allArrayList.add(new All("This afternoon","Awia yi","","", "", ""));

        allArrayList.add(new All("Evening","Anwummere","","", "", ""));
        allArrayList.add(new All("This evening","Anwummere yi","","", "", ""));

        allArrayList.add(new All("Night","Anadwo","","", "", ""));
        allArrayList.add(new All("This night","Anadwo yi","","", "", ""));

        allArrayList.add(new All("Midnight","Dasuom","","", "", ""));

        allArrayList.add(new All("Dawn","Anɔpatutu","","", "", ""));


        allArrayList.add(new All("1 am","Anɔpa Dɔnkoro","","", "", ""));
        allArrayList.add(new All("1:01","Dɔnkoro apa ho simma baako","","", "", ""));
        allArrayList.add(new All("1:05","Dɔnkoro apa ho simma num","","", "", ""));
        allArrayList.add(new All("1:15","Dɔnkoro apa ho simma dunum","","", "", ""));



        allArrayList.add(new All("2 am","Anɔpa Nnɔnmmienu","","", "", ""));
        allArrayList.add(new All("2:30 (1)","Nnɔnmmienu ne fa","","", "", ""));
        allArrayList.add(new All("2:30 (2)","Nnɔnmmienu apa ho simma aduasa","","", "", ""));


        allArrayList.add(new All("3 am","Anɔpa Nnɔnmmiɛnsa ","","", "", ""));

        allArrayList.add(new All("4 am","Anɔpa Nnɔnnan","","", "", ""));
        allArrayList.add(new All("5 am","Anɔpa Nnɔnnum","","", "", ""));
        allArrayList.add(new All("6 am","Anɔpa Nnɔnsia","","", "", ""));
        allArrayList.add(new All("7 am","Anɔpa Nnɔnson","","", "", ""));
        allArrayList.add(new All("8 am","Anɔpa Nnɔnwɔtwe","","", "", ""));
        allArrayList.add(new All("9 am","Anɔpa Nnɔnkron","","", "", ""));
        allArrayList.add(new All("10 am","Anɔpa Nnɔndu","","", "", ""));
        allArrayList.add(new All("11 am","Anɔpa Nnɔndubaako","","", "", ""));
        allArrayList.add(new All("12 am","Nnɔndummienu","","", "", ""));
        allArrayList.add(new All("Noon (1)","Nnɔndummienu","","", "", ""));
        allArrayList.add(new All("Noon (2)","Owigyinae","","", "", ""));
        allArrayList.add(new All("1 pm","Awia Dɔnkoro","","", "", ""));
        allArrayList.add(new All("2 pm","Awia Nnɔnmmienu","","", "", ""));
        allArrayList.add(new All("3 pm","Awia Nnɔnmmiɛnsa","","", "", ""));
        allArrayList.add(new All("4 pm","Anwummere Nnɔnnan","","", "", ""));
        allArrayList.add(new All("5 pm","Anwummere Nnɔnnum","","", "", ""));
        allArrayList.add(new All("6 pm","Anwummere Nnɔnsia","","", "", ""));
        allArrayList.add(new All("7 pm","Anwummere Nnɔnson","","", "", ""));
        allArrayList.add(new All("8 pm","Anadwo Nnɔnwɔtwe","","", "", ""));
        allArrayList.add(new All("9 pm","Anadwo Nnɔnkron","","", "", ""));
        allArrayList.add(new All("10 pm","Anadwo Nnɔndu","","", "", ""));
        allArrayList.add(new All("11 pm","Anadwo Nnɔndubaako","","", "", ""));
        allArrayList.add(new All("12 pm (1)","Anadwo dummienu","","", "", ""));
        allArrayList.add(new All("12 pm (2)","Dasuom","","", "", ""));


        //WEATHER ARRAY

        allArrayList.add(new All("Rain","Osu","","", "", ""));
        allArrayList.add(new All("It is raining","Osu retɔ","","", "", ""));
        allArrayList.add(new All("Will it rain today?","Osu bɛtɔ nnɛ anaa?","","", "", ""));
        allArrayList.add(new All("Did it rain yesterday?","Osu tɔɔ ɛnnora anaa?","","", "", ""));
        allArrayList.add(new All("It will rain today","Osu bɛtɔ nnɛ","","", "", ""));
        allArrayList.add(new All("Umbrella","Kyinneɛ","","", "", ""));
        allArrayList.add(new All("Take the umbrella","Fa kyinneɛ no","","", "", ""));
        allArrayList.add(new All("It is drizzling","Osu repetepete","","", "", ""));

        allArrayList.add(new All("Snow","Sukyerɛmma","","", "", ""));


        allArrayList.add(new All("Sun","Awia","","", "", ""));
        allArrayList.add(new All("It is sunny","Awia abɔ","","", "", ""));
        allArrayList.add(new All("It is very sunny","Awia abɔ paa","","", "", ""));
        allArrayList.add(new All("It is hot today","Ewiem ayɛ hye nnɛ","","", "", ""));
        allArrayList.add(new All("Sunset","Owitɔe","","", "", ""));
        allArrayList.add(new All("The sun has set","Owia no akɔtɔ","","", "", ""));
        allArrayList.add(new All("Sunrise","Owipue","","", "", ""));
        allArrayList.add(new All("The sun is rising","Owia no repue","","", "", ""));
        allArrayList.add(new All("The sun has risen","Owia no apue","","", "", ""));

        allArrayList.add(new All("It is warm","Ahohuru wom","","", "", ""));
        allArrayList.add(new All("I am feeling hot","Ɔhyew de me","","", "", ""));
        allArrayList.add(new All("I am feeling warm","Ahohuru de me","","", "", ""));
        allArrayList.add(new All("It is cold","Awɔw wom","","", "", ""));
        allArrayList.add(new All("It is very cold","Awɔw wom paa","","", "", ""));
        allArrayList.add(new All("I am feeling cold","Awɔw de me","","", "", ""));
        allArrayList.add(new All("It is very chilly","Awɔw wom paa","","", "", ""));

        allArrayList.add(new All("Lightning","Anyinam","","", "", ""));
        allArrayList.add(new All("Thunder","Apranaa","","", "", ""));
        allArrayList.add(new All("Cloud","Mununkum","","", "", ""));
        allArrayList.add(new All("Rain clouds have formed","Osu amuna","","", "", ""));
        allArrayList.add(new All("It is cloudy","Ewiem ayɛ kusuu","","", "", ""));
        allArrayList.add(new All("Wind","Mframa","","", "", ""));
        allArrayList.add(new All("It is windy","Mframa rebɔ","","", "", ""));
        allArrayList.add(new All("It is very windy","Mframa rebɔ paa","","", "", ""));

        allArrayList.add(new All("Sky (1)","Soro","","", "", ""));
        allArrayList.add(new All("Sky (2)","Ewiem","","", "", ""));
        allArrayList.add(new All("Weather","Ewiem tebea","","", "", ""));
        allArrayList.add(new All("How is the weather like?","Ewiem tebea te sɛn?","","", "", ""));
        allArrayList.add(new All("What will the weather be like today?","Ɛnnɛ ewiem tebea bɛyɛ sɛn?","","", "", ""));
        allArrayList.add(new All("Weather changes","Ewiem nsakrae","","", "", ""));
        allArrayList.add(new All("Stars","Nsoromma","","", "", ""));
        allArrayList.add(new All("The stars are glittering","Nsoromma no rehyerɛn","","", "", ""));

        allArrayList.add(new All("Misty","Ɛbɔ","","", "", ""));
        allArrayList.add(new All("It is misty","Ɛbɔ asi","","", "", ""));

        allArrayList.add(new All("Harmattan","Ɔpɛ","","", "", ""));
        allArrayList.add(new All("Harmattan is here","Ɔpɛ asi","","", "", ""));

        allArrayList.add(new All("Shade","Onwunu","","", "", ""));
        allArrayList.add(new All("Storm","Ahum","","", "", ""));
        allArrayList.add(new All("Rainbow","Nyankontɔn","","", "", ""));
        allArrayList.add(new All("I saw the rainbow","Me huu nyankontɔn no","","", "", ""));


        allArrayList.add(new All("Moon","Bosome","","", "", ""));


        //NUMBERS

        allArrayList.add(new All("0", "Hwee/Ohunu","","", "", ""));
        allArrayList.add(new All("1", "Baako","","", "", ""));
        allArrayList.add(new All("2", "Mmienu","","", "", ""));
        allArrayList.add(new All("3", "Mmiɛnsa","","", "", ""));
        allArrayList.add(new All("4", "Ɛnan","","", "", ""));
        allArrayList.add(new All("5", "Enum","","", "", ""));
        allArrayList.add(new All("6", "Nsia","","", "", ""));
        allArrayList.add(new All("7", "Nson","","", "", ""));
        allArrayList.add(new All("8", "Nwɔtwe","","", "", ""));
        allArrayList.add(new All("9", "Nkron","","", "", ""));
        allArrayList.add(new All("10", "Du","","", "", ""));

        allArrayList.add(new All("11", "Dubaako","","", "", ""));
        allArrayList.add(new All("12", "Dummienu","","", "", ""));
        allArrayList.add(new All("13", "Dummiɛnsa","","", "", ""));
        allArrayList.add(new All("14", "Dunan","","", "", ""));
        allArrayList.add(new All("15", "Dunum","","", "", ""));
        allArrayList.add(new All("16", "Dunsia","","", "", ""));
        allArrayList.add(new All("17", "Dunson","","", "", ""));
        allArrayList.add(new All("18", "Dunwɔtwe","","", "", ""));
        allArrayList.add(new All("19", "Dunkron","","", "", ""));
        allArrayList.add(new All("20", "Aduonu","","", "", ""));

        allArrayList.add(new All("21", "Aduonu baako","","", "", ""));
        allArrayList.add(new All("22", "Aduonu mmienu","","", "", ""));
        allArrayList.add(new All("23", "Aduonu mmiɛnsa","","", "", ""));
        allArrayList.add(new All("24", "Aduonu nan","","", "", ""));
        allArrayList.add(new All("25", "Aduonu num","","", "", ""));
        allArrayList.add(new All("26", "Aduonu nsia","","", "", ""));
        allArrayList.add(new All("27", "Aduonu nson","","", "", ""));
        allArrayList.add(new All("28", "Aduonu nwɔtwe","","", "", ""));
        allArrayList.add(new All("29", "Aduonu nkron","","", "", ""));
        allArrayList.add(new All("30", "Aduasa","","", "", ""));


        allArrayList.add(new All("31", "Aduasa baako","","", "", ""));
        allArrayList.add(new All("32", "Aduasa mmienu","","", "", ""));
        allArrayList.add(new All("33", "Aduasa mmiɛnsa","","", "", ""));
        allArrayList.add(new All("34", "Aduasa nan","","", "", ""));
        allArrayList.add(new All("35", "Aduasa num","","", "", ""));
        allArrayList.add(new All("36", "Aduasa nsia","","", "", ""));
        allArrayList.add(new All("37", "Aduasa nson","","", "", ""));
        allArrayList.add(new All("38", "Aduasa nwɔtwe","","", "", ""));
        allArrayList.add(new All("39", "Aduasa nkron","","", "", ""));
        allArrayList.add(new All("40", "Aduanan","","", "", ""));

        allArrayList.add(new All("41", "Aduanan baako","","", "", ""));
        allArrayList.add(new All("42", "Aduanan mmienu","","", "", ""));
        allArrayList.add(new All("43", "Aduanan mmiɛnsa","","", "", ""));
        allArrayList.add(new All("44", "Aduanan nan","","", "", ""));
        allArrayList.add(new All("45", "Aduanan num","","", "", ""));
        allArrayList.add(new All("46", "Aduanan nsia","","", "", ""));
        allArrayList.add(new All("47", "Aduanan nson","","", "", ""));
        allArrayList.add(new All("48", "Aduanan nwɔtwe","","", "", ""));
        allArrayList.add(new All("49", "Aduanan nkron","","", "", ""));
        allArrayList.add(new All("50", "Aduonum","","", "", ""));

        allArrayList.add(new All("51", "Aduonum baako","","", "", ""));
        allArrayList.add(new All("52", "Aduonum mmienu","","", "", ""));
        allArrayList.add(new All("53", "Aduonum mmiɛnsa","","", "", ""));
        allArrayList.add(new All("54", "Aduonum nan","","", "", ""));
        allArrayList.add(new All("55", "Aduonum num","","", "", ""));
        allArrayList.add(new All("56", "Aduonum nsia","","", "", ""));
        allArrayList.add(new All("57", "Aduonum nson","","", "", ""));
        allArrayList.add(new All("58", "Aduonum nwɔtwe","","", "", ""));
        allArrayList.add(new All("59", "Aduonum nkron","","", "", ""));
        allArrayList.add(new All("60", "Aduosia","","", "", ""));

        allArrayList.add(new All("61", "Aduosia baako","","", "", ""));
        allArrayList.add(new All("62", "Aduosia mmienu","","", "", ""));
        allArrayList.add(new All("63", "Aduosia mmiɛnsa","","", "", ""));
        allArrayList.add(new All("64", "Aduosia nan","","", "", ""));
        allArrayList.add(new All("65", "Aduosia num","","", "", ""));
        allArrayList.add(new All("66", "Aduosia nsia","","", "", ""));
        allArrayList.add(new All("67", "Aduosia nson","","", "", ""));
        allArrayList.add(new All("68", "Aduosia nwɔtwe","","", "", ""));
        allArrayList.add(new All("69", "Aduosia nkron","","", "", ""));
        allArrayList.add(new All("70", "Aduoson","","", "", ""));

        allArrayList.add(new All("71", "Aduoson baako","","", "", ""));
        allArrayList.add(new All("72", "Aduoson mmienu","","", "", ""));
        allArrayList.add(new All("73", "Aduoson mmiɛnsa","","", "", ""));
        allArrayList.add(new All("74", "Aduoson nan","","", "", ""));
        allArrayList.add(new All("75", "Aduoson num","","", "", ""));
        allArrayList.add(new All("76", "Aduoson nsia","","", "", ""));
        allArrayList.add(new All("77", "Aduoson nson","","", "", ""));
        allArrayList.add(new All("78", "Aduoson nwɔtwe","","", "", ""));
        allArrayList.add(new All("79", "Aduoson nkron","","", "", ""));
        allArrayList.add(new All("80", "Aduowɔtwe","","", "", ""));

        allArrayList.add(new All("81", "Aduowɔtwe baako","","", "", ""));
        allArrayList.add(new All("82", "Aduowɔtwe mmienu","","", "", ""));
        allArrayList.add(new All("83", "Aduowɔtwe mmiɛnsa","","", "", ""));
        allArrayList.add(new All("84", "Aduowɔtwe nan","","", "", ""));
        allArrayList.add(new All("85", "Aduowɔtwe num","","", "", ""));
        allArrayList.add(new All("86", "Aduowɔtwe nsia","","", "", ""));
        allArrayList.add(new All("87", "Aduowɔtwe nson","","", "", ""));
        allArrayList.add(new All("88", "Aduowɔtwe nwɔtwe","","", "", ""));
        allArrayList.add(new All("89", "Aduowɔtwe nkron","","", "", ""));
        allArrayList.add(new All("90", "Aduokron","","", "", ""));

        allArrayList.add(new All("91", "Aduokron baako","","", "", ""));
        allArrayList.add(new All("92", "Aduokron mmienu","","", "", ""));
        allArrayList.add(new All("93", "Aduokron mmiɛnsa","","", "", ""));
        allArrayList.add(new All("94", "Aduokron nan","","", "", ""));
        allArrayList.add(new All("95", "Aduokron num","","", "", ""));
        allArrayList.add(new All("96", "Aduokron nsia","","", "", ""));
        allArrayList.add(new All("97", "Aduokron nson","","", "", ""));
        allArrayList.add(new All("98", "Aduokron nwɔtwe","","", "", ""));
        allArrayList.add(new All("99", "Aduokron nkron","","", "", ""));
        allArrayList.add(new All("100", "Ɔha","","", "", ""));

        allArrayList.add(new All("101", "Ɔha ne baako","","", "", ""));
        allArrayList.add(new All("102", "Ɔha ne mmienu","","", "", ""));
        allArrayList.add(new All("103", "Ɔha ne mmiɛnsa","","", "", ""));
        allArrayList.add(new All("104", "Ɔha ne nan","","", "", ""));
        allArrayList.add(new All("105", "Ɔha ne num","","", "", ""));
        allArrayList.add(new All("106", "Ɔha ne nsia","","", "", ""));
        allArrayList.add(new All("107", "Ɔha ne nson","","", "", ""));
        allArrayList.add(new All("108", "Ɔha ne nwɔtwe","","", "", ""));
        allArrayList.add(new All("109", "Ɔha ne nkron","","", "", ""));
        allArrayList.add(new All("110", "Ɔha ne du","","", "", ""));

        allArrayList.add(new All("111", "Ɔha ne dubaako","","", "", ""));
        allArrayList.add(new All("112", "Ɔha ne dummienu","","", "", ""));
        allArrayList.add(new All("113", "Ɔha ne dummiɛnsa","","", "", ""));
        allArrayList.add(new All("114", "Ɔha ne dunan","","", "", ""));
        allArrayList.add(new All("115", "Ɔha ne dunum","","", "", ""));
        allArrayList.add(new All("116", "Ɔha ne dunsia","","", "", ""));
        allArrayList.add(new All("117", "Ɔha ne dunson","","", "", ""));
        allArrayList.add(new All("118", "Ɔha ne dunwɔtwe","","", "", ""));
        allArrayList.add(new All("119", "Ɔha ne dunkron","","", "", ""));
        allArrayList.add(new All("120", "Ɔha aduonu","","", "", ""));


        allArrayList.add(new All("121", "Ɔha aduonu baako","","", "", ""));
        allArrayList.add(new All("122", "Ɔha aduonu mmienu","","", "", ""));
        allArrayList.add(new All("123", "Ɔha aduonu mmiɛnsa","","", "", ""));
        allArrayList.add(new All("124", "Ɔha aduonu nan","","", "", ""));
        allArrayList.add(new All("125", "Ɔha aduonu num","","", "", ""));

        allArrayList.add(new All("130", "Ɔha aduasa","","", "", ""));
        allArrayList.add(new All("140", "Ɔha aduanan","","", "", ""));
        allArrayList.add(new All("150", "Ɔha aduonum","","", "", ""));
        allArrayList.add(new All("160", "Ɔha aduosia","","", "", ""));
        allArrayList.add(new All("170", "Ɔha aduoson","","", "", ""));
        allArrayList.add(new All("180", "Ɔha aduowɔtwe","","", "", ""));
        allArrayList.add(new All("190", "Ɔha aduokron","","", "", ""));
        allArrayList.add(new All("200", "Ahanu","","", "", ""));


        allArrayList.add(new All("201", "Ahanu ne baako","","", "", ""));
        allArrayList.add(new All("202", "Ahanu ne mmienu","","", "", ""));
        allArrayList.add(new All("203", "Ahanu ne mmiɛnsa","","", "", ""));
        allArrayList.add(new All("204", "Ahanu ne nan","","", "", ""));
        allArrayList.add(new All("205", "Ahanu ne num","","", "", ""));
        allArrayList.add(new All("206", "Ahanu ne nsia","","", "", ""));
        allArrayList.add(new All("207", "Ahanu ne nson","","", "", ""));
        allArrayList.add(new All("208", "Ahanu ne nwɔtwe","","", "", ""));
        allArrayList.add(new All("209", "Ahanu ne nkron","","", "", ""));
        allArrayList.add(new All("210", "Ahanu ne du","","", "", ""));
        allArrayList.add(new All("211", "Ahanu ne dubaako","","", "", ""));

        allArrayList.add(new All("220", "Ahanu aduonu","","", "", ""));
        allArrayList.add(new All("221", "Ahanu aduonu baako","","", "", ""));
        allArrayList.add(new All("230", "Ahanu aduasa","","", "", ""));
        allArrayList.add(new All("240", "Ahanu aduanan","","", "", ""));
        allArrayList.add(new All("250", "Ahanu aduonum","","", "", ""));

        allArrayList.add(new All("300", "Ahasa","","", "", ""));
        allArrayList.add(new All("400", "Ahanan","","", "", ""));
        allArrayList.add(new All("500", "Ahanum","","", "", ""));
        allArrayList.add(new All("600", "Ahansia","","", "", ""));
        allArrayList.add(new All("700", "Ahanson","","", "", ""));
        allArrayList.add(new All("800", "Ahanwɔtwe","","", "", ""));
        allArrayList.add(new All("900", "Ahankron","","", "", ""));
        allArrayList.add(new All("1000", "Apem","","", "", ""));


        allArrayList.add(new All("1001", "Apem ne baako","","", "", ""));
        allArrayList.add(new All("1002", "Apem ne mmienu","","", "", ""));
        allArrayList.add(new All("1003", "Apem ne mmiɛnsa","","", "", ""));
        allArrayList.add(new All("1004", "Apem ne nan","","", "", ""));
        allArrayList.add(new All("1005", "Apem ne num","","", "", ""));

        allArrayList.add(new All("1010", "Apem ne du","","", "", ""));
        allArrayList.add(new All("1011", "Apem ne dubaako","","", "", ""));
        allArrayList.add(new All("1012", "Apem ne dummienu","","", "", ""));
        allArrayList.add(new All("1013", "Apem ne dummiɛnsa","","", "", ""));
        allArrayList.add(new All("1014", "Apem ne dunan","","", "", ""));
        allArrayList.add(new All("1015", "Apem ne dunum","","", "", ""));

        allArrayList.add(new All("1020", "Apem aduonu","","", "", ""));
        allArrayList.add(new All("1021", "Apem aduonu baako","","", "", ""));
        allArrayList.add(new All("1022", "Apem aduonu mmienu","","", "", ""));
        allArrayList.add(new All("1023", "Apem aduonu mmiɛnsa","","", "", ""));
        allArrayList.add(new All("1024", "Apem aduonu nan","","", "", ""));
        allArrayList.add(new All("1025", "Apem aduonu num","","", "", ""));

        allArrayList.add(new All("1100", "Apem ɔha","","", "", ""));
        allArrayList.add(new All("1101", "Apem ɔha ne baako","","", "", ""));
        allArrayList.add(new All("1102", "Apem ɔha ne mmienu","","", "", ""));
        allArrayList.add(new All("1103", "Apem ɔha ne mmiɛnsa","","", "", ""));
        allArrayList.add(new All("1104", "Apem ɔha ne nan","","", "", ""));
        allArrayList.add(new All("1105", "Apem ɔha ne num","","", "", ""));

        allArrayList.add(new All("1200", "Apem ahanu","","", "", ""));
        allArrayList.add(new All("1201", "Apem ahanu ne baako","","", "", ""));
        allArrayList.add(new All("1202", "Apem ahanu ne mmienu","","", "", ""));
        allArrayList.add(new All("1203", "Apem ahanu ne mmiɛnsa","","", "", ""));
        allArrayList.add(new All("1204", "Apem ahanu ne nan","","", "", ""));
        allArrayList.add(new All("1205", "Apem ahanu ne num","","", "", ""));

        allArrayList.add(new All("1300", "Apem ahasa","","", "", ""));
        allArrayList.add(new All("1400", "Apem ahanan","","", "", ""));
        allArrayList.add(new All("1500", "Apem ahanum","","", "", ""));
        allArrayList.add(new All("1600", "Apem ahansia","","", "", ""));
        allArrayList.add(new All("1700", "Apem ahanson","","", "", ""));
        allArrayList.add(new All("1800", "Apem ahanwɔtwe","","", "", ""));
        allArrayList.add(new All("1900", "Apem ahankron","","", "", ""));
        allArrayList.add(new All("2000", "Mpem mmienu","","", "", ""));

        allArrayList.add(new All("2001", "Mpem mmienu ne baako","","", "", ""));
        allArrayList.add(new All("2010", "Mpem mmienu ne du","","", "", ""));
        allArrayList.add(new All("2100", "Mpem mmienu ne ɔha","","", "", ""));
        allArrayList.add(new All("2101", "Mpem mmienu ɔha ne baako","","", "", ""));
        allArrayList.add(new All("2201", "Mpem mmienu ahanu ne baako","","", "", ""));
        allArrayList.add(new All("2301", "Mpem mmienu ahasa ne baako","","", "", ""));

        allArrayList.add(new All("3000", "Mpem mmiɛnsa","","", "", ""));
        allArrayList.add(new All("4000", "Mpem nan","","", "", ""));
        allArrayList.add(new All("5000", "Mpem num","","", "", ""));
        allArrayList.add(new All("6000", "Mpem nsia","","", "", ""));
        allArrayList.add(new All("7000", "Mpem nson","","", "", ""));
        allArrayList.add(new All("8000", "Mpem nwɔtwe","","", "", ""));
        allArrayList.add(new All("9000", "Mpem nkron","","", "", ""));
        allArrayList.add(new All("10000", "Mpem du","","", "", ""));

        allArrayList.add(new All("10001", "Mpem du ne baako","","", "", ""));
        allArrayList.add(new All("10100", "Mpem du ne ɔha","","", "", ""));
        allArrayList.add(new All("10101", "Mpem du, ɔha ne baako","","", "", ""));
        allArrayList.add(new All("10200", "Mpem du ne ahanu","","", "", ""));
        allArrayList.add(new All("10201", "Mpem du, ahanu ne baako","","", "", ""));
        allArrayList.add(new All("10300", "Mpem du ne ahasa","","", "", ""));

        allArrayList.add(new All("11000", "Mpem dubaako","","", "", ""));
        allArrayList.add(new All("11001", "Mpem dubaako ne baako","","", "", ""));
        allArrayList.add(new All("11011", "Mpem dubaako ne dubaako","","", "", ""));
        allArrayList.add(new All("11121", "Mpem dubaako, ɔha ne aduonu baako","","", "", ""));

        allArrayList.add(new All("12000", "Mpem dummienu","","", "", ""));
        allArrayList.add(new All("13000", "Mpem dummiɛnsa","","", "", ""));
        allArrayList.add(new All("14000", "Mpem dunan","","", "", ""));
        allArrayList.add(new All("15000", "Mpem dunum","","", "", ""));
        allArrayList.add(new All("16000", "Mpem dunsia","","", "", ""));
        allArrayList.add(new All("17000", "Mpem dunson","","", "", ""));
        allArrayList.add(new All("18000", "Mpem dunwɔtwe","","", "", ""));
        allArrayList.add(new All("19000", "Mpem dunkron","","", "", ""));
        allArrayList.add(new All("20000", "Mpem aduonu","","", "", ""));

        allArrayList.add(new All("30000", "Mpem aduasa","","", "", ""));
        allArrayList.add(new All("40000", "Mpem aduanan","","", "", ""));
        allArrayList.add(new All("50000", "Mpem aduonum","","", "", ""));
        allArrayList.add(new All("60000", "Mpem aduosia","","", "", ""));
        allArrayList.add(new All("70000", "Mpem aduoson","","", "", ""));
        allArrayList.add(new All("80000", "Mpem aduowɔtwe","","", "", ""));
        allArrayList.add(new All("90000", "Mpem aduokron","","", "", ""));
        allArrayList.add(new All("100 000", "Mpem ɔha","","", "", ""));


        allArrayList.add(new All("1,000,000", "Ɔpepem baako","","", "", ""));
        allArrayList.add(new All("2 000 000", "Ɔpepem mmienu","","", "", ""));
        allArrayList.add(new All("10 000 000", "Ɔpepem du","","", "", ""));

        allArrayList.add(new All("100 000 000", "Ɔpepem ɔha","","", "", ""));

        allArrayList.add(new All("1 000 000 000", "Ɔpepepem baako","","", "", ""));



        /////////////////////////////////ALL ARRAY LIST  END///////////////////////////



        //////PROVERBS///////////////

        // proverbsArrayList.add(new Proverbs());

        proverbsArrayList.add(new Proverbs("Woforo dua pa a ɛnna yepia woɔ","If you climb a the good tree, gets a push","If you start something good you will attract people to help"));
        proverbsArrayList.add(new Proverbs("Wusie nni ahoɔden wɔ mframa kurom","The smoke has no power in the city of the wind","The stranger has no power in a strange land"));
        proverbsArrayList.add(new Proverbs("Nokware di etuo", "The truth gets a gun","Speaking the truth boldly can at times end your life as if you called for to be shot by a gun"));
        proverbsArrayList.add(new Proverbs("Sɛnea sekan te no saa ara na ne bɔha nso te","The shape of the sword is the same as its scabbard","Your behaviour tells where you come from"));
        proverbsArrayList.add(new Proverbs("Aboa aserewa hwɛ ne kɛse ho na wanwene ne buo", "The silverbird weaves its nest according to its size", "Do what you can do and avoid trying to do things beyond your ability in order to please others"));
        proverbsArrayList.add(new Proverbs("Abofra bɔ nnwa na ɔmmɔ akyekyedeɛ","A child breaks the shell of a crab and not the tortoise", "A child must know his limit. Some things are for adults"));
        proverbsArrayList.add(new Proverbs("Aboa bi bɛka wo a, na ofiri wo ntoma mu","If an animal will bite you, it is the one hidden in your clothes", "It is people close to you that will hurt you"));
        proverbsArrayList.add(new Proverbs("Ɔba nyansafo, yebu no bɛ, yɛnnka no asɛm","We speak in proverbs to a wise son, we do not use direct statements","Just a proverb can be used to instruct a wise person. No need to explain things in detail to a wise person"));
    proverbsArrayList.add(new Proverbs("Ti koro nkɔ agyina","One head does not consult","It is better to consult with others before making a decision"));
        proverbsArrayList.add(new Proverbs("Anomaa antu a, obua da","If a bird does not fly, it starves to death","If you don't take action and work you will not gain anything and will die as a result. Laziness produces nothing"));
        proverbsArrayList.add(new Proverbs("Obi nnim oberempɔn ahyɛase","No one knows the beginning of a great man","You cannot always tell how great a man will be by his current state. A poor person can become a great man in the future"));
    proverbsArrayList.add(new Proverbs("Agya bi wu a, agya bi te ase","If a father dies, another father lives","If a parent dies, you can find someone who will look after you like your parent"));
       proverbsArrayList.add(new Proverbs("Animguase mfata Akanni ba","To be disgraced is not deserving of an Akan", "If you have respect for yourself then it is better to die than to be disgraced"));
        proverbsArrayList.add(new Proverbs("Agorɔ bɛsɔ a, efiri anɔpa","You can tell from the morning if the play will be nice","You can tell from the beginning of a venture how successfull it will be in the future"));
        proverbsArrayList.add(new Proverbs("Kwaterekwa se ɔbɛma wo ntoma a, tie ne din","If a naked person says that he will give you a cloth, listen to his name","Be careful when someone who is in need of something himself promises to give you that thing. Dont trust all promises. Take into consideratoin the calibre of a person who promises you something"));
        proverbsArrayList.add(new Proverbs("Yɛsoma onyansafo, ɛnyɛ anamɔntenten","It is the wise person that we send on errand but not a person with long steps","Speed should not override efficiency. Getting things done properly is better than getting it done fast but not properly."));
        proverbsArrayList.add(new Proverbs("Aboa a onni dua no, Nyame na ɔpra ne ho","An animal without a tail is cleaned by God","God provides for those who have no means of catering for themselves"));
        proverbsArrayList.add(new Proverbs("Borɔferɛ a ɛyɛ dɛ na abaa da ase","You will find a stick beneath the pawpaw tree which has tasty fruits","If you see people flocking to a particular venture it is because it is profitable"));
        proverbsArrayList.add(new Proverbs("Prayɛ, sɛ wuyi baako a na ebu, woka bom a emmu","If you remove one broomstick it will break but if you put all the broomsticks together it will not break","We are stronger when we are united. It is hard to defeat a united people than a single person"));
        proverbsArrayList.add(new Proverbs("Nsateaa nyinaa nnyɛ pɛ","All fingers are not the same","We all have different abilities"));
        proverbsArrayList.add(new Proverbs("Obi nnim a, obi kyerɛ","If one does not know, another teaches","You should allow others to teach you things you don't konw. You should listen to advice"));
        proverbsArrayList.add(new Proverbs("Abofra hunu ne nsa hohoro a ɔne mpanyimfoɔ didi","If a child learns how to wash his hands, he will eat with adults","If a person learns and applies the customs and traditions, people in higher positions will work with him. "));
        proverbsArrayList.add(new Proverbs("Yɛwo wo to esie so a, wo nnkyɛ tenten yɛ","If you are born onto an anthill, you become tall quickly","If you have a good foundation in life, it is easy for you to succeed in life. If your family is rich, you are able to make money early in life"));
        proverbsArrayList.add(new Proverbs("Ayɔnko gorɔ nti na ɔkɔtɔ annya ti","It is because of mingling with friends that the crab has no head","Too many friends can make you lose a lot. Choose friends wisely"));
        proverbsArrayList.add(new Proverbs("Wo nsa akyi bɛyɛ wo dɛ a ɛnte sɛ wo nsa yam","The back of your hand can be sweet but it will not be as your palm or inner surface of your hand","The original is always better than imitation"));

        proverbsArrayList.add(new Proverbs("Obi fom kum a, yɛn mfom ndwa","If one kills by mistake, we do not cut up by mistake","We do not deliberately repay someone with a bad deed for their unintentional mistake "));
        proverbsArrayList.add(new Proverbs("Wuhu sɛ wo yɔnko abɔdwesɛ rehye a na wasa nsuo asi wo de ho","If you see that the beard of a friend is on fire, fetch water and put by your beard","If something bad happens to a neighbour, plan on how you will deal it with if it happens to you next"));
        proverbsArrayList.add(new Proverbs("Dua a enya wo a ɛbɛwɔ w'ani no, yetu ase; yɛnsensene ano","A tree which is likely to pierce your eye must be uprooted, not merely pruned","If a problem has the potential to harm you, we do not solve it partially but rather we solve it completely. Eliminate completely anything that can harm you"));
        proverbsArrayList.add(new Proverbs("Nyansapɔ wɔsane no badwemma","A discerning man loosens a hard tight know","It takes a wise person to solve hard riddles. One who solves a complex problem is wise"));
        proverbsArrayList.add(new Proverbs("Ahunu bi pɛn nti na aserewa regyegye ne ba agorɔ a na wayi n'ani ato nkyɛn","When playing with its child, the silverbird looks away because of what it has seen before","The behaviour of some people are as a result of the bad experiences they have encountered in their lives"));
        proverbsArrayList.add(new Proverbs("Abaa a yɛde bɔ Takyi no yɛde bɛbɔ Baa","The cane which is used on Takyi will also be used on Baa","The punishment given to someone for a crime will be the same punishment for another who commits the same crime. What is done to someone will be the same that will happen to you if you act like him"));
        proverbsArrayList.add(new Proverbs("Abɛ bi rebewu a na ɛsɔ","When some palm trees are about to die, its wine tastes good","Some people get to their best when they are about to retire or in their old age. The aged are the most experienced"));
        proverbsArrayList.add(new Proverbs("Nea ɔwɔ aka no pɛn no suro sonsono","He who has been bitten by a snake before, fears the worm", "A bad experience with a particular venture makes one afraid of anything resembling it"));
        proverbsArrayList.add(new Proverbs("Efie biara Mensah wɔ mu","Every household has a third born","In every community there will be people whose opinion are totally different which might result in problems"));
        proverbsArrayList.add(new Proverbs("Abofra a ɔmma ne maame nna no, bentoa mpa ne to da","The enema will not depart from the buttucks of ea child who doesn't let his mother sleep", "If you make trouble for your leaders who cater for you, you will also not have peace"));
        proverbsArrayList.add(new Proverbs("Hu m'ani so ma me nti na atwe mmienu nam","Deers walk in pairs so that one can blow the eye of the other if needed","It is good to have a partner so that he will support you when you are in need"));
        proverbsArrayList.add(new Proverbs("Obi nnom aduro mma ɔyarefo","One does not take medicine for a sick person", "Don't expect someone to handle your responsibilities for you. There are some responsibilities you cannot do for another person"));
        proverbsArrayList.add(new Proverbs("Akokɔbere nim adekyee nanso otie firi akokɔ nini ano","The hen knows of the new day but it listens to the announcement from the cock","Even though you might be knowledgeable in something it is always good to listen to the elderly and wait for direction"));
        proverbsArrayList.add(new Proverbs("Ano da hɔ kwa a, ɛkeka nsɛm","A mouth which is idle will say many things","Those who have nothing to do becomes gossipers or commit crimes"));
        proverbsArrayList.add(new Proverbs("Ɔsansa fa adeɛ a ɔde kyerɛ amansan","When the hawk picks up something it shows it to the universe","An honest person will not hide his works"));
        proverbsArrayList.add(new Proverbs("Ɛtoa na ɛpɛ na ahoma da ne kɔn mu","The bottle likes it that is why there is a rope around its neck","It is your fault if you allow your enemies to trap you"));
        proverbsArrayList.add(new Proverbs("Dadeɛ bi twa dadeɛ bi","An iron can cut another iron or can sharpen another iron","There is someone or something stronger than you. Even if you are strong it doesn't mean that you are unbeatable"));
        proverbsArrayList.add(new Proverbs("Obi atifi nso yɛ obi anaafoɔ","Your north is someone's south","What you see as new maybe old to someone. "));
        proverbsArrayList.add(new Proverbs("Abosomakoterɛ se ntɛm yɛ, brɛbrɛ nso yɛ","The chameleon claims that to be fast is good and to be slow is also good","If you do anything in good faith it is good"));
        proverbsArrayList.add(new Proverbs("Wo sum borɔdeɛ a, sum kwadu","You should take good care of the banana just as you take good care of the plantain","Give the same attention to those you see as good and those you see as bad because you don't know when they will be useful to you"));
        proverbsArrayList.add(new Proverbs("Nea ɔte fam no nsuro ahweaseɛ","The one who is sitting down does not fear falling","If you have done all that is required of you, you don't fear what will come. If you are at your lowest point, you do not fear humiliation"));
        proverbsArrayList.add(new Proverbs("Abɔdwesɛ bɛtoo ani ntɔn nhwi","The eyebrow was there before the beard came","No matter how high your current position is, you have to give respect to the elderly"));
        proverbsArrayList.add(new Proverbs("Koterɛ nnkɔdi mako mma mfifire nkɔfiri apɔnkyerɛni", "If the lizard consumes pepper, it's not the frog that sweats","The one who commits a crime must bear his own punishment"));
        proverbsArrayList.add(new Proverbs("Obi ntɔn n'akokɔ bedeɛ kwa","No one sells his laying hen for nothing","There is no action without cause. If there is nothing wrong, no one will sell his valuable property"));
        proverbsArrayList.add(new Proverbs("Abaa nna hɔ mma kraman nnka nipa","The stick should not lie idle while a dog bites a human","Use your resources for your good. Don't keep money and end up dying from a minor ailment"));
    }



}
