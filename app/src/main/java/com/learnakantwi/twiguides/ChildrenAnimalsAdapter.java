package com.learnakantwi.twiguides;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ChildrenAnimalsAdapter extends BaseAdapter implements Filterable {

    Context context;
    ArrayList<Animals> originalArray , tempArray;
    //TimeAdapter.CustomFilter cf;

    public ChildrenAnimalsAdapter(Context context, ArrayList<Animals> originalArray) {
        this.context = context;
        this.originalArray = originalArray;
        this.tempArray = originalArray;
    }


    public void update(ArrayList<Animals> results){
        originalArray = new ArrayList<>();
        originalArray.addAll(results);
        notifyDataSetChanged();
    }

    public int position(){
        return position();
    }

    @Override
    public int getCount() {
        return originalArray.size();
    }

    @Override
    public Object getItem(int position) {
        return originalArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView= inflater.inflate(R.layout.custom_children_images, null);



        final TextView tvEnglish = convertView.findViewById(R.id.speakEnglishTime);
        final TextView tvTwi = convertView.findViewById(R.id.speakTwiTime);

        tvEnglish.setText(originalArray.get(position).getEnglishAnimals());
        tvTwi.setText(originalArray.get(position).getTwiAnimals());
        //if image would have been --- setImageResource.

      /*  tvTwi.setOnClickListener(new
                                         View.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {
                                                 String me1 =  tvTwi.getText().toString();
                                                 String you = tvEnglish.getText().toString();
                                                 tvTwi.setTextColor(Color.BLUE);

                                                 //PlayFromFirebase playFromFirebase = new PlayFromFirebase();

                                                 //String me1 = playFromFirebase.viewTextConvert(me);

                                                 Toast.makeText(context, "Yes: " + me1 + " -"+ you, Toast.LENGTH_SHORT).show();
                                             }
                                         });
*/
        return convertView;


    }

    @Override
    public Filter getFilter() {
        return null;
    }


}