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

public class AllAdapter extends BaseAdapter implements Filterable {

    Context context;
    ArrayList<All> originalArray , tempArray;
    //TimeAdapter.CustomFilter cf;

    public AllAdapter(Context context, ArrayList<All> originalArray) {
        this.context = context;
        this.originalArray = originalArray;
        this.tempArray = originalArray;
    }


    public void update(ArrayList<All> results){
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
        convertView= inflater.inflate(R.layout.custom_all, null);

        TextView twiMain = (TextView) convertView.findViewById(R.id.TwiMain);
        TextView twi1 = convertView.findViewById(R.id.twi1);
        TextView twi2 = convertView.findViewById(R.id.twi2);

        TextView englishMain = (TextView) convertView.findViewById(R.id.englishMain);
        TextView eng1 = (TextView) convertView.findViewById(R.id.eng1);
        TextView eng2 = (TextView) convertView.findViewById(R.id.eng2);

        twi1.setText(originalArray.get(position).getTwi1());
        twi2.setText(originalArray.get(position).getTwi2());
        twiMain.setText(originalArray.get(position).getTwiMain());

        englishMain.setText(originalArray.get(position).getEnglishmain());
        eng1.setText(originalArray.get(position).getEnglish1());
        eng2.setText(originalArray.get(position).getEnglish2());
        //if image would have been --- setImageResource.

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return null;
    }

}