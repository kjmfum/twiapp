package com.learnakantwi.twiguides;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;

import java.util.ArrayList;

public class ProverbsAdapter extends BaseAdapter implements Filterable {

    Context context;
    ArrayList<Proverbs> originalArray , tempArray;


    public ProverbsAdapter(Context context, ArrayList<Proverbs> originalArray) {
        this.context = context;
        this.originalArray = originalArray;
        this.tempArray = originalArray;
    }


    public void update(ArrayList<Proverbs> results){
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

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView= inflater.inflate(R.layout.custom_proverbs, null);

        TextView proverbsTwiTextView = convertView.findViewById(R.id.proverbTwitext);
        TextView proverbsLiteralEnglishView = convertView.findViewById(R.id.proverbLiteralEnglish);
        TextView proverbsEnglishMeaningView = convertView.findViewById(R.id.proverbMeaningEnglish);


        proverbsTwiTextView.setText(originalArray.get(position).getTwiProverb());
        proverbsLiteralEnglishView.setText(originalArray.get(position).getProverbLiteral());

        proverbsEnglishMeaningView.setText(originalArray.get(position).getProverbMeaning());
        //if image would have been --- setImageResource.

        return convertView;
    }

    public String playSound(int position){
       return originalArray.get(position).getTwiProverb();
    }

    @Override
    public Filter getFilter() {
        return null;
    }

}