package com.learnakantwi.twiguides;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Games extends AppCompatActivity {

    Button btStartGame;

    private void openPortal() {
        String url = "https://www.gamezop.com/?id=WkzWqOZ_A";
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(ContextCompat.getColor(this, R.color.blue));
        builder.setShowTitle(true);
        // builder.addDefaultShareMenuItem();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.intent.setPackage("com.android.chrome");
        customTabsIntent.launchUrl(this, Uri.parse(url));
       /* PendingIntent pendingIntent = new PendingIntent(this, DivisionMain.class);
        startActivity(pendingIntent);*/

        //builder.setActionButton( , "testing Mic", pendingIntent, true);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);

        //openPortal();

        btStartGame = findViewById(R.id.startGame);


        btStartGame.setVisibility(View.VISIBLE);

        btStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPortal();
            }
        });


        if (MainActivity.Subscribed == 1){
            //alertDialog
            btStartGame.setText("This is a non Twi section that Contains Ads \n Click to Continue and  \n PLAY GAMES");
        }
        else{
            openPortal();
        }



    }
}