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

//import android.support.v7.app.AppCompatActivity;

public class FamilyActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<Family> familyArrayList;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //MenuInflater menuInflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.main_menu, menu);

        final MenuItem item = menu.findItem(R.id.menusearch);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(FamilyActivity.this, query, Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<Family> results = new ArrayList<>();
                for (Family x: familyArrayList ){

                    if(x.getEnglishFamily().toLowerCase().contains(newText.toLowerCase()) || x.getTwiFamily().toLowerCase().contains(newText.toLowerCase())){
                        results.add(x);
                    }

                    ((FamilyAdapter)listView.getAdapter()).update(results);
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

        if (b.contains(" ") || b.contains("/") || b.contains(",") || b.contains("'")) {
            b = b.replace(" ", "");
            b = b.replace("/", "");
            b= b.replace(",","");
            b= b.replace("'","");
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family);

        listView = findViewById(R.id.familyListView);

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
        familyArrayList.add(new Family("Children (1)", "mma"));
        familyArrayList.add(new Family("Children (2)", "mmofra"));
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

        familyArrayList.add(new Family("Niece", "Wɔfaase"));
        familyArrayList.add(new Family("Nephew", "Wɔfaase"));

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












        FamilyAdapter familyAdapter = new FamilyAdapter(this, familyArrayList);
        listView.setAdapter(familyAdapter);

    }
}
