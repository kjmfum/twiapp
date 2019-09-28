package com.learnakantwi.twiguides;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

public class AnimalsAdapter extends BaseAdapter implements Filterable {

    Context context;
    ArrayList<Animals> originalArray , tempArray;
    //TimeAdapter.CustomFilter cf;

    public AnimalsAdapter(Context context, ArrayList<Animals> originalArray) {
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
        convertView= inflater.inflate(R.layout.custom_time, null);

        TextView textView = (TextView) convertView.findViewById(R.id.speakEnglishTime);
        TextView textView1 = convertView.findViewById(R.id.speakTwiTime);

        textView.setText(originalArray.get(position).getEnglishAnimals());
        textView1.setText(originalArray.get(position).getTwiAnimals());
        //if image would have been --- setImageResource.

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return null;
    }

}