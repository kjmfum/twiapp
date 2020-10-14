package com.learnakantwi.twiguides;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class PleaseSubscribePage extends AppCompatActivity {

    Button btSubscribe;
    String coming;



    public void goToPleaseSubPage() {
        Intent intent = new Intent(getApplicationContext(), InAppActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.please_subscribe_page);

        btSubscribe = findViewById(R.id.btSubscribe);
        btSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPleaseSubPage();
            }
        });

        Intent intent = getIntent();
        coming = intent.getStringExtra("coming");

        if (coming!=null && coming.equals("soon")){
            btSubscribe.setText("Coming Soon \n Available in next update");
            btSubscribe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        else if (coming!=null && coming.equals("ads")){
            //Toast.makeText(this, "I'm here", Toast.LENGTH_SHORT).show();
            btSubscribe.setText("Coming Soon To \n Subscribed Customers\n Click to Subscribe");
        }





    }
}
