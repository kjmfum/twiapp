package com.learnakantwi.twiguides;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class QuizHighScoresHome extends AppCompatActivity {

    String Ads="";
    Intent intent;

    public void goToLevel(String category){
        Intent intent = new Intent(this, QuizTimedAllFinishPage.class);
        intent.putExtra("category",category);
        intent.putExtra("Ads",Ads);
        startActivity(intent);
    }

    public void goToCategoryType(View view){
        int idview = view.getId();
        Button me = view.findViewById(idview);

        String category = me.getText().toString();

        switch(category){
            case "ALL VOCABULARY" :
                goToLevel("All Vocabulary");
                return;
            case "NUMBERS" :
                goToLevel("Numbers");
                return;
            case "CHILDREN ANIMALS" :
                goToLevel("Children Animals");
                return;
            case "ANIMALS" :
                goToLevel("Animals");
                return;
            default:
                goToLevel("All Vocabulary");
                //Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
                return;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_high_scores_home);

        intent = getIntent();
        Ads = intent.getStringExtra("Ads");

    }
}
