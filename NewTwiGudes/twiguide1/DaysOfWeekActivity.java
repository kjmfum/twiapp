package com.learnakantwi.twiguides;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

//import android.support.v7.app.AppCompatActivity;

public class DaysOfWeekActivity extends AppCompatActivity {
    ListView daysOfWeekListView;
    EditText daysOfWeekEditText;
    DaysOfWeekAdapter myAdapterDaysOfWk;
    ArrayList<DaysOfWeek> daysOfWeeksArray;

    public void log3(View view){
        int idview= view.getId();

        TextView blabla = (TextView) view.findViewById(idview);
        String a = (String) blabla.getText();

        Toast.makeText(this, a, Toast.LENGTH_SHORT).show();
        String b = a.toLowerCase();

        boolean d = b.contains("ɔ");
        boolean e = b.contains("ɛ");

        if (d || e ){
            b= b.replace("ɔ","x");
            b= b.replace("ɛ","q");
        }

        int resourceId = getResources().getIdentifier(b, "raw", "com.learnakantwi.twiguide");


        final MediaPlayer player = MediaPlayer.create(this, resourceId);
        player.start();

        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                player.release();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //MenuInflater menuInflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.main_menu, menu);

        final MenuItem item = menu.findItem(R.id.menusearch);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(DaysOfWeekActivity.this, query, Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<DaysOfWeek> results = new ArrayList<>();
                for (DaysOfWeek x: daysOfWeeksArray ){

                    if(x.getNameEnglish().toLowerCase().contains(newText.toLowerCase()) || x.getNameTwi().toLowerCase().contains(newText.toLowerCase())){
                        results.add(x);
                    }

                    ((DaysOfWeekAdapter)daysOfWeekListView.getAdapter()).update(results);
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
           /* case R.id.settings:
                Log.i("Menu Item Selected", "Settings");
                playAll();
                return true;
            case R.id.alphabets:
                Log.i("Menu Item Selected", "Alphabets");
                return  true;*/

            case R.id.main:
                //Log.i("Menu Item Selected", "Alphabets");
                goToMain();
                return  true;
            default:
                return false;
        }
    }


    public void goToMain(){
        Intent intent = new Intent(getApplicationContext(), Home.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_days_of_week);

        daysOfWeekListView = findViewById(R.id.lvdaysofweek);



        daysOfWeeksArray = new ArrayList<>();

        daysOfWeeksArray.add(new DaysOfWeek("Monday", "Dwoada"));
        daysOfWeeksArray.add(new DaysOfWeek("Tuesday", "Benada"));
        daysOfWeeksArray.add(new DaysOfWeek("Wednesday", "Wukuada"));
        daysOfWeeksArray.add(new DaysOfWeek("Thursday", "Yawada"));
        daysOfWeeksArray.add(new DaysOfWeek("Friday", "Fiada"));
        daysOfWeeksArray.add(new DaysOfWeek("Saturday", "Memeneda"));
        daysOfWeeksArray.add(new DaysOfWeek("Sunday", "Kwasiada"));
        // daysOfWeeksArray.add(new DaysOfWeek("Holiday", "Afofida"));


        myAdapterDaysOfWk = new DaysOfWeekAdapter(this, daysOfWeeksArray);
        daysOfWeekListView.setAdapter(myAdapterDaysOfWk);
    }


}
