package com.learnakantwi.twiguides;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Games extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser User;
    String displayName;
    String currentUserEmail;
    Button btStartGame;
    ListView lvGames;

    TextView tvGameUser;
    ArrayList<String> gameNameList;
    Map<String, String> gameObject = new HashMap<>();

    private void openPortal(String url) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(ContextCompat.getColor(this, R.color.blue));
        builder.setShowTitle(true);
        // builder.addDefaultShareMenuItem();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.intent.setPackage("com.android.chrome");
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //MenuInflater menuInflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.signinout_menu, menu);

     /*   final MenuItem item = menu.findItem(R.id.menusearch);
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
        });*/

        return super.onCreateOptionsMenu(menu);


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {

            case R.id.signIn:
                signInMenu();
                return true;
            case R.id.signOut:
                signOutMenu();
                return true;
            case R.id.main:
                goToMain();
                return true;
            default:
                return false;
        }
    }

    public void goToMain(){
        Intent intent;
        if (MainActivity.Subscribed != 1){
            intent = new Intent(getApplicationContext(), HomeMainActivity.class);
        }
        else{
            intent = new Intent(getApplicationContext(), SubPHomeMainActivity.class);
        }
        startActivity(intent);
    }

    public void signOutMenu(){
        if (mAuth.getCurrentUser() != null){
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(this, "Signed Out", Toast.LENGTH_SHORT).show();

            //checkSignedStatus();
            tvGameUser.setBackgroundColor(Color.GREEN);
            tvGameUser.setText("Please Sign in To Save Game Progress");
            tvGameUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SignIn();
                }
            });
        }else{
            Toast.makeText(this, "Not Signed In", Toast.LENGTH_SHORT).show();
        }
    }

    public void signInMenu(){
        if (mAuth.getCurrentUser() != null){
            Toast.makeText(this, "You are Already Signed In as\n"+ mAuth.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
        }else{
            SignIn();
           // Toast.makeText(this, "Not Signed In", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);

        tvGameUser = findViewById(R.id.tvGameSignIn);

        mAuth = FirebaseAuth.getInstance();
        User = mAuth.getCurrentUser();

        if(User != null){
            tvGameUser.setText(User.toString());
        }

        //openPortal();
        gameNameList = new ArrayList<>();
        gameObject.put("One More Flight", "https://www.gamezop.com/g/BJ-ZsT2zOeZ?id=WkzWqOZ_A");
        gameObject.put("Time Warp", "https://www.gamezop.com/g/BkEv3wn-t?id=WkzWqOZ_A");
        gameObject.put("Table Tennis Shots", "https://www.gamezop.com/g/HJY4pfJP9JQ?id=WkzWqOZ_A");
        gameObject.put("Snack Time", "https://www.gamezop.com/g/SkKlAYSbuE?id=WkzWqOZ_A");
        gameObject.put("Don't Eat Trash", "https://www.gamezop.com/g/r1HAtSWO4?id=WkzWqOZ_A");
        gameObject.put("Cute Towers 2", "https://www.gamezop.com/g/ByZ3DF_hd?id=WkzWqOZ_A");
        gameObject.put( "Don't Touch the Walls", "https://www.gamezop.com/g/rybx12amqCB?id=WkzWqOZ_A");
        gameObject.put("Drift Control", "https://www.gamezop.com/g/BkxuV7ZkK?id=WkzWqOZ_A");
        gameObject.put("Cyberfusion", "https://www.gamezop.com/g/HJXei0j?id=WkzWqOZ_A");
        gameObject.put("Let Me Grow", "https://www.gamezop.com/g/SklmW1ad_?id=WkzWqOZ_A");
        gameObject.put("Enchanted Waters", "https://www.gamezop.com/g/HJskh679Cr?id=WkzWqOZ_A");
        gameObject.put("Tic Tac Toe 11", "https://www.gamezop.com/g/Hk2yhp7cCH?id=WkzWqOZ_A");
        gameObject.put("Snakes & Ladders", "https://www.gamezop.com/g/rJWyhp79RS?id=WkzWqOZ_A");

        gameNameList.add("Cute Towers 2"); //https://www.gamezop.com/g/ByZ3DF_hd?id=WkzWqOZ_A
        gameNameList.add("Cyberfusion"); // https://www.gamezop.com/g/HJXei0j?id=WkzWqOZ_A
        gameNameList.add("Let Me Grow"); //https://www.gamezop.com/g/SklmW1ad_?id=WkzWqOZ_A
        gameNameList.add("Snakes & Ladders");//https://www.gamezop.com/g/rJWyhp79RS?id=WkzWqOZ_A
        gameNameList.add("Enchanted Waters"); // https://www.gamezop.com/g/HJskh679Cr?id=WkzWqOZ_A;
        gameNameList.add("Drift Control"); //https://www.gamezop.com/g/BkxuV7ZkK?id=WkzWqOZ_A
        gameNameList.add("Don't Touch the Walls");//https://www.gamezop.com/g/rybx12amqCB?id=WkzWqOZ_A
        gameNameList.add("Time Warp");  // https://www.gamezop.com/g/BkEv3wn-t?id=WkzWqOZ_A
        gameNameList.add("Table Tennis Shots"); //https://www.gamezop.com/g/HJY4pfJP9JQ?id=WkzWqOZ_A
        gameNameList.add("Snack Time"); //https://www.gamezop.com/g/SkKlAYSbuE?id=WkzWqOZ_A
        gameNameList.add("Don't Eat Trash"); //https://www.gamezop.com/g/r1HAtSWO4?id=WkzWqOZ_A
        gameNameList.add("One More Flight"); // "https://www.gamezop.com/g/BJ-ZsT2zOeZ?id=WkzWqOZ_A");
        gameNameList.add("Tic Tac Toe 11");//https://www.gamezop.com/g/Hk2yhp7cCH?id=WkzWqOZ_A






        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, gameNameList);


        btStartGame = findViewById(R.id.startGame);
        lvGames = findViewById(R.id.lvGames);
        lvGames.setAdapter(arrayAdapter);
        lvGames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(currentUserEmail != null){
                    openPortal(gameObject.get(gameNameList.get(i))+"&sub="+ currentUserEmail);
                }
                else{
                    openPortal(gameObject.get(gameNameList.get(i)));
                }

            }
        });
        lvGames.setVisibility(View.INVISIBLE);
        tvGameUser.setVisibility(View.INVISIBLE);



        btStartGame.setVisibility(View.VISIBLE);

        btStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btStartGame.setVisibility(View.INVISIBLE);
                lvGames.setVisibility(View.VISIBLE);
                tvGameUser.setVisibility(View.VISIBLE);
               // openPortal();
            }
        });


        if (MainActivity.Subscribed == 1){
            //alertDialog
            btStartGame.setText("This is a non Twi section that Contains Ads \n Click to Continue and  \n PLAY GAMES");
            lvGames.setVisibility(View.INVISIBLE);
        }
        else{
            btStartGame.setVisibility(View.INVISIBLE);
            lvGames.setVisibility(View.VISIBLE);
            tvGameUser.setVisibility(View.VISIBLE);
            //openPortal();
        }
    }

    public void SignIn(){
        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
        intent.putExtra("nextScreen", "Games");
        startActivity(intent);
    }

    public void checkSignedStatus(){
        if (mAuth.getCurrentUser()!=null){
            currentUserEmail = mAuth.getCurrentUser().getEmail();
            displayName = mAuth.getCurrentUser().getDisplayName();

            User = mAuth.getCurrentUser();
            SpannableStringBuilder ssb = new SpannableStringBuilder();
            if (User.isEmailVerified()){
                ssb.append("Akwaaba: ");
                ForegroundColorSpan fcsGreen = new ForegroundColorSpan(Color.GREEN);
                ssb.setSpan(fcsGreen, 0,ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            if (displayName == null){
                displayName ="";
                ssb.append(currentUserEmail);
            }else{
                ssb.append(displayName);
            }

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(ssb);
            // tvSignIn.setText(stringBuilder);
            tvGameUser.setText(ssb);
            tvGameUser.setBackgroundColor(Color.WHITE);
        }else{
            tvGameUser.setBackgroundColor(Color.GREEN);
            tvGameUser.setText("Please Sign in To Save Game Progress");
            tvGameUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SignIn();
                }
            });
        }
    }

    @Override
    protected void onStart() {
        checkSignedStatus();
        super.onStart();
    }
}

