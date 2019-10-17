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

public class NumbersAdapter extends BaseAdapter implements Filterable {

    private Context context;
    private int myResource;
    ArrayList<Numbers> originalArray , tempArray;

    public NumbersAdapter(Context context, ArrayList<Numbers> originalArray ) {

        this.context = context;
        this.originalArray = originalArray;
        this.tempArray = originalArray;
    }


    public void update(ArrayList<Numbers> results){
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

        String figures = originalArray.get(position).getFigure();
        String numWord = originalArray.get(position).getNumberWord();

        Numbers numbers = new Numbers(figures, numWord);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView= inflater.inflate(R.layout.custom_time, null);

        TextView tvTwi =convertView.findViewById(R.id.speakTwiTime);
        TextView tvEnglish =convertView.findViewById(R.id.speakEnglishTime);


        tvEnglish.setText(figures);
        tvTwi.setText(numWord);

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return null;
    }
}
