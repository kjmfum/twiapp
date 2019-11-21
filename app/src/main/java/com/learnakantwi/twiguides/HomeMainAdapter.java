package com.learnakantwi.twiguides;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class HomeMainAdapter extends BaseAdapter implements Filterable {

    Context context;
    ArrayList<HomeMainButton> originalArray , tempArray;
    //TimeAdapter.CustomFilter cf;

    public HomeMainAdapter(Context context, ArrayList<HomeMainButton> originalArray) {
        this.context = context;
        this.originalArray = originalArray;
        this.tempArray = originalArray;
    }


    public void update(ArrayList<HomeMainButton> results){
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
        convertView= inflater.inflate(R.layout.custom_home, null);

        TextView textView =  convertView.findViewById(R.id.homeButtonText);
        //TextView textView1 = convertView.findViewById(R.id.speakTwiTime);
        ImageView imageView = convertView.findViewById(R.id.homeButtonimage);


        textView.setText(originalArray.get(position).getNameofActivity());
        //textView1.setText(originalArray.get(position).getTwiAnimals());

        imageView.setImageResource(originalArray.get(position).getImageOfActivity());
        //if image would have been --- setImageResource.

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return null;
    }

}