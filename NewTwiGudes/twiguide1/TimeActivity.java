package com.learnakantwi.twiguides;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class TimeActivity extends AppCompatActivity {

    ArrayList<Time> timeArrayList;
    ListView timeListView;
    TextView textView;
    TimeAdapter timeAdapter;

    public void log2(View view) {
        int idview = view.getId();

        TextView blabla = (TextView) view.findViewById(idview);
        String a = (String) blabla.getText();

        Toast.makeText(this, a, Toast.LENGTH_SHORT).show();
    }

    public void timeClick(View view){

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


        if (b.contains(" ") || b.contains("/") || b.contains(",") || b.contains("(") || b.contains(")") || b.contains("-") | b.contains("?")) {
            b = b.replace(" ", "");
            b = b.replace("/", "");
            b= b.replace(",","");
            b= b.replace("(","");
            b= b.replace(")","");
            b= b.replace("-","");
            b= b.replace("?","");
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

    /*
        public void timeClick (View view){


            // player.stop();

            int idview= view.getId();
            String ourId = view.getResources().getResourceEntryName(idview);


            TextView blabla = (TextView) view.findViewById(idview);


            String a = (String) blabla.getText();
            String b = a.toLowerCase();


            boolean d = b.contains("ɔ");
            boolean e = b.contains("ɛ");

            if (b.equals("enum")){
                b="qnum";
            }

            if (d || e){
                b= b.replace("ɔ","x");
                b= b.replace("ɛ","q");
            }
            if (b.contains(" ") || b.contains("/") || b.contains(",")) {
                b = b.replace(" ", "");
                b = b.replace("/", "");
                b= b.replace(",","");
            }

            if (b.matches("[0-9]+")){
                b=b;
            }
            else {

                int resourceId = getResources().getIdentifier(b, "raw", "com.learnakantwi.twiguide");

                //String OurId = view.getResources().getResourceEntryName(id);

                final MediaPlayer player = MediaPlayer.create(this, resourceId);
                player.start();

                player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        player.release();
                    }
                });
            }


            Toast.makeText(this, a, Toast.LENGTH_SHORT).show();

        }
    */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //MenuInflater menuInflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.main_menu, menu);

        final MenuItem item = menu.findItem(R.id.menusearch);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(TimeActivity.this, query, Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<Time> results = new ArrayList<>();
                for (Time x: timeArrayList ){

                    if(x.getEnglishTime().toLowerCase().contains(newText.toLowerCase()) || x.getTwiTime().toLowerCase().contains(newText.toLowerCase())){
                        results.add(x);
                    }

                    ((TimeAdapter)timeListView.getAdapter()).update(results);
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
        setContentView(R.layout.activity_time);

        timeListView = findViewById(R.id.timeListView);
        textView = findViewById(R.id.speakEnglishTime);

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


        timeAdapter = new TimeAdapter(this,timeArrayList);

        timeListView.setAdapter(timeAdapter);





    }
}
