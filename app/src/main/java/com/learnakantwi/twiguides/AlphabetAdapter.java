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

public class AlphabetAdapter extends BaseAdapter implements Filterable {
    Context context;
    ArrayList<Alphabets> originalArray, tempArray;



    public AlphabetAdapter (Context context, ArrayList<Alphabets> originalArray) {

        this.context = context;
        this.originalArray = originalArray;
        this.tempArray= originalArray;
    }

    public void update(ArrayList<Alphabets> results){
        originalArray = new ArrayList<>();
        originalArray.addAll(results);
        notifyDataSetChanged();
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
        String upper = originalArray.get(position).getUpper();
        String lower = originalArray.get(position).getLower();
        String both = originalArray.get(position).getBoth();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView= inflater.inflate(R.layout.custom_alphabet, null);



        /*LayoutInflater  inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.from(parent.getContext()).inflate(R.layout.adapter_view, parent ,false);

        */

        TextView textView1 = (TextView) convertView.findViewById(R.id.Text1);
        TextView textView2 = (TextView) convertView.findViewById(R.id.Text2);
        TextView textView3 = (TextView) convertView.findViewById(R.id.Text3);


        textView1.setText(both);
        textView2.setText(upper);
        textView3.setText(lower);

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return null;
    }

}
